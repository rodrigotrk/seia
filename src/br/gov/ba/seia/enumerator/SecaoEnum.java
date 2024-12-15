package br.gov.ba.seia.enumerator;

public enum SecaoEnum {

	ADMINISTRACAO(1),
	CADASTRO(2),
	CONSULTAS(3),
	REQUERIMENTOS(4),
	PROCESSOS(5),
	ANEXO_III(6),
	SECAO(8),
	RELATORIOS(9),
	LINKS_EXTERNOS(10),
	CONSULTA_PUBLICA(11),
	CADASTRO_NOVO(12),
	CONDICIONANTES_AMBIENTAIS(13);
	
	private Integer id;

	private SecaoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
