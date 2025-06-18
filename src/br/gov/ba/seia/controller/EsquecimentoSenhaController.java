package br.gov.ba.seia.controller;

import java.util.Collection;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIForm;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.UsuarioExternoDTO;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.service.EsquecimentoSenhaService;
import br.gov.ba.seia.service.RecuperarLoginService;
import br.gov.ba.seia.service.UsuarioExternoService;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named
@RequestScoped
public class EsquecimentoSenhaController extends SeiaControllerAb {

	private UIForm formularioASerLimpo;

	private String login;
	private String cpf;
	
	private UsuarioExternoDTO usuarioExterno;

	@EJB
	EsquecimentoSenhaService esquecimentoSenhaService;
	@EJB
	private UsuarioExternoService usuarioExternoService;
	@EJB
	private RecuperarLoginService recuperarLoginService;  

	public EsquecimentoSenhaController() {}

	public void novoEsquecimentoSenha() {

		setLogin(null);
		setCpf(null);
		
		
		limparTela();
	}
	public void limparTela() {

		limparComponentesFormulario(formularioASerLimpo);
		usuarioExterno = new UsuarioExternoDTO();

	}

	public void enviarEsquecimentoSenha() {

		try {

			PessoaFisica lPessoaFisica = esquecimentoSenhaService.enviarEsquecimentoSenha(new PessoaFisica(getCpf(), new Usuario(getLogin())));

			this.addMensagemInformativa("A nova senha foi enviada com sucesso para " + lPessoaFisica.getNomPessoa() + ".", null);
		}
		catch (Exception exception) {

			JsfUtil.addErrorMessage(exception.getMessage());
			getRequestContext().addCallbackParam("validationFailed", true);
		}
	}
	
	public void enviarLoginUsuario(){
		
		try {
			
			if(this.validacaoEnvioLogin()){
			PessoaFisica pessoaF = recuperarLoginService.recuperarLoginService(cpf,usuarioExterno.getPessoaFisica().getPessoa().getDesEmail());
			
			if(!Util.isNullOuVazio(pessoaF)){
				
				EmailUtil emailUtil = new EmailUtil();
				emailUtil.enviarEmailHtml(pessoaF.getPessoa().getDesEmail(), "Recuperação de login", getMessage(pessoaF.getUsuario().getDscLogin()));
				MensagemUtil.sucesso("Login enviado para seu e-mail");
				Html.esconder("dlgEsqueciUsuario");
			}else{
				MensagemUtil.erro("CPF ou e-mail não encontrado");
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		
	}
	
	private boolean validacaoEnvioLogin(){
		boolean isValido = true;
		if(cpf.isEmpty()){
			isValido = false;
			MensagemUtil.erro("O campo CPF é de preenchimento obrigatório.");
		}
		if(usuarioExterno.getPessoaFisica().getPessoa().getDesEmail().isEmpty()){
			isValido = false;
			MensagemUtil.erro("O campo e-mail é de preenchimento obrigatório.");
		}
		
		return isValido;
	}
	
	
	
	public String getMessage(String login){
		StringBuilder mensagem = new StringBuilder();
		
		mensagem.append("Prezado(a),");
		mensagem.append("<br />Seu login de acesso ao sistema Seia de acordo com seus dados cadastrados é: " + "<b>"+login+"</b>");
		mensagem.append("<br /> <br /> Atte.,");
		mensagem.append("<br />Central de Atendimento/INEMA.");
		
		return mensagem.toString();
	}
	
	
	public void reenviarEmailUsuario() {
		try {
			if (this.validacaoReenvioEmail()) {
				Collection<Usuario> usuarios = usuarioExternoService
						.listarPorEmailCPF(usuarioExterno);
				if (!usuarios.isEmpty()) {
					for (Usuario lUsuario : usuarios) {
						usuarioExterno.setUsuario(lUsuario);
						usuarioExternoService.enviarEmail(usuarioExterno
								.getUsuario().getPessoaFisica().getPessoa()
								.getDesEmail(), usuarioExterno.getUsuario().getPessoaFisica().getNomPessoa(), usuarioExterno.getUsuario()
								.getPessoaFisica().getIdePessoaFisica()
								.toString(), JsfUtil.getIpExterno(), usuarioExterno.getUsuario().getDscLogin());
					}
					JsfUtil.addSuccessMessage("E-mail reenviado com sucesso. Você receberá no seu e-mail um link para ativar sua conta.");
				} else {
					JsfUtil.addWarnMessage("O CPF e ou endereço de email não foi(ram) encontrado(s) no sistema pode(m) ter sido digitado(s) de forma incorreta ou o usuário referente aos dados já deve estar ativado, favor entrar em contato com a Central de Atendimento para maiores informações.");
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	
	private boolean validacaoReenvioEmail() {
		Boolean isValido = true;
		if (usuarioExterno.getPessoaFisica().getPessoa().getDesEmail()
				.isEmpty()) {
			isValido = false;
			JsfUtil.addErrorMessage("O campo Email é de preenchimento obrigatório.");
		}
		if (usuarioExterno.getPessoaFisica().getNumCpf().isEmpty()) {
			isValido = false;
			JsfUtil.addErrorMessage("O campo CPF é de preenchimento obrigatório.");
		}
		if (!isValido) {
			getRequestContext().addCallbackParam("validationFailed", true);
		}
		return isValido;
	}
	

	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}

	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}

	public UsuarioExternoDTO getUsuarioExterno() {
		if(Util.isNullOuVazio(usuarioExterno)){
			usuarioExterno = new UsuarioExternoDTO();
		}
		return usuarioExterno;
	}

	public void setUsuarioExterno(UsuarioExternoDTO usuarioExterno) {
		this.usuarioExterno = usuarioExterno;
	}
	
	
	
}