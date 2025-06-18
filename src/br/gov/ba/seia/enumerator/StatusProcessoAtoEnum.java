package br.gov.ba.seia.enumerator;

/**
 * @author Alexandre
 *
 */
public enum StatusProcessoAtoEnum {

	NULO(0),
	AGUARDANDO_ANALISE(1),
	EM_ANALISE(2),
	DEFERIDO_PELO_TECNICO(3),
	INDEFERIDO_PELO_TECNICO(4),
	DEFERIDO(5),
	INDEFERIDO(6),
	TRANSFERIDO(7),
	EMITIDO(8),
	ALTERADO(9)
	;
	
	private Integer id;
	
	private StatusProcessoAtoEnum(int id){
		this.id = id;
	}
	
	public Integer getId(){
		return this.id;	
	}
	
}
