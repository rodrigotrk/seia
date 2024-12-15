package br.gov.ba.seia.enumerator;

public enum ImovelRuralAbaEnum {


	DADOS_BASICOS("dadosBasicosTab"),
	DOCUMENTACAO("documentacaoTab"),
	LOCALIZACAO_GEOGRAFICA("localizacaoGeograficaTab"),
	QUESTIONARIO("questionarioTab"),
	DADOS_ESPECIFICOS("dadosEspecificosTab"),
	CONFIRMACAO("confirmacaoTab")

	;

	private String aba;

	ImovelRuralAbaEnum(String aba){
		this.aba = aba;
	}

	public String getAba() {
		return aba;
	}

}
