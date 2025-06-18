package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.Telefone;

public class TelefoneDAOImpl extends AbstractDAO<Telefone>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<Telefone> telefoneIDAO;
	
	@Override
	protected IDAO<Telefone> getDAO() {
		return telefoneIDAO;
	}
	
}
