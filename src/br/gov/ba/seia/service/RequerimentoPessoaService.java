package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RequerimentoPessoaService {

	@Inject
	private IDAO<RequerimentoPessoa> requerimentoPessoaDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRequerimentoPessoa(RequerimentoPessoa requerimentoPessoa) {
		
		if(!isExisteRequerimentoPessoa(requerimentoPessoa)){
			requerimentoPessoaDAO.salvarOuAtualizar(requerimentoPessoa);			
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RequerimentoPessoa obterPorRequerimentoETipoPessoa(Requerimento pRequerimento, TipoPessoaRequerimento pTipoPessoaRequerimento){
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimento", pRequerimento.getIdeRequerimento());
		parametros.put("ideTipoPessoaRequerimento", pTipoPessoaRequerimento.getIdeTipoPessoaRequerimento());
		return requerimentoPessoaDAO.buscarEntidadePorNamedQuery("RequerimentoPessoa.findByRequerimentoPessoa", parametros);
	}
	
	public Collection<RequerimentoPessoa> buscarRequerimentoPessoaPorRequerimentoUnico(RequerimentoUnico requerimento) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimento", requerimento.getIdeRequerimentoUnico());
		List<RequerimentoPessoa> collRequerimentoPessoa = requerimentoPessoaDAO.buscarPorNamedQuery("RequerimentoPessoa.findByIdeRequerimento", parametros);		
		return collRequerimentoPessoa;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RequerimentoPessoa buscarRequerimentoPessoa(Integer ideRequerimento, Integer tipoPessoaRequerimento) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimento", ideRequerimento);
		parametros.put("ideTipoPessoaRequerimento", tipoPessoaRequerimento);
		return requerimentoPessoaDAO.obterPorNamedQuery("RequerimentoPessoa.findByRequerimentoPessoa", parametros);
	}
	
	public boolean isPessoaAssociadoRequerimento(int idePessoa, int ideRequerimento) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoPessoa.class)
				.createAlias("requerimento", "r")
				.createAlias("pessoa", "p")
				.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
				.add(Restrictions.eq("p.idePessoa", idePessoa));
		
		if(Util.isNullOuVazio(requerimentoPessoaDAO.buscarPorCriteria(criteria))){
			return false;
		}
		else{
			return true;		
		}
	}

	
	public Collection<RequerimentoPessoa> listarClientesDoRequerimento(Integer ideRequerimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoPessoa.class, "rp");
		
		
		criteria		
			.createAlias("rp.ideTipoPessoaRequerimento", "tipo")
			.createAlias("rp.pessoa", "pessoa")
			.createAlias("pessoa.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento))
			.add(Restrictions.eq("pessoa.indExcluido", false))
			.add(Restrictions.not(Restrictions.in("tipo.ideTipoPessoaRequerimento", new Integer[]{TipoPessoaRequerimentoEnum.ATENDENTE.getId()})));
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(ProcuradorRepresentante.class, "pr");
		subCriteria.add(Restrictions.eq("pr.indExcluido", true))
			.add(Restrictions.or(Restrictions.eqProperty("pr.ideProcurador", "pf.idePessoaFisica"), 
				Restrictions.eqProperty("pr.idePessoaJuridica", "pessoa.idePessoa")))
			.setProjection(Projections.property("ideProcurador"));
		
		criteria.add(Property.forName("pessoa.idePessoa").notIn(subCriteria))
				.setProjection(Projections.projectionList()
				.add(Projections.property("pessoa.desEmail"),"pessoa.desEmail")
				.add(Projections.property("pessoa.idePessoa"), "pessoa.idePessoa"))
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(RequerimentoPessoa.class))
		;
		
		return this.requerimentoPessoaDAO.listarPorCriteria(criteria);
	}
	
	public Collection<RequerimentoPessoa> listarEnvolvidosRequerimento(Integer ideRequerimento) throws Exception{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoPessoa.class)
				.createAlias("requerimento", "requerimento")
				.createAlias("pessoa", "pessoa")
				.createAlias("ideTipoPessoaRequerimento", "tipoPessoa")
		
		.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));
		
		return this.requerimentoPessoaDAO.listarPorCriteria(detachedCriteria);
	}
	
	
	public boolean isExisteRequerimentoPessoa(RequerimentoPessoa requerimentoPessoa) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoPessoa.class)
				.createAlias("requerimento", "requerimento")
				.createAlias("pessoa", "pessoa")
				.createAlias("ideTipoPessoaRequerimento", "tipoPessoa")

		.add(Restrictions.eq("requerimento.ideRequerimento", requerimentoPessoa.getRequerimento().getIdeRequerimento()))
		.add(Restrictions.eq("pessoa.idePessoa",             requerimentoPessoa.getPessoa().getIdePessoa()))
		.add(Restrictions.eq("tipoPessoa.ideTipoPessoaRequerimento",     requerimentoPessoa.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento()));
		
		return requerimentoPessoaDAO.buscarPorCriteria(detachedCriteria) != null;
		
	}

}