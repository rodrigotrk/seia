package br.gov.ba.seia.enumerator;

public enum TecnicaLavraEnum {

	DRAGAGEM(5, "Dragagem"),
	OUTROS(9, "Outros"); 

	private Integer ideTecnicaLavra;
	private String nomTecnicaLavra;
	
	private TecnicaLavraEnum(Integer ideTecnicaLavra, String nomTecnicaLavra) {
		this.ideTecnicaLavra = ideTecnicaLavra;
		this.nomTecnicaLavra = nomTecnicaLavra;
	}
	public Integer getIdeTecnicaLavra() {
		return ideTecnicaLavra;
	}
	public void setIdeTecnicaLavra(Integer ideTecnicaLavra) {
		this.ideTecnicaLavra = ideTecnicaLavra;
	}
	public String getNomTecnicaLavra() {
		return nomTecnicaLavra;
	}
	public void setNomTecnicaLavra(String nomTecnicaLavra) {
		this.nomTecnicaLavra = nomTecnicaLavra;
	}

}
