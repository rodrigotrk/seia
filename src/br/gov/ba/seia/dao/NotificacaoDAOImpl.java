package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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

import org.apache.log4j.Level;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.joda.time.DateTime;

import br.gov.ba.seia.dto.NotificacaoFinalDTO;
import br.gov.ba.seia.dto.StatusNotificacao;
import br.gov.ba.seia.entity.IntegranteEquipe;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.service.PermissaoPerfilService;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificacaoDAOImpl {
	
	@Inject
	private IDAO<Notificacao> notificacaoDAO;
	@EJB
	private PermissaoPerfilService permissaoPerfilService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Notificacao notificacao)  {
		notificacaoDAO.salvarOuAtualizar(notificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Notificacao notificacao)  {
		notificacaoDAO.atualizar(notificacao);
		notificacaoDAO.sessionFlush();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacao(){
		return notificacaoDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregarGet(Integer ideNotificacao){
		return notificacaoDAO.carregarGet(ideNotificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregarLoad(Integer ideNotificacao) {
		return notificacaoDAO.carregarLoad(ideNotificacao);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregar(Notificacao notificacao){
		return notificacaoDAO.carregarGet(notificacao.getIdeNotificacao());
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacoesNaoRespondidas() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class);
		criteria.add(Restrictions.isNotNull("dtcEnvio"));
		criteria.add(Restrictions.isNull("indRetorno"));
		criteria.add(Restrictions.isNotNull("dtcValidade"));
		criteria.add(Restrictions.lt("dtcValidade", new Date()));
		
		List<Notificacao>lista = notificacaoDAO.listarPorCriteria(criteria);
		if(!Util.isNullOuVazio(lista)){
			return lista;
		}
		else {
			lista = new ArrayList<Notificacao>();
			return lista;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer bucarQtdNotificacoesEmitidas(Integer ideProcesso, TipoNotificacaoEnum tipoNotificacaoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class);
		criteria
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.add(Restrictions.eq("indAprovado", true))
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			.add(Restrictions.eq("tipo", tipoNotificacaoEnum.getTipo()))
			.setProjection(Projections.count("ideNotificacao"))
		;
		
		return notificacaoDAO.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregarNotificacao(Notificacao notificacao) {
		Map<String,Object> parametros = new HashMap<String, Object>();
		parametros.put("ideNotificacao", notificacao.getIdeNotificacao());
		
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct n ");
		sql.append("from Notificacao n ");
		sql.append("     inner join fetch n.ideProcesso p  ");
		sql.append("     left join fetch n.justificativaRejeicaoCollection jus ");
		sql.append("     left join fetch n.idePautaCriacao pc  ");		
		sql.append("where n.ideNotificacao = :ideNotificacao ");
		
		notificacao = notificacaoDAO.buscarPorQuery(sql.toString(), parametros);
		
		sql = new StringBuilder();
		
		sql.append("select distinct n ");
		sql.append("from Notificacao n ");
		sql.append("     inner join n.ideProcesso p  ");
		sql.append("     left join fetch n.notificacaoMotivoNotificacaoCollection m ");	
		sql.append("where n.ideNotificacao = :ideNotificacao ");
		
		notificacao.setNotificacaoMotivoNotificacaoCollection(
				notificacaoDAO.buscarPorQuery(sql.toString(), parametros).getNotificacaoMotivoNotificacaoCollection());
		
		return notificacao;
		 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacaoEnviada(Integer ideProcesso) {
		DetachedCriteria criteria = getCriteriaComPessoa();
		criteria.add(Restrictions.eq("notificacao.ideProcesso.ideProcesso", ideProcesso));
		criteria.add(Restrictions.eq("notificacao.indAprovado", true));
		return notificacaoDAO.listarPorCriteria(criteria); 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacaoReprovada(Integer ideProcesso) {
		DetachedCriteria criteria = getCriteriaComPessoa();
		criteria.add(Restrictions.eq("notificacao.ideProcesso.ideProcesso", ideProcesso));
		criteria.add(Restrictions.eq("notificacao.indAprovado", false));
		return notificacaoDAO.listarPorCriteria(criteria);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private DetachedCriteria getCriteriaComPessoa(){
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class,"notificacao");
		criteria.createAlias("notificacao.notificacaoMotivoNotificacaoCollection", "motivos",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("notificacao.idePautaAprovacao", "idePautaAprovacao");
		criteria.createAlias("notificacao.idePautaCriacao", "idePautaCriacao");
		criteria.createAlias("idePautaAprovacao.idePessoaFisica","funcionario");
		criteria.createAlias("idePautaCriacao.idePessoaFisica","funcionarioCriacao");
		criteria.createAlias("funcionario.pessoaFisica","pessoafisica");
		criteria.createAlias("funcionarioCriacao.pessoaFisica","pessoafisicaCriacao");
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregarById(Integer ideNotificacao)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class)
			.createAlias("ideProcesso", "processo")
			.createAlias("processo.ideRequerimento", "requerimento")
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideNotificacao"),"ideNotificacao")
				.add(Projections.property("tipo"),"tipo")
				.add(Projections.property("dscNotificacao"),"dscNotificacao")
				.add(Projections.property("dtcValidade"),"dtcValidade")
				.add(Projections.property("dtcEnvio"),"dtcEnvio")
				.add(Projections.property("numNotificacao"),"numNotificacao")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")
				.add(Projections.property("dscOutroJustificativa"),"dscOutroJustificativa")
				.add(Projections.property("qtdDiasValidade"),"qtdDiasValidade")
				
				.add(Projections.property("processo.ideProcesso"),"ideProcesso.ideProcesso")
				.add(Projections.property("processo.numProcesso"),"ideProcesso.numProcesso")
				.add(Projections.property("processo.dtcFormacao"),"ideProcesso.dtcFormacao")
				
				.add(Projections.property("requerimento.ideRequerimento"),"ideProcesso.ideRequerimento.ideRequerimento")
				.add(Projections.property("requerimento.numRequerimento"),"ideProcesso.ideRequerimento.numRequerimento")
				.add(Projections.property("requerimento.dtcCriacao"),"ideProcesso.ideRequerimento.dtcCriacao")
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(Notificacao.class))
			
			.add(Restrictions.eq("ideNotificacao", ideNotificacao));
	
		return this.notificacaoDAO.buscarPorCriteria(criteria);		
	}
	


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacoesNaoVisualizadas() {
		DateTime dt = new DateTime().minusDays(10);
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class,"notificacao");
		criteria.add(Restrictions.isNotNull("notificacao.dtcEnvio"));
		criteria.add(Restrictions.isNull("notificacao.dtcValidade"));
		criteria.add(Restrictions.lt("notificacao.dtcValidade", dt.toDate()));
		
		List<Notificacao> lista = this.notificacaoDAO.listarPorCriteria(criteria);
		if (Util.isNullOuVazio(lista)){
			return new ArrayList<Notificacao>();
		}
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void emitirNotificacoesParciais(Processo processo) {
		String sql = "update NotificacaoParcial notf set notf.indEmissao = true  where notf.ideProcesso.ideProcesso = :ideProcesso";
		Map<String,Object> parametros = new HashMap<String, Object>();
		parametros.put("ideProcesso", processo.getIdeProcesso());
		this.notificacaoDAO.executarQuery(sql, parametros);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregarMotivosNotificacao(Integer ideNotificacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class);
		criteria
			.setFetchMode("notificacaoMotivoNotificacaoCollection", FetchMode.EAGER)
			.add(Restrictions.eq("ideNotificacao", ideNotificacao))
		;
		return notificacaoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean verificarExistenciaNotificacao(Integer ideProcesso, Pauta pauta)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcesso", new Processo(ideProcesso));
		params.put("idePautaCriacao", pauta);
		StringBuilder sql = new StringBuilder();
		
		sql.append("select n ");
		sql.append("from Notificacao n ");
		sql.append("where n.numNotificacao is null ");
		sql.append("     and n.indEnviadoAprovacao = true ");
		sql.append("     and n.ideProcesso = :ideProcesso ");
		sql.append("     and n.idePautaCriacao <> :idePautaCriacao ");
		
		List<Notificacao> notificacoes = (List<Notificacao>) notificacaoDAO.listarPorQuery(sql.toString(), params);
		
		if(notificacoes.isEmpty()){
			return false;
		}		
		return true;
	
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Notificacao> filtrarlistaNotificacao(int startPage, int maxPage, Map<String, Object> params)  {
		DetachedCriteria criteria = getCriteria();
		
		if(Util.isNullOuVazio(criteria)) return null;
		
		criteria = useFiltersParams(params, criteria);
		criteria.addOrder(Order.asc("not.dtcEnvio"));
		
		Collection<Notificacao> lista = notificacaoDAO.listarPorCriteriaDemanda(criteria, startPage, maxPage);
		
		for(Notificacao not : lista) {
			Hibernate.initialize(not.getIdeProcesso());
		}
		
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countNotificacao(Map<String, Object> params)  {
		DetachedCriteria criteria = getCriteria();
		criteria = useFiltersParams(params, criteria);		
		List<Notificacao> lista = notificacaoDAO.listarPorCriteria(criteria);
		if(lista != null && !lista.isEmpty()){
			return lista.size();
		}else{
			return 0;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacaoLider(Processo ideProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class);
		criteria
			.add(Restrictions.eq("ideProcesso", ideProcesso))
			.setFetchMode("ideMotivoNotificacao", FetchMode.JOIN);
		return notificacaoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> obterNotificacaoProcesso(Processo ideProcesso, int tipo)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideProcesso", ideProcesso);
		params.put("tipo", tipo);
		
		StringBuilder sql = new StringBuilder();
		sql.append("select n ");
		sql.append("from Notificacao n ");
		sql.append("where n.ideProcesso = :ideProcesso ");
		sql.append("and n.tipo = :tipo ");
		sql.append("and n.numNotificacao is not null ");
		sql.append("order by n.numNotificacao desc");
		
		return notificacaoDAO.listarPorQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacaoEnviadaParaAprovacaoByProcesso(Processo ideProcesso) {
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideProcesso", ideProcesso);
		StringBuilder sql = new StringBuilder();		
		
		sql.append("select n ");
		sql.append("from Notificacao n ");
		sql.append("     left join fetch n.notificacaoMotivoNotificacaoCollection ");
		sql.append("     left join fetch n.ideProcesso p");
		sql.append("     left join fetch p.ideRequerimento ");
		sql.append("where n.ideProcesso=:ideProcesso ");
		sql.append("      and (n.indEnviadoAprovacao = true or n.indRejeitado = true)");
		sql.append("      and n.dtcEnvio is null ");
		
		try{
			return notificacaoDAO.listarPorQuery(sql.toString(), parametros);
		}
		catch(Exception e){
			return null;
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacaoByProcessoTipoNot(Processo ideProcesso, TipoNotificacaoEnum tipoNotificacaoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class)
			.createAlias("ideProcesso", "processo",JoinType.INNER_JOIN)
			.createAlias("notificacaoMotivoNotificacaoCollection", "motivo",JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("processo.ideProcesso",ideProcesso.getIdeProcesso()))
			.add(Restrictions.eq("tipo",tipoNotificacaoEnum.getTipo()));
		
		return notificacaoDAO.listarPorCriteria(criteria);		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacoesAprovadasByProcesso(Processo ideProcesso) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class);
		criteria.createAlias("ideProcesso", "processo",JoinType.INNER_JOIN);
		criteria.createAlias("idePautaAprovacao", "pautaAprovacao",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pautaAprovacao.idePessoaFisica", "funcionario",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("funcionario.pessoaFisica", "pessoaFisica",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pessoaFisica.pessoa", "pessoa",JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("processo.ideProcesso",ideProcesso.getIdeProcesso()));
		criteria.add(Restrictions.eq("indAprovado",true));
		criteria.addOrder(Order.asc("dtcCriacao"));		
		return notificacaoDAO.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Notificacao> filtrarlistaNotificacaoWS(int startPage, int maxPage, Map<String, Object> params)  {
		DetachedCriteria criteria = getCriteriaWS((Boolean) params.get("usuarioExterno"), (PessoaFisica) params.get("usuarioLogado"));				
		if(Util.isNullOuVazio(criteria)){
			return null;
		}		
		criteria = useFiltersParams(params, criteria);
		criteria.addOrder(Order.asc("not.dtcEnvio"));
		Collection<Notificacao> lista = notificacaoDAO.listarPorCriteriaDemanda(criteria, startPage, maxPage);			
		for(Notificacao not : lista){
			Hibernate.initialize(not.getIdeProcesso());
		}
		return lista;
	}
	
	private DetachedCriteria getCriteriaWS(boolean usuarioExterno, PessoaFisica usuarioLogado)  {
		DetachedCriteria criteria;
		if(usuarioExterno) {
			List<Integer>listaPessoas = permissaoPerfilService.listarIdesPessoasAptas(usuarioLogado);
			
			if(Util.isNullOuVazio(listaPessoas)){
				return null;
			}
			criteria = DetachedCriteria.forClass(Notificacao.class, "not");
			criteria.createAlias("ideProcesso", "processo", JoinType.INNER_JOIN);
			criteria.createAlias("processo.ideRequerimento", "requerimento");
			
			criteria.createAlias("requerimento.empreendimentoRequerimentoCollection","empReq");
			criteria.createAlias("empReq.ideEmpreendimento","empreendimentos");
			
			criteria.add(Restrictions.in("empreendimentos.idePessoa.idePessoa",listaPessoas));
			//Usuário externo só consulta notificações emitidas.
			criteria.add(Restrictions.eq("not.indCancelado", false));
			criteria.add(Restrictions.eq("not.indAprovado", true));
			criteria.add(Restrictions.eq("not.indRejeitado", false));
			
			criteria.setProjection(
					Projections.distinct(
							Projections.projectionList()
							.add(Projections.property("ideNotificacao"),"ideNotificacao")
							.add(Projections.property("numNotificacao"),"numNotificacao")
							.add(Projections.property("dscNotificacao"),"dscNotificacao")
							.add(Projections.property("dtcValidade"),"dtcValidade")
							.add(Projections.property("indAprovado"),"indAprovado")
							.add(Projections.property("dtcEnvio"),"dtcEnvio")
							.add(Projections.property("indRetorno"),"indRetorno")
							.add(Projections.property("dtcCriacao"),"dtcCriacao")
							.add(Projections.property("dtcReprovacao"),"dtcReprovacao")
							.add(Projections.property("indCancelado"),"indCancelado")
							.add(Projections.property("indRejeitado"),"indRejeitado")
							.add(Projections.property("indEnviadoAprovacao"),"indEnviadoAprovacao")
							.add(Projections.property("dscOutroJustificativa"),"dscOutroJustificativa")
							.add(Projections.property("tipo"),"tipo")
							.add(Projections.property("qtdDiasValidade"),"qtdDiasValidade")
							.add(Projections.property("ideLegislacao"),"ideLegislacao")
							.add(Projections.property("idePautaCriacao"),"idePautaCriacao")
							.add(Projections.property("processo.ideProcesso"),"ideProcesso.ideProcesso")
							.add(Projections.property("processo.dtcFormacao"),"ideProcesso.dtcFormacao")
							.add(Projections.property("processo.numProcesso"),"ideProcesso.numProcesso")							
							.add(Projections.property("requerimento.ideRequerimento"),"processo.ideRequerimento.ideRequerimento")
							)
					)
					.setResultTransformer(new AliasToNestedBeanResultTransformer(Notificacao.class));			
		}
		else {
			criteria = DetachedCriteria.forClass(Notificacao.class, "not");
			criteria.createAlias("ideProcesso", "processo", JoinType.INNER_JOIN);
		}
		
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao buscarNotificaoPorNumero(String numNotificacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class);
		criteria.createAlias("ideProcesso", "processo", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("numNotificacao",numNotificacao));
		return notificacaoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> buscarNotificacaoPorPrazo(Integer qtdDias) {
		Date dataIni = Util.calcularProximoDiaHoraInicial(qtdDias);
		Date dataFim = Util.calcularProximoDiaHoraFinal(qtdDias);
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class);
		criteria.createAlias("ideProcesso", "processo", JoinType.INNER_JOIN);
		criteria.add(Restrictions.between("dtcValidade", dataIni, dataFim));
		criteria.add(Restrictions.isNull("indRetorno"));
		return notificacaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countNotificacaoWS(Map<String, Object> params)  {
		DetachedCriteria criteria = getCriteriaWS((Boolean) params.get("usuarioExterno"), (PessoaFisica) params.get("usuarioLogado"));
		criteria = useFiltersParams(params, criteria);		
		return notificacaoDAO.count(criteria);
	}
	
	@SuppressWarnings("unchecked")
	private DetachedCriteria useFiltersParams(Map<String, Object> params, DetachedCriteria criteria) {
		
		if(params.get("numNotificacao") != null){
			String numNotificacao = (String) params.get("numNotificacao");
			criteria.add(Restrictions.ilike("not.numNotificacao", numNotificacao.trim(), MatchMode.ANYWHERE));
		}
		if(params.get("numProcesso") != null){
			String numProcesso = (String) params.get("numProcesso");
			criteria.add(Restrictions.ilike("processo.numProcesso", numProcesso.trim(), MatchMode.ANYWHERE));
		}
		if(params.get("periodoInicio") != null && params.get("periodoFim") != null){
			criteria.add(Restrictions.between("not.dtcEnvio", params.get("periodoInicio"), params.get("periodoFim")));
		}
		if(params.get("ideMotivoNotificacao") != null){
			criteria.createAlias("not.notificacaoMotivoNotificacaoCollection", "motivo");
			criteria.createAlias("motivo.ideMotivoNotificacao", "motivoNotificacao");
			criteria.add(Restrictions.eq("motivoNotificacao.ideMotivoNotificacao", params.get("ideMotivoNotificacao")));
		}
		
		if(params.get("filtroPesquisa") != null && !((List<StatusNotificacao>) params.get("filtroPesquisa")).isEmpty()){
			List<StatusNotificacao> statusNotificacao = (List<StatusNotificacao>) params.get("filtroPesquisa");
			Map<Integer,String> parametroPorStatus = new HashMap<Integer, String>();
			parametroPorStatus.put(1, "not.numNotificacao");//"Emitida"
			parametroPorStatus.put(2, "not.indRejeitado");//"Para Revisão"
			parametroPorStatus.put(3, "not.indCancelado");//"Cancelada"
			parametroPorStatus.put(4, "not.indEnviadoAprovacao");//"Aguardando Aprovação"
			if(statusNotificacao.size() == 1){
				if(statusNotificacao.get(0).getIdeStatus()==1){
						criteria.add(Restrictions.isNotNull(parametroPorStatus.get(1)));
				}
				else if(statusNotificacao.get(0).getIdeStatus()==4){
					criteria.add(Restrictions.eq(parametroPorStatus.get(4), true))
							.add(Restrictions.eq(parametroPorStatus.get(3), false)) 
							.add(Restrictions.eq(parametroPorStatus.get(2),false))
							.add(Restrictions.isNull(parametroPorStatus.get(1)));
				}
				else{
					criteria.add(Restrictions.eq(parametroPorStatus.get(statusNotificacao.get(0).getIdeStatus()), true));
				}
			}
			else{
				Disjunction ors = Restrictions.disjunction();
				for(StatusNotificacao st : statusNotificacao){
					if(st.getIdeStatus()==1){
						ors.add(Restrictions.isNotNull(parametroPorStatus.get(1)));
					}
					else if(st.getIdeStatus()==4){
						ors.add(Restrictions.conjunction().add(Restrictions.eq(parametroPorStatus.get(4), true))
														  .add(Restrictions.eq(parametroPorStatus.get(3), false))
								                          .add(Restrictions.eq(parametroPorStatus.get(2),false))
								                          .add(Restrictions.isNull(parametroPorStatus.get(1))));
					}
					else{
						ors.add(Restrictions.eq(parametroPorStatus.get(st.getIdeStatus()), true));
					}
				}
				criteria.add(ors);
			}

		}
		if(params.get("ideArea")!=null){
			criteria.createAlias("idePautaCriacao", "pautaCriacao");
			criteria.createAlias("pautaCriacao.idePessoaFisica","pessoaPauta");			
			
			criteria.add(Subqueries.propertyIn("pessoaPauta.idePessoaFisica", 
					DetachedCriteria.forClass(IntegranteEquipe.class)
						
						.createAlias("idePessoaFisica", "fun", JoinType.INNER_JOIN)
						.createAlias("ideEquipe", "eq", JoinType.INNER_JOIN)
						
						.add(Restrictions.eq("indLiderEquipe", true))
						.add(Restrictions.eq("eq.ideArea", params.get("ideArea")))
						
						.setProjection(Projections.property("fun.idePessoaFisica"))
			));
		}
		
		if(params.get("respondida") != null){
			criteria.add(Restrictions.eq("not.indRetorno", params.get("respondida")));
		}		
		if(params.get("indAprovado")!=null) {
			criteria.add(Restrictions.eq("not.indAprovado", params.get("indAprovado")));
		}
		
		return criteria;
	}
	
	private DetachedCriteria getCriteria()  {
		DetachedCriteria criteria;
		
		if(ContextoUtil.getContexto().isUsuarioExterno()) {
			List<Integer>listaPessoas = permissaoPerfilService.listarIdesPessoas();
			
			if(Util.isNullOuVazio(listaPessoas)) return null;
			
			criteria = DetachedCriteria.forClass(Notificacao.class, "not");
			criteria.createAlias("ideProcesso", "processo", JoinType.INNER_JOIN);
			criteria.createAlias("processo.ideRequerimento", "requerimento");
			
			criteria.createAlias("requerimento.requerimentoPessoaCollection","reqPes");
			criteria.createAlias("reqPes.pessoa","pes");
			criteria.createAlias("reqPes.ideTipoPessoaRequerimento", "tpr", JoinType.LEFT_OUTER_JOIN);
			
			criteria.add(Restrictions.in("pes.idePessoa",listaPessoas));
			
			//Usuário externo só consulta notificações emitidas.
			criteria.add(Restrictions.eq("not.indCancelado", false));
			criteria.add(Restrictions.eq("not.indAprovado", true));
			criteria.add(Restrictions.eq("not.indRejeitado", false));
			
			criteria.add(Restrictions.eq("tpr.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
			
			criteria.setProjection(
				Projections.distinct(
					Projections.projectionList()
						.add(Projections.property("ideNotificacao"),"ideNotificacao")
						.add(Projections.property("numNotificacao"),"numNotificacao")
						.add(Projections.property("dscNotificacao"),"dscNotificacao")
						.add(Projections.property("dtcValidade"),"dtcValidade")
						.add(Projections.property("indAprovado"),"indAprovado")
						.add(Projections.property("dtcEnvio"),"dtcEnvio")
						.add(Projections.property("indRetorno"),"indRetorno")
						.add(Projections.property("dtcCriacao"),"dtcCriacao")
						.add(Projections.property("dtcReprovacao"),"dtcReprovacao")
						.add(Projections.property("indCancelado"),"indCancelado")
						.add(Projections.property("indRejeitado"),"indRejeitado")
						.add(Projections.property("indEnviadoAprovacao"),"indEnviadoAprovacao")
						.add(Projections.property("dscOutroJustificativa"),"dscOutroJustificativa")
						.add(Projections.property("tipo"),"tipo")
						.add(Projections.property("qtdDiasValidade"),"qtdDiasValidade")
						.add(Projections.property("ideLegislacao"),"ideLegislacao")
						.add(Projections.property("idePautaCriacao"),"idePautaCriacao")
						.add(Projections.property("idePautaAprovacao"),"idePautaAprovacao")
						.add(Projections.property("processo.ideProcesso"),"ideProcesso.ideProcesso")
						.add(Projections.property("processo.numProcesso"),"ideProcesso.numProcesso")							
						.add(Projections.property("requerimento.ideRequerimento"),"processo.ideRequerimento.ideRequerimento")
					)
				).setResultTransformer(new AliasToNestedBeanResultTransformer(Notificacao.class));	
		} else {
			criteria = DetachedCriteria.forClass(Notificacao.class, "not");
			criteria.createAlias("ideProcesso", "processo", JoinType.INNER_JOIN);
		}
		
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao obterNotificacaoParaAProvacao(Processo ideProcesso, StatusFluxo statusFluxo) {
		
		List<Notificacao> listaNotificacoes = null;
		
		Notificacao notificacao = null;
		
		if(statusFluxo.getIdeStatusFluxo().equals(StatusFluxoEnum.NOTIFICACAO_ENVIADA_PARA_REVISAO.getStatus())){
			listaNotificacoes = listarNotificacaoRejeitadas(ideProcesso);
		}
		else {
			listaNotificacoes = listarNotificacaoEnviadaParaAprovacaoByProcesso(ideProcesso);
		}
		
		for(Notificacao not: listaNotificacoes) {
			
			if(not.getIndEnviadoAprovacao() && isNotificacaoAguardandoAprovacao(not)){
				notificacao = not;
				break;
			}else if(isNotificacaoRejeitada(not)){
				notificacao = not;
			}
		}
		
		if(!Util.isNullOuVazio(notificacao)){
			notificacao = carregarNotificacao(notificacao);
			return notificacao;
		}
		
		return new Notificacao();
	}
	
	private boolean isNotificacaoAguardandoAprovacao(Notificacao notificacao) {
		return (Util.assertEquals(notificacao.getIndCancelado(), false) 
				&& Util.assertEquals(notificacao.getIndAprovado(), false)
				&& Util.assertEquals(notificacao.getIndRejeitado(), false));		
	}

	private boolean isNotificacaoRejeitada(Notificacao notificacao) {
		return (Util.assertEquals(notificacao.getIndCancelado(), false) 
				&& Util.assertEquals(notificacao.getIndAprovado(), false)   
				&& Util.assertEquals(notificacao.getIndRejeitado(), true) );
	}

	private boolean isNotificacaoCancelada(Notificacao notificacao) {		
		return (Util.assertEquals(notificacao.getIndCancelado(), true) 
				&& Util.assertEquals(notificacao.getIndAprovado(), false)   
				&& Util.assertEquals(notificacao.getIndRejeitado(), false) );
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao obterNotificacaoFinal(NotificacaoFinalDTO dto) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class)
			.createAlias("ideProcesso", "p",JoinType.INNER_JOIN)
			.createAlias("notificacaoMotivoNotificacaoCollection", "motivo",JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.isNull("numNotificacao"))
			.add(Restrictions.isNull("indRetorno"))
			.add(Restrictions.ne("indCancelado",true))
			.add(Restrictions.eq("tipo",dto.getTipoNotificacaoEnum().getTipo()))
			.add(Restrictions.eq("p.ideProcesso",dto.getVwProcesso().getIdeProcesso()));
		
		Notificacao notificacao = notificacaoDAO.buscarPorCriteria(criteria);
		
		if(Util.isNull(notificacao) || isNotificacaoCancelada(notificacao)) {
			notificacao = new Notificacao();
			notificacao.setTipo(dto.getTipoNotificacaoEnum().getTipo());
		}
		
		return notificacao; 
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacaoRespondidasBy(Integer ideProcesso, Integer ideImovel, MotivoNotificacaoEnum motivoNotificacaoEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class)
				.createAlias("ideProcesso", "p")
				.createAlias("arquivoProcessoCollection", "arq")
				.createAlias("arq.ideImovel", "i",JoinType.LEFT_OUTER_JOIN,Restrictions.sqlRestriction("i3_.ide_imovel="+ideImovel))
				.createAlias("notificacaoMotivoNotificacaoCollection", "notmotnot",JoinType.INNER_JOIN,Restrictions.sqlRestriction("notmotnot4_.ide_motivo_notificacao=arq2_.ide_motivo_notificacao"))
				.createAlias("notmotnot.ideMotivoNotificacao", "motivo")
				// Processo
				.add(Restrictions.eq("p.ideProcesso", ideProcesso))
				// Notificacao - Respondida
				.add(Restrictions.eq("indRetorno", true))
				.add(Restrictions.isNotNull("dtcResposta"))
				// Motivo
				.add(Restrictions.eq("motivo.ideMotivoNotificacao", motivoNotificacaoEnum.getId()))
				;
		return notificacaoDAO.listarPorCriteria(criteria); 
	}

	public List<Notificacao> listarPorProcesso(Processo proc) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(Notificacao.class, "not")
				.createAlias("not.ideProcesso", "processo", JoinType.INNER_JOIN)
				.createAlias("not.notificacaoMotivoNotificacaoCollection", "motivo", JoinType.LEFT_OUTER_JOIN)
				
				.add(Restrictions.eq("processo.ideProcesso", proc.getIdeProcesso()));
			
			return notificacaoDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Notificacao> listarNotificacaoRejeitadas(Processo ideProcesso) {
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideProcesso", ideProcesso);
		StringBuilder sql = new StringBuilder();		
		
		sql.append("select n ");
		sql.append("from Notificacao n ");
		sql.append("where n.ideProcesso=:ideProcesso ");
		sql.append("      and (n.indEnviadoAprovacao = false or n.indRejeitado = true)");
		sql.append("      and n.dtcEnvio is null ");
		
		try{
			return notificacaoDAO.listarPorQuery(sql.toString(), parametros);
		}
		catch(Exception e){
			return null;
		}
	}
}