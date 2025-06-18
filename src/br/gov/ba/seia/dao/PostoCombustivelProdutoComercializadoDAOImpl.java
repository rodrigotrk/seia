package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.PostoCombustivelProdutoComercializado;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class PostoCombustivelProdutoComercializadoDAOImpl extends BaseDAO<PostoCombustivelProdutoComercializado> {
	
	@Inject
	IDAO<PostoCombustivelProdutoComercializado> postoCombustivelProdutoComercializadoDAO;
	
	public Collection<PostoCombustivelProdutoComercializado> carregarProdutosComercializadosByIdeLac(Integer ideLac)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PostoCombustivelProdutoComercializado.class)
			.createAlias("produto", "produto")
			.createAlias("postoCombustivel", "posto");
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("qtdVendida"),"qtdVendida")
				
				.add(Projections.property("produto.ideProduto"),"produto.ideProduto")
				.add(Projections.property("produto.dscProduto"),"produto.dscProduto")
				
				.add(Projections.property("posto.ideLac"),"postoCombustivel.ideLac")
		).setResultTransformer(new AliasToNestedBeanResultTransformer(PostoCombustivelProdutoComercializado.class));
		
		criteria.add(Restrictions.eq("posto.ideLac", ideLac));
		
		return this.postoCombustivelProdutoComercializadoDAO.listarPorCriteria(criteria);
	}
	
	@Override
	protected IDAO<PostoCombustivelProdutoComercializado> getDao() {
		return this.postoCombustivelProdutoComercializadoDAO;
	}

	public void remover(Integer ideProduto,Integer idePostoCombustivel)  {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideProduto", ideProduto);
		map.put("idePostoCombustivel", idePostoCombustivel);
		this.postoCombustivelProdutoComercializadoDAO.executarNamedQuery("PostoCombustivelProdutoComercializado.removerByIde",map);
	}
	

}
