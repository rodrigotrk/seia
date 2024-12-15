package br.gov.ba.seia.controller;

import javax.ejb.EJB;
import javax.faces.component.UIForm;

import org.apache.log4j.Level;
import org.primefaces.model.LazyDataModel;

import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

public abstract class CadastrarUsuario extends SeiaControllerAb {

	@EJB
	protected PessoaFisicaService pessoaFisicaService;
	
	protected String cpf;
	protected String nome;
	protected UIForm formularioASerLimpo;
	protected LazyDataModel<PessoaFisica> modelPessoasFisica;
	protected boolean editando;
	protected boolean desabilitaCampos = true;
	
	public abstract boolean validarLogin() ;
	public abstract boolean isCPFcadastrado();
	public abstract void salvarAtualizarUsuario();
	public abstract void novoUsuario();
	
	public void consultarCpf() {
		try {
			if (!isCPFcadastrado()) {
				desabilitaCampos = false;
			} 
			else {
				desabilitaCampos = true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void limparTela(){
		editando = false;
		limparComponentesFormulario(formularioASerLimpo);
	}
	
	public String getDataAtual() {
		return Util.getDataHoje();
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
	public boolean isEditando() {
		return editando;
	}
	public void setEditando(boolean editando) {
		this.editando = editando;
	}
	public boolean isDesabilitaCampos() {
		return desabilitaCampos;
	}
	public void setDesabilitaCampos(boolean desabilitaForm) {
		this.desabilitaCampos = desabilitaForm;
	}
	public LazyDataModel<PessoaFisica> getModelPessoasFisica() {
		return modelPessoasFisica;
	}
	public void setModelPessoasFisica(LazyDataModel<PessoaFisica> modelPessoasFisica) {
		this.modelPessoasFisica = modelPessoasFisica;
	}
}