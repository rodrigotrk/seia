package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.AreaMunicipio;
import br.gov.ba.seia.util.Util;

public class AreaMunicipioDAOImpl {

	 @SuppressWarnings("unchecked")
	    public List<AreaMunicipio> getAreasMunicipios(AreaMunicipio pAreaMunicipio) {

		 StringBuilder lEjbql = new StringBuilder("select areaMunicipio from AreaMunicipio areaMunicipio ");

	    	if (!Util.isNull(pAreaMunicipio)) {

	    		lEjbql.append("where 1 = 1 ");

	    		if (!Util.isNullOuVazio(pAreaMunicipio.getArea()) && !Util.isNullOuVazio(pAreaMunicipio.getArea().getIdeArea())) lEjbql.append(" AND areaMunicipio.area.ideArea = :ideArea");

	    		if (!Util.isNullOuVazio(pAreaMunicipio.getMunicipio()) && !Util.isNullOuVazio(pAreaMunicipio.getMunicipio().getIdeMunicipio())) lEjbql.append(" AND areaMunicipio.municipio.ideMunicipio = :ideMunicipio");
	    	}

	    	lEjbql.append(" order by areaMunicipio.area.nomArea, areaMunicipio.municipio.nomMunicipio");

	    	EntityManager lEntityManager = DAOFactory.getEntityManager();

	    	lEntityManager.joinTransaction();

	    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

	    	if (!Util.isNull(pAreaMunicipio)) {

	    		if (!Util.isNullOuVazio(pAreaMunicipio.getArea()) && !Util.isNullOuVazio(pAreaMunicipio.getArea().getIdeArea())) lQuery.setParameter("ideArea", pAreaMunicipio.getArea().getIdeArea());

	    		if (!Util.isNullOuVazio(pAreaMunicipio.getMunicipio()) && !Util.isNullOuVazio(pAreaMunicipio.getMunicipio().getIdeMunicipio())) lQuery.setParameter("ideMunicipio", pAreaMunicipio.getMunicipio().getIdeMunicipio()); 
	    	}

	    	return lQuery.getResultList();
	    }
}
