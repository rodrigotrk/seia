package br.gov.ba.seia.controller.abstracts;

import java.util.List;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.FceIntervencaoBarragem;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaConcedida;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


public abstract class BaseFceBarragemController extends BaseFceOutorgaComDocumentoAdicionalController {

	protected List<OutorgaConcedida> listaOutorgaConcedida;
	
	/**
	 * Lista Outorga por localizaçao geografica
	 */
	public void listarOutorgaLocalizacaoGeografica(){
		try {
			super.listaOutorgaLocalizacaoGeografica = fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaDeBarragem(super.requerimento);
			for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : super.listaOutorgaLocalizacaoGeografica){
				tratarPontoLocGeo(outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Outorga Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * 
	 * @param lista
	 */
	public void tratarPontos(List<FceIntervencaoBarragem> lista){
		for(FceIntervencaoBarragem barragem : lista){
			if(!super.isFceTecnico()){
				tratarPontoLocGeo(barragem.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica());				
			} 
			else {
				tratarPontoLocGeo(barragem.getIdeFceOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica());				
			}
		}
	}
	
	/**
	 * 
	 * @param localizacaoGeografica
	 */
	private void tratarPontoLocGeo(LocalizacaoGeografica localizacaoGeografica) {
		super.fceOutorgaServiceFacade.tratarSomentePonto(localizacaoGeografica);
	}
}