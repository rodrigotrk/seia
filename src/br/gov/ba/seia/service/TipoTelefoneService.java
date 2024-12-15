package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoTelefone;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoTelefoneService {

	@Inject
	private IDAO<TipoTelefone> daoTipoTelefone;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTipoTelefone(TipoTelefone tipoTelefone) throws Exception {
		daoTipoTelefone.salvar(tipoTelefone);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoTelefone> listarTipoTelefone() {
		return daoTipoTelefone.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoTelefone> filtrarListaTipoTelefone(TipoTelefone tipoTelefone) {
		return daoTipoTelefone.listarPorExemplo(tipoTelefone);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipoTelefone(TipoTelefone tipoTelefone) throws Exception {
		daoTipoTelefone.remover(tipoTelefone);
	}
}
