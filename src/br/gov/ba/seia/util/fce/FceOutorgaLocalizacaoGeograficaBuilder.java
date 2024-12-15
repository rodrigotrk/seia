package br.gov.ba.seia.util.fce;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalCaptacao;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo (eduardo.fernandes@zcr.com.br)
 * @since 27/01/2016
 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> #ticket
 */
public class FceOutorgaLocalizacaoGeograficaBuilder {

	private Fce fce;
	private TipologiaEnum tipologiaEnum;
	private LocalizacaoGeografica localizacaoGeografica;
	private OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica;
	private String nomRio;
	private int numTempoCaptacao;
	private LocalCaptacao localCaptacao;
	
	public FceOutorgaLocalizacaoGeograficaBuilder comFce(Fce fce){
		this.fce = fce;
		return this;
	}
	
	public FceOutorgaLocalizacaoGeograficaBuilder comTipologia(TipologiaEnum tipologiaEnum){
		this.tipologiaEnum = tipologiaEnum;
		return this;
	}
	
	public FceOutorgaLocalizacaoGeograficaBuilder comLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica){
		this.localizacaoGeografica = localizacaoGeografica;
		return this;
	}
	
	public FceOutorgaLocalizacaoGeograficaBuilder comOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica){
		this.outorgaLocalizacaoGeografica = outorgaLocalizacaoGeografica;
		return this;
	}
	
	public FceOutorgaLocalizacaoGeograficaBuilder comRio(String nomeRio){
		this.nomRio = nomeRio;
		return this;
	}
	
	public FceOutorgaLocalizacaoGeograficaBuilder comTempoCaptacao(int qtdHoras){
		this.numTempoCaptacao = qtdHoras;
		return this;
	}
	
	public FceOutorgaLocalizacaoGeograficaBuilder comLocalCaptacao(LocalCaptacao localCaptacao){
		this.localCaptacao = localCaptacao;
		return this;
	}
	
	public FceOutorgaLocalizacaoGeografica construirFceOutorgaLocalizacaoGeografica(){
		if(!Util.isNull(tipologiaEnum)){
			return new FceOutorgaLocalizacaoGeografica(this.fce, this.outorgaLocalizacaoGeografica, this.localizacaoGeografica, new Tipologia(tipologiaEnum.getId()), this.nomRio, this.localCaptacao, this.numTempoCaptacao);
		} else {
			return new FceOutorgaLocalizacaoGeografica(this.fce, this.outorgaLocalizacaoGeografica, this.localizacaoGeografica, null, this.nomRio, this.localCaptacao, this.numTempoCaptacao);
		}
	}
}
