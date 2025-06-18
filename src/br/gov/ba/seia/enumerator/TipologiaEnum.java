package br.gov.ba.seia.enumerator;

public enum TipologiaEnum {
	
	AGRICULTURA_IRRIGADA_OUT(6, ""),
	APROVEITAMENTO_HIDRELETRICO(330,""),
	AGRICULTURA_DE_SEQUEIRO(419, "A1.1.1"),
	AGRICULTURA_IRRIGADA_APE(420, "A1.1.2"),
	PECUARIA_EXTENSIVA(422, "A2.1.1"),
	SILVICULTURA(23, "A3.1"),
	CRIACAO_DE_ANIMAIS(7, "A2"),
	OUTORGA(292, "X"),
	ATOS_FLORESTAIS(293, "Y"),
	G11(388, "G1.1"),
	G21(389, "G2.1"),
	G22(390, "G2.2"),
	G23(391, "G2.3"),
	G24(392, "G2.4"),
	AQUICULTURA(16, "A2.3"), // Melhoria #6590
	CRIACAO_CONFINADA(10, "A2.2"), // Melhoria #6590
	CARCINICULTURA(335, "A2.4"), // Melhoria #6590
	BARRAGEM_E_DIQUES(333, "Z1"), 
	INTERVENÇÃO_EM_MANACIAL_HIDRICO(334, "Z1.1"),
	CAPTACAO_SUPERFICIAL(302, ""), // #6934
	CAPTACAO_SUBTERRANEA(303, ""), // #6934
	LANCAMENTO_EFLUENTES(304, ""), // #6934
	INTERVENCAO_CORPO_HIDRICO(305, ""), // #6934
	CRIACAO_FAUNA_SILVESTRE(408, ""),
	TRANSPORTADORA_RESIDUOS_PERIGOSOS(207, ""),
	COMBUSTIVEIS(50, "B5"),
	EXTRACAO_PETROLEO_GAS(53, "B6"),
	MINERACAO(28, "B"),
	SILVICULTURA_PSS_ATE_200_HA(430, "A3.3"), //Melhoria #9540
	CONSTRUCAO_LINHA(222,"E2.5"),
	TERMOELETRICA_GRUPO_GERADORES(219, "E2.2"),
	GERACAO_ENERGIA_ELETRICA_FONTE_EOLICA(366, "E2.4"),
	GERACAO_ENERGIA_SOLAR_FOTOVOLTAICA(367, "E2.7"),
	BOVINOS_BUBALINOS_MUARES_EQUINOS(433,"A2.2.1"),
	CONSTRUCAO_LINHAS_DE_DISTRIBUICAO_ENERGIA_ELETRICA_MAIORES_QUE_69KV(426,"E2.3"),
	AVES_PEQUENOS_MAMIFEROS(434,"A2.2.2"),
	CAPRINO_OVINOS(435,"A2.2.3"),
	SUINOS(432,"A2.2.4"),
	CRECHE_SUINOS(437,"A2.2.5"),
	CRIADOUROS_COMERCIAS(396,"H1.1.1"),
	CRIADOUROS_CIENTIFICOS_CRAS_CETAS_MANTENEDORES(440,"H1.1.2"),
	ZOOLOGICOS(441,"H1.1.3"),
	CRIADOUROS_COMERCIAS_ABELHAS(442,"H1.2.1"),
	ABATEDOUROS_FRIGORIFICOS(403,"H2.1.1");
	

	
	private int id;
	private String codTipologia;

	private TipologiaEnum(int id, String codigo) {
		this.id = id;
		this.codTipologia = codigo;
	}

	public int getId() {
		return id;
	}

	public String getCodTipologia() {
		return codTipologia;
	}
	

	public static TipologiaEnum getEnum(int ideTipologia) {
		for (TipologiaEnum tipologiaEnum : TipologiaEnum.values()) {
			if(tipologiaEnum.getId() == ideTipologia){
				return tipologiaEnum;
			}
		}

		throw new IllegalArgumentException("Ato Ambiental não encontrado!");
	}
}