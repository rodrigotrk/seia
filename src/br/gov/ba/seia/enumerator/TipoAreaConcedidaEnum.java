package br.gov.ba.seia.enumerator;

/**
 * @author eduardo.fernandes 
 * @since 10/03/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
public enum TipoAreaConcedidaEnum {
	
	ATIVIDADE(1),
	EMPREENDIMENTO(2),
	GALPAO_DE_INSUMOS(3),
	OBJETO_DE_LICENCA_LINHA_DE_TRANSMISSAO_E_DISTRIBUICAO(4)
	;
	
	private TipoAreaConcedidaEnum(Integer ide) {
		this.ide = ide;
	}

	private Integer ide;

	public Integer getIde() {
		return ide;
	}

	public TipoAreaConcedidaEnum getEnum(Integer ide) {
		TipoAreaConcedidaEnum[] enums = TipoAreaConcedidaEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getIde().equals(ide)) {
				return enums[i];
			}
		}
	    throw new IllegalArgumentException("Tipo área concedida não encontrada!");
	}
}