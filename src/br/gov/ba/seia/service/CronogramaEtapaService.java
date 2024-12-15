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

import br.gov.ba.seia.dao.CronogramaEtapaDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CronogramaEtapa;
import br.gov.ba.seia.entity.CronogramaRecuperacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CronogramaEtapaService {
	

	@Inject
	CronogramaEtapaDAOImpl daoImpl;

	@Inject
	IDAO<CronogramaEtapa> dao;
	
	public List<CronogramaEtapa> listarCronogramaEtapaByCronogramaRecuperacao(CronogramaRecuperacao pCronogramaRecuperacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CronogramaEtapa.class, "cronogramaEtapa");
		criteria.createAlias("ideTipoRecuperacao", "tipoRecuperacao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideCronogramaRecuperacao", pCronogramaRecuperacao));
		criteria.addOrder(Order.asc("ideCronogramaEtapa"));
		return dao.listarPorCriteria(criteria);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CronogramaEtapa pCronogramaEtapa)  {
		this.daoImpl.salvar(pCronogramaEtapa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(CronogramaEtapa pCronogramaEtapa)  {
		this.daoImpl.atualizar(pCronogramaEtapa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAllByIdeCronogramaRecuperacao(Integer pIdeCronogramaRecuperacao)  {
		this.daoImpl.excluirAllByIdeCronogramaRecuperacao(pIdeCronogramaRecuperacao);
	}
	
}
