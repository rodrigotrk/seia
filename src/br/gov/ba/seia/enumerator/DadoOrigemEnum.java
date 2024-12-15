package br.gov.ba.seia.enumerator;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 */
public enum DadoOrigemEnum {

	REQUERIMENTO(1),
	TECNICO(2),
	REENQUADRAMENTO(3)
	;

	private Integer id;

	private DadoOrigemEnum(Integer id){
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public Integer setId(Integer id) {
		return id;
	}

}
