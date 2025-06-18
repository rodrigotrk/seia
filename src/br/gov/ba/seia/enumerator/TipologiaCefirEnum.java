package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.Tipologia;

public enum TipologiaCefirEnum {
	
	PRODUTOS_AGRICULTURA_NEW(417),
	PRODUTOS_AGRICULTURA(3),
		AGRICULTURA_OLD(4),
		AGRICULTURA_DE_SEQUEIRO(5),
		AGRICULTURA_IRRIGADA(6),

	CRIACAO_DE_ANIMAIS(7),
		PECUARIA(8),
		PERCUARIA_NEW(421),
		CRIACOES_CONFINADAS(10),
			BOVINOS_BUBALINOS_MUARES_EQUINOS_CRIACAO_CONFINADA(11),
			AVES_PEQUENOS_MAMIFEROS_CRIACAO_CONFINADA(12),
			CAPRINOS_OVINOS_CRIACAO_CONFINADA(13),
			SUINOS_CRIACAO_CONFINADA(14),
			CRECHE_SUINOS_CRIACAO_CONFINADA(15),
		AQUICULTURA(16),
			PSICULTURA_INTENSIVA_EM_VIVEIROS_ESCAVADOS(17),
			PSICULTURA_MARINHA_TANQUE_REDE_RACEWAY_SIMILAR_OLD(18),
			PSICULTURA_CONTINENTAL_TANQUE_REDE_RACEWAY_SIMILAR(107),
		CARCINICULTURA(335),
			CARCINICULTURA_EM_VIVEIROS_ESCAVADOS_OLD(19),
			CARCINICULTURA_EM_VIVEIROS_ESCAVADOS(363),
			CARCINICULTURA_EM_VIVEIROS_ESCAVADOS_EM_APICUNS_E_SALGADOS(364),
		RANICULTURA(20),
		ALGICULTURA_E_MALACOCULTURA_OLD(21),
		ALGICULTURA(338),
		MALACOCULTURA(339),
	
	SILVICULTURA(22),
		SILVICULTURA_NAO_VINCULADA_PROCESSOS_INDUSTRIAIS(23), 
		SILVICULTURA_VINCULADA_PROCESSOS_INDUSTRIAIS(340),
		PRODUCAO_CARVAO(24),
		SILVICULTURA_VINCULADA_PSS(415),
		SILVICULTURA_VINCULADA_PSS_AREA_ATE_200_HA(430),
		SILVICULTURA_NAO_VINCULADA_PSS(416),
	ASSENTAMENTO_REFORMA_AGRARIA(27),
	PRODUCAO_CARVAO_VEGETAL_CARATER_TEMPORARIO(407),
	
	MANEJO_FLORESTAL_SUSTENTAVEL(405),
	MANEJO_CABRUCA(406),
	CRIACAO_CONFINADA_SEMI_CONFINADA(432);
		

	private Integer id;
	
	TipologiaCefirEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public static boolean containsTipologiaEnum(Tipologia tipologia) {
		
		for (TipologiaCefirEnum enumTipologia : TipologiaCefirEnum.values()) {						
			if(enumTipologia.getId().equals(tipologia.getIdeTipologia())){
					return true;
			}			
		}
		return false;
		
	}
	
	public static TipologiaCefirEnum obterTipologiaEnum(Integer ideTipologia) {
		
		for (TipologiaCefirEnum enumTipologia : TipologiaCefirEnum.values()) {						
			if(enumTipologia.getId().equals(ideTipologia)){
					return enumTipologia;
			}			
		}
		return null;		
	}
}
