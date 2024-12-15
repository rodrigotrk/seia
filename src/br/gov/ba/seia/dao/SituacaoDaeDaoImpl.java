package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.SituacaoDae;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SituacaoDaeDaoImpl {

	@Inject
	IDAO<SituacaoDae> cerhSituacaoDaeImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SituacaoDae> listarSituacaoDae() {
		return cerhSituacaoDaeImpl.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SituacaoDae listarSituacaoByIde(Integer ideSituacao) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SituacaoDae.class);
		
		criteria.add(Restrictions.eq("ideSitucaoDae", ideSituacao));
		
		return cerhSituacaoDaeImpl.buscarPorCriteria(criteria);
	}

}
