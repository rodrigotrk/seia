package br.gov.ba.seia.facade;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.TramitacaoReenquadramentoProcesso;
import br.gov.ba.seia.interfaces.TramitacaoInterface;
import br.gov.ba.seia.service.AlertaService;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TramitacaoReenquadramentoProcessoServiceFacade {
	
	@Inject
	private IDAO<TramitacaoReenquadramentoProcesso> tramitacaoReenquadramentoProcessoDAO;
	
	@Inject
	private IDAO<TramitacaoInterface> tramitacaoInterfaceoDAO;
	
	@EJB
	private AlertaService alertaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTamitacaoRequerimentoAtual(Integer ideProcessoReenquadramento, TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcesso)  {
		TramitacaoReenquadramentoProcesso tramitacaoRequerimentoAtual = buscarUltimaTramitacaoPorReenquadramentoProcesso(ideProcessoReenquadramento);
		return tramitacaoReenquadramentoProcesso.equals(tramitacaoRequerimentoAtual);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TramitacaoReenquadramentoProcesso buscarUltimaTramitacaoPorReenquadramentoProcesso(Integer ideProcessoReenquadramento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TramitacaoReenquadramentoProcesso.class)
			
			.createAlias("ideStatusReenquadramento","st", JoinType.INNER_JOIN)
			.createAlias("idePessoa","p", JoinType.INNER_JOIN)
				
			.add(Subqueries.propertyEq("ideTramitacaoReenquadramentoProcesso", 
					DetachedCriteria.forClass(TramitacaoReenquadramentoProcesso.class)
						.createAlias("ideProcessoReenquadramento","req", JoinType.INNER_JOIN)
						.add(Restrictions.eq("req.ideProcessoReenquadramento", ideProcessoReenquadramento))
						.setProjection(Projections.max("ideTramitacaoReenquadramentoProcesso"))
				)
			)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideTramitacaoReenquadramentoProcesso"),"ideTramitacaoReenquadramentoProcesso")
				.add(Projections.property("st.ideStatusReenquadramento"),"ideStatusReenquadramento.ideStatusReenquadramento")
				.add(Projections.property("p.idePessoa"),"idePessoa.idePessoa")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TramitacaoReenquadramentoProcesso.class))
		;
		
		return tramitacaoReenquadramentoProcessoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void criarTramitacaoReenquadramentoProcessoSemFlush(ProcessoReenquadramento processoReenquadramento, StatusReenquadramento statusReenquadramento, Pessoa pPessoa) {
		TramitacaoReenquadramentoProcesso tramitacao = new TramitacaoReenquadramentoProcesso();
		tramitacao.setDtcMovimentacao(new Date());
		tramitacao.setIdePessoa(pPessoa);
		tramitacao.setIdeProcessoReenquadramento(processoReenquadramento);
		tramitacao.setIdeStatusReenquadramento(statusReenquadramento);
		tramitacaoReenquadramentoProcessoDAO.salvarSemFlush(tramitacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void criarTramitacaoReenquadramentoProcesso(ProcessoReenquadramento processoReenquadramento, StatusReenquadramento statusReenquadramento, Pessoa pPessoa) {
		TramitacaoReenquadramentoProcesso tramitacao = new TramitacaoReenquadramentoProcesso();
		tramitacao.setDtcMovimentacao(new Date());
		tramitacao.setIdePessoa(pPessoa);
		tramitacao.setIdeProcessoReenquadramento(processoReenquadramento);
		tramitacao.setIdeStatusReenquadramento(statusReenquadramento);
		tramitacaoReenquadramentoProcessoDAO.salvar(tramitacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TramitacaoInterface> listarTramitacaoReenquadramentoProcessoPorProcessoReenquadramento(
			Integer ideProcessoReenquadramento, int first, int pageSize) {
		DetachedCriteria criteria = criteriaListarReenquadramentoProcesso(ideProcessoReenquadramento);
		criteria.addOrder(Order.asc("ideTramitacaoReenquadramentoProcesso"));
		return tramitacaoInterfaceoDAO.listarPorCriteriaDemanda(criteria, first, pageSize);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer listarTramitacaoReenquadramentoProcessoPorProcessoReenquadramentoCount(Integer ideProcessoReenquadramento) {
		DetachedCriteria criteria = criteriaListarReenquadramentoProcesso(ideProcessoReenquadramento);
		return tramitacaoInterfaceoDAO.count(criteria);
	}
	
	private DetachedCriteria criteriaListarReenquadramentoProcesso(Integer ideProcessoReenquadramento) {
		return DetachedCriteria.forClass(TramitacaoReenquadramentoProcesso.class)
				.createAlias("ideProcessoReenquadramento", "ideProcessoReenquadramento")
				.createAlias("ideStatusReenquadramento", "ideStatusReenquadramento")
				.createAlias("idePessoa", "pessoa")
				.createAlias("pessoa.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoa.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideProcessoReenquadramento.ideProcessoReenquadramento", ideProcessoReenquadramento));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TramitacaoReenquadramentoProcesso> buscarTramitacaoPorReenquadramentoProcessoEStatus(Integer ideProcessoReenquadramento, Integer ideStatusReenquadramento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TramitacaoReenquadramentoProcesso.class)
			
			//.createAlias("ideStatusReenquadramento","st", JoinType.INNER_JOIN)
			//.createAlias("idePessoa","p", JoinType.INNER_JOIN)

			.add(Restrictions.eq("ideProcessoReenquadramento.ideProcessoReenquadramento", ideProcessoReenquadramento))
			.add(Restrictions.eq("ideStatusReenquadramento.ideStatusReenquadramento", ideStatusReenquadramento))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideTramitacaoReenquadramentoProcesso"),"ideTramitacaoReenquadramentoProcesso")
				//.add(Projections.property("st.ideStatusReenquadramento"),"ideStatusReenquadramento.ideStatusReenquadramento")
				//.add(Projections.property("p.idePessoa"),"idePessoa.idePessoa")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TramitacaoReenquadramentoProcesso.class))
		;
		
		return tramitacaoReenquadramentoProcessoDAO.listarPorCriteria(criteria);
	}
}
