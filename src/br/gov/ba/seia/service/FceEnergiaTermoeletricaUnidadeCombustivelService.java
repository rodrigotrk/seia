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
	import br.gov.ba.seia.entity.FceEnergiaTermoeletricaUnidade;
import br.gov.ba.seia.entity.FceEnergiaTermoeletricaUnidadeCombustivel;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaTermoeletricaUnidadeCombustivelService {
	
	@Inject
	private IDAO<FceEnergiaTermoeletricaUnidadeCombustivel> fceEnergiaTermoeletricaUnidadeCombustivelIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergiaTermoeletricaUnidadeCombustivel fceEnergiaTermoeletricaUnidadeCombustivel) {
		this.fceEnergiaTermoeletricaUnidadeCombustivelIDAO.salvarOuAtualizar(fceEnergiaTermoeletricaUnidadeCombustivel);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceEnergiaTermoeletricaUnidadeCombustivel> listarCombustivelByIdEnergiaUnidade(FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergiaTermoeletricaUnidadeCombustivel.class)
		.add(Restrictions.eq("fceEnergiaTermoeletricaUnidade.ideFceEnergiaTermoeletricaUnidade", fceEnergiaTermoeletricaUnidade.getIdeFceEnergiaTermoeletricaUnidade())); 
				
		return this.fceEnergiaTermoeletricaUnidadeCombustivelIDAO.listarPorCriteria(detachedCriteria, Order.asc("ideFceEnergiaTermoeletricaUnidadeCombustivel"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(FceEnergiaTermoeletricaUnidadeCombustivel fceEnergiaTermoeletricaUnidadeCombustivel)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideFceEnergiaTermoeletricaUnidadeCombustivel", fceEnergiaTermoeletricaUnidadeCombustivel.getIdeFceEnergiaTermoeletricaUnidadeCombustivel());
		this.fceEnergiaTermoeletricaUnidadeCombustivelIDAO.executarNamedQuery("FceEnergiaTermoeletricaUnidadeCombustivel.removeByPk", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerByUnidade(FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fceEnergiaTermoeletricaUnidade", fceEnergiaTermoeletricaUnidade);
		this.fceEnergiaTermoeletricaUnidadeCombustivelIDAO.executarNamedQuery("FceEnergiaTermoeletricaUnidadeCombustivel.removeByUnidade", parametros);
	}
	
}
