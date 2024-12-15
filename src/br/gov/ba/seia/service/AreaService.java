package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.EquipeDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Equipe;
import br.gov.ba.seia.entity.Funcionalidade;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.TipoPauta;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoAreaEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AreaService {

	@Inject
	IDAO<Area> areaDAO;

	@EJB
	private ParametroService parametroService;
	@EJB
	private PautaService pautaService;
	@EJB
	private EquipeDAOImpl equipeDAOImpl;
	@EJB
	private PessoaFisicaService pessoaFisicaService;
	
	@EJB
	AtoAmbientalService atoAmbientalService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> listarAreas() {

		return areaDAO.listarTodos();

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> listarAreaParaRequerimentoEnquadradoSemTipologia()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		criteria		
			.add(Restrictions.in("ideArea", new Integer[] {
					AreaEnum.COFIS.getId(),
					AreaEnum.COGED.getId(),
					AreaEnum.NOUT.getId(),
					AreaEnum.COINS.getId(),
					AreaEnum.COINE.getId(),
					AreaEnum.COFAQ.getId(),
					AreaEnum.COTUR.getId(),
					AreaEnum.COASP.getId(),
					AreaEnum.COMIM.getId(),
					AreaEnum.COIND.getId(),
					AreaEnum.COGES.getId()
			}))
			.add(Restrictions.eq("indExcluido", false))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideArea"),"ideArea")
				.add(Projections.property("nomArea"),"nomArea")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Area.class))
		;
		return areaDAO.listarPorCriteria(criteria, Order.asc("nomArea"));
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> listarAreasFilhasOrderByAsc(Area areaPai)  {
		StringBuilder sql = new StringBuilder();
		Map<String,Object> params = new HashMap<String, Object>();
		
		sql.append("select a ");
		sql.append("from Area a ");
		sql.append("where a.indExcluido = false ");
		if(areaPai!=null) {
			sql.append("and a.ideAreaPai = :area ");
			params.put("area", areaPai);
		}
		sql.append("order by a.nomArea asc ");

		return areaDAO.listarPorQuery(sql.toString(), params);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area buscarAreaAtualProcesso(Integer ideProcesso)  {
		
		Area areaPrincipal = null;
		Equipe equipe = equipeDAOImpl.buscarEquipeAtual(ideProcesso);
		
		if(Util.isNull(equipe)) {
			DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
			criteria
				.createAlias("ideArea", "a", JoinType.INNER_JOIN)
				.createAlias("a.ideAreaPai", "ap", JoinType.INNER_JOIN)
				
				.add(Property.forName("ideControleTramitacao").eq(
						DetachedCriteria.forClass(ControleTramitacao.class)
							.add(Restrictions.eq("indFimDaFila", true))
							.setProjection(Projections.max("ideControleTramitacao"))
					)
				)
				.setProjection(Projections.projectionList()
					.add(Projections.property("a.ideArea"),"ideArea")
					.add(Projections.property("ap.ideArea"),"ideAreaPai.ideArea")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Area.class))
			;
			areaPrincipal = areaDAO.buscarPorCriteria(criteria);
		}
		else {
			areaPrincipal = equipe.getIdeArea();
		}
		
		return areaPrincipal;
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area buscarAreaEquipePorProcesso(Integer ideProcesso)  {
		
		DetachedCriteria criteria = 
		
		DetachedCriteria.forClass(ControleTramitacao.class)
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("ideStatusFluxo", "st", JoinType.INNER_JOIN)
			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			.add(Restrictions.eq("st.ideStatusFluxo", StatusFluxoEnum.ANALISE_TECNICA.getStatus()))
			.setProjection(Projections.max("ideControleTramitacao"))
		;
		
		Object retorno = areaDAO.buscarPorCriteria(criteria);
		Integer ideUltimaTramitacaoAnaliseTecnica = Integer.valueOf(retorno.toString());
		
		criteria = DetachedCriteria.forClass(Equipe.class);
		
		criteria
			.createAlias("ideArea", "a", JoinType.INNER_JOIN)
			.createAlias("ideProcesso", "p", JoinType.INNER_JOIN)
			.createAlias("integranteEquipeCollection", "ie", JoinType.INNER_JOIN)
			.createAlias("ie.idePessoaFisica", "fun", JoinType.INNER_JOIN)

			.add(Restrictions.eq("p.ideProcesso", ideProcesso))
			
			.add(Property.forName("fun.idePessoaFisica").eq(
				DetachedCriteria.forClass(ControleTramitacao.class)
					.createAlias("idePauta", "pta", JoinType.INNER_JOIN)
					.createAlias("pta.idePessoaFisica", "fun", JoinType.INNER_JOIN)
					.add(Restrictions.eq("ideControleTramitacao", ideUltimaTramitacaoAnaliseTecnica))
					.setProjection(Projections.property("fun.idePessoaFisica"))
			))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("a.ideArea"), "ideArea")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Area.class))
		;
		
		return areaDAO.buscarPorCriteria(criteria);
		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area retornarAreaDoGestorQuePermitiuAcessoNaSuaPauta(PessoaFisica pessoaFisica)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pessoa", pessoaFisica);
		params.put("funcionalidade", new Funcionalidade(51));

		StringBuilder sql = new StringBuilder();

		sql.append("select func.ideArea ");
		sql.append("from FuncionalidadeAcaoPessoaFisica fapf ");
		sql.append("     inner join fapf.funcionalidadeAcaoPessoaFisicaPautaCollection fapfPauta ");
		sql.append("     inner join fapfPauta.idePauta pauta2 ");
		sql.append("     inner join pauta2.idePessoaFisica func ");
		sql.append("where fapf.idePessoaFisica = :pessoa ");
		sql.append("      and fapf.ideFuncionalidade = :funcionalidade ");

		return areaDAO.buscarPorQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Area> listarPorRequerimento(Requerimento pRequerimento, Boolean isAtoRloRlu) throws Exception {
		
		List<Area> lista = new ArrayList<Area>();
		// Obtem área que sempre aparece
		Parametro parametro = parametroService.obterPorEnum(ParametroEnum.AREA_COINS);
		Integer idAreaCOINS = parametroService.obterValorInt(parametro);
		Area coins = areaDAO.carregarGet(idAreaCOINS);
		
		if(!coins.getIndExcluido()){
			lista.add(coins);
		}
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimento", pRequerimento.getIdeRequerimento());
		parametros.put("ideAreaConstante", idAreaCOINS);

		// Busca areas por requerimento
		List<Area> lA = areaDAO.buscarPorNamedQuery("Area.findByIdeRequerimento", parametros);
		
		if(!Util.isLazyInitExcepOuNull(lA) && !Util.isNullOuVazio(lA)) {
			lista.addAll(lA);
		} else {
			lA = areaDAO.buscarPorNamedQuery("Area.findByIdeRequerimentoSemTipologiaPrincipal", parametros);
			lista.addAll(lA);
		}
		
		//Caso esteja enquadrado no ato RLO ou RLU, deve listar também a área COFIS
		if(!Util.isNull(isAtoRloRlu) && isAtoRloRlu){
			parametros = new HashMap<String, Object>();
			parametros.put("ideArea", 12); //COFIS
			lista.addAll(areaDAO.buscarPorNamedQuery("Area.findByIdeArea", parametros));
		}
		
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area retornarAreaCoordenadorEquipe(Processo processo, Funcionario funcionario)  {

		StringBuilder sql = new StringBuilder();

		sql.append("select e.ideArea ");
		sql.append("from EquipeProcesso e ");
		sql.append("where e.ideProcesso = :ideProcesso ");
		sql.append("and e.idePessoaFisica = :idePessoaFisica ");

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideProcesso", processo);
		parametros.put("idePessoaFisica", funcionario);

		return areaDAO.buscarPorQuery(sql.toString(), parametros);
	}

	/**
	 * Método que retorna a area da coordenação que aprovou a ultima tramitação
	 * @author Lucas Oliveira Monteiro
	 * @param Processo
	 * @return Area
	 * */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area retornarCoordenacaoDaAprovacaoNotificacao(Processo pProcesso)  {

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideProcesso", pProcesso);
		parametros.put("ideStatusFluxo", Arrays.asList(new StatusFluxo(4), new StatusFluxo(16)));
		parametros.put("ideTipoArea", 2);

		StringBuilder sql = new StringBuilder();
		sql.append("select ct.ideArea ");
		sql.append("from ControleTramitacao ct ");
		sql.append("where ct.ideControleTramitacao = (select max(ct.ideControleTramitacao) ");
		sql.append("                                  from ControleTramitacao ct inner join ct.ideArea a ");
		sql.append("                                  where ct.ideProcesso = :ideProcesso ");
		sql.append("                                        and ct.ideStatusFluxo in :ideStatusFluxo ");
		sql.append("                                        and a.ideTipoArea = :ideTipoArea) ");

		return areaDAO.buscarPorQuery(sql.toString(), parametros);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> filtrarListaAreas(Area pArea) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		
		criteria
			.createAlias("ideOrgao","o", JoinType.INNER_JOIN)
			.createAlias("ideAreaPai","apai", JoinType.INNER_JOIN)
			.createAlias("idePessoaFisica","fun", JoinType.LEFT_OUTER_JOIN)
			.createAlias("fun.pessoaFisica","pf", JoinType.LEFT_OUTER_JOIN)
		;
		
		if (!Util.isNull(pArea)) {
	
			criteria.add(Restrictions.eq("indExcluido", pArea.getIndExcluido()));
	
			if (!Util.isNull(pArea.getIdeArea())) {
				criteria.add(Restrictions.eq("ideArea", pArea.getIdeArea()));
			}
	
			if (!Util.isNull(pArea.getNomArea())) {
				criteria.add(Restrictions.ilike("nomArea", pArea.getNomArea(), MatchMode.END));
			}
		}
		
		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideArea"), "ideArea")
				.add(Projections.property("nomArea"), "nomArea")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")
				.add(Projections.property("indExcluido"), "indExcluido")
				.add(Projections.property("dtcExclusao"), "dtcExclusao")
				.add(Projections.property("ideTipoArea"), "ideTipoArea")
				.add(Projections.property("apai.ideArea"), "ideAreaPai.ideArea")
				.add(Projections.property("apai.nomArea"), "ideAreaPai.nomArea")
				.add(Projections.property("o.ideOrgao"), "ideOrgao.ideOrgao")
				.add(Projections.property("o.dscOrgao"), "ideOrgao.dscOrgao")
				.add(Projections.property("ideTipoArea"), "ideTipoArea")
				.add(Projections.property("fun.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.idePessoaFisica"), "idePessoaFisica.pessoaFisica.idePessoaFisica")
				.add(Projections.property("pf.nomPessoa"), "idePessoaFisica.pessoaFisica.nomPessoa")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Area.class))
		;
		
		try {
			return areaDAO.listarPorCriteria(criteria, Order.asc("nomArea"));
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> filtrarAreasPorOrgao(Orgao o) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("ideOrgao", o);
		params.put("indExcluido", false);
		return areaDAO.buscarPorNamedQuery("Area.findByOrgaoWithoutCotic", params);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area carregarGet(Integer ideArea)  {
		return buscarPorID(ideArea);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarArea(Area pArea) {
		pArea.setDtcCriacao(new Date());
		areaDAO.salvar(pArea);
		Pauta lPauta = pautaService.filtrarPauta(new Pauta(pArea));
		if (Util.isNullOuVazio(lPauta))
			lPauta = new Pauta();

		lPauta.setIdeArea(pArea);
		lPauta.setIdeTipoPauta(new TipoPauta(TipoPautaEnum.PAUTA_AREA.getTipo()));

		pautaService.salvar(lPauta);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Area pArea)  {
		areaDAO.atualizar(pArea);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void merge(Area pArea)  {
		areaDAO.merge(pArea);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirArea(Area pArea)  {

		pArea.setIndExcluido(true);
		pArea.setDtcExclusao(new Date());

		areaDAO.atualizar(pArea);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Area> listarPorRequerimento(RequerimentoUnico pRequerimento, Boolean isAtoRloRlu) throws Exception {

		List<Area> lista = new ArrayList<Area>();
		// Obtem área que sempre aparece
		Parametro parametro = parametroService.obterPorEnum(ParametroEnum.AREA_COINS);
		Integer idAreaCOINS = parametroService.obterValorInt(parametro);
		Area coins = areaDAO.carregarGet(idAreaCOINS);

		if (!coins.getIndExcluido()) {
			lista.add(coins);
		}

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideRequerimento", pRequerimento.getIdeRequerimentoUnico());
		parametros.put("ideAreaConstante", idAreaCOINS);
		// Busca areas por requerimento
		lista.addAll(areaDAO.buscarPorNamedQuery("Area.findByIdeRequerimento", parametros));
		
		if(existeAtoOutorga(pRequerimento.getIdeRequerimentoUnico())){
			parametros = new HashMap<String, Object>();
			parametros.put("ideArea", AreaEnum.NOUT.getId()); // NOUT
			lista.addAll(areaDAO.buscarPorNamedQuery("Area.findByIdeArea", parametros));
		}
		
		if (!Util.isNull(isAtoRloRlu) && isAtoRloRlu) {
			parametros = new HashMap<String, Object>();
			parametros.put("ideArea", 12); // COFIS
			lista.addAll(areaDAO.buscarPorNamedQuery("Area.findByIdeArea", parametros));
		}
		
		return lista;
	}

	private boolean existeAtoOutorga(Integer ideRequerimento)  {
		Collection<AtoAmbiental> atosOutroga = this.atoAmbientalService.filtrarListaAtoAmbientalOutorgaPorEnquadramentoRequerimento(ideRequerimento);
		return !Util.isNullOuVazio(atosOutroga);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Funcionario obterPessoaFisicaCoordenadorPorIdeArea(Integer ideArea) throws Exception {
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideArea", ideArea);
		
		Area area = areaDAO.buscarEntidadePorNamedQuery("Area.findPessoaFisicaByIdeArea", parametros);
		
		if(!Util.isNull(area) && !Util.isNull(area.getIdePessoaFisica())) {
			return area.getIdePessoaFisica();
		} else {
			JsfUtil.addErrorMessage("Não foi possível localizar o funcionário responsável pela área.");
			throw new SeiaException("Não foi possível localizar o funcionário responsável pela área.");
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area obterAreaFuncionarioCoordenadorPorIdeFuncionario(Integer idePessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		criteria.add(Restrictions.eq("idePessoaFisica.idePessoaFisica", idePessoaFisica));
		criteria.add(Restrictions.eq("indExcluido", Boolean.FALSE));

		return areaDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area buscarAreaPaiDoProcesso(Integer ideArea)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class,"area")
				.add(Restrictions.eq("area.ideArea", ideArea));
		
		Area primaria =  areaDAO.buscarPorCriteria(criteria);
		
		DetachedCriteria detachedcriteria = DetachedCriteria.forClass(Area.class,"area")
				.add(Restrictions.eq("area.ideArea", primaria.getIdeAreaPai().getIdeArea()));
		
		return areaDAO.buscarPorCriteria(detachedcriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area carregar(Area area) {
		return buscarPorID(area.getIdeArea());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area carregar(Integer ideArea) {
		return buscarPorID(ideArea);
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> listarAreaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		criteria.createAlias("tipologiaGrupoAreaCollection", "grupoArea");
		criteria.add(Restrictions.eq("grupoArea.ideTipologiaGrupo", tipologiaGrupo));
		return areaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> filtrarListaAreaSouce(Collection<Area> listAreas) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		Collection<Integer> ids = new ArrayList<Integer>();
		for (Area area : listAreas) {
			ids.add(area.getIdeArea());
		}
		// clausula not in
		Criterion in = Restrictions.in("ideArea", ids);
		criteria.add(Restrictions.not(in));
		return areaDAO.listarPorCriteria(criteria);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> filtrarListaAreaLike(Area area) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		criteria.add(Restrictions.ilike("nomArea", area.getNomArea(), MatchMode.START));
		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.addOrder(Order.asc("nomArea"));
		return areaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> obterAreaPorAreaPai(Area area) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		criteria.add(Restrictions.eq("ideAreaPai.ideArea", area.getIdeArea()));
		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.add(Restrictions.ne("ideTipoArea", 1));
		criteria.addOrder(Order.asc("nomArea"));
		return areaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> listaDiretoriasArea()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		criteria.add(Restrictions.eq("ideTipoArea", 1));
		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.addOrder(Order.asc("nomArea"));
		return areaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> listarCoordenacoes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		criteria.add(Restrictions.eq("ideTipoArea", TipoAreaEnum.COORDENACAO.getTipo()));
		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.addOrder(Order.asc("nomArea"));
		return areaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area>  listarCoordenacoesAnexo(Boolean isAtoRloRlu){
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);

		// Caso esteja enquadrado no ato RLO ou RLU, deve listar também a área COFIS
		if (isAtoRloRlu){
			criteria.add(Restrictions.in("ideArea", new Object[] { AreaEnum.COMIM.getId(), AreaEnum.COINE.getId(),
					AreaEnum.COTUR.getId(), AreaEnum.COINS.getId(), AreaEnum.COFIS.getId(), AreaEnum.COFAQ.getId(),
					AreaEnum.COASP.getId(), AreaEnum.COIND.getId(), AreaEnum.NOUT.getId(), AreaEnum.ATEND.getId()})); // COMIN,COINE,COTUR,COINS,COFIS,COFAQ,COASP,COIND,NOUT
		}else{
			criteria.add(Restrictions.in("ideArea", new Object[] { AreaEnum.COMIM.getId(), AreaEnum.COINE.getId(),
					AreaEnum.COTUR.getId(), AreaEnum.COINS.getId(), AreaEnum.COFAQ.getId(), AreaEnum.COASP.getId(),
					AreaEnum.COIND.getId() , AreaEnum.NOUT.getId(), AreaEnum.ATEND.getId()})); // COMIN,COINE,COTUR,COINS,COFAQ,COASP,COIND,NOUT
		}

		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.addOrder(Order.asc("nomArea"));
		return areaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> filtrarAreasUR() {
							
		return 	areaDAO.buscarPorNamedQuery("Area.findByUR");

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area filtrarAreaUrMunicipio(Municipio municipio) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideMunicipio", municipio.getIdeMunicipio());
		ArrayList<Area> listaAreas = (ArrayList<Area>) areaDAO.buscarPorNamedQuery("Area.findByMunicipio", parametros);
		if (!Util.isNullOuVazio(listaAreas))
			return listaAreas.get(0);
		return new Area();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area carregarCOGED() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		criteria.add(Restrictions.eq("ideArea", AreaEnum.COGED.getId()));// COGED
		criteria.add(Restrictions.eq("indExcluido", false));
		return areaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area carregarAreaByCriteria(Integer idArea) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		criteria.add(Restrictions.eq("ideArea", idArea));
		criteria.add(Restrictions.eq("indExcluido", false));
		return areaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Area> listarAreasByIds(Integer...areas) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Area.class)
				.add(Restrictions.in("ideArea", areas));
		
		return this.areaDAO.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area buscarAreaPorPessoaFisica(Integer idePessoaFisica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class);
		
		criteria
			.createAlias("funcionarioCollection", "func", JoinType.INNER_JOIN)
			.createAlias("func.pessoaFisica", "pf", JoinType.INNER_JOIN)
			.add(Restrictions.eq("pf.idePessoaFisica", idePessoaFisica));
		
		return areaDAO.buscarPorCriteria(criteria);
	}
	/**
	 * Método que retorna a área principal se {@link isAreaSecundaria} == false ou a área se secundária se {@link isAreaSecundaria} == true
	 * @author eduardo.fernandes
	 * @param processo
	 * @param isAreaSecundaria
	 * @return {@link Area}
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area buscarAreaDoProcessoByPauta(Processo processo, boolean isAreaSecundaria) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		criteria.createAlias("idePauta", "p", JoinType.INNER_JOIN);
		criteria.createAlias("p.ideArea", "a", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("a.idePessoaFisica","pf", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("ideProcesso.ideProcesso", processo.getIdeProcesso()));
		criteria.add(Restrictions.eq("ideStatusFluxo", new StatusFluxo(StatusFluxoEnum.FORMADO.getStatus())));
		criteria.add(Restrictions.eq("indAreaSecundaria", isAreaSecundaria));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("a.ideArea"), "ideArea")
				.add(Projections.property("a.ideAreaPai"), "ideAreaPai")
				.add(Projections.property("pf.idePessoaFisica"), "idePessoaFisica.idePessoaFisica"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Area.class));
		return areaDAO.buscarPorCriteria(criteria); 
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area buscaAreaProcessoIndAreaSecundariaTrue(Integer ideProcesso) {
		
		Area areaDiruc = new Area(AreaEnum.DIRUC.getId());
		DetachedCriteria criteria = DetachedCriteria.forClass(ControleTramitacao.class);
		
		criteria
			.createAlias("idePauta", "pauta", JoinType.INNER_JOIN)
			.createAlias("pauta.ideArea", "area", JoinType.INNER_JOIN)
			.createAlias("area.ideAreaPai", "areaPai", JoinType.INNER_JOIN)
			.createAlias("area.idePessoaFisica", "func", JoinType.INNER_JOIN)
			
			.add(Restrictions.eq("areaPai.ideArea", areaDiruc.getIdeArea()))
			.add(Restrictions.eq("ideProcesso.ideProcesso", ideProcesso))
			.add(Restrictions.eq("indAreaSecundaria", true))
			.add(Restrictions.eq("indFimDaFila", true))
						
			.setProjection(Projections.projectionList()
				.add(Projections.property("area.ideArea"),"ideArea")
				.add(Projections.property("func.idePessoaFisica"),"idePessoaFisica.idePessoaFisica")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Area.class))
		;
		
		return areaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Area> listarUnidadesRegionais()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(Area.class)
				.add(Restrictions.ilike("nomArea", "unidade regional", MatchMode.ANYWHERE))
				.add(Restrictions.eq("indExcluido", false));
		return areaDAO.listarPorCriteria(criteria);
	}
	
	public Area buscarPorID(Integer idArea) {
		
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(Area.class)
				
				.createAlias("ideOrgao","o", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideAreaPai","apai", JoinType.LEFT_OUTER_JOIN)
				.createAlias("idePessoaFisica","fun", JoinType.LEFT_OUTER_JOIN)
				.createAlias("fun.pessoaFisica","pf", JoinType.LEFT_OUTER_JOIN)
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideArea"),"ideArea")
					.add(Projections.property("nomArea"),"nomArea")
					.add(Projections.property("dtcCriacao"),"dtcCriacao")
					.add(Projections.property("indExcluido"),"indExcluido")
					.add(Projections.property("dtcExclusao"),"dtcExclusao")
					.add(Projections.property("ideTipoArea"),"ideTipoArea")
					
					.add(Projections.property("apai.ideArea"), "ideAreaPai.ideArea")
					.add(Projections.property("apai.nomArea"), "ideAreaPai.nomArea")
					.add(Projections.property("o.ideOrgao"), "ideOrgao.ideOrgao")
					.add(Projections.property("o.dscOrgao"), "ideOrgao.dscOrgao")
					.add(Projections.property("ideTipoArea"), "ideTipoArea")
					.add(Projections.property("fun.idePessoaFisica"), "idePessoaFisica.idePessoaFisica")
					.add(Projections.property("pf.idePessoaFisica"), "idePessoaFisica.pessoaFisica.idePessoaFisica")
					.add(Projections.property("pf.nomPessoa"), "idePessoaFisica.pessoaFisica.nomPessoa")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(Area.class))
				
				.add(Restrictions.eq("ideArea", idArea));
				
			return areaDAO.buscarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}