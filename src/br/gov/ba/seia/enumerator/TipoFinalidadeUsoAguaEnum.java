package br.gov.ba.seia.enumerator;

public enum TipoFinalidadeUsoAguaEnum {

	AQUICULTURA(1),
	ABASTECIMENTO_HUMANO(2),
	ABASTECIMENTO_INDUSTRIAL(3),
	DESSEDENTACAO_E_CRIACAO_ANIMAL(4),
	IRRIGACAO(5),
	SERVICOS(6),
	OBRAS_CIVIS(7),
	MINERACAO(8),
	PULVERIZACAO_AGRICOLA(9),
	ABASTECIMENTO_PRA_EMPRESA_DE_TRANSPORTE(10),
	DESSEDENTACAO_ANIMAL(11),
	
	LAZER_TURISMO(13),
	ESGOTO_DOMESTICO(14),
	CRIACAO_ANIMAIS(15),
	EFLUENTE_INDUSTRIAL(16),

	ESGOTO_INDUSTRIAL(26),
	AQUICULTURA_VIVEIRO_ESCAVADO(25),
	AQUICULTURA_TANQUE_REDE(26),
	ABASTECIMENTO_PUBLICO(28),

	INFRAESTRUTURA(29),
	MINERACAO_EXTRACAO_AREIA(30),
	TERMOELETRICA(31),
	TRANSPOSICAO(32),
	OUTROS(33),
	
	AQUICULTURA_TANQUE_ESCAVADO(34),
	INDUSTRIAL(35),
	ESGOTAMENTO_SANITARIO_DOMESTICO(36),
	ESGOTAMENTO_SANITARIO_ABASTECIMENTO_PUBLICO(37),
	CRIACAO_ANIMAL(38),
	OUTRA(39),
	
	COMERCIO_SERVICOS(40),
	
	RESERVATORIO_BARRAMENTO_REGULARIZACAO(42),
	APROVEITAMENTO_HIDRELETRICO(41)

	;

	private Integer id;
	
	
	private TipoFinalidadeUsoAguaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	
	public static TipoFinalidadeUsoAguaEnum getEnum(Integer id) {
		TipoFinalidadeUsoAguaEnum[] enums = TipoFinalidadeUsoAguaEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == id) {
				return enums[i];
			}
			
		}
		throw new IllegalArgumentException("Não encontrado!");
	}
	

}
