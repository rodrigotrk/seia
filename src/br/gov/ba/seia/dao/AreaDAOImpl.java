package br.gov.ba.seia.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Equipe;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class AreaDAOImpl {
	
	@Inject
	private IDAO<Area> areaDAO;
	
	public Area buscarAreaCoordenadorAtual(Integer ideProcesso) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Equipe.class);
		criteria
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.add(Property.forName("ideEquipe").eq(
					DetachedCriteria.forClass(Equipe.class)
						.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
						.add(Restrictions.eq("p.ideProcesso", ideProcesso))
						.setProjection(Projections.max("ideEquipe"))
			))
			
			.setProjection(Projections.property("a.ideArea").as("ideArea"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Area.class))
		;
		return areaDAO.buscarPorCriteria(criteria);
	}

	@SuppressWarnings("unchecked")
    public List<Area> getAreas(Area pArea) {

    	StringBuilder lEjbql = new StringBuilder("select area from Area area ");

    	if (!Util.isNull(pArea)) {

    		lEjbql.append("where area.indExcluido = :indExcluido ");

    		if (!Util.isNull(pArea.getIdeArea())) lEjbql.append(" AND area.ideArea = :ideArea");

    		if (!Util.isNull(pArea.getNomArea())) lEjbql.append(" AND lower(area.nomArea) LIKE lower(:nomArea)");
    	}

    	lEjbql.append(" order by area.nomArea");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());
    	
    	if (!Util.isNull(pArea)) {

    		lQuery.setParameter("indExcluido", pArea.getIndExcluido());

    		if (!Util.isNull(pArea.getIdeArea())) lQuery.setParameter("ideArea", pArea.getIdeArea());

    		if (!Util.isNull(pArea.getNomArea())) lQuery.setParameter("nomArea", pArea.getNomArea() + "%");
    	}

    	return lQuery.getResultList();
    }
    
    @SuppressWarnings("unchecked")
	public List<Area> getAreasTipo(Area pArea) {

    	StringBuilder lEjbql = new StringBuilder("select area from Area area ");

    	if (!Util.isNull(pArea)) {

    		lEjbql.append("where area.indExcluido = :indExcluido ");
    		lEjbql.append("AND area.ideTipoArea = :ideTipoArea");

    		if (!Util.isNull(pArea.getIdeArea())) lEjbql.append(" AND area.ideArea = :ideArea");

    		if (!Util.isNull(pArea.getNomArea())) lEjbql.append(" AND lower(area.nomArea) LIKE lower(:nomArea)");
    	}

    	lEjbql.append(" order by area.nomArea");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pArea)) {

    		lQuery.setParameter("indExcluido", pArea.getIndExcluido());

    		if (!Util.isNull(pArea.getIdeArea())) lQuery.setParameter("ideArea", pArea.getIdeArea());
    		
    		if (!Util.isNull(pArea.getIdeTipoArea())) lQuery.setParameter("ideTipoArea", pArea.getIdeTipoArea());

    		if (!Util.isNull(pArea.getNomArea())) lQuery.setParameter("nomArea", pArea.getNomArea() + "%");
    	}

    	return lQuery.getResultList();
    }
}