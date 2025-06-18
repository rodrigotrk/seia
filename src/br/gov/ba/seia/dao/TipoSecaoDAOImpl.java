package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.TipoSecao;
import br.gov.ba.seia.util.Util;

public class TipoSecaoDAOImpl implements TipoSecaoDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<TipoSecao> getTiposSecao(TipoSecao pTipoSecao) {

    	StringBuilder lEjbql = new StringBuilder("select tipoSecao from TipoSecao tipoSecao ");

    	if (!Util.isNull(pTipoSecao)) {

    		lEjbql.append("where tipoSecao.indExcluido = :indExcluido ");

    		if (!Util.isNull(pTipoSecao.getIdeTipoSecao())) lEjbql.append(" AND tipoSecao.ideTipoSecao = :ideTipoSecao");

    		if (!Util.isNull(pTipoSecao.getDscTipoSecao())) lEjbql.append(" AND lower(tipoSecao.dscTipoSecao) LIKE lower(:dscTipoSecao)");
    	}

    	lEjbql.append(" order by tipoSecao.dscTipoSecao");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pTipoSecao)) {

    		lQuery.setParameter("indExcluido", pTipoSecao.getIndExcluido());

    		if (!Util.isNull(pTipoSecao.getIdeTipoSecao())) lQuery.setParameter("ideTipoSecao", pTipoSecao.getIdeTipoSecao());

    		if (!Util.isNull(pTipoSecao.getDscTipoSecao())) lQuery.setParameter("dscTipoSecao", pTipoSecao.getDscTipoSecao() + "%");
    	}

    	return lQuery.getResultList();
    }
}