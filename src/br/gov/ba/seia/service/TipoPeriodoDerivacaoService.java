package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceAbastecimentoHumano;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoPeriodoDerivacaoService {

	@Inject
	private IDAO<TipoPeriodoDerivacao> tipoPeriodoDerivacaoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoPeriodoDerivacao> listarTipoPeriodoDerivacao() throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoPeriodoDerivacao.class)
			.add(Restrictions.eq("indAtivo", true));
		
		return tipoPeriodoDerivacaoDAO.listarPorCriteria(criteria, Order.asc("ideTipoPeriodoDerivacao"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoPeriodoDerivacao buscarTipoPeriodoDerivacaoPorIdeFceAbsHumano (FceAbastecimentoHumano fceAbastecimentoHumano) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoPeriodoDerivacao.class)
			.add(Restrictions.eq("fceIdeAbastecimentoHumano", fceAbastecimentoHumano))
			.setFetchMode("fceIdeAbastecimentoHumano", FetchMode.JOIN)
			.setFetchMode("ideTipoPeriodoDerivacao", FetchMode.JOIN);
			
		return tipoPeriodoDerivacaoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoPeriodoDerivacao> listarTipoPeriodoDerivacaoPorListaTipoPeriodoDerivacaoEnum(List<TipoPeriodoDerivacaoEnum> listTipoPeriodoDerivacaoEnum) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoPeriodoDerivacao.class);
		
		Disjunction disjunction = Restrictions.disjunction();
		
		for(TipoPeriodoDerivacaoEnum tipo : listTipoPeriodoDerivacaoEnum){
			disjunction.add(Restrictions.eq("ideTipoPeriodoDerivacao", tipo.getId()));
		}
		
		criteria.add(disjunction);
		
		return tipoPeriodoDerivacaoDAO.listarPorCriteria(criteria);
	}
}
