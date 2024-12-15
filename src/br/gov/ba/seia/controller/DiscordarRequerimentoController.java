package br.gov.ba.seia.controller;

import java.io.Serializable;
import java.util.Date;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.CategoriaDocumento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.CategoriaDocumentoEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.service.DiscordarReenquadramentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author paulo
 *
 */
@Named("discordarRequerimentoController")
@SessionScoped
public class DiscordarRequerimentoController implements Serializable {
	private static final long serialVersionUID = 1L;

	private ProcessoReenquadramentoDTO processoReenquadramentoDTO;
	private String nomeFile;
	private UploadedFile discordarReenquadramentoFile;
	
	@Inject
	private NovoRequerimentoController novoRequerimentoController;
	
	@EJB
	private DiscordarReenquadramentoService discordarReenquadramentoService;
	
	
	private ArquivoProcesso arq;
	

	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(
			ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}
	
	
	public void fileUploadListener(FileUploadEvent event) {
		discordarReenquadramentoFile = event.getFile();
		
		if(Util.isNullOuVazio(discordarReenquadramentoFile)) {
			JsfUtil.addErrorMessage("O Upload Documento é obrigatório!");
		}else {
			String caminho = FileUploadUtil.Enviar(event, DiretorioArquivoEnum.PROCESSO.toString());
			Pessoa pessoa = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
			nomeFile = event.getFile().getFileName();
			arq = new ArquivoProcesso(caminho,"Justificativa para não reenquadrar", processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso(), pessoa);
			arq.setCategoriaDocumento(new CategoriaDocumento(CategoriaDocumentoEnum.JUSTIFICATIVA_NAO_REENQUADRAR.getId()));
			arq.setDtcCriacao(new Date());
		}
	}
	
	public void discordarReenquadramento() {
		if(Util.isNullOuVazio(discordarReenquadramentoFile)) {
			JsfUtil.addErrorMessage("O Upload Documento é obrigatório!");
		}else {
			Html.exibir("dlgNaoConcordoConfirm");
		}
	}
	
	public void confirmDiscordarReenquadramento() {
		
		try {
			discordarReenquadramentoService.discordarReenquadramento(processoReenquadramentoDTO,arq);
			inicializarVariaveis();
			FacesContext.getCurrentInstance().getExternalContext().redirect(novoRequerimentoController.URL_PAUTA_REENQUADRAMENTO);
		} catch (Exception e) {
			
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/**
	 * Método criado para verificação de Questões do Requerimento
	 * 
	 */
	public void verificarQuestoes() {
		
	}

	public UploadedFile getDiscordarReenquadramentoFile() {
		return discordarReenquadramentoFile;
	}

	public void setDiscordarReenquadramentoFile(UploadedFile discordarReenquadramentoFile) {
		this.discordarReenquadramentoFile = discordarReenquadramentoFile;
	}
	
	
	/**<p>Verifica o perfil do usuário se <b>Atendente</b> ou <b>Requerente</b> 
	 * para habilitar opção <b>Discordar do Reenquadramento</b></p>
	 * @return boolean
	 * @since 09/08/2019
	 * @author Antoniony
	 * */
	public boolean isRenderedDiscordar() {
		if(isRequerente() || isAtendente()) {
			return true;
		}		
		return false;
	}
	
	public boolean isRequerente() {
		Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
		if(!Util.isNullOuVazio(processoReenquadramentoDTO.getListaPessoaRequerimento())) {
			for(Pessoa p : processoReenquadramentoDTO.getListaPessoaRequerimento()) {
				if(usuarioLogado.getIdePessoaFisica().equals(p.getIdePessoa())) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isAtendente() {
		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado())) {
			return PerfilEnum.ATENDENTE.getId().equals(ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil());
		}
		return false;
	}

	
	public void carregarProcessoReenquadramento(ProcessoReenquadramentoDTO prc) {
		inicializarVariaveis();
		this.processoReenquadramentoDTO = prc;
	}
	
	public void inicializarVariaveis() {
		processoReenquadramentoDTO = null;
		arq = null;
		discordarReenquadramentoFile = null;
		nomeFile = null;
	}
	/**<p>Verifica qual modal deve ser exibido caso o usuario seja requerente 
	 * e tenha realizado modificação em 1 das abas</p>
	 * 
	 * */
	public void showDiscordar() {
		Boolean isRespostaChange = (Boolean) SessaoUtil.recuperarObjetoSessao("isRespostaChange");
		this.arq = null;
		this.discordarReenquadramentoFile = null;
		this.nomeFile = null;
		if(!ContextoUtil.getContexto().getUsuarioLogado().getIndTipoUsuario()
				&& !Util.isNullOuVazio(isRespostaChange) && isRespostaChange) {
			Html.exibir("dlgAlteracaoAba");
		}else {
			Html.exibir("dlgNaoConcordo");
		}
	}
	
	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}
	
}