package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.Cronograma;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

public class CronogramaDAOImpl {
	
	@Inject
	IDAO<Cronograma> cronogramaDAO;
	/**
	 * 
	 * LINHA COMENTADA BY #6329	criteria.add(Restrictions.eq("ideArea", area));
	 * */
	public List<Cronograma> filtrarCronogramaByProcesso(Processo processo, Area area){
		DetachedCriteria criteria = DetachedCriteria.forClass(Cronograma.class, "cronograma" );		
		criteria.createAlias("controleCronogramaCollection", "controleCronograma", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideProcesso", processo));

		criteria.add(Restrictions.or(Restrictions.eq("controleCronograma.indExcluido", Boolean.FALSE), 
				     Restrictions.isNull("controleCronograma.indExcluido")));
		
		return cronogramaDAO.listarPorCriteria(criteria, Order.asc("controleCronograma.dtcItemPrevista"));
	
	}
	
	public void criarCronogramaDoProcesso(Processo processo, Area area){
		Cronograma crono = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(Cronograma.class, "cronograma" );		
		criteria.add(Restrictions.eq("ideProcesso", processo));
		criteria.add(Restrictions.eq("ideArea", area));
		
		crono = cronogramaDAO.buscarPorCriteria(criteria);
		
		if(Util.isNullOuVazio(crono)){
			crono = new Cronograma(processo);
			crono.setIdeArea(area);
			cronogramaDAO.salvar(crono);
		}
		else
			Log4jUtil.log(CronogramaDAOImpl.class.getName(), org.apache.log4j.Level.INFO, new Exception("CronogramaDAOImpl.criarCronogramaDoProcesso() - CRONOGRAMA JÁ EXISTE!"));
	}

    @SuppressWarnings("unchecked")
    public Cronograma getCronograma(Cronograma pCronograma) {

    	StringBuilder lEjbql = new StringBuilder("select cronograma from Cronograma cronograma ");

    	if (!Util.isNull(pCronograma)) {
    		lEjbql.append("where 1 = 1 ");
    		if (!Util.isNull(pCronograma.getIdeCronograma())) 
    			lEjbql.append(" AND cronograma.ideCronograma = :ideCronograma");

    		if (!Util.isNull(pCronograma.getIdeProcesso()) && !Util.isNull(pCronograma.getIdeProcesso().getIdeProcesso()))
    			lEjbql.append(" AND cronograma.ideProcesso.ideProcesso = :ideProcesso");
    	}

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pCronograma)) {

    		if (!Util.isNull(pCronograma.getIdeCronograma())) lQuery.setParameter("ideCronograma", pCronograma.getIdeCronograma());

    		if (!Util.isNull(pCronograma.getIdeProcesso()) && !Util.isNull(pCronograma.getIdeProcesso().getIdeProcesso())) lQuery.setParameter("ideProcesso", pCronograma.getIdeProcesso().getIdeProcesso());
    	}

    	Collection<Cronograma> lColecaoCronograma = lQuery.getResultList();

    	for (Cronograma lCronograma : lColecaoCronograma) {

    		return lCronograma;
    	}

   		return null;
    }
    
}