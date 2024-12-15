package br.gov.ba.seia.entity;

public class Requerente{
	private String nomeRazaoSocial;
	private String cpf;
	
	public Requerente() {
	}
	
	public Requerente(String nomeRazaoSocial, String cpf){
		this.nomeRazaoSocial = nomeRazaoSocial;
		this.cpf = cpf;
	}
	
	public String getNomeRazaoSocial() {
		return nomeRazaoSocial;
	}
	public void setNomeRazaoSocial(String nomeRazaoSocial) {
		this.nomeRazaoSocial = nomeRazaoSocial;
	}


	public String getCpf() {
		return cpf;
	}


	public void setCpf(String cpf) {
		this.cpf = cpf;
	}


	

	

	


		
	
}