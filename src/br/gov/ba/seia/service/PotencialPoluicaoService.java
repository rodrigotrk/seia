package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PotencialPoluicaoDAOImpl;
import br.gov.ba.seia.entity.PotencialPoluicao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PotencialPoluicaoService {

	@Inject
	PotencialPoluicaoDAOImpl potencialPoluicaoDAOImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvarPotencialPoluicao(PotencialPoluicao potencialPoluicao)  {
		potencialPoluicaoDAOImpl.salvarPotencialPoluicao(potencialPoluicao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PotencialPoluicao> listaPotencialPoluicao()  {
		return potencialPoluicaoDAOImpl.listaPotencialPoluicao();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PotencialPoluicao carregarPotencialPoluicao(Integer id)  {
		return potencialPoluicaoDAOImpl.carregarPotencialPoluicao(id);
	}

}
