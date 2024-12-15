package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.BaseDAO;
import br.gov.ba.seia.dao.PostoCombustivelTipoServicoDAOImpl;
import br.gov.ba.seia.entity.PostoCombustivelTipoServico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PostoCombustivelTipoServicoService extends BaseService<PostoCombustivelTipoServico>{

	@Inject
	PostoCombustivelTipoServicoDAOImpl postoCombustivelTipoServicoDAOImpl;
	
	public void remover(Integer ideLac)  {
		this.postoCombustivelTipoServicoDAOImpl.remover(ideLac);
	}
	
	public Collection<PostoCombustivelTipoServico> carregarByIdeLac(Integer ideLac)  {
		return this.postoCombustivelTipoServicoDAOImpl.carregarByIdeLac(ideLac);
	}
	
	@Override
	protected BaseDAO<PostoCombustivelTipoServico> getDaoImpl() {
		return this.postoCombustivelTipoServicoDAOImpl;
	}

}
