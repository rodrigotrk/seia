package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CerhSituacaoDaeDaoImpl;
import br.gov.ba.seia.entity.SituacaoDae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhSituacaoDaeService {

	@Inject
	CerhSituacaoDaeDaoImpl cerhSituacaoDaeDaoImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SituacaoDae> listarCerhSituacaoDae() {
		return cerhSituacaoDaeDaoImpl.listarCerhSituacaoDae();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SituacaoDae buscarSituacaoByIde(Integer ideSituacao) {
		return cerhSituacaoDaeDaoImpl.listarSituacaoByIde(ideSituacao);
	}

}