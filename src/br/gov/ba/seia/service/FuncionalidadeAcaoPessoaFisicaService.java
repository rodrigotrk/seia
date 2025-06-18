package br.gov.ba.seia.service;

import java.util.Collection;
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
import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisica;
import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisicaPauta;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.enumerator.FuncionalidadeControlePermissaoAcessoPautaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FuncionalidadeAcaoPessoaFisicaService {

	@Inject
	IDAO<FuncionalidadeAcaoPessoaFisica> funcionalidadeAcaoPessoaFisicaDAO;
	@Inject
	IDAO<FuncionalidadeAcaoPessoaFisicaPauta> funcionalidadeAcaoPessoaFisicaPautaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FuncionalidadeAcaoPessoaFisica funcionalidadeAcaoPessoaFisica) {
		funcionalidadeAcaoPessoaFisicaDAO.salvar(funcionalidadeAcaoPessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(FuncionalidadeAcaoPessoaFisica funcionalidadeAcaoPessoaFisica) {
		funcionalidadeAcaoPessoaFisicaDAO.remover(funcionalidadeAcaoPessoaFisica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FuncionalidadeAcaoPessoaFisica buscarFuncionalidadeAcaoPessoaFisicaPorFuncionalidadePorAcaoPorFuncionario
		(Funcionalidade ideFuncionalidade, Acao ideAcao, Funcionario idePessoaFisica)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FuncionalidadeAcaoPessoaFisica.class)
				
				.createAlias("ideFuncionalidade", "f", JoinType.INNER_JOIN)
				.createAlias("ideAcao", "a", JoinType.INNER_JOIN)
				.createAlias("idePessoaFisica", "pf", JoinType.INNER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideFuncionalidadeAcaoPessoaFisica"), "ideFuncionalidadeAcaoPessoaFisica")
					.add(Projections.property("f.ideFuncionalidade"), "ideFuncionalidade.ideFuncionalidade")
					.add(Projections.property("a.ideAcao"), "ideAcao.ideAcao")
					.add(Projections.property("pf.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(FuncionalidadeAcaoPessoaFisica.class))
				
				.add(Restrictions.eq("f.ideFuncionalidade", ideFuncionalidade.getIdeFuncionalidade()))
				.add(Restrictions.eq("a.ideAcao", ideAcao.getIdeAcao()))
				.add(Restrictions.eq("pf.idePessoaFisica", idePessoaFisica.getIdePessoaFisica()));
		
		return funcionalidadeAcaoPessoaFisicaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FuncionalidadeAcaoPessoaFisica> listarPessoaFisicaGrupoAcessoPautaPorCoordenador(PessoaFisica idePessoaFisica)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FuncionalidadeAcaoPessoaFisica.class);
		
		criteria
			.createAlias("funcionalidadeAcaoPessoaFisicaPautaCollection","fapfp", JoinType.INNER_JOIN)
			.createAlias("ideAcao", "a", JoinType.INNER_JOIN)
			.createAlias("ideFuncionalidade", "f", JoinType.INNER_JOIN)
			.createAlias("fapfp.idePauta", "pta", JoinType.INNER_JOIN)
			
			.createAlias("pta.idePessoaFisica", "funcGestor", JoinType.INNER_JOIN)
			
			.createAlias("idePessoaFisica", "func", JoinType.INNER_JOIN)
			.createAlias("func.pessoaFisica", "pf", JoinType.INNER_JOIN)
			.createAlias("pf.usuario", "u", JoinType.INNER_JOIN)
			.createAlias("u.idePerfil", "p", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("funcGestor.idePessoaFisica", idePessoaFisica.getIdePessoaFisica()))
			.add(Restrictions.eq("f.ideFuncionalidade", FuncionalidadeControlePermissaoAcessoPautaEnum.PERMISSAO_DE_ACESSO_A_PAUTA.getId()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideFuncionalidadeAcaoPessoaFisica"),"ideFuncionalidadeAcaoPessoaFisica")
				.add(Projections.property("ideFuncionalidade.ideFuncionalidade"),"ideFuncionalidade.ideFuncionalidade")
				.add(Projections.property("a.ideAcao"),"ideAcao.ideAcao")
				.add(Projections.property("f.ideFuncionalidade"),"ideFuncionalidade.ideFuncionalidade")
				.add(Projections.property("func.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.idePessoaFisica"),"idePessoaFisica.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"),"idePessoaFisica.pessoaFisica.nomPessoa")
				.add(Projections.property("u.idePessoaFisica"),"idePessoaFisica.pessoaFisica.usuario.idePessoaFisica")
				.add(Projections.property("p.idePerfil"),"idePessoaFisica.pessoaFisica.usuario.idePerfil.idePerfil")
				.add(Projections.property("p.dscPerfil"),"idePessoaFisica.pessoaFisica.usuario.idePerfil.dscPerfil")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FuncionalidadeAcaoPessoaFisica.class))
		;
		
		List<FuncionalidadeAcaoPessoaFisica> lista = funcionalidadeAcaoPessoaFisicaDAO.listarPorCriteria(criteria);
		
		for(FuncionalidadeAcaoPessoaFisica f : lista) {
			Collection<FuncionalidadeAcaoPessoaFisicaPauta> funcionalidadeAcaoPessoaFisicaPautaCollection = carregar(f);
			f.setFuncionalidadeAcaoPessoaFisicaPautaCollection(funcionalidadeAcaoPessoaFisicaPautaCollection);
		}
		
		return lista;
		
	}

	public Collection<FuncionalidadeAcaoPessoaFisicaPauta> carregar(FuncionalidadeAcaoPessoaFisica f)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FuncionalidadeAcaoPessoaFisicaPauta.class);
		criteria
			.createAlias("ideFuncionalidadeAcaoPessoaFisica","fapf", JoinType.INNER_JOIN)
			.createAlias("idePauta","pta", JoinType.INNER_JOIN)
			.add(Restrictions.eq("fapf.ideFuncionalidadeAcaoPessoaFisica", f.getIdeFuncionalidadeAcaoPessoaFisica()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("indSubstituto"),"indSubstituto")
				.add(Projections.property("fapf.ideFuncionalidadeAcaoPessoaFisica"),"ideFuncionalidadeAcaoPessoaFisica.ideFuncionalidadeAcaoPessoaFisica")
				.add(Projections.property("pta.idePauta"),"idePauta.idePauta")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FuncionalidadeAcaoPessoaFisicaPauta.class))
		;
		
		return funcionalidadeAcaoPessoaFisicaPautaDAO.listarPorCriteria(criteria);
	}	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerTudo(Funcionario funcionario, Pauta pauta, Collection<Acao> acoes) {
		
		if(!Util.isNullOuVazio(acoes)) {

			StringBuilder sql = new StringBuilder();
			
			sql.append("select fapf ");
			sql.append("from FuncionalidadeAcaoPessoaFisica fapf ");
			sql.append("inner join fapf.idePessoaFisica func ");
			sql.append("inner join fapf.ideAcao ac ");
			sql.append("inner join fapf.funcionalidadeAcaoPessoaFisicaPautaCollection fapfp ");
			sql.append("where fapfp.idePauta = :pauta ");
			sql.append("and ac in :acoes ");
			sql.append("and func = :funcionario ");
			
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pauta", pauta);
			params.put("acoes", acoes);
			params.put("funcionario", funcionario);
			
			List<FuncionalidadeAcaoPessoaFisica> lista = funcionalidadeAcaoPessoaFisicaDAO.listarPorQuery(sql.toString(), params);
			
			if(!Util.isNullOuVazio(lista)) {
				sql = new StringBuilder();
				
				sql.append("delete from FuncionalidadeAcaoPessoaFisicaPauta fapfp ");
				sql.append("where fapfp.ideFuncionalidadeAcaoPessoaFisica in :lista ");
				
				params = new HashMap<String, Object>();
				params.put("lista", lista);
				
				funcionalidadeAcaoPessoaFisicaDAO.executarQuery(sql.toString(), params);
				
				
				sql = new StringBuilder();
				
				sql.append("delete from FuncionalidadeAcaoPessoaFisica fapf ");
				sql.append("where fapf in :lista ");
				
				params = new HashMap<String, Object>();
				params.put("lista", lista);
				
				funcionalidadeAcaoPessoaFisicaDAO.executarQuery(sql.toString(), params);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FuncionalidadeAcaoPessoaFisica> listarPermissoes(PessoaFisica funcionario, Pauta pautaGestor)  {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("idePessoaFisica", funcionario);
		params.put("idePauta", pautaGestor);
		
		StringBuilder sql = new StringBuilder();
		sql.append("select fapf ");
		sql.append("from FuncionalidadeAcaoPessoaFisicaPauta fapfPauta ");
		sql.append("     inner join fapfPauta.ideFuncionalidadeAcaoPessoaFisica fapf ");
		sql.append("where fapf.idePessoaFisica = :idePessoaFisica ");
		sql.append("      and fapfPauta.idePauta = :idePauta ");
		
		return funcionalidadeAcaoPessoaFisicaDAO.listarPorQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FuncionalidadeAcaoPessoaFisica> listarPermissoes(Integer idePessoaFisicaLogada, Integer idePessoaFisicaGestor)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FuncionalidadeAcaoPessoaFisica.class);
		
		criteria
			.createAlias("idePessoaFisica", "pf", JoinType.INNER_JOIN)
			.createAlias("funcionalidadeAcaoPessoaFisicaPautaCollection", "fapf", JoinType.INNER_JOIN)
			.createAlias("fapf.idePauta", "pauta", JoinType.INNER_JOIN)
			.createAlias("pauta.idePessoaFisica","funcPauta", JoinType.LEFT_OUTER_JOIN)
			.createAlias("pauta.ideArea","area", JoinType.LEFT_OUTER_JOIN)
			.createAlias("area.idePessoaFisica","funcArea", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("pf.idePessoaFisica", idePessoaFisicaLogada))
			
			.add(Restrictions.or(
					Restrictions.eq("funcPauta.idePessoaFisica", idePessoaFisicaGestor),
					Restrictions.eq("funcArea.idePessoaFisica", idePessoaFisicaGestor)
				)
			)
			
			.setProjection(Projections.projectionList()
					.add(Projections.property("ideFuncionalidade.ideFuncionalidade"),"ideFuncionalidade.ideFuncionalidade")
					.add(Projections.property("ideAcao.ideAcao"),"ideAcao.ideAcao")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FuncionalidadeAcaoPessoaFisica.class))		
		;
		
		return funcionalidadeAcaoPessoaFisicaDAO.listarPorCriteria(criteria);
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTemPermissao(int idePessoaLogada, int permissao)  {
	
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FuncionalidadeAcaoPessoaFisica.class, "funAcaoPessoa")
				.createAlias("idePessoaFisica", "pf")
				.createAlias("ideAcao", "acao")
				.add(Restrictions.eq("pf.idePessoaFisica", idePessoaLogada))
				.add(Restrictions.eq("acao.ideAcao", permissao));
		
		List<FuncionalidadeAcaoPessoaFisica> listaPermissao = funcionalidadeAcaoPessoaFisicaDAO.listarPorCriteria(detachedCriteria);
		
		if(!Util.isNullOuVazio(listaPermissao)) {
			return true;
		}
		else{
			return false;
		}
		
	}
}