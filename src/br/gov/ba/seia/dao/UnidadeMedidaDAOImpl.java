package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class UnidadeMedidaDAOImpl {
	
	@Inject
	IDAO<UnidadeMedida> unidadeMedidaDAO;
	
	public void salvarUnidadeMedida(UnidadeMedida unidadeMedida)  {
		unidadeMedidaDAO.salvarOuAtualizar(unidadeMedida);
	}
	
	
	public Collection<UnidadeMedida> filtrarListaUnidadeMedida(UnidadeMedida unidadeMedida) {
		return unidadeMedidaDAO.listarPorExemplo(unidadeMedida);
	}
	
	public void excluirUnidadeMedida(UnidadeMedida unidadeMedida)  {
		unidadeMedidaDAO.remover(unidadeMedida);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<UnidadeMedida> filtrarListaUnidadeMedidaSouce(Collection<UnidadeMedida> listunidadeMedidas)  {
		
		 DetachedCriteria criteria = DetachedCriteria.forClass(UnidadeMedida.class);
		 Collection<Integer> ids = new ArrayList<Integer>();
		 for (UnidadeMedida um : listunidadeMedidas) {
			ids.add(um.getIdeUnidadeMedida());
		}
		 Criterion in = Restrictions.in("ideUnidadeMedida", ids);
		 criteria.add(Restrictions.not(in));  
		 return unidadeMedidaDAO.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UnidadeMedida> listarUnidadeMedida(List<UnidadeMedida> listunidadeMedidas)  {
		
		 DetachedCriteria criteria = DetachedCriteria.forClass(UnidadeMedida.class);
		 Collection<Integer> ids = new ArrayList<Integer>();
		 for (UnidadeMedida um : listunidadeMedidas) {
			ids.add(um.getIdeUnidadeMedida());
		 }
		 
		 // clausula not in
		 criteria.add(Restrictions.in("ideUnidadeMedida", ids));
		 return unidadeMedidaDAO.listarPorCriteria(criteria);
		
	}
	
	public Collection<UnidadeMedida> listarUnidadeMedida() {
		return unidadeMedidaDAO.listarTodos();
	}

	

	public Collection<UnidadeMedida> listarUnidadeMedidaTipologia(TipologiaGrupo tipologiaGrupo)  {
		 DetachedCriteria criteria = DetachedCriteria.forClass(UnidadeMedida.class);
		 criteria.createAlias("unidadeMedidaTipologiaGrupoCollection", "grupoUnidadeMedida");
		 criteria.add(Restrictions.eq("grupoUnidadeMedida.ideTipologiaGrupo", tipologiaGrupo));
		 return unidadeMedidaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<UnidadeMedida> listarUnidadeMedidaTipologia(TipologiaEnum tipologiaEnum)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(UnidadeMedida.class)
			.createAlias("unidadeMedidaTipologiaGrupoCollection", "umtg")
			.createAlias("umtg.ideTipologiaGrupo", "tg")
			.add(Restrictions.eq("tg.ideTipologia.ideTipologia", tipologiaEnum.getId()))
			.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideUnidadeMedida"), "ideUnidadeMedida")
						.add(Projections.property("codUnidadeMedida"), "codUnidadeMedida")
						.add(Projections.property("nomUnidadadeMedida"), "nomUnidadadeMedida")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(UnidadeMedida.class))
			;
		return unidadeMedidaDAO.listarPorCriteria(criteria);
	}
	
}