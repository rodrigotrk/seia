package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLinhaEnergiaLocalizacaoGeograficaDAOImpl;
import br.gov.ba.seia.entity.FceLinhaEnergiaLocalizacaoGeografica;

@Stateless
public class FceLinhaEnergiaLocalizacaoGeograficaService {

	@Inject
	private FceLinhaEnergiaLocalizacaoGeograficaDAOImpl fceLinhaEnergiaLocalizacaoGeograficaDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaLocalizacaoGeografica(FceLinhaEnergiaLocalizacaoGeografica fceLinhaEnergiaLocalizacaoGeografica) {
		
		fceLinhaEnergiaLocalizacaoGeograficaDAOImpl.removerFceLinhaEnergiaLocalizacaoGeografica(fceLinhaEnergiaLocalizacaoGeografica);
	}
}
