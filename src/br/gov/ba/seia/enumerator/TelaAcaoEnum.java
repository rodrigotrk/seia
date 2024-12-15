package br.gov.ba.seia.enumerator;

public enum TelaAcaoEnum {

	CADASTRAR("cadastrar"),
	EDITAR("editar"),
	EXCLUIR("excluir"),
	INCLUIR("incluir"),
	VISUALIZAR("visualizar");

	protected String acao;

	private TelaAcaoEnum(String acao) {
		this.acao = acao;
	}

	public boolean isEditar() {
		return this.equals(EDITAR);
	}

	public boolean isVisualizar() {
		return this.equals(VISUALIZAR);
	}

	public boolean isCadastrar() {
		return this.equals(CADASTRAR);
	}

	public String getAcao() {
		return acao;
	}


	public static TelaAcaoEnum getEnum(String acao) {
		
		for(TelaAcaoEnum telaAcaoEnum: TelaAcaoEnum.values()){
			if(telaAcaoEnum.getAcao().equalsIgnoreCase(acao)){
				return telaAcaoEnum;
			}
		}
		
		throw new IllegalArgumentException("Ação não encontrada!");
	}

}
