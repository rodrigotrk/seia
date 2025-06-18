package br.gov.ba.seia.enumerator;

public enum CategoriaDocumentoEnum {

	PORTARIA_E_CERTIFICADO(1), 
	CERTIFICADO(2),
	MINUTA(3),
	PARECER(4),
	RESPOSTA(5),
	OUTROS(6),
	DESPACHO(7),
	PORTARIA(8),
	DOCUMENTO_DA_NOTIFICACAO(9),
	JUSTIFICATIVA_NAO_REENQUADRAR(10),
	RESUMO_DE_REQUERIMENTO_DE_REENQUADRAMENTO_DE_PROCESSO(11);
	
	

	private Integer id;

	private CategoriaDocumentoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
