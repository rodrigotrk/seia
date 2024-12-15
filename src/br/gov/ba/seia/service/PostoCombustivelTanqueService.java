package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.PostoCombustivelTanqueDAOImpl;
import br.gov.ba.seia.entity.PostoCombustivelTanque;
import br.gov.ba.seia.entity.PostoCombustivelTanqueProduto;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PostoCombustivelTanqueService extends BaseService<PostoCombustivelTanque> {

	@Inject
	PostoCombustivelTanqueDAOImpl postoCombustivelTanqueDAOImpl;
	
	public Collection<PostoCombustivelTanque> carregarByIdeLac(Integer ideLac)  {
		return this.postoCombustivelTanqueDAOImpl.carregarByIdeLac(ideLac);
	}
	
	public Collection<PostoCombustivelTanqueProduto> carregarProdutoTanqueByIdeTanque(Integer ideTanque)  {
		return this.postoCombustivelTanqueDAOImpl.carregarProdutoTanqueByIdeTanque(ideTanque);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerProdutos(Collection<PostoCombustivelTanqueProduto> produtos)  {
		postoCombustivelTanqueDAOImpl.removerProdutos(produtos);
	}
	
	@Override
	protected BaseDAO<PostoCombustivelTanque> getDaoImpl() {
		return this.postoCombustivelTanqueDAOImpl;
	}

	
}
