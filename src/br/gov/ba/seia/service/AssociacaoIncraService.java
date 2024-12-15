package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AssociacaoIncra;
import br.gov.ba.seia.entity.AssociacaoIncraImovelRural;
import br.gov.ba.seia.entity.PessoaJuridica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AssociacaoIncraService {
	
	@Inject
	private IDAO<AssociacaoIncra> associacaoIncraDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AssociacaoIncra> listarAssociacaoIncraPorImovelRural(List<AssociacaoIncraImovelRural> listAssociacaoIncraImovelRural) {
		List<AssociacaoIncra> listAssociacoes = new ArrayList<AssociacaoIncra>();
		for (AssociacaoIncraImovelRural associacaoImovelRural : listAssociacaoIncraImovelRural) {
			listAssociacoes.add(associacaoImovelRural.getIdeAssociacaoIncra());
		}
		return listAssociacoes;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(AssociacaoIncra associacaoIncra) {
		associacaoIncraDAO.salvarOuAtualizar(associacaoIncra);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(AssociacaoIncra associacaoIncra) {
		associacaoIncraDAO.remover(associacaoIncra);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AssociacaoIncra carregarAssociacaoIncraPorPessoaJuridica(PessoaJuridica idePessoaJuridica)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(AssociacaoIncra.class);
		criteria.add(Restrictions.eq("idePessoaJuridica.idePessoaJuridica", idePessoaJuridica.getIdePessoaJuridica()));
		return associacaoIncraDAO.buscarPorCriteria(criteria);
	}
}
