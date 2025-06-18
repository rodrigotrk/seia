package br.gov.ba.seia.controller;

import java.util.Arrays;
import java.util.List;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;

public class CerhAtosAmbientaisElegiveis {

	public static List<AtoAmbiental> atosElegiveisCaptacao(){
		return 
			Arrays.asList(
				new AtoAmbiental(AtoAmbientalEnum.CAPTACAO_ABASTECIMENTO_HUMANO),
				new AtoAmbiental(AtoAmbientalEnum.CAPTACAO_AQUICULTURA),
				new AtoAmbiental(AtoAmbientalEnum.CAPTACAO_ABASTECIMENTO_INDUSTRIAL),
				new AtoAmbiental(AtoAmbientalEnum.IRRIGACAO),
				new AtoAmbiental(AtoAmbientalEnum.CAPTACAO_PULVERIZACAO_AGRICOLA),
				new AtoAmbiental(AtoAmbientalEnum.CAPTACAO_DESSEDENTACAO_E_CRIACAO_ANIMAL));
	}

	public static List<AtoAmbiental> atosElegiveisLancamento(){
		return 
			Arrays.asList(
				new AtoAmbiental(AtoAmbientalEnum.LANCAMENTO_DE_EFLUENTES_PARA_OS_DIVERSOS_FINS));
	}
	
	public static List<AtoAmbiental> atosElegiveisBarragem(){
		return 
			Arrays.asList(
				new AtoAmbiental(AtoAmbientalEnum.BARRAGEM));
	}
	
	public  static List<AtoAmbiental> atosElegiveisIntervencao(){
		return 
			Arrays.asList(
				new AtoAmbiental(AtoAmbientalEnum.CONSTRUCAO_DE_PIER_DIQUE_CAIS),
				new AtoAmbiental(AtoAmbientalEnum.DRENAGEM_DE_AGUAS_PLUVIAIS_COM_DESAGUE_EM_MANANCIAL),
				new AtoAmbiental(AtoAmbientalEnum.TRAVESSIA_DE_DUTO),
				new AtoAmbiental(AtoAmbientalEnum.CANALIZACAO_E_RETIFICACAO),
				new AtoAmbiental(AtoAmbientalEnum.CONSTRUCAO_DE_PONTE),
				new AtoAmbiental(AtoAmbientalEnum.EXTRAÇÃO_EXPLOTAÇÃO_MINERAL_EM_LEITO_PESQUISA_E_LAVRA_LIMPEZA_DESASSOREAMENTO_E_DRAGAGEM));
	}
	
	public static List<AtoAmbiental> atosLivreEscolha(){
		return 
			Arrays.asList(
				new AtoAmbiental(AtoAmbientalEnum.COUT),
				new AtoAmbiental(AtoAmbientalEnum.ROUT),
				new AtoAmbiental(AtoAmbientalEnum.PPV_OUT),
				new AtoAmbiental(AtoAmbientalEnum.OUTORGA));
	}
}