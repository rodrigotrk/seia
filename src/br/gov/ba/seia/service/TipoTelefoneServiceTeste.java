package br.gov.ba.seia.service;

import java.util.List;

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
public class TipoTelefoneServiceTeste {

	@Inject
	private IDAO<TipoTelefone> tipoTelefoneDao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoTelefone> searchAll() {

		return tipoTelefoneDao.buscarPorNamedQuery("TipoTelefone.findAll");
	}

}
