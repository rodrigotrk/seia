package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLinhaEnergiaResiduoGeradoDAOImpl;
import br.gov.ba.seia.entity.FceLinhaEnergiaResiduoGerado;

@Stateless
public class FceLinhaEnergiaResiduoGeradoService {

	@Inject
	private FceLinhaEnergiaResiduoGeradoDAOImpl fceLinhaEnergiaResiduoGeradoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaResiduoGeradoById(
			FceLinhaEnergiaResiduoGerado fceLinhaEnergiaResiduoGerado)
			 {

		fceLinhaEnergiaResiduoGeradoDAOImpl.removerFceLinhaEnergiaResiduoGeradoById(fceLinhaEnergiaResiduoGerado);
	}

}
