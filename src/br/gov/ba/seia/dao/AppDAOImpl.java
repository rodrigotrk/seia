package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.App;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AppDAOImpl {

	@Inject
	IDAO<App> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public App filtrarById(App pApp) {
		return dao.buscarEntidadePorExemplo(pApp);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(App pApp)  {
		dao.salvar(pApp);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(App pApp) {
		dao.salvarOuAtualizar(pApp);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(App pApp)  {
		dao.atualizar(pApp);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(App pApp) {
		dao.remover(pApp);
	}
}
