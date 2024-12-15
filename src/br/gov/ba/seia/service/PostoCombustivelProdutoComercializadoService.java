package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.PostoCombustivelProdutoComercializadoDAOImpl;
import br.gov.ba.seia.entity.PostoCombustivelProdutoComercializado;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PostoCombustivelProdutoComercializadoService extends BaseService<PostoCombustivelProdutoComercializado> {

	@Inject
	PostoCombustivelProdutoComercializadoDAOImpl postoCombustivelProdutoComercializadoDAOImpl;

	public Collection<PostoCombustivelProdutoComercializado> carregarProdutosComercializadosByIdeLac(Integer ideLac)  {
		return this.postoCombustivelProdutoComercializadoDAOImpl.carregarProdutosComercializadosByIdeLac(ideLac);
	}

	@Override
	protected BaseDAO<PostoCombustivelProdutoComercializado> getDaoImpl() {
		return this.postoCombustivelProdutoComercializadoDAOImpl;
	}

	public void remover(Integer ideProduto,Integer idePostoCombustivel)  {
		this.postoCombustivelProdutoComercializadoDAOImpl.remover(ideProduto,idePostoCombustivel);
	}

}
