package br.gov.ba.seia.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.DAOGeneric;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.exception.AppExceptionError;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Service {
	
	private static final Logger logger = Logger.getLogger(Service.class.getName());  
		
	@Inject
	DAOGeneric<Pessoa> daoModelo;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void execute(Pessoa modelo) throws AppExceptionError {
		logger.info("Debuging.....");
//		daoModelo.salvar(modelo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void execute(List<Pessoa> lista) throws AppExceptionError  {
		System.out.println("Debuging......");		
		//throw new AppExceptionError("Business Erro!!");
	}
}
