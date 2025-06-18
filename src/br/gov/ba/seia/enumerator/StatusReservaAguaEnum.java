package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.StatusReservaAgua;

/**
 * <i>ENUM</i> do {@link StatusReservaAgua}
 * @author eduardo (eduardo.fernandes@zcr.com.br)
 * @since 19/02/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
 */
public enum StatusReservaAguaEnum {

	RESERVADO(1),
	CANCELADO(2);
	
	private Integer ide;

	private StatusReservaAguaEnum(Integer ide) {
		this.ide = ide;
	}

	public Integer getIde() {
		return ide;
	}

}
