package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceFlorestal;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceFlorestalDAOImpl {

	@Inject
	private IDAO<FceFlorestal> idaoFceFlorestal;
	
	public FceFlorestal buscarFceFlorestal(Fce fce) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceFlorestal.class, "fF");
		
		criteria.createAlias("fF.ideFce", "f", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("fF.ideFce.ideFce", fce.getIdeFce()));
		
		return idaoFceFlorestal.buscarPorCriteria(criteria);
	}
	
	
	public void salvar(FceFlorestal fceFlorestal){
		idaoFceFlorestal.salvarOuAtualizar(fceFlorestal);
	}
	
	
}
