package br.gov.ba.seia.facade;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.CerhProcessoDAOImpl;
import br.gov.ba.seia.dao.CerhSituacaoTipoUsoDAOImpl;
import br.gov.ba.seia.dao.TipoCorpoHidricoDAOImpl;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhSituacaoTipoUso;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoCorpoHidrico;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.service.CerhLocalizacaoGeograficaService;
import br.gov.ba.seia.service.CerhProcessoService;
import br.gov.ba.seia.service.CerhService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.TipoFinalidadeUsoAguaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author Alexandre Queiroz 
 * @since 24/03/2017
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhFacade {

	@EJB
	private CerhService cerhService;
	
	@EJB
	private CerhProcessoService cerhProcessoService;
	
	@EJB
	private CerhProcessoDAOImpl cerhProcessoDaoImpl;
	
	@EJB
	private CerhSituacaoTipoUsoDAOImpl situacaoCaptacaoDAO;

	@EJB
	private CerhLocalizacaoGeograficaService cerhLocalizacaoGeograficaService;

	@EJB
	private TipoFinalidadeUsoAguaService tipoFinalidadeUsoAguaService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	
	@EJB
	private MunicipioService municipioService;

	@EJB
	private TipoCorpoHidricoDAOImpl tipoCorpoHidricoDAOImpl;
	
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh buscarParaHistorico(Cerh c){
		try {
			return cerhService.buscarParaHistorico(c);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CerhLocalizacaoGeografica> listarParaHistorico(Cerh c, TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) {
		try {
			return cerhLocalizacaoGeograficaService.listarParaHistorico(c, tipoUsoRecursoHidricoEnum);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTheGeomPersistido(LocalizacaoGeografica localizacaoGeografica) {
		try {
			return !Util.isNullOuVazio(localizacaoGeograficaServiceFacade.retornarGeometriaShapeByLocalizacaoGeografica(localizacaoGeografica));
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações da localização geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAguaCerh(TipologiaEnum tipologiaEnum){
		try{
			return tipoFinalidadeUsoAguaService.listarTipoFinalidadeUsoAguaCerh(tipologiaEnum);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAgua(){
		try{
			return tipoFinalidadeUsoAguaService.listarTipoFinalidadeUsoAgua();
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhSituacaoTipoUso> listarSituacaoCaptacao(){
		try{
			return situacaoCaptacaoDAO.listar();
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoCorpoHidrico> listarTipoCorpoHidrico(){
		try{
			return tipoCorpoHidricoDAOImpl.listar();
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CerhProcesso> listarCerhProcessoPorCerh(Cerh ideCerh, TipoUsoRecursoHidricoEnum tipoRecursoHidricoEnum){
		try{
			return cerhProcessoDaoImpl.listarCerhProcessoPorCerh(ideCerh, tipoRecursoHidricoEnum); 
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh getNumeroCerhAnterior(Cerh cerh) {
		try{
			return cerhService.getNumeroCerhAnterior(cerh);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhProcesso buscarCerhProcesso(String chave, Cerh c1) {
		try{
			return cerhProcessoService.buscarCerhProcesso(chave, c1);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	
}