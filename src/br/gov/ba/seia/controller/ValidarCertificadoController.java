package br.gov.ba.seia.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.hamcrest.core.IsNull;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.CertificadoDTO;
import br.gov.ba.seia.dto.DeclaracaoDTO;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ConsumidorReposicaoFlorestal;
import br.gov.ba.seia.entity.DeclaracaoParcialDae;
import br.gov.ba.seia.entity.DetentorReposicaoFlorestal;
import br.gov.ba.seia.entity.DevedorReposicaoFlorestal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.facade.AtoDeclaratorioFacade;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.ValidacaoCertificadoFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.service.ConsumidorReposicaoFlorestalService;
import br.gov.ba.seia.service.DetentorReposicaoFlorestalService;
import br.gov.ba.seia.service.DevedorReposicaoFlorestalService;
import br.gov.ba.seia.service.MemoriaCalculoDaeParcelaService;
import br.gov.ba.seia.service.RelatoriosRequerimentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Uri;
import br.gov.ba.seia.util.Util;

@Named("validarCertificadoController")
@ViewScoped
public class ValidarCertificadoController {

	@EJB
	private ValidacaoCertificadoFacade facade;
	
	@EJB
	private RelatoriosRequerimentoService relatoriosRequerimentoService;
	
	@EJB
	private AtoDeclaratorioFacade atoDeclaratorioFacade;
	
	@Inject
	private DetentorReposicaoFlorestalService detentorReposicaoFlorestalService;
	
	@Inject
	private ConsumidorReposicaoFlorestalService consumidorReposicaoFlorestalService;
	
	@Inject
	private DevedorReposicaoFlorestalService devedorReposicaoFlorestalService;
	
	@Inject
	private MemoriaCalculoDaeParcelaService memoriaCalculoDaeParcelaService;
	
	@EJB
	ImovelRuralFacade imovelRuralServiceFacade;

	private CertificadoDTO dto;
	
	private DeclaracaoDTO dtoDeclaracao;

	private TipologiaGrupo tipologia;
	private String token;
	private boolean exibeMensagemErroASV;

