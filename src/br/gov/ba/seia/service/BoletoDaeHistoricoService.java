package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.BoletoDaeHistorico;
import br.gov.ba.seia.util.Util;

/**
 * Classe de negocio do boletoDaeHistorico
 * 
 * @author micael.coutinho
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BoletoDaeHistoricoService {

	@Inject
	IDAO<BoletoDaeHistorico> daoBoletoDaeHistorico;

	/**
	 * Metodo que salva um boletoDaeHistorico
	 *
	 * @author micael.coutinho
	 * @param bdh - {@link BoletoDaeHistorico} a ser salvo
	 * @
	 */
	public void salvar(BoletoDaeHistorico boletoDaeHistorico)  {
		if(!Util.isNullOuVazio(boletoDaeHistorico)) {
			daoBoletoDaeHistorico.salvar(boletoDaeHistorico);
		}
	}
	
	public void salvarOuAtualizar(BoletoDaeHistorico boletoDaeHistorico)  {
		if(!Util.isNullOuVazio(boletoDaeHistorico)) {
			daoBoletoDaeHistorico.salvarOuAtualizar(boletoDaeHistorico);
		}
	}
	
	/**
	 * Metodo que busca uma lista de boletoDaeHistorico filtrando por requerimento.
	 *
	 * @author micael.coutinho
	 * @param idRequerimento - ID do requerimento a ser procurado
	 * @
	 */
	public List<BoletoDaeHistorico> findByRequerimento(Integer idRequerimento)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoDaeHistorico.class, "boletoDaeHist");
		detachedCriteria.createAlias("ideMotivoCancelamentoBoleto", "motivo", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideBoletoDae", "boletoDaeReq", JoinType.INNER_JOIN);
		detachedCriteria.createAlias("boletoDaeReq.ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("boletoDaeReq.ideProcesso", "proc", JoinType.LEFT_OUTER_JOIN);
		
		detachedCriteria.add(Restrictions.or(Restrictions.eq("req.ideRequerimento", idRequerimento), Restrictions.eq("proc.ideRequerimento.ideRequerimento", idRequerimento)));
		
		return daoBoletoDaeHistorico.listarPorCriteria(detachedCriteria, Order.asc("ideBoletoDaeHistorico"));
	}
	
	/**
	 * Metodo que remove um boletoDaeHistorico
	 *
	 * @param bdh - {@link BoletoDaeHistorico} a ser salvo
	 * @
	 */
	public void remover(BoletoDaeHistorico boletoDaeHistorico)  {
		if(!Util.isNullOuVazio(boletoDaeHistorico)) {
			daoBoletoDaeHistorico.remover(boletoDaeHistorico);
		}
	}
	
	/**
	 * Metodo que busca uma lista de boleto pagamento historico filtrando por requerimento.
	 */
	public List<BoletoDaeHistorico> findByBoletoDaeRequerimento(Integer idBoleto)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BoletoDaeHistorico.class, "bpH");
		detachedCriteria.createAlias("ideMotivoCancelamentoBoleto", "motivo", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideBoletoDaeRequerimento", "bpR", JoinType.INNER_JOIN);
		detachedCriteria.createAlias("ideBoletoDaeRequerimento.ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN);
		detachedCriteria.createAlias("ideBoletoDaeRequerimento.ideProcesso", "proc", JoinType.LEFT_OUTER_JOIN);
		
		detachedCriteria.add(Restrictions.eq("bpR.ideBoletoDaeRequerimento", idBoleto));
		
		return daoBoletoDaeHistorico.listarPorCriteria(detachedCriteria, Order.asc("ideBoletoDaeHistorico"));
	}
}