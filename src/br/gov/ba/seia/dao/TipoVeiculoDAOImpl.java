package br.gov.ba.seia.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoVeiculo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoVeiculoDAOImpl {

	@Inject
	private IDAO<TipoVeiculo> tipoVeiculoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoVeiculo> listarTodos() {
		return this.tipoVeiculoDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoVeiculo> listarVeiculoPorResiduo() {
		return this.tipoVeiculoDAO.buscarPorNamedQuery("TipoVeiculo.findByResiduo");
	}
}	
