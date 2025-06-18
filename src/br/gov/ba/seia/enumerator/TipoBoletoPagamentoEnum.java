package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.TipoBoletoPagamento;

public enum TipoBoletoPagamentoEnum {
	
	REQUERIMENTO(1, "Requerimento"),
	REQUERIMENTO_BOLETO_COMPLEMENTAR(2, "Requerimento (boleto complementar)"),
	EIA_RIMA_VINCULADO_PROCESSO(3, "EIA/RIMA vinculado a processo"),
	SEGUNDA_VIA_CERTIFICADO(4, "2ª via de certificado"),
	ERRATA_PORTARIA(5, "Errata de portaria"),
	INCLUSAO_NOVO_ATO(6, "Inclusão de novo ato"),
	REENQUADRAMENTO_PROCESSO(7, "Reenquadramento de processo"),
	;
	
	private Integer id;
	private String value;
	
	private TipoBoletoPagamentoEnum(int id, String value) {
		this.id = id;
		this.value = value;
	}

	
	public Integer getId() {
		return id;
	}
	
	public String getValue() {
		return value;
	}
	
	public static TipoBoletoPagamentoEnum getEnum(TipoBoletoPagamento tipoBoletoPagamento) {
		TipoBoletoPagamentoEnum[] enums = TipoBoletoPagamentoEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == tipoBoletoPagamento.getIdeTipoBoletoPagamento()) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("TipoBoletoPagamento não encontrado!");
	}
	
}