	public void validarCertificado() {
		try {
			formatarToken();
			
			if (!validarTokenLikeCertificado()) {
				JsfUtil.addWarnMessage("A chave informada não é válida. Favor digitar novamente.");
				dto = new CertificadoDTO(null);
			}
			
			exibeMensagemErroASV = verificaMensagemErroASV();
			dtoDeclaracao = new DeclaracaoDTO(null);
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private boolean validarTokenLikeCertificado() throws Exception {
		Certificado certificado = buscarCertificadoByToken();
		DeclaracaoParcialDae decParcialDae = buscarDeclaracaoByToken();
		
		if (!Util.isNull(certificado)) {
			dtoDeclaracao = new DeclaracaoDTO(null);
			carregarInformacoesDoCertificado();
		}else if (!Util.isNull(decParcialDae)) {
			dto = new CertificadoDTO(null);
			carregarInformacoesDaDeclaracao(decParcialDae.getIdeDeclaracaoParcialDae());
		}
		
		return !Util.isNull(certificado) ||  !Util.isNull(decParcialDae);
	}

	private void carregarInformacoesDoCertificado() throws Exception {

		montarDTObasico();

		if(dto.isCertificadoDeRequerimento()){

			montarDTOparaCertificadoRequerimento();

			Integer ideRequerimento = dto.getCertificado().getRequerimento().getIdeRequerimento();
			carregarAtosRequerimento(ideRequerimento);

			if(!Util.isNullOuVazio(dto.getCertificado().getRequerimento().getUltimoEmpreendimento())){
				
				Integer ideEmpreendimento = dto.getCertificado().getRequerimento().getUltimoEmpreendimento().getIdeEmpreendimento();
				carregarTipologiaGrupo(ideEmpreendimento);
			}
		} 
		else if(dto.isCertificadoDeCadastro()){
			montarDTOparaCertificadoCadastro();
		} 
		else if(dto.isCertificadoDeImovelRural()){
			montarDTOparaCertificadoImovelRural();
		}
		else if(dto.isCertificadoDeCerh()){
			montarDTOparaCertificadoCerh();
			if(!dto.isPodeImprimir()){
				JsfUtil.addWarnMessage(Util.getString("cerh_certificado_msg_cadastro_nao_completo"));
			}
		} 
		else if(dto.isCrf()){
			montarDTOparaCertificadoRequerimento();
			Integer ideRequerimento = dto.getCertificado().getRequerimento().getIdeRequerimento();
			carregarAtosRequerimento(ideRequerimento);
		}
	}
	
	private void carregarInformacoesDaDeclaracao(Integer ideCertificado) throws Exception {

		montarDTObasicoDeclaracao(ideCertificado);
		DeclaracaoParcialDae certificado = facade.carregarCertificadoParaRequerimento(dtoDeclaracao.getDeclaracao());
		dtoDeclaracao.getDeclaracao().setRequerimento(certificado.getRequerimento());
		dtoDeclaracao.setNumero(certificado.getRequerimento().getNumRequerimento());
		dtoDeclaracao.setNomRequerente(certificado.getRequerimento().getRequerente().getNomeRazao());
		dtoDeclaracao.setNumCpfCnpj(certificado.getRequerimento().getRequerente().getCpfCnpjFormatado());

		Integer ideRequerimento = dtoDeclaracao.getDeclaracao().getRequerimento().getIdeRequerimento();
		carregarAtosRequerimentoDeclaracao(ideRequerimento);

		if(!dtoDeclaracao.isPodeImprimir()){
			JsfUtil.addWarnMessage(Util.getString("cerh_certificado_msg_cadastro_nao_completo"));
		}
	}

	private Certificado buscarCertificadoByToken() throws Exception{
		return facade.buscarCertificadoBy(token);
	}
	
	private DeclaracaoParcialDae buscarDeclaracaoByToken() throws Exception{
		return facade.buscarDeclaracaoBy(token);
	}

	private void montarDTObasico() throws Exception {
		//dto = new CertificadoDTO(facade.buscarCertificadoByNumeroCadastro(numeroCadastro));
		dto = new CertificadoDTO(facade.buscarCertificadoBy(token));
	}
	
	private void montarDTObasicoDeclaracao(Integer ideCertificado) throws Exception {
		dtoDeclaracao = new DeclaracaoDTO(facade.buscarDeclaracaoBy(ideCertificado));
		System.out.println("teste");
	}

	private void montarDTOparaCertificadoRequerimento() throws Exception {
		Certificado certificado = facade.carregarCertificadoParaRequerimento(dto.getCertificado());
		dto = new CertificadoDTO(certificado, certificado.getRequerimento());
	}
	
	private void montarDTOparaCertificadoImovelRural() throws Exception {
		Certificado certificado = facade.carregarCertificadoParaRequerimento(dto.getCertificado());
		dto = new CertificadoDTO(certificado, certificado.getRequerimento(), certificado.getRequerimento().getImovel());
		
		dto.getCertificado().getRequerimento().setImovel(facade.carregarImovelByCertificado(certificado.getIdeCertificado()));
				
		if(!Util.isNullOuVazio(dto.getCertificado().getRequerimento().getImovel())) {
			facade.buscarImovelRuralBy(dto.getCertificado().getRequerimento().getImovel().getIdeImovel());
			dto.getCertificado().getRequerimento().getImovel().setImovelRural(facade.buscarImovelRuralBy(dto.getCertificado().getRequerimento().getImovel().getIdeImovel()));
		}

	}

	private void montarDTOparaDeclaracaoRequerimento() throws Exception {
		DeclaracaoParcialDae certificado = facade.carregarCertificadoParaRequerimento(dtoDeclaracao.getDeclaracao());
		dtoDeclaracao = new DeclaracaoDTO(certificado, certificado.getRequerimento());
	}

	private void montarDTOparaCertificadoCadastro() throws Exception {
		Certificado certificado = facade.carregarCertificadoParaCadastro(dto.getCertificado());
		dto = new CertificadoDTO(certificado, certificado.getCadastro());
	}

	private void montarDTOparaCertificadoCerh() throws Exception {
		Certificado certificado = facade.carregarCertificadoParaCerh(dto.getCertificado());
		dto = new CertificadoDTO(certificado, certificado.getIdeCerh());
	}

	private void carregarAtosRequerimento(Integer ideRequerimento) throws Exception {
		dto.getCertificado().getRequerimento().setAtosAmbientais(facade.listarAtosAmbientaisBy(ideRequerimento));
	}
	
	private void carregarAtosRequerimentoDeclaracao(Integer ideRequerimento) throws Exception {
		dtoDeclaracao.getDeclaracao().getRequerimento().setAtosAmbientais(facade.listarAtosAmbientaisBy(ideRequerimento));
	}

	private void carregarTipologiaGrupo(Integer ideEmpreendimento) throws Exception {
		tipologia = facade.buscarTipologiaGrupoBy(ideEmpreendimento);
	}

	private void formatarToken() {
		token = token.replaceAll("-", "");
	}

	public String getNomeTipoCertificado(){
		String nome = "Nº "; 
		if(!Util.isNullOuVazio(dto)){
			if(dto.isCertificadoDeRequerimento() || dto.isCertificadoDeImovelRural()){
				nome += "Requerimento";
			} 
			else if(dto.isCertificadoDeCadastro() || dto.isCertificadoDeCerh()){
				nome += "Cadastro";
			}
		}else if(!Util.isNullOuVazio(dtoDeclaracao)){
			nome += "Requerimento";
		}
		return nome += ":";
	}

	public String getDscTipoCertificado(){
		String imprimir = "Imprimir Certificado ";
		if(!Util.isNull(dto)){
			if(!Util.isNull(dto.getCertificado())){
				if(dto.isExisteTipoCertificado()){
					imprimir += TipoCertificadoEnum.getDscTipoCertificado(dto.getCertificado().getTipoCertificado());
				} 
				else {
					if(dto.isRcfp()){
						imprimir += "RCFP";
					} 
					else if (dto.isRfp()){
						imprimir += "RFP";
					} 
					else if (dto.isDiap()){
						imprimir += "DIAP";
					} 
					else {
						imprimir += "Ato Declaratório";
					}
				}
			}
		}
		if(!Util.isNullOuVazio(dtoDeclaracao)){
			if(!Util.isNull(dtoDeclaracao.getDeclaracao())){
				imprimir += "de Cumprimento de Reposição Florestal";
			}
		}
		return imprimir;
	}
	
	public String getDscComplementoEndereco(){
		String dsc = "Endereço ";
		if(dto.isCertificadoDeImovelRural()){
			dsc += "Imóvel";
		} 
		else {
			dsc += "Empreendimento";
		}
		return dsc += ":";
	}

	/*
	 * IMPRESSAO
	 */
	public StreamedContent getImprimirCertificado() {
		if(!Util.isNullOuVazio(dto)){
			if(!Util.isNull(dto.getCertificado())){
				if(dto.isCertificadoDeRequerimento()){
					if(dto.isDla()){
						return getCertificadoDLA();
					}
					else if(dto.isLac()){
						return getCertificadoLac();
					}
					else if(dto.isRcfp()){
						return getCertificadoRCFP();
					}
					else if(dto.isRfp() || dto.isDiap()){
						return getCertificadoNovosAtosDeclaratorios(); 
					}
					else if(dto.isApe()){
						return getCertificadoAPE(); 
					}
					else if(dto.isInexigibilidade()){
						return getCertificadoInexigibilidade();
					}
					else if(dto.isRlac()){
						return getCertificadoRlac();
					}
					else {
						return getDocumentoDeclaracao();
					}
				} 
				else if(dto.isCertificadoDeCadastro()){
					/*return getCertificadoCadastro();*/
				} 
				else if(dto.isCertificadoDeImovelRural()){
					if(dto.getCertificado().isTermoCompromisso()){
						return getTermoDeCompromisso();
						
					} 
					else if (dto.getCertificado().isCefir()){
						return getCertificadoCEFIR();
					}
					else if (dto.getCertificado().isAvisoBndes()){
						return getImprimirAvisoBndes();
					}
					
				}
				else if(dto.isCertificadoDeCerh()){
					return getCertificadoCerh();
				}
				else if (dto.isCrf()) {
					return getCertificadoCRF(); 
				} 
			}
		}
		if(!Util.isNullOuVazio(dtoDeclaracao)){
			if(!Util.isNull(dtoDeclaracao.getDeclaracao())){
				try {
					return imprimirDeclaracao();
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}
		}
		return null;
	}

	private StreamedContent getCertificadoNovosAtosDeclaratorios() {
		try {
			return facade.getImprimir(dto);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	/*private StreamedContent getCertificadoCadastro() {
		try {
			return facade.getImprimir(dto.getCertificado().getCadastro());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}*/

	public StreamedContent getCertificadoLac() {
		try {
			return facade.getImprimir("certificado_lac_tcra.jasper", dto.getCertificado().getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			JsfUtil.addErrorMessage(e.getMessage());
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getCertificadoRlac() {
		try {
			return facade.getImprimir("certificado_rlac.jasper", dto.getCertificado().getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			JsfUtil.addErrorMessage(e.getMessage());
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getCertificadoDLA() {
		try {
			return facade.getImprimir("dla.jasper", dto.getCertificado().getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getDeclaracaoRelatorio() {
		try {
			return facade.getImprimir("dla.jasper", dto.getCertificado().getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent gerarRfp(Integer ideRequerimento) throws Exception {
		return new ImpressoraAtoDeclaratorio().imprimirCertificado(ideRequerimento, DocumentoObrigatorioEnum.FORMULARIO_RFP);
	}
	
	public StreamedContent getCertificadoCEFIR() {
		return facade.getImprimirCertificadoCefir(dto.getCertificado().getRequerimento().getIdeRequerimento(), dto.getCertificado().getRequerimento().getImovel().getIdeImovel());
	}

	public StreamedContent getTermoDeCompromisso() {
		return facade.getImprimirTermoDeCompromisso(dto.getCertificado().getRequerimento().getImovel().getIdeImovel());
	}

	public StreamedContent getCertificadoRFP() {
		try {
			return facade.getImprimir("relatorio_comprovante_rfp.jasper", dto.getCertificado().getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	public StreamedContent getCertificadoRCFP() {
		try {
			return facade.getImprimir("relatorio_rcfp.jasper", dto.getCertificado().getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	public StreamedContent getCertificadoAPE() {
		try {
			return facade.getImprimir("certificado_ape.jasper", dto.getCertificado().getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	public StreamedContent getCertificadoCRF() {
		try {
			return relatoriosRequerimentoService.gerarDocumentoCRF(dto.getCertificado().getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	public StreamedContent getDocumentoDeclaracao() {
		try {
			return facade.getImprimir("documentoDeclaracao.jasper", dto.getCertificado().getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	private StreamedContent getCertificadoCerh() {
		try {
			return facade.getImprimir(dto.getCertificado().getIdeCerh());
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("123456")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
	
	private StreamedContent getImprimirAvisoBndes() {
		return imovelRuralServiceFacade.getImprimirAvisoBndes(dto.getCertificado().getRequerimento().getImovel().getImovelRural().getIdeImovelRural(), true);
	}
	
	public boolean verificaMensagemErroASV(){
		List<Certificado> listCertificado = facade.listarPorNumeroCertificado(this.token);
		return listCertificado.size() > 1;

	}

	private StreamedContent imprimirDeclaracao() throws Exception {
		
		Map<String, Object> lParametros = new HashMap<String, Object>();
		lParametros.put("SUBREPORT_DIR", retornaCaminhoDeclaracaoReposicaoFlorestal());
		lParametros.put("NOME_RELATORIO", obterModeloDeclaracao(dtoDeclaracao.getDeclaracao().getRequerimento()));
		lParametros.put("ide_requerimento", dtoDeclaracao.getDeclaracao().getRequerimento().getIdeRequerimento());
		lParametros.put("parcela_referencia", dtoDeclaracao.getDeclaracao().getIdeMemoriaCalculoDaeParcela().getNumParcelaReferencia());
		lParametros.put("valor_parcela", dtoDeclaracao.getDeclaracao().getIdeMemoriaCalculoDaeParcela().getValor());
		lParametros.put("valor_parcela_extenso", Util.valorEmReaisParaExtenso(new BigDecimal(dtoDeclaracao.getDeclaracao().getIdeMemoriaCalculoDaeParcela().getValor())));
		
		return new RelatorioUtil(lParametros).gerar();
	}

	/*
	 * flags
	 */
	public boolean isExisteCertificado(){
		return !Util.isNull(dto) && dto.isExisteCertificado();
	}
	
	public boolean isExisteDeclaracao(){
		return !Util.isNull(dtoDeclaracao) && dtoDeclaracao.isExisteDeclaracaoParcialDae();
	}
	
	/*
	 * getters & setters
	 */
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public TipologiaGrupo getTipologia() {
		return tipologia;
	}

	public CertificadoDTO getDto() {
		return dto;
	}

	public boolean isExibeMensagemErroASV() {
		return exibeMensagemErroASV;
	}

	public DeclaracaoDTO getDtoDeclaracao() {
		return dtoDeclaracao;
	}
	
	public static String retornaCaminhoDeclaracaoReposicaoFlorestal() {	
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

		return sessao.getServletContext().getRealPath(File.separator + Uri.URL_DECLARACAO_REPOSICAO_FLORESTAL) + File.separator;
	}
	
	public String obterModeloDeclaracao(Requerimento requerimento){
		String modeloDeclaracao = "";
		try {
			DetentorReposicaoFlorestal detentor = detentorReposicaoFlorestalService.obterDetentorReposicaoFlorestalPorRequerimento(requerimento.getIdeRequerimento());
			ConsumidorReposicaoFlorestal consumidor = consumidorReposicaoFlorestalService.obterConsumidorReposicaoFlorestalPorRequerimento(requerimento.getIdeRequerimento());
			DevedorReposicaoFlorestal devedor = devedorReposicaoFlorestalService.obterDevedorPorRequerimento(requerimento.getIdeRequerimento());
			
			if(!Util.isNullOuVazio(detentor) && Util.isNullOuVazio(consumidor) && Util.isNullOuVazio(devedor)){
				modeloDeclaracao = "declaracao_parcial_quitacao_modelo01.jasper";
			}else if(Util.isNullOuVazio(detentor) && !Util.isNullOuVazio(consumidor) && Util.isNullOuVazio(devedor)){
				modeloDeclaracao = "declaracao_parcial_quitacao_modelo02.jasper";
			}else if(Util.isNullOuVazio(detentor) && Util.isNullOuVazio(consumidor) && !Util.isNullOuVazio(devedor)){
				modeloDeclaracao = "declaracao_parcial_quitacao_modelo03.jasper";
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return modeloDeclaracao;
	}
	
	public StreamedContent getCertificadoInexigibilidade() {
		try {
			return facade.getImprimirInexigibilidade(dto.getCertificado().getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("")); // ADICIONAR MSG DE ERRO
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}
}
