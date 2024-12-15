package br.gov.ba.seia.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.TipoVeiculo;
import br.gov.ba.seia.service.TipoVeiculoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoVeiculoFacade {
	

	@EJB
	private TipoVeiculoService tipoVeiculoService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoVeiculo> listarTodos() throws Exception {
		return this.tipoVeiculoService.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoVeiculo> listarVeiculoPorResiduo() throws Exception {
		return this.tipoVeiculoService.listarVeiculoPorResiduo();
	}

	
}
