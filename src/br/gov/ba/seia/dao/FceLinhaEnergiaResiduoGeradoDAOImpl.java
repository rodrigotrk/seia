package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.FceLinhaEnergiaResiduoGerado;

@Stateless
public class FceLinhaEnergiaResiduoGeradoDAOImpl {

	@Inject
	IDAO<FceLinhaEnergiaResiduoGerado> fceLinhaEnergiaResiduoGeradoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaResiduoGeradoById(FceLinhaEnergiaResiduoGerado fceLinhaEnergiaResiduoGerado) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLinhaEnergia", fceLinhaEnergiaResiduoGerado.getIdeFceLinhaEnergia());
		parameters.put("ideTipoResiduoGerado", fceLinhaEnergiaResiduoGerado.getIdeTipoResiduoGerado());
		fceLinhaEnergiaResiduoGeradoDAO.executarNamedQuery("FceLinhaEnergiaResiduoGerado.removeFceLinhaEnergiaResiduoGerado", parameters);
	}
}
