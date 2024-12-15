
package br.gov.ba.seia.enumerator;


public enum TipoCompomenteHtmlEnum {

	INPUTTEXT(1),
	SELECTONEMENU(2), 
	SELECTCHECKBOX(3),
	SELECTONERADIO(4);
	
	private int id;
	
	
	private TipoCompomenteHtmlEnum(int id){
		this.id = id;
	}

	public int getIdTipoComponente(){
		return this.id;
	}
	
}
