package br.gov.ba.seia.dto;


public class ImovelRuralCdaDTO {
	
	private String nomProprietarioRequerente;
	private String cpfCnpj;
	private Integer numProcesso;
	private String nomImovel;
	private Double valAreaDeclarada;
	private Double codIbgeMunicipio;
	private Integer ideLocalizacaoGeografica;
	private String dscObservacao;
	private boolean indRejeitado;
	
	
	public ImovelRuralCdaDTO () {
		
	}


	public String getNomProprietarioRequerente() {
		return nomProprietarioRequerente;
	}


	public void setNomProprietarioRequerente(String nomProprietarioRequerente) {
		this.nomProprietarioRequerente = nomProprietarioRequerente;
	}


	public String getCpfCnpj() {
		return cpfCnpj;
	}


	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}


	public Integer getNumProcesso() {
		return numProcesso;
	}


	public void setNumProcesso(Integer numProcesso) {
		this.numProcesso = numProcesso;
	}


	public String getNomImovel() {
		return nomImovel;
	}


	public void setNomImovel(String nomImovel) {
		this.nomImovel = nomImovel;
	}


	public Double getValAreaDeclarada() {
		return valAreaDeclarada;
	}


	public void setValAreaDeclarada(Double valAreaDeclarada) {
		this.valAreaDeclarada = valAreaDeclarada;
	}


	public Double getCodIbgeMunicipio() {
		return codIbgeMunicipio;
	}


	public void setCodIbgeMunicipio(Double codIbgeMunicipio) {
		this.codIbgeMunicipio = codIbgeMunicipio;
	}


	public Integer getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}


	public void setIdeLocalizacaoGeografica(Integer ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}


	public String getDscObservacao() {
		return dscObservacao;
	}


	public void setDscObservacao(String dscObservacao) {
		this.dscObservacao = dscObservacao;
	}


	public boolean isIndRejeitado() {
		return indRejeitado;
	}


	public void setIndRejeitado(boolean indRejeitado) {
		this.indRejeitado = indRejeitado;
	}	
}
