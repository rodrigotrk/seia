package br.gov.ba.seia.entity.generic;

import java.io.Serializable;
import java.util.List;

import br.gov.ba.seia.entity.LocalizacaoGeografica;

public abstract class AbstractEntityLocalizacaoGeografica extends AbstractEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	public abstract List<LocalizacaoGeografica> getLocalizacoesGeograficas();
	/**
	 * Limpar localização geografica
	 */
	public void limparLocalizacaoGeografica() {
		if(getLocalizacoesGeograficas() != null) {
			for (LocalizacaoGeografica localizacaoGeografica : getLocalizacoesGeograficas()) {
				localizacaoGeografica.setIdeLocalizacaoGeografica(null);
				localizacaoGeografica = null;
			} 

		} 
	}

}
