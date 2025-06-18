package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLinhaEnergiaDAOImpl;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLinhaEnergia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLinhaEnergiaService {

	@Inject
	private FceLinhaEnergiaDAOImpl fceLinhaEnerdiaDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(FceLinhaEnergia fceLinhaEnergia) {
		fceLinhaEnerdiaDAOImpl.salvarOuAtualizar(fceLinhaEnergia);
	}
	
	public FceLinhaEnergia buscarFceLinhaEnergiaPorFce(Fce fce) {
		return fceLinhaEnerdiaDAOImpl.buscarFceLinhaEnergiaPorFce(fce);
	}
}
