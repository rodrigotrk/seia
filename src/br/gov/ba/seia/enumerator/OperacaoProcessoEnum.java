package br.gov.ba.seia.enumerator;

public enum OperacaoProcessoEnum {
	CONSULTAR(1),
	DISTRIBUIR(2),
	PAUTA_TECNICO(3),
	PAUTA_GESTOR(4);

	private int operacao;

	private OperacaoProcessoEnum(int i) {
		operacao = i;
	}

	public int getOperacao() {
		return operacao;
	}
}