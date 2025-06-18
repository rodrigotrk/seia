/**
 * 
 */
package br.gov.ba.seia.controller;

import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaTipoCaptacao;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.service.EmpreendimentoRequerimentoService;
import br.gov.ba.seia.service.FceOutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author lesantos
 *
 */
public abstract class FceRequerimentoController extends FceController{
	
	@EJB
	private EmpreendimentoRequerimentoService empreendimentoRequerimentoService;
	
	@EJB
	protected LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@EJB
	private OutorgaService outorgaService;
	
	@EJB
	protected OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	
	@EJB
	protected FceOutorgaLocalizacaoGeograficaService fceOutorgaLocalizacaoGeograficaService;
	
	private List<OutorgaLocalizacaoGeografica> captacoesSubterraneas,captacoesSuperficiais, lancamentosEfluentes;
	
	private boolean captacaoPrecipitacaoPluvial, captacaoConcessessionarioPublica, captacaoSuperficial, captacaoSubterranea;
	
	private List<FceOutorgaLocalizacaoGeografica> listFceOutorgaLocalizacaoGeografica;
	
	protected void carregarOutorgas() throws Exception {
		try {
			List<Outorga> outorgasCaptacao = outorgaService.getOutorgaByModalidadeRequerimento(ModalidadeOutorgaEnum.CAPTACAO, requerimento);

			for(Outorga outorga : outorgasCaptacao){
				OutorgaTipoCaptacao outorgaTipoCaptacao = this.outorgaService.buscarOutorgaTipoCaptacaoByIdeOutorga(outorga);
				if(!Util.isNull(outorgaTipoCaptacao) && Util.isNull(outorgaTipoCaptacao.getIdeTipoCaptacao())){
					if(TipoCaptacaoEnum.CAPTACAO_CONCESSIONARIA_PUBLICA.getId().intValue() == outorgaTipoCaptacao.getIdeTipoCaptacao().getIdeTipoCaptacao()){
						captacaoConcessessionarioPublica = true;
					}else if(TipoCaptacaoEnum.CAPTACAO_PRECIPITACAO_METEOROLOGICA_PLUVIAL.getId().intValue() == outorgaTipoCaptacao.getIdeTipoCaptacao().getIdeTipoCaptacao()){
						captacaoPrecipitacaoPluvial = true;
					}
				}
			}

			captacoesSubterraneas = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA, requerimento);
			if(!Util.isNullOuVazio(captacoesSubterraneas)){
				captacaoSubterranea  = true;
			}

			captacoesSuperficiais = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL, requerimento);
			if(!Util.isNullOuVazio(captacoesSuperficiais)){
				captacaoSuperficial  = true;
			}

			lancamentosEfluentes = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaComDadoGeograficoByModalidadeOutorga(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES, requerimento);
			
			listFceOutorgaLocalizacaoGeografica = fceOutorgaLocalizacaoGeograficaService.listarFceOutorgaLocGeoByModalidadeIdeRequerimento(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES, requerimento);
		
			requerimento.setUltimoEmpreendimento(empreendimentoRequerimentoService.buscarEmpreendimentoRequerimento(requerimento).getIdeEmpreendimento());
		}catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	public List<OutorgaLocalizacaoGeografica> getCaptacoesSubterraneas() {
		return captacoesSubterraneas;
	}


	public void setCaptacoesSubterraneas(List<OutorgaLocalizacaoGeografica> captacoesSubterraneas) {
		this.captacoesSubterraneas = captacoesSubterraneas;
	}


	public List<OutorgaLocalizacaoGeografica> getCaptacoesSuperficiais() {
		return captacoesSuperficiais;
	}


	public void setCaptacoesSuperficiais(List<OutorgaLocalizacaoGeografica> captacoesSuperficiais) {
		this.captacoesSuperficiais = captacoesSuperficiais;
	}


	public List<OutorgaLocalizacaoGeografica> getLancamentosEfluentes() {
		return lancamentosEfluentes;
	}


	public void setLancamentosEfluentes(List<OutorgaLocalizacaoGeografica> lancamentosEfluentes) {
		this.lancamentosEfluentes = lancamentosEfluentes;
	}


	public boolean isCaptacaoPrecipitacaoPluvial() {
		return captacaoPrecipitacaoPluvial;
	}


	public void setCaptacaoPrecipitacaoPluvial(boolean captacaoPrecipitacaoPluvial) {
		this.captacaoPrecipitacaoPluvial = captacaoPrecipitacaoPluvial;
	}


	public boolean isCaptacaoConcessessionarioPublica() {
		return captacaoConcessessionarioPublica;
	}


	public void setCaptacaoConcessessionarioPublica(boolean captacaoConcessessionarioPublica) {
		this.captacaoConcessessionarioPublica = captacaoConcessessionarioPublica;
	}


	public boolean isCaptacaoSuperficial() {
		return captacaoSuperficial;
	}


	public void setCaptacaoSuperficial(boolean captacaoSuperficial) {
		this.captacaoSuperficial = captacaoSuperficial;
	}


	public boolean isCaptacaoSubterranea() {
		return captacaoSubterranea;
	}


	public void setCaptacaoSubterranea(boolean captacaoSubterranea) {
		this.captacaoSubterranea = captacaoSubterranea;
	}


	public List<FceOutorgaLocalizacaoGeografica> getListFceOutorgaLocalizacaoGeografica() {
		return listFceOutorgaLocalizacaoGeografica;
	}


	public void setListFceOutorgaLocalizacaoGeografica(
			List<FceOutorgaLocalizacaoGeografica> listFceOutorgaLocalizacaoGeografica) {
		this.listFceOutorgaLocalizacaoGeografica = listFceOutorgaLocalizacaoGeografica;
	}
	
}
