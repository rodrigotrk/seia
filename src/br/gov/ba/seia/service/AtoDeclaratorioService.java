package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.AtoDeclaratorioDAOImpl;
import br.gov.ba.seia.entity.AtoDeclaratorio;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtoDeclaratorioService {

	
	@Inject
	private AtoDeclaratorioDAOImpl atoDeclaratorioDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtoDeclaratorio(AtoDeclaratorio atoDeclaratorio) {
	
		try {
			atoDeclaratorioDAOImpl.salvar(atoDeclaratorio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtoDeclaratorio carregarAtoDeclaratorio(AtoDeclaratorio ideAtoDeclaratorio) {

		try {
			return atoDeclaratorioDAOImpl.carregarAtoDeclaratorio(ideAtoDeclaratorio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	
}
