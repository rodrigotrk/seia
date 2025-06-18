package br.gov.ba.seia.controller.abstracts;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public abstract class BaseFceOutorgaSemDocumentoAdicionalController extends BaseFceOutorgaController {

	protected List<OutorgaLocalizacaoGeografica> listaCaptacaoSuperficial;
	protected List<OutorgaLocalizacaoGeografica> listaCaptacaoSubterranea;


	public void carregarListaOutorgaLocGeoByTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua){
		try {
			super.listaOutorgaLocalizacaoGeografica = super.obterInformacoesGeoBahia(fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaBy(tipoFinalidadeUsoAgua, super.requerimento));
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Outorga Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que separa em duas listas as {@link OutorgaLocalizacaoGeografica} cadastradas em Captações Superficiais e Captações Subterrâneas cadastradas na ETAPA 4 do {@link Requerimento}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 08/04/2015
	 */
	@SuppressWarnings("unchecked")
	public void separarCaptacoes(){
		Map<String, Object> captacoes = super.separarCaptacoes(super.listaOutorgaLocalizacaoGeografica);
		listaCaptacaoSuperficial = (List<OutorgaLocalizacaoGeografica>) captacoes.get("captacoesSuperficial");
		listaCaptacaoSubterranea = (List<OutorgaLocalizacaoGeografica>) captacoes.get("captacoesSubterranea");
	}

	@Override
	public void limparDadosOutorga(){
		listaCaptacaoSuperficial = null;
		listaCaptacaoSubterranea = null;
		super.limparDadosOutorga();
	}
}