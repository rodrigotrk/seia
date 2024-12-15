package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.DetalhamentoBoleto;
import br.gov.ba.seia.entity.ParametroCalculo;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ParametroCalculoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("boletoController")
@ViewScoped
public class BoletoController {

	private DetalhamentoBoleto detalhamentoBoletoACalcular;

	@EJB
	private ParametroCalculoService parametroCalculoService;

	@Inject
	BoletoAutomatizadoController boletoAutomatizadoController;

	public void calcularTaxaBoleto(DetalhamentoBoleto detalhamentoBoleto) {
		try {

			this.detalhamentoBoletoACalcular = detalhamentoBoleto;

			if (this.isIrrigacao()) {
				this.calcularValorIrrigacao(detalhamentoBoleto);
			}

			if (this.isRevisaoOuProrrogacao()) {
				this.calcularPorcentagemLicencaAnterior();
			}

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void calcularValor() {
		
		if(isIrrigacao()){
			this.calcularValorIrrigacao(this.detalhamentoBoletoACalcular);
		}
		else if(isPulverizacao()) {
			this.calcularValorPulverizacao(this.detalhamentoBoletoACalcular);
		}
	}
	

	public void calcularValorIrrigacao(DetalhamentoBoleto detalhamentoBoleto)  {
		Collection<ParametroCalculo> parametros = carregarParametros(detalhamentoBoleto);
		this.calcularValorIrrigacao(detalhamentoBoleto, parametros);
	}
	
	public void calcularValorPulverizacao(DetalhamentoBoleto detalhamentoBoleto) {
		Collection<ParametroCalculo> parametros = carregarParametros(detalhamentoBoleto);
		this.calcularValorPulverizacao(detalhamentoBoleto, parametros);
	}

	private void calcularValorIrrigacao(DetalhamentoBoleto detalhamentoBoleto, Collection<ParametroCalculo> parametros) {
		double valor = 0;

		for (ParametroCalculo parametroCalculo : parametros) {

			if (!parametroCalculo.isExigeCalculo()) {
				continue;
			}

			Double areaMaxima = null;
			if (!Util.isNull(parametroCalculo.getAreaMaxima())) {
				areaMaxima = parametroCalculo.getAreaMaxima().doubleValue();
			}

			Double areaMinima = null;
			if (!Util.isNull(parametroCalculo.getAreaMinima())) {
				areaMinima = parametroCalculo.getAreaMinima().doubleValue();
			}else{
				areaMinima =  0.0;
			}

			if ((Util.isNull(areaMaxima) || areaMaxima >= detalhamentoBoleto.getAreaCaptacao()) && (areaMinima < detalhamentoBoleto.getAreaCaptacao())) {
				valor = parametroCalculo.getValorTaxa().doubleValue();

				if (!Util.isNull(parametroCalculo.getFatorMultiplicador())) {
					valor += detalhamentoBoleto.getAreaCaptacao()
							* parametroCalculo.getFatorMultiplicador().doubleValue();
				}

				break;
			}
		}

		this.detalhamentoBoletoACalcular.setValor(new BigDecimal(valor));
	}
	
	private void calcularValorPulverizacao(DetalhamentoBoleto detalhamentoBoleto, Collection<ParametroCalculo> parametros) {
		double valor = 0;

		for (ParametroCalculo parametroCalculo : parametros) {

			if (!parametroCalculo.isExigeCalculo()) {
				continue;
			}

			Double areaMaxima = null;
			if (!Util.isNull(parametroCalculo.getAreaMaxima())) {
				areaMaxima = parametroCalculo.getAreaMaxima().doubleValue();
			}

			Double areaMinima = null;
			if (!Util.isNull(parametroCalculo.getAreaMinima())) {
				areaMinima = parametroCalculo.getAreaMinima().doubleValue();
			}else{
				areaMinima = 0.0;
			}

			if ((Util.isNull(areaMaxima) || areaMaxima >= detalhamentoBoleto.getAreaCaptacao()) && (areaMinima < detalhamentoBoleto.getAreaCaptacao())) {
				valor = parametroCalculo.getValorTaxa().doubleValue();

				if (!Util.isNull(parametroCalculo.getFatorMultiplicador())) {
					valor += detalhamentoBoleto.getAreaCaptacao()
							* parametroCalculo.getFatorMultiplicador().doubleValue();
				}

				break;
			}
		}

		this.detalhamentoBoletoACalcular.setValor(new BigDecimal(valor));
	}

	private Collection<ParametroCalculo> carregarParametros(DetalhamentoBoleto detalhamentoBoleto) {
		Collection<ParametroCalculo> parametros = this.parametroCalculoService.listarParametrosBoleto(detalhamentoBoleto, null);
		this.detalhamentoBoletoACalcular.setParametros(parametros);
		return parametros;
	}

	public void calcularPorcentagemLicencaAnterior() {
		try {
			if (!Util.isNull(detalhamentoBoletoACalcular.getValorLicencaAnterior())) {
				Double valor = (this.detalhamentoBoletoACalcular.getValorLicencaAnterior().doubleValue() * 0.3);
				this.detalhamentoBoletoACalcular.setValor(new BigDecimal(valor));
			} else {
				this.calcularValorIrrigacao(detalhamentoBoletoACalcular);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void atualizarValorBoleto() {

		try {
			this.atualizarListaDeDetalhamentos();
			this.boletoAutomatizadoController.calcular();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void atualizarListaDeDetalhamentos() {
		List<DetalhamentoBoleto> detalhamentosBoleto = (List<DetalhamentoBoleto>) this.boletoAutomatizadoController.getDetalhamentosBoleto();
		int indexOf = detalhamentosBoleto.indexOf(detalhamentoBoletoACalcular);
		detalhamentosBoleto.set(indexOf, detalhamentoBoletoACalcular);
	}
	
	public boolean getIsRendered() {
		
		return isIrrigacao() || isPulverizacao();
		
	}

	public boolean isIrrigacao() {
		
		if(!Util.isNull(this.detalhamentoBoletoACalcular.getIdeAtoAmbiental()) && this.detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.IRRIGACAO.getId())){
			return true;
		}
		else if (!Util.isNull(this.detalhamentoBoletoACalcular.getIdeTipoFinalidadeUsoAgua()) && this.detalhamentoBoletoACalcular.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId())) {
			return true;
		}
		return false;
	}
	
	public boolean isPulverizacao() {
		
		if(!Util.isNull(this.detalhamentoBoletoACalcular.getIdeAtoAmbiental()) && this.detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental().equals(AtoAmbientalEnum.PULVERIZACAO.getId())){
			return true;
		}
		else if (!Util.isNull(this.detalhamentoBoletoACalcular.getIdeTipoFinalidadeUsoAgua())&&this.detalhamentoBoletoACalcular.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.PULVERIZACAO_AGRICOLA.getId())) {
			return true;
		}
		return false;
	}

	public boolean isRevisaoOuProrrogacao() {
		return this.detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental()
				.equals(AtoAmbientalEnum.PRORROGACAO_AUTORIZACAO.getId())
				|| this.detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental()
						.equals(AtoAmbientalEnum.PRORROGACAO_LICENCA.getId())
				|| this.detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental()
						.equals(AtoAmbientalEnum.PRORROGACAO_AUTORIZACAO_SUPRESSAO.getId())
				|| this.detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental()
						.equals(AtoAmbientalEnum.REVISAO_CONDICIONANTE.getId())
				|| this.detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental()
						.equals(AtoAmbientalEnum.PPV_EPMF.getId())
				|| this.detalhamentoBoletoACalcular.getIdeAtoAmbiental().getIdeAtoAmbiental()
						.equals(AtoAmbientalEnum.PPV_OUT.getId());						
	}

	public DetalhamentoBoleto getDetalhamentoBoletoACalcular() {
		return detalhamentoBoletoACalcular;
	}

	public void setDetalhamentoBoletoACalcular(DetalhamentoBoleto detalhamentoBoletoACalcular) {
		this.detalhamentoBoletoACalcular = detalhamentoBoletoACalcular;
	}

}
