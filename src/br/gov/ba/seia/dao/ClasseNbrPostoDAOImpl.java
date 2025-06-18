package br.gov.ba.seia.dao;

import javax.inject.Inject;

import br.gov.ba.seia.entity.ClasseNbrPosto;

public class ClasseNbrPostoDAOImpl extends BaseDAO<ClasseNbrPosto> {

	@Inject
	IDAO<ClasseNbrPosto> classeNbrPostoDAO;

	@Override
	protected IDAO<ClasseNbrPosto> getDao() {
		return this.classeNbrPostoDAO;
	}

}
