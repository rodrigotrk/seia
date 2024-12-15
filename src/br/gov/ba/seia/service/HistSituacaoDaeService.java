package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CerhHistSituacaoDaeDAOImpl;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.HistSituacaoDae;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Usuario;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class HistSituacaoDaeService {

	@Inject
	private CerhHistSituacaoDaeDAOImpl cerhHistSituacaoDaeDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarHistSituacaoDae(HistSituacaoDae cerhHistSituacaoDae) {
		cerhHistSituacaoDaeDAOImpl.salvarHistSituacaoDae(cerhHistSituacaoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarHistSituacaoDae(HistSituacaoDae cerhHistSituacaoDae) {
		cerhHistSituacaoDaeDAOImpl.atualizarHistSituacaoDae(cerhHistSituacaoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<HistSituacaoDae> listarHistSituacaoDae() {
		return cerhHistSituacaoDaeDAOImpl.listarHistSituacaoDae();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public HistSituacaoDae obterUltimoHistSituacaoDae(Dae ideDae)  {
		return cerhHistSituacaoDaeDAOImpl.obterUltimoHistSituacaoDae(ideDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public HistSituacaoDae obterHistSituacaoDae(Dae ideDae, Requerimento req)  {
		return cerhHistSituacaoDaeDAOImpl.obterHistSituacaoDae(ideDae, req);
	}
}
