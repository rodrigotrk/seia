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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceEnergiaTermoEletrica;
import br.gov.ba.seia.entity.FceEnergiaTermoeletricaUnidade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaTermoeletricaUnidadeService {
	
	@Inject
	private IDAO<FceEnergiaTermoeletricaUnidade> fceEnergiaTermoeletricaUnidadeIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade) {
		this.fceEnergiaTermoeletricaUnidadeIDAO.salvarOuAtualizar(fceEnergiaTermoeletricaUnidade);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceEnergiaTermoeletricaUnidade> listarUnidadeByTermoeletrica(FceEnergiaTermoEletrica fceEnergiaTermoeletrica) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergiaTermoeletricaUnidade.class)
		.createAlias("localizacaoGeografica", "ideLocalizacaoGeografica",JoinType.LEFT_OUTER_JOIN)
		.add(Restrictions.eq("fceEnergiaTermoeletrica.ideFceEnergiaTermoEletrica", fceEnergiaTermoeletrica.getIdeFceEnergiaTermoEletrica())); 
		
		return this.fceEnergiaTermoeletricaUnidadeIDAO.listarPorCriteria(detachedCriteria, Order.asc("ideFceEnergiaTermoeletricaUnidade"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideFceEnergiaTermoeletricaUnidade", fceEnergiaTermoeletricaUnidade.getIdeFceEnergiaTermoeletricaUnidade());
		this.fceEnergiaTermoeletricaUnidadeIDAO.executarNamedQuery("FceEnergiaTermoeletricaUnidade.removeByPk", parametros);
	}
}
