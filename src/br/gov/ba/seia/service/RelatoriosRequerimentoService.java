package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.CumprimentoReposicaoFlorestal;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LacErb;
import br.gov.ba.seia.entity.LacPostoCombustivel;
import br.gov.ba.seia.entity.LacTransporte;
import br.gov.ba.seia.entity.LacTransporteProduto;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.facade.AtoDeclaratorioFacade;
import br.gov.ba.seia.facade.LacErbServiceFacade;
import br.gov.ba.seia.facade.LacPostoServiceFacade;
import br.gov.ba.seia.facade.LacServiceFacade;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.ReportUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RelatoriosRequerimentoService {
	
	@EJB
	private CertificadoService certificadoService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private LicencaService licencaService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private LacTransporteService lacTransporteService;
	@EJB
	private LacServiceFacade lacServiceFacade;
	@EJB
	private LacErbServiceFacade lacErbServiceFacade;
	@EJB
	private LacPostoServiceFacade lacPostoServiceFacade;
	@EJB
	private AtoDeclaratorioFacade atoDeclaratorioFacade;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private CumprimentoReposicaoFlorestalService cumprimentoReposicaoFlorestalService;
	
	@Inject
	private CertificadoUtil certificadoUtil;
	
	public StreamedContent gerarDocumentoCRF(Requerimento requerimento) throws Exception {
		gerarCertificadoCRF(requerimento);
		CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal = cumprimentoReposicaoFlorestalService.obterCumprimentoReposicaoFlorestalPorRequerimento(requerimento);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("IDE_REQUERIMENTO", requerimento.getIdeRequerimento());
		params.put("valor_por_extenso", Util.valorEmReaisParaExtenso(cumprimentoReposicaoFlorestal.getVlrPecuniario()));
		RelatorioUtil lRelatorio = new RelatorioUtil("reposicaoFlorestal/certificado_crf.jasper", params);
		DefaultStreamedContent relatorio = (DefaultStreamedContent) lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		relatorio.setContentType("application/pdf");
		return relatorio;
		
	}
	
	public StreamedContent gerarDocumentoDLA(Integer ideRequerimento) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide_requerimento", ideRequerimento);
		RelatorioUtil lRelatorio = new RelatorioUtil("dla.jasper", params);
		DefaultStreamedContent relatorio = (DefaultStreamedContent) lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		relatorio.setContentType("application/pdf");
		return relatorio;
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public StreamedContent imprimirRelatorioCorteFloresta(Integer ideRequerimento) throws Exception {
		gerarCertificados(ideRequerimento);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide_requerimento", ideRequerimento);
		return new RelatorioUtil("relatorio_rcfp.jasper", params).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public StreamedContent imprimirDQC(Integer ideRequerimento) throws Exception {
		gerarCertificados(ideRequerimento);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide_requerimento", ideRequerimento);
		RelatorioUtil lRelatorio = new RelatorioUtil("documentoDeclaracao.jasper", params);
		return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public StreamedContent imprimirRegistroCorteFlorestaProducaoPlantada(Integer ideRequerimento) throws Exception {
		gerarCertificados(ideRequerimento);
		return new ImpressoraAtoDeclaratorio().imprimirCertificado(ideRequerimento, DocumentoObrigatorioEnum.FORMULARIO_RFP);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public StreamedContent imprimirCertificadoRLAC(Integer ideRequerimento) throws Exception {
		gerarCertificados(ideRequerimento);
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_requerimento", ideRequerimento);
		lParametros.put("classes", tratarClasseRiscoTransporte(new Requerimento(ideRequerimento)));
		return new RelatorioUtil("certificado_rlac.jasper", lParametros, false).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public StreamedContent imprimirCertificadoLacPosto(Integer ideRequerimento) throws Exception {
		
		gerarCertificados(ideRequerimento);
		
		if(!lacServiceFacade.hasToken(ideRequerimento)){
			Certificado certificado = lacServiceFacade.carregarCertificado(ideRequerimento);
			lacServiceFacade.atualizarTokenCertificadoLac(certificado);
		}
		
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_requerimento", ideRequerimento);
		return new RelatorioUtil("certificado_lac_tcra.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public StreamedContent imprimirCertificadoAPE(Integer ideRequerimento) throws Exception {
		
		gerarCertificados(ideRequerimento);
		
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_requerimento", ideRequerimento);
		return new RelatorioUtil("certificado_ape.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public StreamedContent imprimirCertificadoLacTransporte(Integer ideRequerimento) throws Exception {
		
		gerarCertificados(ideRequerimento);
		
		if(!lacServiceFacade.hasToken(ideRequerimento)){
			Certificado certificado = lacServiceFacade.carregarCertificado(ideRequerimento);
			lacServiceFacade.atualizarTokenCertificadoLac(certificado);
		}
		
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_req", ideRequerimento);
		lParametros.put("classes", tratarClasseRiscoTransporte(new Requerimento(ideRequerimento)));
		return new RelatorioUtil("certificado_lac_transporte_2.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public StreamedContent imprimirCertificadoTcraLac(Integer ideRequerimento) throws Exception {
		
		gerarCertificados(ideRequerimento);
		
		if(!lacServiceFacade.hasToken(ideRequerimento)){
			Certificado certificado = lacServiceFacade.carregarCertificado(ideRequerimento);
			lacServiceFacade.atualizarTokenCertificadoLac(certificado);
		}
		
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_requerimento", ideRequerimento);
		return new RelatorioUtil("certificado_lac_tcra.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public StreamedContent imprimirCertificadoLacErb(Integer ideRequerimento) throws Exception {
		
		gerarCertificados(ideRequerimento);
		
		if(!lacServiceFacade.hasToken(ideRequerimento)){
			Certificado certificado = lacServiceFacade.carregarCertificado(ideRequerimento);
			lacServiceFacade.atualizarTokenCertificadoLac(certificado);
		}
		
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_requerimento", ideRequerimento);
		return new RelatorioUtil("certificado_lac_tcra.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	

	private String tratarClasseRiscoTransporte(Requerimento requerimento) {
		List<LacTransporteProduto> listaTransporteProduto = new ArrayList<LacTransporteProduto>();
		String str = "";
		Exception erro = null;

		try {
			Licenca licenca = licencaService.getLicencaByIdeRequerimentoSimples(requerimento);

			if(!Util.isNull(licenca) && !Util.isNullOuVazio(licenca.getNumProcessoLicenca())) {

				Processo processo = processoService.buscarProcessoPorCriteria(licenca.getNumProcessoLicenca());

				if(!Util.isNullOuVazio(processo) && !Util.isNull(processo.getIdeRequerimento())) {

					listaTransporteProduto = lacTransporteService.buscarClasseRisco(processo.getIdeRequerimento());
				}
			}

			//Divide as classes no formato padrão de texto.
			for (int x = 0; x < listaTransporteProduto.size(); x++) {
				if(listaTransporteProduto.size() - x == 2 && !str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1)+" e ";
				}else if(listaTransporteProduto.size() - x == 1 && !str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1);
				}else if(!str.contains(listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1))){
					str +=listaTransporteProduto.get(x).getIdeProduto().getDscClasseRisco().substring(0, 1)+", ";
				}
			}

			//Caso haja apenas uma classe de risco remove a virgula do fim
			if(str.endsWith(", ")){
				str = str.substring(0, str.length()-2);
			}

			//Caso a String esteja no formato 1, 2 ou 1,2, 3 e assim sucessivamente adiciona o "e" ficando 1 e 2 ou 1, 2 e 3
			if(!str.contains("e") && str.length() > 4){
				str = str.substring(0, str.length()-3) + str.substring(str.length()-3).replaceAll(",", " e");
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		} finally {
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

		return str;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void gerarCertificados(Integer ideRequerimento) throws Exception {
		Requerimento requerimento = new Requerimento(ideRequerimento);
		List<Certificado> certificados = certificadoService.carregarByIdRequerimento(requerimento.getIdeRequerimento());
		requerimento.setAtosAmbientais(atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(requerimento.getIdeRequerimento()));
		for (AtoAmbiental atoAmbiental : requerimento.getAtosAmbientais()) {
			Certificado certificado = certificadoUtil.gerarCertificado(atoAmbiental, requerimento);
			if (!certificados.contains(certificado)) {
				String numeroCertificado = this.certificadoService.gerarNumeroCertificado(certificado);
				certificado.setNumCertificado(numeroCertificado);
				certificadoService.salvar(certificado);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarCertificadoCRF(Requerimento requerimento) {
		List<Certificado> certificados = certificadoService.carregarByIdRequerimento(requerimento.getIdeRequerimento());
		requerimento.setAtosAmbientais(atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(requerimento.getIdeRequerimento()));

		Certificado certificado = CertificadoUtil.gerarCertificadoByTipo(requerimento, TipoCertificadoEnum.CRF);
		if (!certificados.contains(certificado)) {
			String numeroCertificado = this.certificadoService.gerarNumeroCertificadoByTipo(certificado);
			certificado.setNumCertificado(numeroCertificado);
			certificadoService.salvar(certificado);
		}
	}
	
	public StreamedContent imprimirRelatorioLacErb(Integer ideRequerimento) throws Exception {
		LacErb lac = lacErbServiceFacade.carregarLacErbByIdeRequerimento(ideRequerimento);
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_lac", lac.getIdeLac());
		lParametros.put("legislacao", lac.getCondicionantesFormularioLAC());
		
		return new RelatorioUtil("lac_erb.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public StreamedContent imprimirRelatorioLacPosto(Integer ideRequerimento) throws Exception {
		LacPostoCombustivel lac = lacPostoServiceFacade.carregarLacPostoByIdeRequerimento(ideRequerimento);
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_lac", lac.getIdeLac());
		lParametros.put("legislacao", lac.getCondicionantesFormularioLAC());
		return new RelatorioUtil("lac_posto.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public StreamedContent imprimirRelatorioLacTransportadora(Integer ideRequerimento) throws Exception {
		LacTransporte lac = lacTransporteService.buscarLacTransporteByIdeRequerimento(new Requerimento(ideRequerimento));
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_lac", lac.getIdeLac().getIdeLac());
		return new RelatorioUtil("lac_transportadora.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public StreamedContent imprimirRelatorioDTRP(Integer ideRequerimento) throws Exception {
		gerarCertificados(ideRequerimento);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide_requerimento", ideRequerimento);
		RelatorioUtil lRelatorio = new RelatorioUtil("relatorio_dtrp.jasper", params);
		return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public StreamedContent gerarResumoRequerimento(Integer ideRequerimento) throws Exception {

		List<Imovel> listaImovel = null;
		Map<String, Object> params = new HashMap<String, Object>();

		Empreendimento empreendimento = empreendimentoService.buscarEmpreendimentoPorRequerimento(ideRequerimento);
		
		if(!Util.isNull(empreendimento) && !Util.isNull(empreendimento.getIdeEmpreendimento())){
			listaImovel = (List<Imovel>) empreendimentoService.obterTipoImovelPorIdeEmpreendimento(empreendimento.getIdeEmpreendimento()).getImovelCollection();
			params.put("nom_tipo_imovel", listaImovel.isEmpty() ? "Não Identificado" : listaImovel.get(0).getIdeTipoImovel().getNomTipoImovel());
		}

		params.put("ide_requerimento", ideRequerimento);

		if(requerimentoService.isRequerimentoAntigo(ideRequerimento)){
			RelatorioUtil lRelatorio = new RelatorioUtil("resumo_requerimento.jasper", params);
			return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		}
		else{
			return  new ReportUtil().imprimir("resumo_requerimento.jasper", params);
		}
	}

}