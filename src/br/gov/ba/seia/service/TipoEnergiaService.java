package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipoEnergiaDAOImpl;
import br.gov.ba.seia.entity.TipoEnergia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoEnergiaService {
	
	@Inject
	private TipoEnergiaDAOImpl tipoEnergiaDaoImpl; 

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<TipoEnergia> buscarTiposEnergias() throws Exception {

		return tipoEnergiaDaoImpl.buscarTiposEnergias();
	}
	
}
