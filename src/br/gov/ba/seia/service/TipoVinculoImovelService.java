package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoVinculoImovel;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoVinculoImovelService {

	@Inject
	IDAO<TipoVinculoImovel> tipoVinculoImovelDAO;

	public Collection<TipoVinculoImovel> listarTipoVinculoImoveis() {
		return tipoVinculoImovelDAO.listarTodos();
	}

	public List<TipoVinculoImovel> listarTipoVinculoImoveisCefir() {
		List<TipoVinculoImovel> listaTipoVinculos = tipoVinculoImovelDAO.listarTodos();
		listaTipoVinculos.remove(6);
		listaTipoVinculos.remove(5);
		listaTipoVinculos.remove(4);
		listaTipoVinculos.remove(3);
		listaTipoVinculos.remove(2);
		return listaTipoVinculos;
	}

}
