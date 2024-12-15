package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.ProdutoDAOImpl;
import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.entity.TipoProduto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProdutoService extends BaseService<Produto> {

	@Inject
	ProdutoDAOImpl produtoDAOImpl;
	
	@Override
	public BaseDAO<Produto> getDaoImpl() {
		return this.produtoDAOImpl;
	}
	
	public Produto carregarProdutoByDescricao(String dscProduto)  {
		return produtoDAOImpl.carregarProdutoByDescricao(dscProduto);
	}
	/**
	 * @Comentários Método usado para listar Produto de acordo com o TipoProduto pois a tabela é usada em LacPosto e LacTransportadora.
	 * @author eduardo.fernandes
	 * @return lista de Produtos 
	 * @
	 */
	public List<Produto> carregarListaProdutosByTipoProduto(int ideTipoProduto) {
		Produto produto = new Produto();
		produto.setIdeTipoProduto(new TipoProduto(ideTipoProduto));
		return produtoDAOImpl.listarProdutosByIdeTipoProduto(produto);
	}
	
	/**
	 * @Comentários Método usado na [LacTransporteController] para filtrar a busca de Produto no banco.
	 * @param produto
	 * @return List<Produto>
	 * @author eduardo.fernandes
	 */
	public List<Produto> pesquisarProdutos(Produto produto){
		return new ProdutoDAOImpl().filtrarProdutos(produto);
	}

	/**
	 * @Comentários Método que retorna um Produto de acordo com o Id (Integer) passado pelo usuário.
	 * @param ide
	 * @return Produto
	 * @
	 * @author eduardo.fernandes
	 */
	public Produto obterProdutoByIde(Integer ide) {
		return produtoDAOImpl.obterProdutoByIdeProduto(ide);
	}
}
