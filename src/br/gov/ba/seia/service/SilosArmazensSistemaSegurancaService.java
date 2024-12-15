package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.SilosArmazen;
import br.gov.ba.seia.entity.SilosArmazensSistemaSeguranca;
import br.gov.ba.seia.entity.SistemaSegurancaSilosArmazen;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilosArmazensSistemaSegurancaService {

	@Inject
	private IDAO<SilosArmazensSistemaSeguranca> idao;

	@Inject
	private IDAO<SistemaSegurancaSilosArmazen> idaoSistema;
	
	public List<SilosArmazensSistemaSeguranca> listarSilosArmazensSistemaSeguranca() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(SilosArmazensSistemaSeguranca.class);
		
		criteria.add(Restrictions.eq("indAtivo", true));
		
		return idao.listarPorCriteria(criteria, Order.asc("ideSilosArmazensSistemaSeguranca"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvaSistemaSegurancaSilosArmazen(SistemaSegurancaSilosArmazen sistemaSegurancaSilosArmazen) throws Exception{
		idaoSistema.salvarOuAtualizar(sistemaSegurancaSilosArmazen);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSistemaSeguraSilosArmazens(SilosArmazen ideSilosArmazens) throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideSilosArmazen", ideSilosArmazens);
		idaoSistema.executarNamedQuery("SistemaSegurancaSilosArmazen.removerByIdeSilos", params);
	}

}
