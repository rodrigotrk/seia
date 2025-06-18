package br.gov.ba.seia.enumerator;

public enum CerhObrasHidraulicasEnum {

	RETIFICACAO_CANALIZACAO(4),
	DESVIO(5);
	
	private Integer ide;
	
	private CerhObrasHidraulicasEnum(Integer ide) {
		this.ide = ide;
	}

	public Integer getIde(){
		return this.ide;
	}
}
