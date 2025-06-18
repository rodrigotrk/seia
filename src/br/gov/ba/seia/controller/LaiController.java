package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.LacTransporteProduto;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.facade.AtoDeclaratorioFacade;
import br.gov.ba.seia.facade.DeclaracaoIntervencaoAppFacade;
import br.gov.ba.seia.facade.FormarEquipeServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorioDIAP;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.LacService;
import br.gov.ba.seia.service.LacTransporteService;
import br.gov.ba.seia.service.LicencaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RelatoriosRequerimentoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.validators.CnpjValidator;
import br.gov.ba.seia.validators.CpfValidator;

@Named("laiController")
@ViewScoped
public class LaiController {

	@EJB
	private ProcessoService processoService;

	@EJB
	private MunicipioService municipioService;
	
	@EJB
	private ParametroService parametroService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private RelatoriosRequerimentoService relatorioRequerimentoServiceFacade;

	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;
	
	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private CertificadoService certificadoService;
	
	@EJB
	private LacTransporteService lacTransporteService;
	
	@EJB
	private LicencaService licencaService;
	
	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private LacService lacService;
	
	@EJB
	private AtoDeclaratorioFacade atoDeclaratorioFacade;
	
	@EJB
	private DeclaracaoIntervencaoAppFacade diapFacade;
	
	@EJB
	private ProcessoAtoService processoAtoService;
	
	@EJB
	private FormarEquipeServiceFacade formarEquipeServiceFacade;
	
	@Inject
	private CertificadoUtil certificadoUtil;
	
	private LazyDataModel<Processo> dataModelProcessos;

	private VwConsultaProcesso vwProcesso;
	
	private Processo processo;

	private String nome;
	private String numeroProcesso;
	private String numDocumento;
	private String desTipologiaAto;
	private String empreendimento;

	private Boolean showProcessos;

	private Municipio  municipioSelecionado;
	private List<Municipio> municipios;
		
	public LaiController() {

	}

	@PostConstruct
	public void init() {
		this.showProcessos = false;
	}
	
	public void consultar() throws Exception {

		if (!validar()) {
			return;
		}

		carregarPaginacao();

	}

	private boolean validar() {

		boolean valido = true;

		if (!isFiltroValido()) {
			JsfUtil.addErrorMessage("Algum filtro deve ser preenchido.");
			showProcessos = false;
			valido = false;
		}

		if (!Util.isNullOuVazio(numDocumento)) {
			boolean documentoValido = CpfValidator.validaCPF(numDocumento) || CnpjValidator.validarCNPJ(numDocumento);
			if (!documentoValido) {
				JsfUtil.addErrorMessage("O campo CPF/CNPJ está inválido.");
				showProcessos = false;
				valido = false;
			}
		}

		return valido;

	}

	private void carregarPaginacao() {
		dataModelProcessos = new LazyDataModel<Processo>() {

			private static final long serialVersionUID = -9150943634752597868L;

			@Override
			public List<Processo> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<Processo> processos = null;
				Exception erro = null;
				try {
					setPageSize(pageSize);

					if (isFiltroValido()) {
						processos = listarProcessoComAto(first, pageSize);
					} else {
						processos = new ArrayList<Processo>();
					}

				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage(e.getMessage());
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
				
				return processos;
			}

		};
		dataModelProcessos.setRowCount(getRowCountProcesso());
	}

	private List<Processo> listarProcessoComAto(int first, int pageSize) throws Exception {
		List<Processo> processos;
		
		processos = processoService.filtrarListaProcessosLai(first, pageSize, numeroProcesso, numDocumento, nome, municipioSelecionado, empreendimento);
		for (Processo processo : processos) {
			processo.setAtosAmbientais(processoService.obterListaAtosAmbientais(processo.getIdeProcesso()));
		}
		return processos;
	}
	
