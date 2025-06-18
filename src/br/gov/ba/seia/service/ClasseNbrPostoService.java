package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.ClasseNbrPostoDAOImpl;
import br.gov.ba.seia.entity.ClasseNbrPosto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClasseNbrPostoService extends BaseService<ClasseNbrPosto>{

	@Inject
	ClasseNbrPostoDAOImpl classeNbrPostoDAOImpl; 
	
	@Override
	protected BaseDAO<ClasseNbrPosto> getDaoImpl() {
		return this.classeNbrPostoDAOImpl;
	}

	
}
