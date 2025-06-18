package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.FceLinhaEnergiaTipoEnergia;
@Stateless
public class FceLinhaEnergiaTipoEnergiaDAOImpl {

	@Inject
	private IDAO<FceLinhaEnergiaTipoEnergia> fceLinhaEnergiaFceLinhaEnergiaTipoEnergiaDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaTipoEnergia(FceLinhaEnergiaTipoEnergia fceLinhaEnergiaTipoEnergia) {
	
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLinhaEnergia", fceLinhaEnergiaTipoEnergia.getIdeFceLinhaEnergia());
		parameters.put("ideFceLinhaEnergiaTipoEnergia", fceLinhaEnergiaTipoEnergia.getIdeFceLinhaEnergiaTipoEnergia());
		
		fceLinhaEnergiaFceLinhaEnergiaTipoEnergiaDAOImpl.executarNamedQuery("FceLinhaEnergiaTipoEnergia.removeFceLinhaEnergiaTipoEnergia", parameters);
	}
}
