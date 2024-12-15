package br.gov.ba.seia.enumerator;

public enum TccaProjetoTipoStatusEnum {

	CADASTRO_INCOMPLETO(1, "Cadastro Incompleto"),
	VIGENTE(2, "Vigente"),
	PREVISTO(3, "Previsto"),
	EM_EXECUCAO(4, "Em Execução"),
	EXPIRADO(5, "Expirado"),
	CANCELADO(6, "Cancelado"),
	REMANEJADO(7, "Remanejado"),
	CONCLUIDO(8, "Concluído");
	
	private int id;
	private String nome;
	
	private TccaProjetoTipoStatusEnum(int id, String nome) {
		this.id = id;
		this.nome = nome;
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
}