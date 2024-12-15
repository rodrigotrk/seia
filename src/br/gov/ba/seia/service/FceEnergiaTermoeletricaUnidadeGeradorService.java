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
import br.gov.ba.seia.entity.FceEnergiaTermoeletricaUnidadeGerador;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaTermoeletricaUnidadeGeradorService {
	
	@Inject
	private IDAO<FceEnergiaTermoeletricaUnidadeGerador> fceEnergiaTermoeletricaUnidadeGeradorIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergiaTermoeletricaUnidadeGerador fceEnergiaTermoeletricaUnidadeGerador) {
		this.fceEnergiaTermoeletricaUnidadeGeradorIDAO.salvarOuAtualizar(fceEnergiaTermoeletricaUnidadeGerador);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceEnergiaTermoeletricaUnidadeGerador> listarGeradorByUnidade(FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergiaTermoeletricaUnidadeGerador.class)		
		.add(Restrictions.eq("fceEnergiaTermoeletricaUnidade.ideFceEnergiaTermoeletricaUnidade", fceEnergiaTermoeletricaUnidade.getIdeFceEnergiaTermoeletricaUnidade())); 
		
		return this.fceEnergiaTermoeletricaUnidadeGeradorIDAO.listarPorCriteria(detachedCriteria, Order.asc("ideFceEnergiaTermoeletricaUnidadeGerador"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(FceEnergiaTermoeletricaUnidadeGerador fceEnergiaTermoeletricaUnidadeGerador)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideFceEnergiaTermoeletricaUnidadeGerador", fceEnergiaTermoeletricaUnidadeGerador.getIdeFceEnergiaTermoeletricaUnidadeGerador());
		this.fceEnergiaTermoeletricaUnidadeGeradorIDAO.executarNamedQuery("FceEnergiaTermoeletricaUnidadeGerador.removeByPk", parametros);
	}
}
