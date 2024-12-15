package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLinhaEnergiaDestinoResiduoDAOImpl;
import br.gov.ba.seia.entity.FceLinhaEnergiaDestinoResiduo;

@Stateless
public class FceLinhaEnergiaDestinoResiduoService {

	@Inject
	private FceLinhaEnergiaDestinoResiduoDAOImpl fceLinhaEnergiaDestinoResiduoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaDestinoResiduo(FceLinhaEnergiaDestinoResiduo fceLinhaEnergiaDestinoResiduo) {
		
		fceLinhaEnergiaDestinoResiduoDAOImpl.removerFceLinhaEnergiaDestinoResiduo(fceLinhaEnergiaDestinoResiduo);
	}
}
