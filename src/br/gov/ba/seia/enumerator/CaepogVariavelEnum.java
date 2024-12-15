package br.gov.ba.seia.enumerator;

public enum CaepogVariavelEnum {

	PROFUNDIDADE_PERFURADA(1, "Profundidade Perfurada", "metros"),
	EXTENSAO(2, "Extensão", "metros"),
	DIAMETRO(3, "Diâmetro", "metros"),
	TPH(4, "TPH", "un"),
	VOLUME_ESTIMADO(5, "Volume Estimado", "m³"),
	SALINIDADE(6, "Salinidade", "mg/l");
	
	private int id;
	private String nome;
	private String unidade;
	
	private CaepogVariavelEnum(int id, String nome, String unidade) {
		this.id = id;
		this.nome = nome;
		this.unidade = unidade;
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
	public String getUnidade() {
		return unidade;
	}
	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}
}
