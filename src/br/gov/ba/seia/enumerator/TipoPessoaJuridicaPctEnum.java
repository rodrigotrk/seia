package br.gov.ba.seia.enumerator;

import javax.faces.event.ActionEvent;

public enum TipoPessoaJuridicaPctEnum {
	
	ASSOCIACAO(1,"associacao"),
	CONCEDENTE(2,"concedente"),
	CONCESSIONARIO(3,"concessionario")
	;
	
	private Integer id;
	private String value;
	
	private TipoPessoaJuridicaPctEnum(Integer id, String value){
		this.id = id;
		this.value=value;
	}
	
	public static TipoPessoaJuridicaPctEnum getEnum(ActionEvent evt) {
		String clientId = evt.getComponent().getClientId();
		for(TipoPessoaJuridicaPctEnum t : values()) {
			if(clientId.contains(t.getValue())) {
				return t;
			}
		}
		throw new IllegalArgumentException("Parametro inválido!");
	}
	
	public Integer getId(){
		return this.id;	
	}

	public String getValue() {
		return value;
	}


}
