package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.EstadoCivil;
import br.gov.ba.seia.util.Util;

public class EstadoCivilDAOImpl implements EstadoCivilDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<EstadoCivil> getEstadosCivil(EstadoCivil pEstadoCivil) {

    	StringBuilder lEjbql = new StringBuilder("select estadoCivil from EstadoCivil estadoCivil ");

    	if (!Util.isNull(pEstadoCivil)) {

    		lEjbql.append("where 1 = 1 ");

    		if (!Util.isNull(pEstadoCivil.getIdeEstadoCivil())) lEjbql.append(" AND estadoCivil.ideEstadoCivil = :ideEstadoCivil");

    		if (!Util.isNull(pEstadoCivil.getNomEstadoCivil())) lEjbql.append(" AND lower(estadoCivil.nomEstadoCivil) LIKE lower(:nomEstadoCivil)");
    	}

    	lEjbql.append(" order by estadoCivil.nomEstadoCivil");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pEstadoCivil)) {

    		if (!Util.isNull(pEstadoCivil.getIdeEstadoCivil())) lQuery.setParameter("ideEstadoCivil", pEstadoCivil.getIdeEstadoCivil());

    		if (!Util.isNull(pEstadoCivil.getNomEstadoCivil())) lQuery.setParameter("nomEstadoCivil", pEstadoCivil.getNomEstadoCivil() + "%");
    	}

    	return lQuery.getResultList();
    }
}