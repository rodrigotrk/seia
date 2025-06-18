package br.gov.ba.seia.enumerator;

public enum LacEnum {

 	LAC_ERB           (104), 
 	LAC_POSTO         (105),
 	LAC_TRANSPORTADORA(1263);

	private Integer id;

	private LacEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
}
