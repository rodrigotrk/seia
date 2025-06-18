package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.SituacaoDaeDaoImpl;
import br.gov.ba.seia.entity.SituacaoDae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SituacaoDaeService {

	@Inject
	SituacaoDaeDaoImpl situacaoDaeDaoImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SituacaoDae> listarSituacaoDae() throws Exception{
		return situacaoDaeDaoImpl.listarSituacaoDae();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SituacaoDae buscarSituacaoByIde(Integer IdeSituacao) throws Exception{
		return situacaoDaeDaoImpl.listarSituacaoByIde(IdeSituacao);
	}

}