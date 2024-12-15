package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.RegistroFlorestaProducaoImovelDaoImpl;
import br.gov.ba.seia.entity.RegistroFlorestaProducao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistroFlorestaProducaoImovelService {

	@Inject
	private RegistroFlorestaProducaoImovelDaoImpl registroFlorestaProducaoImovelDaoImpl;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel registroFlorestaProducaoImovel){
		try {
			registroFlorestaProducaoImovelDaoImpl.salvarRegistroFlorestaProducaoImovel(registroFlorestaProducaoImovel);
				
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovel(List<RegistroFlorestaProducaoImovel> registroFlorestaProducaoImovel){
		try {
			registroFlorestaProducaoImovelDaoImpl.salvarRegistroFlorestaProducaoImovel(registroFlorestaProducaoImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegistroFlorestaProducaoImovel> listarRegistroFlorestaProducaoImovel(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
		try {
			return registroFlorestaProducaoImovelDaoImpl.listarRegistroFlorestaProducaoImovel(ideRegistroFlorestaProducao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel) {
		try {
			registroFlorestaProducaoImovelDaoImpl.excluirIdeRegistroFlorestaProducaoImovel(ideRegistroFlorestaProducaoImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	

}
