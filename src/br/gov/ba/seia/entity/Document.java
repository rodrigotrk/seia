package br.gov.ba.seia.entity;

public class Document {
	private String codigo;
	private String name;
	private String cnae;
	
	private String coord;
	
	
	
	public Document(String codigo, String name, String cnae, String coord) {		
		this.codigo = codigo;
		this.name = name;
		this.cnae = cnae;
		
		this.cnae = coord;
		
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCnae() {
		return cnae;
	}


	public void setCnae(String cnae) {
		this.cnae = cnae;
	}


	public String getCoord() {
		return coord;
	}


	public void setCoord(String coord) {
		this.coord = coord;
	}


	
	
	
	
	
	
	
}
