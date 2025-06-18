package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.PostoCombustivelTipoServico;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PostoCombustivelTipoServicoDAOImpl extends BaseDAO<PostoCombustivelTipoServico> {

	@Inject
	IDAO<PostoCombustivelTipoServico> postoCombustivelTipoServicoDAO;

	public void remover(Integer ideLac)  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideLac", ideLac);
		this.postoCombustivelTipoServicoDAO.executarNamedQuery("PostoCombustivelTipoServico.removerByIdeLac",map);
	}
	
	public Collection<PostoCombustivelTipoServico> carregarByIdeLac(Integer ideLac)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PostoCombustivelTipoServico.class)
				.createAlias("postoCombustivel", "posto")
				.createAlias("tipoServicoPosto", "tipo");
			
			criteria.setProjection(Projections.projectionList()
										
					.add(Projections.property("dscOutros"),"dscOutros")
					
					.add(Projections.property("tipo.ideTipoServicoPosto"),"tipoServicoPosto.ideTipoServicoPosto")
					.add(Projections.property("tipo.dscTipoServicoPosto"),"tipoServicoPosto.dscTipoServicoPosto")
					
					.add(Projections.property("posto.ideLac"),"postoCombustivel.ideLac")
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(PostoCombustivelTipoServico.class));
			
			criteria.add(Restrictions.eq("posto.ideLac", ideLac));
			
			return this.postoCombustivelTipoServicoDAO.listarPorCriteria(criteria);
	}

	@Override
	protected IDAO<PostoCombustivelTipoServico> getDao() {
		return this.postoCombustivelTipoServicoDAO;
	}

}
