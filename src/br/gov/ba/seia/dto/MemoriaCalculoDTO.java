package br.gov.ba.seia.dto;

public class MemoriaCalculoDTO {
	
	private String nomTipo;
	
	private String formula;

	private boolean rendered = Boolean.TRUE;
	
	public MemoriaCalculoDTO (String nomTipo, String formula) {
		this.nomTipo = nomTipo;
		this.formula = formula;
	}
	
	public String getNomTipo() {
		return nomTipo;
	}

	public void setNomTipo(String nomTipo) {
		this.nomTipo = nomTipo;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public boolean isRendered() {
		return rendered;
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}
	
	
}
