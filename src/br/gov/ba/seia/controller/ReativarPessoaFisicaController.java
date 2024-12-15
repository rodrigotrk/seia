package br.gov.ba.seia.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.faces.component.UIForm;
import javax.inject.Named;

import br.gov.ba.seia.dto.UsuarioExternoDTO;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.PessoaFisicaJuridicaReativarService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;




@Named("reativarPessoaFisicaController")
@ViewScoped

public class ReativarPessoaFisicaController extends SeiaControllerAb {

	private String cpf;
	private String nome;
	private List<PessoaFisica> modelPessoasFisica;
	private UsuarioExternoDTO usuarioReativar;
	
	private boolean desabilitaCampos = true;
	private boolean editando;
	
	private UIForm formularioASerLimpo;
	
	@EJB
	private PessoaFisicaJuridicaReativarService pessoaFisicaJuridicaReativarService;
	
	public ReativarPessoaFisicaController() {
	}
	
	public void pesquisarPessoasFisica() {
		try {
			modelPessoasFisica = (List<PessoaFisica>) pessoaFisicaJuridicaReativarService.filtrarListaPessoasFisicasExcluidas(new PessoaFisica(nome, cpf));
		}
		catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	public List<PessoaFisica> getModelPessoasFisica() {

		if (Util.isNull(modelPessoasFisica)) {

			try {
				modelPessoasFisica = (List<PessoaFisica>) pessoaFisicaJuridicaReativarService.filtrarListaPessoasFisicasExcluidas(new PessoaFisica(nome, cpf));
								
			}
			catch (Exception exception) {

				JsfUtil.addErrorMessage(exception.getMessage());
			}
		}
		return modelPessoasFisica;
	}
	
	
	public void limparTela() {
		desabilitaCampos = false;
		editando = true;
		limparComponentesFormulario(formularioASerLimpo);
	}
	
	public void reativarUsuario(){
		try {
			
			pessoaFisicaJuridicaReativarService.atualizarPessoa(usuarioReativar);
			
		}
			catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	public UsuarioExternoDTO getUsuarioReativar() {
		if (Util.isNull(usuarioReativar)) usuarioReativar = new UsuarioExternoDTO();

		return usuarioReativar;
	}
	
	public void setUsuarioReativar(UsuarioExternoDTO usuarioExterno) {
		this.usuarioReativar = usuarioExterno;
	}
	
	
	
	public void setModelPessoasFisica(List<PessoaFisica> modelPessoasFisica) {
		this.modelPessoasFisica = modelPessoasFisica;
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

	public boolean getEditando() {
		return editando;
	}
	
}
