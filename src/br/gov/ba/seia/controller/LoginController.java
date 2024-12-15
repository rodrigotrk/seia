package br.gov.ba.seia.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.service.UsuarioService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("loginController")
@RequestScoped
public class LoginController extends SeiaControllerAb {

	@EJB
	private UsuarioService usuarioService;

	private String loginUsuario;
	private boolean exibePanel = true;

	public LoginController() {
	}
	
	@PostConstruct
	public void init() {
		
		Boolean ativarUsuario = Boolean.FALSE;
		Boolean cadastroUsuario = Boolean.FALSE;
		String booleanUsuario = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("ativarUsuario");
		String booleanCadastroUsuario = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("cadastroConcluido");
		
		if(booleanUsuario != null){
			ativarUsuario = Boolean.valueOf(booleanUsuario);
		}
		
		if(booleanCadastroUsuario != null){
			cadastroUsuario = Boolean.valueOf(booleanCadastroUsuario);
		}
		
		if(ativarUsuario){
			String usuario = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("loginUsuario");			
			if(usuario != null){
				usuario = Util.decrypt(Util.decodeBase64(usuario.substring(64).replace(" ", "+")), Util.CHAVEENCRIPITACAO) ;
				Usuario user = new Usuario();	
				user.setIdePessoaFisica(Integer.valueOf(usuario));
				ativarUsuario(user);
			}
		}
		
		if(cadastroUsuario){
			String msg = (String) SessaoUtil.recuperarRemoverObjetoSessao("msgCadastroConcluido");			
			JsfUtil.addSuccessMessage(msg);
		}		
		
	}
		
	public void ativarUsuario(Usuario usuario) {

		if (!Util.isNullOuVazio(usuario)) {
			try {
				usuario = usuarioService.ativarUsuario(usuario);
				this.addMensagemInformativa("O usuário " + usuario.getDscLogin() + " foi ativado.", null);
			}catch (Exception e) {
				this.addMensagemErro(e.getMessage(), null);
			}
		}else {
			this.addMensagemErro("O usuário não foi informado.", null);
		}

		setExibePanel(false);
	}
	
	public String getLoginUsuario() {
		return loginUsuario;
	}
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public boolean isExibePanel() {
		return exibePanel;
	}
	public void setExibePanel(boolean exibePanel) {
		this.exibePanel = exibePanel;
	}
	public String getCaptchaPublicKey() {
		return ConfigEnum.RECAPTCHA_PUBLIC_KEY.toString();
	}
}