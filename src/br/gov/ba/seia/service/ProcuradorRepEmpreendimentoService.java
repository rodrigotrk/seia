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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.ProcuradorRepEmpreendimento;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcuradorRepEmpreendimentoService {
	
	
	@Inject
	private IDAO<ProcuradorRepEmpreendimento> procuradorRepEmpreendimentoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ProcuradorRepEmpreendimento procuradorRepEmpreendimento)  {
		procuradorRepEmpreendimentoDAO.salvarOuAtualizar(procuradorRepEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ProcuradorRepEmpreendimento procuradorRepEmpreendimento)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcuradorRepEmpreendimento", procuradorRepEmpreendimento.getIdeProcuradorRepEmpreendimento());
		params.put("dtcExclusao", new Date());
		params.put("indExcluido", Boolean.TRUE);
		procuradorRepEmpreendimentoDAO.executarNamedQuery("ProcuradorRepEmpreendimento.UpdateParaExclusao", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorRepEmpreendimento> listarByEmpreendimento(Empreendimento empreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcuradorRepEmpreendimento.class);
		criteria.createAlias("ideProcuradorRepresentante", "procRep");
		criteria.createAlias("procRep.ideProcurador", "pessoaFisicaProcurador");
		criteria.createAlias("ideEmpreendimento", "empreendimento");
		criteria.add(Restrictions.eq("empreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		return procuradorRepEmpreendimentoDAO.listarPorCriteria(criteria);
		
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorRepEmpreendimento> listarByEmpreendimentoComProjection(Empreendimento empreendimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcuradorRepEmpreendimento.class);
		criteria.createAlias("ideProcuradorRepresentante", "procRep");
		criteria.createAlias("procRep.ideProcurador", "pessoaFisicaProcurador");
		criteria.createAlias("pessoaFisicaProcurador.pessoa", "pessoa");
		criteria.createAlias("ideEmpreendimento", "empreendimento");
		criteria.add(Restrictions.eq("empreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcuradorRepEmpreendimento"),"ideProcuradorRepEmpreendimento")
				.add(Projections.property("pessoa.desEmail"),"ideProcuradorRepresentante.ideProcurador.pessoa.desEmail")
				.add(Projections.property("empreendimento.nomEmpreendimento"),"ideEmpreendimento.nomEmpreendimento")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorRepEmpreendimento.class));
		
		return procuradorRepEmpreendimentoDAO.listarPorCriteria(criteria);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorRepEmpreendimento buscarPorIdViaCriteria(ProcuradorRepEmpreendimento procurador)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcuradorRepEmpreendimento.class)
				.add(Restrictions.eq("ideProcuradorRepEmpreendimento", procurador.getIdeProcuradorRepEmpreendimento()))
				.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		return procuradorRepEmpreendimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorRepEmpreendimento buscarPorProcuradorRepresentanteViaCriteria(ProcuradorRepresentante pIdeProcuradorRepresentante, Integer ideEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcuradorRepEmpreendimento.class)
				.add(Restrictions.eq("ideProcuradorRepresentante", pIdeProcuradorRepresentante))
				.add(Restrictions.eq("ideEmpreendimento.ideEmpreendimento", ideEmpreendimento))
				.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		return procuradorRepEmpreendimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProcuradorRepEmpreendimento buscarProcuradorByPessoaAndRequerente(Integer idePessoa, Integer ideRequerente, Integer ideEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcuradorRepEmpreendimento.class)
			.createAlias("ideProcuradorRepresentante", "ipr")
			.createAlias("ipr.idePessoaJuridica", "pj")
			.createAlias("ipr.ideProcurador", "pf")
			
			.add(Restrictions.eq("indExcluido", false))
			.add(Restrictions.eq("pj.idePessoaJuridica", ideRequerente))
			.add(Restrictions.eq("pf.idePessoaFisica", idePessoa))
			.add(Restrictions.gt("dtcFimProcuracao", new Date()))
			.add(Restrictions.eq("ideEmpreendimento.ideEmpreendimento", ideEmpreendimento));
		
		return this.procuradorRepEmpreendimentoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcuradorRepEmpreendimento> listarProcuradorRepEmpreendimentoPorRequerimento(Integer ideRequerimento, List<Integer> listaIdeEmpreendimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcuradorRepEmpreendimento.class);
				
		criteria
			.add(Restrictions.in("ideEmpreendimento.ideEmpreendimento", listaIdeEmpreendimento))
			.add(Restrictions.eq("indExcluido", false))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideProcuradorRepEmpreendimento"),"ideProcuradorRepEmpreendimento")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")
				.add(Projections.property("indExcluido"),"indExcluido")
				.add(Projections.property("dtcExclusao"),"dtcExclusao")
				.add(Projections.property("dscCaminhoProcuracao"),"dscCaminhoProcuracao")
				.add(Projections.property("dtcInicioProcuracao"),"dtcInicioProcuracao")
				.add(Projections.property("dtcFimProcuracao"),"dtcFimProcuracao")
				.add(Projections.property("indAssinaturaObrigatoria"),"indAssinaturaObrigatoria")
				.add(Projections.property("ideProcuradorRepresentante.ideProcuradorRepresentante"),"ideProcuradorRepresentante.ideProcuradorRepresentante")
				.add(Projections.property("ideEmpreendimento.ideEmpreendimento"),"ideEmpreendimento.ideEmpreendimento")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ProcuradorRepEmpreendimento.class))
		;
		
		return procuradorRepEmpreendimentoDAO.listarPorCriteria(criteria);
	}
	
}
