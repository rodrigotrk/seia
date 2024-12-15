package br.gov.ba.seia.enumerator;

public enum TipoEspecieFlorestalEnum {

	NATIVA(1),
	EXOTICA(2);
	
	private int id;
	
	private TipoEspecieFlorestalEnum(int id){
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
