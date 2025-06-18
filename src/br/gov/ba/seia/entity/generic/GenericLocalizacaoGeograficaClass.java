package br.gov.ba.seia.entity.generic;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.interfaces.LocalizacaoGeofraficaInterface;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public abstract class GenericLocalizacaoGeograficaClass extends AbstractEntity implements LocalizacaoGeofraficaInterface{
	private static final long serialVersionUID = 1L;
	/**
	 * limpar localização geografica
	 */
	@Override
	public LocalizacaoGeografica limparLocalizacaoGeografica() {
		try {
			if(getIdeLocalizacaoGeografica() != null) {
				LocalizacaoGeografica localizacaoGeografica = getIdeLocalizacaoGeografica().clone();
				getIdeLocalizacaoGeografica().setIdeLocalizacaoGeografica(null);
				return localizacaoGeografica;
			} else {
				return null;
			}

		} 
		catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

}
