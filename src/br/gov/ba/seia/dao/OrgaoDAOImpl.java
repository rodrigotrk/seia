package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.util.Util;

public class OrgaoDAOImpl implements OrgaoDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<Orgao> getOrgaos(Orgao pOrgao) {

    	StringBuilder lEjbql = new StringBuilder("select orgao from Orgao orgao ");

    	if (!Util.isNull(pOrgao)) {

    		lEjbql.append("where orgao.indExcluido = :indExcluido ");

    		if (!Util.isNull(pOrgao.getIdeOrgao())) lEjbql.append(" AND orgao.ideOrgao = :ideOrgao");

    		if (!Util.isNull(pOrgao.getDscOrgao())) lEjbql.append(" AND lower(orgao.dscOrgao) LIKE lower(:dscOrgao)");
    	}

    	lEjbql.append(" order by orgao.dscOrgao");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pOrgao)) {

    		lQuery.setParameter("indExcluido", pOrgao.getIndExcluido());

    		if (!Util.isNull(pOrgao.getIdeOrgao())) lQuery.setParameter("ideOrgao", pOrgao.getIdeOrgao());

    		if (!Util.isNull(pOrgao.getDscOrgao())) lQuery.setParameter("dscOrgao", pOrgao.getDscOrgao() + "%");
    	}

    	return lQuery.getResultList();
    }
}