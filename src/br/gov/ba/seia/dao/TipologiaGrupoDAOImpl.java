package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class TipologiaGrupoDAOImpl implements TipologiaGrupoDAOIf {

	@Inject
	IDAO<TipologiaGrupo> tipologiaGrupoDAO;
	
    @Override
	@SuppressWarnings("unchecked")
    public List<TipologiaGrupo> getTipologiasGrupos(TipologiaGrupo pTipologiaGrupo) {

    	StringBuilder lEjbql = new StringBuilder("select tipologiaGrupo from TipologiaGrupo tipologiaGrupo ");

    	if (!Util.isNull(pTipologiaGrupo)) {

    		lEjbql.append("where tipologiaGrupo.indExcluido = :indExcluido ");

    		if (!Util.isNull(pTipologiaGrupo.getIdeTipologia())) lEjbql.append(" AND tipologiaGrupo.ideTipologia = :ideTipologia");
    	}

    	lEjbql.append(" order by tipologiaGrupo.ideTipologia.desTipologia");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pTipologiaGrupo)) {

    		lQuery.setParameter("indExcluido", pTipologiaGrupo.getIndExcluido());

    		if (!Util.isNull(pTipologiaGrupo.getIdeTipologia())) lQuery.setParameter("ideTipologia", pTipologiaGrupo.getIdeTipologia());
    	}

    	return lQuery.getResultList();
    }

    //SELECT u FROM UnidadeMedidaTipologiaGrupo u left join u.ideTipologiaGrupo tg left join tg.empreendimentoTipologiaCollection et 
    //WHERE et.empreendimento.ideEmpreendimento = :ideEmpreendimento
	public Collection<TipologiaGrupo> buscarPorEmpreendimento(Integer ideEmpreendimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaGrupo.class)
				.createAlias("unidadeMedidaTipologiaGrupo", "unidadeMedidaGrupo",JoinType.LEFT_OUTER_JOIN)
				.createAlias("unidadeMedidaGrupo.ideUnidadeMedida", "unidadeMedida",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipologia", "tipologia")
				.createAlias("empreendimentoTipologiaCollection", "emp")
		
				.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipologiaGrupo").as("ideTipologiaGrupo"))
				.add(Projections.property("unidadeMedidaGrupo.ideUnidadeMedidaTipologiaGrupo").as("unidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo"))
				.add(Projections.property("unidadeMedida.ideUnidadeMedida").as("unidadeMedidaTipologiaGrupo.ideUnidadeMedida.ideUnidadeMedida"))
				.add(Projections.property("unidadeMedida.codUnidadeMedida").as("unidadeMedidaTipologiaGrupo.ideUnidadeMedida.codUnidadeMedida"))
				.add(Projections.property("tipologia.ideTipologia").as("ideTipologia.ideTipologia"))
				.add(Projections.property("tipologia.desTipologia").as("ideTipologia.desTipologia"))
				.add(Projections.property("tipologia.codTipologia").as("ideTipologia.codTipologia"))
		);
		
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(TipologiaGrupo.class));
		criteria.add(Restrictions.eq("emp.empreendimento.ideEmpreendimento", ideEmpreendimento));
		
		return tipologiaGrupoDAO.listarPorCriteria(criteria);
	}
	
	public TipologiaGrupo buscarTipologiaPorEmpreendimento(Integer ideEmpreendimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaGrupo.class)
				.createAlias("unidadeMedidaTipologiaGrupo", "unidadeMedidaGrupo",JoinType.LEFT_OUTER_JOIN)
				.createAlias("unidadeMedidaGrupo.ideUnidadeMedida", "unidadeMedida",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipologia", "tipologia")
				.createAlias("empreendimentoTipologiaCollection", "emp")
				
				.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipologiaGrupo").as("ideTipologiaGrupo"))
				.add(Projections.property("unidadeMedidaGrupo.ideUnidadeMedidaTipologiaGrupo").as("unidadeMedidaTipologiaGrupo.ideUnidadeMedidaTipologiaGrupo"))
				.add(Projections.property("unidadeMedida.ideUnidadeMedida").as("unidadeMedidaTipologiaGrupo.ideUnidadeMedida.ideUnidadeMedida"))
				.add(Projections.property("unidadeMedida.codUnidadeMedida").as("unidadeMedidaTipologiaGrupo.ideUnidadeMedida.codUnidadeMedida"))
				.add(Projections.property("tipologia.ideTipologia").as("ideTipologia.ideTipologia"))
				.add(Projections.property("tipologia.desTipologia").as("ideTipologia.desTipologia"))
				.add(Projections.property("tipologia.codTipologia").as("ideTipologia.codTipologia")))
		
				.add(Restrictions.eq("emp.empreendimento.ideEmpreendimento", ideEmpreendimento))
				
				.setResultTransformer(new AliasToNestedBeanResultTransformer(TipologiaGrupo.class));
		
		return tipologiaGrupoDAO.buscarPorCriteriaMaxResult(criteria);
	}
}