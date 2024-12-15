package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.ProcuradorPfEmpreendimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcuradorPfEmpreendimentoService {

	@Inject
	private IDAO<ProcuradorPfEmpreendimento> procuradorPfEmpreendimentoDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ProcuradorPfEmpreendimento procuradorPfEmpreendimento)  {
		procuradorPfEmpreendimentoDAO.salvarOuAtualizar(procuradorPfEmpreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ProcuradorPfEmpreendimento procuradorPfEmpreendimento)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcuradorPfEmpreendimento", procuradorPfEmpreendimento.getIdeProcuradorPfEmpreendimento());
		params.put("dtcExclusao", new Date());
		params.put("indExcluido", Boolean.TRUE);
		procuradorPfEmpreendimentoDAO.executarNamedQuery("ProcuradorPfEmpreendimento.UpdateParaExclusao", params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorPfEmpreendimento> listarByEmpreendimento(Empreendimento empreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcuradorPfEmpreendimento.class);
		criteria.createAlias("ideProcuradorPessoaFisica", "procPf");
		criteria.createAlias("procPf.ideProcurador", "pessoaFisicaProcurador");
		criteria.createAlias("ideEmpreendimento", "empreendimento");
		criteria.add(Restrictions.eq("empreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		
		return procuradorPfEmpreendimentoDAO.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorPfEmpreendimento> listarByEmpreendimentoComProjection(Empreendimento empreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcuradorPfEmpreendimento.class);
		criteria.createAlias("ideProcuradorPessoaFisica", "procPf");
		criteria.createAlias("procPf.ideProcurador", "pessoaFisicaProcurador");
		criteria.createAlias("pessoaFisicaProcurador.pessoa", "pessoa");
		criteria.createAlias("ideEmpreendimento", "empreendimento");
		criteria.add(Restrictions.eq("empreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcuradorPfEmpreendimento"),"ideProcuradorPfEmpreendimento")
				.add(Projections.property("pessoa.desEmail"),"ideProcuradorPessoaFisica.ideProcurador.pessoa.desEmail")
				.add(Projections.property("empreendimento.nomEmpreendimento"),"ideEmpreendimento.nomEmpreendimento")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorPfEmpreendimento.class));
		
		return procuradorPfEmpreendimentoDAO.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorPfEmpreendimento buscarPorIdViaCriteria(ProcuradorPfEmpreendimento procurador)  {
		DetachedCriteria criteria = DetachedCriteria
				.forClass(ProcuradorPfEmpreendimento.class)
				.add(Restrictions.eq("ideProcuradorPessoaFisica.ideProcuradorPessoaFisica",
						procurador.getIdeProcuradorPfEmpreendimento()))
				.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		return procuradorPfEmpreendimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorPfEmpreendimento buscarProcuradorByPessoaAndRequerente(Integer idePessoa, Integer ideRequerente)
			 {
		DetachedCriteria criteria = getCriteriaBuscarProcbyPessoaAndRequerente(idePessoa, ideRequerente);

		return this.procuradorPfEmpreendimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorPfEmpreendimento> listarProcuradorPfEmpreendimentoPorRequerimento(Integer ideRequerimento, List<Integer> listaIdeEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcuradorPfEmpreendimento.class);
		
		criteria
			.add(Restrictions.in("ideEmpreendimento.ideEmpreendimento", listaIdeEmpreendimento))
			.add(Restrictions.eq("indExcluido", false))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcuradorPfEmpreendimento"), "ideProcuradorPfEmpreendimento")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("indExcluido"), "indExcluido")
				.add(Projections.property("dtcExclusao"), "dtcExclusao")
				.add(Projections.property("dscCaminhoProcuracao"), "dscCaminhoProcuracao")
				.add(Projections.property("dtcInicioProcuracao"), "dtcInicioProcuracao")
				.add(Projections.property("dtcFimProcuracao"), "dtcFimProcuracao")
				.add(Projections.property("indAssinaturaObrigatoria"), "indAssinaturaObrigatoria")
				.add(Projections.property("ideProcuradorPessoaFisica.ideProcuradorPessoaFisica"), "ideProcuradorPessoaFisica.ideProcuradorPessoaFisica")
				.add(Projections.property("ideEmpreendimento.ideEmpreendimento"), "ideEmpreendimento.ideEmpreendimento")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorPfEmpreendimento.class));
		
		return procuradorPfEmpreendimentoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorPfEmpreendimento buscarProcuradorByPessoaAndRequerenteAndEmpreendimento(Integer idePessoa, Integer ideRequerente, Integer ideEmpreendimento)
			 {
		DetachedCriteria criteria = getCriteriaBuscarProcbyPessoaAndRequerente(idePessoa, ideRequerente);
		criteria.createAlias("ideEmpreendimento", "empreendimento", JoinType.INNER_JOIN)
		.add(Restrictions.eq("empreendimento.ideEmpreendimento", ideEmpreendimento))
		.add(Restrictions.eq("indExcluido", false));

		return this.procuradorPfEmpreendimentoDAO.buscarPorCriteria(criteria);
	}

	/**
	 * @param idePessoa
	 * @param ideRequerente
	 * @return
	 * @throws HibernateException
	 */
	private DetachedCriteria getCriteriaBuscarProcbyPessoaAndRequerente(Integer idePessoa, Integer ideRequerente)			 {
		return DetachedCriteria.forClass(ProcuradorPfEmpreendimento.class)
				.createAlias("ideProcuradorPessoaFisica", "ippf", JoinType.INNER_JOIN)
				.createAlias("ippf.idePessoaFisica", "pf", JoinType.INNER_JOIN)
				.createAlias("ippf.ideProcurador", "proc", JoinType.INNER_JOIN)
				.add(Restrictions.eq("pf.idePessoaFisica", ideRequerente))
				.add(Restrictions.eq("proc.idePessoaFisica", idePessoa));
	}
}