	public void visualizarProcesso(ActionEvent event){
		
		Processo processo = (Processo) event.getComponent().getAttributes().get("ideProcesso");
		
		Exception erro = null;
		try {
			this.vwProcesso = this.vwConsultaProcessoService.getProcessoCompletoLAI(processo.getIdeProcesso());
			DetalharProcessoController detalharProcessoController = (DetalharProcessoController) SessaoUtil.recuperarManagedBean("#{detalharProcessoController}", DetalharProcessoController.class);
			
			
			this.vwProcesso.setListProcessoAto(detalharProcessoController.carregaProcessoAto(vwProcesso.getListProcessoAto()));
			this.vwProcesso.setListProcessoAtoReequadramento(detalharProcessoController.carregaProcessoAto(vwProcesso.getListProcessoAtoReequadramento()));
			
			Collection<ProcessoAto> listProcessoAto = formarEquipeServiceFacade.ajustarProcessoAto(vwProcesso.getListProcessoAto());
			this.vwProcesso.setListProcessoAto(listProcessoAto);
			
			detalharProcessoController.setVwProcesso(vwProcesso);
			
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	private boolean isFiltroValido() {
		return !Util.isNullOuVazio(numeroProcesso) || !Util.isNullOuVazio(numDocumento) || !Util.isNullOuVazio(nome) || !Util.isNullOuVazio(municipioSelecionado) || !Util.isNullOuVazio(empreendimento);
	}
	
	public void limparTela() {
		this.nome = null;
		this.municipioSelecionado = null;
		this.empreendimento = null;
		this.numDocumento = null;
		this.numeroProcesso = null;
		dataModelProcessos = null;
		showProcessos = false;
		
	}

	private int getRowCountProcesso() {
		int totalRowCount = 0;
		
		try {
			
			if (isFiltroValido()) {
			
				totalRowCount = processoService.countLai(numeroProcesso, numDocumento, nome, municipioSelecionado, empreendimento);
			}
			if (totalRowCount != 0) {
				this.showProcessos = true;
			} else {
				this.showProcessos = false;
				JsfUtil.addSuccessMessage("Não Foram encontrados Registros.");
			}
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return totalRowCount;
	}

	public Collection<AtoAmbiental> obterAtosProcesso(Processo processo) {
		Exception erro = null;
		try {
			return this.processoService.obterListaAtosAmbientais(processo.getIdeProcesso());
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return null;
	}

	public StreamedContent imprimirCertificadoLac(Processo processo) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		Requerimento requerimento = gerarCertificado(processo);		
		
		if(lacService.isLacErb(requerimento.getIdeRequerimento()) || lacService.isLacPosto(requerimento.getIdeRequerimento())){
			lParametros.put("ide_requerimento", requerimento.getIdeRequerimento());
			return new RelatorioUtil("certificado_lac_tcra.jasper", lParametros, true).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);			
		}
		
		else if(lacService.isLacTransporte(requerimento.getIdeRequerimento())){
			lParametros.put("ide_req", requerimento.getIdeRequerimento());
			return new RelatorioUtil("certificado_lac_transporte_2.jasper", lParametros, true).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);			
		}
		else {
			System.out.print("Não foi Possivel Gerar o Certificado Lac");
			return null;
		}
		
	}

	public StreamedContent getImprimirCertificadoDiap(Processo processo) throws Exception {
		Requerimento requerimento = gerarCertificado(processo);
		return new ImpressoraAtoDeclaratorioDIAP(diapFacade.montarTextoRepresentadoPor(requerimento)).imprimirCertificado(requerimento.getIdeRequerimento(), DocumentoObrigatorioEnum.FORMULARIO_DIAP);
	}
	
	public StreamedContent gerarRfp(Processo processo) throws Exception {
		Requerimento requerimento = gerarCertificado(processo);
		return new ImpressoraAtoDeclaratorio().imprimirCertificado(requerimento.getIdeRequerimento(), DocumentoObrigatorioEnum.FORMULARIO_RFP);
	}

	public StreamedContent gerarRcfp(Processo processo) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		
		Requerimento requerimento = gerarCertificado(processo);
		
		lParametros.put("ide_requerimento", requerimento.getIdeRequerimento());
		return new RelatorioUtil("relatorio_rcfp.jasper", lParametros, true).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	public StreamedContent gerarDocumentoDeclaracaoPdf(Processo processo) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			Requerimento requerimento = gerarCertificado(processo);			
			params.put("ide_requerimento", requerimento.getIdeRequerimento());
			RelatorioUtil lRelatorio = new RelatorioUtil("documentoDeclaracao.jasper", params, true);
			return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	public StreamedContent getGerarDetalhesNotificacaoPdf(Notificacao notificacaoSelecionada) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_notificacao", notificacaoSelecionada.getIdeNotificacao());
			
			if(notificacaoSelecionada.getTipo()==1){
				RelatorioUtil lRelatorio = new RelatorioUtil("detalhesNotificacao.jasper", params);
				return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);				
			}
			else if(notificacaoSelecionada.getTipo()==2){
				RelatorioUtil lRelatorio = new RelatorioUtil("detalhesNotificacaoComunicacao.jasper", params);
				return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);	
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}

		return null;
	}
	
	public boolean isAtoDeclaratorio(Processo processo) {
		for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
			if (!atoAmbiental.getIndDeclaratorio()) {
				return false;
			}
		}
		return true && !isLac(processo) && !isRcfp(processo) && !isRfp(processo) && !isRLAC(processo) && !isAtoDiap(processo);
	}

	public boolean isLac(Processo processo) {
		for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
			if (atoAmbiental.isLac()) {
				return true;
			}
		}
		return false;
	}

	public boolean isAtoDiap(Processo processo) {
		for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
			if (atoAmbiental.isDiap()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isRfp(Processo processo) {
		for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
			if (atoAmbiental.isRfp()) {
				return true;
			}
		}
		return false;
	}

	public boolean isRcfp(Processo processo) {
		for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
			if (atoAmbiental.isRcfp()) {
				return true;
			}
		}
		
		return false;
	}
	
	public StreamedContent getDocumentoObrigatorioSC(String dscCaminho) {
		try {
			return gerenciaArquivoService.getContentFile(dscCaminho);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
		}
		return null;
	}
	
	public StreamedContent getArquivoProcessoSC(ArquivoProcesso arquivoProcesso) {
		try {			
			return gerenciaArquivoService.getContentFile(arquivoProcesso.getDscCaminhoArquivo());
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage("Arquivo não encontrado!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return null;
	}

	public boolean isDocLac(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento){
		return this.isDocLacErb(documentoObrigatorioRequerimento) || this.isDocLacPosto(documentoObrigatorioRequerimento);
	}
	
	public boolean isDocLacErb(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
	
		Boolean isValido = Boolean.FALSE;
		
		try {
			
			if (!Util.isNull(documentoObrigatorioRequerimento) && !Util.isNull(documentoObrigatorioRequerimento.getIdeDocumentoAto())
					&& !Util.isNull(documentoObrigatorioRequerimento.getIdeDocumentoAto().getIdeDocumentoObrigatorio())) {
				
				DocumentoObrigatorio documentoObrigatorio = documentoObrigatorioRequerimento.getIdeDocumentoAto().getIdeDocumentoObrigatorio();
				
				if (!Util.isNullOuVazio(documentoObrigatorio)
						&& !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())
						&& Util.isNullOuVazio(documentoObrigatorio.getDscCaminhoArquivo())
						&& documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_ERB).getDscValor())) {
					
					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return isValido;
	}

	public boolean isDocLacPosto(DocumentoObrigatorioRequerimento documentoObrigatorioRequerimento) {
	
		Boolean isValido = Boolean.FALSE;
		
		try {
			
			if (!Util.isNull(documentoObrigatorioRequerimento) && !Util.isNull(documentoObrigatorioRequerimento.getIdeDocumentoAto())
					&& !Util.isNull(documentoObrigatorioRequerimento.getIdeDocumentoAto().getIdeDocumentoObrigatorio())) {
				
				DocumentoObrigatorio documentoObrigatorio = documentoObrigatorioRequerimento.getIdeDocumentoAto().getIdeDocumentoObrigatorio();
				
				if (!Util.isNullOuVazio(documentoObrigatorio)
						&& !Util.isNullOuVazio(documentoObrigatorio.getIdeDocumentoObrigatorio())
						&& Util.isNullOuVazio(documentoObrigatorio.getDscCaminhoArquivo())
						&& documentoObrigatorio.getIdeDocumentoObrigatorio().intValue() == Integer.parseInt(this.parametroService.obterPorEnum(ParametroEnum.IDE_LAC_POSTO).getDscValor())) {
					
					isValido = Boolean.TRUE;
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
		return isValido;
	}
	
	public String getDesTipologiaAto() {
		return desTipologiaAto;
	}
	
	public String setDesTipologiaAto(Integer ideAtoAmbiental, Integer ideProcesso) {
		ProcessoAto processoAto = new ProcessoAto(ideProcesso, ideAtoAmbiental);
		try{
			return desTipologiaAto=tipologiaService.buscarTipologiaDoAtoAmbiental(processoAto).getDesTipologia();
		}
		catch(NoResultException n){			
			return desTipologiaAto=null;
		}
		catch (Exception e) {
			return desTipologiaAto=null;
		}
	}
	
	public boolean isRLAC(Processo processo) {
		
		try {
			if (!Util.isNull(processo) && !Util.isNull(processo.getIdeRequerimento()) && !Util.isNull(processo.getIdeRequerimento().getIdeRequerimento())) {
				for (AtoAmbiental ato: processo.getAtosAmbientais()) {
					if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.RLAC.getId())) {
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent imprimirCertificadoRLAC(Processo processo) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		
		Requerimento requerimento = gerarCertificado(processo);
		
		lParametros.put("ide_requerimento", requerimento.getIdeRequerimento());
		lParametros.put("classes", tratarClasseRiscoTransporte(processo.getIdeRequerimento()));
		return new RelatorioUtil("certificado_rlac.jasper", lParametros, true).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	private Requerimento gerarCertificado(Processo processo) throws Exception {
		Requerimento requerimento = processo.getIdeRequerimento();
		List<Certificado> certificados = this.certificadoService.carregarByIdRequerimento(requerimento.getIdeRequerimento());
		for (AtoAmbiental atoAmbiental : processo.getAtosAmbientais()) {
			Certificado certificado = certificadoUtil.gerarCertificado(atoAmbiental, requerimento);
			if (!certificados.contains(certificado)) {
				String numeroCertificado = this.certificadoService.gerarNumeroCertificado(certificado);
				certificado.setNumCertificado(numeroCertificado);
				this.certificadoService.salvar(certificado);
			}
		}
		return requerimento;
	}
	
	public boolean isAPE(Processo processo) {
			
			try {
				if (!Util.isNull(processo) && !Util.isNull(processo.getIdeRequerimento()) && !Util.isNull(processo.getIdeRequerimento().getIdeRequerimento())) {
					for (AtoAmbiental ato: processo.getAtosAmbientais()) {
						if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.APE.getId())) {
							
							VwConsultaProcesso consultaProcesso = this.vwConsultaProcessoService.buscarVwConsultaProcessoPorIdeProcesso(processo.getIdeProcesso(), true);	
							
							if(consultaProcesso.getStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.CONCLUIDO.getStatus())) {
								if(processoAtoService.listarAtosPorProcessoComOuSemStatus(consultaProcesso.getIdeProcesso(), true, StatusProcessoAtoEnum.EMITIDO).size() > 0)return true;
								
							}
						}
					}
				}
				return false;
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
	 }
	
	public StreamedContent imprimirCertificadoAPE(Processo processo) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		
		Requerimento requerimento = gerarCertificado(processo);
		
		lParametros.put("ide_requerimento", requerimento.getIdeRequerimento());
		lParametros.put("classes", tratarClasseRiscoTransporte(processo.getIdeRequerimento()));
		return new RelatorioUtil("certificado_ape.jasper", lParametros, true).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
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
	
	public LazyDataModel<Processo> getDataModelProcessos() {
		return dataModelProcessos;
	}

	public void setDataModelProcessos(LazyDataModel<Processo> dataModelProcessos) {
		this.dataModelProcessos = dataModelProcessos;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}
	
	
	public void setEmpreendimento(String empreendimento) {
		this.empreendimento = empreendimento;
	}
	
	public String getEmpreendimento() {
		return empreendimento;
	}


	public Boolean getShowProcessos() {
		return showProcessos;
	}

	public void setShowProcessos(Boolean showProcessos) {
		this.showProcessos = showProcessos;
	}

	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public Processo getProcesso() {
		return processo;
	}

	public void setProcesso(Processo processo) {
		this.processo = processo;
	}

	public List<Municipio> getMunicipios() {
		
		if(Util.isNullOuVazio(municipios)){
			
			try {
				municipios = municipioService.filtrarListaMunicipiosDaBahia();

			} catch (Exception e) {
				JsfUtil.addErrorMessage("Erro: Não foi possível listar os municípios.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		
		return municipios;
	}

	public void setMunicipios(List<Municipio> municipios) {
		this.municipios = municipios;
	}

	public Municipio getMunicipioSelecionado() {
		return municipioSelecionado;
	}

	public void setMunicipioSelecionado(Municipio municipioSelecionado) {
		this.municipioSelecionado = municipioSelecionado;
	}

	public String getNumeroProcesso() {
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}
	
	
}
