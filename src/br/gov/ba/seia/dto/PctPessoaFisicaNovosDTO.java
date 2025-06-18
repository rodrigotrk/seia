package br.gov.ba.seia.dto;

import java.util.ArrayList;
import java.util.Collection;

import br.gov.ba.seia.entity.PessoaFisica;

public class PctPessoaFisicaNovosDTO {

	Collection<PessoaFisica> pessoaFisicaRepsNovos;
	
	Collection<PessoaFisica> pessoaFisicaMembrosNovos;
	
	Collection<String> listaCpfArquivo;
	
	public Collection<PessoaFisica> getPessoaFisicaRepsNovos() {
		return pessoaFisicaRepsNovos;
	}
	public void setPessoaFisicaRepsNovos(Collection<PessoaFisica> pessoaFisicaRepsNovos) {
		this.pessoaFisicaRepsNovos = pessoaFisicaRepsNovos;
	}
	public Collection<PessoaFisica> getPessoaFisicaMembrosNovos() {
		return pessoaFisicaMembrosNovos;
	}
	public void setPessoaFisicaMembrosNovos(Collection<PessoaFisica> pessoaFisicaMembrosNovos) {
		this.pessoaFisicaMembrosNovos = pessoaFisicaMembrosNovos;
	}
	public Collection<String> getListaCpfArquivo() {
		return listaCpfArquivo;
	}
	public void setListaCpfArquivo(Collection<String> listaCpfArquivo) {
		this.listaCpfArquivo = listaCpfArquivo;
	}
	
	public PctPessoaFisicaNovosDTO() {
		this.pessoaFisicaRepsNovos = new ArrayList<PessoaFisica>();
		this.pessoaFisicaMembrosNovos = new ArrayList<PessoaFisica>();
		this.listaCpfArquivo = new ArrayList<String>();
	}

	public PctPessoaFisicaNovosDTO(Collection<PessoaFisica> pessoaFisicaRepsNovos,
			Collection<PessoaFisica> pessoaFisicaMembrosNovos) {
		super();
		this.pessoaFisicaRepsNovos = pessoaFisicaRepsNovos;
		this.pessoaFisicaMembrosNovos = pessoaFisicaMembrosNovos;
	}
	
	public PctPessoaFisicaNovosDTO(Collection<PessoaFisica> pessoaFisicaRepsNovos,
			Collection<PessoaFisica> pessoaFisicaMembrosNovos, Collection<String> listaCpfArquivo) {
		super();
		this.pessoaFisicaRepsNovos = pessoaFisicaRepsNovos;
		this.pessoaFisicaMembrosNovos = pessoaFisicaMembrosNovos;
		this.listaCpfArquivo = listaCpfArquivo;
	}
	
	
	

	
}
