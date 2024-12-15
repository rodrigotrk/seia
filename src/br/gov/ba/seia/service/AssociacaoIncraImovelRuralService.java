package br.gov.ba.seia.service;

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
import br.gov.ba.seia.entity.ImovelRural;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AssociacaoIncraImovelRuralService {
	
	@Inject
	private IDAO<AssociacaoIncraImovelRural> associacaoIncraImovelRuralDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(AssociacaoIncraImovelRural associacaoIncraImovelRural) {
		associacaoIncraImovelRuralDAO.salvarOuAtualizar(associacaoIncraImovelRural);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AssociacaoIncraImovelRural> listarAssociacaoIncraImovelRuralPorImovelRural(ImovelRural ideImovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AssociacaoIncraImovelRural.class);
		criteria.add(Restrictions.eq("ideImovelRural.ideImovelRural", ideImovelRural.getIdeImovelRural()));
		return associacaoIncraImovelRuralDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AssociacaoIncraImovelRural buscarAssociacaoIncraImovelRural(AssociacaoIncra ideAssociacaoIncra, ImovelRural ideImovelRural){
		DetachedCriteria criteria = DetachedCriteria.forClass(AssociacaoIncraImovelRural.class);
		criteria.add(Restrictions.eq("ideAssociacaoIncra.ideAssociacaoIncra", ideAssociacaoIncra.getIdeAssociacaoIncra()));
		criteria.add(Restrictions.eq("ideImovelRural.ideImovelRural", ideImovelRural.getIdeImovelRural()));
		return associacaoIncraImovelRuralDAO.buscarPorCriteria(criteria); 
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(AssociacaoIncraImovelRural associacaoIncraImovelRural)  {
		associacaoIncraImovelRuralDAO.remover(associacaoIncraImovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AssociacaoIncraImovelRural> buscarAssociacaoIncraImovelRural(AssociacaoIncra ideAssociacaoIncra)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(AssociacaoIncraImovelRural.class);
		criteria.add(Restrictions.eq("ideAssociacaoIncra.ideAssociacaoIncra", ideAssociacaoIncra.getIdeAssociacaoIncra()));
		return associacaoIncraImovelRuralDAO.listarPorCriteria(criteria); 
	}
}
