package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.Requerimento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComprovantePagamentoService {

	@Inject
	private IDAO<ComprovantePagamento> comprovanteDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ComprovantePagamento pComprovantePagamento) {
		comprovanteDAO.salvar(pComprovantePagamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ComprovantePagamento obterPorRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamento.class,"c1")
				.createAlias("c1.ideBoletoPagamentoRequerimento", "bpr")
				.createAlias("bpr.ideRequerimento", "req")
				.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()));
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(ComprovantePagamento.class,"c2")
			.createAlias("c2.ideBoletoPagamentoRequerimento", "bpr")
			.createAlias("bpr.ideRequerimento", "req")
			.setProjection(Projections.projectionList().add(Projections.max("c2.ideComprovantePagamento")))
			.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()));
		
		criteria.add(Subqueries.propertyEq("c1.ideComprovantePagamento", subCriteria));
		
		return comprovanteDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ComprovantePagamento obterPorIdRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamento.class)
				.createAlias("ideBoletoPagamentoRequerimento", "bpr")
				.createAlias("bpr.ideRequerimento", "req")
				.createAlias("idePessoaValidacao", "idePessoaValidacao",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaValidacao.pessoaFisica", "pessoaFisica")
				.add(Restrictions.eq("req.ideRequerimento",ideRequerimento));
				
				
		return comprovanteDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizar(ComprovantePagamento pComprovantePagamento) {
		comprovanteDAO.salvarOuAtualizar(pComprovantePagamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ComprovantePagamento> listarByIdeRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamento.class)
				.createAlias("ideBoletoPagamentoRequerimento", "bpr")
				.createAlias("bpr.ideRequerimento", "req")
				.createAlias("idePessoaValidacao", "idePessoaValidacao",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaValidacao.pessoaFisica", "pessoaFisica",JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("req.ideRequerimento",ideRequerimento));
				
		return comprovanteDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ComprovantePagamento> listarByIdeRequerimentoAndTipoBoletoPagamento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamento.class)
				.createAlias("ideBoletoPagamentoRequerimento", "bpr")
				.createAlias("bpr.ideRequerimento", "req")
				.createAlias("bpr.ideTipoBoletoPagamento", "tbp")
				.add(Restrictions.eq("req.ideRequerimento",ideRequerimento))
				.add(Restrictions.eq("tbp.ideTipoBoletoPagamento",1));
		return comprovanteDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ComprovantePagamento carregarByIdeRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamento.class)
				.createAlias("ideBoletoPagamentoRequerimento", "bpr")
				.createAlias("bpr.ideRequerimento", "req")
				.createAlias("idePessoaValidacao", "idePessoaValidacao",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaValidacao.pessoaFisica", "pessoaFisica",JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("req.ideRequerimento",ideRequerimento));
				
		return comprovanteDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Collection<ComprovantePagamento> comprovantesPagamento) {
		for (ComprovantePagamento comprovantePagamento : comprovantesPagamento) {
			this.salvarAtualizar(comprovantePagamento);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ComprovantePagamento consultarPorIdBoletoPagamentoRequerimento(Integer idBoletoPagamentoRequerimento) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideBoletoPagamentoRequerimento", idBoletoPagamentoRequerimento);
		return comprovanteDAO.buscarEntidadePorNamedQuery("ComprovantePagamento.findByIdBoletoPagamentoRequerimento", parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ComprovantePagamento> listarByIdeRequerimentoHistoricoNaoCancelado(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ComprovantePagamento.class)
				.createAlias("ideBoletoPagamentoRequerimento", "bpr")
				.createAlias("bpr.ideRequerimento", "req")
				.createAlias("idePessoaValidacao", "idePessoaValidacao",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaValidacao.pessoaFisica", "pessoaFisica",JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("req.ideRequerimento",ideRequerimento))
				.add(Restrictions.sqlRestriction(
						"bpr1_.ide_boleto_pagamento_requerimento not in("
							+ " SELECT ide_boleto_pagamento_requerimento FROM boleto_pagamento_requerimento b "
							+ " LEFT JOIN boleto_pagamento_historico h on b.ide_boleto_pagamento_requerimento = h.ide_boleto_pagamento"
							+ " WHERE ide_tipo_boleto_pagamento = 1 AND h.ide_status_boleto_pagamento = 5) "
						+ " AND bpr1_.ide_requerimento = "+ideRequerimento)) ;
				
		return comprovanteDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ComprovantePagamento comprovantePagamento) {
		comprovanteDAO.remover(comprovantePagamento);
	}
}