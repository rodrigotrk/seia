package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.FceLinhaEnergiaDestinoResiduo;

@Stateless
public class FceLinhaEnergiaDestinoResiduoDAOImpl {

	@Inject
	private IDAO<FceLinhaEnergiaDestinoResiduo> fceLinhaEnergiaDestinoResiduoIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaDestinoResiduo(FceLinhaEnergiaDestinoResiduo fceLinhaEnergiaDestinoResiduo) {
	
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLinhaEnergia", fceLinhaEnergiaDestinoResiduo.getIdeFceLinhaEnergia());
		parameters.put("ideDestinoResiduo", fceLinhaEnergiaDestinoResiduo.getIdeDestinoResiduo());
		
		fceLinhaEnergiaDestinoResiduoIDAO.executarNamedQuery("FceLinhaEnergiaDestinoResiduo.removeFceLinhaEnergiaDestinoResiduo", parameters);
	}
}
