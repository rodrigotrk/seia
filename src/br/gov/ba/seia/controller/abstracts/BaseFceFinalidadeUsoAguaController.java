package br.gov.ba.seia.controller.abstracts;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


public abstract class BaseFceFinalidadeUsoAguaController extends BaseFceOutorgaComDocumentoAdicionalController {

	/**
	 * Carrega lista de Outorga por Localização geografica e tipo de finalidade do uso da agua
	 * @param tipoFinalidadeUsoAgua
	 */
	public void carregarListaOutorgaLocGeoByTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua){
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