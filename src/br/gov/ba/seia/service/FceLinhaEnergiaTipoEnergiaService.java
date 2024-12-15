package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLinhaEnergiaTipoEnergiaDAOImpl;
import br.gov.ba.seia.entity.FceLinhaEnergiaTipoEnergia;

@Stateless
public class FceLinhaEnergiaTipoEnergiaService {

	@Inject
	private FceLinhaEnergiaTipoEnergiaDAOImpl fceLinhaEnergiaTipoEnergiaDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaTipoEnergia(FceLinhaEnergiaTipoEnergia fceLinhaEnergiaTipoEnergia) {
		
		fceLinhaEnergiaTipoEnergiaDAOImpl.removerFceLinhaEnergiaTipoEnergia(fceLinhaEnergiaTipoEnergia);
	}
}
