package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.DadoGeografico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DadoGeograficoDAOImpl extends AbstractDAO<DadoGeografico>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<DadoGeografico> dao;
	
	@Override
	protected IDAO<DadoGeografico> getDAO() {
		return dao;
	}

}
