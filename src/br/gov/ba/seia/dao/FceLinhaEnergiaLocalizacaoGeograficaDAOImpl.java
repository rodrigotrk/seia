package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.FceLinhaEnergiaLocalizacaoGeografica;
@Stateless
public class FceLinhaEnergiaLocalizacaoGeograficaDAOImpl {

	@Inject
	private IDAO<FceLinhaEnergiaLocalizacaoGeografica> fceLinhaEnergiaLocalizacaoGeograficaIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaLocalizacaoGeografica(FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeografica){
	
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLinhaEnergia", fceLinhaEnergiaLocalizacaoGeografica.getIdeFceLinhaEnergia());
		parameters.put("ideLocalizacaoGeografica", fceLinhaEnergiaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
		
		fceLinhaEnergiaLocalizacaoGeograficaIDAO.executarNamedQuery("FceLinhaEnergiaLocalizacaoGeografica.removeFceLinhaEnergiaLocalizacaoGeografica", parameters);
	}
}
