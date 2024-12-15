package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.NivelCompetencia;
import br.gov.ba.seia.util.Util;

public class NivelCompetenciaDAOImpl implements NivelCompetenciaDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<NivelCompetencia> getNiveisCompetencia(NivelCompetencia pNivelCompetencia) {

    	StringBuilder lEjbql = new StringBuilder("select nivelCompetencia from NivelCompetencia nivelCompetencia ");

    	if (!Util.isNull(pNivelCompetencia)) {

    		lEjbql.append("where 1 = 1 ");

    		if (!Util.isNull(pNivelCompetencia.getIdeNivelCompetencia())) lEjbql.append(" AND nivelCompetencia.ideNivelCompetencia = :ideNivelCompetencia");
    	}

    	lEjbql.append(" order by nivelCompetencia.ideNivelCompetencia");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pNivelCompetencia)) {

    		if (!Util.isNull(pNivelCompetencia.getIdeNivelCompetencia())) lQuery.setParameter("ideNivelCompetencia", pNivelCompetencia.getIdeNivelCompetencia());
    	}

    	return lQuery.getResultList();
    }
}