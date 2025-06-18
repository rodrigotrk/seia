package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceCanal;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCanalService {

	@Inject
	private IDAO<FceCanal> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceCanal(FceCanal fceCanal)  {
		idao.salvar(fceCanal);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarFceCanal(FceCanal fceCanal)  {
		idao.atualizar(fceCanal);
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public FceCanal buscarPorFce(Fce fce)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCanal.class)
				.createAlias("tipoRio","tipoRio", JoinType.LEFT_OUTER_JOIN)
				.createAlias("caracteristicasCanal", "caracteristicasCanal", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("fce", fce));
		return idao.buscarPorCriteria(criteria);
	}
}
