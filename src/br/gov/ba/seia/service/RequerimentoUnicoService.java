package br.gov.ba.seia.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.RequerimentoUnicoDAOIf;
import br.gov.ba.seia.dao.TipologiaGrupoDAOImpl;
import br.gov.ba.seia.dto.RequerimentoUnicoDTO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ComunicacaoRequerimento;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.FaseEmpreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.ObjetivoLimpezaArea;
import br.gov.ba.seia.entity.ObjetivoRequerimentoLimpezaArea;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PorteTipologia;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoTramite;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoImovel;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.RequerimentoPessoaPK;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoIntervencao;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TipoReceptor;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author mario.junior
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RequerimentoUnicoService {

	@Inject
	private IDAO<RequerimentoUnico> requerimentoUnicoDAO;
	@Inject
	private IDAO<TramitacaoRequerimento> tramitacaoRequerimentoDAO;
	@Inject
	private IDAO<Requerimento> requerimentoDAO;
	@Inject
	private IDAO<TipoIntervencao> tipoIntervencaoDAO;
	@Inject
	private IDAO<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaDAO;
	@Inject
	private IDAO<TipoReceptor> tipoReceptorDAO;
	@Inject
	private IDAO<TipoCaptacao> tipoCapitacaoDAO;
	@Inject
	private IDAO<ObjetivoLimpezaArea> objetivoLimpezaAreaDAO;
	@Inject
	private IDAO<ObjetivoRequerimentoLimpezaArea> objetivoReqLimpezaAreaDAO;
	@Inject
	private IDAO<Empreendimento> empreendimentoDAO;
	@EJB
	private PorteService porteService;
	@Inject
	private IDAO<Imovel> imovelDAO;
	@Inject
	private IDAO<FaseEmpreendimento> faseEmpreendimentoDAO;
	@Inject
	private IDAO<RequerimentoImovel> requerimentoImovelDAO;
	@Inject
	private IDAO<Orgao> orgaoDAO;
	@Inject
	private TipologiaGrupoDAOImpl tipologiaGrupoDAOImpl;
	@Inject
	private IDAO<ProcessoTramite> processoTramiteDAO;
	@Inject
	private RequerimentoUnicoDAOIf requerimentoUnicoDAOIf;
	@Inject
	private IDAO<RequerimentoTipologia> requerimentoTipologiaDAO;
	@Inject
	private IDAO<ProcuradorPessoaFisica> procuradorPessoaFisicaDAO;
	@Inject
	private IDAO<ProcuradorRepresentante> procuradorRepresentanteDAO;
	@Inject
	private IDAO<RepresentanteLegal> representanteLegalDAO;
	@Inject
	private IDAO<Parametro> parametroDAO;
	@Inject
	private IDAO<PorteTipologia> porteTipologiaDAO;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private RequerimentoPessoaService requerimentoPessoaService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequrimentoService;
	@EJB
	private ComunicacaoRequerimentoService comunicacaoService;
	@EJB
	private PermissaoPerfilService permissaoPerfilService;
	@EJB
	private EnderecoService enderecoService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private PessoaService pessoaService;
	@Inject
	private IDAO<Processo> processoDAO;
	@Inject
	private IDAO<Integer> integerIdeRequerimentoDAO;
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;
	@EJB
	private EmpreendimentoRequerimentoService empreendimentoRequerimentoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequerimentoUnico cadastrarRequerimentoUnico(RequerimentoUnico requerimentoUnico) throws Exception {
		Collection<RequerimentoImovel> collRequerimentoImovel = requerimentoUnico.getRequerimento().getRequerimentoImovelCollection();
		requerimentoUnico.getRequerimento().setRequerimentoImovelCollection(new ArrayList<RequerimentoImovel>());
		Collection<ProcessoTramite> collProcessoTramite = requerimentoUnico.getRequerimento().getProcessoTramiteCollection();
		requerimentoUnico.getRequerimento().setProcessoTramiteCollection(new ArrayList<ProcessoTramite>());
		Collection<RequerimentoTipologia> collRequerimentoTipologia = requerimentoUnico.getRequerimentoTipologiaCollection();
		requerimentoUnico.setRequerimentoTipologiaCollection(new ArrayList<RequerimentoTipologia>());
		if (!Util.isNull(collProcessoTramite) && collProcessoTramite.size() > 0) {
			for (ProcessoTramite processoTramite : collProcessoTramite) {
				processoTramite.setIdeRequerimento(requerimentoUnico.getRequerimento());
				processoTramiteDAO.salvarOuAtualizar(processoTramite);
			}
		}
		if (!Util.isNull(collRequerimentoImovel) && collRequerimentoImovel.size() > 0) {
			for (RequerimentoImovel requerimentoImovel : collRequerimentoImovel) {
				requerimentoImovel.setIdeTipoImovelRequerimento(requerimentoService.carregarTipoImovelRequerimentoById(1));
				requerimentoImovel.setImovel(requerimentoImovel.getImovel());
				requerimentoImovel.setRequerimento(requerimentoUnico.getRequerimento());
				requerimentoImovel.setDtcCriacao(new Date());
				requerimentoImovel.setRequerimento(requerimentoUnico.getRequerimento());
				requerimentoService.salvarRequerimentoImovel(requerimentoImovel);
				requerimentoUnico.getRequerimento().getRequerimentoImovelCollection().add(requerimentoImovel);
			}
		}
		if (!Util.isNullOuVazio(requerimentoUnico.getIdeLocalizacaoGeografica()) && !requerimentoUnico.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeograficaNotNull()) {
			localizacaoGeograficaService.salvarOuAtualizarLocalizacaoGeograficaVertices(requerimentoUnico.getIdeLocalizacaoGeografica());
		}
		requerimentoUnico.setIdeRequerimentoUnico(requerimentoUnico.getRequerimento().getIdeRequerimento());
		this.salvarRequerimentoUnico(requerimentoUnico);
		
		if(!Util.isNull(collRequerimentoTipologia)){
			for (RequerimentoTipologia requerimentoTipologia : collRequerimentoTipologia) {
				requerimentoTipologia.setIdeRequerimentoTipologia(null);
				requerimentoTipologia.setIdeRequerimento(requerimentoUnico.getRequerimento());
				requerimentoTipologiaDAO.salvar(requerimentoTipologia);
			}
		}
		return this.recuperarRequerimentoUnicoPorId(requerimentoUnico);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public RequerimentoUnico editarRequerimentoUnico(RequerimentoUnico requerimentoUnico) throws Exception {
		Collection<RequerimentoImovel> collRequerimentoImovel = requerimentoUnico.getRequerimento().getRequerimentoImovelCollection();
		requerimentoUnico.getRequerimento().setRequerimentoImovelCollection(new ArrayList<RequerimentoImovel>());
		Collection<ProcessoTramite> collProcessoTramite = requerimentoUnico.getRequerimento().getProcessoTramiteCollection();
		requerimentoUnico.getRequerimento().setProcessoTramiteCollection(new ArrayList<ProcessoTramite>());
		Collection<RequerimentoTipologia> collRequerimentoTipologia = requerimentoUnico.getRequerimentoTipologiaCollection();
		requerimentoUnico.setRequerimentoTipologiaCollection(new ArrayList<RequerimentoTipologia>());
		if (requerimentoUnico.getIdeLocalizacaoGeografica() != null) {
			try {
				localizacaoGeograficaService.salvarOuAtualizarLocalizacaoGeograficaVertices(requerimentoUnico.getIdeLocalizacaoGeografica());
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		if (Hibernate.isInitialized(collRequerimentoImovel) || !Util.isNullOuVazio(collRequerimentoImovel)) {
			for (RequerimentoImovel requerimentoImovel : collRequerimentoImovel) {
				if (requerimentoImovel.isRequerimentoUnico()) {
					requerimentoImovel.setIndExcluido(Boolean.FALSE);
					requerimentoImovel.setDtcExclusao(null);
					this.salvarRequerimentoImovel(requerimentoImovel);
					requerimentoUnico.getRequerimento().getRequerimentoImovelCollection().add(requerimentoImovel);
				}
			}
		}
		try {
			requerimentoService.inserirRequerimento(requerimentoUnico.getRequerimento());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		for (ProcessoTramite processoTramite : collProcessoTramite) {
			processoTramite.setIdeRequerimento(requerimentoUnico.getRequerimento());
			processoTramiteDAO.salvarOuAtualizar(processoTramite);
		}
		if (requerimentoUnico.getRequerimento().getCollProcessotramiteExclusao() != null && requerimentoUnico.getRequerimento().getCollProcessotramiteExclusao().size() > 0) {
			for (ProcessoTramite processoTramite : requerimentoUnico.getRequerimento().getCollProcessotramiteExclusao()) {
				processoTramiteDAO.remover(processoTramite);
			}
		}
		this.salvarRequerimentoUnico(requerimentoUnico);
		for (RequerimentoTipologia requerimentoTipologia : collRequerimentoTipologia) {
			requerimentoTipologiaDAO.salvarOuAtualizar(requerimentoTipologia);
		}
		return this.recuperarRequerimentoUnicoPorId(requerimentoUnico);
	}



	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerRequerimentoImovel(RequerimentoImovel requerimentoImovel) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimento", requerimentoImovel.getRequerimento());
		parametros.put("ideImovel", requerimentoImovel.getImovel());
		requerimentoImovelDAO.executarNamedQuery("RequerimentoImovel.remove", parametros);
	}

	public RequerimentoUnico recuperarRequerimentoUnicoPorId(RequerimentoUnico requerimentoUnico){
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimentoUnico", requerimentoUnico.getIdeRequerimentoUnico());
		RequerimentoUnico requerimentoUnicoObjeto = requerimentoUnicoDAO.buscarEntidadePorNamedQuery("RequerimentoUnico.findByIdeRequerimentoUnico", parametros);
		
		if(!Util.isNullOuVazio(requerimentoUnicoObjeto)) {
			
			if (requerimentoUnicoObjeto.getIdeLocalizacaoGeografica() != null && Util.isNullOuVazio(requerimentoUnicoObjeto.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())) {
				requerimentoUnicoObjeto.setIdeLocalizacaoGeografica(localizacaoGeograficaService.carregar(requerimentoUnicoObjeto.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
			}
			
			if(!Util.isNullOuVazio(requerimentoUnicoObjeto.getRequerimento())) {
			
				Requerimento requerimento = requerimentoUnicoObjeto.getRequerimento();
				
				
				if(Util.isLazyInitExcepOuNull(requerimento.getEmpreendimentoRequerimentoCollection())){
					requerimento.setEmpreendimentoRequerimentoCollection(empreendimentoRequerimentoService.listarEmpreendimentoRequerimento(requerimento));					
				}	
									
				if (!Util.isNull(requerimento.getIdeEnderecoContato())) {
					Integer ideEndereco = requerimento.getIdeEnderecoContato().getIdeEndereco();
					requerimento.setIdeEnderecoContato(this.enderecoService.carregar(ideEndereco));
				}
			}
		}
		
		return requerimentoUnicoObjeto;
	}

	public void recuperarProcessoTramite(RequerimentoUnico requerimentoUnico) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimento", requerimentoUnico.getRequerimento().getIdeRequerimento());
		requerimentoUnico.getRequerimento().setProcessoTramiteCollection(processoTramiteDAO.buscarPorNamedQuery("ProcessoTramite.findByRequerimento", parametros));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRequerimentoUnico(RequerimentoUnico requerimentoUnico){
		requerimentoUnicoDAO.salvarOuAtualizar(requerimentoUnico);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRequerimentoImovel(RequerimentoImovel requerimentoImovel)  {
		requerimentoImovelDAO.salvarOuAtualizar(requerimentoImovel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RequerimentoUnico buscarRequerimentoUnico(RequerimentoUnico requerimentoUnico) throws Exception {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimentoUnico", requerimentoUnico.getIdeRequerimentoUnico());
		RequerimentoUnico req = requerimentoUnicoDAO.buscarEntidadePorNamedQuery("RequerimentoUnico.findByIdeRequerimentoUnico", parametros);
		req.setEnquadramento(enquadramentoService.buscarEnquadramentoPorRequerimentoUnico(req.getRequerimento()));
		req.getRequerimento().setRequerimentoPessoaCollection(requerimentoPessoaService.buscarRequerimentoPessoaPorRequerimentoUnico(req));
		return req;
	}

	public List<RequerimentoUnicoDTO> filtrarRequerimentoUnicoByConsulta(RequerimentoUnico pRequerimentoUnico, Empreendimento empreendimentoRequerimento, Date pDataInicio, Date pDataFinal, StatusRequerimento statusRequerimento, Pessoa requerente,
			TipoPessoaRequerimento tipoPessoaRequerimento){
		//Se usuï¿½rio externo, mostra todos os empreendimentos seguindo as regras 56 e 177.
		if (ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil() == 2) {
			List<Integer> lst = new ArrayList<Integer>();
			lst.addAll(permissaoPerfilService.listarIdesPessoaFisicaAptos());
			lst.addAll(permissaoPerfilService.listarIdesPessoaJuridicaAptos());
			return requerimentoUnicoDAOIf.listaRequerimentoUnicoComParametro(pRequerimentoUnico, empreendimentoRequerimento, pDataInicio, pDataFinal, statusRequerimento, requerente, tipoPessoaRequerimento, lst);
		} else {
			return requerimentoUnicoDAOIf.listaRequerimentoUnicoComParametro(pRequerimentoUnico, empreendimentoRequerimento, pDataInicio, pDataFinal, statusRequerimento, requerente, tipoPessoaRequerimento, null);
		}
	}

	public List<Integer> listarIdesRequerimentoUnicoParaUsuarioExterno(){
		//Se usuï¿½rio externo, mostra todos os empreendimentos seguindo as regras 56 e 177.
		List<Integer> lst = new ArrayList<Integer>();
		lst.addAll(permissaoPerfilService.listarIdesPessoaFisicaAptos());
		lst.addAll(permissaoPerfilService.listarIdesPessoaJuridicaAptos());
		
		DetachedCriteria  criteria = DetachedCriteria.forClass(Requerimento.class);
		
		criteria
			.createAlias("requerimentoPessoaCollection", "req_pess")
			.createAlias("req_pess.ideTipoPessoaRequerimento", "tp_pess")
			.createAlias("req_pess.pessoa", "pessoa")
			.add(Restrictions.eq("tp_pess.ideTipoPessoaRequerimento", TipoPessoaRequerimentoEnum.REQUERENTE.getId()))
			.add(Restrictions.in("pessoa.idePessoa", lst))
			.setProjection(Projections.property("ideRequerimento"));
		
		List<Integer> listIds = integerIdeRequerimentoDAO.listarPorCriteria(criteria);
		
		return listIds;
	}

	public Collection<TipoIntervencao> listarTodosTipoIntervencao() {
		return tipoIntervencaoDAO.buscarPorNamedQuery("TipoIntervencao.findAll");
	}

	public Collection<TipoFinalidadeUsoAgua> listarTodosTipoFinalidadeUsoAgua() {
		return tipoFinalidadeUsoAguaDAO.buscarPorNamedQuery("TipoFinalidadeUsoAgua.findAll");
	}

	public Collection<TipoReceptor> listarTodosTipoReceptor() {
		return tipoReceptorDAO.buscarPorNamedQuery("TipoReceptor.findAll");
	}

	public Collection<TipoCaptacao> listarTodosTipoCaptacao() {
		return tipoCapitacaoDAO.buscarPorNamedQuery("TipoCaptacao.findAll");
	}

	public Collection<ObjetivoLimpezaArea> listarTodosObjetivoLimpezaArea() {
		return objetivoLimpezaAreaDAO.buscarPorNamedQuery("ObjetivoLimpezaArea.findAll");
	}
	
	public List<ObjetivoLimpezaArea> listTodosObjetivoLimpezaArea() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetivoLimpezaArea.class);
		return objetivoLimpezaAreaDAO.listarPorCriteria(criteria, Order.asc("nomObjetivoLimpezaArea"));
	}
	
	public Collection<ObjetivoRequerimentoLimpezaArea> listarRequerimentoObjetivoLimpezaAreaPorRequerimento(Requerimento requerimento) throws Exception {
		Collection<ObjetivoRequerimentoLimpezaArea> collObjRequerimentoLimpArea = new ArrayList<ObjetivoRequerimentoLimpezaArea>();
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetivoRequerimentoLimpezaArea.class);
		criteria.createAlias("ideObjetivoLimpezaArea", "objetivoLimpezaArea");
		criteria.createAlias("ideRequerimento", "requerimento");
		criteria.createAlias("ideRequerimentoImovel", "requerimentoImovel", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("requerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		collObjRequerimentoLimpArea = objetivoReqLimpezaAreaDAO.listarPorCriteria(criteria);
		
		return collObjRequerimentoLimpArea;
	}

	public List<String> listarTipoIntervencaoPorRequerimento(RequerimentoUnico requerimentoUnico) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimentoUnico", requerimentoUnico.getIdeRequerimentoUnico());
		List<TipoIntervencao> collTipoIntervencao = tipoIntervencaoDAO.buscarPorNamedQuery("TipoIntervencao.findTipoIntervencaoByRequerimentoUnico", parametros);
		List<String> listTipoIntervencao = new ArrayList<String>();
		for (TipoIntervencao tipoIntervencao : collTipoIntervencao) {
			listTipoIntervencao.add(tipoIntervencao.getIdeTipoIntervencao().toString());
		}
		return listTipoIntervencao;
	}

	public List<String> listarTipoFinalidadeUsoAguaPorRequerimento(RequerimentoUnico requerimentoUnico) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimentoUnico", requerimentoUnico.getIdeRequerimentoUnico());
		parametros.put("indRequerimento", true);
		List<TipoFinalidadeUsoAgua> collTipoFinalidadeAgua = tipoFinalidadeUsoAguaDAO.buscarPorNamedQuery("TipoFinalidadeUsoAgua.findTipoFinalidadeAguaByRequerimentoUnico", parametros);
		List<String> listTipoFinalidadeUsoAgua = new ArrayList<String>();
		for (TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua : collTipoFinalidadeAgua) {
			listTipoFinalidadeUsoAgua.add(tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua().toString());
		}
		return listTipoFinalidadeUsoAgua;
	}

	public List<String> listarTipoReceptorPorRequerimento(RequerimentoUnico requerimentoUnico) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimentoUnico", requerimentoUnico.getIdeRequerimentoUnico());
		List<TipoReceptor> collTipoReceptor = tipoReceptorDAO.buscarPorNamedQuery("TipoReceptor.findTipoReceptorByRequerimentoUnico", parametros);
		List<String> listTipoReceptor = new ArrayList<String>();
		for (TipoReceptor tipoReceptor : collTipoReceptor) {
			listTipoReceptor.add(tipoReceptor.getIdeTipoReceptor().toString());
		}
		return listTipoReceptor;
	}

	public List<String> listarTipoCaptacaoPorRequerimento(RequerimentoUnico requerimentoUnico)  {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimentoUnico", requerimentoUnico.getIdeRequerimentoUnico());
		List<TipoCaptacao> collTipoCaptacao = tipoCapitacaoDAO.buscarPorNamedQuery("TipoCaptacao.findTipoCaptacaoByRequerimentoUnico", parametros);
		List<String> listTipocaptacao = new ArrayList<String>();
		for (TipoCaptacao tipoCaptacao : collTipoCaptacao) {
			listTipocaptacao.add(tipoCaptacao.getIdeTipoCaptacao().toString());
		}
		return listTipocaptacao;
	}

	@SuppressWarnings("unchecked")
	public List<String> listarObjetivoLimpezaAreaPorRequerimento(RequerimentoUnico requerimentoUnico) throws Exception {
		List<String> listaObj = null;
		
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT ola.nom_objetivo_limpeza_area FROM objetivo_limpeza_area ola ");
		lSql.append("inner join objetivo_requerimento_limpeza_area orla on orla.ide_objetivo_limpeza_area = ola.ide_objetivo_limpeza_area ");
		lSql.append("where orla.ide_requerimento  = "+requerimentoUnico.getIdeRequerimentoUnico());
		
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
								
		listaObj  = lQuery.getResultList();
		
		return listaObj;
	}
	
	public List<ObjetivoRequerimentoLimpezaArea> listarObjetivoRequerimentoLimpezaAreaPorRequerimento(RequerimentoUnico requerimentoUnico) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(ObjetivoRequerimentoLimpezaArea.class);
		criteria.createAlias("ideRequerimentoImovel", "requerimentoImovel", JoinType.INNER_JOIN);
		criteria.createAlias("ideObjetivoLimpezaArea", "objetivoLimpezaArea", JoinType.INNER_JOIN);
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", requerimentoUnico.getRequerimento().getIdeRequerimento()));
		criteria.add(Restrictions.eq("requerimentoImovel.indExcluido", false));
		
		List<ObjetivoRequerimentoLimpezaArea> listaObjetivoRequerimentoLimpezaArea = null;
		listaObjetivoRequerimentoLimpezaArea = objetivoReqLimpezaAreaDAO.listarPorCriteria(criteria);
		return listaObjetivoRequerimentoLimpezaArea;
	}

	public Collection<FaseEmpreendimento> listarTodosFaseEmpreendimento() {
		return faseEmpreendimentoDAO.buscarPorNamedQuery("FaseEmpreendimento.findAll");
	}

	public Collection<Empreendimento> listarEmpreendimentoPorPessoa(Pessoa pessoa) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		if (!Util.isNullOuVazio(pessoa)) {
			parametros.put("idePessoa", pessoa.getIdePessoa());
		} else {
			parametros.put("idePessoa", null);
		}
		parametros.put("indExcluido", false);
		Collection<Empreendimento> collEmpreendimento = empreendimentoDAO.buscarPorNamedQuery("Empreendimento.findByIdePessoaAtivado", parametros);
		return collEmpreendimento;
	}

	public Collection<Empreendimento> listarEmpreendimentoPorProcurador(Pessoa pessoa, Pessoa requerente) {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		if (!Util.isNullOuVazio(pessoa)) {
			parametros.put("idePessoa", pessoa.getIdePessoa());
		} else {
			parametros.put("idePessoa", null);
		}
		if (!Util.isNullOuVazio(requerente)) {
			parametros.put("requerente", requerente.getIdePessoa());
		} else {
			parametros.put("requerente", null);
		}
		Collection<Empreendimento> collEmpreendimento = empreendimentoDAO.buscarPorNamedQuery("Empreendimento.findByIdePessoaProcuradorPessoaFisica", parametros);
		collEmpreendimento.addAll(empreendimentoDAO.buscarPorNamedQuery("Empreendimento.findByIdePessoaProcuradorPessoaJuridica", parametros));
		return collEmpreendimento;
	}

	public Collection<RequerimentoTipologia> listarRequerimentoTipologia(Empreendimento empreendimento) throws Exception {
		Collection<TipologiaGrupo> listaTipologiaGrupo = tipologiaGrupoDAOImpl.buscarPorEmpreendimento(empreendimento.getIdeEmpreendimento());

		Collection<RequerimentoTipologia> collRequerimentoTipologia = new ArrayList<RequerimentoTipologia>();
		for(TipologiaGrupo tipologiaGrupo : listaTipologiaGrupo){
			RequerimentoTipologia requerimentoTipologia = new RequerimentoTipologia();
			if(Util.isNullOuVazio(tipologiaGrupo.getUnidadeMedidaTipologiaGrupo())){
				tipologiaGrupo.setUnidadeMedidaTipologiaGrupo(new UnidadeMedidaTipologiaGrupo());
			}			
			tipologiaGrupo.getUnidadeMedidaTipologiaGrupo().setIdeTipologiaGrupo(tipologiaGrupo);
			requerimentoTipologia.setIdeUnidadeMedidaTipologiaGrupo(tipologiaGrupo.getUnidadeMedidaTipologiaGrupo());			
			collRequerimentoTipologia.add(requerimentoTipologia);
		}
		return collRequerimentoTipologia;
	}

	public Collection<RequerimentoTipologia> listarRequerimentoTipologiaPorRequerimento(Requerimento requerimento) throws Exception {
		
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideRequerimento", requerimento.getIdeRequerimento());
	
		Collection<RequerimentoTipologia> collRequerimentoTipologia = requerimentoTipologiaService.buscarRequerimentoTipologiaByRequerimento(requerimento);
		for (RequerimentoTipologia reqTip : collRequerimentoTipologia) {
			reqTip.setPreenchidoUsuario(Boolean.TRUE);
			String valorAtividade = reqTip.getValAtividade().toString();
			
			if (valorAtividade.substring(valorAtividade.length() - 2, valorAtividade.length()).contains(".")) {
				valorAtividade = reqTip.getValAtividade().toString().replace(".", ",") + "0";
			} else {
				valorAtividade = reqTip.getValAtividade().toString().replace(".", ",");
			}
			
			reqTip.setValAtividadeString(valorAtividade);
		}
		
		return collRequerimentoTipologia;
	}

	public String formatarValorRequerimentoTipologia(String valor) {
		String formatada = "";
		if (valor.length() > 2) {
			formatada = valor.substring(0, valor.length() - 2) + "," + valor.substring(valor.length() - 2);
		}
		if (valor.length() > 6) {
			formatada = formatada.substring(0, valor.length() - 7) + "." + valor.substring(valor.length() - 7);
		}
		return formatada;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Porte verificarPorte(DataModel<RequerimentoTipologia> requerimentoTipologia) throws Exception {
		int porteMaior = 0;
		Porte porte = null;
		Collection<Porte> collPorte = new ArrayList<Porte>();
		for (RequerimentoTipologia requerimentoTipologiaObject : requerimentoTipologia) {
			Integer tipologiaGrupo = requerimentoTipologiaObject.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologiaGrupo();
			BigDecimal valor = requerimentoTipologiaObject.getValAtividade();
			Porte p = porteService.calcularValorPorte(tipologiaGrupo,valor);
			if (p == null) {
				Porte po = new Porte();
				po.setIdePorte(6);
				requerimentoTipologiaObject.setPorte(po);
			} else {
				requerimentoTipologiaObject.setPorte(p);
			}
			collPorte.add(p);
		}
		for (Porte po : collPorte) {
			int valorPorte = 0;
			if (po != null) {
				if (po.getSglPorte() != null && po.getSglPorte().equalsIgnoreCase("mi")) {
					valorPorte = 1;
				} else if (po.getSglPorte() != null && po.getSglPorte().equalsIgnoreCase("pe")) {
					valorPorte = 2;
				} else if (po.getSglPorte() != null && po.getSglPorte().equalsIgnoreCase("me")) {
					valorPorte = 3;
				} else if (po.getSglPorte() != null && po.getSglPorte().equalsIgnoreCase("gr")) {
					valorPorte = 4;
				} else if (po.getSglPorte() != null && po.getSglPorte().equalsIgnoreCase("ex")) {
					valorPorte = 5;
				}
			}
			if (valorPorte > porteMaior) {
				porteMaior = valorPorte;
				porte = po;
			}
		}
		if (porte == null) {
			Map<String, Object> parametros = new TreeMap<String, Object>();
			parametros.put("idePorte", 6);
			porte = porteService.buscarPorte(parametros);
			if(!Util.isNull(porte)) 
				porte.setNomPorte("Porte dispensado de licenciamento Ambiental. Continue com o requerimento para verificar a necessidade de outros atos.");
		}
		return porte;
	}

	

	public Collection<RequerimentoImovel> recuperarImoveisEmpreendimento(Empreendimento empreendimento) {
		Collection<RequerimentoImovel> collRequerimentoImovel = new ArrayList<RequerimentoImovel>();
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		Collection<Imovel> collImovel = imovelDAO.buscarPorNamedQuery("Imovel.findByEmpreendimentoComOuSemImovel", parametros);
		for (Imovel imovel : collImovel) {
			RequerimentoImovel requerimentoImovel = new RequerimentoImovel();
			requerimentoImovel.setImovel(imovel);
			collRequerimentoImovel.add(requerimentoImovel);
		}
		return collRequerimentoImovel;
	}

	public Collection<RequerimentoImovel> recuperarImoveisRequerimento(Requerimento requerimento, Empreendimento empreendimento) {
		Collection<RequerimentoImovel> collRequerimentoImovel = new ArrayList<RequerimentoImovel>();
		DetachedCriteria criteria = DetachedCriteria.forClass(RequerimentoImovel.class);
		criteria.createAlias("imovel", "imovel");
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica");
		criteria.add(Restrictions.eq("requerimento", requerimento));
		
		collRequerimentoImovel = requerimentoImovelDAO.listarPorCriteria(criteria);
		
		for (RequerimentoImovel requerimentoImovel : collRequerimentoImovel) {
			if(Util.isNullOuVazio(requerimentoImovel.getImovel())){
				Hibernate.initialize(requerimentoImovel.getImovel());
				if (!requerimentoImovel.isIndExcluido()) {
					requerimentoImovel.setRequerimentoUnico(true);
				}
			}
		}
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("requerimento", requerimento);
		parametros.put("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		Collection<Imovel> collImovel = imovelDAO.buscarPorNamedQuery("Imovel.findByAusenteRequerimento", parametros);
		for (Imovel imovel : collImovel) {
			RequerimentoImovel requerimentoImovel = new RequerimentoImovel();
			requerimentoImovel.setImovel(imovel);
			collRequerimentoImovel.add(requerimentoImovel);
		}
		return collRequerimentoImovel;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void recuperarLocalizacaoGeograficaPorRequerimentoUnico(RequerimentoUnico requerimentoUnico) throws Exception {
		requerimentoUnico.setIdeLocalizacaoGeografica(localizacaoGeograficaService.carregar(requerimentoUnico.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarRequerimentoUnico(RequerimentoUnico ideRequerimentoUnico) throws Exception {
		requerimentoUnicoDAO.salvarOuAtualizar(ideRequerimentoUnico);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Orgao recuperarOrgao(Orgao orgao){
		return orgaoDAO.carregarGet(orgao.getIdeOrgao());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void geraNumeroRequerimento(Requerimento requerimento) throws Exception {
		requerimento.setNumRequerimento(gerarNumero(requerimento));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void geraNumeroRequerimentoTipo(Requerimento requerimento, String tipo) throws Exception {
		requerimento.setNumRequerimento(gerarNumeroTipo(requerimento, tipo));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumero(Requerimento requerimento) throws Exception {
		StringBuilder numeroRequerimento = new StringBuilder();
		
		int contador = 0 ;
		boolean primeiraConsulta = true;	
		
		while(primeiraConsulta || requerimentoService.isExisteNumRequerimento(requerimento)){	
			contador++;
		
			if(!primeiraConsulta){
				numeroRequerimento = new StringBuilder();
			}
			
			numeroRequerimento  
				.append(new SimpleDateFormat("yyyy").format(new Date()))
				.append('.');
			
			numeroRequerimento
				.append(Util.lpad(requerimento.getIdeOrgao().getCodOrgao().toString(), '0', 3))
			    .append('.');
		
			numeroRequerimento 
				.append(getNumByOrgaoByAno(requerimento, contador))
				.append('/');
				
			numeroRequerimento  
				.append(requerimento.getIdeOrgao().getDscSiglaOrgao())
				.append("/REQ");
			
			primeiraConsulta = false;
			requerimento.setNumRequerimento(numeroRequerimento.toString());
		}
		
		return numeroRequerimento.toString();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumeroTipo(Requerimento requerimento, String tipo) throws Exception {
		StringBuilder numeroRequerimento = new StringBuilder();
		
		int contador = 0 ;
		boolean primeiraConsulta = true;	
		
		while(primeiraConsulta || requerimentoService.isExisteNumRequerimento(requerimento)){	
			contador++;
		
			if(!primeiraConsulta){
				numeroRequerimento = new StringBuilder();
			}
			
			numeroRequerimento  
				.append(new SimpleDateFormat("yyyy").format(new Date()))
				.append('.');
			
			numeroRequerimento
				.append(Util.lpad(requerimento.getIdeOrgao().getCodOrgao().toString(), '0', 3))
			    .append('.');
		
			numeroRequerimento 
				.append(getNumByOrgaoByAno(requerimento, contador))
				.append('/');
				
			numeroRequerimento  
				.append(requerimento.getIdeOrgao().getDscSiglaOrgao())
				.append("/")
				.append(tipo);
			
			primeiraConsulta = false;
			requerimento.setNumRequerimento(numeroRequerimento.toString());
		}
		
		return numeroRequerimento.toString();
	}

	
	private String getNumByOrgaoByAno(Requerimento requerimento, int contador)throws Exception, NumberFormatException {
		Requerimento r = this.getMaiorNumeroRequerimentoByOrgaoByAnoAtual(requerimento);
		String numRequerimento = null;	
		
		if (!Util.isNull(r) && !Util.isNullOuVazio(r.getNumRequerimento())) {
			if (r.getNumRequerimento().length() >= 16) {
				numRequerimento = Util.lpad(Integer.toString(Integer.parseInt(r.getNumRequerimento().substring(9, 15)) + contador), '0', 6);
			}
		}else{
			numRequerimento =  Util.lpad(Integer.toString(contador) , '0', 6);			
		}
		
		return numRequerimento;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Requerimento getMaiorNumeroRequerimentoByOrgaoByAnoAtual(Requerimento requerimento) throws Exception{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Requerimento.class)
			.add(Restrictions.eq("ideOrgao", requerimento.getIdeOrgao()))
			.add(Restrictions.sqlRestriction("substring (num_requerimento, 0, 5) = '"+ Calendar.getInstance().get(Calendar.YEAR) + "'"));
		
			if(Calendar.getInstance().get(Calendar.YEAR) == 2016){
				detachedCriteria.add(Restrictions.sqlRestriction("((Substring (num_requerimento, 10, 6)) :: INTEGER ) < 28405 OR ((Substring (num_requerimento, 10, 6)) :: INTEGER ) > 29321"));
			}
	
			detachedCriteria.setProjection(Projections.projectionList()
					.add(Projections.max("numRequerimento"),"numRequerimento")

			).setResultTransformer(new AliasToNestedBeanResultTransformer(Requerimento.class));
		return requerimentoDAO.buscarPorCriteria(detachedCriteria);
	}
	
	public void enviarEmailAprovado(Enquadramento enquadramento) throws Exception {
		Requerimento requerimento = requerimentoService.carregar(enquadramento.getIdeRequerimentoUnico().getRequerimento());
		List<String> emails = carregarListaEmails(enquadramento);
		String textoMsg = textoEmailAprovado(enquadramento).toString();
		enviarEmails("SEIA - Comunicado - Requerimento nÃºmero " + enquadramento.getIdeRequerimentoUnico().getRequerimento().getNumRequerimento() + "", emails, textoMsg);
		salvarComunicacao(requerimento, textoMsg);
	}

	private List<String> carregarListaEmails(Enquadramento enquadramento) throws Exception {
		List<String> emails = new ArrayList<String>();
		Requerimento requerimento = requerimentoService.carregar(enquadramento.getIdeRequerimentoUnico().getRequerimento());
		if(!Util.isNullOuVazio(requerimento.getDesEmail()))
				emails.add(requerimento.getDesEmail());
		for (RequerimentoPessoa rp : requerimento.getRequerimentoPessoaCollection()) {
			if (!Util.isNullOuVazio(rp.getPessoa().getDesEmail()) && !emails.contains(rp.getPessoa().getDesEmail()) && !rp.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.ATENDENTE.getId())) {
				emails.add(rp.getPessoa().getDesEmail());
			}
		}
		return emails;
	}

	public void salvarComunicacao(Requerimento req, String texto) throws Exception {
		ComunicacaoRequerimento comunicacao = new ComunicacaoRequerimento();
		comunicacao.setIdeRequerimento(req);
		comunicacao.setDtcComunicacao(new Date());
		comunicacao.setDesMensagem(texto);
		comunicacaoService.salvar(comunicacao);
	}

	private StringBuilder textoEmailAprovado(Enquadramento enquadramento) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("O Requerimento de nÂº " + enquadramento.getIdeRequerimentoUnico().getRequerimento().getNumRequerimento() + "  foi enquadrado com sucesso.\n");
		buffer.append("Para regularizaÃ§Ã£o do empreendimento, os seguintes atos sÃ£o necessários:\n");
		List<AtoAmbiental> atos = (List<AtoAmbiental>) enquadramento.getAtoAmbientalCollection();
		for (int i = 0; i < atos.size(); i++) {
			buffer.append(atos.get(i).getNomAtoAmbiental() + (i < (atos.size() - 1) ? ", " : "."));
		}
		buffer.append("\n");
		buffer.append("Favor acessar o SEIA para efetuar o envio da documentaÃ§Ã£o obrigatÃ³ria e o preenchimento do(s) formulÃ¡rio(s) cabÃ­veis.");
		buffer.append("\n At.te,");
		buffer.append("\n Central de Atendimento/INEMA.");
		return buffer;
	}

	public void enviarEmailNaoAprovado(Enquadramento enquadramento) throws Exception {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Prezado(a), Não foi possí­vel enquadrar o Requerimento de nÂº " + enquadramento.getIdeRequerimentoUnico().getRequerimento().getNumRequerimento() + ". Segue justificativa:\n");
		buffer.append(enquadramento.getDscJustificativa() + "\n");
		buffer.append("Para que o processo de requerimento seja concluÃ­do, Ã© necessÃ¡ria que \n");
		buffer.append("se resolvam as pendÃªncias. Para tal, entre no SEIA com seu login e \n");
		buffer.append("senha, acesse a funcionalidade de consulta de Requerimentos (item de menu Requerimento, subitem Consultar)");
		buffer.append(" e altere o requerimento indicado conforme justificativa acima.");
		buffer.append("Atte.,\n");
		buffer.append("Central de Atendimento/INEMA.");
		List<String> emails = carregarListaEmails(enquadramento);
		String texto = buffer.toString();
		enviarEmails("SEIA - Comunicado - Requerimento nÃºmero " + enquadramento.getIdeRequerimentoUnico().getRequerimento().getNumRequerimento() + "", emails, texto);
		salvarComunicacao(enquadramento.getIdeRequerimentoUnico().getRequerimento(), texto);
	}

	private void enviarEmails(String assunto, List<String> lista, String texto) throws Exception {
		EmailUtil eMailUtil = new EmailUtil();
		eMailUtil.enviarEmailHtml(null, null, lista, assunto, texto);
	}

	public List<RequerimentoUnicoDTO> consultaRequerimentoUnico(RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento, Municipio municipio,ArrayList<Municipio> municipios,
			Usuario usuario, Integer firstPage, Integer pageSize,Map<String, Object> params) throws Exception {
		List<RequerimentoUnicoDTO> collRequerimentoUnico = null;
		if (ContextoUtil.getContexto().getUsuarioLogado().getIndTipoUsuario()) {
			return requerimentoUnicoDAOIf.listarRequerimentoUnico(requerimentoUnico, dataInicio, dataFim, empreendimento, requerente, statusRequerimento,municipio,municipios, usuario, firstPage, pageSize,params);
		} else {
			List<Integer> listaIdes = new ArrayList<Integer>();
			listaIdes.addAll(permissaoPerfilService.listarIdesPessoas());
			collRequerimentoUnico = requerimentoUnicoDAOIf.listarRequerimentoUnicoExterno(requerimentoUnico, dataInicio, dataFim, empreendimento, requerente, statusRequerimento,municipio,municipios, usuario, listaIdes, firstPage, pageSize,params);
		}
		return collRequerimentoUnico;
	}


	public List<ProcuradorPessoaFisica> recuperarProcuradorPessoaFisicaAssinaturaObrigatoriaPorRequerenteEmpreendimento(Pessoa pessoa, Empreendimento empreendimento) throws Exception {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("idePessoa", pessoa.getIdePessoa());
		parametros.put("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		parametros.put("indAssinaturaObrigatoria", Boolean.TRUE);
		parametros.put("indExcluido", Boolean.FALSE);
		
		List<ProcuradorPessoaFisica> collProcuradorPessoaFisica = procuradorPessoaFisicaDAO.buscarPorNamedQuery("ProcuradorPessoaFisica.findByRequerenteEmpreendimento", parametros);
		
		for (ProcuradorPessoaFisica p : collProcuradorPessoaFisica) {
			p.getIdeProcurador().setPessoa(pessoaService.carregarGet(p.getIdeProcurador().getIdePessoaFisica()));
		}
		
		return collProcuradorPessoaFisica;
	}

	public List<ProcuradorRepresentante> recuperarProcuradorRepresentanteAssinaturaObrigatoriaPorRequerenteEmpreendimento(Pessoa pessoa, Empreendimento empreendimento) throws Exception {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("idePessoa", pessoa.getIdePessoa());
		parametros.put("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		parametros.put("indExcluido", Boolean.FALSE);

		//#10643
		
		List<ProcuradorRepresentante> collProcuradorRepresentante = procuradorRepresentanteDAO.buscarPorNamedQuery("ProcuradorRepresentante.findByRequerenteEmpreendimento", parametros);
		
		for (ProcuradorRepresentante p : collProcuradorRepresentante) {
			p.getIdeProcurador().setPessoa(pessoaService.carregarGet(p.getIdeProcurador().getIdePessoaFisica()));
		}
		
		return collProcuradorRepresentante;
	}

	public List<RepresentanteLegal> recuperarRepresentanteLegalAssinaturaObrigatoriaPorRequerente(Pessoa pessoa) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(RepresentanteLegal.class)
				.setFetchMode("idePessoaFisica", FetchMode.JOIN)
				.setFetchMode("pf.pessoa", FetchMode.JOIN)
				
				.add(Restrictions.eq("idePessoaJuridica.idePessoaJuridica", pessoa.getIdePessoa()))
				.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		
//				#6131
//				.add(Restrictions.eq("indAssinaturaObrigatoria", Boolean.TRUE))
		
		List<RepresentanteLegal> collRepresentanteLegal = representanteLegalDAO.listarPorCriteria(criteria);
		
		return collRepresentanteLegal;
	}

	public Parametro recuperarParametro(Parametro parametro) throws Exception {
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("nomParametro", parametro.getNomParametro());
		parametro = parametroDAO.buscarEntidadePorNamedQuery("Parametro.findByNomParametro", parametros);
		return parametro;
	}

	public List<Processo> carregarProcessosEmpreendimentos(Empreendimento empreendimento) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class, "processo")
			.createAlias("processo.ideRequerimento", "requerimento", JoinType.LEFT_OUTER_JOIN)
			.createAlias("requerimento.empreendimentoRequerimentoCollection","empReq")
			.createAlias("empReq.ideEmpreendimento","empreendimentos")
			.createAlias("requerimento.empreendimentoCollection", "empreendimentos")
			.add(Restrictions.eq("empreendimentos.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		List<Processo> lista = processoDAO.listarPorCriteria(criteria, Order.asc("dtcFormacao"));
		if (Util.isNullOuVazio(lista)) {
			return new ArrayList<Processo>();
		}
		
		return lista;
		
	}

	
	public boolean verificarRequerimentoUnicoAberto(Empreendimento empreendimento) throws Exception {
		return requerimentoUnicoDAOIf.listarRequerimentoUnicoAbertos(empreendimento);
	}
	
	public boolean verificarRequerimentoAssociadoEmpreendimento(Empreendimento empreendimento) throws Exception {
		return requerimentoUnicoDAOIf.listarRequerimentosAssociadosEmpreendimento(empreendimento);
	}

	public RequerimentoUnico recuperarUltimoRequerimentoUnicoProcessado(Empreendimento empreendimento) throws Exception {
		return requerimentoUnicoDAOIf.recuperarUltimoRequerimentoUnicoProcessado(empreendimento);
	}
	
	public Requerimento recuperarUltimoRequerimento(Empreendimento empreendimento) throws Exception {
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		
		sql.append("select max(r) ");
		sql.append("from Requerimento r ");
		sql.append("inner JOIN r.empreendimentoRequerimentoCollection empReq ");
		sql.append("inner JOIN empReq.ideEmpreendimento emp ");
		sql.append("where emp = :empreendimento ");
		
		param.put("empreendimento", empreendimento);
		
		return requerimentoDAO.buscarPorQuery(sql.toString(), param);
		
	}
	
	public Integer retornaTramitacaoRequerimentoUnico(Integer ideRequerimentoUnico) throws Exception {
		
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		
		sql.append("select tr ");
		sql.append("from TramitacaoRequerimento tr ");
		sql.append("inner join fetch tr.ideRequerimento r ");
		sql.append("where tr.dtcMovimentacao = (select max(tr2.dtcMovimentacao) ");
		sql.append("                           from TramitacaoRequerimento tr2  ");
		sql.append("                           where tr2.ideRequerimento = r.ideRequerimento) ");
		sql.append("and r.ideRequerimento = :ideRequerimentoUnico ");
		
		param.put("ideRequerimentoUnico", ideRequerimentoUnico);
		
		TramitacaoRequerimento t = tramitacaoRequerimentoDAO.buscarPorQuery(sql.toString(), param);
		
		return t.getIdeStatusRequerimento().getIdeStatusRequerimento();
		
	}

	public PorteTipologia recuperarPortePequenoPorTipologiaGrupo(TipologiaGrupo tipologiaGrupo) throws Exception {
		PorteTipologia porteTipologia = null;
		Map<String, Object> parametros = new TreeMap<String, Object>();
		parametros.put("idePorte", 2);
		parametros.put("ideTipologiaGrupo", tipologiaGrupo.getIdeTipologiaGrupo());
		porteTipologia = porteTipologiaDAO.buscarEntidadePorNamedQuery("PorteTipologia.findByIdePorteIdePorteTipologia", parametros);
		return porteTipologia;
	}

	public FaseEmpreendimento carregaFaseEmpreendimentoByIdeRequerimento(Integer ideRequerimento) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(FaseEmpreendimento.class)
				.createAlias("empreendimentoRequerimentoCollection", "empreendimentoRequerimento");
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideFaseEmpreendimento"), "ideFaseEmpreendimento")
				.add(Projections.property("nomFaseEmpreendimento"), "nomFaseEmpreendimento")
				.add(Projections.property("empreendimentoRequerimento.ideRequerimento"), "empreendimentoRequerimentoCollection.ideRequerimento"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(FaseEmpreendimento.class));
		criteria.add(Restrictions.eq("empreendimentoRequerimento.ideRequerimento.ideRequerimento", ideRequerimento));
		return faseEmpreendimentoDAO.buscarPorCriteria(criteria);
	}

	public Integer countRequerimentoUnico(int first, int pageSize,RequerimentoUnico requerimentoUnico, Date dataInicio, Date dataFim, Empreendimento empreendimento, Pessoa requerente, StatusRequerimento statusRequerimento,Municipio municipio,ArrayList<Municipio> municipios, Usuario usuario,Map<String, Object> params) throws Exception {
		if (ContextoUtil.getContexto().getUsuarioLogado().getIndTipoUsuario()) {
			return requerimentoUnicoDAOIf.countListarRequerimentoUnico(first,pageSize,requerimentoUnico, dataInicio, dataFim, empreendimento, requerente, statusRequerimento,municipio,municipios, usuario,params);
		} else {
			List<Integer> listaIdes = new ArrayList<Integer>();
			listaIdes.addAll(permissaoPerfilService.listarIdesPessoas());
			return requerimentoUnicoDAOIf.countListarRequerimentoUnicoExterno(first,pageSize,requerimentoUnico, dataInicio, dataFim, empreendimento, requerente, statusRequerimento,municipio,municipios, usuario, listaIdes,params);
		}
	}

	public RequerimentoUnico recuperarRequerimentoUnicoProcessoFormadoAnterior(Empreendimento empreendimento) throws Exception {
		return requerimentoUnicoDAOIf.recuperarRequerimentoUnicoProcessoFormadoAnterior(empreendimento);
	}

	public void RequerimentoUnicoInitializeEmpreendimento(RequerimentoUnico requerimentoUnico) throws Exception {
		requerimentoUnico.setRequerimento(new Requerimento(requerimentoUnico.getIdeRequerimentoUnico()));
		requerimentoUnico.setRequerimento(requerimentoService.buscarEntidadePorId(requerimentoUnico.getRequerimento()));
	
		List<EmpreendimentoRequerimento> empreendimentos = new ArrayList<EmpreendimentoRequerimento>();
		
		for (Empreendimento empreendimento : empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimentoUnico.getRequerimento())) {
			EmpreendimentoRequerimento empreendimentoRequerimento = new EmpreendimentoRequerimento();
			empreendimentoRequerimento.setIdeEmpreendimento(empreendimento);
		}
		
		requerimentoUnico.getRequerimento().setEmpreendimentoRequerimentoCollection(empreendimentos);
		
	}

	public List<RequerimentoUnico> buscarRequerimentoUnicoDispensavelDePagamento(Integer ideRequerimento) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoUnico.class)
			.createAlias("enquadramento", "e")
			.createAlias("e.atoAmbientalCollection", "ato")
		.setProjection(Projections.projectionList()
			.add(Projections.property("ideRequerimentoUnico"),"ideRequerimentoUnico")
			.add(Projections.property("numVazaoCaptacao"),"numVazaoCaptacao")
			.add(Projections.property("ato.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
		);
		
		detachedCriteria.add( 
			Restrictions.disjunction()
			.add(Restrictions.between("numVazaoCaptacao", BigDecimal.ZERO, BigDecimal.valueOf(43.2)))
			.add(Restrictions.eq("ato.ideAtoAmbiental", AtoAmbientalEnum.PERFURACAO_POCO.getId()))
			.add(Restrictions.ne("ato.ideAtoAmbiental", AtoAmbientalEnum.PERFURACAO_POCO.getId()))
		);
				
		detachedCriteria.add(Restrictions.eq("this.ideRequerimentoUnico", ideRequerimento));
		
		detachedCriteria.setResultTransformer(new AliasToNestedBeanResultTransformer(RequerimentoUnico.class));
		
		return requerimentoUnicoDAO.listarPorCriteria(detachedCriteria);		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void inserirRequerimento(Requerimento requerimento) throws Exception {
		
		Collection<RequerimentoPessoa> collRequerimentoPessoa = requerimento.getRequerimentoPessoaCollection();
		requerimento.setRequerimentoPessoaCollection(new ArrayList<RequerimentoPessoa>());
		
		Collection<TramitacaoRequerimento> collTramitacaoRequerimento = requerimento.getTramitacaoRequerimentoCollection();
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		
		this.requerimentoService.inserirRequerimento(requerimento);
		salvarTramitacaoRequerimento(requerimento, collTramitacaoRequerimento);
		

		salvarPessoasRequerimento(requerimento,collRequerimentoPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarRequerimento(Requerimento requerimento) throws Exception {
		
		Collection<RequerimentoPessoa> collRequerimentoPessoa = requerimento.getRequerimentoPessoaCollection();
		requerimento.setRequerimentoPessoaCollection(new ArrayList<RequerimentoPessoa>());
		
		Collection<TramitacaoRequerimento> collTramitacaoRequerimento = requerimento.getTramitacaoRequerimentoCollection();
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		
		this.requerimentoService.atualizarRequerimento(requerimento);
		salvarTramitacaoRequerimento(requerimento, collTramitacaoRequerimento);
		
		salvarPessoasRequerimento(requerimento,collRequerimentoPessoa);
	}
	
	public boolean existeEmpreendimentoRequerimento(Requerimento requerimento) throws Exception {
		EmpreendimentoRequerimento empReq = empreendimentoService.buscarEmpreendimentoRequerimento(requerimento);
		return !Util.isLazyInitExcepOuNull(empReq) && !Util.isNullOuVazio(empReq);
	}

	public void salvarTramitacaoRequerimento(Requerimento requerimento, Collection<TramitacaoRequerimento> collTramitacaoRequerimento) {
		if(!Util.isLazyInitExcepOuNull(collTramitacaoRequerimento)) {
			for (TramitacaoRequerimento tramitacaoRequerimento : collTramitacaoRequerimento) {
				tramitacaoRequerimento.setIdeRequerimento(requerimento);
				tramitacaoRequerimento.setDtcMovimentacao(new Date());
				tramitacaoRequrimentoService.salvar(tramitacaoRequerimento);
				requerimento.getTramitacaoRequerimentoCollection().add(tramitacaoRequerimento);
			}
		}
	}

	public void salvarEmpreendimentoRequerimento(Requerimento requerimento) throws Exception {
		if(!Util.isLazyInitExcepOuNull(requerimento.getEmpreendimentoRequerimentoCollection())) {
			
		try{
			
			EmpreendimentoRequerimento emp = new EmpreendimentoRequerimento();
			List<Empreendimento> empreendimentos = new ArrayList<Empreendimento>(); 
								
			for (EmpreendimentoRequerimento empReq : requerimento.getEmpreendimentoRequerimentoCollection()) {
				empreendimentos.add(empReq.getIdeEmpreendimento());			
			}
	
			for (Empreendimento empree : empreendimentos) {					
				emp.setIdeEmpreendimento(empree);
			}
			
			emp.setIdeRequerimento(requerimento);	
			empreendimentoService.salvarEmpreendimentoRequerimento(emp);
			
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
			
		}
	}

	public void salvarPessoasRequerimento(Requerimento requerimento,Collection<RequerimentoPessoa> collRequerimentoPessoa) {
		if(!Util.isNullOuVazio(collRequerimentoPessoa)){

			for (RequerimentoPessoa requerimentoPessoa : collRequerimentoPessoa) {
				
				requerimentoPessoa.setRequerimento(requerimento);
				
				RequerimentoPessoaPK requerimentoPessoaPK = new RequerimentoPessoaPK();
				
				requerimentoPessoaPK.setIdePessoa(requerimentoPessoa.getPessoa().getIdePessoa());
				requerimentoPessoaPK.setIdeRequerimento(requerimentoPessoa.getRequerimento().getIdeRequerimento());
				requerimentoPessoaPK.setIdeTipoPessoaRequerimento(requerimentoPessoa.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento());
				
				requerimentoPessoa.setRequerimentoPessoaPK(requerimentoPessoaPK);
				requerimentoPessoaService.salvarRequerimentoPessoa(requerimentoPessoa);							
			}
		}
	}

	public RequerimentoPessoaPK setRequerimentoPessoaPK(RequerimentoPessoa p){
			
		RequerimentoPessoaPK pk = new RequerimentoPessoaPK();
		
		pk.setIdePessoa			(p.getPessoa().getIdePessoa());
		pk.setIdeRequerimento	(p.getRequerimento().getIdeRequerimento());
		pk.setIdeTipoPessoaRequerimento(p.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento());
	
		return pk;
		
	}
	
	/**
	 * Método para verificar se a lista de {@link RequerimentoTipologia} daquele {@link Empreendimento} contém {@link Tipologia} com código ("X" ou "Y") ou ("X" e "Y")   
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 03/10/2014
	 * @param empreendimento
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isTipologiaDoEmpreendimentoXouY(Empreendimento empreendimento) throws Exception{
		List<RequerimentoTipologia> listaRequerimentoTipologia = (List<RequerimentoTipologia>) listarRequerimentoTipologia(empreendimento);
		if(!Util.isNull(listaRequerimentoTipologia)){
			switch (listaRequerimentoTipologia.size()) {
			// caso a lista tenha apenas uma Tipologia, verificar se ela do código X ou Y
			case 1:
				String desTipologia = listaRequerimentoTipologia.get(0).getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia().getCodTipologia();
				return desTipologia.compareTo("X") == 0  || desTipologia.compareTo("Y") == 0;
				// caso a lista tenha duas Tipologias, verificar se são do código X e Y
			case 2:
				boolean isX = false, isY = false;
				for(RequerimentoTipologia requerimentoTipologia : listaRequerimentoTipologia){
					if(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia().getCodTipologia().compareTo("X") == 0){
						isX = true;
					} else if(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia().getCodTipologia().compareTo("Y") == 0){
						isY = true;
					}
				}
				return isX && isY;
			default:
				return false;
			}
		}
		return false;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void geraNumeroRequerimentoDTRP(Requerimento requerimento) throws Exception {
		requerimento.setNumRequerimento(gerarNumeroDTRP(requerimento));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumeroDTRP(Requerimento requerimento) throws Exception {
		StringBuilder numeroRequerimento = new StringBuilder();
		
		int contador = 0 ;
		boolean primeiraConsulta = true;	
		
		while(primeiraConsulta || requerimentoService.isExisteNumRequerimento(requerimento)){	
			contador++;
		
			if(!primeiraConsulta){
				numeroRequerimento = new StringBuilder();
			}
			
			numeroRequerimento  
				.append(new SimpleDateFormat("yyyy").format(new Date()))
				.append('.');
			
			numeroRequerimento
				.append(Util.lpad(requerimento.getIdeOrgao().getCodOrgao().toString(), '0', 3))
			    .append('.');
		
			numeroRequerimento 
				.append(getNumByOrgaoByAno(requerimento, contador))
				.append('/');
				
			numeroRequerimento  
				.append(requerimento.getIdeOrgao().getDscSiglaOrgao())
				.append("/DTRP");
			
			primeiraConsulta = false;
			requerimento.setNumRequerimento(numeroRequerimento.toString());
		}
		
		return numeroRequerimento.toString();
	}

}