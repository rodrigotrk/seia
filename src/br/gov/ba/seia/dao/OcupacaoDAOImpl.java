package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.Ocupacao;
import br.gov.ba.seia.util.Util;

public class OcupacaoDAOImpl implements OcupacaoDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<Ocupacao> getOcupacoes(Ocupacao pOcupacao) {

    	StringBuilder lEjbql = new StringBuilder("select ocupacao from Ocupacao ocupacao ");

    	if (!Util.isNull(pOcupacao)) {

    		lEjbql.append("where 1 = 1 ");

    		if (!Util.isNull(pOcupacao.getIdeOcupacao())) lEjbql.append(" AND ocupacao.ideOcupacao = :ideOcupacao");

    		if (!Util.isNull(pOcupacao.getNomOcupacao())) lEjbql.append(" AND lower(ocupacao.nomOcupacao) LIKE lower(:nomOcupacao)");
    	}

    	lEjbql.append(" order by ocupacao.nomOcupacao");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pOcupacao)) {

    		if (!Util.isNull(pOcupacao.getIdeOcupacao())) lQuery.setParameter("ideOcupacao", pOcupacao.getIdeOcupacao());

    		if (!Util.isNull(pOcupacao.getNomOcupacao())) lQuery.setParameter("nomOcupacao", pOcupacao.getNomOcupacao() + "%");
    	}

    	return lQuery.getResultList();
    }
}