package br.gov.ba.seia.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.CategoriaDocumento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.enumerator.CategoriaDocumentoEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.PaginaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AceiteReenquadramentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("aceiteReenquadramentoController")
@ViewScoped
public class AceiteReenquadramentoController implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	private AceiteReenquadramentoService aceiteReenquadramentoService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;

	private Boolean aceite;
	private ProcessoReenquadramentoDTO processoReenquadramentoDTO;
	private String nomeFile;
	private UploadedFile discordarReenquadramentoFile;
	private ArquivoProcesso arquivoProcesso;
	
	
	/**
	 * Método que carrega o processo de reenquadramento
	 * @param ProcessoReenquadramentoDTO
	 */
	public void carregarProcessoReenquadramento(ProcessoReenquadramentoDTO prc) {
		inicializarVariaveis();
		this.processoReenquadramentoDTO = prc;
	}
	
	/**
	 * Método utilizado para inicializar todas as variáveis do controller
	 */
	public void inicializarVariaveis() {
		processoReenquadramentoDTO = null;
		limparCampos();
	}	
	
	/**
	 * Método chamado no onChange do aceite do reenquadramento
	 */
	public void onChangeAceite() {
		if(aceite) {
			Html.exibir("dlgConcordoReenquadramento");
		}
		else {
			limparAceite();
			Html.exibir("dlgNaoConcordo");
		}
	}
	
	/**
	 * Método chamado no onChange do aceite do reenquadramento
	 */
	public void limparAceite() {
		limparCampos();
		Html.atualizar("frmAceiteReenquadramento:pnlResposta","formUploadJustificativaNaoConcordo");
	}

	/**
	 * Método para limpar variáveis em tela
	 */
	private void limparCampos() {
		aceite = null;
		discordarReenquadramentoFile = null;
		arquivoProcesso = null;
		nomeFile = null;
	}
	
	/**
	 * Método responsável pelo upload da justificativa caso o requerente não aceite o reenquadramento do processo
	 * @param event
	 */
	public void fileUploadListener(FileUploadEvent event) {
		discordarReenquadramentoFile = event.getFile();
		if(Util.isNullOuVazio(discordarReenquadramentoFile)) {
			JsfUtil.addErrorMessage("O Upload Documento é obrigatório!");
		}
		else {
			String caminho = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.PROCESSO.toString());
			Pessoa pessoa = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
			nomeFile = event.getFile().getFileName();
			arquivoProcesso = new ArquivoProcesso(caminho,"Justificativa para não reenquadrar", processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso(), pessoa);
			arquivoProcesso.setCategoriaDocumento(new CategoriaDocumento(CategoriaDocumentoEnum.JUSTIFICATIVA_NAO_REENQUADRAR.getId()));
			arquivoProcesso.setDtcCriacao(new Date());
		}
	}
	
	/**
	 * Método que exibe a tela de discordar do reenquadramento
	 */
	public void discordarReenquadramento() {
		if(Util.isNullOuVazio(discordarReenquadramentoFile)) {
			JsfUtil.addErrorMessage("O Upload Documento é obrigatório!");
		}else {
			Html.exibir("dlgDiscordoReenquadramento");
		}
	}
	
	/**
	 * Método que confirma que o requerente está de acordo com o reenquadramento
	 */
	public void confirmarConcordarReenquadramento(ActionEvent evt) {
		try {
			aceiteReenquadramentoService.concordarReenquadramento(processoReenquadramentoDTO);
			Object object = evt.getComponent().getAttributes().get("metodoLiberarEdicao");
			if(!Util.isNull(object)) {
				((MetodoUtil) object).executeMethod();
			}
			Html.atualizar("pnlAceiteReenquadramento","abas");
			Html.esconder("dlgConcordoReenquadramento");
			MensagemUtil.sucesso("Você aceitou o reenquadramento do processo. Edite o formulário do requerimento segundo as orientações do orgão.");
		}
		catch (Exception e) {
			Log4jUtil.log(AceiteReenquadramentoController.class.getSimpleName(), Level.ERROR, e);
		}
	}
	
	/**
	 * Método que confirma que o requerente não está de acordo com o reenquadramento
	 */
	public void confirmarDiscordarReenquadramento() {
		try {
			aceiteReenquadramentoService.discordarReenquadramento(processoReenquadramentoDTO,arquivoProcesso);
			FacesContext.getCurrentInstance().getExternalContext().redirect(PaginaEnum.PAUTA_REENQUADRAMENTO.getUrl());
		}
		catch (Exception e) {
			Log4jUtil.log(AceiteReenquadramentoController.class.getSimpleName(), Level.ERROR, e);
		}
	}
	
	public boolean isRenderedAceiteReenquadramento() {
		if(!Util.isNull(processoReenquadramentoDTO) && !processoReenquadramentoDTO.isVisualizar()) {
			return Util.isNull(processoReenquadramentoDTO.getProcessoReenquadramento().getIndAceiteRequerente()); 
		}
		return false;
	}
	
	/**<p>Verifica qual modal deve ser exibido caso o usuario seja requerente 
	 * e tenha realizado modificação em 1 das abas</p>
	 * 
	 * */
	public void showDiscordar() {
		Boolean isRespostaChange = (Boolean) SessaoUtil.recuperarObjetoSessao("isRespostaChange");
		this.arquivoProcesso = null;
		this.discordarReenquadramentoFile = null;
		this.nomeFile = null;
		if(!ContextoUtil.getContexto().getUsuarioLogado().getIndTipoUsuario()
				&& !Util.isNullOuVazio(isRespostaChange) && isRespostaChange) {
			Html.exibir("dlgAlteracaoAba");
		}else {
			Html.exibir("dlgNaoConcordo");
		}
	}
	
	/**
	 *Método que realiza o download do arquivo de justificativia
	 * @return
	 */
	public StreamedContent getArquivoBaixar() {		
		if(!Util.isNull(arquivoProcesso)) {			
			try {
				return gerenciaArquivoService.getContentFile(arquivoProcesso.getDscCaminhoArquivo());
			} catch (Exception e) {
				MensagemUtil.erro010();
				Log4jUtil.log(AceiteReenquadramentoController.class.getSimpleName(), Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return null;
	}
	
	/***
	 * Verifica a existencia do arquivo de justificativa 
	 * de discordância do reenquadramento
	 * @return
	 */
	public Boolean getVerificarExistenciaJustificativa() {
		return Util.isNull(arquivoProcesso) ? Boolean.FALSE : Boolean.TRUE; 
	}
	
	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public Boolean getAceite() {
		return aceite;
	}

	public void setAceite(Boolean aceite) {
		this.aceite = aceite;
	}
	
	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}
	
	public UploadedFile getDiscordarReenquadramentoFile() {
		return discordarReenquadramentoFile;
	}

	public void setDiscordarReenquadramentoFile(UploadedFile discordarReenquadramentoFile) {
		this.discordarReenquadramentoFile = discordarReenquadramentoFile;
	}

	public ArquivoProcesso getArquivoProcesso() {
		return arquivoProcesso;
	}

	public void setArquivoProcesso(ArquivoProcesso arquivoProcesso) {
		this.arquivoProcesso = arquivoProcesso;
	}
	
	
}