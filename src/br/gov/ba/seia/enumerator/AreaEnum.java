package br.gov.ba.seia.enumerator;

public enum AreaEnum {

	
	ATEND(34), 
	COASP(39), 
	COFAQ(9), 
	COFIS(12), 
	COGED(38), 
	COGES(26),
	COIND(45), 
	COINE(6), 
	COINS(8), 
	COMIM(3),
	COTUR(7), 
	DIREG(1), 
	DIRRE(2), 
	DIRUC(24), 
	NOUT(76),
	SELIC(56),
	COGEC(13);
	
	
	private Integer id;

	AreaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
