package br.gov.ba.seia.controller.abstracts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaConcedida;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.fce.FceOutorgaLocalizacaoGeograficaBuilder;


public abstract class BaseFceCaptacaoController extends BaseFceOutorgaComDocumentoAdicionalController {
		
	protected List<OutorgaConcedida> listaOutorgaConcedida;
	
	public void listarOutorgaLocGeoBy(TipoCaptacaoEnum captacaoEnum){
		try {
			super.listaOutorgaLocalizacaoGeografica = super.obterInformacoesGeoBahia(super.fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaBy(captacaoEnum, super.requerimento));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Outorga Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	protected FceOutorgaLocalizacaoGeografica prepararDuplicacaoFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica, OutorgaLocalizacaoGeografica outorgaLocGeoOriginal, TipologiaEnum tipologiaEnum) throws Exception{
		
		LocalizacaoGeografica locGeoDuplicada = super.fceOutorgaServiceFacade.duplicarLocalizacaoGeografica(outorgaLocGeoOriginal.getIdeLocalizacaoGeografica());
		
		FceOutorgaLocalizacaoGeograficaBuilder builder = new FceOutorgaLocalizacaoGeograficaBuilder()
			.comFce(super.fce)
			.comLocalizacaoGeografica(locGeoDuplicada);
		
		if(!Util.isNull(fceOutorgaLocalizacaoGeografica)){ // Captação Superficial
			builder.comOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica())
					.comRio(fceOutorgaLocalizacaoGeografica.getNomRio())
					.comLocalCaptacao(fceOutorgaLocalizacaoGeografica.getLocalCaptacao())
					.comTempoCaptacao(fceOutorgaLocalizacaoGeografica.getNumTempoCaptacao());
		
		} else { // Captação Subterrânea
			builder.comOutorgaLocalizacaoGeografica(outorgaLocGeoOriginal);
		}
		
		builder.comTipologia(tipologiaEnum);
		
		FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaDuplicada = builder.construirFceOutorgaLocalizacaoGeografica();
		
		prepararListaFceOutorgaLocGeoFinalidade(outorgaLocGeoOriginal, fceOutorgaLocalizacaoGeograficaDuplicada);
		
		return fceOutorgaLocalizacaoGeograficaDuplicada;
	}
	
	protected void adicionarFceOutorgaLocalizacaoGeografica(TipologiaEnum tipologiaEnum, FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica){
		if(tipologiaEnum.equals(TipologiaEnum.CAPTACAO_SUPERFICIAL)){
			super.listaFceOutorgaLocalizacaoGeograficaCapSup.add(fceOutorgaLocalizacaoGeografica);
		} 
		else if(tipologiaEnum.equals(TipologiaEnum.CAPTACAO_SUBTERRANEA)){
			super.listaFceOutorgaLocalizacaoGeograficaCapSub.add(fceOutorgaLocalizacaoGeografica);
		}
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 06/01/2016
	 */
	protected void prepararListaFceOutorgaLocGeoFinalidade(OutorgaLocalizacaoGeografica outorgaLocGeoOriginal, FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaDuplicada) throws Exception {
		// Preparar lista de FceOutorgaLocalizacaoGeograficaFinalidade.
		List<OutorgaLocalizacaoGeograficaFinalidade> listaOutorgaLocGeoFinalidade = super.fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaFinalidadeBy(outorgaLocGeoOriginal); 
		if(!Util.isNullOuVazio(listaOutorgaLocGeoFinalidade)){
			fceOutorgaLocalizacaoGeograficaDuplicada.setListaFinalidade(new ArrayList<FceOutorgaLocalizacaoGeograficaFinalidade>());
			for(OutorgaLocalizacaoGeograficaFinalidade outorgaLocalizacaoGeograficaFinalidade : listaOutorgaLocGeoFinalidade){
				TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua = outorgaLocalizacaoGeograficaFinalidade.getIdeTipoFinalidadeUsoAgua();
				boolean indCaptacao = outorgaLocalizacaoGeograficaFinalidade.getIndCaptacao();
				fceOutorgaLocalizacaoGeograficaDuplicada.getListaFinalidade().add(new FceOutorgaLocalizacaoGeograficaFinalidade(fceOutorgaLocalizacaoGeograficaDuplicada, tipoFinalidadeUsoAgua, indCaptacao));
			}
		}
	}
	
	protected void tratarPontosListaFceOutorgaLocalizacaoGeografica(TipologiaEnum tipologiaEnum) throws Exception{
		if(tipologiaEnum.equals(TipologiaEnum.CAPTACAO_SUPERFICIAL)){
			super.fceOutorgaServiceFacade.tratarPontosListaFceOutorgaLocalizacaoGeografica(super.listaFceOutorgaLocalizacaoGeograficaCapSup);	
		}
		else if(tipologiaEnum.equals(TipologiaEnum.CAPTACAO_SUBTERRANEA)){
			super.fceOutorgaServiceFacade.tratarPontosListaFceOutorgaLocalizacaoGeografica(super.listaFceOutorgaLocalizacaoGeograficaCapSub);	
		}
	}
	
	protected void duplicacao(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica){
		try {
			salvarFceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica);
			salvarFceOutorgaLocalizacaoGeograficaFinalidade(fceOutorgaLocalizacaoGeografica);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " as informações da Captação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método que salvar o FceOutorgaLocalizacaoGeografica
	 * @author eduardo.fernandes
	 * @throws Exception 
	 */
	protected void salvarFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) throws Exception{
		super.salvarFce();
		fceOutorgaLocalizacaoGeografica.setIdeFce(super.fce);
		super.fceOutorgaServiceFacade.salvarFceOutorgaLocGeo(fceOutorgaLocalizacaoGeografica);
	}
	
	/**
	 * Método para salvar a lista de {@link FceOutorgaLocalizacaoGeograficaFinalidade}
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @throws Exception 
	 * @since 06/01/2016
	 */
	private void salvarFceOutorgaLocalizacaoGeograficaFinalidade(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) throws Exception{
		if(!Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getListaFinalidade())){
			super.fceOutorgaServiceFacade.salvarListaFceOutorgaLocGeoFinalidade(fceOutorgaLocalizacaoGeografica.getListaFinalidade());
		}
	}

	/**
	 * Método que salva as {@link OutorgaConcedida} no momento da duplicação do {@link Fce} na {@link AnaliseTecnica}. 
	 * @author eduardo.fernandes
	 * @throws Exception
	 * @see <a href="http://10.105.12.26/redmine/issues/7550">#7550</a>
	 */
	protected void salvarListaOutorgaConcedida() {
		try {
			super.salvarListaOutorgaConcedida(listaOutorgaConcedida);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " as Outorgas Concedidas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
}