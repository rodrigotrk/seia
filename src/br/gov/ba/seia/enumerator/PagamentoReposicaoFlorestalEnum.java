package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;

public enum PagamentoReposicaoFlorestalEnum {
	
	DETENTOR(1),
	CONSUMIDOR(2),
	DEVEDOR(3),
	ASV(21),
	AML(22),
	RECONHECIMENTO_FLORESTAL_REMANESCENTE(23);
	
	private Integer id;
	
	private PagamentoReposicaoFlorestalEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public static PagamentoReposicaoFlorestalEnum getEnum(PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal) {
		PagamentoReposicaoFlorestalEnum[] enums = PagamentoReposicaoFlorestalEnum.values();
		for(int i = 0; i < enums.length ; i++){
			//if(enums[i].getId() == atoAmbiental.getIdeAtoAmbiental()) {
			if(enums[i].getId().equals(pagamentoReposicaoFlorestal.getIdePagamentoReposicaoFlorestal())){
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Ato Ambiental não encontrado!");
	}
}
