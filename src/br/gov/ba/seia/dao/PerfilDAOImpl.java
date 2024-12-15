package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.Perfil;
import br.gov.ba.seia.util.Util;

public class PerfilDAOImpl implements PerfilDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<Perfil> getPerfis(Perfil pPerfil) {

    	StringBuilder lEjbql = new StringBuilder("select perfil from Perfil perfil left join fetch perfil.ideFuncionalidadePrincipal ");

    	if (!Util.isNull(pPerfil)) {

    		lEjbql.append("where perfil.indExcluido = :indExcluido ");

    		if (!Util.isNull(pPerfil.getIdePerfil())) lEjbql.append(" AND perfil.idePerfil = :idePerfil");

    		if (!Util.isNull(pPerfil.getDscPerfil())) lEjbql.append(" AND lower(perfil.dscPerfil) LIKE lower(:dscPerfil)");
    	}

    	lEjbql.append(" order by perfil.dscPerfil");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pPerfil)) {

    		lQuery.setParameter("indExcluido", pPerfil.getIndExcluido());

    		if (!Util.isNull(pPerfil.getIdePerfil())) lQuery.setParameter("idePerfil", pPerfil.getIdePerfil());

    		if (!Util.isNull(pPerfil.getDscPerfil())) lQuery.setParameter("dscPerfil", pPerfil.getDscPerfil() + "%");
    	}

    	return lQuery.getResultList();
    }
}