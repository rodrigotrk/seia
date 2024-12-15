package br.gov.ba.seia.facade;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.CertificadoDTO;
import br.gov.ba.seia.entity.AssentadoIncraImovelRural;
import br.gov.ba.seia.entity.AtividadeInexigivel;
import br.gov.ba.seia.entity.AtividadeInexigivelModeloCertificadoInexigibilidade;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoGeral;
import br.gov.ba.seia.entity.DeclaracaoParcialDae;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.enumerator.TipoArlEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.enumerator.TipoLocalAtividadeInexigivelEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorioDIAP;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorioIF;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeInfoGeralService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeService;
import br.gov.ba.seia.service.DeclaracaoParcialDaeService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.ModeloCertificadoInexigibilidadeService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.ReservaLegalService;
import br.gov.ba.seia.service.TipologiaGrupoService;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Uri;
import br.gov.ba.seia.util.Util;

/**
 * Facade da validação de {@link Certificado}
 * 
 * @author eduardo.fernandes 
 * @since 25/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8447">#8447</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ValidacaoCertificadoFacade {
	
	@EJB
	private CertificadoService certificadoService;
	
	@EJB
	private DeclaracaoParcialDaeService declaracaoParcialDaeService;
	
	@EJB
	private TipologiaGrupoService tipologiaGrupoService;

	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private ImovelRuralService imovelRuralService;
	
	@EJB
	private ImovelRuralFacade imovelRuralFacade;

	@EJB
	private ReservaLegalService reservaLegalService;
	
	@EJB
	private RepresentanteLegalService representanteLegalService;
	
	@EJB
	private AtoDeclaratorioFacade atoDeclaratorioFacade;
	
	@EJB
	private DeclaracaoIntervencaoAppFacade diapFacade;
	
	@EJB
	private DeclaracaoInexigibilidadeService declaracaoInexigibilidadeService;
	
	@EJB
	private ModeloCertificadoInexigibilidadeService modeloCertificadoInexigibilidadeService;
	
	private DeclaracaoInexigibilidadeInfoGeralService declaracaoInexigibilidadeInfoGeralService;
	
	/**
	 * Método que busca o {@link Certificado} pelo <i>ideCertificado</i> ou pelo <i>numToken</i>. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 25/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado buscarCertificadoBy(Object obj)  {
		if(obj instanceof Integer){
			return certificadoService.carregarCertificadoByide((Integer) obj);
		} 
		else if (obj instanceof String){
			return certificadoService.carregarByToken((String) obj);
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado buscarCertificadoByNumeroCadastro(String numeroCadastro)  {
		return certificadoService.carregarCertificadoByNumCertificado(numeroCadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoParcialDae buscarDeclaracaoBy(Object obj) throws Exception {
		if(obj instanceof Integer){
			return declaracaoParcialDaeService.carregaDeclaracaoByide((Integer) obj);
		} 
		else if (obj instanceof String){
			return declaracaoParcialDaeService.carregarByToken((String) obj);
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarCertificadoParaRequerimento(Certificado certificado) {
		return certificadoService.carregarInformacoesRequerimentoByCertificado(certificado.getIdeCertificado());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoParcialDae carregarCertificadoParaRequerimento(DeclaracaoParcialDae certificado) throws Exception{
		return declaracaoParcialDaeService.carregarInformacoesRequerimentoByCertificado(certificado.getIdeDeclaracaoParcialDae());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarCertificadoParaCadastro(Certificado certificado) {
		return certificadoService.buscarCertificadoCadastro(certificado.getNumCertificado());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Certificado carregarCertificadoParaCerh(Certificado certificado)  {
		return certificadoService.carregarInformacoesCerhByCertificado(certificado.getIdeCertificado());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtosAmbientaisBy(Integer ideRequerimento) {
		return atoAmbientalService.filtrarListaAtoAmbientalPorEnquadramentoRequerimento(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipologiaGrupo buscarTipologiaGrupoBy(Integer ideEmpreendimento)  {
		return tipologiaGrupoService.buscarTipologiaPorEmpreendimento(ideEmpreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRural buscarImovelRuralBy(Integer ideImovel) {
		return imovelRuralFacade.carregarByIde(ideImovel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegal buscarReservaLegalBy(ImovelRural imovelRural)  {
		return imovelRuralFacade.buscaReservaLegalByImovelRural(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRural> listarImoveisEmCompensacaoDeReservaLegal(Integer ideImovel) {
		return imovelRuralFacade.imoveisEmCompensacaoDeReserva(ideImovel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean isRlMenorQueVintePorCento(ImovelRural pImovelRural) throws Exception {
		return imovelRuralFacade.isRlMenor20PorCento(pImovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegal buscarReservaLegalBy(Integer ideImovel) {
		return reservaLegalService.buscaReservaLegalByImovelRural(new ImovelRural(ideImovel));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReservaLegal buscaReservaLegalByImovelRural(ImovelRural imovelRural) {
		return reservaLegalService.buscaReservaLegalByImovelRural(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRural> imoveisEmCompensacaoDeReserva(Integer ideImovel) {
		return imovelRuralService.imoveisEmCompensacaoDeReserva(ideImovel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean hasTermosDeCompromisso(Integer ideImovel)  {
		return imovelRuralFacade.hasTermosDeCompromisso(ideImovel);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerarCertificadoImovelRural(TipoCertificadoEnum termoDeCompromisso, Integer ideImovel)  {
		imovelRuralFacade.gerarCertificado(termoDeCompromisso, ideImovel);		
	}
	
	private StringBuilder getURLGeoBahia(String parametros) throws MalformedURLException, IOException {
		return criarStreamComUrl(obterStringUrlGeoBahiaPorTipo(parametros));
	}

	private StringBuilder getURLGeoBahia(Integer ideImovel) throws MalformedURLException, IOException {
		return criarStreamComUrl(obterStringUrlGeoBahiaPorTipo("idimov="+ideImovel+"&res=640%20480"));
	}

	private StringBuilder criarStreamComUrl(String pUrl) throws MalformedURLException, IOException {
		URL url = new URL(pUrl);

		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

		StringBuilder buffer = new StringBuilder();

		String linha;
		while ((linha = br.readLine()) != null) {
			buffer.append(linha);
		}
		br.close();
		return buffer;
	}

	private String obterStringUrlGeoBahiaPorTipo(String parametros) {
		return URLEnum.CAMINHO_GEOBAHIA_CERTIFICADO + parametros;
	}
	
	public StreamedContent getImprimirCertificadoCefir(Integer ideRequerimento, Integer ideImovel) {
		try {
			Map<String, Object> lParametros = new HashMap<String, Object>();
			String linkGeobahia = "";
			String linkGeobahiaRL = "";
			List<String> linkCompensacaoRL = null;
			Boolean indSobreposicaoApp = false;

			ImovelRural imovelRural = buscarImovelRuralBy(ideImovel);
			imovelRural.setReservaLegal(buscarReservaLegalBy(imovelRural));

			try {

				StringBuilder buffer = getURLGeoBahia(ideImovel);

				linkGeobahia = ConfigEnum.GEOBAHIA_SERVER + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20");
				
				List<ImovelRural> listImoveisEmCompensacaoDeReserva = listarImoveisEmCompensacaoDeReservaLegal(ideImovel);
				
				if (!Util.isNullOuVazio(listImoveisEmCompensacaoDeReserva)) {
					linkCompensacaoRL = new ArrayList<String>();
					for (int i = 0; i < listImoveisEmCompensacaoDeReserva.size(); i++) {
						buffer = getURLGeoBahia("idimov=" + ideImovel + "&res=640%20480&idimovc=" + listImoveisEmCompensacaoDeReserva.get(i));
						linkCompensacaoRL.add(ConfigEnum.GEOBAHIA_SERVER + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20"));
					}
				}
				lParametros.put("isRlMenorQueVintePorCento", isRlMenorQueVintePorCento(imovelRural));
			} catch (Exception e) {
				lParametros.put("isRlMenorQueVintePorCento", false);
				System.out.println(e.getMessage());
			}

			lParametros.put("SHAPE", linkGeobahia);
			lParametros.put("SHAPE_RL", linkGeobahiaRL);
			lParametros.put("ind_sobreposicao_app", indSobreposicaoApp);

			if (!Util.isNullOuVazio(linkCompensacaoRL)) {
				lParametros.put("SHAPE_COMPENSACAO_RL", linkCompensacaoRL);
				lParametros.put("AREA_IMOVEL", imovelRural.getValArea());
			}

			lParametros.put("ide_imovel", ideImovel);
			return new RelatorioUtil("certificado_cefir.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF,
					true);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			return null;
		}
	}

	
	public StreamedContent getImprimirTermoDeCompromisso(Integer ideImovel) {
		try {
			ReservaLegal lReserva = buscaReservaLegalByImovelRural(new ImovelRural(ideImovel));
			Boolean indSobreposicaoApp = false;
			String linkGeobahia = "";
			String linkGeobahiaRL = "";
			List<String> linkCompensacaoRL = null;

			StringBuilder buffer = getURLGeoBahia(ideImovel);

			if(!Util.isNullOuVazio(buffer)) {

				linkGeobahia = ConfigEnum.GEOBAHIA_SERVER + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20");
				
				List<ImovelRural> listImoveisEmCompensacaoDeReserva = new ArrayList<ImovelRural>();
				
				if(lReserva.getIdeTipoArl().getIdeTipoArl().equals(3)) {
					listImoveisEmCompensacaoDeReserva = imoveisEmCompensacaoDeReserva(ideImovel);
				}
				
				if(!Util.isNullOuVazio(listImoveisEmCompensacaoDeReserva)) {
					linkCompensacaoRL = new ArrayList<String>();
					for (int i = 0; i < listImoveisEmCompensacaoDeReserva.size(); i++) {
						buffer = getURLGeoBahia("idimov="+ideImovel+"&res=640%20480&idimovc=" + listImoveisEmCompensacaoDeReserva.get(i)); 
						linkCompensacaoRL.add(ConfigEnum.GEOBAHIA_SERVER + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20"));
					}
				}
			}

			if(!Util.isNullOuVazio(lReserva)) {
				indSobreposicaoApp = lReserva.getIndSobreposicaoApp();
			}
			
			if (!hasTermosDeCompromisso(ideImovel)) {
				gerarCertificadoImovelRural(TipoCertificadoEnum.TERMO_DE_COMPROMISSO, ideImovel);
			}
			
			return impTermoDeCompromisso(ideImovel, indSobreposicaoApp,  linkGeobahia,linkGeobahiaRL, linkCompensacaoRL);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}
	
	public StreamedContent impTermoDeCompromisso(Integer ideImovelRural, Boolean indSobreposicaoApp, String linkGeobahia, String linkGeobahiaRL, List<String> linkCompensacaoRL) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();

		ImovelRural imovelRural = this.imovelRuralService.carregarTudo(new ImovelRural(ideImovelRural));
		imovelRural.setAppCollection(imovelRuralFacade.listarAppByImovelRural(imovelRural));					
		imovelRural.setAreaProdutivaCollection(imovelRuralFacade.listarAreaProdutivaByImovelRural(imovelRural));
		imovelRural.setVegetacaoNativa(imovelRuralFacade.listarVegetacaoNativaByImovelRural(imovelRural));

		Collection<PessoaImovel> listPessoaImovel = this.imovelRuralService.filtrarPROPRIETARIOImovel(new Imovel(ideImovelRural));
		Collection<Pessoa> listPessoa = this.imovelRuralService.listarProprietariosJustoPossuidoresImovel(ideImovelRural);
		Collection<RepresentanteLegal> representantesLegais = null;
		TipoVinculoImovel tipoVinculoImovelRural = new TipoVinculoImovel(TipoVinculoImovel.TIPO_VINCULO_PROPRIETARIO);

		List<AssentadoIncraImovelRural> assentados = this.imovelRuralFacade.listarAssentadoIncraImovelRuralPorImovelRural(imovelRural);
		List<PessoaFisica> listAssentado = new ArrayList<PessoaFisica>();
		if(!Util.isNullOuVazio(assentados)) {
			for (AssentadoIncraImovelRural assentadoIncraImovelRural : assentados) {
				listAssentado.add(assentadoIncraImovelRural.getIdeAssentadoIncra().getIdePessoaFisica());
			}
		}

		if(!Util.isNullOuVazio(listPessoaImovel)){
			for (PessoaImovel pessoaImovel : listPessoaImovel) {
				if(pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)){
					tipoVinculoImovelRural.setIdeTipoVinculoImovel(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR);
				}
			}
		}

		if(!Util.isNullOuVazio(listPessoa) && !listPessoa.isEmpty()){
			representantesLegais = representanteLegalService.listarRepresentantesLegais(listPessoa);
		}
		imovelRural.setReservaLegal(reservaLegalService.buscaReservaLegalByImovelRural(imovelRural));

		lParametros.put("TERMO", RelatorioUtil.formatarTermoDeCompromisso(imovelRural, tipoVinculoImovelRural, listPessoa, representantesLegais, listAssentado));
		if(tipoVinculoImovelRural.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)){
			lParametros.put("IMOVEL", "<i>" + imovelRural.getDesDenominacao() + "</i>" + (!Util.isNullOuVazio(imovelRural.getRegistroMatricula()) ? ", Nº de registro do documento de posse: <i>"+imovelRural.getRegistroMatricula()+ "</i>" : "") + (!Util.isNullOuVazio(imovelRural.getNumItr()) ? ", ITR: <i>"+imovelRural.getNumItr() + "</i>" : ""));
		}else{
			lParametros.put("IMOVEL", "<i>" + imovelRural.getDesDenominacao() + "</i>" + ", Matrícula: <i>"+imovelRural.getRegistroMatricula()+ "</i>" + (!Util.isNullOuVazio(imovelRural.getNumItr()) ? ", ITR: <i>"+imovelRural.getNumItr() + "</i>" : ""));
		}
		lParametros.put("ENDERECO", imovelRural.getImovel().getIdeEndereco().getEnderecoCompletoSemPais());
		lParametros.put("ide_imovel", ideImovelRural);
		lParametros.put("ind_sobreposicao_app", indSobreposicaoApp);
		lParametros.put("isRlMenorQueVintePorCento", isRlMenorQueVintePorCento(imovelRural));
		lParametros.put("temMaisDeUmProprietario", temMaisDeUmProprietario(imovelRural, tipoVinculoImovelRural, listPessoa, representantesLegais, listAssentado));

		lParametros.put("precisaLicencaAP", imovelRuralService.precisaDeLicencaAreaProdutiva(imovelRural));
		lParametros.put("precisaOutorga", imovelRuralService.precisaDeOutorga(imovelRural));
		lParametros.put("possuiPradAPP", imovelRuralService.possuiPraApp(imovelRural));
		lParametros.put("possuiPradRL", imovelRuralService.possuiPraRl(imovelRural));
		lParametros.put("possuiPradOP", imovelRuralService.possuiPradOP(imovelRural));
		lParametros.put("isJustoPossuidor", imovelRuralService.carregarTipoVinculoImovel(imovelRural).getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR));

		//SHAPES
		lParametros.put("SHAPE", linkGeobahia);
		lParametros.put("SHAPE_RL", linkGeobahiaRL);

		if(!Util.isNullOuVazio(linkCompensacaoRL)) {
			lParametros.put("SHAPE_COMPENSACAO_RL", linkCompensacaoRL);
			lParametros.put("AREA_IMOVEL", imovelRural.getValArea());
		}

		if(imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.ECIP.getId())) {
			lParametros.put("isRlEmCompensacao", true);
		} else {
			lParametros.put("isRlEmCompensacao", false);
		}

		if(imovelRural.isImovelINCRA()){
			return new RelatorioUtil("termo_de_compromisso_cefir_incra.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);			
		}
		return new RelatorioUtil("termo_de_compromisso_cefir.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	private boolean temMaisDeUmProprietario(ImovelRural imovelRural, TipoVinculoImovel tipoVinculoImovelRural, Collection<Pessoa> listPessoa, Collection<RepresentanteLegal> representantesLegais, List<PessoaFisica> listAssentado) {
		int qtdProprietarios = 0;
		
		if((!Util.isNullOuVazio(listPessoa) || !listPessoa.isEmpty())){
			qtdProprietarios += listPessoa.size();				
		}
		
		if(!Util.isNullOuVazio(listAssentado)) {
			qtdProprietarios += listAssentado.size();
		}
		
		if(qtdProprietarios > 1){
			return true;
		} else {
			return false;
		}
	}
	
	public StreamedContent getImprimir(CadastroAtividadeNaoSujeitaLic cadastro) throws Exception{
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_cadastro", cadastro.getIdeCadastroAtividadeNaoSujeitaLic());
		lParametros.put("TEM_MARCA_DAGUA", true);
		lParametros.put("TIPO_ATIVIDADE", cadastro.getTipoAtividadeNaoSujeitaLicenciamento());
		lParametros.put("NOME_RELATORIO", "certificado.jasper");
		return getImprimir(lParametros);
	}
	
	public StreamedContent getImprimir(CertificadoDTO dto) throws Exception{
		Requerimento req = dto.getCertificado().getRequerimento();
		ImpressoraAtoDeclaratorioIF imprimir = null;
		DocumentoObrigatorioEnum docEnum = null;
		if(dto.isDiap()){
			docEnum = DocumentoObrigatorioEnum.FORMULARIO_DIAP;
			imprimir = new ImpressoraAtoDeclaratorioDIAP(diapFacade.montarTextoRepresentadoPor(req));
		} 
		else {
			if(dto.isRfp()){
				docEnum = DocumentoObrigatorioEnum.FORMULARIO_RFP;
			}
			imprimir = new ImpressoraAtoDeclaratorio();
		}
		return imprimir.imprimirCertificado(req.getIdeRequerimento(), docEnum);
	}
	
	
	public StreamedContent getImprimir(Cerh ideCerh) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_cerh", ideCerh.getIdeCerh());
		lParametros.put("TEM_MARCA_DAGUA", true);
		lParametros.put("CERH", true);
		lParametros.put("NOME_RELATORIO", "certificado.jasper");
		return getImprimir(lParametros);
	}
	
	public StreamedContent getImprimir(String relatorioCertificado, Requerimento requerimento) throws Exception{
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("ide_requerimento", requerimento.getIdeRequerimento());
		lParametros.put("TEM_MARCA_DAGUA", true);
		lParametros.put("NOME_RELATORIO", relatorioCertificado);
		return getImprimir(lParametros);
	}

	private StreamedContent getImprimir(Map<String, Object> lParametros) throws Exception {
		return new RelatorioUtil(lParametros).gerar();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Certificado> listarPorNumeroCertificado(String token){
		return certificadoService.listarPorNumeroCertificado(token);
	}
	
	public StreamedContent getImprimirInexigibilidade(Requerimento requerimento) throws Exception{
			Map<String, Object> lParametros = new HashMap<String, Object>();
			lParametros.put("SUBREPORT_DIR", retornaCaminhoRelatorioInexigibilidade());
			lParametros.put("NOME_RELATORIO", obterModeloCertificado(requerimento.getIdeRequerimento()));
			lParametros.put("IDE_REQUERIMENTO", requerimento.getIdeRequerimento());
			lParametros.put("TEM_MARCA_DAGUA", true);
			return getImprimir(lParametros);
	}
	
	private String obterModeloCertificado(Integer ideRequerimento) {
		String modelo = null;
		
		DeclaracaoInexigibilidade declaracao = declaracaoInexigibilidadeService.obterDeclaracaoPorRequerimento(new Requerimento(ideRequerimento));
		
		if(!Util.isNull(declaracao)) {
			AtividadeInexigivel atividade = declaracao.getAtividadeInexigivel();
			
			if(!Util.isNull(atividade)) {
				AtividadeInexigivelModeloCertificadoInexigibilidade modeloCertificado = modeloCertificadoInexigibilidadeService.obterModeloCertificadoPorAtividadeInexigivel(atividade);
				
				if(!Util.isNull(modeloCertificado)) {
					modelo = modeloCertificado.getModeloCertificadoInexigibilidade().getDescricao();
				}
				
				if(Util.isNull(modelo)) {
					DeclaracaoInexigibilidadeInfoGeral declaracaoInexigibilidadeInfoGeral = declaracaoInexigibilidadeInfoGeralService.obterDeclaracaoInfoGeralPor(declaracao);
					
					if(declaracaoInexigibilidadeInfoGeral != null) {
						if(declaracaoInexigibilidadeInfoGeral.getTipoLocalAtividadeInexigivel() != null){
							if(TipoLocalAtividadeInexigivelEnum.LOCAL_ESPECIFICO.getId().equals(declaracaoInexigibilidadeInfoGeral.getTipoLocalAtividadeInexigivel().getIdeTipoLocalAtividadeInexigivel())) {
								modelo = "certificado_inexigibilidade_modelo01";
							}else if(TipoLocalAtividadeInexigivelEnum.DIVERSOS_MUNICIPIOS_NA_BAHIA.getId().equals(declaracaoInexigibilidadeInfoGeral.getTipoLocalAtividadeInexigivel().getIdeTipoLocalAtividadeInexigivel())) {
								modelo = "certificado_inexigibilidade_modelo03";
							}
						}
					}
				}
			}
		}
		
		if(modelo != null) {
			modelo = modelo + ".jasper";
		}
		
		return modelo;
	}
	
	public static String retornaCaminhoRelatorioInexigibilidade() {	
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		return sessao.getServletContext().getRealPath(File.separator + Uri.URL_RELATORIOS_INEXIGIBILIDADE) + File.separator;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Imovel carregarImovelByCertificado(Integer ideCertificado) throws Exception {
		return certificadoService.carregarImovelByCertificado(ideCertificado);
	}
}