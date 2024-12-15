package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.Secao;
import br.gov.ba.seia.util.Util;

public class SecaoDAOImpl implements SecaoDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<Secao> getSecoes(Secao pSecao) {

    	StringBuilder lEjbql = new StringBuilder("select secao from Secao secao ");

    	if (!Util.isNull(pSecao)) {

    		lEjbql.append("where secao.indExcluido = :indExcluido ");

    		if (!Util.isNull(pSecao.getIdeSecao())) lEjbql.append(" AND secao.ideSecao = :ideSecao");

    		if (!Util.isNull(pSecao.getDscSecao())) lEjbql.append(" AND lower(secao.dscSecao) LIKE lower(:dscSecao)");
    	}

    	lEjbql.append(" order by secao.dscSecao");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pSecao)) {

    		lQuery.setParameter("indExcluido", pSecao.getIndExcluido());

    		if (!Util.isNull(pSecao.getIdeSecao())) lQuery.setParameter("ideSecao", pSecao.getIdeSecao());

    		if (!Util.isNull(pSecao.getDscSecao())) lQuery.setParameter("dscSecao", pSecao.getDscSecao() + "%");
    	}

    	return lQuery.getResultList();
    }
}