package br.gov.ba.seia.service;

import java.math.BigDecimal;
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
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.GeoReferenciavelDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.LocalizacaoGeograficaDAOImpl;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.GeoReferenciavel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.ObjetivoLimpezaArea;
import br.gov.ba.seia.entity.ObjetivoRequerimentoLimpezaArea;
import br.gov.ba.seia.entity.ParamPersistDadoGeo;
import br.gov.ba.seia.entity.PctImovelRural;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.RequerimentoImovel;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.ObjetivoLimpezaAreaEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.SistemaCoordenadaEnum;
import br.gov.ba.seia.enumerator.TipoIntervencaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author mario.junior
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LocalizacaoGeograficaService {

	@Inject
	IDAO<LocalizacaoGeografica> localizacaoGeograficaDAO;
	@Inject
	IDAO<ImovelRural> imovelRuralDAO;
	@Inject
	IDAO<ObjetivoRequerimentoLimpezaArea> objetivoRequerimentoLimpezaAreaDAO;
	@Inject
	IDAO<RequerimentoImovel> requerimentoImovelDAO;
	@Inject
	IDAO<DadoGeografico> dadoGeograficoDAO;
	@Inject
	IDAO<Empreendimento> daoEmpreendimento;
	@Inject
	IDAO<ParamPersistDadoGeo> paramPersistDadoGeoDAO;
	@Inject
	private IDAO<String> stringDAO;
	@Inject
	private IDAO<Object> daoObject;
	
	@EJB
	private GeoReferenciavelDAOImpl geoReferenciavelDAOImpl;
	@EJB
	private DatumService serviceDatum;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private ImovelRuralService imovelRuralService;
	@EJB
	private ProhidrosService prohidrosService;
	@EJB
	private MunicipioService municipioService;
	@EJB
	private VerticeService verticeService;
	@EJB
	private ParamPersistDadoGeoService paramPersistDadoGeoService;
	@EJB
	private LocalizacaoGeograficaDAOImpl locGeoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica)  {
		localizacaoGeograficaDAO.salvarOuAtualizar(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica)  {
		localizacaoGeografica = (LocalizacaoGeografica) localizacaoGeograficaDAO.mergeComRetorno(localizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LocalizacaoGeografica> listarLocalizacaoGeograficaAbaFlorestal(Integer ideRequerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		criteria
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("idePergunta", "p", JoinType.INNER_JOIN)
			.createAlias("ideLocalizacaoGeografica", "l", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("indResposta", true))
			.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
			.add(Restrictions.in("p.codPergunta", new String[] {
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_1.getCod(), 
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_11.getCod(), 
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_111.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_12.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_13.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_14.getCod(), 
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_15.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_152.getCod(), 
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1521.getCod(), 
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_16.getCod(), 
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_17.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_18.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_19.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p10.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p103.getCod(), 
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p101.getCod(), 
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p12.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p121.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p13.getCod(), 
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p14.getCod(),
					PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA5_D1_1p15.getCod()
				}))
			
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("l.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(LocalizacaoGeografica.class))
		;
		
		return localizacaoGeograficaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LocalizacaoGeografica buscarLocalizacaoGeograficaAbaFlorestal(Integer ideRequerimento, Integer ideImovel, PerguntaEnum perguntaEnum)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		
		criteria
		
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("idePergunta", "p", JoinType.INNER_JOIN)
			.createAlias("ideLocalizacaoGeografica", "l", JoinType.INNER_JOIN)
			.createAlias("ideImovel", "i", JoinType.INNER_JOIN)
			.createAlias("l.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("indResposta", true))
			.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
			.add(Restrictions.eq("i.ideImovel", ideImovel))
			.add(Restrictions.eq("p.codPergunta", perguntaEnum.getCod()))
			
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("l.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica")
					.add(Projections.property("sc.ideSistemaCoordenada"),"ideSistemaCoordenada.ideSistemaCoordenada")
					.add(Projections.property("sc.nomSistemaCoordenada"),"ideSistemaCoordenada.nomSistemaCoordenada")
					.add(Projections.property("l.ideClassificacaoSecao"), "ideClassificacaoSecao")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(LocalizacaoGeografica.class))
		;
		
		return localizacaoGeograficaDAO.buscarPorCriteria(criteria);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LocalizacaoGeografica buscarLocalizacaoGeograficaEmpreendimento(Integer ideRequerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoRequerimento.class);
		
		criteria
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("r.florestalCollection", "f", JoinType.INNER_JOIN)
			.createAlias("ideEmpreendimento", "e", JoinType.INNER_JOIN)
			.createAlias("e.ideLocalizacaoGeografica", "l", JoinType.INNER_JOIN)
			.createAlias("l.ideClassificacaoSecao", "cs", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
			
			.setProjection(
					Projections.distinct(
						Projections.projectionList()
							.add(Projections.property("l.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica")
							.add(Projections.property("cs.ideClassificacaoSecao"),"ideClassificacaoSecao.ideClassificacaoSecao")
			))
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(LocalizacaoGeografica.class))
		;
		
		return localizacaoGeograficaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isUnidadeConservacao(Integer ideLocalizacaoGeografica)  {
		
		String sql = "SELECT * from sp_info_uconservacao(:ideLocalizacaoGeografica)";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideLocalizacaoGeografica", ideLocalizacaoGeografica);
		
		@SuppressWarnings("rawtypes")
		List result = localizacaoGeograficaDAO.buscarPorNativeQuery(sql, params);
		
		if(!Util.isNullOuVazio(result)) {
			for(Object resultElement : result) {
				Object[] item = (Object[]) resultElement; 
				if(item[0]!=null || item[1]!=null){
					return true;
				}
			}
		}
		return false;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Object salvarOuAtualizarLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica, Object obj, ObjetivoLimpezaArea objLimpArea) throws Exception  {
		Exception erro = null;
		try {
			localizacaoGeograficaDAO.salvarOuAtualizar(localizacaoGeografica);
			ContextoUtil.getContexto().setLocalizacaoSalvaNoReq(localizacaoGeografica);
			if (obj.getClass() == ImovelRural.class) {
				ImovelRural imovelRural = (ImovelRural) obj;
				imovelRuralService.salvarImovelRural(imovelRural);
				return null;
			}
			if (obj.getClass() == RequerimentoImovel.class) {
				RequerimentoImovel requerimentoImovel = (RequerimentoImovel) ((RequerimentoImovel) obj).clone();
				requerimentoImovel.setIdeLocalizacaoGeografica(localizacaoGeografica);
				requerimentoImovel.setDtcCriacao(new Date());
				if (!Util.isNullOuVazio(localizacaoGeografica.getVlrArea())
						&& !localizacaoGeografica.getVlrArea().equals(new BigDecimal(0)))
					requerimentoImovel.setVlrArea(BigDecimal.valueOf(localizacaoGeografica.getVlrArea().doubleValue()));
				requerimentoImovel.setDscPontoReferencia(localizacaoGeografica.getDesLocalizacaoGeografica());
				if (Util.isNullOuVazio(requerimentoImovel.getRequerimento().getRequerimentoImovelCollection())) {
					requerimentoImovel.getRequerimento().setRequerimentoImovelCollection(
							new ArrayList<RequerimentoImovel>());
					requerimentoImovel.getRequerimento().getRequerimentoImovelCollection().add(requerimentoImovel);
				} else {
					requerimentoImovel.getRequerimento().getRequerimentoImovelCollection().add(requerimentoImovel);
				}
				requerimentoImovel.getPerguntaRequerimento().setIdeLocalizacaoGeografica(
						requerimentoImovel.getIdeLocalizacaoGeografica());
				if (!Util.isNullOuVazio(requerimentoImovel.getPerguntaRequerimento())
						&& !Util.isNullOuVazio(requerimentoImovel.getPerguntaRequerimento().getIdePergunta())
						&& requerimentoImovel.getPerguntaRequerimento().getIdePergunta().getCodPergunta()
								.equals(PerguntaEnum.PERGUNTA_PRODUCAO_VOLUMETRICA_DE_MADEIRA.getCod())
						&& !requerimentoImovel.getPerguntaRequerimento().getIdePergunta().getResposta()) {
					ObjetivoRequerimentoLimpezaArea objReqLimpTemp = new ObjetivoRequerimentoLimpezaArea();
					objLimpArea.setNomObjetivoLimpezaArea(ObjetivoLimpezaAreaEnum.LIMPEZA_DE_AREA_SILVICULTURA
							.getNomeObjetivoLimpezaAreaEnum(objLimpArea.getIdeObjetivoLimpezaArea()));
					objReqLimpTemp.setIdeObjetivoLimpezaArea(objLimpArea);
					if (Util.isNullOuVazio(requerimentoImovel.getObjetivoRequerimentoLimpezaAreaCollection())) {
						requerimentoImovel
								.setObjetivoRequerimentoLimpezaAreaCollection(new ArrayList<ObjetivoRequerimentoLimpezaArea>());
					}
					requerimentoImovel.getObjetivoRequerimentoLimpezaAreaCollection().add(objReqLimpTemp);
					requerimentoImovelDAO.salvarOuAtualizar(requerimentoImovel);
					for (ObjetivoRequerimentoLimpezaArea objetivoRequerimentoLimpezaArea : requerimentoImovel
							.getObjetivoRequerimentoLimpezaAreaCollection()) {
						if (objetivoRequerimentoLimpezaArea.getIdeObjetivoLimpezaArea().getIdeObjetivoLimpezaArea() == objLimpArea
								.getIdeObjetivoLimpezaArea().intValue()) {
							objetivoRequerimentoLimpezaArea.setIdeLocalizacaoGeografica(localizacaoGeografica);
							objetivoRequerimentoLimpezaArea.setIdeObjetivoLimpezaArea(objLimpArea);
							objetivoRequerimentoLimpezaArea.setIdeRequerimentoImovel(requerimentoImovel);
							objetivoRequerimentoLimpezaArea.setIdeRequerimento(requerimentoImovel.getRequerimento());
							objetivoRequerimentoLimpezaAreaDAO.salvarOuAtualizar(objetivoRequerimentoLimpezaArea);
							ContextoUtil.getContexto().setObjReqLimpArea(
									(ObjetivoRequerimentoLimpezaArea) objetivoRequerimentoLimpezaArea.clone());
						}
					}
				}
				requerimentoImovelDAO.salvarOuAtualizar(requerimentoImovel);

				PerguntaRequerimento pergReq = new PerguntaRequerimento();
				pergReq = requerimentoImovel.getPerguntaRequerimento();
				pergReq.setDtcResposta(new Date());
				pergReq.setIdeRequerimento(requerimentoImovel.getRequerimento());
				pergReq.setIdeImovel(requerimentoImovel.getImovel());
				pergReq.setIdeLocalizacaoGeografica(requerimentoImovel.getIdeLocalizacaoGeografica());
				pergReq.setIndExcluido(false);
				if (Util.isNullOuVazio(pergReq.getIndResposta())) {
					throw new NullPointerException(
							"Não foi possível obter o valor da resposta informada. Para resolver o problema, mude sua resposta e depois volte para a resposta que deseja e repita o procedimento.");
				}
				if (!Util.isNull(pergReq))
					perguntaRequerimentoService.salvarOuAtualizarPerguntaReq(pergReq);
				
				ContextoUtil.getContexto().setPerguntaReq(pergReq.clone());
				return requerimentoImovel;
			}
			if (obj.getClass() == Empreendimento.class) {
				Empreendimento empreendimento = (Empreendimento) obj;
				empreendimento.setIdeLocalizacaoGeografica(localizacaoGeografica);
				Map<String, Object> parametros = new HashMap<String, Object>();
				parametros.put("ideLocGeo", empreendimento.getIdeLocalizacaoGeografica());
				parametros.put("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
				daoEmpreendimento.executarNamedQuery("Empreendimento.atualizarLocGeoEmpreendimento", parametros);
				return null;
			}
			return obj;
		} catch (NullPointerException nullException) {
			System.out.println(nullException.getMessage());
			System.out.println("***** Não foi possível salvar o(a) " + obj.getClass().getName());
			throw nullException;
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			System.out.println("***** Não foi possível salvar o(a) " + obj.getClass().getName());
			throw e;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarlocalizacaoComImovel(LocalizacaoGeografica localizacaoGeografica, ImovelRural imovelRural)
			 {
		localizacaoGeograficaDAO.salvarOuAtualizar(localizacaoGeografica);
		imovelRuralDAO.atualizar(imovelRural);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarlocalizacao(LocalizacaoGeografica localizacaoGeografica)  {

		if(Util.isNullOuVazio(localizacaoGeografica.getParamPersistDadoGeoCollection())) {
			localizacaoGeografica.setParamPersistDadoGeoCollection(null);
		}
		
		localizacaoGeograficaDAO.salvarOuAtualizar(localizacaoGeografica);
	}

	/**
	 * Metodo que salva localização geografica e retorna a mesma atualizada.
	 * Identico ao salvarlocalizacao()
	 * 
	 * @param localizacaoGeografica
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LocalizacaoGeografica salvarLocalizacao(LocalizacaoGeografica localizacaoGeografica)  {
		localizacaoGeograficaDAO.salvarOuAtualizar(localizacaoGeografica);
		return localizacaoGeografica;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarLocalizacaoGeograficaVertices(LocalizacaoGeografica localizacaoGeografica)
			 {
		Collection<DadoGeografico> collVertice = localizacaoGeografica.getDadoGeograficoCollection();
		localizacaoGeografica.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());

		localizacaoGeograficaDAO.salvarOuAtualizar(localizacaoGeografica);
		for (DadoGeografico vertice : collVertice) {
			vertice.setIdeLocalizacaoGeografica(localizacaoGeografica);
			verticeService.salvarOuAtualizarVertice(vertice);
			localizacaoGeografica.getDadoGeograficoCollection().add(vertice);
		}

		localizacaoGeograficaDAO.salvarOuAtualizar(localizacaoGeografica);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean validaVerticeMunicipio(String latitude, String longitude, Empreendimento empreendimento, String srid)  {
		
		if(Util.isNull(empreendimento)){
			empreendimento = (Empreendimento) SessaoUtil.recuperarRemoverObjetoSessao("emp"); 
		}
		
		Collection<Municipio> municipios = municipioService.listarTodosMunicipiosEnvolvidos(empreendimento.getIdeEmpreendimento());
		
		if(!Util.isNullOuVazio(municipios) && municipios.size() > 1){
			return validaVerticeMunicipio(latitude, longitude, municipios, srid);
					
		}
		
		Double ibgeMunicipio = municipioService.getMunicipioByEmpreendimento(empreendimento).getCoordGeobahiaMunicipio();
		Double ibgeCoord = new Double(0.0);
		latitude = latitude.replace(',', '.');
		longitude = longitude.replace(',', '.');
		
		try {
			ibgeCoord = prohidrosService.getIBGEMunicipio(Double.parseDouble(longitude), Double.parseDouble(latitude), srid);
		} catch (Exception e) {
			MensagemUtil.erro("Não foi possível validar a coordenada. Servidor Seia está sem acesso ao Prohidros.");
		}
		
		return validaCodigoIBGE(ibgeMunicipio, ibgeCoord);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean validaVerticeMunicipio(String latitude, String longitude, Collection<Municipio> municipios, String srid)   {

		latitude = latitude.replace(',', '.');
		longitude = longitude.replace(',', '.');
		Double ibgeCoord = prohidrosService.getIBGEMunicipio(Double.parseDouble(longitude), Double.parseDouble(latitude), srid);
		for(Municipio municipio : municipios){
			Double ibgeMunicipio = municipioService.obterCodigoIBGE(municipio);
			
			if(validaCodigoIBGE(ibgeMunicipio, ibgeCoord)){
				return true;
			}
			
		}
		return false;
	}

	private Boolean validaCodigoIBGE(Double ibgeMunicipio, Double ibgeCoord) {
		return  (!Util.isNullOuVazio(ibgeMunicipio) && ibgeMunicipio.equals(ibgeCoord));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LocalizacaoGeografica carregar(Integer ide)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(LocalizacaoGeografica.class, "localizacaoGeografica")
				.createAlias("ideSistemaCoordenada", "sistemaCoordenada", JoinType.INNER_JOIN)
				.createAlias("ideClassificacaoSecao", "classificacaoSecao", JoinType.INNER_JOIN)
				.createAlias(".dadoGeograficoCollection", "dg")
				.add(Restrictions.eq("ideLocalizacaoGeografica", ide));
		LocalizacaoGeografica localizacao = localizacaoGeograficaDAO.buscarPorCriteria(criteria);
		if(!Util.isNullOuVazio(localizacao.getDadoGeograficoCollection())) {
			if(Util.isLazyInitExcepOuNull(localizacao.getDadoGeograficoCollection())) {
				Hibernate.initialize(localizacao.getDadoGeograficoCollection());
			}
		}
		
		return localizacao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LocalizacaoGeografica carregar(LocalizacaoGeografica localizacaoGeografica)  {
		return carregar(localizacaoGeografica.getIdeLocalizacaoGeografica());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DadoGeografico> listarDadoGeografico(LocalizacaoGeografica locGeo, Integer classifSec)  {
		List<DadoGeografico> listDadoGeo = null;
		List<DadoGeografico> tempDadoGeo = null;
		String sql = new String(
				"SELECT dg.ide_dado_geografico, dg.coord_geo_numerica, dg.ide_localizacao_geografica, csg.ide_classificacao_secao FROM dado_geografico dg"
				+ " inner join localizacao_geografica lg on lg.ide_localizacao_geografica = dg.ide_localizacao_geografica "
				+ " inner join classificacao_secao_geometrica csg on csg.ide_classificacao_secao = lg.ide_classificacao_secao "
						+ "WHERE dg.ide_localizacao_geografica = " + locGeo.getIdeLocalizacaoGeografica());
		if (classifSec.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())) {
			sql = sql + " AND the_geom is not null " + "AND coord_geo_numerica is null " + "LIMIT 10;";
		} else {
			sql = sql + " AND coord_geo_numerica is not null;";
		}
		tempDadoGeo = dadoGeograficoDAO.buscarPorNativeQuery(sql, null);
		listDadoGeo = new ArrayList<DadoGeografico>();
		for (Object tempObj : tempDadoGeo.toArray()) {
			Object[] resultElement = (Object[]) tempObj;
			DadoGeografico tempDado = new DadoGeografico((Integer) resultElement[0]);
			if (resultElement[1] != null)
				tempDado.setCoordGeoNumerica(resultElement[1].toString());
			if (resultElement[2] != null)
				tempDado.setIdeLocalizacaoGeografica(new LocalizacaoGeografica((Integer) resultElement[2]));
			if (resultElement[3] != null)
				tempDado.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica((Integer) resultElement[3]));
			listDadoGeo.add(tempDado);
		}

		return listDadoGeo;
	}

	public LocalizacaoGeografica carregarSomenteComSistemaCoordenada(Integer ide)  {
		LocalizacaoGeografica localizacao = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(LocalizacaoGeografica.class, "localizacaoGeografica");
		criteria.createAlias("ideSistemaCoordenada", "sistemaCoordenada", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideClassificacaoSecao", "classificacaoSecao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideLocalizacaoGeografica", ide));
		localizacao = localizacaoGeograficaDAO.buscarPorCriteria(criteria);
		localizacao.setDadoGeograficoCollection(null);
		localizacao.setParamPersistDadoGeoCollection(null);
		return localizacao;
	}

	public Boolean verificaSeExisteTheGeomValido(Integer ideLocalizacaoGeograf)  {
		List<LocalizacaoGeografica> listLocGeo = new ArrayList<LocalizacaoGeografica>();
		String sql = new String("SELECT ide_dado_geografico FROM dado_geografico "
				+ "WHERE ide_localizacao_geografica = " + ideLocalizacaoGeograf
				+ " AND the_geom is not null AND coord_geo_numerica is null " + "LIMIT 1;");

		listLocGeo = localizacaoGeograficaDAO.buscarPorNativeQuery(sql, null);
		if (!Util.isNullOuVazio(listLocGeo)) {
			if (listLocGeo.isEmpty()) {
				return false;
			} else
				return true;
		} else
			return false;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDadosPersistidos(LocalizacaoGeografica localizacaoGeografica)  {
		
		if(!Util.isNullOuVazio(localizacaoGeografica)) {
			excluirDadosPersistidos(localizacaoGeografica.getIdeLocalizacaoGeografica());
			excluirByIdLocalizacaoGeografica(localizacaoGeografica);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDadosPersistidos(Integer pIdeLocGeo)  {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideLocalizacaoGeografica", pIdeLocGeo);
		
		String delSQL = "DELETE FROM arquivo_processo WHERE ide_localizacao_geografica = :ideLocalizacaoGeografica";
		localizacaoGeograficaDAO.executarNativeQuery(delSQL, params);

		delSQL = "DELETE FROM param_persist_dado_geo WHERE ide_localizacao_geografica = :ideLocalizacaoGeografica";
		localizacaoGeograficaDAO.executarNativeQuery(delSQL, params);

		delSQL = "DELETE FROM dado_geografico WHERE ide_localizacao_geografica = :ideLocalizacaoGeografica";
		localizacaoGeograficaDAO.executarNativeQuery(delSQL, params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerById(DadoGeografico dadoGeografico)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideDadoGeografico", dadoGeografico.getIdeDadoGeografico());

		this.dadoGeograficoDAO.executarNamedQuery("DadoGeografico.removerById", params);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirByIdLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideLocalizacaoGeografica", localizacaoGeografica.getIdeLocalizacaoGeografica());
		dadoGeograficoDAO.executarNamedQuery("DadoGeografico.removerByIdLocalizacaoGeo", params);
		localizacaoGeograficaDAO.executarNamedQuery("LocalizacaoGeografica.deleteByIde", params);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarGeoReferenciavel(GeoReferenciavel geoReferenciavel) {
		geoReferenciavelDAOImpl.salvarAtualizar(geoReferenciavel);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarGeoReferenciavel(GeoReferenciavel geo)  {
		salvarGeoReferenciavel(geo);
	}

	/**
	 * Metodo que identifica o {@link SistemaCoordenada} do arquivo PRJ.
	 *  
	 * @return <code> null - Para arquivo inválido</code> 
	 *
	 * @author ivanildo.souza
	 * @since 08/02/2017
	 */
	public SistemaCoordenada obterSistCoordPRJ(String caminhoArquivoPRJ) {
		SistemaCoordenada sistCoordenada = null;
		String arquivo = FileUploadUtil.obterReferenciaEspacial(caminhoArquivoPRJ);
		String split[] = arquivo.split("\"",3);
		split = arquivo.split(",");
		
		Collection<SistemaCoordenada> listaDatums;
		
		// Validação de Arquivo PRJ Inválido
		if(split[0].length() < 8){
			return null;
		}
		
		String tipoCoord = (split[0]).substring(0,6);
		String datumCoord = (split[0]).substring(7).replace("\"", "");
		
		if(tipoCoord.equalsIgnoreCase("GEOGCS")){
			if(datumCoord.toLowerCase().contains("sad") || (datumCoord.toLowerCase().contains("south") && datumCoord.toLowerCase().contains("american")) && datumCoord.toLowerCase().contains("69")){
				sistCoordenada = new SistemaCoordenada(SistemaCoordenadaEnum.GEOGRAFICA_SAD69.getId());
			}else if(datumCoord.toLowerCase().contains("sirgas") && datumCoord.toLowerCase().contains("2000")){
				sistCoordenada = new SistemaCoordenada(SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getId());
			}else{
				return null;
			}
		}
		else if(tipoCoord.equalsIgnoreCase("PROJCS")){
			if(datumCoord.toLowerCase().contains("sad") || (datumCoord.toLowerCase().contains("south") && datumCoord.toLowerCase().contains("american")) && datumCoord.toLowerCase().contains("69")){
				if(datumCoord.contains("23")){
					sistCoordenada = new SistemaCoordenada(SistemaCoordenadaEnum.UTM_23_SAD69.getId());
				}
				else if(datumCoord.contains("24")){
					sistCoordenada = new SistemaCoordenada(SistemaCoordenadaEnum.UTM_24_SAD69.getId());
				}
				else{
					return null;
				}
			}
			else if((datumCoord.toLowerCase().contains("sirgas") && datumCoord.toLowerCase().contains("2000")) || (datumCoord.toLowerCase().contains("wgs") && datumCoord.toLowerCase().contains("84"))){
				if(datumCoord.contains("23")){
					sistCoordenada = new SistemaCoordenada(SistemaCoordenadaEnum.UTM_23_SIRGAS_2000.getId());
				}
				else if(datumCoord.contains("24")){
					sistCoordenada = new SistemaCoordenada(SistemaCoordenadaEnum.UTM_24_SIRGAS_2000.getId());
				}
				else{
					return null;
				}
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
		
		listaDatums = serviceDatum.listarDatum();
		for (SistemaCoordenada sistema : listaDatums) {
			if (sistema.getIdeSistemaCoordenada().equals(sistCoordenada.getIdeSistemaCoordenada())){
				sistCoordenada = sistema;
			}
		}
		
		return serviceDatum.carregar(sistCoordenada.getIdeSistemaCoordenada());
	}
	
	public boolean existeTheGeom(LocalizacaoGeografica localizacaoGeografica)  {
		if (!Util.isNull(localizacaoGeografica)) {
			if (localizacaoGeografica.getNovosArquivosShapeImportados()){
				return true;
			}else if(!localizacaoGeografica.getLocalizacaoExcluida()){
				return verificaSeExisteTheGeomValido(localizacaoGeografica.getIdeLocalizacaoGeografica());
			}
		} else if (localizacaoGeografica != null && localizacaoGeografica.getIdeLocalizacaoGeografica() == null) {
			if (localizacaoGeografica.getNovosArquivosShapeImportados())
				return true;
			else
				return false;
		} 
		return false;
	}
	
	public void persistirShapes(LocalizacaoGeografica localizacaoGeografica, Double codigoMunicipio) throws Exception  {
		LocalizacaoGeografica lgTemp = localizacaoGeografica;
		String[] retorno = null;
		
		retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(lgTemp, false, codigoMunicipio, false, 1);

		if (retorno != null) {
			if (retorno.length > 0) {
				if (retorno[0].equalsIgnoreCase("ERRO")) {
					localizacaoGeografica.setNovosArquivosShapeImportados(false);
					throw new Exception(retorno[2] + " [" + retorno[1] + "]");
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persistirDadoGeografico(LocalizacaoGeografica localizacaoGeografica, String geometria) throws Exception  {
		try {
			paramPersistDadoGeoService.persistirDadoGeografico(localizacaoGeografica.getIdeLocalizacaoGeografica(), geometria);
		} catch (Exception e) {
			localizacaoGeografica.setNovosArquivosShapeImportados(false);
			throw new Exception(e.getMessage());
		}

	}
	
	/**
	 * Método criado para chamar a function <i>duplicar_localizacao_geografica</i> para duplicar os dados de <i>localizacao_geografica</i> e <i>dado_geografico</i> que foram inseridos na <b>etapa 4</b> do requerimento.
	 * @param {@link LocalizacaoGeografica} - Ponto inserido no Requerimento
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 22/12/2015
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LocalizacaoGeografica duplicarLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica)  {
		String sql = "SELECT duplicar_localizacao_geografica(:idLocGeoOriginal)";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idLocGeoOriginal", localizacaoGeografica.getIdeLocalizacaoGeografica());
		
		@SuppressWarnings("rawtypes")
		List result = localizacaoGeograficaDAO.buscarPorNativeQuery(sql, params);
		
		if(!Util.isNullOuVazio(result)) {
			
			for(Object resultElement : result) {
				Object ide = resultElement; 
				
				if(!Util.isNull(ide)){
					return new LocalizacaoGeografica(new Integer(ide.toString())); 
				}
			}
		}
		
		return new LocalizacaoGeografica(); 
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTudoPorLocalizacaoGeografica(LocalizacaoGeografica locGeo) {
		try {
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideLocalizacaoGeografica", locGeo.getIdeLocalizacaoGeografica());
			
			for (DadoGeografico dg : listarDadoGeografico(locGeo, ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId())) {
				params.put("ideDadoGeografico", dg.getIdeDadoGeografico());
				paramPersistDadoGeoDAO.executarNamedQuery("ParamPersistDadoGeo.excluirPorLocGeoOUDadoGeo", params);
				params.remove("ideDadoGeografico");
			}
			
			for (DadoGeografico dg : listarDadoGeografico(locGeo, ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())) {
				params.put("ideDadoGeografico", dg.getIdeDadoGeografico());
				paramPersistDadoGeoDAO.executarNamedQuery("ParamPersistDadoGeo.excluirPorLocGeoOUDadoGeo", params);
				params.remove("ideDadoGeografico");
			}
			
			dadoGeograficoDAO.executarNamedQuery("DadoGeografico.removerByIdLocalizacaoGeo", params);
			localizacaoGeograficaDAO.executarNamedQuery("LocalizacaoGeografica.deleteByIde", params);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Double> listarCodIBGE(Integer ideLocalizacaoGeografica)  {
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideLocalizacaoGeografica", ideLocalizacaoGeografica);
		
		List<String> lista = stringDAO.buscarPorNativeQuery("select * from sp_get_municipio(:ideLocalizacaoGeografica)", parametros);
		if(!Util.isNullOuVazio(lista)) {
			List<Double> retorno = new ArrayList<Double>();
			for(String s : lista) {
				retorno.add(Double.valueOf(s));
			}
			return retorno;
		}
		
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LocalizacaoGeografica> listarLocalizacaoGeograficaBy(Processo ideProcesso, ModalidadeOutorgaEnum modalidadeOutorgaEnum, TipoIntervencaoEnum tipoIntervencaoEnum)  {
		return locGeoDAOImpl.listarLocalizacaoGeograficaByNumProcessoAndModalidadeOutorga(ideProcesso, modalidadeOutorgaEnum, tipoIntervencaoEnum);
	}
	
	/**
	 * Retorna a distância entre localizações geográficas do tipo Ponto
	 * @param locGeoA
	 * @param locGeoB
	 * @return distancia
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public BigDecimal getDistanciaEntrePontos(LocalizacaoGeografica locGeoA, LocalizacaoGeografica locGeoB) {
		StringBuilder sql = new StringBuilder("SELECT st_distance_sphere(");
		sql.append("(SELECT the_geom from dado_geografico  where ide_localizacao_geografica  = " + locGeoA.getIdeLocalizacaoGeografica() + "),");
		sql.append("(SELECT the_geom from dado_geografico  where ide_localizacao_geografica  = " + locGeoB.getIdeLocalizacaoGeografica() + ")");
		sql.append(");");
		BigDecimal value = daoObject.obterBigDecimalPorNativeQuery(sql.toString(), null);
		if(value != null){
			return value;
		}
		return new BigDecimal(0);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LocalizacaoGeografica> listarLocalizacaoGeograficaPorProcessoIntervencaoBarragem(String numPrecesso){
		
		List<LocalizacaoGeografica> listLocalizacao = new ArrayList<LocalizacaoGeografica>();
		try {
			
		DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class)
				
				.createAlias("ideRequerimento", "req",JoinType.INNER_JOIN)
				.createAlias("req.fceCollection", "fce", JoinType.INNER_JOIN)
				.createAlias("fce.ideAnaliseTecnica", "at", JoinType.INNER_JOIN)
				.createAlias("fce.fceIntervencaoBarragemCollection", "fceIBC", JoinType.INNER_JOIN)
				.createAlias("fceIBC.ideOutorgaLocalizacaoGeografica", "outLoc", JoinType.INNER_JOIN)
				.createAlias("outLoc.ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
				.createAlias("lg.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN);
		
				criteria.add(Restrictions.ilike("numProcesso", numPrecesso))
				.add(Restrictions.eq("at.indAprovado", Boolean.TRUE))

				.setProjection(Projections.projectionList()
						
					.add(Projections.property("lg.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica")
					.add(Projections.property("sc.ideSistemaCoordenada"),"ideSistemaCoordenada.ideSistemaCoordenada")
					.add(Projections.property("sc.nomSistemaCoordenada"),"ideSistemaCoordenada.nomSistemaCoordenada")
					.add(Projections.property("lg.ideClassificacaoSecao"), "ideClassificacaoSecao")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(LocalizacaoGeografica.class))
				;
				
				listLocalizacao = localizacaoGeograficaDAO.listarPorCriteria(criteria);
				
				if(!Util.isNullOuVazio(listLocalizacao)){
					for(LocalizacaoGeografica loc : listLocalizacao){
						loc.setDadoGeograficoCollection(listarDadoGeografico(loc, ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
					}
				}
				
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
				
		return listLocalizacao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<LocalizacaoGeografica> listarLocalizacaoGeograficaPorProcessoLancamentoEfluenteOuCapSubterranea(String numPrecesso){
		
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(Processo.class)
				.createAlias("ideRequerimento", "req",JoinType.INNER_JOIN)
				.createAlias("req.fceCollection", "fce", JoinType.INNER_JOIN)
				.createAlias("fce.ideAnaliseTecnica", "at", JoinType.INNER_JOIN)
				.createAlias("fce.fceOutorgaLocalizacaoGeografica", "folg", JoinType.INNER_JOIN)
				.createAlias("folg.ideLocalizacaoGeografica", "lg", JoinType.INNER_JOIN)
				.createAlias("lg.ideSistemaCoordenada", "sc", JoinType.INNER_JOIN)
		
				.add(Restrictions.ilike("numProcesso", numPrecesso))
				.add(Restrictions.eq("at.indAprovado", Boolean.TRUE))

				.setProjection(Projections.projectionList()
					.add(Projections.property("lg.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica")
					.add(Projections.property("sc.ideSistemaCoordenada"),"ideSistemaCoordenada.ideSistemaCoordenada")
					.add(Projections.property("sc.nomSistemaCoordenada"),"ideSistemaCoordenada.nomSistemaCoordenada")
					.add(Projections.property("lg.ideClassificacaoSecao"), "ideClassificacaoSecao")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(LocalizacaoGeografica.class));
				
				List<LocalizacaoGeografica> listLocalizacao = localizacaoGeograficaDAO.listarPorCriteria(criteria);
				
				if(!Util.isNullOuVazio(listLocalizacao)){
					for(LocalizacaoGeografica loc : listLocalizacao){
						loc.setDadoGeograficoCollection(listarDadoGeografico(loc, ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
					}
				}
				
			return listLocalizacao;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}
	
	public void sessionFlushAndClear() {
		/* Session flush adicionado para solucionar problema de Transferência de Titularidade
		 *  com proprietário já incluido no Imovel*/
		localizacaoGeograficaDAO.sessionFlush();
		localizacaoGeograficaDAO.sessionClear();
	}

	public LocalizacaoGeografica buscarPctLocalizacaoGeografica(ImovelRural imovelRural) {
		
	
		StringBuilder sql = new StringBuilder();
		sql.append("select ir ");
		sql.append("from ImovelRural ir ");
		sql.append("     inner join fetch ir.ideLocalizacaoGeograficaPctLimiteTerritorio ");
		sql.append("     inner join fetch ir.ideLocalizacaoGeograficaPct ");
		sql.append(" where ir.ideImovelRural = :ideImovelRural");
		
		
		Map<String, Object> params = new HashMap<String,Object>();
		params.put("ideImovelRural", imovelRural.getIdeImovelRural());
		
		ImovelRural imovelRuralObj = imovelRuralDAO.buscarPorQuery(sql.toString(), params);
				
		imovelRural.setIdeLocalizacaoGeograficaPct(imovelRuralObj.getIdeLocalizacaoGeograficaPct());
		imovelRural.setIdeLocalizacaoGeograficaPctLimiteTerritorio(imovelRuralObj.getIdeLocalizacaoGeograficaPctLimiteTerritorio());
		return imovelRuralObj.getIdeLocalizacaoGeograficaPctLimiteTerritorio();
	}
}
