package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ParticipacaoAcionaria;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class ParticipacaoAcionariaDAOImpl {
	
	@Inject
	IDAO<ParticipacaoAcionaria> participacaoAcionariaDao;
	
	public void salvarParticipacaoAcionaria(ParticipacaoAcionaria participacaoAcionaria) {
		participacaoAcionariaDao.salvarOuAtualizar(participacaoAcionaria);
	}
	
	public Collection<ParticipacaoAcionaria> buscaParticipacaoAcionariaPorPessoaJuridica(PessoaJuridica pessoajuridica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ParticipacaoAcionaria.class,"pa");
		criteria.createAlias("pa.idePessoaJuridica", "pjfiltro");
		criteria.createAlias("pa.idePessoa", "p");
		criteria.createAlias("p.pessoaFisica","pf", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("p.pessoaJuridica","pj", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pj.ideNaturezaJuridica","naturezaJuridica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pf.idePais", "pais", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("pjfiltro.idePessoaJuridica", pessoajuridica.getIdePessoaJuridica()));
		criteria.add(Restrictions.eq("pa.indExcluido", false));
		return participacaoAcionariaDao.listarPorCriteria(criteria);
	}
	
	public void excluirParticipacaoAcionaria(ParticipacaoAcionaria participacaoAcionaria) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideParticipacaoAcionaria", participacaoAcionaria.getIdeParticipacaoAcionaria());
		participacaoAcionariaDao.executarNamedQuery("ParticipacaoAcionaria.removerParticipacaoAcionaria", parametros);
	}
	
	public ParticipacaoAcionaria buscarParticipacaoAcionariaPorID(Integer ideParticipacaoAcionaria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ParticipacaoAcionaria.class,"pa");
			criteria.createAlias("pa.idePessoaJuridica", "pjfiltro");
			criteria.createAlias("pa.idePessoa", "p");
			criteria.createAlias("p.pessoaFisica","pf", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("p.pessoaJuridica","pj", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("pj.ideNaturezaJuridica","naturezaJuridica", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("pf.idePais", "pais", JoinType.LEFT_OUTER_JOIN);
			
			criteria.add(Restrictions.eq("pa.ideParticipacaoAcionaria", ideParticipacaoAcionaria));
			criteria.add(Restrictions.eq("pa.indExcluido", false));
		
		return participacaoAcionariaDao.buscarPorCriteria(criteria);
	}
	
	
	public boolean isParticipanteAcionarioInativado(int idePessoaFisica, int idePessoaJuridica) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RepresentanteLegal.class)
			.createAlias("idePessoa", "idePessoa")
			.createAlias("idePessoaJuridica", "idePessoaJuridica")
			.add(Restrictions.eq("idePessoa.idePessoa", idePessoaFisica))
			.add(Restrictions.eq("idePessoaJuridica.idePessoaJuridica", idePessoaJuridica))
			.add(Restrictions.eq("indExcluido", true))
		
			.setProjection(Projections.projectionList()
					.add(Projections.property("indExcluido"),"indExcluido"))
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(RepresentanteLegal.class));
			

		ParticipacaoAcionaria participante = participacaoAcionariaDao.buscarPorCriteria(detachedCriteria);
		
		if(!Util.isNullOuVazio(participante) && participante.getIndExcluido()){
			return true;
		}else{
			return false;
		}
		
	}

}
