package br.gov.ba.seia.enumerator;

/**
 * Enum para Secao Geometrica.
 * 
 * @since 04/10/17
 * @author rafael.nascimento
 */
public enum SecaoGeometricaEnum {

	TRAPEZOIDAL(1),
	RETANGULAR(2),
	CIRCULAR(3),
	OUTROS(4);
	
	private Integer id;

	private SecaoGeometricaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
