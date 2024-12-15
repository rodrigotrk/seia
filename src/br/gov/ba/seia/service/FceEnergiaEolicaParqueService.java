package br.gov.ba.seia.service;
import java.util.List;

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
import br.gov.ba.seia.entity.FceEnergiaEolica;
import br.gov.ba.seia.entity.FceEnergiaEolicaParque;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaEolicaParqueService {
	
	@Inject
	private IDAO<FceEnergiaEolicaParque> fceEnergiaEolicaParqueIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergiaEolicaParque fceEnergiaEolicaParque) {
		this.fceEnergiaEolicaParqueIDAO.salvarOuAtualizar(fceEnergiaEolicaParque);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(FceEnergiaEolicaParque parque)  {
		this.fceEnergiaEolicaParqueIDAO.remover(parque);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceEnergiaEolicaParque> listarEolicaParqueByFceEnergiaEolica(FceEnergiaEolica fceEnergiaEolica) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergiaEolicaParque.class)
		.createAlias("localizacaoGeografica", "ideLocalizacaoGeografica",JoinType.LEFT_OUTER_JOIN)
		.add(Restrictions.eq("fceEnergiaEolica.ideFceEnergiaEolica", fceEnergiaEolica.getIdeFceEnergiaEolica())); 
		
		return this.fceEnergiaEolicaParqueIDAO.listarPorCriteria(detachedCriteria, Order.asc("ideFceEnergiaEolicaParque"));
	}
	
}
