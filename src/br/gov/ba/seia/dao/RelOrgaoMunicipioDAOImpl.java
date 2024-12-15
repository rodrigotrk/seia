package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.RelOrgaoMunicipio;
import br.gov.ba.seia.util.Util;

public class RelOrgaoMunicipioDAOImpl {

    @SuppressWarnings("unchecked")
    public List<RelOrgaoMunicipio> getOrgaosMunicipios(RelOrgaoMunicipio pRelOrgaoMunicipio) {

    	StringBuilder lEjbql = new StringBuilder("select relOrgaoMunicipio from RelOrgaoMunicipio relOrgaoMunicipio ");

    	if (!Util.isNull(pRelOrgaoMunicipio)) {

    		lEjbql.append("where 1 = 1 ");

    		if (!Util.isNullOuVazio(pRelOrgaoMunicipio.getOrgao()) && !Util.isNullOuVazio(pRelOrgaoMunicipio.getOrgao().getIdeOrgao())) lEjbql.append(" AND relOrgaoMunicipio.orgao.ideOrgao = :ideOrgao");

    		if (!Util.isNullOuVazio(pRelOrgaoMunicipio.getMunicipio()) && !Util.isNullOuVazio(pRelOrgaoMunicipio.getMunicipio().getIdeMunicipio())) lEjbql.append(" AND relOrgaoMunicipio.municipio.ideMunicipio = :ideMunicipio");
    	}

    	lEjbql.append(" order by relOrgaoMunicipio.orgao.dscOrgao, relOrgaoMunicipio.municipio.nomMunicipio");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pRelOrgaoMunicipio)) {

    		if (!Util.isNullOuVazio(pRelOrgaoMunicipio.getOrgao()) && !Util.isNullOuVazio(pRelOrgaoMunicipio.getOrgao().getIdeOrgao())) lQuery.setParameter("ideOrgao", pRelOrgaoMunicipio.getOrgao().getIdeOrgao());

    		if (!Util.isNullOuVazio(pRelOrgaoMunicipio.getMunicipio()) && !Util.isNullOuVazio(pRelOrgaoMunicipio.getMunicipio().getIdeMunicipio())) lQuery.setParameter("ideMunicipio", pRelOrgaoMunicipio.getMunicipio().getIdeMunicipio()); 
    	}

    	return lQuery.getResultList();
    }
}