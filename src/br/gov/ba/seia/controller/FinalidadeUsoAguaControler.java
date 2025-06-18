package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("finalidadeController")
@ViewScoped
public class FinalidadeUsoAguaControler {

	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;

	private Collection<TipoFinalidadeUsoAgua> tiposFinalidadesUsoAgua;

	private OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica;

	private boolean indCaptacao;

	public void loadCaptacaoSubterranea() {
		try {
			this.carregarFinalidadesUsoAgua(ModalidadeOutorgaEnum.CAPTACAO_SUBTERRANEA);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	public void loadCaptacaoSuperficial() {
		try {
			this.carregarFinalidadesUsoAgua(ModalidadeOutorgaEnum.CAPTACAO_SUPERFICIAL);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	public void loadLancamentosEfluentes() {
		try {
			this.indCaptacao = false;
			this.carregarFinalidadesUsoAgua(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	public void loadIntervencao() {
		try {
			this.indCaptacao = false;
			this.carregarFinalidadesUsoAgua(ModalidadeOutorgaEnum.INTERVENCAO);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void loadAproveitamentoHidreletrico() {
		try {
			this.indCaptacao = false;
			this.carregarFinalidadesUsoAgua(ModalidadeOutorgaEnum.POTENCIAL_HIDRELETRICO);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarFinalidadesUsoAgua(ModalidadeOutorgaEnum modalidadeOutorgaEnum) throws Exception {
		Integer idTipologia = modalidadeOutorgaEnum.getIdTipologia();
		this.tiposFinalidadesUsoAgua = this.tipoFinalidadeUsoAguaService.buscarFinalidadeUsoAguaByIdeTipologia(idTipologia);
	}

	public void salvar(){
		ArrayList<TipoFinalidadeUsoAgua> finalidades = Util.sigletonList(this.outorgaLocalizacaoGeografica.getListaFinalidadeUsoAgua());

		if(this.indCaptacao){
			finalidades = Util.sigletonList(this.outorgaLocalizacaoGeografica.getListaFinalidadeUsoAguaCaptacao());
		}

		for (TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua : finalidades) {
			tipoFinalidadeUsoAgua.setIndCaptacao(this.indCaptacao);
		}

		if(this.indCaptacao)
			this.outorgaLocalizacaoGeografica.setListaFinalidadeUsoAguaCaptacao(finalidades);
		else
			this.outorgaLocalizacaoGeografica.setListaFinalidadeUsoAgua(finalidades);
		}

	public Collection<TipoFinalidadeUsoAgua> getTiposFinalidadesUsoAgua() {
		return tiposFinalidadesUsoAgua;
	}

	public void setTiposFinalidadesUsoAgua(Collection<TipoFinalidadeUsoAgua> tiposFinalidadesUsoAgua) {
		this.tiposFinalidadesUsoAgua = tiposFinalidadesUsoAgua;
	}

	public OutorgaLocalizacaoGeografica getOutorgaLocalizacaoGeografica() {
		return outorgaLocalizacaoGeografica;
	}

	public void setOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		this.outorgaLocalizacaoGeografica = outorgaLocalizacaoGeografica;
	}

	public boolean isIndCaptacao() {
		return indCaptacao;
	}

	public void setIndCaptacao(boolean indCaptacao) {
		this.indCaptacao = indCaptacao;
	}


}
