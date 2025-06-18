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
import br.gov.ba.seia.entity.MedidaControleEmissao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MedidaControleEmissaoService {

	@Inject
	private IDAO<MedidaControleEmissao> idao;
	
	public List<MedidaControleEmissao> listarMedidaControleEmissao() {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MedidaControleEmissao.class);
		
		criteria.add(Restrictions.eq("indAtivo", true));
		
		return idao.listarPorCriteria(criteria, Order.asc("ideMedidaControleEmissao"));
	}
}
