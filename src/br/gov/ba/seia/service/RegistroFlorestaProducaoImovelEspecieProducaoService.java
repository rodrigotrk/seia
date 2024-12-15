package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.RegistroFlorestaProducaoImovelEspecieProducaoDaoImpl;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelEspecieProducao;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistroFlorestaProducaoImovelEspecieProducaoService {

	@Inject
	private RegistroFlorestaProducaoImovelEspecieProducaoDaoImpl registroFlorestaProducaoImovelEspecieProducaoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoImovelEspecieProducao(RegistroFlorestaProducaoImovelEspecieProducao ideRegistroFlorestaProducaoImovelEspecieProducao) {
		try {
			registroFlorestaProducaoImovelEspecieProducaoDAOImpl.excluirRegistroFlorestaProducaoImovelEspecieProducao(ideRegistroFlorestaProducaoImovelEspecieProducao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoImovelEspecieProducao(List<RegistroFlorestaProducaoImovelEspecieProducao> ideRegistroFlorestaProducaoImovelEspecieProducao) {
		try {
			registroFlorestaProducaoImovelEspecieProducaoDAOImpl.excluirRegistroFlorestaProducaoImovelEspecieProducao(ideRegistroFlorestaProducaoImovelEspecieProducao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovelEspecieProducao(List<RegistroFlorestaProducaoImovelEspecieProducao> registroFlorestaProducaoImovelEspecieProducaoList) {
		try {
			registroFlorestaProducaoImovelEspecieProducaoDAOImpl.salvarRegistroFlorestaProducaoImovelEspecieProducao(registroFlorestaProducaoImovelEspecieProducaoList);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegistroFlorestaProducaoImovelEspecieProducao> listarRegistroFlorestaProducaoImovelEspecieProducao(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel) {
		try {
			return registroFlorestaProducaoImovelEspecieProducaoDAOImpl.listarRegistroFlorestaProducaoImovelEspecieProducao(ideRegistroFlorestaProducaoImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
}
