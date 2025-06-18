package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.List;

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

import br.gov.ba.seia.dao.AcaoDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AcaoService {

	@Inject
	AcaoDAOImpl acaoDAOImpl;
	
	@Inject
	IDAO<Acao> acaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Acao> listarPermissoesPorFuncionalidadePorFuncionarioPorGestor(Funcionalidade funcionalidade, Funcionario funcionario, Pauta pauta) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Acao.class);
		criteria
			.createAlias("funcionalidadeAcaoPessoaFisicaCollection", "fapf", JoinType.INNER_JOIN)
			.createAlias("fapf.funcionalidadeAcaoPessoaFisicaPautaCollection","fapfp", JoinType.INNER_JOIN)
			.createAlias("fapfp.idePauta","pta", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("pta.idePauta", pauta.getIdePauta()))
			.add(Restrictions.eq("fapf.idePessoaFisica", funcionario))
			.add(Restrictions.eq("fapf.ideFuncionalidade", funcionalidade))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideAcao"),"ideAcao")
				.add(Projections.property("dscAcao"),"dscAcao")
				.add(Projections.property("indExcluido"),"indExcluido")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")
				.add(Projections.property("dtcExclusao"),"dtcExclusao")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Acao.class))
		;
		
		return acaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Acao filtrarById(Acao pAcao) {
		return this.acaoDAOImpl.filtrarById(pAcao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Acao carregaGet(Acao pAcao){
		return this.acaoDAO.carregarGet(pAcao.getIdeAcao());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Acao> filtrarLista(Acao pAcao) {
		return this.acaoDAOImpl.getAcoes(pAcao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Acao pAcao)  {
		this.acaoDAOImpl.salvar(pAcao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Acao pAcao)  {
		this.acaoDAOImpl.atualizar(pAcao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Acao pAcao) {
		this.acaoDAOImpl.excluir(pAcao);
	}
	
	
}