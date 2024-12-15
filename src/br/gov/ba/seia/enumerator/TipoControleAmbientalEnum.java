package br.gov.ba.seia.enumerator;

public enum TipoControleAmbientalEnum {


	SISTEMAS_FOSSAS_SEPTICAS(1,"Sistema de fossas sépticas"),
	REDE_DRENAGEM_EFLUENTES(2,"Rede de drenagem de efluentes"),
	SISTEMA_SEPARADOR_AGUA_OLEO(3,"Sistema separador água de óleo (SAO)"),
	SISTEMA_TRATAMENTO_EFLUENTE(4,"Sistema de tratamento do efluente"),
	DIQUE_CONTENCAO_ENTORNO_TANQUES_ABASTECIMENTO(5,"Dique ou canaleta de contenção no entorno dos tanques de abastecimento"),
	AREA_ESPECIFICA_ESTOCAGEM_TEMP_RESIDUS_PERIGOSOS(6,"Área específica para estocagem temporária de resíduos perigosos"),
	COLETA_ARMAZENAMENTO_OLEO_LUBRIFICANTE(7,"Coleta e armazenamento de óleo lubrificante usado"),
	ENVIA_EFLUENTE_TRATAMENTO_EXTERNO(8,"Envia o efluente para tratamento externo");
	
	private int id;
	private String dscTipoControle;
	
	private TipoControleAmbientalEnum(int id, String dscTipoControle){
		this.id = id;
	}
	
	public int getId(){
		return this.id;	
	}

	public String getDscTipoControle() {
		return dscTipoControle;
	}

}
