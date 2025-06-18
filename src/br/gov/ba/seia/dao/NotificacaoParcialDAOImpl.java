package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dto.NotificacaoFinalDTO;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoParcial;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificacaoParcialDAOImpl {
	
	@EJB
	private PautaDAOImpl pautaDAOImpl;
	
	@Inject
	private IDAO<NotificacaoParcial> notificacaoParcialDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcessos(NotificacaoParcial notificacaoParcial)  {
		notificacaoParcialDAO.remover(notificacaoParcial);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(NotificacaoParcial notificacaoParcial)  {
		notificacaoParcialDAO.atualizar(notificacaoParcial);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(NotificacaoParcial notificacaoParcial)  {
		notificacaoParcialDAO.salvarOuAtualizar(notificacaoParcial);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NotificacaoParcial> listarNotificacaoParcialByProcesso(Processo processo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoParcial.class);
		criteria.createAlias("ideProcesso", "processo");
		criteria.createAlias("idePauta", "pauta");
		criteria.createAlias("pauta.idePessoaFisica", "funcionario");
		criteria.createAlias("funcionario.pessoaFisica", "pessoaFisica");
		criteria.add(Restrictions.eq("processo.ideProcesso", processo.getIdeProcesso()));
		return notificacaoParcialDAO.listarPorCriteria(criteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NotificacaoParcial> listarNotificacaoPrazoParcialByProcesso(Processo processo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoParcial.class);
		criteria.createAlias("ideProcesso", "processo");
		criteria.createAlias("idePauta", "pauta");
		criteria.createAlias("pauta.idePessoaFisica", "funcionario");
		criteria.createAlias("funcionario.pessoaFisica", "pessoaFisica");
		criteria.add(Restrictions.eq("processo.ideProcesso", processo.getIdeProcesso()));
		criteria.add(Restrictions.eq("tipo", 1));
		return notificacaoParcialDAO.listarPorCriteria(criteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NotificacaoParcial> listarNotificacaoComunicacaoParcialByProcesso(Processo processo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoParcial.class);
		criteria.createAlias("ideProcesso", "processo");
		criteria.createAlias("idePauta", "pauta");
		criteria.createAlias("pauta.idePessoaFisica", "funcionario");
		criteria.createAlias("funcionario.pessoaFisica", "pessoaFisica");
		criteria.add(Restrictions.eq("processo.ideProcesso", processo.getIdeProcesso()));
		criteria.add(Restrictions.eq("tipo", 2));
		return notificacaoParcialDAO.listarPorCriteria(criteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<NotificacaoParcial> listarNotificacaoParcialBy(Processo ideProcesso, Pauta pautaFuncionario)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoParcial.class);
		criteria.add(Restrictions.eq("ideProcesso.ideProcesso", ideProcesso.getIdeProcesso()));
		criteria.add(Restrictions.eq("idePauta.idePauta", pautaFuncionario.getIdePauta()));
		criteria.add(Restrictions.eq("indEmissao", Boolean.FALSE));
		return notificacaoParcialDAO.listarPorCriteria(criteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<NotificacaoParcial> listarNotificacaoParcialByTipo(Processo ideProcesso, Pauta pautaFuncionario, TipoNotificacaoEnum tipoNotificacaoEnum)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoParcial.class);
		criteria.add(Restrictions.eq("ideProcesso.ideProcesso", ideProcesso.getIdeProcesso()));
		criteria.add(Restrictions.eq("idePauta.idePauta", pautaFuncionario.getIdePauta()));
		criteria.add(Restrictions.eq("indEmissao", Boolean.FALSE));
		criteria.add(Restrictions.eq("tipo", tipoNotificacaoEnum.getTipo()));
		return notificacaoParcialDAO.listarPorCriteria(criteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NotificacaoParcial obterNotificacaoParcial(Usuario usuario, Processo ideProcesso)  {
		// Parcial - > buscar por processo + pauta_logada + ind_emissao false
		Pauta pautaFuncionario = pautaDAOImpl.obtemPautaPorIdeFuncionario(usuario.getIdePessoaFisica());
		List<NotificacaoParcial> listaNotificacao = listarNotificacaoParcialBy(ideProcesso, pautaFuncionario);

		if (Util.isNullOuVazio(listaNotificacao)) {
			return new NotificacaoParcial();
		}

		for (NotificacaoParcial notificacao : listaNotificacao) {
			if (notificacao.getIndEmissao() == false) {
				return notificacao;
			}

		}

		return listaNotificacao.get(listaNotificacao.size() - 1);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NotificacaoParcial obterNotificacaoParcialByTipo(Usuario usuario, Processo ideProcesso, TipoNotificacaoEnum tipoNotificacaoEnum)  {
		// Parcial - > buscar por processo + pauta_logada + ind_emissao false
		Pauta pautaFuncionario = pautaDAOImpl.obtemPautaPorIdeFuncionario(usuario.getIdePessoaFisica());
		List<NotificacaoParcial> listaNotificacao = listarNotificacaoParcialByTipo(ideProcesso, pautaFuncionario, tipoNotificacaoEnum);

		if (Util.isNullOuVazio(listaNotificacao)) {
			return new NotificacaoParcial();
		}

		for (NotificacaoParcial notificacao : listaNotificacao) {
			if (notificacao.getIndEmissao() == false) {
				return notificacao;
			}

		}

		return listaNotificacao.get(listaNotificacao.size() - 1);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<NotificacaoParcial> listarNotificacaoParcial() {
		return notificacaoParcialDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NotificacaoParcial> listarNotificacaoParcialPorPessoaFisica(Pauta IdePauta, Processo ideProcesso) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idePauta", IdePauta);
		params.put("ideProcesso", ideProcesso);
		params.put("indEmissao", false);
		return notificacaoParcialDAO.buscarPorNamedQuery("NotificacaoParcial.findByPautaProcesso", params);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NotificacaoParcial carregaGet(NotificacaoParcial notificacaoParcial){		
		return notificacaoParcialDAO.carregarGet(notificacaoParcial.getIdeNotificacaoParcial());
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NotificacaoParcial> listarNotificacaoParcialPorNotificacaoFinal(Integer ideNotificacao)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(NotificacaoParcial.class)
			.createAlias("ideNotificacao", "n", JoinType.INNER_JOIN)
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("idePauta", "pta", JoinType.INNER_JOIN)
			.createAlias("pta.idePessoaFisica", "fun", JoinType.INNER_JOIN)
			.createAlias("fun.pessoaFisica", "pf", JoinType.INNER_JOIN)
			.add(Restrictions.eq("n.ideNotificacao",ideNotificacao))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideNotificacaoParcial"),"ideNotificacaoParcial")
				.add(Projections.property("dscTextoParcial"),"dscTextoParcial")
				.add(Projections.property("indEmissao"),"indEmissao")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")
				.add(Projections.property("tipo"),"tipo")
				.add(Projections.property("n.ideNotificacao"),"ideNotificacao.ideNotificacao")
				.add(Projections.property("p.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("pta.idePauta"),"idePauta.idePauta")
				.add(Projections.property("fun.idePessoaFisica"),"idePauta.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.idePessoaFisica"),"idePauta.idePessoaFisica.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"),"idePauta.idePessoaFisica.pessoaFisica.nomPessoa")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(NotificacaoParcial.class))
		;
		return notificacaoParcialDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NotificacaoParcial> listarNotificacaoParcialPorNotificacaoFinalComDependencias(Notificacao ideNotificacao)  {
		
		DetachedCriteria  criteria = DetachedCriteria.forClass(NotificacaoParcial.class);
		
		criteria
			.createAlias("idePauta", "pauta", JoinType.INNER_JOIN)
			.createAlias("pauta.idePessoaFisica", "func", JoinType.INNER_JOIN)
			.createAlias("func.pessoaFisica", "pf", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideNotificacao", ideNotificacao))
			.setProjection(Projections.projectionList()
					.add(Projections.property("ideNotificacaoParcial"),"ideNotificacaoParcial")
					.add(Projections.property("dscTextoParcial"),"dscTextoParcial")
					.add(Projections.property("indEmissao"),"indEmissao")
					.add(Projections.property("dtcCriacao"),"dtcCriacao")
					.add(Projections.property("tipo"),"tipo")
					.add(Projections.property("idePauta"),"idePauta")
					.add(Projections.property("pf.nomPessoa"),"idePauta.idePessoaFisica.pessoaFisica.nomPessoa")
					.add(Projections.property("ideProcesso"),"ideProcesso")
					.add(Projections.property("ideNotificacao"),"ideNotificacao"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(NotificacaoParcial.class))
			;
		
		return notificacaoParcialDAO.listarPorCriteria(criteria);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<NotificacaoParcial> filtrarListaProcessos(NotificacaoParcial notificacaoParcial) {
		return notificacaoParcialDAO.listarPorExemplo(notificacaoParcial);
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NotificacaoParcial filtrarProcessoById(NotificacaoParcial notificacaoParcial)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcesso", notificacaoParcial.getIdeProcesso());
		return notificacaoParcialDAO.buscarEntidadePorNamedQuery("NotificacaoParcial.findByIdeNotificacaoParcial", params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NotificacaoParcial carregarProcesso(Integer id){
		NotificacaoParcial notificacaoParcial = notificacaoParcialDAO.carregarGet(id);
		Hibernate.initialize(notificacaoParcial.getIdeNotificacaoParcial());
		return notificacaoParcial;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NotificacaoParcial> listarNotificacaoParcial(Integer ideProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoParcial.class);
		criteria.add(Restrictions.eq("ideProcesso.ideProcesso", ideProcesso));
		
		List<NotificacaoParcial> list = notificacaoParcialDAO.listarPorCriteria(criteria);
		
		for(NotificacaoParcial np : list){
			Hibernate.initialize(np.getIdePauta().getIdePessoaFisica().getPessoaFisica());
		}
		
		return list;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NotificacaoParcial> listarNotificacaoParcialPorTecnicos(Processo ideProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoParcial.class);
		criteria.add(Restrictions.eq("ideProcesso", ideProcesso))
			.add(Restrictions.eq("indEmissao", Boolean.FALSE))
			.setFetchMode("idePauta", FetchMode.JOIN)
			.setFetchMode("idePauta.idePessoaFisica", FetchMode.JOIN)
			.setFetchMode("idePauta.idePessoaFisica.pessoaFisica", FetchMode.JOIN);
		return notificacaoParcialDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NotificacaoParcial> listarNotificacaoParcialPorTecnicosTipoNot(NotificacaoFinalDTO dto, Integer ideAreaCoordenador)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoParcial.class);
		criteria
			.createAlias("idePauta","pta", JoinType.INNER_JOIN)
			.createAlias("ideProcesso","p", JoinType.INNER_JOIN)
			.createAlias("pta.idePessoaFisica","fun", JoinType.INNER_JOIN)
			.createAlias("fun.pessoaFisica", "pf", JoinType.INNER_JOIN)
			.createAlias("p.equipeCollection", "eq", JoinType.INNER_JOIN)
			.createAlias("eq.ideArea", "aeq", JoinType.INNER_JOIN)
			
			.createAlias("ideNotificacao","n", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("indEmissao", false))
			.add(Restrictions.eq("tipo", dto.getTipoNotificacaoEnum().getTipo()))
			.add(Restrictions.eq("p.ideProcesso", dto.getProcesso().getIdeProcesso()))
			.add(Restrictions.eq("aeq.ideArea",ideAreaCoordenador))
			
			.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections.property("ideNotificacaoParcial")))
				.add(Projections.property("ideNotificacaoParcial"), "ideNotificacaoParcial")
				.add(Projections.property("dscTextoParcial"), "dscTextoParcial")
				.add(Projections.property("indEmissao"), "indEmissao")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("tipo"), "tipo")
				.add(Projections.property("p.ideProcesso"), "ideProcesso.ideProcesso")
				.add(Projections.property("pta.idePauta"), "idePauta.idePauta")
				.add(Projections.property("fun.idePessoaFisica"), "idePauta.idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.idePessoaFisica"), "idePauta.idePessoaFisica.pessoaFisica.idePessoafisica")
				.add(Projections.property("pf.nomPessoa"), "idePauta.idePessoaFisica.pessoaFisica.nomPessoa")
				.add(Projections.property("n.ideNotificacao"), "ideNotificacao.ideNotificacao")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(NotificacaoParcial.class))
		;

		return notificacaoParcialDAO.listarPorCriteria(criteria);
	}
	
}