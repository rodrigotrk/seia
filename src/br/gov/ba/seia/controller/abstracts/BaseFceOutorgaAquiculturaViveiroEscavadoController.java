package br.gov.ba.seia.controller.abstracts;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Abstração
 *
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 16/04/2015
 */
public abstract class BaseFceOutorgaAquiculturaViveiroEscavadoController extends BaseFceOutorgaAquiculturaController {

	public void listarOutorgaLocGeoFromRequerimentoBy(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua){
		try {
			super.listaOutorgaLocalizacaoGeografica = super.obterInformacoesGeoBahia(super.fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaBy(tipoFinalidadeUsoAgua, super.requerimento));
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Outorga Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
}