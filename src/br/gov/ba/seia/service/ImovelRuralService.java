package br.gov.ba.seia.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;
import org.quartz.xml.ValidationException;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.dao.EmpreendimentoDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.ImovelRuralDAOIf;
import br.gov.ba.seia.dao.ImovelRuralDAOImpl;
import br.gov.ba.seia.dao.ValidacaoGeoSeiaDAOIf;
import br.gov.ba.seia.dto.ArquivoAnexarEmailDTO;
import br.gov.ba.seia.dto.ImovelRuralDTO;
import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.AreaProdutiva;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividade;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ContratoConvenioCefir;
import br.gov.ba.seia.entity.CronogramaRecuperacao;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.DocumentoImovelRuralPosse;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoImovel;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralUsoAgua;
import br.gov.ba.seia.entity.ImovelRuralUsoAguaIntervencao;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaPct;
import br.gov.ba.seia.entity.ProcessoTramiteImovelRural;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.ReservaLegalAverbada;
import br.gov.ba.seia.entity.ResponsavelImovelRural;
import br.gov.ba.seia.entity.TipoEstadoConservacao;
import br.gov.ba.seia.entity.TipoSeguimentoPct;
import br.gov.ba.seia.entity.TipoTerritorioPct;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.entity.auditoria.FiltroAuditoria;
import br.gov.ba.seia.entity.auditoria.HistValor;
import br.gov.ba.seia.entity.auditoria.imovel.ImovelRuralQuestionario;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.enumerator.StatusCadastroImovelRuralEnum;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.enumerator.TipoArlEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.enumerator.TipoEnderecoEnum;
import br.gov.ba.seia.enumerator.TipoEstadoConservacaoEnum;
import br.gov.ba.seia.enumerator.TipoUsoAgua;
import br.gov.ba.seia.enumerator.TipologiaCefirEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.exception.AppExceptionError;
import br.gov.ba.seia.exception.AreaDeclaradaInvalidaException;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.exception.CodigoIbgeMunicipioInvalidoException;
import br.gov.ba.seia.exception.ImovelSuspensoException;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.exception.SobreposicaoAreasException;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.auditoria.AuditoriaMaps;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ImovelRuralService {

	private static final ResourceBundle BUNDLE_AUDITORIA = ResourceBundle.getBundle("/auditoria");
	private static final Integer PROCURADOR = 5;
	private final int PROPRIETARIO = 1;
	private final int JUSTO_POSSUIDOR = 2;
	private final String ASSUNTO_EMAIL_AVISO_BNDES = "AVISO IMPORTANTE - CAR/CEFIR";
	private final String EMAIL_COMUNICADO_CEFIR = "comunicado.cefir@inema.ba.gov.br";

	@Inject
	private IDAO<ImovelRural> imovelRuralDAO;
	@Inject
	private IDAO<Imovel> imovelDAO;
	@Inject
	private IDAO<DocumentoImovelRural> documentoImovelRuralDAO;
	@Inject
	private IDAO<ReservaLegal> reservaLegalDAO;
	@Inject
	private IDAO<ReservaLegalAverbada> reservaLegalAverbadaDAO;
	@Inject
	private IDAO<PessoaImovel> pessoaImovelDAO;
	@Inject
	private IDAO<Pessoa> pessoaDAO;
	@Inject
	private IDAO<EmpreendimentoImovel> empreendimentoImovelDAO;
	@Inject
	private IDAO<ProcessoTramiteImovelRural> processoTramiteImovelRuralDAO;
	@Inject
	private ImovelRuralDAOImpl imovelRuralImplDao;
	@Inject
	private IDAO<Object> daoObject;
	@Inject
	private ImovelRuralDAOIf imovelRuralDAOIf;
	@Inject
	private ValidacaoGeoSeiaDAOIf validarGeoSeiaDAO;
	@Inject
	private EmpreendimentoDAOImpl empreendimentoDAOImpl;

	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private AuditoriaFacade auditoriaFacade;
	@EJB
	private ImovelRuralMudancaStatusJustificativaService irMudancaStatusJustificativaService;
	@EJB
	private ModuloFiscalService moduloFiscalService;
	@EJB
	private EnderecoService enderecoService;
	@EJB
	private TipologiaService tipologiaService;
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	@EJB
	private ImovelService imovelService;
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	@EJB
	private ReservaLegalService reservaLegalService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private CertificadoService certificadoService;
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	@EJB
	private TipoTerritorioPctService tipoTerritorioPctService;
	@EJB
	private TipoSeguimentoPctService tipoSeguimentoPctService;
	@EJB
	private PessoaJuridicaPctService pessoaJuridicaPctService;

	public boolean verificarReservaLegal(Integer ideEmpreendimento) {

//		List<ImovelRural> lImovelRural = null;
		BigInteger count = BigInteger.ZERO;

		String sql = "select count(distinct ir.ide_imovel_rural) " + "from empreendimento e "
				+ "     inner join imovel_empreendimento ie on ie.ide_empreendimento = e.ide_empreendimento "
				+ "     inner join imovel_rural ir on ir.ide_imovel_rural = ie.ide_imovel "
				+ "     inner join reserva_legal rl on rl.ide_imovel_rural = ir.ide_imovel_rural " + "where  1=1 "
				+ "     and rl.ide_tipo_arl in(2,3) " + "     and e.ide_empreendimento = " + ideEmpreendimento;
		Exception erro = null;
		try {
//			lImovelRural = imovelRuralDAO.listarClasseComNativeQuery(sql, ImovelRural.class);
			count = imovelRuralDAO.countNativeQuery(sql);
			if (BigInteger.ZERO.compareTo(count) != 0) {
				return true;
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);// log
		} finally {
			if (erro != null)
				throw Util.capturarException(erro, Util.SEIA_EXCEPTION);
		}

		return false;
	}

	public Collection<ImovelRural> filtrarImoveisByItr(String itr) {
		Map<String, Object> paramImovel = new HashMap<String, Object>();
		paramImovel.put("numItr", itr);
		paramImovel.put("indExcluido", Boolean.FALSE);

		return imovelRuralDAO.buscarPorNamedQuery("ImovelRural.findByNumItr", paramImovel);
	}

	/**
	 * Lista imoveis por Municipio, por proprietário do imóvel, por solicitante, por
	 * Nome do imovel(Denominacao), por Número do Certificado e por Número do Token
	 * utilizando o criteriaDemanda.
	 * 
	 * @param dadosParaBusca
	 * @param denominacao
	 * @param certificado
	 * @param token
	 * @param isAtende
	 * @param first
	 * @param pageSize
	 * @return @
	 */
	public List<Imovel> listarPorCriteriaDemanda(ImovelRuralDTO dadosParaBusca, String denominacao, int first,
			int pageSize) {
		String imoveisListadosPorPontos = "";

		if (!Util.isNullOuVazio(dadosParaBusca.getPontosPesquisa())) {
			imoveisListadosPorPontos = daoObject
					.buscarPorNativeQuery("SELECT sp_get_imovel(ARRAY" + dadosParaBusca.getPontosPesquisa() + ")", null)
					.toString().replace("[", "").replace("]", "");
			if (Util.isNullOuVazio(imoveisListadosPorPontos))
				return new ArrayList<Imovel>();
		}

		List<Imovel> listaImoveis = imovelRuralImplDao.listarConsultaPrincipal(dadosParaBusca, denominacao,
				imoveisListadosPorPontos, first, pageSize);

		for (Imovel imovel : listaImoveis) {
			try {
				if (validarGeoSeiaDAO.listarImoveisDebitoRl(imovel.getIdeImovel(), null).size() > 0) {
					imovel.getImovelRural().setCedeAreaParaCompensacaoRl(true);
				}
			} catch (Exception e) {
				imovel.getImovelRural().setCedeAreaParaCompensacaoRl(false);
			}
		}

		return listaImoveis;
	}

	public Integer count(ImovelRuralDTO dadosParaBusca, String denominacao) {

		String imoveisListadosPorPontos = "";

		if (!Util.isNullOuVazio(dadosParaBusca.getPontosPesquisa())) {
			imoveisListadosPorPontos = daoObject
					.buscarPorNativeQuery("SELECT sp_get_imovel(ARRAY" + dadosParaBusca.getPontosPesquisa() + ")", null)
					.toString().replace("[", "").replace("]", "");

			if (Util.isNullOuVazio(imoveisListadosPorPontos))
				return 0;
		}

		return imovelRuralImplDao.countConsultaPrincipal(dadosParaBusca, denominacao, imoveisListadosPorPontos);
	}

	public Integer qtdImoveisPendentesPorRequerente(Integer idePessoa) {
		return imovelRuralImplDao.qtdImoveisPendentesPorRequerente(idePessoa);
	}

	public Collection<Pessoa> listarProprietariosImovel(Integer ideImovel) {
		DetachedCriteria criteria = getCriteriaProprietario(ideImovel);
		criteria.add(Restrictions.eq("tipo.ideTipoVinculoImovel", PROPRIETARIO));

		return this.pessoaDAO.listarPorCriteria(criteria);
	}

	public Collection<Pessoa> listarProprietariosJustoPossuidoresImovel(Integer ideImovel) {
		DetachedCriteria criteria = getCriteriaProprietario(ideImovel);
		Criterion rest1 = Restrictions.eq("tipo.ideTipoVinculoImovel", PROPRIETARIO);
		Criterion rest2 = Restrictions.eq("tipo.ideTipoVinculoImovel", JUSTO_POSSUIDOR);
		criteria.add(Restrictions.or(rest1, rest2));

		return this.pessoaDAO.listarPorCriteria(criteria);

	}

	public Collection<Pessoa> listarProprietariosJustoPossuidoresImovelPorDemanda(Integer ideImovel, Integer first,
			Integer pageSize) {
		DetachedCriteria criteria = getCriteriaProprietario(ideImovel);
		Criterion rest1 = Restrictions.eq("tipo.ideTipoVinculoImovel", PROPRIETARIO);
		Criterion rest2 = Restrictions.eq("tipo.ideTipoVinculoImovel", JUSTO_POSSUIDOR);
		criteria.add(Restrictions.or(rest1, rest2));

		return this.pessoaDAO.listarPorCriteriaDemanda(criteria, first, pageSize);

	}

	public int listarProprietariosJustoPossuidoresImovelCount(Integer ideImovel) {
		DetachedCriteria criteria = getCriteriaProprietario(ideImovel);
		Criterion rest1 = Restrictions.eq("tipo.ideTipoVinculoImovel", PROPRIETARIO);
		Criterion rest2 = Restrictions.eq("tipo.ideTipoVinculoImovel", JUSTO_POSSUIDOR);
		criteria.add(Restrictions.or(rest1, rest2));

		return this.pessoaDAO.count(criteria);

	}

	public ArrayList<Imovel> listarPorRequerimento(Integer ideRequerimento) {
		return imovelRuralImplDao.listarPorRequerimento(ideRequerimento);
	}

	public DetachedCriteria getCriteriaProprietario(Integer ideImovel) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Pessoa.class)
				.createAlias("pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enderecoPessoaCollection", "end", JoinType.LEFT_OUTER_JOIN,
						Restrictions.eq("end.ideTipoEndereco.ideTipoEndereco", TipoEnderecoEnum.RESIDENCIAL.getId()))
				.createAlias("end.ideEndereco", "e", JoinType.LEFT_OUTER_JOIN)
				.createAlias("e.ideLogradouro", "log", JoinType.LEFT_OUTER_JOIN)
				.createAlias("log.ideBairro", "br", JoinType.LEFT_OUTER_JOIN)
				.createAlias("br.ideMunicipio", "m", JoinType.LEFT_OUTER_JOIN)
				.createAlias("m.ideEstado", "est", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pessoaImovelCollection", "pi", JoinType.INNER_JOIN,
						Restrictions.eq("pi.indExcluido", false))
				.createAlias("pi.ideTipoVinculoImovel", "tipo", JoinType.INNER_JOIN);

		criteria.setProjection(Projections.projectionList().add(Projections.property("idePessoa"), "idePessoa")
				.add(Projections.property("pf.idePessoaFisica"), "pessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.numCpf"), "pessoaFisica.numCpf")
				.add(Projections.property("pf.nomPessoa"), "pessoaFisica.nomPessoa")
				.add(Projections.property("pj.idePessoaJuridica"), "pessoaJuridica.idePessoaJuridica")
				.add(Projections.property("pj.numCnpj"), "pessoaJuridica.numCnpj")
				.add(Projections.property("pj.nomRazaoSocial"), "pessoaJuridica.nomRazaoSocial")
				.add(Projections.property("e.numEndereco"), "endereco.numEndereco")
				.add(Projections.property("e.desComplemento"), "endereco.desComplemento")
				.add(Projections.property("log.nomLogradouro"), "endereco.ideLogradouro.nomLogradouro")
				.add(Projections.property("log.numCep"), "endereco.ideLogradouro.numCep")
				.add(Projections.property("br.nomBairro"), "endereco.ideLogradouro.ideBairro.nomBairro")
				.add(Projections.property("m.nomMunicipio"),
						"endereco.ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")
				.add(Projections.property("est.nomEstado"),
						"endereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.nomEstado")

		).setResultTransformer(new AliasToNestedBeanResultTransformer(Pessoa.class));

		criteria.add(Restrictions.eq("pi.ideImovel.ideImovel", ideImovel))
				.add(Restrictions.eq("pi.indExcluido", false));
		return criteria;
	}

	/**
	 * Retorna a lista de pessoas que tem relacionamento com o imóvel, sendo elas
	 * proprietárias.
	 * 
	 * @author Desconhecido, mas quem fez esse comentário javadoc foi Micael
	 *         Coutinho
	 * @param imovel
	 * @return Collection<PessoaImovel> @
	 */
	public Collection<PessoaImovel> filtrarPROPRIETARIOImovel(Imovel imovel) {
		List<Integer> idesTipoVinculo = new ArrayList<Integer>();
		idesTipoVinculo.add(1); // Proprietário
		idesTipoVinculo.add(2); // Justo Possuidor
		idesTipoVinculo.add(6); // Representante de comunidade

		DetachedCriteria criteria = getCriteria(imovel);
		criteria.add(Restrictions.in("tipoVinculo.ideTipoVinculoImovel", idesTipoVinculo));
		criteria.add(Restrictions.eq("indExcluido", false));

		return pessoaImovelDAO.listarPorCriteria(criteria);
	}

	public Collection<PessoaImovel> filtrarPessoasPorImovel(Imovel imovel) {
		DetachedCriteria criteria = getCriteria(imovel);

		return pessoaImovelDAO.listarPorCriteria(criteria);
	}

	private DetachedCriteria getCriteria(Imovel imovel) {
	    DetachedCriteria criteria = DetachedCriteria.forClass(PessoaImovel.class, "pessoaDoImovel");
	    criteria.createAlias("pessoaDoImovel.idePessoa", "pessoa");
	    criteria.createAlias("pessoa.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN);
	    criteria.createAlias("pessoa.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN);
	    criteria.createAlias("pf.idePais", "pais", JoinType.LEFT_OUTER_JOIN);
	    criteria.createAlias("pj.ideNaturezaJuridica", "naturezaJuridica", JoinType.LEFT_OUTER_JOIN);
	    criteria.createAlias("pessoaDoImovel.ideTipoVinculoImovel", "tipoVinculo", JoinType.LEFT_OUTER_JOIN);
	    criteria.createAlias("pessoaDoImovel.ideImovel", "imovel", JoinType.LEFT_OUTER_JOIN);
	    criteria.createAlias("imovel.imovelRural", "imovelrural", JoinType.LEFT_OUTER_JOIN);
	    criteria.createAlias("imovelrural.outrosPassivosAmbientais", "outrosPassivosAmbientais", JoinType.LEFT_OUTER_JOIN,Restrictions.eq("outrosPassivosAmbientais.indExcluido",false));
 
	    if (Util.isNullOuVazio(imovel)) {
	        criteria.add(Restrictions.eq("pessoaDoImovel.ideImovel.ideImovel", null));
	    } else {
	        criteria.add(Restrictions.eq("pessoaDoImovel.ideImovel.ideImovel", imovel.getIdeImovel()));
	    }
	    criteria.add(Restrictions.eq("pessoaDoImovel.indExcluido", false));
	    return criteria;
	}

	/**
	 * @author micael.coutinho Retorna a lista de pessoas que tem relacionamento com
	 *         o imóvel, sendo elas proprietárias ou não.
	 * @param imovel
	 * @return Collection<PessoaImovel> @
	 */
	public List<PessoaImovel> filtrarPessoasProprietariasOuNaoPorImovel(Imovel imovel) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaImovel.class, "pessoaDoImovel");
		criteria.createAlias("pessoaDoImovel.idePessoa", "pessoa");
		criteria.createAlias("pessoaDoImovel.ideTipoVinculoImovel", "tipoVinculoPessoaImovel");
		criteria.createAlias("pessoa.pessoaFisica", "pf", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pessoa.pessoaJuridica", "pj", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pf.idePais", "pais", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("pj.ideNaturezaJuridica", "naturezaJuridica", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("pessoaDoImovel.ideImovel", imovel));
		criteria.add(Restrictions.eq("pessoaDoImovel.indExcluido", false));
		criteria.addOrder(Order.asc("pessoaDoImovel.idePessoaImovel"));

		return pessoaImovelDAO.listarPorCriteria(criteria);
	}

	/**
	 * Carrega o imovelRural e suas dependencias, usando criteria, apenas informando
	 * o id do imovel como parametro.
	 * 
	 * @param id
	 * @return ImovelRural @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRural carregar(Integer id) {
		if (id == -1)
			return new ImovelRural(-1);

		return imovelRuralImplDao.carregarImovelPorId(id);
	}

	public Boolean imovelComRequerimentoAberto(Integer ideImovel) {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT un.ide_requerimento_unico FROM Requerimento_Unico un ");
		lSql.append("INNER JOIN requerimento req on req.ide_requerimento = un.ide_requerimento_unico ");
		lSql.append("INNER JOIN tramitacao_Requerimento tran on tran.ide_requerimento = req.ide_requerimento ");
		lSql.append(
				"INNER JOIN Status_Requerimento status on status.ide_status_requerimento = tran.ide_status_requerimento ");
		lSql.append("INNER JOIN empreendimento_requerimento emp on emp.ide_requerimento = req.ide_requerimento ");
		lSql.append("INNER JOIN imovel_empreendimento ie ON ie.ide_empreendimento = emp.ide_empreendimento ");
		lSql.append("INNER JOIN IMOVEL_RURAL IR ON IR.IDE_IMOVEL_RURAL = IE.IDE_IMOVEL ");
		lSql.append("WHERE req.ind_Excluido = :indExcluido ");

		lSql.append(
				"AND tran.dtc_Movimentacao = (SELECT MAX(req_tran.dtc_Movimentacao) FROM tramitacao_Requerimento req_tran where ide_requerimento = req.ide_requerimento) ");
		lSql.append("AND ie.ide_imovel = :ideImovel ");

		lSql.append(" AND status.ide_Status_Requerimento not in (8, 9)");
		lSql.append(" ORDER BY req.dtc_Criacao");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());

		lQuery.setParameter("indExcluido", Boolean.FALSE);
		lQuery.setParameter("ideImovel", ideImovel);

		@SuppressWarnings("unchecked")
		List<Object[]> result = lQuery.getResultList();

		return !result.isEmpty();
	}

	public Collection<Imovel> carregarImoveisByEmpreendimento(Empreendimento empreendimento) {
		/**
		 * Empreendimento e =
		 * empreendimentoService.carregarById(empreendimento.getIdeEmpreendimento());
		 * Hibernate.initialize(e.getImovelCollection()); return
		 * e.getImovelCollection();
		 **/
		DetachedCriteria criteria = DetachedCriteria.forClass(Imovel.class);
		criteria.createAlias("ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("endereco.ideLogradouro", "logradouro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.ideMunicipio", "municipio", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("logradouro.ideBairro", "bairro", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("imovelRural", "imovelR", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("imovelR.ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("localizacaoGeografica.ideSistemaCoordenada", "sistemaCoordenada",
				JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("localizacaoGeografica.ideClassificacaoSecao", "classificacaoSecao",
				JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("empreendimentoCollection", "emprendimento");
		criteria.add(Restrictions.eq("emprendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));
		Collection<Imovel> imoveis = imovelDAO.listarPorCriteria(criteria);
		return imoveis;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ImovelRural salvarImovelRural(ImovelRural imovelRural) {
		Imovel imovel = imovelRural.getImovel();
		imovel.setDtcCriacao(new Date());

		imovelDAO.salvar(imovel);
		imovelRuralDAO.salvar(imovelRural);

		return imovelRural;
	}

	public void salvarImovelPessoa(PessoaImovel pessoaImovel) {
		salvarImovelPessoaImpl(pessoaImovel);
	}

	public PessoaImovel getSalvarImovelPessoa(PessoaImovel pessoaImovel) {
		pessoaImovel = salvarImovelPessoaImpl(pessoaImovel);
		return pessoaImovel;
	}

	private PessoaImovel salvarImovelPessoaImpl(PessoaImovel pessoaImovel) {
		PessoaImovel pessoaPersist = pessoaImovel;
		pessoaPersist.setDtcCriacao(new Date());

		try {
			if (pessoaPersist != null && !Util.isNullOuVazio(pessoaPersist.getIdePessoa())
					&& pessoaPersist.getIdeImovel() != null && pessoaPersist.getIdeImovel().getIdeImovel() != null) {

				PessoaImovel pImovelJaExistente = buscarPessoaImovelPorPessoaEImovel(pessoaPersist.getIdePessoa(),
						pessoaPersist.getIdeImovel());

				if (Util.isNullOuVazio(pImovelJaExistente)) {
					pessoaImovelDAO.salvarOuAtualizar(pessoaPersist);
				} else {
					pImovelJaExistente.setIdeTipoVinculoImovel(pessoaImovel.getIdeTipoVinculoImovel());
					if (ContextoUtil.getContexto().isPCT()) {
						pImovelJaExistente.setDscTipoVinculoPCTOutros(pessoaImovel.getDscTipoVinculoPCTOutros());
						pImovelJaExistente.setIdeTipoVinculoPCT(pessoaImovel.getIdeTipoVinculoPCT());
					}
					pessoaImovelDAO.salvarOuAtualizar(pImovelJaExistente);
					pessoaImovel = pImovelJaExistente;
				}
			}

			return pessoaImovel;

		} catch (Exception e) {
			System.out.println("Erro ao buscar pessoa de imovel: " + e.toString() + "");
			System.out.println("Tentando inserir uma nova pessoa imóvel.");

			pessoaImovelDAO.salvarOuAtualizar(pessoaPersist);

			return pessoaImovel;
		}
	}

	public void atualizarPessoaImovel(PessoaImovel pessoaImovel) throws Exception {
		if (Util.isNullOuVazio(pessoaImovel.getIdePessoaImovel())) {
			if (!Util.isNullOuVazio(pessoaImovel.getIdePessoa().getIdePessoa())
					&& !Util.isNullOuVazio(pessoaImovel.getIdeImovel().getIdeImovel())) {
				PessoaImovel pessImov = buscarPessoaImovelPorPessoaEImovel(pessoaImovel.getIdePessoa(),
						pessoaImovel.getIdeImovel());
				if (Util.isNullOuVazio(pessImov))
					throw new Exception("Não existe relacionamento entre a pessoa de CPF/CNPJ:"
							+ pessoaImovel.getIdePessoa().getCpfCnpj() + " com o imóvel: "
							+ pessoaImovel.getIdeImovel().getImovelRural().getDesDenominacao());
				else if (pessoaImovel.atributosPessoaEImovelIguais(pessImov)) {
					pessImov.setIdeTipoVinculoImovel(pessoaImovel.getIdeTipoVinculoImovel());
					pessoaImovelDAO.salvarOuAtualizar(pessImov);
				} else {
					throw new Exception("Não encontramos a relação da pessoa informada com o imóvel salvo.");
				}
			}
		}
	}

	public PessoaImovel buscarPessoaImovelPorPessoaEImovel(Pessoa pessoa, Imovel imovel) {

		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaImovel.class, "pessoaImovel")
				.createAlias("pessoaImovel.ideImovel", "imovel", JoinType.INNER_JOIN)
				.createAlias("pessoaImovel.idePessoa", "pessoa", JoinType.INNER_JOIN)
				.add(Restrictions.eq("imovel.ideImovel", imovel.getIdeImovel()))
				.add(Restrictions.eq("pessoa.idePessoa", pessoa.getIdePessoa()))
				.add(Restrictions.eq("pessoaImovel.indExcluido", false))
				.addOrder(Order.desc("pessoaImovel.dtcCriacao"));

		List<PessoaImovel> lista = pessoaImovelDAO.listarPorCriteria(criteria, Order.desc("idePessoaImovel"));

		if (!Util.isNullOuVazio(lista)) {
			return lista.get(0);
		}

		return null;
	}

	public PessoaImovel obterTudoPessoaImovel(Pessoa pessoa, Imovel imovel) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PessoaImovel.class, "pessoaImovel");
		criteria.createAlias("pessoaImovel.ideImovel", "imovel", JoinType.INNER_JOIN);
		criteria.createAlias("pessoaImovel.idePessoa", "pessoa", JoinType.INNER_JOIN);
		criteria.createAlias("pessoaImovel.ideTipoVinculoImovel", "tipoVinculo", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("imovel.ideImovel", imovel.getIdeImovel()));
		criteria.add(Restrictions.eq("pessoa.idePessoa", pessoa.getIdePessoa()));
		criteria.add(Restrictions.eq("pessoaImovel.indExcluido", false));
		criteria.addOrder(Order.desc("pessoaImovel.dtcCriacao"));

		PessoaImovel personImov = null;
		List<PessoaImovel> lista = pessoaImovelDAO.listarPorCriteria(criteria, Order.desc("idePessoaImovel"));
		personImov = lista.get(0);

		return personImov;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ImovelRural imovelRural) {
		localizacaoGeograficaService.sessionFlushAndClear();
		if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica())) {
			localizacaoGeograficaService
					.salvarOuAtualizarLocalizacaoGeografica(imovelRural.getIdeLocalizacaoGeografica());
		}
		/** SALVA A PROCURACAO PARA O IMÓVEL */
		if (!Util.isNullOuVazio(imovelRural.getIdeDocumentoProcuracao())) {
			documentoImovelRuralDAO.salvarOuAtualizar(imovelRural.getIdeDocumentoProcuracao());
			imovelRural.setIdeDocumentoProcuracao(imovelRural.getIdeDocumentoProcuracao());
		}

		if (!Util.isNullOuVazio(imovelRural.getReservaLegal())) {
			reservaLegalDAO.salvarOuAtualizar(imovelRural.getReservaLegal());
		}
		imovelDAO.salvarOuAtualizar(imovelRural.getImovel());
		imovelRuralDAO.salvarOuAtualizar(imovelRural);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTransferencia(ImovelRural imovelRural) {
		// Log4jUtil.log(this.getClass().getSimpleName(), Level.INFO, "Flush e Clear -
		// 622");
		// localizacaoGeograficaService.sessionFlushAndClear();

		/** SALVA A PROCURACAO PARA O IMÓVEL */
		if (!Util.isNullOuVazio(imovelRural.getIdeDocumentoProcuracao())) {
			Log4jUtil.log(this.getClass().getSimpleName(), Level.INFO, "Salvando Documento de Imovel Rural - 627");
			documentoImovelRuralDAO.salvarOuAtualizar(imovelRural.getIdeDocumentoProcuracao());
			imovelRural.setIdeDocumentoProcuracao(imovelRural.getIdeDocumentoProcuracao());
		}
		Log4jUtil.log(this.getClass().getSimpleName(), Level.INFO, "Salvando Imovel - 631");
		imovelDAO.salvarOuAtualizar(imovelRural.getImovel());
		Log4jUtil.log(this.getClass().getSimpleName(), Level.INFO, "Salvando Imovel Rural- 631");
		imovelRuralDAO.salvarOuAtualizar(imovelRural);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarComMerge(ImovelRural imovelRural) {
		if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica())) {
			localizacaoGeograficaService.atualizarLocalizacaoGeografica(imovelRural.getIdeLocalizacaoGeografica());
		}
		/** SALVA A PROCURACAO PARA O IMÓVEL */
		if (!Util.isNullOuVazio(imovelRural.getIdeDocumentoProcuracao())) {
			documentoImovelRuralDAO.salvarOuAtualizar(imovelRural.getIdeDocumentoProcuracao());
			imovelRural.setIdeDocumentoProcuracao(imovelRural.getIdeDocumentoProcuracao());
		}

		if (!Util.isNullOuVazio(imovelRural.getReservaLegal())) {
			imovelRural.setReservaLegal((ReservaLegal) reservaLegalDAO.mergeComRetorno(imovelRural.getReservaLegal()));
		}
		imovelRural.setImovel((Imovel) imovelDAO.mergeComRetorno(imovelRural.getImovel()));
		imovelRural = (ImovelRural) imovelRuralDAO.mergeComRetorno(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarImovelRural(ImovelRural imovelRural) {
		imovelRuralDAO.salvarOuAtualizar(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarComplementoImovel(ImovelRural imovelRural) {

		if (!Util.isNullOuVazio(imovelRural.getProcessoTramiteImovelRuralCollection())) {
			this.desassociarProcessoTramite(imovelRural);
			List<ProcessoTramiteImovelRural> pTramiteRural = (List<ProcessoTramiteImovelRural>) imovelRural
					.getProcessoTramiteImovelRuralCollection();

			for (ProcessoTramiteImovelRural processoTramiteImovelRural : pTramiteRural) {
				processoTramiteImovelRural.setIdeImovelRural(imovelRural);
				processoTramiteImovelRural.setDtcCriacao(new Date());
			}

			processoTramiteImovelRuralDAO.salvarEmLote(pTramiteRural);
		}

		imovelDAO.salvarOuAtualizar(imovelRural.getImovel());
		imovelRuralDAO.salvarOuAtualizar(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desassociarProcessoTramite(ImovelRural pImovelRural) {
		String deleteSQL = "delete from processo_tramite_imovel_rural where ide_imovel_rural = :ideImovelRural";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideImovelRural", pImovelRural.getIdeImovelRural());
		processoTramiteImovelRuralDAO.executarNativeQuery(deleteSQL, params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarSomenteImovelRural(ImovelRural imovelRural) {
		imovelDAO.salvarOuAtualizar(imovelRural.getImovel());
		imovelRuralDAO.salvarOuAtualizar(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPessoaImovel(PessoaImovel pessoaImovel) {
		pessoaImovel.setIndExcluido(true);
		pessoaImovel.setDtcExclusao(new Date());
		pessoaImovelDAO.salvarOuAtualizar(pessoaImovel);
	}

	@SuppressWarnings("null")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Imovel imovel) {

		Imovel lImovel = imovel;
		DocumentoImovelRuralPosse docImovelRuralPosse = null;
		lImovel.setIndExcluido(true);
		lImovel.setDtcExclusao(new Date());

		imovelDAO.salvarOuAtualizar(lImovel);

		if (!Util.isNull(docImovelRuralPosse)) {
			docImovelRuralPosse.setIdeImovelRural(null);

		}

		if (!Util.isNullOuVazio(imovel.getImovelRural())
				&& !Util.isNullOuVazio(imovel.getImovelRural().getIdeDocumentoProcuracao())) {
			removerDocumentoProcuracao(imovel.getImovelRural());
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desassociarImovel(Imovel imovel, Integer ideEmpreendimento) {
		String deleteSQL = "delete from imovel_empreendimento where ide_imovel = :ideImovel AND ide_empreendimento = :ideEmpreendimento";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideImovel", imovel.getIdeImovel());
		params.put("ideEmpreendimento", ideEmpreendimento);
		imovelDAO.executarNativeQuery(deleteSQL, params);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void associarImovel(ImovelRural imovel, Empreendimento empreendimento) {

		boolean imovelExistente = false;

		if (!Util.isNullOuVazio(imovel) && !Util.isNullOuVazio(imovel.getIdeImovelRural())) {

			empreendimento = empreendimentoDAOImpl
					.buscarEmpreendimentoComImovelPessoaELocalizacaoCarregadas(empreendimento);
			empreendimento.setImovelCollection(imovelService.filtrarListaImovelPorEmpreendimento(empreendimento));

			for (Imovel imov : empreendimento.getImovelCollection()) {
				if (imov.getIdeImovel().equals(imovel.getIdeImovelRural()))
					imovelExistente = true;
			}
		}

		if (!imovelExistente) {
			String deleteSQL = "insert into imovel_empreendimento values (:ideImovel, :ideEmpreendimento)";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideImovel", imovel.getIdeImovelRural());
			params.put("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
			imovelDAO.executarNativeQuery(deleteSQL, params);
		}
	}

	public Empreendimento associarEmpreendimentoAoImovel(ImovelRural imovel, Empreendimento empreendimento) {
		boolean imovelExistente = false;
		Empreendimento emp = empreendimentoDAOImpl
				.buscarEmpreendimentoComImovelPessoaELocalizacaoCarregadas(empreendimento);
		emp.setImovelCollection(imovelService.filtrarListaImovelPorEmpreendimento(empreendimento));

		for (Imovel imov : emp.getImovelCollection()) {
			if (!Util.isNullOuVazio(imov) && imov.getIdeImovel().equals(imovel.getIdeImovelRural())) {
				imovelExistente = true;
			}
		}
		if (!imovelExistente) {
			String insertSQL = "insert into imovel_empreendimento values (:ideImovel, :ideEmpreendimento)";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideImovel", imovel.getIdeImovelRural());
			params.put("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
			imovelDAO.executarNativeQuery(insertSQL, params);
		}
		return emp;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarImovelEmpreendimento(EmpreendimentoImovel empreendimentoImovel) {
		empreendimentoImovelDAO.salvar(empreendimentoImovel);
	}

	public List<ImovelRural> listarImoveisPorItrAndEmpreendimento(String itr, Integer empreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRural.class, "imovelRural");
		criteria.createAlias("imovelRural.imovel", "imovel");
		criteria.createAlias("imovel.empreendimentoCollection", "empreendimento", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("imovel.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("imovelRural.ideLocalizacaoGeografica", "localizacao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("numItr", itr));
		criteria.add(Restrictions.eq("imovel.indExcluido", false));
		criteria.addOrder(Order.asc("desDenominacao"));
		List<ImovelRural> lista = imovelRuralDAO.listarPorCriteria(criteria);

		if (Util.isNullOuVazio(lista)) {
			return new ArrayList<ImovelRural>();
		} else {
			return lista;
		}
	}

	/**
	 * 
	 * Valida o imovel rural de acordo com a regra: ZCR/RN0027 @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean isImovelRuralValido(ImovelRural imovelRural) {

		if (Util.isNullOuVazio(imovelRural)) {
			JsfUtil.addErrorMessage("Imóvel nulo");
			return false;
		}

		if (Util.isNullOuVazio(imovelRural.getNumItr()) && !imovelRural.isMenorQueQuatroModulosFiscais()) {
			JsfUtil.addErrorMessage("Imóvel sem número de itr");
			return false;
		}

		if (Util.isNullOuVazio(imovelRural.getDesDenominacao())) {
			JsfUtil.addErrorMessage("Imóvel sem denominação");
			return false;
		}

		imovelRural.getImovel().setPessoaImovelCollection(filtrarPessoasPorImovel(imovelRural.getImovel()));

		if (imovelRural.getImovel().getPessoaImovelCollection().size() < 1) {
			JsfUtil.addErrorMessage("Imóvel sem pessoa relacionada");
			return false;
		}

		if (Util.isNullOuVazio(imovelRural.getValArea())) {
			JsfUtil.addErrorMessage("Imóvel sem valor de área");
			return false;
		}

		if (Util.isNullOuVazio(imovelRural.getNumFolha()) && !imovelRural.isMenorQueQuatroModulosFiscais()) {
			JsfUtil.addErrorMessage("Imóvel sem número de folha");
			return false;
		}

		if (Util.isNullOuVazio(imovelRural.getDesLivro()) && !imovelRural.isMenorQueQuatroModulosFiscais()) {
			JsfUtil.addErrorMessage("Imóvel sem descrição de livro");
			return false;
		}

		if (Util.isNullOuVazio(imovelRural.getDesCartorio()) && !imovelRural.isMenorQueQuatroModulosFiscais()) {
			JsfUtil.addErrorMessage("Imóvel sem descrição cartório");
			return false;
		}

		if (Util.isNullOuVazio(imovelRural.getDesComarca()) && !imovelRural.isMenorQueQuatroModulosFiscais()) {
			JsfUtil.addErrorMessage("Imóvel sem comarca");
			return false;
		}

		if (Util.isNullOuVazio(imovelRural.getNumMatricula()) && Util.isNullOuVazio(imovelRural.getNumRegistro())
				&& !imovelRural.isMenorQueQuatroModulosFiscais()) {
			JsfUtil.addErrorMessage("Imóvel sem número de matrícula e sem número de registro ");
			return false;
		}

		if (Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco())) {
			JsfUtil.addErrorMessage("Imóvel sem endereço");
			return false;
		}
		return true;

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRural> carregarTipoReservaImovelByRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRural.class, "imovelRural")
				.createAlias("imovelRural.imovel", "imovel").createAlias("imovel.empreendimentoCollection", "emp")
				.createAlias("emp.empreendimentoRequerimentoCollection", "empreq")
				.createAlias("empreq.empreendimentoCollection", "emp")
				.createAlias("imovelRural.reservaLegalAverbadaCollection", "reserva")
				.createAlias("reserva.ideTipoArl", "tipo");

		criteria.setProjection(
				Projections.projectionList().add(Projections.property("ideImovelRural"), "ideImovelRural")
						.add(Projections.property("tipo.ideTipoArl"), "reservaLegalAverbada.ideTipoArl.ideTipoArl"));

		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));

		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(ImovelRural.class));

		return imovelRuralDAO.listarPorCriteria(criteria);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDocumento(ImovelRural imovelRural) {

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDocumentoAprovacaoRl(ReservaLegal reservaLegal) {
		DocumentoImovelRural arqExcluido = reservaLegal.getIdeDocumentoAprovacao();
		reservaLegal.setIdeDocumentoAprovacao(null);
		reservaLegalDAO.atualizar(reservaLegal);
		arqExcluido.setImovelRural(null);
		documentoImovelRuralDAO.remover(arqExcluido);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDocumentoAverbacaoRl(ReservaLegalAverbada reservaLegalAverbada) {
		DocumentoImovelRural arqExcluido = reservaLegalAverbada.getIdeDocumentoAverbacao();
		reservaLegalAverbada.setIdeDocumentoAverbacao(null);
		reservaLegalAverbadaDAO.atualizar(reservaLegalAverbada);
		arqExcluido.setImovelRural(null);
		documentoImovelRuralDAO.remover(arqExcluido);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDocumentoProcuracao(ImovelRural imovelRural) {
		DocumentoImovelRural arqExcluido = imovelRural.getIdeDocumentoProcuracao();
		imovelRural.setIdeDocumentoProcuracao(null);
		imovelRuralDAO.salvarOuAtualizar(imovelRural);
		arqExcluido.setImovelRural(null);
		documentoImovelRuralDAO.remover(arqExcluido);
	}

	public List<ImovelRural> listarImovelRuralByProprietario(Pessoa proprietario, int first, int pageSize) {
		return imovelRuralImplDao.listarImovelRuralByProprietario(proprietario, first, pageSize);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRural carregarById(Integer id) {
		ImovelRural imov;
		imov = imovelRuralDAO.carregarGet(id);
		if (!Util.isNullOuVazio(imov)) {
			Hibernate.initialize(imov.getResponsavelImovelRuralCollection());
		}
		return imov;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int countListarImovelByProprietario(Pessoa donoEmpreendimento) {
		Exception erro = null;
		try {
			return imovelRuralImplDao.countListarImovelByProprietario(donoEmpreendimento);
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);// log
			return 0;
		} finally {
			if (erro != null)
				throw Util.capturarException(erro, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRural carregarImovelRural(Integer ideImovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRural.class, "imovelRural");
		criteria.createAlias("imovelRural.imovel", "imovel");
		criteria.createAlias("imovel.empreendimentoCollection", "empreendimento", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("imovel.ideEndereco", "endereco", JoinType.LEFT_OUTER_JOIN)
				.createAlias("endereco.ideLogradouro", "log", JoinType.INNER_JOIN)
				.createAlias("log.ideBairro", "br", JoinType.INNER_JOIN)
				.createAlias("log.ideTipoLogradouro", "tp", JoinType.INNER_JOIN)
				.createAlias("br.ideMunicipio", "m", JoinType.INNER_JOIN)
				.createAlias("m.ideEstado", "est", JoinType.INNER_JOIN);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections.property("ideImovelRural")), "ideImovelRural")
				.add(Projections.property("desDenominacao"), "desDenominacao")
				.add(Projections.property("numMatricula"), "numMatricula")
				.add(Projections.property("numRegistro"), "numRegistro").add(Projections.property("numItr"), "numItr")
				.add(Projections.property("desCartorio"), "desCartorio")

				.add(Projections.property("endereco.numEndereco"), "imovel.ideEndereco.numEndereco")
				.add(Projections.property("endereco.desComplemento"), "imovel.ideEndereco.desComplemento")
				.add(Projections.property("log.nomLogradouro"), "imovel.ideEndereco.ideLogradouro.nomLogradouro")
				.add(Projections.property("log.numCep"), "imovel.ideEndereco.ideLogradouro.numCep")
				.add(Projections.property("tp.nomTipoLogradouro"),
						"imovel.ideEndereco.ideLogradouro.ideTipoLogradouro.nomTipoLogradouro")
				.add(Projections.property("br.nomBairro"), "imovel.ideEndereco.ideLogradouro.ideBairro.nomBairro")
				.add(Projections.property("m.nomMunicipio"),
						"imovel.ideEndereco.ideLogradouro.ideBairro.ideMunicipio.nomMunicipio")
				.add(Projections.property("est.nomEstado"),
						"imovel.ideEndereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.nomEstado")
				.add(Projections.property("est.desSigla"),
						"imovel.ideEndereco.ideLogradouro.ideBairro.ideMunicipio.ideEstado.desSigla")

		).setResultTransformer(new AliasToNestedBeanResultTransformer(ImovelRural.class));

		criteria.add(Restrictions.eq("imovelRural.ideImovelRural", ideImovelRural));

		return this.imovelRuralDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa buscarJustoPossuidor(Integer ideImovelRural) {
		DetachedCriteria criteria = getCriteriaProprietario(ideImovelRural);
		criteria.add(Restrictions.eq("tipo.ideTipoVinculoImovel", JUSTO_POSSUIDOR));

		return this.pessoaDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralQuestionario carregarQuestionario(ImovelRural pImovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRural.class);
		criteria.add(Restrictions.eq("ideImovelRural", pImovelRural.getIdeImovelRural()));
		ImovelRural lImovelRural = imovelRuralDAO.buscarPorCriteria(criteria);
		ImovelRuralQuestionario lIRQ = new ImovelRuralQuestionario();
		lIRQ.setDesCartorio(lImovelRural.getDesCartorio());
		lIRQ.setDesComarca(lImovelRural.getDesComarca());
		lIRQ.setDesDenominacao(lImovelRural.getDesDenominacao());
		lIRQ.setDesLivro(lImovelRural.getDesLivro());
		lIRQ.setNumCcir(lImovelRural.getNumCcir());
		lIRQ.setNumFolha(lImovelRural.getNumFolha());
		lIRQ.setNumItr(lImovelRural.getNumItr());
		lIRQ.setNumMatricula(lImovelRural.getNumMatricula());
		lIRQ.setValArea(lImovelRural.getValArea());
		return lIRQ;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRural carregarTudo(ImovelRural pImovelRural) throws CampoObrigatorioException {
		if (Util.isNullOuVazio(pImovelRural)) {
			throw new CampoObrigatorioException("Imóvel rural null");
		} else if (Util.isNullOuVazio(pImovelRural.getIdeImovelRural())) {
			throw new CampoObrigatorioException("É necessário passar o id do imóvel");
		}
		ImovelRural imovel = imovelRuralDAOIf.carregarImovelPorId(pImovelRural.getIdeImovelRural());
		List<ImovelRuralUsoAgua> imovelUsoAguaCollection = new ArrayList<ImovelRuralUsoAgua>();
		for (ImovelRuralUsoAgua imovelUsoAgua : imovel.getImovelRuralUsoAguaCollection()) {
			if (!imovelUsoAgua.isIndExcluido()) {
				imovelUsoAguaCollection.add(imovelUsoAgua);
			}
		}
		imovel.setImovelRuralUsoAguaCollection(imovelUsoAguaCollection);
		return imovel;
	}

	// JOÃO BATISTA JOSÉ DE BARROS -- 325.234.245-68
	public void invalidarCertificado(ImovelRural imovelRural) {
		if (!imovelRural.getIdeImovelRural()
				.equals(14458)) { /**
									 * - Solução paliativa para atender solicitação de Maria Daniela de alteração do
									 * requerente do imóvel SÍTIO BOA ESPERANÇA
									 */
			imovelRural.setStsCertificado(null);
			imovelRural.setStatusCadastro(StatusCadastroImovelRuralEnum.REGISTRO_INCOMPLETO.getId());
			imovelRural.setPrazoValidade(null);
		}
		atualizar(imovelRural);
	}

	public List<FiltroAuditoria> filtrarHistorico(Date dataInicio, Date dataFim, ImovelRural ideImovelRural, int first,
			int pageSize) throws NumberFormatException, Exception {
		ideImovelRural = carregarTudo(ideImovelRural);

		Integer IdeRegistroModificacoStatus = irMudancaStatusJustificativaService
				.buscarIdeStatusPorImovelRural(ideImovelRural);

		List<FiltroAuditoria> filtroHistorico = imovelRuralImplDao.filtrarHistorico(dataInicio, dataFim, ideImovelRural,
				first, pageSize, IdeRegistroModificacoStatus);
		List<FiltroAuditoria> filtroHistoricoNovo = new ArrayList<FiltroAuditoria>();
		for (FiltroAuditoria filtroAuditoria : filtroHistorico) {
			String key = filtroAuditoria.getCampo().getIdeTabela().getNomeTabela().replace("_", "") + "."
					+ filtroAuditoria.getCampo().getNomeCampo();
			if (BUNDLE_AUDITORIA.containsKey(key)) {
				filtroAuditoria.getCampo().setNomeCampo(BUNDLE_AUDITORIA.getString(key));
				prepararValoresAmigaveis(filtroAuditoria);
				filtroHistoricoNovo.add(filtroAuditoria);
			}
		}
		return filtroHistoricoNovo;
	}

	private void prepararValoresAmigaveis(FiltroAuditoria filtroAuditoria) throws NumberFormatException, Exception {
		prepararBooleans(filtroAuditoria.getValorAntigo());
		if (!filtroAuditoria.getValorAntigo().getValor().trim().equals("Não informado")) {
			prepararValoresAmigaveis(filtroAuditoria.getValorAntigo(), filtroAuditoria.getCampo().getNomeCampo());
		}

		prepararBooleans(filtroAuditoria.getValorNovo());
		if (!filtroAuditoria.getValorNovo().getValor().trim().equals("Não informado")) {
			prepararValoresAmigaveis(filtroAuditoria.getValorNovo(), filtroAuditoria.getCampo().getNomeCampo());
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param filtroAuditoria
	 * @since 22/07/2015
	 */
	private void prepararBooleans(HistValor histValor) {
		if (!Util.isNullOuVazio(histValor.getValor())) {
			if (histValor.getValor().trim().equals("null")) {
				histValor.setValor("Não informado");
			} else if (histValor.getValor().trim().equals("true")) {
				histValor.setValor("Sim");
			} else if (histValor.getValor().trim().equals("false")) {
				histValor.setValor("Não");
			}
		} else {
			histValor.setValor("Não informado");
		}
	}

	private String obterNomeRequerente(HistValor valor) {
		PessoaFisica pessoaFisica = null;
		PessoaJuridica pessoaJuridica = null;
		try {
			pessoaFisica = pessoaFisicaService.carregarPessoaFisicaGet(Integer.valueOf(valor.getValor().trim()));
			pessoaJuridica = pessoaJuridicaService.buscarPessoaJuridicaByIde(Integer.valueOf(valor.getValor().trim()));
			if (!Util.isNull(pessoaFisica)) {
				return pessoaFisica.getNomPessoa();
			}

			if (!Util.isNull(pessoaJuridica)) {
				return pessoaJuridica.getNomRazaoSocial();
			}
		} catch (NumberFormatException e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}

		return "Não informado";
	}

	private void prepararValoresAmigaveis(HistValor histValor, String nomCampo)
			throws NumberFormatException, Exception {
		if (nomCampo.equals("Forma do vínculo com o titular")) {
			histValor.setValor(AuditoriaMaps.tipoVinculoLabel.get(Integer.valueOf(histValor.getValor().trim())));
		} else if (nomCampo.equals("Tipo de Inserção")) {
			histValor.setValor(AuditoriaMaps.tipoInsercaoLabel.get(histValor.getValor().trim()));
		} else if (nomCampo.equals("Sistema de Coordenada/Referência Espacial")) {
			histValor.setValor(AuditoriaMaps.sistemaCoordenadaLabel.get(histValor.getValor().trim()));
		} else if (nomCampo.equals("Informe o Tipo de Reserva legal (regularizada ou pretendida)")) {
			histValor.setValor(AuditoriaMaps.tipoReservaLabel.get(histValor.getValor().trim()));
		} else if (nomCampo.equals("Estado de conservação")) {
			histValor.setValor(AuditoriaMaps.estadoDeConservacaoLabel.get(histValor.getValor().trim()));
		} else if (nomCampo.equals("Atividade")) {
			histValor.setValor(AuditoriaMaps.tipoRecuperacaoLabel.get(histValor.getValor().trim()));
		} else if (nomCampo.equals("Origem do certificado")) {
			histValor.setValor(AuditoriaMaps.tipoOrigemCertificadoLabel.get(histValor.getValor().trim()));
		} else if (nomCampo.equals("Informe o Tipo de Área de Preservação Permanente")) {
			histValor.setValor(AuditoriaMaps.tipoAreaPreservacaoPermanenteLabel.get(histValor.getValor().trim()));
		} else if (nomCampo.equals("Informe o Tipo de Produção")) {
			histValor.setValor(AuditoriaMaps.tipoDeProducaoLabel.get(histValor.getValor().trim()));
		} else if (nomCampo.equals("Status do imóvel rural")) {
			histValor.setValor(AuditoriaMaps.statusImovelLabel.get(Integer.valueOf(histValor.getValor().trim())));
		} else if (nomCampo.equals("Tipo uso")) {
			histValor.setValor(AuditoriaMaps.tipoUsoAguaLabel.get(Integer.valueOf(histValor.getValor().trim())));
		} else if (nomCampo.equals("Status reserva legal")) {
			histValor.setValor(AuditoriaMaps.statusReservaLegalLabel.get(Integer.valueOf(histValor.getValor().trim())));
		} else if (nomCampo.equals("Requerente")) {
			histValor.setValor(obterNomeRequerente(histValor));
		} else if (nomCampo.compareTo("Status do Bloqueio") == 0) {
			valorAmigavelParaStatusReservaLegalBloqueio(histValor);
		} else if (nomCampo.equals("Usuário que suspendeu o imóvel")) {
			histValor.setValor(obterNomeRequerente(histValor));
		} else if (nomCampo.equals("Tipo de vinculo/domínio")) {
			histValor.setValor(obterNomePctTipoViculo(histValor));
		} else if (nomCampo.equals("Tipos de segmentos do PCT")) {
			histValor.setValor(obterNomePctSegmento(histValor));
		} else if (nomCampo.equals("CNPJ e Razão Social das associações da comunidade")) {
			histValor.setValor(obterCNPJRazaoSocialPct(histValor));
		}
	}

	private String obterCNPJRazaoSocialPct(HistValor histValor) throws NumberFormatException, Exception {
	    String[] valores = converterValorEmArray(histValor.getValor());

	    List<String> vlr = new ArrayList<String>();

	    for (String valor : valores) {
	        if (valor.equals("null") || valor.equals(" null") ) {
	            vlr.add("Não informado");
	        } else {
	            PessoaJuridicaPct pessoaJuridicaPct = pessoaJuridicaPctService.obterPessoaJuridicaPct(Integer.valueOf(valor.trim()));
	            vlr.add(pessoaJuridicaPct.getIdePessoaJuridica().getNumCnpj() + " - " + pessoaJuridicaPct.getIdePessoaJuridica().getNomRazaoSocial());
	        }
	    }
	    return vlr.toString();

	}

	private String obterNomePctTipoViculo(HistValor histValor) throws NumberFormatException, Exception {
		TipoTerritorioPct tipoTerritorioPct = tipoTerritorioPctService
				.obterTipoTerritorioPct(Integer.valueOf(histValor.getValor().trim()));
		return tipoTerritorioPct.getDscTipoTerritorioPct();
	}

	private String[] converterValorEmArray(String valor) {
		valor = valor.replace("[", "");
		valor = valor.replace("]", "");
		return valor.split(",");
	}

	private String obterNomePctSegmento(HistValor histValor) throws NumberFormatException, Exception {
		String[] valores = converterValorEmArray(histValor.getValor());

		List<String> vlr = new ArrayList<String>();
		for (String valor : valores) {
			TipoSeguimentoPct tipoSeguimentoPct = tipoSeguimentoPctService
					.obterTipoSeguimentoPct(Integer.valueOf(valor.trim()));
			vlr.add(tipoSeguimentoPct.getDscTipoSeguimentoPct());
		}
		return vlr.toString();
	}

	/**
	 * Método para exibir os Status do bloqueio da Reserva Legal.
	 * 
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param filtroAuditoria
	 * @since 22/07/2015
	 */
	private void valorAmigavelParaStatusReservaLegalBloqueio(HistValor histValor) {
		if (histValor.getValor().compareTo("Sim") == 0) {
			histValor.setValor("Em Análise");
		} else if (histValor.getValor().compareTo("Não") == 0) {
			histValor.setValor("Liberado");
		}
	}

	public List<ImovelRural> imovelSemPermissaoASV(Empreendimento empreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRural.class).createAlias("imovel", "imovel")
				.createAlias("imovel.empreendimentoCollection", "emp").add(Restrictions.eq("indPermissaoASV", false))
				.add(Restrictions.eq("emp.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));

		return imovelRuralDAO.listarPorCriteria(criteria);
	}

	public List<Imovel> listarImoveisRuraisComInformacoesIguais(ImovelRural imovelRural) {
		return imovelRuralImplDao.listarImoveisRuraisComInformacoesIguais(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRural> imoveisEmCompensacaoDeReserva(Integer ideImovelRural) {
		return imovelRuralImplDao.imoveisEmCompensacaoDeReserva(ideImovelRural);
	}

	public void excluirSupressaoVegetacaoPorImovelRural(Integer ideImovelRural) {
		imovelRuralImplDao.excluirSupressaoVegetacaoPorImovelRural(ideImovelRural);
	}

	public void validarVinculoComImovel(Pessoa pessoa) throws AppExceptionError {
		// Requerente, proprietário, responsável técnico, assentado
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT count(*) ");
		lSql.append("FROM imovel i ");
		lSql.append("INNER JOIN imovel_rural ir ON (i.ide_imovel = ir.ide_imovel_rural) ");
		lSql.append("INNER JOIN pessoa_imovel pi ON (ir.ide_imovel_rural = pi.ide_imovel) ");
		lSql.append("LEFT JOIN responsavel_imovel_rural ri ON (ir.ide_imovel_rural = ri.ide_imovel_rural) ");
		lSql.append("WHERE i.ind_excluido = false AND pi.ind_excluido = false  AND ri.ind_excluido = false AND ");
		lSql.append("	   (ide_requerente_cadastro = :idePessoa OR ");
		lSql.append("	    pi.ide_pessoa 	        = :idePessoa OR ");
		lSql.append("	    ri.ide_pessoa_fisica    = :idePessoa)  ");

		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();

		Query lQuery = lEntityManager.createNativeQuery(lSql.toString());
		lQuery.setParameter("idePessoa", pessoa.getIdePessoa());

		BigInteger result = (BigInteger) lQuery.getSingleResult();

		if (result != null && result.intValue() > 0) {
			throw new AppExceptionError("Esse registro não pode ser excluído.");
		}
	}

	public void excluirImovelRuralTacPorImovelRural(Integer ideImovelRural) {
		imovelRuralImplDao.excluirImovelRuralTacPorImovelRural(ideImovelRural);
	}

	public void excluirImovelRuralPradPorImovelRural(Integer ideImovelRural) {
		imovelRuralImplDao.excluirImovelRuralPradPorImovelRural(ideImovelRural);
	}

	public TipoVinculoImovel carregarTipoVinculoImovel(ImovelRural imovelRural) {
		try {
			if (!Util.isNullOuVazio(imovelRural)) {
				Collection<PessoaImovel> proprietarios = filtrarPessoasPorImovel(imovelRural.getImovel());
				for (PessoaImovel pessoaImovel : proprietarios) {
					if (pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel()
							.equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)) {
						return new TipoVinculoImovel(2, "Justo Possuidor");
					}
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		}
		return new TipoVinculoImovel(1, "Proprietário");
	}

	public ImovelRural montarGeracaoCertificadoImovelRural(ImovelRural imovelRural) throws Exception {
		// Verificação de emissão de Certificado ou Termo de Compromisso
		boolean precisaOutorga = false;
		boolean precisaLicencaAp = false;
		boolean temPraRl = false;
		boolean temPraApp = false;
		boolean temPradOP = false;
		boolean temJustoPossuidor = false;

		TipoVinculoImovel tipoVinculoImovel = carregarTipoVinculoImovel(imovelRural);

		if (tipoVinculoImovel.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)) {
			temJustoPossuidor = true;
		}

		precisaOutorga = precisaDeOutorga(imovelRural);

		precisaLicencaAp = precisaDeLicencaAreaProdutiva(imovelRural);

		if (!Util.isNullOuVazio(imovelRural.getReservaLegal())) {

			temPraRl = possuiPraRl(imovelRural);
		}

		temPraApp = possuiPraApp(imovelRural);

		temPradOP = possuiPradOP(imovelRural);

		if (precisaOutorga || precisaLicencaAp || /* precisaLicencaVn || */ temPraRl || temPraApp || temPradOP
				|| temJustoPossuidor) { // verifica se existe passivo ambiental
			imovelRural.setStsCertificado(false);
		} else {
			imovelRural.setStsCertificado(true);
		}

		if (imovelRural.isImovelPCT()) {
			imovelRural.setStsCertificado(true);
		}

		// Prazo de 2 anos de validade tanto para o Termo quanto para o Certificado
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, 730);
		imovelRural.setPrazoValidade(calendar.getTime());

		imovelRural.setIndPrecisaOutorga(precisaOutorga);
		imovelRural.setIndTemPrad(temPraRl || temPraApp);
		return imovelRural;
	}

	public boolean possuiPradOP(ImovelRural imovelRural) {
		boolean temPraOP = false;
		if (!Util.isNullOuVazio(imovelRural.getIndOutrosPassivos()) && imovelRural.getIndOutrosPassivos()) {
			temPraOP = true;
		}
		return temPraOP;
	}

	public boolean possuiArquivoPradOP(ImovelRural imovelRural) {
		boolean temPraOP = false;
		if (imovelRural.getIndOutrosPassivos()
				&& !Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao())
				&& !Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao()
						.getIdeDocumentoObrigatorio())
				&& !Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao()
						.getIdeDocumentoObrigatorio().getDscCaminhoArquivo())) {
			temPraOP = true;
		}
		return temPraOP;
	}

	private boolean isEstadoConservacaoNecessitaPrad(TipoEstadoConservacao tipoEstadoConservacao) {
		return !Util.isNullOuVazio(tipoEstadoConservacao) && (tipoEstadoConservacao.getIdeTipoEstadoConservacao()
				.equals(TipoEstadoConservacaoEnum.DEGRADADA.getId())
				|| tipoEstadoConservacao.getIdeTipoEstadoConservacao()
						.equals(TipoEstadoConservacaoEnum.PARCIALMENTE_DEGRADADA.getId()));
	}

	public boolean possuiPraRl(ImovelRural imovelRural) {
		return isEstadoConservacaoNecessitaPrad(imovelRural.getReservaLegal().getIdeTipoEstadoConservacao());
	}

	public boolean possuiArquivoPraRl(ImovelRural imovelRural) {
		boolean temPradRl = false;
		if (isEstadoConservacaoNecessitaPrad(imovelRural.getReservaLegal().getIdeTipoEstadoConservacao())
				&& isCronogramaRecuperacaoComDocumento(imovelRural.getReservaLegal().getCronogramaRecuperacao())) {
			temPradRl = true;
		}
		return temPradRl;
	}

	private boolean isCronogramaRecuperacaoComDocumento(CronogramaRecuperacao cronogramaRecuperacao) {
		return !Util.isNullOuVazio(cronogramaRecuperacao)
				&& !Util.isNullOuVazio(cronogramaRecuperacao.getIdeDocumentoObrigatorio())
				&& !Util.isNullOuVazio(cronogramaRecuperacao.getIdeDocumentoObrigatorio().getDscCaminhoArquivo());
	}

	public boolean possuiPraApp(ImovelRural imovelRural) {
		if (imovelRural.getIndApp()) {
			for (App app : imovelRural.getAppCollection()) {
				if (isEstadoConservacaoNecessitaPrad(app.getIdeTipoEstadoConservacao())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean possuiArquivoPraApp(ImovelRural imovelRural) {
		if (imovelRural.getIndApp()) {
			for (App app : imovelRural.getAppCollection()) {
				if (isEstadoConservacaoNecessitaPrad(app.getIdeTipoEstadoConservacao())
						&& isCronogramaRecuperacaoComDocumento(app.getCronogramaRecuperacao())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean precisaDeLicencaAreaProdutiva(ImovelRural imovelRural) throws Exception {
		boolean precisaLicencaAp = false;
		if (!Util.isNullOuVazio(imovelRural.getIndAreaProdutiva()) && imovelRural.getIndAreaProdutiva()) { // Se ele
																											// informou
																											// area
																											// produtiva
																											// (verificar
																											// todas as
																											// areas de
																											// acordo
																											// com o
																											// anexo)
			if (!Util.isNullOuVazio(imovelRural.getAreaProdutivaCollection())) {

				for (AreaProdutiva ap : imovelRural.getAreaProdutivaCollection()) {
					Integer idTipologia = null;
					Integer idTipologiaSubGrupo = null;
					if (ap.possuiTipologiaCadastrada()) {
						idTipologia = ap.getIdeTipologia().getIdeTipologia();
					}

					if (ap.possuiTipologiaSubGrupoCadastrada()) {
						idTipologiaSubGrupo = ap.getIdeTipologiaSubgrupo().getIdeTipologia();
					}

					if (!Util.isNullOuVazio(idTipologia)
							&& idTipologia.equals(TipologiaCefirEnum.CRIACAO_DE_ANIMAIS.getId())) {
						// CRIAÇÕES CONFINADAS
						if (!Util.isNullOuVazio(idTipologiaSubGrupo)
								&& idTipologiaSubGrupo.equals(TipologiaCefirEnum.CRIACOES_CONFINADAS.getId())) {
							// PESQUISAR APTA PARA CONTABILIZAR QTD DE CABEÇA
							List<AreaProdutivaTipologiaAtividade> apta = new ArrayList<AreaProdutivaTipologiaAtividade>();
							apta = (List<AreaProdutivaTipologiaAtividade>) tipologiaService
									.carregarTipologiaAtividadeByAreaProdutiva(ap);

							for (AreaProdutivaTipologiaAtividade ta : apta) {
								TipologiaCefirEnum tipologiaAtividadeEnum = TipologiaCefirEnum
										.obterTipologiaEnum(ta.getIdeTipologiaAtividade().getIdeTipologiaAtividade());
								if (!Util.isNull(tipologiaAtividadeEnum)) {
									switch (tipologiaAtividadeEnum) {
									// Bovino, bubalino, muares e equínos
									case BOVINOS_BUBALINOS_MUARES_EQUINOS_CRIACAO_CONFINADA:
										if (ta.getIdeAreaProdutivaTipologiaAtividadeAnimal().getQtdCabeca() >= 50
												&& (Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
											precisaLicencaAp = true;
										}
										break;
									// Aves e pequenos mamíferos
									case AVES_PEQUENOS_MAMIFEROS_CRIACAO_CONFINADA:
										if (ta.getIdeAreaProdutivaTipologiaAtividadeAnimal().getQtdCabeca() >= 12000
												&& (Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
											precisaLicencaAp = true;
										}
										break;
									// Caprino e ovinos
									case CAPRINOS_OVINOS_CRIACAO_CONFINADA:
										if (ta.getIdeAreaProdutivaTipologiaAtividadeAnimal().getQtdCabeca() >= 500
												&& (Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
											precisaLicencaAp = true;
										}
										break;
									// Suínos
									case SUINOS_CRIACAO_CONFINADA:
										if (ta.getIdeAreaProdutivaTipologiaAtividadeAnimal().getQtdCabeca() >= 300
												&& (Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
											precisaLicencaAp = true;
										}
										break;
									// Creche de suínos
									case CRECHE_SUINOS_CRIACAO_CONFINADA:
										if (ta.getIdeAreaProdutivaTipologiaAtividadeAnimal().getQtdCabeca() >= 1000
												&& (Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
											precisaLicencaAp = true;
										}
										break;
									default:
										break;
									}
								}

							}
						} else if (!Util.isNullOuVazio(idTipologiaSubGrupo)
								&& idTipologiaSubGrupo.equals(TipologiaCefirEnum.AQUICULTURA.getId())) {
							List<AreaProdutivaTipologiaAtividade> apta = new ArrayList<AreaProdutivaTipologiaAtividade>();
							apta = (List<AreaProdutivaTipologiaAtividade>) tipologiaService
									.carregarTipologiaAtividadeByAreaProdutiva(ap);

							for (AreaProdutivaTipologiaAtividade ta : apta) {
								TipologiaCefirEnum tipologiaAtividadeEnum = TipologiaCefirEnum
										.obterTipologiaEnum(ta.getIdeTipologiaAtividade().getIdeTipologiaAtividade());
								if (!Util.isNull(tipologiaAtividadeEnum)) {
									switch (tipologiaAtividadeEnum) {
									// 107 - Piscicultura continental em tanques-rede
									case PSICULTURA_CONTINENTAL_TANQUE_REDE_RACEWAY_SIMILAR:
										if ((Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
											precisaLicencaAp = true;
										}
										break;
									// 17 - Piscicultura intensiva em viveiros escavados
									case PSICULTURA_INTENSIVA_EM_VIVEIROS_ESCAVADOS:
										if (ap.getValArea() >= 1
												&& (Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
											precisaLicencaAp = true;
										}
										break;
									default:
										break;
									}
								}
							}
						} else if (!Util.isNullOuVazio(idTipologiaSubGrupo)
								&& idTipologiaSubGrupo.equals(TipologiaCefirEnum.MALACOCULTURA.getId())) {
							if (ap.getValArea() >= 1 && (Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
								precisaLicencaAp = true;
							}
						} else if (!Util.isNullOuVazio(idTipologiaSubGrupo)
								&& idTipologiaSubGrupo.equals(TipologiaCefirEnum.ALGICULTURA.getId())) {
							if (ap.getValArea() >= 1 && (Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
								precisaLicencaAp = true;
							}

						} else if (!Util.isNullOuVazio(idTipologiaSubGrupo)
								&& idTipologiaSubGrupo.equals(TipologiaCefirEnum.RANICULTURA.getId())) {
							if ((Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
								precisaLicencaAp = true;
							}
						} else if (!Util.isNullOuVazio(idTipologiaSubGrupo)
								&& idTipologiaSubGrupo.equals(TipologiaCefirEnum.CARCINICULTURA.getId())) {
							List<AreaProdutivaTipologiaAtividade> apta = new ArrayList<AreaProdutivaTipologiaAtividade>();
							apta = (List<AreaProdutivaTipologiaAtividade>) tipologiaService
									.carregarTipologiaAtividadeByAreaProdutiva(ap);
							for (AreaProdutivaTipologiaAtividade ta : apta) {
								TipologiaCefirEnum tipologiaAtividadeEnum = TipologiaCefirEnum
										.obterTipologiaEnum(ta.getIdeTipologiaAtividade().getIdeTipologiaAtividade());
								if (!Util.isNull(tipologiaAtividadeEnum)) {
									switch (tipologiaAtividadeEnum) {
									// 363 - Carcinicultura em viveiros escavados
									case CARCINICULTURA_EM_VIVEIROS_ESCAVADOS:
										if ((Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
											precisaLicencaAp = true;
										}
										break;
									// 364 - Carcinicultura em viveiros escavados em
									// apicuns e salgados
									case CARCINICULTURA_EM_VIVEIROS_ESCAVADOS_EM_APICUNS_E_SALGADOS:
										if ((Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
											precisaLicencaAp = true;
										}
										break;
									default:
										break;
									}
								}
							}
						} else if (!Util.isNullOuVazio(idTipologiaSubGrupo)
								&& idTipologiaSubGrupo.equals(TipologiaCefirEnum.PECUARIA.getId())) {
							if (!imovelRural.isMenorQueQuatroModulosFiscais()) {
								if ((Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
									precisaLicencaAp = true;
								}
							}
						}
					} else if (!Util.isNullOuVazio(idTipologia)
							&& idTipologia.equals(TipologiaCefirEnum.SILVICULTURA.getId())) {
						if (!Util.isNullOuVazio(idTipologiaSubGrupo)
								&& idTipologiaSubGrupo.equals(TipologiaCefirEnum.PRODUCAO_CARVAO.getId())) {
							if ((Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
								precisaLicencaAp = true;
							}
						} else if (!Util.isNullOuVazio(idTipologiaSubGrupo)
								&& idTipologiaSubGrupo.equals(TipologiaCefirEnum.SILVICULTURA_VINCULADA_PSS.getId())) {
							if ((Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
								precisaLicencaAp = true;
							}
						}
					} else if (!Util.isNullOuVazio(idTipologia)
							&& idTipologia.equals(TipologiaCefirEnum.PRODUTOS_AGRICULTURA.getId())) {
						if (!imovelRural.isMenorQueQuatroModulosFiscais()) {
							if ((Util.isNull(ap.getLicenciada()) || !ap.getLicenciada())) {
								precisaLicencaAp = true;
							}
						}
					}
				}
			} else {
				precisaLicencaAp = true;
			}
		}
		return precisaLicencaAp;
	}

	public boolean precisaLicencaDeSupressaoVegetacao(ImovelRural imovelRural) {
		return imovelRural.getIndSupressaoVegetacao() != null && imovelRural.getIndSupressaoVegetacao() // Se ele
																										// informou que
																										// tem area
																										// suprimida
																										// depois de
																										// 22/07/2008
				&& !imovelRural.getSupressaoVegetacao().isIndProcesso(); // Se ele nao preencheu o numero do processo
	}

	public boolean precisaDeOutorga(ImovelRural imovelRural) {
		boolean precisaOutorga = false;
		if ((!Util.isNull(imovelRural.getIndSubterraneo()) && imovelRural.getIndSubterraneo())
				|| (!Util.isNull(imovelRural.getIndSuperficial()) && imovelRural.getIndSuperficial())
				|| (!Util.isNull(imovelRural.getIndLancamentoManancial()) && imovelRural.getIndLancamentoManancial())
				|| (!Util.isNull(imovelRural.getIndIntervencao()) && imovelRural.getIndIntervencao())) { // Validar se
																											// ele
																											// preencheu
																											// as
																											// outorgas

			if (!Util.isNullOuVazio(imovelRural.getImovelRuralUsoAguaCollection())) {
				for (ImovelRuralUsoAgua imovelRuralUsoAgua : imovelRural.getImovelRuralUsoAguaCollection()) { // Se ele
																												// informou
																												// que
																												// faz
																												// algum
																												// uso
																												// da
																												// agua
					precisaOutorga = true;
					if ((!Util.isNull(imovelRuralUsoAgua.getIndDispensa()) && imovelRuralUsoAgua.getIndDispensa())
							|| (!Util.isNull(imovelRuralUsoAgua.getIndProcesso())
									&& imovelRuralUsoAgua.getIndProcesso())) { // Se ele informa que possui processo
						precisaOutorga = false;
					}
				}
			} else {
				precisaOutorga = true;
			}
		}
		return precisaOutorga;
	}

	public boolean validaExclusaoProprietario(PessoaImovel proprietarioExclusao, ImovelRural imovelRural)
			throws Exception {
		if (!proprietarioExclusao.getIdePessoa().getIdePessoa()
				.equals(imovelRural.getIdeRequerenteCadastro().getIdePessoa())) {
			if (validaExclusaoProprietarioImovelCreditandoRl(proprietarioExclusao, imovelRural)
					&& validaExclusaoProprietarioImovelDebitandoRl(proprietarioExclusao, imovelRural))
				return true;
			else
				throw new Exception(
						"Não é possível excluir o proprietário do imóvel em função da compensação de reserva legal entre imóveis de mesmo proprietário.");
		} else {
			throw new Exception(Util.getString("cefir_msg_exclusao_proprietario_imovel"));
		}
	}

	private boolean validaExclusaoProprietarioImovelCreditandoRl(PessoaImovel proprietarioExclusao,
			ImovelRural imovelRural) throws Exception {
		Collection<PessoaImovel> listaTodosProprietarios = null;
		Collection<PessoaImovel> listaTodosProprietariosImovelDebito = null;
		List<PessoaImovel> listaProprietariosIguais = new ArrayList<PessoaImovel>();
		List<Object[]> listaImoveisDebitoRl = null;
		String geometriaImovel = null;

		if (!Util.isNullOuVazio(proprietarioExclusao.getIdePessoaImovel())) {
			listaTodosProprietarios = this.filtrarPROPRIETARIOImovel(imovelRural.getImovel());

			if (imovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
				geometriaImovel = validacaoGeoSeiaService
						.buscarGeometriaShapeTemporario(imovelRural.getIdeImovelRural(), 1, null);
			}

			listaImoveisDebitoRl = validarGeoSeiaDAO.listarImoveisDebitoRl(imovelRural.getIdeImovelRural(),
					geometriaImovel);

			if (!Util.isNullOuVazio(listaImoveisDebitoRl)) {
				for (Object[] imovelDebito : listaImoveisDebitoRl) {
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("ideImovelRural", (Integer) imovelDebito[0]);
					ImovelRural imovelRuralDeb = imovelRuralDAO
							.buscarEntidadePorNamedQuery("ImovelRural.findByIdeImovelRural", params);

					listaTodosProprietariosImovelDebito = this.filtrarPROPRIETARIOImovel(imovelRuralDeb.getImovel());

					for (PessoaImovel pessoaImovelDebito : listaTodosProprietariosImovelDebito) {
						if (proprietarioExclusao.getIdePessoa().getIdePessoa()
								.equals(pessoaImovelDebito.getIdePessoa().getIdePessoa())) {
							for (PessoaImovel pessoaImovel : listaTodosProprietarios) {
								for (PessoaImovel pessoaImovelDebito2 : listaTodosProprietariosImovelDebito) {
									if (pessoaImovel.getIdePessoa().getIdePessoa()
											.equals(pessoaImovelDebito2.getIdePessoa().getIdePessoa())) {
										listaProprietariosIguais.add(pessoaImovel);
									}
								}
							}

							if (listaProprietariosIguais.size() == 1) {
								return false;
							}
						}
					}
				}
			}
		}

		return true;
	}

	private boolean validaExclusaoProprietarioImovelDebitandoRl(PessoaImovel proprietarioExclusao,
			ImovelRural imovelRural) throws Exception {
		Collection<PessoaImovel> listaTodosProprietarios = null;
		Collection<PessoaImovel> listaTodosProprietariosImovelCredito = null;
		List<PessoaImovel> listaProprietariosIguais = new ArrayList<PessoaImovel>();
		List<Object[]> listaImoveisCreditoRl = null;
		String geometriaRl = null;

		// Se a RL do imóvel for do tipo Em Compensação entre Imóveis do Mesmo
		// Proprietário (3)
		if (!Util.isNullOuVazio(imovelRural.getReservaLegal())
				&& !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeTipoArl())
				&& imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(3)) {
			if (!Util.isNullOuVazio(proprietarioExclusao.getIdePessoaImovel())) {
				listaTodosProprietarios = this.filtrarPROPRIETARIOImovel(imovelRural.getImovel());

				if (imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					geometriaRl = validacaoGeoSeiaService
							.buscarGeometriaShapeTemporario(imovelRural.getIdeImovelRural(), 2, null);
				}

				listaImoveisCreditoRl = validarGeoSeiaDAO.listarImoveisCreditoRl(imovelRural.getIdeImovelRural(),
						geometriaRl, false);

				if (!Util.isNullOuVazio(listaImoveisCreditoRl)) {
					for (Object[] imovelCredito : listaImoveisCreditoRl) {
						Map<String, Object> params = new HashMap<String, Object>();
						params.put("ideImovelRural", (Integer) imovelCredito[0]);
						ImovelRural imovelRuralDeb = imovelRuralDAO
								.buscarEntidadePorNamedQuery("ImovelRural.findByIdeImovelRural", params);

						listaTodosProprietariosImovelCredito = this
								.filtrarPROPRIETARIOImovel(imovelRuralDeb.getImovel());

						for (PessoaImovel pessoaImovelCredito : listaTodosProprietariosImovelCredito) {
							if (proprietarioExclusao.getIdePessoa().getIdePessoa()
									.equals(pessoaImovelCredito.getIdePessoa().getIdePessoa())) {
								for (PessoaImovel pessoaImovel : listaTodosProprietarios) {
									for (PessoaImovel pessoaImovelCredito2 : listaTodosProprietariosImovelCredito) {
										if (pessoaImovel.getIdePessoa().getIdePessoa()
												.equals(pessoaImovelCredito2.getIdePessoa().getIdePessoa())) {
											listaProprietariosIguais.add(pessoaImovel);
										}
									}
								}

								if (listaProprietariosIguais.size() == 1) {
									return false;
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	public boolean verificarExistenciaProprietarios(Collection<PessoaImovel> listProprietariosImovel) {
		for (PessoaImovel pessoaImovel : listProprietariosImovel) {
			if (pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel()
					.equals(TipoVinculoImovel.TIPO_VINCULO_PROPRIETARIO))
				return true;
		}
		return false;
	}

	public List<ImovelRural> buscarImovelRuralPorContratoConvenio(ImovelRural imovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRural.class)
				.add(Restrictions.eq("ideContratoConvenioCefir", imovelRural.getIdeContratoConvenioCefir()));

		return imovelRuralDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isExisteImovelRuralPorContratoConvenio(ContratoConvenioCefir contratoConvenioCefir) {
		return imovelRuralDAO.isExiste(DetachedCriteria.forClass(ImovelRural.class)
				.add(Restrictions.eq("ideContratoConvenioCefir.ideContratoConvenioCefir",
						contratoConvenioCefir.getIdeContratoConvenioCefir())));
	}

	public Boolean validaImovelRuralparaRequerimento(ImovelRural imovelRural, TipoVinculoImovel tipoVinculoImovel)
			throws Exception {
		if (Util.isNullOuVazio(imovelRural) || Util.isNullOuVazio(imovelRural.getImovel())) {
			throw new Exception("Favor salvar o Imóvel Rural e completar o cadastro antes de gerar o requerimento.");
		}

		if (!imovelRural.isMenorQueQuatroModulosFiscais() && !(ContextoUtil.getContexto().isPCT())) {

			if (Util.isNullOuVazio(imovelRural.getNumItr())
					&& ((!imovelRural.isImovelCDA() && !imovelRural.isImovelBNDES())
							|| (imovelRural.getStatusCadastro() == null || !imovelRural.isCadastrado()))) {

				throw new Exception(
						"O campo ITR/Receita Federal é de preenchimento obrigatório para imóveis maiores que 4 módulos fiscais");
			}

			if (Util.isNullOuVazio(imovelRural.getNumFolha()) && !tipoVinculoImovel.isJustoPossuidor()
					&& !imovelRural.isImovelCDA() && !imovelRural.isImovelBNDES()) {

				throw new Exception(
						"O campo Folhas é de preenchimento obrigatório para imóveis maiores que 4 módulos fiscais");
			}

			if (Util.isNullOuVazio(imovelRural.getDesLivro()) && !tipoVinculoImovel.isJustoPossuidor()
					&& !imovelRural.isImovelCDA()) {
				throw new Exception(
						"O campo Livro é de preenchimento obrigatório para imóveis maiores que 4 módulos fiscais");
			}

			if (Util.isNullOuVazio(imovelRural.getDesCartorio()) && !imovelRural.isImovelCDA()
					&& !imovelRural.isImovelBNDES()) {
				throw new Exception(
						"O campo Cartório é de preenchimento obrigatório para imóveis maiores que 4 módulos fiscais");
			}

			if (Util.isNullOuVazio(imovelRural.getIdeMunicipioCartorio()) && !tipoVinculoImovel.isJustoPossuidor()
					&& !imovelRural.isImovelCDA() && !imovelRural.isImovelBNDES()) {

				throw new Exception(
						"O campo Municípo/Comarca é de preenchimento obrigatório para imóveis maiores que 4 módulos fiscais");
			}

		}

		if (Util.isNullOuVazio(imovelRural.getImovel().getIdeEndereco())) {
			throw new Exception(
					"Seus dados de Endereço estão incompletos. Favor preencher as informações necessárias para o cadastro de Imóvel.");
		}

		if (!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica())) {
			if (!localizacaoGeograficaService.existeTheGeom(imovelRural.getIdeLocalizacaoGeografica())) {
				throw new Exception(
						"Seus dados de Localização Geográfica estão incompletos. Favor informar o ShapeFile do Imóvel");
			}
		} else {
			throw new Exception(
					"Seus dados de Localização Geográfica não foram preenchidos. Favor preencher as informações necessárias para salvar a Localização Geográfica do Imóvel.");
		}

		if (Util.isNullOuVazio(imovelRural.getImovel().getProprietario())
				&& !Util.isNullOuVazio(imovelRural.getImovel().getPessoaImovelCollection())
				&& (imovelRural.getImovel().getPessoaImovelCollection().size() < 1)) {
			throw new Exception(
					"Seu Imóvel Rural não contém um proprietário. Favor preencher as informações do Proprietário do Imóvel.");
		}

		if (imovelRural.isImovelINCRA()) {
			List<String> listMensagens = new ArrayList<String>();
			if (Util.isNull(imovelRural.getValFracaoIdeal())) {
				listMensagens.add("O campo Fração ideal é de preenchimento obrigatório");
			}
			if (Util.isNull(imovelRural.getDtcCriacaoAssentamento())) {
				listMensagens.add("O campo Data de criação do assentamento é de preenchimento obrigatório");
			}
			if (Util.isNull(imovelRural.getCodSipra())) {
				listMensagens.add("O campo Código Sipra é de preenchimento obrigatório");
			}
			if (!listMensagens.isEmpty()) {
				throw new Exception(listMensagens.toString());
			}
		}
		return true;
	}

	public void validaImovelParaFinalizacao(ImovelRural pImovelRural) throws Exception {
		try {
			String geometriaIm = null;

			if (pImovelRural.isObrigatorioRl() && (Util.isNull(pImovelRural.getReservaLegal())
					|| Util.isNull(pImovelRural.getReservaLegal().getValArea()))) {
				if (pImovelRural.isImovelPCT()) {
					throw new CampoObrigatorioException("A reserva legal é de preenchimento obrigatório");
				}
				throw new CampoObrigatorioException(
						"Antes de finalizar o cadastro salve as informações complementares do Imóvel Rural.");
			}

			/** VALIDA A EXISTÊNCIA DE PROCURAÇÃO DO IMOVEL RURAL */
			if (possuiProcurador(pImovelRural) && Util.isNullOuVazio(pImovelRural.getIdeDocumentoProcuracao())
					&& !pImovelRural.isImovelBNDES()) {
				throw new CampoObrigatorioException("O Upload da procuração é de preenchimento Obrigatório!");
			}
			/**
			 * Obtem a geometria do limite do imóvel através do arquivo shape temporário ou
			 * diretamente do banco
			 */

			if (ContextoUtil.getContexto().isPCT()) {
				if (pImovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getNovosArquivosShapeImportados()) {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(
							pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_TERRITORIO_PCT.getId(), null);
				} else {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural
							.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
				}

				validacaoGeoSeiaService.validarComunidadeRlPCT(geometriaIm,
						TemaGeoseiaEnum.COMUNIDADE_RESERVA_LEGAL_PCT.getId(), pImovelRural);
			} else {
				if (pImovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(
							pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), null);
				} else {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShape(
							pImovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}
			}

			/** Aplica validação de área, sobreposição e município */
			validacaoGeoSeiaService.validarAreaDeclaradaShapeTemporario(pImovelRural.getValArea(), geometriaIm);

			/**
			 * #9605: o sistema não valida a sobreposição do limite do imóvel para os
			 * registros que se enquandrem simultaneamente nas seguintes condições
			 * 
			 * if(!(pImovelRural.getImovel().getImovelRural().isMenorQueQuatroModulosFiscais()
			 * //possuir ate 4 modulos fiscais &&
			 * pImovelRural.getImovel().getImovelRural().isFinalizado() //status registrado
			 * && !pImovelRural.getIndDesejaCompletarInformacoes() //nao marcar a opção
			 * "deseja completar suas informações(...)" no questionário &&
			 * pImovelRural.getImovel().getImovelRural().getIdeContratoConvenioCefir() ==
			 * null)){ //não estar associado a contrato/convenio
			 * 
			 * validacaoGeoSeiaService.validaSobreposicaoImovel(geometriaIm, pImovelRural);
			 * }
			 */

			if (ContextoUtil.getContexto().isPCT()) {
				if (Util.isNullOuVazio(pImovelRural.getIdePctImovelRural().getPctFamiliaCollection())) {
					throw new CampoObrigatorioException("O preencimento dos membros da família é obrigatório.");
				}
				if ((!Util.isNullOuVazio(pImovelRural.getIdePctImovelRural().getIndAssociacaoComunidade())
						&& pImovelRural.getIdePctImovelRural().getIndAssociacaoComunidade())
						&& Util.isNullOuVazio(
								pImovelRural.getIdePctImovelRural().getPessoaJuridicaPctListaAssociacao())) {
					throw new CampoObrigatorioException("Nenhuma associação foi informada para este cadastro.");
				}

				if (pImovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(
							pImovelRural.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), null);
				} else {
					geometriaIm = validacaoGeoSeiaService.buscarGeometriaShape(
							pImovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				}

				if (!validacaoGeoSeiaService.validaSobreposicaoImovelTerritorio(geometriaIm,
						pImovelRural.getIdeImovelRural())) {
					listaImoveisSobrepostos(geometriaIm, pImovelRural);
					throw new SeiaException(
							"Caro usuário, o limite do seu Imóvel Rural sobrepõe algum(ns) imóvel(eis) PCT cadastrado(s): "
									+ listaImoveisSobrepostos(geometriaIm, pImovelRural)
									+ " Para maiores informações, por favor, entre em contato com o Service Desk do SEIA, através do email atendimento.seia@inema.ba.gov.br.");
				}

				if (pImovelRural.getIsFinalizado()) {
					String theGeomLimiteTerritorio = null;
					String theGeomLimiteComunidade = null;

					if (pImovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
							.getNovosArquivosShapeImportados()) {
						theGeomLimiteTerritorio = pImovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio()
								.getTheGeom();
					} else {
						theGeomLimiteTerritorio = validacaoGeoSeiaService.buscarGeometriaShape(pImovelRural
								.getIdeLocalizacaoGeograficaPctLimiteTerritorio().getIdeLocalizacaoGeografica());
					}

					if (pImovelRural.getIdeLocalizacaoGeografica().getNovosArquivosShapeImportados()) {
						theGeomLimiteComunidade = pImovelRural.getIdeLocalizacaoGeografica().getTheGeom();
					} else {
						theGeomLimiteComunidade = validacaoGeoSeiaService.buscarGeometriaShape(
								pImovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
					}

					if (!validacaoGeoSeiaService.verificaSobreposicaoShapeUsandoContainsTheGeom(theGeomLimiteTerritorio,
							theGeomLimiteComunidade)) {
						throw new SeiaException(
								"A poligonal da comunidade inserida está fora dos limites do território informado.");
					}

					if (!validacaoGeoSeiaService.verificaPontoNoShapeUsandoContainsTheGeom(theGeomLimiteTerritorio,
							pImovelRural.getIdeLocalizacaoGeograficaPctLimiteTerritorio(),
							pImovelRural.getIdeLocalizacaoGeograficaPct())) {
						throw new SeiaException(
								"A coordenada do ponto/sede deve estar dentro do território/comunidade.");
					}
				}
			}

			if (!pImovelRural.isImovelINCRA() && !ContextoUtil.getContexto().isPCT()) {
				validacaoGeoSeiaService.validaSobreposicaoImovel(geometriaIm, pImovelRural);
			}

			if (pImovelRural.isImovelINCRA()) {
				validacaoGeoSeiaService.validaSobreposicaoImovelIncra(geometriaIm, pImovelRural);
			}

			if (Util.isNullOuVazio(pImovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio()
					.getCoordGeobahiaMunicipio())) {
				throw new SeiaException(
						"Erro ao obter o munícipio do imóvel, o identificar parece estar nulo ou vazio.");
			} else {
				// validacaoGeoSeiaService.validarSobreposicaoShapeTemporario(geometriaIm,
				// TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(),
				// pImovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				validacaoGeoSeiaService.validarCodigoIbgeMunicipioShape(geometriaIm, pImovelRural.getImovel()
						.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio().intValue());
			}
		} catch (SeiaException sx) {
			throw new Exception(sx.getMessage());
		} catch (AreaDeclaradaInvalidaException a) {
			throw new Exception("A área registrada em cartório do Imóvel Rural (" + a.getAreaDeclarada()
					+ " ha) não confere com a área do shapefile importado (" + a.getAreaCalculada() + " ha).");
		} catch (SobreposicaoAreasException s) {
			String ret = listaImoveisSobrepostos(s.getTheGeom(), pImovelRural);

			if (!Util.isNullOuVazio(ret)) {
				throw new Exception(
						"Caro usuário, o limite do seu Imóvel Rural sobrepõe o(s) seguinte(s) imóvel(eis) cadastrado(s): "
								+ ret
								+ " Para maiores informações, por favor, entre em contato com o Service Desk do SEIA, através do email atendimento.seia@inema.ba.gov.br");
			} else {
				throw new Exception(
						"Caro usuário, o limite do seu Imóvel Rural sobrepõe algum(ns) imóvel(eis) cadastrado(s). Para maiores informações, por favor, entre em contato com o Service Desk do SEIA, através do email atendimento.seia@inema.ba.gov.br");
			}
		} catch (CodigoIbgeMunicipioInvalidoException c) {
			throw new Exception(c.getMessage());
		} catch (CampoObrigatorioException c) {
			throw new Exception(c.getMessage());
		} catch (ImovelSuspensoException i) {
			throw new Exception(i.getMessage());
		} catch (Exception e) {
			throw new Exception("Erro na validação do Imóvel, contate o administrador do sistema.");
		}
	}

	public boolean possuiProcurador(ImovelRural imovelRural) {
		Collection<PessoaImovel> pessoasPorImovel = null;
		if (!Util.isNullOuVazio(imovelRural.getImovel().getIdeImovel())) {
			pessoasPorImovel = filtrarPessoasProprietariasOuNaoPorImovel(imovelRural.getImovel());
			for (PessoaImovel pessoaImovel : pessoasPorImovel) {
				if (pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel().equals(PROCURADOR)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Método para validar os campos obrigatórios do Responsável técnico e a
	 * obrigatoriedade de cadastro de ao menos um Responsável técnico
	 * 
	 * @param pImovelRural
	 * @throws CampoObrigatorioException
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 13/10/2015
	 */
	public void validaResponavelTecnico(ImovelRural pImovelRural) throws CampoObrigatorioException {
		Integer qtdResponsaveisCadastrados = 0;
		if (!Util.isNullOuVazio(pImovelRural.getResponsavelImovelRuralCollection())
				&& !pImovelRural.getResponsavelImovelRuralCollection().isEmpty()) {
			for (ResponsavelImovelRural responsavelImovelRural : pImovelRural.getResponsavelImovelRuralCollection()) {
				if (responsavelImovelRural.getIndExcluido()) {
					continue;
				}

				qtdResponsaveisCadastrados++;
				if (!ContextoUtil.getContexto().isPCT() && !pImovelRural.isMenorQueQuatroModulosFiscais()
						&& (possuiArquivoPraApp(pImovelRural) || possuiArquivoPradOP(pImovelRural)
								|| possuiArquivoPraRl(pImovelRural))) {
					if (!pImovelRural.isImovelCDA()
							&& Util.isNullOuVazio(responsavelImovelRural.getIdeDocumentoResponsavel())
							&& !responsavelImovelRural.getIndExcluido()) {
						throw new CampoObrigatorioException("Favor importar o arquivo da ART do Responsável Técnico.");
					}
				}
				if (!pImovelRural.isImovelCDA()
						&& Util.isNullOuVazio(responsavelImovelRural.getIdePessoaFisica().getIdeEscolaridade())) {
					throw new CampoObrigatorioException("Favor informar a escolaridade do Responsável Técnico.");
				}

				if (Util.isNullOuVazio(responsavelImovelRural.getIdePessoaFisica().getIdePais())
						|| Util.isNullOuVazio(responsavelImovelRural.getIdePessoaFisica().getPessoa().getDesEmail())) {
					throw new CampoObrigatorioException(
							"Prezado(a) usuário(a), verificamos que o cadastro do responsável técnico informado encontra-se incompleto. Favor atualizar as informações no cadastro de pessoa física.");
				}
			}
		}
		if (qtdResponsaveisCadastrados < 1 && !ContextoUtil.getContexto().isPCT()) {
			if (!pImovelRural.isImovelCDA() && !pImovelRural.isImovelBNDES()) {
				throw new CampoObrigatorioException(
						"Favor cadastrar o Responsável Técnico das informações ambientais.");
			}
		}
	}

	/**
	 * Método para validar a obrigatoriedade de cadastro de ao menos um Uso da água
	 * 
	 * @param imovelRural
	 * @param aceiteCondicoesRecuperacaoOp
	 * @throws CampoObrigatorioException
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 13/10/2015
	 */
	public void validaOutrosPassivosParaFinalizacao(ImovelRural imovelRural, boolean aceiteCondicoesRecuperacaoOp)
			throws CampoObrigatorioException {
		if (!Util.isNull(imovelRural.getIndOutrosPassivos()) && imovelRural.getIndOutrosPassivos()) {
			if (!imovelRural.isImovelINCRA() && !imovelRural.isImovelCDA() && !imovelRural.isImovelBNDES()) {
				if (!aceiteCondicoesRecuperacaoOp) {
					throw new CampoObrigatorioException(
							"É necessário aceitar o termo com as condições para recuperação de áreas degradadas em outros passivos.");
				}
				if (Util.isNull(imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao())
						|| Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao()
								.getCronogramaEtapaCollection())
						|| imovelRural.getOutrosPassivosAmbientais().getCronogramaRecuperacao()
								.getCronogramaEtapaCollection().isEmpty()) {
					throw new CampoObrigatorioException(
							"É necessário adicionar o cronograma de recuperação em outros passivos.");
				}
				if (Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad())
						|| Util.isNullOuVazio(imovelRural.getOutrosPassivosAmbientais().getIdeDocumentoPrad()
								.getDscCaminhoArquivo())) {
					throw new CampoObrigatorioException("É necessário importar o arquivo PRAD em outros passivos.");
				}
			}
		}
	}

	/**
	 * Método para validar a obrigatoriedade de cadastro de ao menos um Uso da água
	 * 
	 * @param imovelRural
	 * @return void
	 * @throws CampoObrigatorioException
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 13/10/2015
	 */
	public void validaUsoAguaParaFinalizacao(ImovelRural imovelRural) throws CampoObrigatorioException {
		boolean isValido = true;
		if (!Util.isNull(imovelRural.getIndSubterraneo()) && imovelRural.getIndSubterraneo()) {
			if (!temUsoAgua(imovelRural, "1")) {
				isValido = false;
			}
		}

		if (!Util.isNull(imovelRural.getIndSuperficial()) && imovelRural.getIndSuperficial()) {
			if (!temUsoAgua(imovelRural, "2")) {
				isValido = false;
			}
		}

		if (!Util.isNullOuVazio(imovelRural.getIndLancamentoManancial()) && imovelRural.getIndLancamentoManancial()) {
			if (!temUsoAgua(imovelRural, "3")) {
				isValido = false;
			}
		}

		if (!Util.isNull(imovelRural.getIndIntervencao()) && imovelRural.getIndIntervencao()) {
			if (!temUsoAgua(imovelRural, "4")) {
				isValido = false;
			}
		}

		if (!isValido && !imovelRural.isImovelCDA() && !imovelRural.isImovelBNDES()) {
			throw new CampoObrigatorioException("Por favor complete as informações de Uso da Água.");
		}
	}

	public boolean temUsoAgua(ImovelRural imovelRural, String tipoUsoAgua) {
		for (ImovelRuralUsoAgua imovelRuralUsoAgua : imovelRural.getImovelRuralUsoAguaCollection()) {
			if (imovelRuralUsoAgua.getTipoUso().equals(TipoUsoAgua.INTERVENCAO.getId().toString())) {
				for (ImovelRuralUsoAguaIntervencao intervencao : imovelRuralUsoAgua
						.getImovelRuralUsoAguaIntervencaoCollection()) {
					if (!Util.isNull(intervencao.getIdeTipoIntervencao())
							&& !Util.isNull(intervencao.getIdeTipoIntervencao().getIdeTipoIntervencao())) {
						return true;
					}
				}

			}
			if (imovelRuralUsoAgua.getTipoUso().equals(tipoUsoAgua) && !imovelRuralUsoAgua.isIndExcluido()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Método para validar se a quantidade de módulos fiscais dos imóveis
	 * cadastrados pelo projeto CAR/BNDES/INEMA é até 4
	 * 
	 * @param pImovelRural
	 * @return void
	 * @throws ValidationException
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 13/10/2015
	 */
	public void validarQtdModulosFiscaisImovelBndes(ImovelRural pImovelRural) throws ValidationException {
		if (pImovelRural.isImovelBNDES() && !Util.isNullOuVazio(pImovelRural.getQtdModuloFiscal())
				&& pImovelRural.getQtdModuloFiscal() > 4) {

			throw new ValidationException(Util.getString("cefir_msg_validacao_modulos_fiscais_projeto_car_bndes"));
		}
	}

	/**
	 * Método responsável por enviar um email com o aviso do projeto car BNDES para
	 * o requerente do imóvel
	 * 
	 * @param pImovelRural
	 * @return void
	 * @throws Exception
	 * @ @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 13/10/2015
	 */
	public void enviarEmailComAvisoImovelBndes(ImovelRural pImovelRural) throws Exception {
		EmailUtil emailUtil = new EmailUtil();
		List<ArquivoAnexarEmailDTO> listArquivosAnexar = new ArrayList<ArquivoAnexarEmailDTO>();
		StreamedContent avisoBndes = getImprimirAvisoBndes(pImovelRural.getIdeImovelRural(), false);
		ArquivoAnexarEmailDTO arquivoAnexar = new ArquivoAnexarEmailDTO("application/pdf", "aviso_cefir_car_bndes.pdf",
				"Aviso CEFIR CAR BNDES", avisoBndes.getStream());
		listArquivosAnexar.add(arquivoAnexar);
		emailUtil.enviarEmailComAnexos(carregarDestinatario(pImovelRural), ASSUNTO_EMAIL_AVISO_BNDES,
				carregarMansagemEmailAvisoBndes(), EMAIL_COMUNICADO_CEFIR, listArquivosAnexar);
	}

	private String carregarMansagemEmailAvisoBndes() {
		StringBuilder mensagem = new StringBuilder();
		mensagem.append("<html>");
		mensagem.append("<head></head>");
		mensagem.append("<body style=\"font-family: serif;font-size: medium; color: black;\">");
		mensagem.append("Prezado (a) Proprietário/Justo Possuidor,<br><br>");
		mensagem.append(
				"Seu cadastro no CEFIR foi finalizado com sucesso. O número CAR será disponibilizado em até 48 horas.<br><br>");
		mensagem.append(
				"Para obter o <b>Termo de Compromisso</b> ou o <b>Certificado de Inscrição</b> no Cadastro Estadual Florestal de ");
		mensagem.append(
				"Imóveis Rurais - CEFIR é necessário completar as informações no sistema, em atenção às normas do Estado da Bahia. <br><br>");
		mensagem.append("<b>Confira as informações completas no documento anexo.</b><br><br><br>");
		mensagem.append("Cordialmente,<br><br>");
		mensagem.append("<img src='http://s1.postimg.org/xzdv3l7u3/logo_inema_assinatura_email.png' />");
		mensagem.append("</body>");
		mensagem.append("</html>");
		return mensagem.toString();
	}

	private String carregarDestinatario(ImovelRural pImovelRural) {
		return pImovelRural.getIdeRequerenteCadastro().getDesEmail();
	}

	public StreamedContent getImprimirAvisoBndes(Integer ideImovel, boolean hasMarcaDagua) {
		try {
			ReservaLegal lReserva = reservaLegalService.buscaReservaLegalByImovelRural(new ImovelRural(ideImovel));
			String linkGeobahia = "";
			String linkGeobahiaRL = "";
			List<String> linkCompensacaoRL = null;

			StringBuilder buffer = getURLGeoBahia(ideImovel);

			if (!Util.isNullOuVazio(buffer)) {
				linkGeobahia = ConfigEnum.GEOBAHIA_SERVER
						+ buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20");

				List<ImovelRural> listImoveisEmCompensacaoDeReserva = new ArrayList<ImovelRural>();

				if (!Util.isNull(lReserva) && lReserva.getIdeTipoArl().getIdeTipoArl().equals(3)) {
					listImoveisEmCompensacaoDeReserva = imoveisEmCompensacaoDeReserva(ideImovel);
				}

				if (!Util.isNullOuVazio(listImoveisEmCompensacaoDeReserva)) {
					linkCompensacaoRL = new ArrayList<String>();

					for (int i = 0; i < listImoveisEmCompensacaoDeReserva.size(); i++) {
						buffer = getURLGeoBahia("idimov=" + ideImovel + "&res=640%20480&idimovc="
								+ listImoveisEmCompensacaoDeReserva.get(i));
						linkCompensacaoRL.add(ConfigEnum.GEOBAHIA_SERVER
								+ buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20"));
					}
				}
			}

			if (!hasAvisoBndes(ideImovel)) {
				this.gerarCertificado(TipoCertificadoEnum.AVISO_BNDES, ideImovel);
			}

			return this.impAvisoBndes(ideImovel, linkGeobahia, linkGeobahiaRL, linkCompensacaoRL, hasMarcaDagua);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return null;
		}
	}

	private StringBuilder getURLGeoBahia(Integer ideImovel) throws IOException {
		return criarStreamComUrl(obterStringUrlGeoBahiaPorTipo("idimov=" + ideImovel + "&res=640%20480"));
	}

	private StringBuilder getURLGeoBahia(String parametros) throws IOException {
		return criarStreamComUrl(obterStringUrlGeoBahiaPorTipo(parametros));
	}

	private StringBuilder criarStreamComUrl(String pUrl) throws IOException {
		URL url = null;
		StringBuilder buffer = null;
		BufferedReader br = null;
		InputStreamReader in = null;
		try {
			url = new URL(pUrl);
			in = new InputStreamReader(url.openStream());
			br = new BufferedReader(in);

			buffer = new StringBuilder();
			String linha;
			while ((linha = br.readLine()) != null) {
				buffer.append(linha);
			}
		} catch (MalformedURLException e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		} catch (IOException e) {
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
		} finally {
			if (!Util.isNull(in)) {
				in.close();
			}
			if (!Util.isNull(br)) {
				br.close();
			}
		}

		return buffer;
	}

	private String obterStringUrlGeoBahiaPorTipo(String parametros) {
		return URLEnum.CAMINHO_GEOBAHIA_CERTIFICADO + parametros;
	}

	public boolean hasAvisoBndes(Integer ideImovel) {
		Requerimento requerimento = requerimentoService.carregarRequerimentoByIdeImovel(ideImovel);
		if (!Util.isNullOuVazio(requerimento)) {
			Certificado certificado = this.certificadoService
					.obterUltimoCertificadoPorRequerimento(requerimento.getIdeRequerimento());
			if (!Util.isNullOuVazio(certificado)) {
				return certificado.isAvisoBndes();
			}
			return false;
		}
		return false;
	}

	public void gerarCertificado(TipoCertificadoEnum tipoCertificado, Integer ideImovel) {
		Requerimento requerimento = requerimentoService.carregarRequerimentoByIdeImovel(ideImovel);
		Certificado certificado = CertificadoUtil.gerarCertificadoByTipo(requerimento, tipoCertificado);
		String numeroCertificado = this.certificadoService.gerarNumeroCertificadoByTipo(certificado);
		certificado.setNumCertificado(numeroCertificado);
		this.certificadoService.salvar(certificado);
		auditoriaFacade.salvar(certificado);
	}

	private StreamedContent impAvisoBndes(Integer ideImovelRural, String linkGeobahia, String linkGeobahiaRL,
			List<String> linkCompensacaoRL, boolean hasMarcaDagua) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();

		ImovelRural imovelRural = carregarTudo(new ImovelRural(ideImovelRural));

		imovelRural.setReservaLegal(reservaLegalService.buscaReservaLegalByImovelRural(imovelRural));

		lParametros.put("ide_imovel", ideImovelRural);

		// SHAPES
		lParametros.put("SHAPE", linkGeobahia);
		lParametros.put("SHAPE_RL", linkGeobahiaRL);

		if (!Util.isNullOuVazio(linkCompensacaoRL)) {
			lParametros.put("SHAPE_COMPENSACAO_RL", linkCompensacaoRL);
			lParametros.put("AREA_IMOVEL", imovelRural.getValArea());
		}

		if (!Util.isNull(imovelRural.getReservaLegal())
				&& imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.ECIP.getId())) {
			lParametros.put("isRlEmCompensacao", true);
		} else {
			lParametros.put("isRlEmCompensacao", false);
		}

		return new RelatorioUtil("aviso_bndes_cefir.jasper", lParametros, hasMarcaDagua)
				.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}

	/**
	 * Método que retorna lista de Imóveis sobrepostos pela theGeom exceto o proprio
	 * imóvel passado por parametro.
	 * 
	 * @author ivanildo.souza
	 * 
	 * @param ideLocGeoCollection
	 * @param imovelRural
	 * @return @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ImovelRural> listarImoveisSobrepostosPorId(Collection<Integer> ideImovelSobrepostoCollection,
			ImovelRural imovelRural) {

		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRural.class)
				.createAlias("imovel", "i", JoinType.INNER_JOIN)
				.add(Restrictions.in("ideImovelRural", ideImovelSobrepostoCollection))
				.add(Restrictions.eq("i.indExcluido", false))

				.setProjection(Projections.projectionList()
						.add(Projections.property("ideImovelRural"), "ideImovelRural")
						.add(Projections.property("desDenominacao"), "desDenominacao")
						.add(Projections.property("statusCadastro"), "statusCadastro")
						.add(Projections.property("indDesejaCompletarInformacoes"), "indDesejaCompletarInformacoes"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(ImovelRural.class))

				.addOrder(Order.asc("desDenominacao"));

		List<ImovelRural> imoveis = imovelRuralDAO.listarPorCriteria(criteria);

		imoveis.remove(imovelRural);

		return imoveis;

	}

	/**
	 * 
	 * Método que retorna uma String concatenada com os nomes dos imóveis
	 * sobrepostos pela theGeom.
	 *
	 * @author ivanildo.souza
	 *
	 * @param theGeom
	 * @return String @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)

	public String listaImoveisSobrepostos(String theGeom, ImovelRural imovel) {
		StringBuilder stringImoveisSobrepostos = new StringBuilder();

		List<ImovelRural> listaImoveisSobrepostos = new ArrayList<ImovelRural>();

		for (ImovelRural ir : listarImoveisSobrepostosPorId(localizacaoGeograficaServiceFacade
				.getCollectionLocalizacaoGeograficaSobrepostaBy(theGeom, imovel.getIdeImovelRural()), imovel)) {

			if (!ir.isRegistrado()) {
				listaImoveisSobrepostos.add(ir);
			}
		}

		int POS_ULTIMO_IMOVEL = listaImoveisSobrepostos.size() - 1;
		int POS_PENULTIMO_IMOVEL = POS_ULTIMO_IMOVEL - 1;

		for (ImovelRural imovelRural : listaImoveisSobrepostos) {

			stringImoveisSobrepostos.append(imovelRural.getDesDenominacao());

			if (imovelRural.equals(listaImoveisSobrepostos.get(POS_ULTIMO_IMOVEL))) {
				stringImoveisSobrepostos.append(".");
				break;
			} else if (imovelRural.equals(listaImoveisSobrepostos.get(POS_PENULTIMO_IMOVEL))) {
				stringImoveisSobrepostos.append(" e ");
			} else {
				stringImoveisSobrepostos.append(", ");
			}
		}

		return stringImoveisSobrepostos.toString();
	}

	public Integer countImoveisCadastroIncompleto(ImovelRuralDTO dadosParaBusca) {

		return imovelRuralImplDao.countImoveisCadastroIncompleto(dadosParaBusca);

	}

	public void enviarEmailTransferenciaTitularidade(List<String> destinatarioL, String nomeImovel) throws Exception {
		StringBuilder msg = new StringBuilder();
		msg.append("Prezado(a),\n\n").append("Foi realizada a transferência do Imóvel Rural ").append(nomeImovel)
				.append(" para sua titularidade. \n").append("Para concluir a transação, acesse o sistema ")
				.append("CEFIR (http://www.sistema.seia.ba.gov.br/) ")
				.append("e atualize o cadastro com as novas informações. \n\n\n ").append("Atenciosamente, \n")
				.append("Central de Atendimento/INEMA");
		EmailUtil emailUtil = new EmailUtil();
		if (!Util.isNullOuVazio(destinatarioL)) {
			emailUtil.enviarEmail(destinatarioL, null, null, "SEIA - Transferência de Titularidade de Imóvel",
					msg.toString());
		}

	}

	public StreamedContent getImprimirHistorico(Date dataInicio, Date dataFim, Integer ideImovelRural, String status, List<FiltroAuditoria> auditoria)
			throws Exception {
		final Map<String, Object> lParametros = new HashMap<String, Object>();
		try {
		ImovelRural imovelRural = this.carregarTudo(new ImovelRural(ideImovelRural));
		JRBeanCollectionDataSource historico = new JRBeanCollectionDataSource(auditoria);
	 
		lParametros.put("consulta", historico);
		lParametros.put("nomeImovel", imovelRural.getDesDenominacao());
		if(!Util.isNullOuVazio(imovelRural.getIdeRequerenteCadastro().getPessoaFisica())) {
			lParametros.put("requerente", imovelRural.getIdeRequerenteCadastro().getPessoaFisica().getNomPessoa());
		}
		else {
			lParametros.put("requerente", imovelRural.getIdeRequerenteCadastro().getPessoaJuridica().getNomRazaoSocial());
		}
		lParametros.put("municipio", imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getNomMunicipio());
		lParametros.put("status", status);
		lParametros.put("dataFinalizacao", imovelRural.getDtcFinalizacao());
		return new RelatorioUtil("relatorioHistoricoAlteracoes.jasper", lParametros, "logoInemaRelatorio.png",
				"sema_vertical.png").gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);

		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return null;
		}
	}

	public void invalidarCertificadoTransferenciaTitularidade(ImovelRural imovelRural) {
		if (!imovelRural.getIdeImovelRural()
				.equals(14458)) { /**
									 * - Solução paliativa para atender solicitação de Maria Daniela de alteração do
									 * requerente do imóvel SÍTIO BOA ESPERANÇA
									 */
			imovelRural.setStsCertificado(null);
			imovelRural.setStatusCadastro(StatusCadastroImovelRuralEnum.REGISTRO_INCOMPLETO.getId());
			imovelRural.setPrazoValidade(null);
		}
		atualizarTransferencia(imovelRural);

	}

	public void callTransferenciaFunction(Integer ideImovelRural, Integer ideRequerenteAnterior,
			Integer ideRequerenteNovo) {

		imovelRuralImplDao.callTransferenciaFunction(ideImovelRural, ideRequerenteAnterior, ideRequerenteNovo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void inativar(Imovel imovel) {

		imovel.setIndExcluido(true);
		imovel.setDtcExclusao(new Date());
		imovel.setIdeUsuarioExclusao(ContextoUtil.getContexto().getUsuarioLogado());

		imovelDAO.salvarOuAtualizar(imovel);

	}
}
