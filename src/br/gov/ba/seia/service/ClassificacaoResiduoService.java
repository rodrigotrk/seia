package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ClassificacaoResiduo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClassificacaoResiduoService {

	@Inject
	private IDAO<ClassificacaoResiduo> idao;
	
	public List<ClassificacaoResiduo> listarClassificacaoResiduo() {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ClassificacaoResiduo.class);
		
		criteria.add(Restrictions.eq("indAtivo", true));
		
		return idao.listarPorCriteria(criteria, Order.asc("ideClassificacaoResiduo"));
	}
}
