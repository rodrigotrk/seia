package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoModalidadeErb;

public class TipoModalidadeErbDAOImpl {
	@Inject
	private IDAO<TipoModalidadeErb> daoTipoModalidadeErb;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoModalidadeErb> listar() {
		return daoTipoModalidadeErb.buscarPorNamedQuery("TipoModalidadeErb.findAll");
	}
}
