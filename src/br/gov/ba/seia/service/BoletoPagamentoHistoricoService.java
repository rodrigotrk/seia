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
import br.gov.ba.seia.entity.BoletoPagamentoHistorico;
import br.gov.ba.seia.util.Util;

/**
 * Classe de negocio do boleto pagamento historico
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 10/12/2013
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoPagamentoHistoricoService {

	@Inject
	IDAO<BoletoPagamentoHistorico> daoBPH;

	/**
	 * Metodo que salva um boleto pagamento historico
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param bph - {@link BoletoPagamentoHistorico} a ser salvo
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(BoletoPagamentoHistorico bph)  {
		if(!Util.isNullOuVazio(bph)) {
			daoBPH.salvar(bph);
		}
	}
	
	/**
	 * Metodo que busca uma lista de boleto pagamento historico filtrando por requerimento.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param idRequerimento - ID do requerimento a ser procurado
	 * @
	 */
	public List<BoletoPagamentoHistorico> findByBoletoPagamentoRequerimento(Integer idBoleto)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoPagamentoHistorico.class, "bpH");
		detachedCriteria.createAlias("ideMotivoCancelamentoBoleto", "motivo", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("bpH.ideBoletoPagamento", "bpR", JoinType.INNER_JOIN);
		detachedCriteria.createAlias("bpR.ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("bpR.ideProcesso", "proc", JoinType.LEFT_OUTER_JOIN);
		
		detachedCriteria.add(Restrictions.eq("bpR.ideBoletoPagamentoRequerimento", idBoleto));
		
		return daoBPH.listarPorCriteria(detachedCriteria, Order.asc("ideBoletoPagamentoHistorico"));
	}
	
	public BoletoPagamentoHistorico obterUltimoHistoricoPorRequerimento(Integer ideRequerimento)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoPagamentoHistorico.class, "bpH");
		detachedCriteria.createAlias("bpH.ideBoletoPagamento", "boleto", JoinType.INNER_JOIN);
		detachedCriteria.createAlias("boleto.ideRequerimento", "requerimento", JoinType.LEFT_OUTER_JOIN);
		
		detachedCriteria.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));
		detachedCriteria.addOrder(Order.desc("bpH.ideBoletoPagamentoHistorico"));
		
		return daoBPH.buscarPorCriteriaMaxResult(detachedCriteria);
	}
}