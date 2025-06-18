package br.gov.ba.seia.dao;

import java.util.Arrays;
import java.util.Collection;
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

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Comunicacao;
import br.gov.ba.seia.entity.ComunicacaoPerfil;
import br.gov.ba.seia.entity.ComunicacaoPerfilPK;
import br.gov.ba.seia.enumerator.ComunicacaoStatusEnum;
import br.gov.ba.seia.util.DataUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoDAOImpl {

	@Inject
	private IDAO<Comunicacao> comunicacaoDAO;
	@Inject
	private IDAO<ComunicacaoPerfil> comunicacaoPerfilDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarComunicacao(Comunicacao comunicacao) {
		if (comunicacao.getIdeComunicacao() != null && !comunicacao.getComunicacaoPerfilCollection().isEmpty()) {
			removerComunicacaoPerfil(comunicacao);
		}
		comunicacaoDAO.salvarOuAtualizar(comunicacao);

		for (ComunicacaoPerfil cp : comunicacao.getComunicacaoPerfilCollection()) {
			cp.setComunicacaoPerfilPK(
					new ComunicacaoPerfilPK(comunicacao.getIdeComunicacao(), cp.getIdePerfil().getIdePerfil()));
			comunicacaoPerfilDAO.salvar(cp);
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarComunicacao(Comunicacao comunicacao) {
		comunicacaoDAO.atualizar(comunicacao);
	}

	public void removerComunicacaoPerfil(Comunicacao comunicacao) {
		String deleteSQL = "DELETE FROM comunicacao_perfil WHERE ide_comunicacao = :ideComunicacao";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideComunicacao", comunicacao.getIdeComunicacao());
		comunicacaoPerfilDAO.executarNativeQuery(deleteSQL, params);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirComunicacao(Comunicacao comunicacao) {
		for (ComunicacaoPerfil cp : comunicacao.getComunicacaoPerfilCollection()) {
			comunicacaoPerfilDAO.remover(cp);
		}
		comunicacaoDAO.remover(comunicacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Comunicacao find(Integer id) {

		Comunicacao comunicacao = comunicacaoDAO.carregarGet(id);
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideComunicacao", id);
		comunicacao.setComunicacaoPerfilCollection(
				comunicacaoPerfilDAO.buscarPorNamedQuery("ComunicacaoPerfil.findByComunicacao", parametros));
		return comunicacao;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Comunicacao> localizarComunicacaoTemporaria(Integer ideComunicacao, List<Integer> idePerfilL,
			List<Integer> ideStatusL, boolean indAtiva, String tpComunicacao, String titulo, Date dtInicio,
			Date dtFim) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Comunicacao.class)
				.createAlias("idePessoaFisica", "usuario", JoinType.INNER_JOIN)
				.createAlias("comunicacaoPerfilCollection", "cp", JoinType.LEFT_OUTER_JOIN);
		;

		if (!Util.isNullOuVazio(idePerfilL) || !idePerfilL.isEmpty()) {
			criteria.add(Restrictions.in("cp.comunicacaoPerfilPK.idePerfil", idePerfilL));

		}
		if (!Util.isNullOuVazio(indAtiva)) {
			criteria.add(Restrictions.eq("indAtiva", indAtiva));

		}
		if (!Util.isNullOuVazio(ideComunicacao)) {
			criteria.add(Restrictions.not(Restrictions.eq("ideComunicacao", ideComunicacao)));

		}

		if (!Util.isNullOuVazio(tpComunicacao) && !tpComunicacao.isEmpty()) {
			criteria.add(Restrictions.eq("tpComunicacao", tpComunicacao));

		}
		if (!Util.isNullOuVazio(ideStatusL) || !ideStatusL.isEmpty()) {
			criteria.add(Restrictions.in("ideComunicacaoStatus", ideStatusL));
		}
		if (!Util.isNullOuVazio(titulo)) {
			criteria.add(Restrictions.ilike("dscTitulo", titulo, MatchMode.ANYWHERE));
		}
		if (!Util.isNullOuVazio(dtInicio) && Util.isNullOuVazio(dtFim)) {
			criteria.add(Restrictions.le("dtcPeriodoInicio", dtInicio));
		} else if (!Util.isNullOuVazio(dtFim) && !Util.isNullOuVazio(dtInicio)) {
			criteria.add(Restrictions.or(Restrictions.between("dtcPeriodoInicio", dtInicio, dtFim),
			Restrictions.between("dtcPeriodoFim", dtInicio, dtFim)));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("dtcPeriodoInicio"));

		return comunicacaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Comunicacao> findByFilterLast(Integer idePerfil, Integer ideStatus, boolean indAtiva,
			String tpComunicacao) {

			DetachedCriteria criteria = DetachedCriteria.forClass(Comunicacao.class)
					.createAlias("idePessoaFisica", "usuario", JoinType.INNER_JOIN)
					.createAlias("comunicacaoPerfilCollection", "cp", JoinType.LEFT_OUTER_JOIN);
			
		
			if (!Util.isNullOuVazio(idePerfil)) {
				criteria.add(Restrictions.eq("cp.comunicacaoPerfilPK.idePerfil", idePerfil));

			}
			if (!Util.isNullOuVazio(indAtiva)) {
				criteria.add(Restrictions.eq("indAtiva", indAtiva));

			}
			if (!Util.isNullOuVazio(tpComunicacao)) {
				criteria.add(Restrictions.eq("tpComunicacao", tpComunicacao));

			}
			if (!Util.isNullOuVazio(ideStatus)) {
				criteria.add(Restrictions.eq("ideComunicacaoStatus", ideStatus));
			}

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			criteria.addOrder(Order.desc("dtcPeriodoInicio"));
			return comunicacaoDAO.listarPorCriteriaDemanda(criteria, 0, 5);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Comunicacao> findByFiltroPaginator(Integer idePerfil, Integer ideStatus, Boolean indAtiva,
			String tpComunicacao, String titulo, Date dtInicio, Date dtFim, Integer first, Integer pageSize) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Comunicacao.class)
				.createAlias("idePessoaFisica", "usuario", JoinType.INNER_JOIN)
				.createAlias("comunicacaoPerfilCollection", "cp", JoinType.LEFT_OUTER_JOIN);

		addCriteria(criteria, null, idePerfil, indAtiva, tpComunicacao, ideStatus, titulo, dtInicio, dtFim);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("dtcPeriodoInicio"));
		return comunicacaoDAO.listarPorCriteriaDemanda(criteria, first, pageSize);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Comunicacao> buscarComunicacoesParaAtualizacaoDiaria() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Comunicacao.class);
				

		criteria.add(Restrictions.eq("indAtiva", true));
		criteria.add(Restrictions
				.not(Restrictions.in("ideComunicacaoStatus",
						Arrays.asList(ComunicacaoStatusEnum.CANCELADO.getIdComunicacaoStatus(),
								ComunicacaoStatusEnum.ARQUIVADO.getIdComunicacaoStatus()))));
		criteria.add(Restrictions.or(Restrictions.eq("dtcPeriodoInicio", DataUtil.getDataAtual()),
				Restrictions.eq("dtcPeriodoFim", DataUtil.getDataOntem())));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return comunicacaoDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Comunicacao> findAll() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Comunicacao.class)
				.createAlias("comunicacaoPerfilCollection", "cp", JoinType.LEFT_OUTER_JOIN);
		return comunicacaoDAO.listarPorCriteria(criteria);

	}

	public Comunicacao findComunicacaoTemporaria(Integer idePerfil) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Comunicacao.class)
				.createAlias("comunicacaoPerfilCollection", "cp", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideComunicacaoStatus", ComunicacaoStatusEnum.ENVIADO.getIdComunicacaoStatus()))

				.add(Restrictions.eq("tpComunicacao", "T")).add(Restrictions.eq("indAtiva", true))
				.add(Restrictions.eq("cp.comunicacaoPerfilPK.idePerfil", idePerfil))
				.add(Restrictions.le("dtcPeriodoInicio", DataUtil.getDataAtual()))
				.add(Restrictions.ge("dtcPeriodoFim", DataUtil.getDataAtual()))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return comunicacaoDAO.buscarPorCriteriaMaxResult(criteria);

	}

	public List<Comunicacao> findAllByFiltro(Integer idePerfil, String titulo, Date dtInicio) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Comunicacao.class)
				.createAlias("idePessoaFisica", "usuario", JoinType.INNER_JOIN)
				.createAlias("comunicacaoPerfilCollection", "cp", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("tpComunicacao", "N"));

		if (!Util.isNullOuVazio(idePerfil)) {
			criteria.add(Restrictions.eq("cp.comunicacaoPerfilPK.idePerfil", idePerfil));

		}

		criteria.add(Restrictions.eq("indAtiva", true));

		criteria.add(Restrictions.eq("ideComunicacaoStatus", ComunicacaoStatusEnum.ENVIADO.getIdComunicacaoStatus()));

		if (!Util.isNullOuVazio(titulo)) {
			criteria.add(Restrictions.ilike("dscTitulo", titulo, MatchMode.ANYWHERE));
		}
		if (!Util.isNullOuVazio(dtInicio)) {
			criteria.add(Restrictions.eq("dtcPeriodoInicio", dtInicio));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("dtcPeriodoInicio"));
		return comunicacaoDAO.listarPorCriteria(criteria);
	}

	public void addCriteria(DetachedCriteria criteria, Integer ideComunicacao, Integer idePerfil, Boolean indAtiva,
			String tpComunicacao, Integer ideStatus, String titulo, Date dtInicio, Date dtFim) {

		if (!Util.isNullOuVazio(ideComunicacao)) {
			criteria.add(Restrictions.eq("ideComunicacao", ideComunicacao));

		}
		if (!Util.isNullOuVazio(idePerfil)) {
			criteria.add(Restrictions.eq("cp.comunicacaoPerfilPK.idePerfil", idePerfil));

		}
		if (!Util.isNullOuVazio(indAtiva)) {
			criteria.add(Restrictions.eq("indAtiva", indAtiva));

		}
		if (!Util.isNullOuVazio(tpComunicacao)) {
			criteria.add(Restrictions.eq("tpComunicacao", tpComunicacao));

		}
		if (!Util.isNullOuVazio(ideStatus)) {
			criteria.add(Restrictions.eq("ideComunicacaoStatus", ideStatus));
		}
		if (!Util.isNullOuVazio(titulo)) {
			criteria.add(Restrictions.ilike("dscTitulo", titulo, MatchMode.ANYWHERE));
		}
		if (!Util.isNullOuVazio(dtInicio) && Util.isNullOuVazio(dtFim)) {
			criteria.add(Restrictions.eq("dtcPeriodoInicio", dtInicio));
		} else if (!Util.isNullOuVazio(dtFim) && Util.isNullOuVazio(dtInicio)) {
			criteria.add(Restrictions.eq("dtcPeriodoFim", dtFim));
		} else if (!Util.isNullOuVazio(dtFim) && !Util.isNullOuVazio(dtInicio)) {
			criteria.add(Restrictions.between("dtcPeriodoInicio", dtInicio, dtFim));
			criteria.add(Restrictions.between("dtcPeriodoFim", dtInicio, dtFim));
		}
	}

	public Integer count(String titulo, Integer idePerfil, Integer ideStatus, Boolean indAtiva, String tpComunicacao,
			Date dtPeriodoInicial, Date dtPeriodoFinal) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Comunicacao.class)
				.createAlias("idePessoaFisica", "usuario", JoinType.INNER_JOIN)
				.createAlias("comunicacaoPerfilCollection", "cp", JoinType.LEFT_OUTER_JOIN);
		addCriteria(criteria, null, idePerfil, indAtiva, tpComunicacao, ideStatus, titulo, dtPeriodoInicial,
				dtPeriodoFinal);
		return comunicacaoDAO.count(criteria);
	}

}
