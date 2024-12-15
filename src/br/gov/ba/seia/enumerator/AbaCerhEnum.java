package br.gov.ba.seia.enumerator;

public enum AbaCerhEnum {
	
	ABA_DADOS_GERAIS(0,"abaDadosGerais"),
	ABA_CAPTACAO_SUPERFICIAL(1,"abaCaptacaoSuperficial"),
	ABA_CAPTACAO_SUBTERRANEA(2,"abaCaptacaoSubterranea"),
	ABA_LANCAMENTO_EFLUENTES(3,"abaLancamentoEfluentes");
	
	private Integer index;
	private String nome;
	private boolean ativo;
	
	/**
	 * 
	 * @param index
	 * @param nome
	 */
	private AbaCerhEnum(Integer index, String nome) {
		this.index = index;
		this.nome = nome;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	/**
	 * 
	 * @param index
	 * @return aba
	 */
	public AbaCerhEnum getAbaCerhEnum(Integer index){
		for (AbaCerhEnum aba : AbaCerhEnum.values()) {
			if(aba.getIndex().equals(index)){
				return aba;
			}
		}
		throw new IllegalArgumentException("Não encontrado");
	}
	/**
	 * 
	 * @return aba
	 */
	public AbaCerhEnum getAbaEnumAtiva(){
		for (AbaCerhEnum aba : AbaCerhEnum.values()) {
			if(aba.isAtivo()){
				return aba;
			}
		}
		throw new IllegalArgumentException("Não encontrado");
	}
	/**
	 * 
	 */
	public void iniciar(){
		for (AbaCerhEnum aba : AbaCerhEnum.values()) {
			if(aba.getIndex()==0){
				aba.setAtivo(true);
			}else{
				aba.setAtivo(false);
			}
		}
		throw new IllegalArgumentException("Não encontrado");
	}
	
	
}
