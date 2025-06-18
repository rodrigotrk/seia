/**
 * 
 */
package br.gov.ba.seia.enumerator;

/**
 * @author eduardo
 *
 */
public enum TipoPortariaEnum {


	ARLS(1),
	TLA(2),
	AMC(3);
	
	private int id;
	
	private TipoPortariaEnum(int id){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}

	
}
