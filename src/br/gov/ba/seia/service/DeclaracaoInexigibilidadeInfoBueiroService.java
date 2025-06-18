package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoBueiro;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoInexigibilidadeInfoBueiroService {

	@Inject
	private IDAO<DeclaracaoInexigibilidadeInfoBueiro> declaracaoInfoBueiroDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDeclaracaoInfoBueiro(DeclaracaoInexigibilidadeInfoBueiro declaracaoInexigibilidadeInfoBueiro) {
		declaracaoInfoBueiroDAO.remover(declaracaoInexigibilidadeInfoBueiro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidadeInfoBueiro obterDeclaracaoInfoBueiroPor(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoBueiro.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		return this.declaracaoInfoBueiroDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DeclaracaoInexigibilidadeInfoBueiro declaracaoInexigibilidadeInfoBueiro) {
		this.declaracaoInfoBueiroDAO.salvarOuAtualizar(declaracaoInexigibilidadeInfoBueiro);
	}

}
