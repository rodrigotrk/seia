package br.gov.ba.seia.enumerator;

public enum CerhSituacaoRegularizacaoEnum {
	
	EM_ANALISE(1, ""),
	INDEFERIDO(2, ""),
	DISPENSADO(3, "Dispensado"),
	INEXIGIVEL(4, "Inexigível"),
	OUTORGADO(5, ""),
	CANCELADO(6, "")
	;
	
	private int id;
	private String dsc;
	
	private CerhSituacaoRegularizacaoEnum(int id, String dsc){
		this.id = id;
		this.dsc = dsc;
	}
	
	public int getId(){
		return this.id;	
	}

	public String getDsc() {
		return dsc;
	}
	
}
