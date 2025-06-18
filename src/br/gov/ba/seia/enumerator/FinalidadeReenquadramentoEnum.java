package br.gov.ba.seia.enumerator;

public enum FinalidadeReenquadramentoEnum {

		ALTERACAO_ATOS_AUTORIZATIVOS(1), 
		INCLUSAO_NOVOS_ATOS_AUTORIZATIVOS(2), 
		ALTERACAO_POTENCIAL_POLUIDOR_ATIVIDADE(3), 
		ALTERACAO_CLASSE_EMPREENDIMENTO(4), 
		ALTERACAO_TIPOLOGIA(5), 
		CORRECAO_PORTE_EMPREENDIMENTO(6)
	;
	
	private Integer id;

	FinalidadeReenquadramentoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
