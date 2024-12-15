package br.gov.ba.seia.exception;
/**
 * Tratamento de exceção ao inserir area declarada invalida
 * @author 
 *
 */
public class AreaDeclaradaInvalidaException extends Exception {
	private static final long serialVersionUID = 2243472078005170161L;
	
	private Double areaDeclarada;
	private Double areaCalculada;
	
	public AreaDeclaradaInvalidaException(Double areaDeclarada, Double areaCalculada) {
		this.areaDeclarada = areaDeclarada;
		this.areaCalculada = areaCalculada;
	}
	
	public AreaDeclaradaInvalidaException(String mensagem) {
		super(mensagem);
	}

	public Double getAreaDeclarada() {
		return areaDeclarada;
	}

	public void setAreaDeclarada(Double areaDeclarada) {
		this.areaDeclarada = areaDeclarada;
	}

	public Double getAreaCalculada() {
		return areaCalculada;
	}

	public void setAreaCalculada(Double areaCalculada) {
		this.areaCalculada = areaCalculada;
	}

}
