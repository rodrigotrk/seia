package br.gov.ba.seia.middleware.seia.model;
/**
 * Classe modelo para requerimento
 * @author 
 *
 */
public class Requerimento {

	private Integer ideRequerimento;

	private String numRequerimento;

	private Area ideArea;

	public Requerimento() {

	}

	public Integer getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Integer ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public String getNumRequerimento() {
		return numRequerimento;
	}

	public void setNumRequerimento(String numRequerimento) {
		this.numRequerimento = numRequerimento;
	}

	public Area getIdeArea() {
		return ideArea;
	}

	public void setIdeArea(Area ideArea) {
		this.ideArea = ideArea;
	}
}
