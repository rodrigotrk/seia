package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceIntervencaoCaracteristicaExtracao;
import br.gov.ba.seia.entity.FceIntervencaoMineracao;
import br.gov.ba.seia.entity.LancamentoEfluenteLocalizacaoGeografica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIntervencaoMineracaoService {
	
	@Inject
	private IDAO<FceIntervencaoMineracao> idao;

	@Inject
	private IDAO<LancamentoEfluenteLocalizacaoGeografica> idaoLancamentoEfluenteLocalizacaoGeografica;

	@Inject
	private IDAO<FceIntervencaoCaracteristicaExtracao> idaoFceIntervencaoCaracteristicaExtracao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceIntervencaoMineracao fceIntervencaoMineracao) {
		idao.salvar(fceIntervencaoMineracao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(FceIntervencaoMineracao fceIntervencaoMineracao) {
		idao.atualizar(fceIntervencaoMineracao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceIntervencaoMineracao buscarPorFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIntervencaoMineracao.class).add(Restrictions.eq("fce", fce));
		
		FceIntervencaoMineracao fceIntervencaoMineracao = idao.buscarPorCriteria(criteria);
		
		if(fceIntervencaoMineracao != null){
			listarLancamentoEfluenteLocalizacaoGeografica(fceIntervencaoMineracao);
			listarFceIntervencaoCaracteristicaExtracao(fceIntervencaoMineracao);
		}
		
		return fceIntervencaoMineracao;
	}
	
	private void listarLancamentoEfluenteLocalizacaoGeografica(FceIntervencaoMineracao fceIntervencaoMineracao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LancamentoEfluenteLocalizacaoGeografica.class)
				.add(Restrictions.eq("ideFceIntervencaoMineracao", fceIntervencaoMineracao));
		
		fceIntervencaoMineracao.setLancamentoEfluenteLocalizacoesGeografica(idaoLancamentoEfluenteLocalizacaoGeografica.listarPorCriteria(criteria));
	}

	private void listarFceIntervencaoCaracteristicaExtracao(FceIntervencaoMineracao fceIntervencaoMineracao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIntervencaoCaracteristicaExtracao.class)
				.createAlias("fceIntervencaoTipoCaracticasExtracao", "fceIntervencaoTipoCaracticasExtracao", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideFceIntervencaoMineracao", fceIntervencaoMineracao))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		fceIntervencaoMineracao.setFceIntervencaoCaracteristicaExtracoes(idaoFceIntervencaoCaracteristicaExtracao.listarPorCriteria(criteria));
	}
}