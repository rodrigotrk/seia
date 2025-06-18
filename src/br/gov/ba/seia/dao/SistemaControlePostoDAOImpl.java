package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.SistemaControlePosto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class SistemaControlePostoDAOImpl extends BaseDAO<SistemaControlePosto> {

	@Inject
	IDAO<SistemaControlePosto> sistemaControlePostoDAO;
	
	@Override
	protected IDAO<SistemaControlePosto> getDao() {
		return this.sistemaControlePostoDAO;
	}

	public Collection<SistemaControlePosto> carregarByIdeLac(Integer ideLac)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(SistemaControlePosto.class)
				.createAlias("idePostoCombustivel", "posto")
				.createAlias("ideTipoSistemaControlePosto", "tipo");
			
			criteria.setProjection(Projections.projectionList()
										
					.add(Projections.property("ideSistemaControlePosto"),"ideSistemaControlePosto")
					.add(Projections.property("indImplantado"),"indImplantado")
					.add(Projections.property("dtcImplantacao"),"dtcImplantacao")
					
					.add(Projections.property("dtcImplantacao"),"dtcImplantacao")
					
					.add(Projections.property("posto.ideLac"),"idePostoCombustivel.ideLac")
					
					.add(Projections.property("tipo.ideTipoSistemaControlePosto"),"ideTipoSistemaControlePosto.ideTipoSistemaControlePosto")
					.add(Projections.property("tipo.dscTipoSistemaControlePosto"),"ideTipoSistemaControlePosto.dscTipoSistemaControlePosto")
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(SistemaControlePosto.class));
			
			criteria.add(Restrictions.eq("posto.ideLac", ideLac));
			
			return this.sistemaControlePostoDAO.listarPorCriteria(criteria);
	}

	public void remover(Integer ideSistemaControlePosto)  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideSistemaControlePosto",ideSistemaControlePosto);
		this.sistemaControlePostoDAO.executarNamedQuery("SistemaControlePosto.removerByIde",map);
	}

}
