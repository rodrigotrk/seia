package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.AtoAmbiental;

public enum AtoAmbientalEnum {

	LU(1, "LU"),
	LP(2, "LP"),
	LI(3, "LI"),
	LA(4, "LA"),
	LOA(5, "LOA"),
	LO(6, "LO"),
	RLO(7, "RLO"),
	LR(8, "LR"),
	REVISAO_CONDICIONANTE(9,"RC"),
	AUTORIZACAO_AMBIENTAL(11,"AA"),
	ARL(13,"ARL"),
	ARLSF(13,"ARL/SF"),
	DQC(15,"DQC"),
	EPMF(17, "Aprovação da Execução das Etapas do Plano de Manejo Florestal Sustentável (EPMF)"),
	REGISTRO_FLORESTA_PRODUCAO(18,"RFP"), 
	CORTE_FLORESTA_PRODUCAO(19,"RCFP"),
	RVFR(22,"RVFR"),
	PRORROGACAO_LICENCA(23,"PPV/LIC"),
	ROUT(24, "ROUT"),
	CAPTACAO_ABASTECIMENTO_HUMANO(25, "CAPTAÇÃO – ABASTECIMENTO HUMANO"),
	CAPTACAO_AQUICULTURA(26,"CAPTAÇÃO – AQUICULTURA"), 
	CAPTACAO_ABASTECIMENTO_INDUSTRIAL(27,"CAPTAÇÃO – ABASTECIMENTO INDUSTRIAL"), 
	TRAVESSIA_DE_DUTO(28,"TRAVESSIA DE DUTO"), 
	IRRIGACAO(29,"IRRIGACAO"),
	CAPTACAO_PULVERIZACAO_AGRICOLA(30,"CAPTAÇÃO – PULVERIZAÇÃO AGRÍCOLA"),
	PULVERIZACAO(30,"PULVERIZACAO"), 
	CANALIZACAO_E_RETIFICACAO(31,"CANALIZAÇÃO E RETIFICAÇÃO"), 
	CONSTRUCAO_DE_PONTE(32,"CONSTRUÇÃO DE PONTE"),
	DRENAGEM_DE_AGUAS_PLUVIAIS_COM_DESAGUE_EM_MANANCIAL(33,"DRENAGEM DE ÁGUAS PLUVIAIS COM DESÁGUE EM MANANCIAL"),
	EXTRAÇÃO_EXPLOTAÇÃO_MINERAL_EM_LEITO_PESQUISA_E_LAVRA_LIMPEZA_DESASSOREAMENTO_E_DRAGAGEM(34,"EXTRAÇÃO/EXPLOTAÇÃO MINERAL EM LEITO (PESQUISA E LAVRA), LIMPEZA, DESASSOREAMENTO E DRAGAGEM"),
	LANCAMENTO_DE_EFLUENTES_PARA_OS_DIVERSOS_FINS(35,"LANÇAMENTO DE EFLUENTES PARA OS DIVERSOS FINS"),
	CAPTACAO_DESSEDENTACAO_E_CRIACAO_ANIMAL(36,"CAPTAÇÃO – DESSEDENTAÇÃO E CRIAÇÃO ANIMAL"),
	BARRAGEM(37,"BAR"),
	CONSTRUCAO_DE_PIER_DIQUE_CAIS(38,"CONSTRUÇÃO DE PÍER, DIQUE, CAIS"),
	LAC(39, "LAC"),
	LIMPEZA_AREA(40,"RLA"),
	PRORROGACAO_AUTORIZACAO_SUPRESSAO(43,"SV"),
	ANUENCIA(47,"AUC"),
	RLU(48,"RLU"),
	PRORROGACAO_AUTORIZACAO(50,"PPV/AA"),
	PERFURACAO_POCO(54,"OPP"),
	ARRL(58,"ARRL"),
	OUTORGA(97,"OUT"),
	TLA(98,"TLA"),
	ARLS(99,"ARLS"),
	OUTP(108, "OUTP"),
	DOUT(109, "DOUT"), //DISPENSA DE OUTORGA
	DRDH(110, "DRDH"),
	AOUT(111, "AOUT"),
	PPV_OUT(112, "PPV/OUT"),
	COUT(113, "COUT"),
	MNP(10,"MNP"),//
	PPV_EPMF(117,"PPV/EPMF"),
	RLAC(118,"RLAC"),
	SISFAUNA(120, "SIS"),
	APE(121,"Autorização por procedimento especial"),
	LC(119, "LC"),
	AA(11, "AA"),
	ASV(12, "ASV"),
	DIAP(122, "DIAP"),
	AMC(126, "Autorização de Manejo da Cabruca"),
	RLP(123,"RLP"),
	RLI(124,"RLI"),
	DTRP(45, "DTRP"),
	INEXIGIBILIDADE(127, "DI"),
	CRF(128, "Cumprimento de Reposição Florestal"),
	AML(21, "Aproveitamento de Material Lenhoso"),
	AMF(60, "Autorização de Manejo de Fauna"),
	ARTA(14, "Autorizações para o Manejo de Fauna"),
	REAPPO(129,"Renovação de Autorização para Perfuração de Poço");

	private Integer id;
	private String sigla;

	private AtoAmbientalEnum(Integer id, String sigla) {
		this.id = id;
		this.sigla = sigla;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public static AtoAmbientalEnum getEnum(AtoAmbiental atoAmbiental) {
		AtoAmbientalEnum[] enums = AtoAmbientalEnum.values();
		for(int i = 0; i < enums.length ; i++){
			//if(enums[i].getId() == atoAmbiental.getIdeAtoAmbiental()) {
			if(enums[i].getId().equals(atoAmbiental.getIdeAtoAmbiental())){
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Ato Ambiental não encontrado!");
	}

}
