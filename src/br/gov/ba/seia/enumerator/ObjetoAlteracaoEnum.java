package br.gov.ba.seia.enumerator;

public enum ObjetoAlteracaoEnum {
	
	SUBSTITUICAO(1,"Substituição ou instalação de equipamento que altere o processo produtivo, gerando impacto adicional"),
	DESMEMBRAMENTO(2, "Desmembramento de licença"),
	ALTERACAO_CAPACIDADE(3, "Alteração da capacidade de produção com aumento da carga poluidora das emissões líquidas, sólidas ou gasosas"),
	INCORPORACAO(4, "Incorporação de novas áreas"),
	ALTERACAO_PROJETO(5, "Alteração do projeto licenciado, não mencionada nas opções anteriores, gerando impacto adicional");
	
	private Integer id;
	private String descricao;

	private ObjetoAlteracaoEnum(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getDescricao() {
		return descricao;
	}
}