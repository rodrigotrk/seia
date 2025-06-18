package br.gov.ba.seia.exception;
/**
 * Tratamento de exceção para sobreposição de area
 * @author 
 *
 */
public class SobreposicaoAreasException extends Exception {
	
	private static final long serialVersionUID = -111590653317740148L;
	
	private Double percentualSobreposicao;
	
	private String theGeom;
	
	public SobreposicaoAreasException() {
		
	}

	public SobreposicaoAreasException(Double percentual) {
		this.percentualSobreposicao = percentual;
	}

	public SobreposicaoAreasException(String theGeom) {
		this.theGeom = theGeom;
	}
	
	public Double getPercentualSobreposicao() {
		return percentualSobreposicao;
	}
	public void setPercentualSobreposicao(Double percentualSobreposicao) {
		this.percentualSobreposicao = percentualSobreposicao;
	}

	public String getTheGeom() {
		return theGeom;
	}
}
