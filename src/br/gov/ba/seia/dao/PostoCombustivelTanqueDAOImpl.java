package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.PostoCombustivelTanque;
import br.gov.ba.seia.entity.PostoCombustivelTanqueProduto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class PostoCombustivelTanqueDAOImpl extends BaseDAO<PostoCombustivelTanque> {

	@Inject
	IDAO<PostoCombustivelTanque> postoCombustivelTanqueDAO;
	
	@Inject
	IDAO<PostoCombustivelTanqueProduto> postoCombustivelTanqueProdutoDAO;
	
	@Override
	protected IDAO<PostoCombustivelTanque> getDao() {
		return this.postoCombustivelTanqueDAO;
	}

	@Override
	public void remover(Collection<PostoCombustivelTanque> objetos)  {
		for (PostoCombustivelTanque postoCombustivelTanque : objetos) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ideTanque", postoCombustivelTanque.getIdeTanque());
			this.postoCombustivelTanqueDAO.executarNamedQuery("PostoCombustivelTanque.removerByIde",map);
		}
	}
	
	public void removerProdutos(Collection<PostoCombustivelTanqueProduto> objetos)  {
		for (PostoCombustivelTanqueProduto postoCombustivelTanque : objetos) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ideTanqueProduto", postoCombustivelTanque.getIdePostoTanqueProduto());
			this.postoCombustivelTanqueProdutoDAO.executarNamedQuery("PostoCombustivelTanqueProduto.removerByIde",map);
		}
	}
	
	public Collection<PostoCombustivelTanque> carregarByIdeLac(Integer ideLac)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PostoCombustivelTanque.class)
				.createAlias("idePostoCombustivel", "posto")
				.createAlias("ideTipoEstruturaTanque", "estrutura")
				.createAlias("ideTipoParedeTanque", "parede")
				.createAlias("ideTipoTanquePosto", "tipo");
			
			criteria.setProjection(Projections.projectionList()
										
					.add(Projections.property("ideTanque"),"ideTanque")
					.add(Projections.property("nomeTanque"),"nomeTanque")
					.add(Projections.property("indInstalado"),"indInstalado")
					.add(Projections.property("indTipoDescargaLocal"),"indTipoDescargaLocal")
					.add(Projections.property("dscEspTecnica"),"dscEspTecnica")
					.add(Projections.property("dtcInstalacao"),"dtcInstalacao")
					.add(Projections.property("dtcUltimaInspecao"),"dtcUltimaInspecao")
					.add(Projections.property("dtcEstanqueidade"),"dtcEstanqueidade")
					.add(Projections.property("dtcEstanqueidade"),"dtcEstanqueidade")
										
					.add(Projections.property("tipo.ideTipoTanquePosto"),"ideTipoTanquePosto.ideTipoTanquePosto")
					.add(Projections.property("tipo.dscTipoTanquePosto"),"ideTipoTanquePosto.dscTipoTanquePosto")

					.add(Projections.property("parede.ideTipoParedeTanque"),"ideTipoParedeTanque.ideTipoParedeTanque")
					.add(Projections.property("parede.dscTipoParedeTanque"),"ideTipoParedeTanque.dscTipoParedeTanque")
					
					.add(Projections.property("estrutura.ideTipoEstruturaTanque"),"ideTipoEstruturaTanque.ideTipoEstruturaTanque")
					.add(Projections.property("estrutura.dscTipoEstruturaTanque"),"ideTipoEstruturaTanque.dscTipoEstruturaTanque")
					
					.add(Projections.property("posto.ideLac"),"idePostoCombustivel.ideLac")
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(PostoCombustivelTanque.class));
			
			criteria.add(Restrictions.eq("posto.ideLac", ideLac));
			
			return  this.postoCombustivelTanqueDAO.listarPorCriteria(criteria);
	}


	public Collection<PostoCombustivelTanqueProduto> carregarProdutoTanqueByIdeTanque(Integer ideTanque)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PostoCombustivelTanqueProduto.class)
				.createAlias("ideProduto", "produto")
				.createAlias("postoCombustivelTanque", "tanque");
			
			criteria.setProjection(Projections.projectionList()
										
					.add(Projections.property("idePostoTanqueProduto"),"idePostoTanqueProduto")
					.add(Projections.property("valCapacidade"),"valCapacidade")
										
					.add(Projections.property("produto.ideProduto"),"ideProduto.ideProduto")
					.add(Projections.property("produto.dscProduto"),"ideProduto.dscProduto")

					.add(Projections.property("tanque.ideTanque"),"postoCombustivelTanque.ideTanque")

			).setResultTransformer(new AliasToNestedBeanResultTransformer(PostoCombustivelTanqueProduto.class));
			
			criteria.add(Restrictions.eq("tanque.ideTanque", ideTanque));
			
			return  this.postoCombustivelTanqueProdutoDAO.listarPorCriteria(criteria);
	}

	
}
