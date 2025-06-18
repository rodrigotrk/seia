package br.gov.ba.seia.service;

import java.util.ArrayList;
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

import org.apache.log4j.Level;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ModalidadeOutorga;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaTipoCaptacao;
import br.gov.ba.seia.entity.OutorgaTipoReceptor;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoAlteracao;
import br.gov.ba.seia.entity.TipoBarragem;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoIntervencao;
import br.gov.ba.seia.entity.TipoReceptor;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.entity.TipoTravessia;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OutorgaService {

	@Inject
	private IDAO<ModalidadeOutorga> modalidadeOutorgaIDAO;
	@Inject
	private IDAO<Outorga> outorgaIDAO;
	@Inject
	private IDAO<TipoIntervencao> tipoIntervencaoDAO;
	@Inject
	private IDAO<TipoBarragem> tipoBarragemDAO;
	@Inject
	private IDAO<TipoTravessia> tipoTravessiaDAO;
	@Inject
	private IDAO<TipoReceptor> tipoReceptorDAO;
	@Inject
	private IDAO<TipoAlteracao> tipoAlteracaoDAO;
	@Inject
	private IDAO<OutorgaLocalizacaoGeografica> outorgaLocalizacaoGeograficaDAO;
	@Inject
	private IDAO<DadoGeografico> dadoGeograficolDAO;
	@Inject
	private IDAO<LocalizacaoGeografica> localizacaoGeolDAO;
	@Inject
	private IDAO<OutorgaTipoCaptacao> outorgaTipoCaptacaolDAO;
	@Inject
	private IDAO<TipoCaptacao> tipoCaptacaoIDAO;
	@Inject
	private IDAO<OutorgaTipoReceptor> outorgaTipoReceptorIDAO;

	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	@EJB
	private OutorgaLocalizacaoGeograficaFinalidadeService outorgaLocalizacaoGeograficaFinalidadeService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ModalidadeOutorga getModalidadeOutorgaByIde(int ideModalidadeOutorgaSelecionada)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ModalidadeOutorga.class);
		criteria.add(Restrictions.eq("ideModalidadeOutorga", ideModalidadeOutorgaSelecionada));
		return modalidadeOutorgaIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ModalidadeOutorga> listarModalidadeOutorga()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ModalidadeOutorga.class);
		return modalidadeOutorgaIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ModalidadeOutorga> listarModalidadeOutorgaSemPerfuracaoPoco()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ModalidadeOutorga.class);
		criteria.add(Restrictions.not(Restrictions.eq("ideModalidadeOutorga", ModalidadeOutorgaEnum.POCO.getIdModalidade())));
		return modalidadeOutorgaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoCaptacao> listarTipoCaptacao()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoCaptacao.class);
		criteria.add(Restrictions.between("ideTipoCaptacao", 3, 4));
		return tipoCaptacaoIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Outorga> getOutorgaByIdeRequerimentoByNaoNovaOutorga(Requerimento requerimento)  {
		DetachedCriteria criteria = getCriteriaOutorgaByRequerimento(requerimento);
		criteria.add(Restrictions.ne("ts.ideTipoSolicitacao", TipoSolicitacaoEnum.NOVA_OUTORGA.getId()));
		return outorgaIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Outorga> getOutorgaByIdeRequerimento(Requerimento requerimento)  {
		DetachedCriteria criteria = getCriteriaOutorgaByRequerimento(requerimento);
		return outorgaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Outorga> getOutorgaByIdeRequerimentoByNovaOutorga(Requerimento requerimento) {
		DetachedCriteria criteria = getCriteriaOutorgaByRequerimento(requerimento);
		criteria.add(Restrictions.eq("ts.ideTipoSolicitacao", TipoSolicitacaoEnum.NOVA_OUTORGA.getId()));
		return outorgaIDAO.listarPorCriteria(criteria, Order.asc("ideOutorga"));
	}
	
	public DetachedCriteria getCriteriaOutorgaByRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class);
		criteria.createAlias("ideRequerimento", "r");
		criteria.createAlias("ideTipoSolicitacao", "ts");
		criteria.createAlias("ideModalidadeOutorga", "mo", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoAlteracao", "ta", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()));
		return criteria;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Outorga getOutorgaByIdeImovel(Requerimento requerimento, Imovel imovel,
			ModalidadeOutorgaEnum modalidadeOutorgaEnum)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class);
		criteria.createAlias("ideImovel", "i");
		criteria.createAlias("ideModalidadeOutorga", "m");
		criteria.createAlias("ideRequerimento", "r");
		criteria.add(Restrictions.eq("i.ideImovel", imovel.getIdeImovel()));
		criteria.add(Restrictions.eq("m.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()));
		criteria.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()));
		if (!outorgaIDAO.listarPorCriteria(criteria).isEmpty()) {
			return outorgaIDAO.listarPorCriteria(criteria).get(0);
		} else {
			return null;
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarOutorga(Outorga outorga)  {
		outorgaIDAO.salvarOuAtualizar(outorga);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarOutorgaTipoCaptacao(OutorgaTipoCaptacao outorgaTipoCaptacao)  {
		if (Util.isNullOuVazio(buscarOutorgaTipoCaptacaoByIdeOutorga(outorgaTipoCaptacao.getIdeOutorga()))) {
			outorgaTipoCaptacaolDAO.salvarOuAtualizar(outorgaTipoCaptacao);// Se
																			// já
																			// houver
																			// alguma
																			// OutorgaTipoCaptação
																			// não
																			// Salvar
																			// de
																			// novo
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarOutorgaTipoReceptor(OutorgaTipoReceptor outorgaTipoReceptor)  {
		if (Util.isNullOuVazio(buscarOutorgaTipoReceptorByIdeOutorga(outorgaTipoReceptor.getIdeOutorga()))) {
			outorgaTipoReceptorIDAO.salvarOuAtualizar(outorgaTipoReceptor);// Se
																			// já
																			// houver
																			// alguma
																			// outorgaTipoReceptor
																			// não
																			// Salvar
																			// de
																			// novo
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerOutorga(Outorga outorga)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideOutorga", outorga.getIdeOutorga());
		outorgaIDAO.executarNamedQuery("Outorga.excluirByIdeOutorga", params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Outorga> getListaLicencaByIdeRequerimento(Requerimento req)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class);
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", req.getIdeRequerimento()));
		return outorgaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Outorga buscarOutorgaByModalidadeAndRequerimento(ModalidadeOutorgaEnum modalidadeOutorgaEnum,Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class)
				.createAlias("ideTipoSolicitacao", "ts")
				.createAlias("ideModalidadeOutorga", "mo")
				.add(Restrictions.and(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()),
				Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade())));
		return outorgaIDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Outorga buscarOutorgaByModalidadeAndRequerimentoAndTipoSolicitacao(ModalidadeOutorgaEnum modalidadeOutorgaEnum,Requerimento requerimento, TipoSolicitacao tipoSolicitacao)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class);
		criteria.createAlias("ideModalidadeOutorga", "mo");
		criteria.createAlias("ideTipoSolicitacao", "tipoSolicitacao");
		criteria.add(Restrictions.and(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()),
				Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade())));
		criteria.add(Restrictions.eq("tipoSolicitacao.ideTipoSolicitacao", tipoSolicitacao.getIdeTipoSolicitacao()));
		return outorgaIDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Outorga buscarOutorgaByTipoCaptacao(TipoCaptacaoEnum tipoCaptacao,Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class);
		criteria.createAlias("ideModalidadeOutorga", "mo");
		criteria.createAlias("tipoCaptacaoCollection", "tco");
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento())).add(Restrictions.eq("mo.ideModalidadeOutorga", ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade()))
		.add(Restrictions.eq("tco.ideTipoCaptacao", tipoCaptacao.getId()));
		return outorgaIDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Outorga> getOutorgaByModalidadeRequerimento(ModalidadeOutorgaEnum modalidadeOutorgaEnum,
			Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class);
		criteria.createAlias("ideModalidadeOutorga", "mo");
		criteria.add(Restrictions.and(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()),
				Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade())));
		return outorgaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Outorga getOutorgaByIdeModalidadeRequerimentoIntervencao(ModalidadeOutorgaEnum modalidadeOutorgaEnum,
			Requerimento requerimento, TipoIntervencao tipoIntervencao)  {
		/** de acordo com as mudanças do DER, TipoIntervencao passa para OUTORGA LOCALIZACAO GEOGRAFICA*/
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class);
		criteria.createAlias("ideModalidadeOutorga", "mo");
		criteria.createAlias("tipoIntervencaoCollection", "ti");
		criteria.add(Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()));
		criteria.add(Restrictions.eq("ti.ideTipoIntervencao", tipoIntervencao.getIdeTipoIntervencao()));
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		return outorgaIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public OutorgaTipoCaptacao buscarOutorgaTipoCaptacaoByIdeOutorga(Outorga outorga)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaTipoCaptacao.class);
		
		criteria.createAlias("ideOutorga", "o", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideOutorga.ideTipoSolicitacao", "tipoSolicitacao", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoCaptacao", "tc", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tc.ideTipologia", "tp", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideOutorga", outorga));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideOutorgaTipoCaptacao"), "ideOutorgaTipoCaptacao")
					.add(Projections.property("o.ideOutorga"), "ideOutorga;ideOutorga")
					.add(Projections.property("tc.ideTipoCaptacao"), "ideTipoCaptacao.ideTipoCaptacao")
					.add(Projections.property("tp.ideTipologia"), "ideTipoCaptacao.ideTipologia.ideTipologia")
					.add(Projections.property("tipoSolicitacao.ideTipoSolicitacao"), "ideOutorga.ideTipoSolicitacao.ideTipoSolicitacao")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(OutorgaTipoCaptacao.class))
				;
		
		return outorgaTipoCaptacaolDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaTipoCaptacao> listarOutorgaTipoCaptacaoByIdeOutorga(Outorga outorga)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaTipoCaptacao.class);
		
		criteria.setFetchMode("ideOutorga", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoCaptacao", FetchMode.JOIN);
		
		criteria.add(Restrictions.eq("ideOutorga", outorga));
		
		return outorgaTipoCaptacaolDAO.listarPorCriteria(criteria);
	}

	public OutorgaTipoReceptor buscarOutorgaTipoReceptorByIdeOutorga(Outorga outorga)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaTipoReceptor.class);
		criteria.setFetchMode("ideOutorga", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoReceptor", FetchMode.JOIN);
		criteria.add(Restrictions.eq("ideOutorga", outorga));
		return outorgaTipoReceptorIDAO.buscarPorCriteria(criteria);
	}

	/**
	 * 
	 * @param requerimento
	 * @param listaTipoSolicitacao
	 *            Opcional
	 * @return List<Outorga>
	 * @
	 * @Comentario Busca todas as outorgas pertencentes ao requerimento passado
	 *             e/ou Tipo(s) de Solicitação
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Outorga> listarOutorgaByRequerimentoECaptacaoEOuTipoSolicitacao(Requerimento requerimento,
			ModalidadeOutorga modalidadeOutorga, TipoSolicitacao... listaTipoSolicitacao)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class);
		criteria.setFetchMode("ideModalidadeOutorga", FetchMode.JOIN);
		criteria.setFetchMode("ideRequerimento", FetchMode.JOIN);
		criteria.setFetchMode("ideTipoSolicitacao", FetchMode.JOIN);
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		criteria.add(Restrictions.eq("ideModalidadeOutorga", modalidadeOutorga));
		for (TipoSolicitacao tipoSolicitacao : listaTipoSolicitacao) {
			criteria.add(Restrictions.eq("ideTipoSolicitacao", tipoSolicitacao));
		}
		return outorgaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoIntervencao> carregaListaTipoIntervencaoByIndAtivo()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoIntervencao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		criteria.add(Restrictions.isNull("ideTipoIntervencaoPai"));
		return tipoIntervencaoDAO.listarPorCriteria(criteria, Order.asc("ideTipoIntervencao"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoIntervencao> carregaListaTipoIntervencaoByIndAtivoByPaiExiste(Integer ideTipoIntervPai)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoIntervencao.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		criteria.add(Restrictions.eq("ideTipoIntervencaoPai.ideTipoIntervencao", ideTipoIntervPai));
		return tipoIntervencaoDAO.listarPorCriteria(criteria, Order.asc("ideTipoIntervencao"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoBarragem> carregaListaTipoBarragem() {
		return tipoBarragemDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoTravessia> carregaListaTipoTravessia()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoTravessia.class);
		criteria.add(Restrictions.isNull("ideTipoTravessiaPai"));
		return tipoTravessiaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoTravessia> carregaListaSubTipoTravessia(int ideTipoTravessia)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoTravessia.class);
		criteria.add(Restrictions.eq("ideTipoTravessiaPai", ideTipoTravessia));
		return tipoTravessiaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoReceptor> listarTodosTipoReceptor() {
		return tipoReceptorDAO.buscarPorNamedQuery("TipoReceptor.findAll");
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoReceptor> carregaListaTipoReceptor() {
		return tipoReceptorDAO.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAlteracao> carregaListaTipoAlteracao()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAlteracao.class);
		return tipoAlteracaoDAO.listarPorCriteria(criteria, Order.asc("ideTipoAlteracao"));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipoCaptacaoByIdeOutorga(Outorga outorga, TipoCaptacao... tipoCaptacao)  {
		Map<String, Object> params = new HashMap<String, Object>();
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaTipoCaptacao.class);
		criteria.add(Restrictions.eq("ideOutorga", outorga));
		if (tipoCaptacao.length != 0) {
			criteria.add(Restrictions.eq("ideTipoCaptacao", tipoCaptacao[0]));
		}
		if (!Util.isNullOuVazio(outorgaTipoCaptacaolDAO.buscarPorCriteria(criteria))) {
			OutorgaTipoCaptacao outortaTipoCapTemp = outorgaTipoCaptacaolDAO.buscarPorCriteria(criteria);
			params.put("ideOutorgaTipoCaptacao", outortaTipoCapTemp.getIdeOutorgaTipoCaptacao());
			outorgaTipoCaptacaolDAO.executarNamedQuery("OutorgaTipoCaptacao.deleteByIdeOutorgaTipoCaptacao", params);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirOutorga(Outorga outorga)  {
		Map<String, Object> params = new HashMap<String, Object>();
		
		DetachedCriteria criter = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criter.add(Restrictions.eq("ideOutorga", outorga));
		List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeo = outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criter, Order.asc("ideOutorgaLocalizacaoGeografica"));
		
		for (OutorgaLocalizacaoGeografica obj : listaOutorgaLocalizacaoGeo) {
			outorgaLocalizacaoGeograficaFinalidadeService.excluirOutorgaLocalizacaoGeograficaFinalidadesByOLG(obj);
			outorgaLocalizacaoGeograficaService.excluirOutorgaLocalizacaoGeograficaImovel(obj);
		}
		
		params.put("ideOutorga", outorga);
		
		outorgaLocalizacaoGeograficaDAO.executarNamedQuery("OutorgaLocalizacaoGeografica.excluirByIdeOutorga", params);
		
		params.clear();
		params.put("ideOutorga", outorga.getIdeOutorga());
		
		outorgaIDAO.executarNamedQuery("Outorga.excluirByIdeOutorga", params);
		
		params.clear();
		
		for (OutorgaLocalizacaoGeografica obj : listaOutorgaLocalizacaoGeo) {
			
			params.put("ideLocalizacaoGeografica", obj.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			dadoGeograficolDAO.executarNamedQuery("DadoGeografico.removerByIdLocalizacaoGeo", params);
			params.clear();
			
			params.put("ideLocalizacaoGeografica", obj.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			localizacaoGeolDAO.executarNamedQuery("LocalizacaoGeografica.deleteByIde", params);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipoReceptorByIdeOutorga(Outorga outorga, TipoReceptor... tipoReceptor) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaTipoReceptor.class);
			criteria.add(Restrictions.eq("ideOutorga", outorga));
			if (tipoReceptor.length != 0) {
				criteria.add(Restrictions.eq("ideTipoReceptor", tipoReceptor[0]));
			}
			OutorgaTipoReceptor outortaTipoReceptor;
			outortaTipoReceptor = outorgaTipoReceptorIDAO.buscarPorCriteria(criteria);
			if(!Util.isNullOuVazio(outortaTipoReceptor) && !Util.isNullOuVazio(outortaTipoReceptor.getIdeOutorgaTipoReceptor())){
				params.put("ideOutorgaTipoReceptor", outortaTipoReceptor.getIdeOutorgaTipoReceptor());
				outorgaTipoReceptorIDAO.executarNamedQuery("OutorgaTipoReceptor.deleteByIdeOutorgaTipoReceptor", params);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerOutorga(Requerimento requerimento,Integer idTipoCaptacao,Integer ideModalidade)  {
		
		List<Outorga> listOutorgaTemp = this.getOutorgaByModalidadeRequerimento(ModalidadeOutorgaEnum.CAPTACAO,requerimento);
		
		for(Outorga out : listOutorgaTemp){
			OutorgaTipoCaptacao outorgaTipoCaptacao = this.buscarOutorgaTipoCaptacaoByIdeOutorga(out);
			
			if(!Util.isNull(outorgaTipoCaptacao) && !Util.isNullOuVazio(outorgaTipoCaptacao.getIdeOutorga().getIdeTipoSolicitacao()) 
					&& outorgaTipoCaptacao.getIdeOutorga().getIdeTipoSolicitacao().getIdeTipoSolicitacao().equals(4)
					&& outorgaTipoCaptacao.getIdeTipoCaptacao().getIdeTipoCaptacao().equals(idTipoCaptacao)){					
					this.excluirTipoCaptacaoByIdeOutorga(out, new TipoCaptacao(idTipoCaptacao));
					this.excluirOutorga(out);
			}
		}
	}

	
	/**
	 * Como o DER do NR sofreu alterações, TipoIntervencao não serve mais como parâmetro para buscar a OUTORGA. (utilizado em FceIntervencaoBarragemController) 
	 * @param modalidadeOutorgaEnum
	 * @param requerimento
	 * @return Outorga
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Outorga getOutorgaByIdeModalidadeRequerimento(ModalidadeOutorgaEnum modalidadeOutorgaEnum, Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class);
		criteria.createAlias("ideModalidadeOutorga", "mo");
		criteria.add(Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()));
		criteria.add(Restrictions.eq("ideRequerimento", requerimento));
		return outorgaIDAO.buscarPorCriteria(criteria);
	}
}
