package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoReservatorio;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoReservatorioService {

	@Inject
	private IDAO<TipoReservatorio> tipoReservatorioIDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoReservatorio> listaTipoReservatorio() {
		return tipoReservatorioIDAO.listarTodos();
	}
	
}
