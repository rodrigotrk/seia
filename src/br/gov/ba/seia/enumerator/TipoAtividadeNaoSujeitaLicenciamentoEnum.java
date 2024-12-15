package br.gov.ba.seia.enumerator;


public enum TipoAtividadeNaoSujeitaLicenciamentoEnum {
	
	SILOS_E_ARMAZENS(1),
	PESQUISA_MINERAL(2),
	PERFURACAO_DE_POCOS(3),
	MICROGERADORES_EOLICOS(4),
	INSTALACAO_DE_TORRES(5);
	
	private int ide;
	
	private TipoAtividadeNaoSujeitaLicenciamentoEnum(int ide) {
		this.ide = ide;
	}
	
	public int getIde() {
		return this.ide;
	}

	public static TipoAtividadeNaoSujeitaLicenciamentoEnum getEnum(Integer id) {
		TipoAtividadeNaoSujeitaLicenciamentoEnum[] enums = TipoAtividadeNaoSujeitaLicenciamentoEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getIde() == id) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Tipo de Atividade não encontrada!");
	}
}
