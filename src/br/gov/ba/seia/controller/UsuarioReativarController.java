package br.gov.ba.seia.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.dto.UsuarioExternoDTO;
import br.gov.ba.seia.entity.Escolaridade;
import br.gov.ba.seia.entity.EstadoCivil;
import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.entity.Pais;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.UsuarioReativarService;
import br.gov.ba.seia.service.UsuarioService;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SenhaUtil;
import br.gov.ba.seia.util.Util;

@Named("usuarioReativarController")
	@ViewScoped
public class UsuarioReativarController extends SeiaControllerAb {

	private String cpf;
	private String nome;
	private String dscEmail;
	private UsuarioExternoDTO usuarioReativar;
	private List<PessoaFisica> modelPessoasFisica;
	
	
	private UIForm formularioASerLimpo;
	
	private boolean desabilitaCampos = true;
	private boolean editando;

	
	@EJB
	private UsuarioReativarService usuarioReativarService;
 	@EJB
	private PessoaFisicaService pessoaFisicaService;
 	@EJB
 	private UsuarioService usuarioService;
 	
	private Date dataAtual = new Date();
	
	public UsuarioReativarController() {
	}

	public void consultarCpf() {
		if (!editando) {
			if (!cpfCadastrado()) {
				// habilita os campos
				desabilitaCampos = false;
			} else {
				// emite mensagem de erro
				desabilitaCampos = true;
				if (!Util.isNull(usuarioReativar.getPessoaFisica().getNumCpf())) {
					JsfUtil.addErrorMessage("CPF já cadastrado favor entrar em contato com a ATEND");
				}
			}
		}
	}
	
