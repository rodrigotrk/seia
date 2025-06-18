package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.RegistroFlorestaProducaoImovelPlantioDAOImpl;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelPlantio;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistroFlorestaProducaoImovelPlantioService {

	@Inject
	private RegistroFlorestaProducaoImovelPlantioDAOImpl registroFlorestaProducaoImovelPlantioDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoImovelPlantio(RegistroFlorestaProducaoImovelPlantio ideRegistroFlorestaProducaoImovelPlantio) {
		try {
			if(ideRegistroFlorestaProducaoImovelPlantio.getIdeRegistroFlorestaImovelPlantio()!=null){
				registroFlorestaProducaoImovelPlantioDAOImpl.excluirRegistroFlorestaProducaoImovelPlantio(ideRegistroFlorestaProducaoImovelPlantio);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoImovelPlantio(List<RegistroFlorestaProducaoImovelPlantio> ideRegistroFlorestaProducaoImovelPlantio) {
		try {
			registroFlorestaProducaoImovelPlantioDAOImpl.excluirRegistroFlorestaProducaoImovelPlantio(ideRegistroFlorestaProducaoImovelPlantio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovelPlantio(List<RegistroFlorestaProducaoImovelPlantio> registroFlorestaProducaoImovelPlantioList) {
		try {
			registroFlorestaProducaoImovelPlantioDAOImpl.salvarRegistroFlorestaProducaoImovelPlantio(registroFlorestaProducaoImovelPlantioList);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovelPlantio(RegistroFlorestaProducaoImovelPlantio registroFlorestaProducaoImovelPlantio) {
		try {
			registroFlorestaProducaoImovelPlantioDAOImpl.salvarRegistroFlorestaProducaoImovelPlantio(registroFlorestaProducaoImovelPlantio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegistroFlorestaProducaoImovelPlantio> listarRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel) {
		try {
			return registroFlorestaProducaoImovelPlantioDAOImpl.listarRegistroFlorestaProducaoImovel(ideRegistroFlorestaProducaoImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
}
