package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade;
import br.gov.ba.seia.entity.TipoProrrogacaoPrazoValidade;
import br.gov.ba.seia.entity.TipoRevisaoCondicionante;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SolicitacaoAdministrativoService {

	@Inject
	private IDAO<SolicitacaoAdministrativo> solicitacaoAdministrativolDAO;
	@Inject
	private IDAO<TipoProrrogacaoPrazoValidade> tipoProrrogacaoValidadelDAO;
	@Inject
	private IDAO<SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade> solicitacaoAdmTipoValidadelDAO;
	@Inject
	private IDAO<TipoRevisaoCondicionante> tipoRevisaoCondicionanteIDAO;

	@Inject
	private IDAO<Processo> processoDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarSolicitacaoAdministrativa(SolicitacaoAdministrativo solAdm)  {
		solicitacaoAdministrativolDAO.salvarOuAtualizar(solAdm);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SolicitacaoAdministrativo> listarCondicionante()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class);
		criteria.add(Restrictions.isNotNull("ideTipoRevisaoCondicionante"));
		return solicitacaoAdministrativolDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SolicitacaoAdministrativo> listaPrazoValidade()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class);
		criteria.add(Restrictions.isNotNull("ideTipoProrrogacaoPrazoValidade"));
		return solicitacaoAdministrativolDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSolicitacaoAdministrativa(SolicitacaoAdministrativo sAdm)  {
		Map<String, Object> params = new HashMap<String, Object>();
		if (Util.isNullOuVazio(sAdm.getNumCondicionante())) {
			excluirLocalizacaoDadoGeograficoByIdSolicitacao(sAdm);
		}
		params.put("ideSolicitacaoAdministrativo", sAdm.getIdeSolicitacaoAdministrativo());
		solicitacaoAdministrativolDAO.executarNamedQuery(
				"SolicitacaoAdministrativo.deleteByIdeSolicitacaoAdministrativo", params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirLocalizacaoDadoGeograficoByIdSolicitacao(SolicitacaoAdministrativo solicitacaoAdministrativa)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideSolicitacaoAdministrativo", solicitacaoAdministrativa);
		this.solicitacaoAdmTipoValidadelDAO.executarNamedQuery("SolicitacaoAdmTipoPPValidade.removeByideSolicitacaoAdministrativo", params);
	}

	public List<SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade> buscarSolicitacaoAdmTipoValByIdSolicitacao(
			SolicitacaoAdministrativo solAdm)  {
		List<SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade> sATPV = new ArrayList<SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade>();
		DetachedCriteria criteria = DetachedCriteria
				.forClass(SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade.class);
		criteria.add(Restrictions.eq("ideSolicitacaoAdministrativo.ideSolicitacaoAdministrativo",
				solAdm.getIdeSolicitacaoAdministrativo()));
		criteria.createAlias("ideSolicitacaoAdministrativo", "sa");
		criteria.createAlias("ideTipoProrrogacaoPrazoValidade", "tp");
		criteria.createAlias("ideLocalizacaoGeografica", "lg");
		criteria.createAlias("lg.dadoGeograficoCollection", "dgc");
		sATPV = solicitacaoAdmTipoValidadelDAO.listarPorCriteria(criteria);
		for (SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade solTipo : sATPV) {
			if (solTipo != null) {
				solTipo.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			}
		}
		return sATPV;

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TipoProrrogacaoPrazoValidade[] buscarTipoProrrogacaoByIdSolicitacao(SolicitacaoAdministrativo ideSolicitacao)
			 {
		List<SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade> sATPV = new ArrayList<SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade>();
		TipoProrrogacaoPrazoValidade[] listaProrrogacaoVal = new TipoProrrogacaoPrazoValidade[4];

		DetachedCriteria criteria = DetachedCriteria
				.forClass(SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade.class);
		criteria.add(Restrictions.eq("ideSolicitacaoAdministrativo.ideSolicitacaoAdministrativo",
				ideSolicitacao.getIdeSolicitacaoAdministrativo()));
		criteria.setFetchMode("ideSolicitacaoAdministrativo", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoProrrogacaoPrazoValidade", FetchMode.JOIN);
		criteria.setFetchMode("ideLocalizacaoGeografica", FetchMode.JOIN);
		sATPV = solicitacaoAdmTipoValidadelDAO.listarPorCriteria(criteria);

		for (int x = 0; x < sATPV.size(); x++) {
			listaProrrogacaoVal[x] = sATPV.get(x).getIdeTipoProrrogacaoPrazoValidade();
		}
		return listaProrrogacaoVal;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoProrrogacaoPrazoValidade> listarTipoProrrogacaoValidade()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoProrrogacaoPrazoValidade.class);
		criteria.add(Restrictions.isNull("ideTipoProrrogacaoPrazoValidadePai"));
		return tipoProrrogacaoValidadelDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoProrrogacaoPrazoValidade> listaTipoProrrogacaoValidadeFilho()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoProrrogacaoPrazoValidade.class);
		criteria.add(Restrictions.isNotNull("ideTipoProrrogacaoPrazoValidadePai"));
		return tipoProrrogacaoValidadelDAO.listarPorCriteria(criteria, Order.asc("ideTipoProrrogacaoPrazoValidade"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SolicitacaoAdministrativo> getListaProrrogacaoPValidadeByIdeRequerimento(Requerimento req)
			 {
		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class);
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", req.getIdeRequerimento()));
		criteria.add(Restrictions.isNotNull("ideTipoProrrogacaoPrazoValidade"));
		criteria.setFetchMode("ideTipoProrrogacaoPrazoValidade", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoRevisaoCondicionante", FetchMode.SELECT);
		return solicitacaoAdministrativolDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SolicitacaoAdministrativo> getListaCondicionanteByIdeRequerimento(Requerimento req)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class);
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", req.getIdeRequerimento()));
		criteria.setFetchMode("ideTipoProrrogacaoPrazoValidade", FetchMode.SELECT);
		criteria.setFetchMode("ideTipoRevisaoCondicionante", FetchMode.JOIN);
		return solicitacaoAdministrativolDAO.listarPorCriteria(criteria);
	}

	public List<SolicitacaoAdministrativo> getListaTransferenciaTitularidadeByIdeRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class);

		criteria.createAlias("idePessoaDetentorLicenca", "pdl", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pdl.pessoaFisica", "pfdl", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pdl.pessoaJuridica", "pjdl", JoinType.LEFT_OUTER_JOIN);

		criteria.createAlias("idePessoaNovoTitular", "pnt", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pnt.pessoaFisica", "pfnt", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pnt.pessoaJuridica", "pjnt", JoinType.LEFT_OUTER_JOIN);

		criteria.createAlias("ideEmpreendimento", "emp", JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		criteria.add(Restrictions.isNotNull("indDetentorLicenca"));

		return solicitacaoAdministrativolDAO.listarPorCriteria(criteria);
	}

	public SolicitacaoAdministrativo carregarSolicitacaoTransferenciaTitularidadeById(Integer idTransferencia)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class);

		criteria.createAlias("idePessoaDetentorLicenca", "pdl", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pdl.pessoaFisica", "pfdl", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pdl.pessoaJuridica", "pjdl", JoinType.LEFT_OUTER_JOIN);

		criteria.createAlias("idePessoaNovoTitular", "pnt", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pnt.pessoaFisica", "pfnt", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pnt.pessoaJuridica", "pjnt", JoinType.LEFT_OUTER_JOIN);

		criteria.createAlias("ideEmpreendimento", "emp", JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("ideSolicitacaoAdministrativo", idTransferencia));
		criteria.add(Restrictions.isNotNull("indDetentorLicenca"));

		return solicitacaoAdministrativolDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarSolicitacaoAdmTipoValidadeLocalizacaoGeo(
			SolicitacaoAdministrativoTipoProrrogacaoPrazoValidade soliTipo)  {
		solicitacaoAdmTipoValidadelDAO.salvarOuAtualizar(soliTipo);
	}

	/**
	 * @author eduardo.fernandes Lista para chamar os tipo de revisão
	 *         condicionante armazenados no banco.
	 * @return List
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoRevisaoCondicionante> obterListaTipoRevisaoCondicionante() {
		return tipoRevisaoCondicionanteIDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarTPP(TipoProrrogacaoPrazoValidade tipoProrrogacaoPrazoValidade)  {
		tipoProrrogacaoValidadelDAO.remover(tipoProrrogacaoPrazoValidade);
	}

	/**
	 * @author eduardo.fernandes Busca SolicitaçãoAdministrativa salva no banco
	 * @return SolicitacaoAdministrativo
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SolicitacaoAdministrativo obterSolicitacaoAdministrativa(Requerimento requerimento,TipoSolicitacaoEnum tipoSolicitacaoEnum)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class)
				.createAlias("ideRequerimento", "ideRequerimento")
				.createAlias("ideTipoSolicitacao", "ideTipoSolicitacao")
				.createAlias("ideTipoProrrogacaoPrazoValidade", "ideTipoProrrogacaoPrazoValidade",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoRevisaoCondicionante", "ideTipoRevisaoCondicionante",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaDetentorLicenca", "idePessoaDetentorLicenca",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaNovoTitular", "idePessoaNovoTitular",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideEmpreendimento", "ideEmpreendimento",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideSistema", "ideSistema",JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		criteria.add(Restrictions.eq("ideTipoSolicitacao.ideTipoSolicitacao", tipoSolicitacaoEnum.getId()));

		return solicitacaoAdministrativolDAO.buscarPorCriteria(criteria);
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean existeSolicitacaoAdminstrativaByRequerimento(Requerimento requerimento,TipoSolicitacaoEnum tipoSolicitacaoEnum)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class)
				.createAlias("ideRequerimento", "ideRequerimento")
				.createAlias("ideTipoSolicitacao", "ideTipoSolicitacao")
				.createAlias("ideTipoProrrogacaoPrazoValidade", "ideTipoProrrogacaoPrazoValidade",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoRevisaoCondicionante", "ideTipoRevisaoCondicionante",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaDetentorLicenca", "idePessoaDetentorLicenca",JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaNovoTitular", "idePessoaNovoTitular",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideEmpreendimento", "ideEmpreendimento",JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		criteria.add(Restrictions.eq("ideTipoSolicitacao.ideTipoSolicitacao", tipoSolicitacaoEnum.getId()));

		SolicitacaoAdministrativo s = solicitacaoAdministrativolDAO.buscarPorCriteria(criteria);

		if(Util.isNullOuVazio(s)){
			return false;
		}
		else{
			return true;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SolicitacaoAdministrativo buscarARLS(RequerimentoPessoa requerimentoPessoa)  {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class)
				.createAlias("ideTipoSolicitacao", "ideTipoSolicitacao")
				.createAlias("ideRequerimento", "ideRequerimento")
				.createAlias("ideRequerimento.requerimentoPessoaCollection", "requerimentoPessoaCollection")
				.createAlias("ideRequerimento.tramitacaoRequerimentoCollection", "tramitacaoRequerimentoCollection")
				.createAlias("tramitacaoRequerimentoCollection.ideStatusRequerimento", "ideStatusRequerimento")
				.createAlias("requerimentoPessoaCollection.ideTipoPessoaRequerimento", "ideTipoPessoaRequerimento"
						,JoinType.INNER_JOIN,Restrictions.eq("ideTipoPessoaRequerimento.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
						.createAlias("requerimentoPessoaCollection.pessoa", "pessoa")

						.setProjection(Projections.projectionList().add(Property.forName("ideSolicitacaoAdministrativo"),"ideSolicitacaoAdministrativo"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(SolicitacaoAdministrativo.class))

						.add(Restrictions.isNotNull("nomRazaoSocialNova"))
						.add(Restrictions.eq("ideTipoSolicitacao.ideTipoSolicitacao",TipoSolicitacaoEnum.ALTERACAO_RAZAO_SOCIAL.getId()))
						.add(Restrictions.eq("pessoa.idePessoa",requerimentoPessoa.getPessoa().getIdePessoa()));


		DetachedCriteria subCriteria = DetachedCriteria.forClass(TramitacaoRequerimento.class,"ultimo")
				.createAlias("ultimo.ideStatusRequerimento", "status")
				.createAlias("ultimo.ideRequerimento", "req");

		subCriteria.setProjection(Projections.projectionList()
				.add(Projections.max("ultimo.dtcMovimentacao")))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(TramitacaoRequerimento.class));

		detachedCriteria.add(Subqueries.propertyEq("tramitacaoRequerimentoCollection.dtcMovimentacao", subCriteria));
		detachedCriteria.add(Restrictions.not(Restrictions.in("ideStatusRequerimento.ideStatusRequerimento", new Integer[]{ StatusRequerimentoEnum.CANCELADO.getStatus(), StatusRequerimentoEnum.FORMADO.getStatus()})));

		return solicitacaoAdministrativolDAO.buscarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SolicitacaoAdministrativo buscarARLS(Pessoa p, Integer ideRequerimento)  {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class)
				.createAlias("ideTipoSolicitacao", "ideTipoSolicitacao")
				.createAlias("ideRequerimento", "ideRequerimento")
				.createAlias("ideRequerimento.requerimentoPessoaCollection", "requerimentoPessoaCollection")
				.createAlias("requerimentoPessoaCollection.ideTipoPessoaRequerimento", "ideTipoPessoaRequerimento"
						,JoinType.INNER_JOIN,Restrictions.eq("ideTipoPessoaRequerimento.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
						.createAlias("requerimentoPessoaCollection.pessoa", "pessoa")

						.setProjection(Projections.projectionList().add(Property.forName("ideSolicitacaoAdministrativo"),"ideSolicitacaoAdministrativo")
								.add(Property.forName("nomRazaoSocialNova"),"nomRazaoSocialNova"))
								.setResultTransformer(new AliasToNestedBeanResultTransformer(SolicitacaoAdministrativo.class))

								.add(Restrictions.isNotNull("nomRazaoSocialNova"))
								.add(Restrictions.eq("ideTipoSolicitacao.ideTipoSolicitacao",TipoSolicitacaoEnum.ALTERACAO_RAZAO_SOCIAL.getId()))
								.add(Restrictions.eq("ideRequerimento.ideRequerimento", ideRequerimento))
								.add(Restrictions.eq("pessoa.idePessoa", p.getIdePessoa()));

		return solicitacaoAdministrativolDAO.buscarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo getProcessoPai(Processo p2) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Processo.class)
				.createAlias("ideRequerimento", "ideRequerimento")
				.createAlias("ideRequerimento.solicitacaoAdministrativoCollection","solicitacaoAdministrativo");

		if(p2.getNumProcesso()!=null){
			detachedCriteria.add(Restrictions.eq("numProcesso",p2.getNumProcesso()));
		}else{
			detachedCriteria.add(Restrictions.eq("ideProcesso",p2.getIdeProcesso()));
		}

		detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("solicitacaoAdministrativo.numProcesso"),"numProcesso"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Processo.class));

		return processoDAO.buscarPorCriteria(detachedCriteria);
	}

}
