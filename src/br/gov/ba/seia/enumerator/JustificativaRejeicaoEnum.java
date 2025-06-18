package br.gov.ba.seia.enumerator;

public enum JustificativaRejeicaoEnum {
	
	AJUSTE_CONTEUDO_TECNICO(1),
	AJUSTE_CONTEUDO_LEGAL(2),
	CORRECAO_TEXTO(3),
	OUTROS(4);
	
	
	private int codigo;
	
	private JustificativaRejeicaoEnum(int i){
		this.codigo = i;
	}

	public int getCodigo() {
		return codigo;
	}

	
}
