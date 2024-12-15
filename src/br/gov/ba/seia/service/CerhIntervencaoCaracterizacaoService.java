package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CerhIntervencaoCaracterizacaoDAOImpl;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhIntervencaoCaracterizacaoService {
	
	@Inject
	private CerhIntervencaoCaracterizacaoDAOImpl dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarParaHistorico(CerhLocalizacaoGeografica clg) {
		clg.setIdeCerhIntervencaoCaracterizacao(dao.carregarParaHistorico(clg));
	}
	
}
