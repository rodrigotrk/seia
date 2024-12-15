package br.gov.ba.seia.enumerator;

public enum TccaProjetoOperacaoEnum {

	//OPERAÇÃO INICIAL
	DESTINACAO_INICIAL(4, "Destinação Inicial", true),
	
	//EXECUÇÃO DO PROJETO
	DESTINACAO(5, "Destinação", false),
	EXECUCAO(6, "Execução", false),
	
	//MOVIMENTAÇÃO FINANCEIRA
	SUPLEMENTACAO(2, "Suplementação", false),
	REMANEJAMENTO(3, "Remanejamento", false),
	DEVOLUCAO(7, "Devolução", false),
	
	//REAJUSTAR VALOR
	REAJUSTE(1, "Reajuste", false);
	
	private int id;
	private String nome;
	private Boolean indOculto;
	
	private TccaProjetoOperacaoEnum(int id, String nome, boolean indOculto) {
		this.id = id;
		this.nome = nome;
		this.indOculto = indOculto;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getIndOculto() {
		return indOculto;
	}

	public void setIndOculto(Boolean indOculto) {
		this.indOculto = indOculto;
	}
}