package br.gov.ba.seia.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIForm;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.service.AlterarSenhaService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityController;
import br.gov.ba.seia.util.security.SecurityService;

@Named
@RequestScoped
public class AlterarSenhaController extends SeiaControllerAb {

	private UIForm formularioASerLimpo;

	private Usuario usuario;
	
	@EJB
	private AlterarSenhaService alterarSenhaService;
	
	public AlterarSenhaController() {}

	public void novaAlteracaoSenha() {

		setUsuario(new Usuario());

		limparTela();
	}
	
	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
	}

	public void alterarSenha() {
		
		try {
			Usuario usuarioLogado = SecurityService.getUser();
			
			if(usuarioLogado != null && usuarioLogado.getDscSenha() != null 
					&& usuario != null && usuario.getSenhaAntiga() != null
					&& !usuarioLogado.getDscSenha().equals(Util.toMD5(usuario.getSenhaAntiga()))){
				
				JsfUtil.addErrorMessage("Senha antiga incorreta.");
				novaAlteracaoSenha();
				return;
			}
			
			if (usuario != null && usuario.getSenhaAntiga() != null 
					&& usuario.getDscSenha().equals(usuario.getDscConfirmacaoSenha())) {
				
				if(usuarioLogado != null && usuarioLogado.getDscSenha() != null 
						&& usuario != null && usuario.getSenhaAntiga() != null
						&& usuarioLogado.getDscSenha().equals(Util.toMD5(usuario.getDscSenha()))){
					
					JsfUtil.addErrorMessage("A nova senha não pode ser igual a senha antiga.");
					novaAlteracaoSenha();
					return;
				}
				
				
				Matcher m = Pattern.compile("[!@#$%&*?/]{1,}").matcher(usuario.getDscSenha()); 
				Matcher m2 = Pattern.compile("[A-Z]{1,}").matcher(usuario.getDscSenha());
				Matcher m3 = Pattern.compile("[0-9]{1,}").matcher(usuario.getDscSenha());
				if(!m.find() || !m2.find() || !m3.find()){
					JsfUtil.addErrorMessage("Sua senha deve conter pelo menos um caractere especial(!@#$%&*?/), uma letra maiúscula, um numeral e no mínimo de 8 caracteres.");
					novaAlteracaoSenha();
					return;
				}
				try {
					
					usuario.setIdePessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
					
					alterarSenhaService.alterarSenha(usuario);
					
					novaAlteracaoSenha();
					
					SecurityController sc = (SecurityController) SessaoUtil.recuperarManagedBean("#{security}", SecurityController.class);
					
					if(sc != null && sc.isAlteracaoSenhaExpirada()) {
						JsfUtil.addSuccessMessage("A senha foi alterada com sucesso, favor sair do sistema e efetuar login novamente.");
					} else {
						JsfUtil.addSuccessMessage("A senha foi alterada com sucesso.");
					}
				} catch (Exception exception) {

					JsfUtil.addErrorMessage("Não foi possível alterar a senha do usuário logado no sistema.");
					getRequestContext().addCallbackParam("validationFailed", true);
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
				}
			} else {
				JsfUtil.addErrorMessage("A confirmação da senha está incorreta.");
				getRequestContext().addCallbackParam("validationFailed", true);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public Usuario getUsuario() {

		if (Util.isNullOuVazio(usuario)) usuario = new Usuario();
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}
	
	public String cancelar(){
		return "/home.xhtml";
	}
	
}