package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Requerimento;

public class RequerimentoDAOImpl extends AbstractDAO<Requerimento>{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private IDAO<Requerimento> dao;
	
	@Override
	protected IDAO<Requerimento> getDAO() {
		return dao;
	}

}
