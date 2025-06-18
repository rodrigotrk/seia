package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.util.Util;

public class FuncionalidadeDAOImpl implements FuncionalidadeDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<Funcionalidade> getFuncionalidades(Funcionalidade pFuncionalidade) {

    	StringBuilder lEjbql = new StringBuilder("select funcionalidade from Funcionalidade funcionalidade ");

    	if (!Util.isNull(pFuncionalidade)) {

    		lEjbql.append("where funcionalidade.indExcluido = :indExcluido ");

    		if (!Util.isNull(pFuncionalidade.getIdeFuncionalidade())) lEjbql.append(" AND funcionalidade.ideFuncionalidade = :ideFuncionalidade");

    		if (!Util.isNull(pFuncionalidade.getDscFuncionalidade())) lEjbql.append(" AND lower(funcionalidade.dscFuncionalidade) LIKE lower(:dscFuncionalidade)");
    	}

    	lEjbql.append(" order by funcionalidade.dscFuncionalidade");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pFuncionalidade)) {

    		lQuery.setParameter("indExcluido", pFuncionalidade.getIndExcluido());

    		if (!Util.isNull(pFuncionalidade.getIdeFuncionalidade())) lQuery.setParameter("ideFuncionalidade", pFuncionalidade.getIdeFuncionalidade());

    		if (!Util.isNull(pFuncionalidade.getDscFuncionalidade())) lQuery.setParameter("dscFuncionalidade", pFuncionalidade.getDscFuncionalidade() + "%");
    	}

    	return lQuery.getResultList();
    }
}