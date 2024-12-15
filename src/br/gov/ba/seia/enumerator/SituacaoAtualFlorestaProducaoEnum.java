package br.gov.ba.seia.enumerator;

public enum SituacaoAtualFlorestaProducaoEnum {
	
	EM_PROJETO(1, "Em Projeto"), 
	EM_IMPLANTACAO(2, "Em Implatação"),
	TOTALMENTE_IMPLANTADA(3, "Totalmente Implantada");

	private int id;
	private String nome;
	
	private SituacaoAtualFlorestaProducaoEnum(int id, String nome){
		this.id = id;
		this.nome = nome;
	}
	
	public int getId(){
		return this.id;	
	}
	
	public String getNome(){
		return this.nome;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static SituacaoAtualFlorestaProducaoEnum getEnum(Integer id) {
		SituacaoAtualFlorestaProducaoEnum[] enums = SituacaoAtualFlorestaProducaoEnum.values();
		
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == id) {
				return enums[i];
			}
		}
		
		throw new IllegalArgumentException("Não encontrado!");
	}
	
	public static SituacaoAtualFlorestaProducaoEnum getEnum(String nome) {
		SituacaoAtualFlorestaProducaoEnum[] enums = SituacaoAtualFlorestaProducaoEnum.values();
		
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getNome().equals(nome)) {
				return enums[i];
			}
		}
		
		throw new IllegalArgumentException("Não encontrado!");
	}


	
}
