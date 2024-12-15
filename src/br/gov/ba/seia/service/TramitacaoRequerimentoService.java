package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.interfaces.TramitacaoInterface;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
public class TramitacaoRequerimentoService {

	@Inject
	private IDAO<TramitacaoRequerimento> tramitacaoRequerimentoDAO;
	
	@Inject
	private IDAO<TramitacaoInterface> TramitacaoInterfaceDAO;

	@EJB
	private PessoaService pessoaService;
	
	@EJB
	private AlertaService alertaService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TramitacaoRequerimento pTramitacaoRequerimento) {
		tramitacaoRequerimentoDAO.salvarOuAtualizar(pTramitacaoRequerimento);
		alertaService.criarAlerta(pTramitacaoRequerimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void criarTramitacaoRequerimento(Requerimento pRequerimento, StatusRequerimento pStatusRequerimento, Pessoa pPessoa) {
		TramitacaoRequerimento tramitacao = new TramitacaoRequerimento();
		tramitacao.setDtcMovimentacao(new Date());
		tramitacao.setIdePessoa(pPessoa);
		tramitacao.setIdeRequerimento(pRequerimento);
		tramitacao.setIdeStatusRequerimento(pStatusRequerimento);
		tramitacaoRequerimentoDAO.salvar(tramitacao);
		alertaService.criarAlerta(tramitacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void criarTramitacaoRequerimentoSemFlush(Requerimento pRequerimento, StatusRequerimento pStatusRequerimento, Pessoa pPessoa) {
		TramitacaoRequerimento tramitacao = new TramitacaoRequerimento();
		tramitacao.setDtcMovimentacao(new Date());
		tramitacao.setIdePessoa(pPessoa);
		tramitacao.setIdeRequerimento(pRequerimento);
		tramitacao.setIdeStatusRequerimento(pStatusRequerimento);
		tramitacaoRequerimentoDAO.salvarSemFlush(tramitacao);
		alertaService.criarAlerta(tramitacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TramitacaoRequerimento buscarPorIdCriteria(Integer pIdeStatus) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TramitacaoRequerimento.class)
				.add(Restrictions.eq("ideTramitacaoRequerimento", pIdeStatus))
				.setFetchMode("ideStatusRequerimento", FetchMode.JOIN);
		return tramitacaoRequerimentoDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TramitacaoRequerimento buscarUltimaTramitacaoPorRequerimento(Integer pRequerimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TramitacaoRequerimento.class)
			
			.createAlias("ideStatusRequerimento","st", JoinType.INNER_JOIN)
			.createAlias("idePessoa","p", JoinType.INNER_JOIN)
				
			.add(Subqueries.propertyEq("ideTramitacaoRequerimento", 
					DetachedCriteria.forClass(TramitacaoRequerimento.class)
						.createAlias("ideRequerimento","req", JoinType.INNER_JOIN)
						.add(Restrictions.eq("req.ideRequerimento", pRequerimento))
						.setProjection(Projections.max("ideTramitacaoRequerimento"))
				)
			)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideTramitacaoRequerimento"),"ideTramitacaoRequerimento")
				.add(Projections.property("st.ideStatusRequerimento"),"ideStatusRequerimento.ideStatusRequerimento")
				.add(Projections.property("dtcMovimentacao"),"dtcMovimentacao")
				.add(Projections.property("p.idePessoa"),"idePessoa.idePessoa")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TramitacaoRequerimento.class))
		;
		
		TramitacaoRequerimento tramit = tramitacaoRequerimentoDAO.buscarPorCriteria(criteria);
		
		return tramit;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTamitacaoRequerimentoAtual(Integer ideRequerimento, TramitacaoRequerimento tramitacaoRequerimento) {
		TramitacaoRequerimento tramitacaoRequerimentoAtual = buscarUltimaTramitacaoPorRequerimento(ideRequerimento);
		return tramitacaoRequerimento.equals(tramitacaoRequerimentoAtual);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TramitacaoInterface> listarTramitacaoRequerimentoPorRequerimento(Requerimento requerimento, Integer first, Integer pageSize) throws Exception {
		DetachedCriteria criteria = criteriaListarTramitacaoRequerimento(requerimento);
		criteria.addOrder(Order.asc("ideTramitacaoRequerimento"));
		List<TramitacaoInterface> listTramitacaoRequerimento = TramitacaoInterfaceDAO.listarPorCriteriaDemanda(criteria, first, pageSize);
		return listTramitacaoRequerimento;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer listarTramitacaoRequerimentoPorRequerimentoCount(Requerimento requerimento) throws Exception {
		DetachedCriteria criteria = criteriaListarTramitacaoRequerimento(requerimento);
		return tramitacaoRequerimentoDAO.count(criteria);
	}

	private DetachedCriteria criteriaListarTramitacaoRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TramitacaoRequerimento.class)
				.createAlias("ideRequerimento", "requerimento")
				.createAlias("ideStatusRequerimento", "statusRequerimento")
				.createAlias("idePessoa", "pessoa")
				.createAlias("pessoa.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoa.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		return criteria;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarSemFlush(Requerimento requerimento, StatusRequerimentoEnum statusEnum, Pessoa operador) {
		StatusRequerimento statusRequerimento = new StatusRequerimento(statusEnum.getStatus());
		this.criarTramitacaoRequerimentoSemFlush(requerimento, statusRequerimento, operador);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitar(Requerimento requerimento, StatusRequerimentoEnum statusEnum, Pessoa operador) {
		StatusRequerimento statusRequerimento = new StatusRequerimento(statusEnum.getStatus());
		this.criarTramitacaoRequerimento(requerimento, statusRequerimento, operador);
	}

	public void tramitarAutomaticamente(Requerimento requerimento, StatusRequerimentoEnum statusEnum) throws Exception {
		Pessoa operador = this.pessoaService.carregarUsuarioSEIA();
		this.tramitar(requerimento, statusEnum, operador);
	}
	
	public void tramitarAutomaticamente(Requerimento requerimento, StatusRequerimentoEnum statusEnum, Pessoa operador) throws Exception {
		this.tramitar(requerimento, statusEnum, operador);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerTramitacao(TramitacaoRequerimento tramitacaoRequerimento) throws Exception{
		String deleteSQL = "delete from tramitacao_requerimento where ide_tramitacao_requerimento = :ideTramitacaoReq";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideTramitacaoReq",tramitacaoRequerimento.getIdeTramitacaoRequerimento());
		tramitacaoRequerimentoDAO.executarNativeQuery(deleteSQL, params);
		//tramitacaoRequerimentoDAO.remover(tramitacaoRequerimento);
	}
}