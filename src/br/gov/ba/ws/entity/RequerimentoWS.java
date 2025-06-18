package br.gov.ba.ws.entity;

import java.util.Date;

import br.gov.ba.seia.util.Util;

public class RequerimentoWS {

	private Integer ideRequerimento;
	private String numRequerimento;
	private Date dtcRequerimento;
	private String nomStatusRequerimento;
	private String requerente;
	private Integer ideEmpreendimento;
	private Integer ideRequerente;
	private String cpfCnpj;
	private String nomEmpreendimento;
	

	
	public RequerimentoWS(Integer ideRequerimento, String numRequerimento,Integer ideStatusRequerimento, String nomStatusRequerimento, Date dtcMovimentacao, Integer idePessoaFisica, String nomPessoa,String cpf, Integer idePessoaJuridica, String nomRazaoSocial, String cnpj, Integer ideEmpreendimento, String nomEmpreendimento){
		this.ideRequerimento = ideRequerimento;
		this.numRequerimento = numRequerimento;
		this.nomStatusRequerimento = nomStatusRequerimento;
		this.dtcRequerimento = dtcMovimentacao;
		this.nomEmpreendimento = nomEmpreendimento;
		this.ideEmpreendimento = ideEmpreendimento;
		if(!Util.isNullOuVazio(idePessoaFisica)){
			setIdeRequerente(idePessoaFisica);
			requerente = nomPessoa;
			setCpfCnpj(cpf); 
		}else{
			this.ideRequerente =idePessoaJuridica;
			this.requerente = nomRazaoSocial;
			setCpfCnpj(cnpj);
		}
	}
	
	public RequerimentoWS(String numRequerimento,  Date dtcMovimentacao, String nomPessoa, String nomRazaoSocial, String nomEmpreendimento, Integer ideRequerimento){
		this.numRequerimento = numRequerimento;
		this.dtcRequerimento = dtcMovimentacao;
		this.nomEmpreendimento = nomEmpreendimento;
		this.ideRequerimento = ideRequerimento;
		if(!Util.isNullOuVazio(nomPessoa)){
			requerente = nomPessoa;
		}else{
			this.requerente = nomRazaoSocial;
		}
	}
	
	public RequerimentoWS(String numRequerimento, String nomEmpreendimento,
			Date dtcCriacao, Integer ideRequerimento, Integer ideEmpreendimento) {
		this.numRequerimento = numRequerimento;
		this.nomEmpreendimento = nomEmpreendimento;
		this.dtcRequerimento = dtcCriacao;
		this.ideRequerimento = ideRequerimento;
		this.ideEmpreendimento = ideEmpreendimento;
		
	}
	
	public RequerimentoWS(Integer ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public Integer getIdeRequerimento() {
		return ideRequerimento;
	}
	public void setIdeRequerimento(Integer ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}
	public String getNumRequerimento() {
		if(Util.isNullOuVazio(numRequerimento)){
			return "-";
		}
		return numRequerimento;
	}
	public void setNumRequerimento(String numRequerimento) {
		this.numRequerimento = numRequerimento;
	}
	public Date getDtcRequerimento() {
		return dtcRequerimento;
	}
	public void setDtcRequerimento(Date dtcRequerimento) {
		this.dtcRequerimento = dtcRequerimento;
	}
	public String getNomStatusRequerimento() {
		return nomStatusRequerimento;
	}
	public void setNomStatusRequerimento(String nomStatusRequerimento) {
		this.nomStatusRequerimento = nomStatusRequerimento;
	}
	public String getRequerente() {
		return requerente;
	}
	public void setRequerente(String requerente) {
		this.requerente = requerente;
	}
	public Integer getIdeEmpreendimento() {
		return ideEmpreendimento;
	}
	public void setIdeEmpreendimento(Integer ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}
	public String getNomEmpreendimento() {
		if(!Util.isNullOuVazio(nomEmpreendimento)){
			return nomEmpreendimento;
		}
		return "-";
	}
	public void setNomEmpreendimento(String nomEmpreendimento) {
		this.nomEmpreendimento = nomEmpreendimento;
	}

	public Integer getIdeRequerente() {
		return ideRequerente;
	}

	public void setIdeRequerente(Integer ideRequerente) {
		this.ideRequerente = ideRequerente;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
}
