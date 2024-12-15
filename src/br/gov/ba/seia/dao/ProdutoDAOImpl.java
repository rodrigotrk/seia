package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.entity.TipoProduto;
import br.gov.ba.seia.util.Util;

public class ProdutoDAOImpl extends BaseDAO<Produto>{

	@Inject
	IDAO<Produto> produtoDAO;

	@Override
	protected IDAO<Produto> getDao() {
		return this.produtoDAO;
	}

	public Produto carregarProdutoByDescricao(String dscProduto) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("dscProduto", dscProduto);
		return this.produtoDAO.obterPorNamedQuery("Produto.findByDscProduto", parametros);
	}

	/**
	 * @author eduardo.fernandes
	 * @return lista de produtos cadastrados no banco de acordo com o TipoProduto
	 * @throws Exception
	 */
	public List<Produto> listarProdutosByIdeTipoProduto(Produto produto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideTipoProduto", produto.getIdeTipoProduto());
		return produtoDAO.buscarPorNamedQuery("Produto.findByIdeTipoProduto", map);
	}
	
	/**
	 * Método que usa a descrição do Produto para retornar uma lista filtrada do banco.
	 * @param produto
	 * @return List<Produto>
	 * @author eduardo.fernandes
	 */
	@SuppressWarnings("unchecked")
	public List<Produto> filtrarProdutos(Produto produto) {
		StringBuilder lEjbql = new StringBuilder("SELECT p FROM Produto p ");
		if (!Util.isNull(produto)) {
			produto.setIdeTipoProduto(new TipoProduto(1));
			lEjbql.append("WHERE p.ideTipoProduto = :ideTipoProduto ");
			if (!Util.isNull(produto.getIdeProduto())){
				lEjbql.append(" AND p.ideProduto = :ideProduto");
			}
			if (!Util.isNull(produto.getDscProduto())) {
				lEjbql.append(" AND (LOWER(p.dscProduto) LIKE LOWER(:dscProduto) OR LOWER(p.numOnu) LIKE LOWER(:dscProduto))");
			}
		}
		lEjbql.append(" ORDER BY p.numOnu");
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createQuery(lEjbql.toString());
		if (!Util.isNull(produto)) {
			lQuery.setParameter("ideTipoProduto", produto.getIdeTipoProduto());
			if (!Util.isNull(produto.getIdeProduto())) {
				lQuery.setParameter("ideProduto", produto.getIdeProduto());
			}
			if (!Util.isNull(produto.getDscProduto())) {
				lQuery.setParameter("dscProduto", "%" + produto.getDscProduto() + "%");
			}
		}
		return lQuery.getResultList();
	}	
	
	/**
	 * @Comentários Método que retorna um Produto pelo seu Id no banco.
	 * @param ide
	 * @return Produto
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Produto obterProdutoByIdeProduto(Integer ide) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideProduto", ide);
		return produtoDAO.buscarEntidadePorNamedQuery("Produto.findByideProduto", parameters);
	}
}