	public boolean cpfCadastrado() {
		Exception erro = null;
		try {
			
			PessoaFisica lPessoaFisica = pessoaFisicaService.filtrarPessoaFisicaByCpf(usuarioReativar.getPessoaFisica());
			if(Util.isNullOuVazio(lPessoaFisica)) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return true;
	}
	
	public void pesquisarPessoasFisica() {
		try {
			modelPessoasFisica = (List<PessoaFisica>) usuarioReativarService.filtrarListaPessoasFisicasExcluidas(new PessoaFisica(nome, cpf));
		}
		catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	
	

	public void limparTela() {
		desabilitaCampos = false;
		editando = true;
		limparComponentesFormulario(formularioASerLimpo);
	}
	
	public void reativarUsuario(){
		try {
			usuarioReativar.getPessoaFisica().getUsuario().setIndExcluido(false);
			usuarioReativar.getPessoaFisica().getUsuario().setDtcExclusao(null);
			
			usuarioReativarService.atualizarUsuario(usuarioReativar);
			
		}
			catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	
	

	public void reativarUsuarioExterno() {
		try {

			PessoaFisica pessoaFisica = pessoaFisicaService
					.buscarPessoaFisicaByCPF(new PessoaFisica(cpf));

			if (!Util.isNullOuVazio(pessoaFisica) && pessoaFisica.getPessoa().getDesEmail().equals(dscEmail)) {
				pessoaFisica.setUsuario(usuarioService
						.buscarUsuarioPorPessoaFisica(pessoaFisica));

				usuarioReativar = new UsuarioExternoDTO(pessoaFisica,
						pessoaFisica.getUsuario());

				if (!usuarioReativar.getUsuario().getIndTipoUsuario()) {

					usuarioReativar.getPessoaFisica().getUsuario()
							.setIndExcluido(false);
					usuarioReativar.getPessoaFisica().getUsuario()
							.setDtcExclusao(null);
					String senha = SenhaUtil.gerarSenha(8);
					usuarioReativar.getUsuario().setDscSenha(Util.toMD5(senha));
					usuarioReativarService.atualizarUsuario(usuarioReativar);
					enviarEmailReativacao(usuarioReativar,usuarioReativar.getPessoaFisica()
							.getPessoa().getDesEmail(),senha);
					MensagemUtil.sucesso("Usuário reativado com sucesso!");
				} else {
					MensagemUtil.erro("Para reativar usuário interno entre em contato com o service desk.");
				}
			} else {
				MensagemUtil.erro("CPF ou email não encontrado!");
			}
		} catch (Exception exception) {
			MensagemUtil.erro(exception.getMessage());
		}

	}
	protected void enviarEmailReativacao(UsuarioExternoDTO user, 
			String email,String senha) throws Exception{
		EmailUtil sendEmail = new EmailUtil();
		StringBuilder lMsg = new StringBuilder();
		lMsg.append("<html><body>")
		.append("Prezado(a) "+user.getPessoaFisica().getNomPessoa()+ ", saudações!<br><br>")
		.append("<p>Seu login de acesso ao sistema SEIA foi reativado com sucesso.<br><br>")
		.append(" Por questões de segurança, a sua senha foi modificada automaticamente pelo sistema. Abaixo segue a sua nova senha:<br><br>")
		.append("Senha:<b> "+senha+"</b><br><br>")
		.append("Recomendamos que após o seu primeiro acesso proceda a troca da senha.<br><br>")
		.append("Acesse novamente o sistema através do endereço abaixo utilizando a senha fornecida. </p><br>")
		.append("<a href='http://sistema.seia.ba.gov.br'>http://sistema.seia.ba.gov.br</a> <br><br>")
		.append("Atenciosamente,<br><br>")
		.append("<b>Central de Atendimento - INEMA.</b>")
		.append("</body></html>");
		//.append("\n\n Após o seu primeiro acesso com sucesso utilizando a senha acima,")
		//.append("o sistema imediatamente solicitará que você redefina sua senha para uma de sua preferência, ")
		//.append("obedecendo o seguinte critério: mínimo de 8 caracteres, pelo menos um caractere maiúsculo,")
		//.append(" pelo menos um caractere especial e pelo menos um numeral \n Atenciosamente,");
		try{
			
			sendEmail.enviarEmailHtml(email,
				"Reativação de Usuário", lMsg.toString());
		}catch(Exception ex){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, ex);
			throw ex;
		}
	}
	public void novoUsuarioExterno() {

		setDscEmail(null);
		setCpf(null);
			
		limparTela();
	}
	
	
	

	public Collection<Escolaridade> getColEscolaridades() {
    	return usuarioReativarService.filtrarListaEscolaridades(null);
    	
    }
	
	public Collection<EstadoCivil> getColEstadosCivil() {
    	return usuarioReativarService.filtrarListaEstadosCivil(null);
    }
	
	public Collection<Ocupacao> getColOcupacoes() {
    	return usuarioReativarService.filtrarListaOcupacoes(null);
    }
	
	public Collection<Pais> getColPaises() {

    	return usuarioReativarService.filtrarListaPaises(null);
    }
	
	public List<PessoaFisica> getModelPessoasFisica() {

		if (Util.isNull(modelPessoasFisica)) {

			try {
				modelPessoasFisica = (List<PessoaFisica>) usuarioReativarService.filtrarListaPessoasFisicasExcluidas(new PessoaFisica(nome, cpf));
								
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}
		return modelPessoasFisica;
	}
	
	public PessoaFisica carregarPessoaFisica(){
		
		try{
			return usuarioReativarService.carregarPessoaFisica(usuarioReativar.getPessoaFisica());		
		}
		catch (Exception e) {
			return null;
		}		
		
	}
	
	public void setModelPessoasFisica(List<PessoaFisica> modelPessoasFisica) {
		this.modelPessoasFisica = modelPessoasFisica;
	}

	public UsuarioExternoDTO getUsuarioReativar() {
		if (Util.isNull(usuarioReativar)) usuarioReativar = new UsuarioExternoDTO();

		return usuarioReativar;
	}
	
	public void setUsuarioReativar(UsuarioExternoDTO usuarioExterno) {
		this.usuarioReativar = usuarioExterno;
	}

	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getDscEmail() {
		return dscEmail;
	}

	public void setDscEmail(String dscEmail) {
		this.dscEmail = dscEmail;
	}

	public UIForm getFormularioASerLimpo() {
		return formularioASerLimpo;
	}
	public void setFormularioASerLimpo(UIForm formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}
	public boolean isDesabilitaCampos() {
		return desabilitaCampos;
	}
	public void setDesabilitaCampos(boolean desabilitaCampos) {
		this.desabilitaCampos = desabilitaCampos;
	}

	public Date getDataAtual() {
		return dataAtual;
	}
	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}

	public boolean getEditando() {
		return editando;
	}
}