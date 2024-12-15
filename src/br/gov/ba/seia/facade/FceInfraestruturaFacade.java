package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.controller.FceInfraestruturaController;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceOutorgaInfraestrutura;
import br.gov.ba.seia.entity.FceOutorgaTipoInfraestrutura;
import br.gov.ba.seia.entity.FceOutorgaTipoInfraestruturaUtilizada;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;
import br.gov.ba.seia.service.FceOutorgaInfraestruturaService;
import br.gov.ba.seia.service.FceOutorgaTipoInfraestruturaService;
import br.gov.ba.seia.service.FceOutorgaTipoInfraestruturaUtilizadaService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.TipoPeriodoDerivacaoService;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceInfraestruturaFacade {
	
	@EJB
	TipoPeriodoDerivacaoService tipoPeriodoDerivacaoService;
	
	@EJB
	FceOutorgaTipoInfraestruturaService fceOutorgaTipoInfraestruturaService;
	
	@EJB
	FceOutorgaInfraestruturaService fceOutorgaInfraestruturaService;
	
	@EJB
	FceOutorgaTipoInfraestruturaUtilizadaService fceOutorgaTipoInfraestruturaUtilizadaService;
	
	@EJB
	OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoPeriodoDerivacao> listarTipoPeriodoDerivacaoPorListaTipoPeriodoDerivacaoEnum(List<TipoPeriodoDerivacaoEnum> listTipoPeriodoDerivacaoEnum){
		try {
			return tipoPeriodoDerivacaoService.listarTipoPeriodoDerivacaoPorListaTipoPeriodoDerivacaoEnum(listTipoPeriodoDerivacaoEnum);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaTipoInfraestrutura> listarTodosFceOutorgaTipoInfraestrutura(){
		try {
			return fceOutorgaTipoInfraestruturaService.listarTodosFceOutorgaTipoInfraestrutura();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceOutorgaInfraestrutura buscarFceOutorgaInfraestruturaPorFce(Fce fce) {
		try {
			return fceOutorgaInfraestruturaService.buscarFceOutorgaInfraestruturaPorFce(fce);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceInfraestruturaController fic) {
		try {
			fic.prepararParaFinalizar();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaTipoInfraestruturaUtilizada> listarFceOutorgaTipoInfraestruturaUtilizadaPorFceOutorgaTipoInfraestrutura(FceOutorgaInfraestrutura infra) {
		try {
			return fceOutorgaTipoInfraestruturaUtilizadaService.listarPorFceOutorgaTipoInfraestrutura(infra);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceOutorgaTipoInfraestruturaUtilizadaPorFceOutorgaInfraestrutura(FceOutorgaInfraestrutura infra) {
		try {
			fceOutorgaTipoInfraestruturaUtilizadaService.excluirPorFceOutorgaInfraestrutura(infra);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceOutorgaTipoInfraestruturaUtilizada(FceOutorgaTipoInfraestruturaUtilizada tipoInfraUtilizada) {
		try {
			fceOutorgaTipoInfraestruturaUtilizadaService.salvar(tipoInfraUtilizada);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceOutorgaInfraestrutura(FceOutorgaInfraestrutura infra) {
		try {
			fceOutorgaInfraestruturaService.salvar(infra);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaPorRequerimento(Requerimento req) {
		try {
			return outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByRequerimento(req);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}