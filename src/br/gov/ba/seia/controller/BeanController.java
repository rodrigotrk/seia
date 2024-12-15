package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import br.gov.ba.seia.entity.PessoaFisica;


@Named
@ViewScoped
public class BeanController {
	private DataModel<PessoaFisica> modelPessoas;
	private Boolean editMode = Boolean.TRUE;
	private PessoaFisica pessoaSelecionada = new PessoaFisica();
	List<PessoaFisica> listaPessoas;
		
	//@SuppressWarnings("unused")
	public BeanController(){
		
	}
	
	public void delete(){

		listaPessoas.remove(pessoaSelecionada);
		JsfUtil.addSuccessMessage(String.valueOf(listaPessoas.size()));

	}
	
	@PostConstruct
	private void loadModelPessoas(){
		listaPessoas = new ArrayList<PessoaFisica>();
		PessoaFisica pf = new PessoaFisica();
		pf.setIdePessoaFisica(1);
		pf.setNomPessoa("Carlos");
		pf.setNumCpf("02599501571");
		
		PessoaFisica pf2 = new PessoaFisica();
		pf2.setIdePessoaFisica(2);
		pf2.setNomPessoa("Monique");
		pf2.setNumCpf("12345678998");
		listaPessoas.add(pf);
		listaPessoas.add(pf2);
		this.modelPessoas = new ListDataModel<PessoaFisica>(listaPessoas);	
	}
		
	public DataModel<PessoaFisica> getModelPessoas() {
		return modelPessoas;
	}

	public void setModelPessoas(DataModel<PessoaFisica> modelPessoas) {
		this.modelPessoas = modelPessoas;
	}

	public PessoaFisica getPessoaSelecionada() {
		return pessoaSelecionada;
	}

	public void setPessoaSelecionada(PessoaFisica pessoaSelecionada) {
		this.pessoaSelecionada = pessoaSelecionada;
	}

	
	public Boolean getEditMode() {
		return editMode;
	}

	public void setEditMode(Boolean editMode) {
		this.editMode = editMode;
	}
	
}

