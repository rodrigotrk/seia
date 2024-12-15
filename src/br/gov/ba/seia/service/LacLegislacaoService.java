package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.LacLegislacaoDAOImpl;
import br.gov.ba.seia.entity.LacLegislacao;

/***
 * 
 * @author luis
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LacLegislacaoService {

	@Inject
	LacLegislacaoDAOImpl lacLegislacaoDAOImpl;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(LacLegislacao pLacLegislacao)  {
		this.lacLegislacaoDAOImpl.salvarOuAtualizar(pLacLegislacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(LacLegislacao pLacLegislacao)  {
		this.lacLegislacaoDAOImpl.atualizar(pLacLegislacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(LacLegislacao pLacLegislacao)  {
		this.lacLegislacaoDAOImpl.excluir(pLacLegislacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LacLegislacao> carregarByIdeLac(int pIdeLac)  {
		return this.lacLegislacaoDAOImpl.carregarByIdeLac(pIdeLac);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LacLegislacao> carregarByIdeRequerimentoWithLac(int pIdeRequerimento)  {
		return this.lacLegislacaoDAOImpl.carregarByIdeRequerimentoWithLac(pIdeRequerimento);
	}
}