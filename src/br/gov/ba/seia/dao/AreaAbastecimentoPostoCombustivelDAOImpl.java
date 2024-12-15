package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.AreaAbastecimentoPostoCombustivel;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class AreaAbastecimentoPostoCombustivelDAOImpl extends BaseDAO<AreaAbastecimentoPostoCombustivel> {

	@Inject
	IDAO<AreaAbastecimentoPostoCombustivel> areaAbastecimentoPostoCombustivelDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Collection<AreaAbastecimentoPostoCombustivel> objetos) {
		for (AreaAbastecimentoPostoCombustivel areaAbastecimentoPostoCombustivel : objetos) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idePostoCombustivel", areaAbastecimentoPostoCombustivel.getPostoCombustivel().getIdeLac());
			map.put("ideTipoAreaAbastecimento", areaAbastecimentoPostoCombustivel.getTipoAreaAbastecimento().getIdeTipoAreaAbastecimento());
			this.areaAbastecimentoPostoCombustivelDAO.executarNamedQuery("AreaAbastecimentoPostoCombustivel.removerByIdePK",map);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AreaAbastecimentoPostoCombustivel> carregarByIdeLac(Integer ideLac) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaAbastecimentoPostoCombustivel.class)
				.createAlias("postoCombustivel", "posto")
				.createAlias("tipoAreaAbastecimento", "area")
				.createAlias("tipoPermeabilidadeAntesReforma", "antesReforma")
				.createAlias("tipoPermeabilidadeDepoisReforma", "depoisReforma");
			
			criteria.setProjection(Projections.projectionList()
										
					.add(Projections.property("area.ideTipoAreaAbastecimento"),"tipoAreaAbastecimento.ideTipoAreaAbastecimento")
					.add(Projections.property("area.dscTipoAreaAbastecimento"),"tipoAreaAbastecimento.dscTipoAreaAbastecimento")
					
					.add(Projections.property("antesReforma.ideTipoPermeabilidade"),"tipoPermeabilidadeAntesReforma.ideTipoPermeabilidade")
					.add(Projections.property("antesReforma.dscTipoPermeabilidade"),"tipoPermeabilidadeAntesReforma.dscTipoPermeabilidade")
					
					.add(Projections.property("depoisReforma.ideTipoPermeabilidade"),"tipoPermeabilidadeDepoisReforma.ideTipoPermeabilidade")
					.add(Projections.property("depoisReforma.dscTipoPermeabilidade"),"tipoPermeabilidadeDepoisReforma.dscTipoPermeabilidade")
					
					.add(Projections.property("posto.ideLac"),"postoCombustivel.ideLac")
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(AreaAbastecimentoPostoCombustivel.class));
			
			criteria.add(Restrictions.eq("posto.ideLac", ideLac));
			
			return  this.areaAbastecimentoPostoCombustivelDAO.listarPorCriteria(criteria);
	}
	
	@Override
	protected IDAO<AreaAbastecimentoPostoCombustivel> getDao() {
		return this.areaAbastecimentoPostoCombustivelDAO;
	}

}
