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
import br.gov.ba.seia.entity.EspecieHibrido;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EspecieHibridoService {
	
	@Inject
	private IDAO<EspecieHibrido> especieHibridoIDAO;
	
	/**
	 * @return List
	 * @
	 * @INFO Retorna uma lista com todas os objetos do tipo EspecieHibrido
	 */
	public List<EspecieHibrido> buscarTodosEspecieHibrido() {
		DetachedCriteria criteria = DetachedCriteria.forClass(EspecieHibrido.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return especieHibridoIDAO.listarPorCriteria(criteria,Order.asc("dscEspecieHibrido"));
	}

}
