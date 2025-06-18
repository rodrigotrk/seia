package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FceLinhaEnergiaTipoSubestacaoDAOImpl;
import br.gov.ba.seia.entity.FceLinhaEnergiaTipoSubestacao;

@Stateless
public class FceLinhaEnergiaTipoSubestacaoService {

	@Inject
	private FceLinhaEnergiaTipoSubestacaoDAOImpl fceLinhaEnergiaTipoSubestacaoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceLinhaEnergiaTipoSubestacao(FceLinhaEnergiaTipoSubestacao fceLinhaEnergiaTipoSubestacao) {
	
		fceLinhaEnergiaTipoSubestacaoDAOImpl.removerFceLinhaEnergiaTipoSubestacao(fceLinhaEnergiaTipoSubestacao);
	}
}
