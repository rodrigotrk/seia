package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipoVeiculoDAOImpl;
import br.gov.ba.seia.entity.TipoVeiculo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoVeiculoService {
	
	@Inject
	TipoVeiculoDAOImpl tipoVeiculoDAOImpl;
	
	public List<TipoVeiculo> listarTodos() throws Exception {
		return this.tipoVeiculoDAOImpl.listarTodos();
	}

	public List<TipoVeiculo> listarVeiculoPorResiduo() throws Exception {
		return this.tipoVeiculoDAOImpl.listarVeiculoPorResiduo();
	}

	
}
