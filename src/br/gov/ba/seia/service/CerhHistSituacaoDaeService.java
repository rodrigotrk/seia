package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CerhHistSituacaoDaeDAOImpl;
import br.gov.ba.seia.entity.HistSituacaoDae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhHistSituacaoDaeService {

	@Inject
	private CerhHistSituacaoDaeDAOImpl cerhHistSituacaoDaeDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarHistSituacaoDae(HistSituacaoDae histSituacaoDae) {
		cerhHistSituacaoDaeDAOImpl.salvarHistSituacaoDae(histSituacaoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarHistSituacaoDae(HistSituacaoDae histSituacaoDae) {
		cerhHistSituacaoDaeDAOImpl.atualizarHistSituacaoDae(histSituacaoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<HistSituacaoDae> listarHistSituacaoDae() {
		return cerhHistSituacaoDaeDAOImpl.listarHistSituacaoDae();
	}
	
}
