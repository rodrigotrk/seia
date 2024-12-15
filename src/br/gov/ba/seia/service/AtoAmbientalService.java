package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.AtoAmbientalDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.AtoAmbientalTipologia;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.GrupoProcesso;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.entity.ParametroTaxaCertificado;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.SolicitacaoAdministrativoAtoAmbiental;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.CrudOperationEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

/**
 * @author mario.junior
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtoAmbientalService {

	@EJB
	private DocumentoAtoAmbientalService docAtoAmbientalService; 
	
	@EJB
	private ParametroService parametroService; 
	
	@EJB 
	private AtoAmbientalDAOImpl atoAmbientalDAOImpl;
	
	@Inject
	private IDAO<AtoAmbiental> atoAmbientalDAO;
	
	@Inject
	private IDAO<SolicitacaoAdministrativoAtoAmbiental> solicitacaoAdministrativoAtoAmbientalDAO;
	
	@Inject
	private IDAO<AtoAmbientalTipologia> atoAmbientalTipologiaDAO;
	
	@Inject
	private IDAO<GrupoProcesso> grupoProcessoDAO;
	
	@Inject
	private IDAO<ParametroTaxaCertificado> parametroTaxaCertificadoDao;

	
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarAtoAmbiental(AtoAmbiental ato) {
		atoAmbientalDAO.salvarOuAtualizar(ato);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtoAmbiental(AtoAmbiental ato)  {
		atoAmbientalDAO.persistir(ato, CrudOperationEnum.INSERT);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarAtoAmbiental(AtoAmbiental ato) {
		atoAmbientalDAO.persistir(ato, CrudOperationEnum.UPDATE);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAtoAmbiental(AtoAmbiental ato) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideAtoAmbiental", ato.getIdeAtoAmbiental());
		atoAmbientalDAO.executarNamedQuery("AtoAmbiental.removeByIdeAtoAmbiental", params);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtoAmbientalEmLotes(List<AtoAmbiental> atosAmbientais,Enquadramento enquadramento) {
		
		for (AtoAmbiental atoAmbiental : atosAmbientais) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideAtoAmbiental", atoAmbiental.getIdeAtoAmbiental());
			params.put("ideEnquadramento", enquadramento.getIdeEnquadramento());
			atoAmbientalDAO.executarNamedQuery("AtoAmbiental.inserirEnquadramentoAto", params);
		}
		
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbiental()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		return atoAmbientalDAO.listarPorCriteria(criteria, Order.asc("nomAtoAmbiental"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Boolean isAtoExigeTipologia(Integer ideAto) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbientalTipologia.class)
			.createAlias("ideAtoAmbiental", "ato")
			.add(Restrictions.eq("ato.ideAtoAmbiental", ideAto));	
			
		List<AtoAmbientalTipologia> a = atoAmbientalTipologiaDAO.listarPorCriteria(criteria);
		
		return !Util.isNullOuVazio(a);
				
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoAmbiental> listarAtosParaRequerimentosEnquadradosSemTipologia() {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		
		criteria
			.createAlias("ideTipoAto", "tipoAto")
			
			.add(Restrictions.eq("indAtivo", true))	
			.add(Restrictions.or(
					Restrictions.in("ideAtoAmbiental", Arrays.asList(new Integer[] {
							AtoAmbientalEnum.REVISAO_CONDICIONANTE.getId(),
							AtoAmbientalEnum.PRORROGACAO_AUTORIZACAO.getId(),
							AtoAmbientalEnum.PRORROGACAO_LICENCA.getId()})),
					Restrictions.not(Restrictions.in("tipoAto.ideTipoAto", Arrays.asList(new Integer[] {
							TipoAtoEnum.LICENCA.getId(),
							TipoAtoEnum.JURIDICO.getId()})))
				)
			)
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
				.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
			)
			
			.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class))
		;
		
		return atoAmbientalDAO.listarPorCriteria(criteria);
				
	}
			

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalOrderByAsc(TipoAto tipoAto)  {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		criaSqlListAtoByTipoAto(tipoAto, sql, params);
		
		return atoAmbientalDAO.listarPorQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalByTipoAtoByAtivo(TipoAto tipoAto)  {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<String, Object>();
		
		sql.append("select aa ");
		sql.append("from AtoAmbiental aa ");
		if(tipoAto != null && tipoAto.getIdeTipoAto() != null){
			sql.append("where aa.ideTipoAto = :tipoAto ");
			sql.append("and aa.indAtivo = :indAtivo ");
			params.put("tipoAto", tipoAto);
			params.put("indAtivo", true);
		}
		sql.append("order by aa asc ");
		
		
		return atoAmbientalDAO.listarPorQuery(sql.toString(), params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalPorTipoAto(TipoAto tipoAto, boolean verificaIndAtivo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
				
		criteria.add(Restrictions.eq("ideTipoAto", tipoAto));
		
		if(verificaIndAtivo) criteria.add(Restrictions.eq("indAtivo", true));
		
		return atoAmbientalDAO.listarPorCriteria(criteria);
	}

	/**
	 * @param tipoAto
	 * @param sql
	 * @param params
	 */
	private void criaSqlListAtoByTipoAto(TipoAto tipoAto, StringBuilder sql, Map<String, Object> params) {
		sql.append("select aa ");
		sql.append("from AtoAmbiental aa ");
		if(tipoAto != null && tipoAto.getIdeTipoAto() != null){
			sql.append("where aa.ideTipoAto = :tipoAto ");
			params.put("tipoAto", tipoAto);
		}
		sql.append("order by aa asc ");
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalDLA(RequerimentoUnico requerimentoUnico) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		if (requerimentoUnico.getIndDla()) {
			criteria.add(Restrictions.ne("ideTipoAto.ideTipoAto", 1));
		}
		criteria.add(Restrictions.eq("this.indAtivo", true));
		return atoAmbientalDAO.listarPorCriteria(criteria, Order.asc("nomAtoAmbiental"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalParams(Map<String, Object> params) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		if(params.get("nomeAto") != null){
			criteria.add(Restrictions.like("nomAtoAmbiental", (String) params.get("nomeAto"), MatchMode.ANYWHERE));
		}

		return atoAmbientalDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtoAmbiental getAtoAmbientalParams(String nomeAto) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		criteria.add(Restrictions.eq("nomAtoAmbiental", nomeAto));

		return atoAmbientalDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtoAmbiental obterAtoAmbiental(AtoAmbiental ato)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideAtoAmbiental", ato.getIdeAtoAmbiental());
		return atoAmbientalDAO.buscarEntidadePorNamedQuery("AtoAmbiental.findByIdeAtoAmbiental", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalPorTipoAto(Integer ideTipoAto, boolean isTLA)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		
		criteria
			.createAlias("ideTipoAto", "ta", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ta.ideTipoAto",ideTipoAto));
	
			if(isTLA){
				criteria
					.add(Restrictions.eq("indAutomatico",false))
					.add(Restrictions.eq("indAtivo",true));
			}
			
			criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
				.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
				.add(Projections.property("ta.ideTipoAto"),"ideTipoAto.ideTipoAto")
				.add(Projections.property("ta.nomTipoAto"),"ideTipoAto.nomTipoAto")
				
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class))
		;
		
		return atoAmbientalDAO.listarPorCriteria(criteria, Order.asc("nomAtoAmbiental"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalPorTipoAtoAtivo(Integer ideTipoAto, boolean isTLA)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		
		criteria
			.createAlias("ideTipoAto", "ta", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ta.ideTipoAto",ideTipoAto))
			.add(Restrictions.eq("indAtivo",true));
			if(isTLA){
				criteria
					.add(Restrictions.eq("indAutomatico",false));
					
			}
			
			criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
				.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
				.add(Projections.property("ta.ideTipoAto"),"ideTipoAto.ideTipoAto")
				.add(Projections.property("ta.nomTipoAto"),"ideTipoAto.nomTipoAto")
				
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class))
		;
		
		return atoAmbientalDAO.listarPorCriteria(criteria, Order.asc("nomAtoAmbiental"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalPorTipoAtoParaReenquadramento(Integer ideTipoAto, boolean isTLA)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		
		criteria
		.createAlias("ideTipoAto", "ta", JoinType.INNER_JOIN)
		
		.add(Restrictions.eq("ta.ideTipoAto",ideTipoAto))
		.add(Restrictions.eq("indAtivo",true))
		.add(Restrictions.not(Restrictions.in("ideAtoAmbiental", new Integer[]{
				AtoAmbientalEnum.LAC.getId(),
				AtoAmbientalEnum.LOA.getId(),
				AtoAmbientalEnum.RLAC.getId(),
				AtoAmbientalEnum.ARLS.getId(),
				AtoAmbientalEnum.MNP.getId(),
				AtoAmbientalEnum.TLA.getId(),
				AtoAmbientalEnum.APE.getId(),
				AtoAmbientalEnum.COUT.getId(),
				AtoAmbientalEnum.CRF.getId(),
				AtoAmbientalEnum.INEXIGIBILIDADE.getId(),
				AtoAmbientalEnum.DIAP.getId(),
				//Desabilitado a restrição da categoria "Licença" do ato ambiental LA - ticket 33648
				//AtoAmbientalEnum.LA.getId(),
				AtoAmbientalEnum.DQC.getId(),
				AtoAmbientalEnum.DTRP.getId(),
				AtoAmbientalEnum.CORTE_FLORESTA_PRODUCAO.getId(),
				AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO.getId(),
				AtoAmbientalEnum.LIMPEZA_AREA.getId(),
		
		})));
		
		if(isTLA){
			criteria
			.add(Restrictions.eq("indAutomatico",false));
		}
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
				.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
				.add(Projections.property("ta.ideTipoAto"),"ideTipoAto.ideTipoAto")
				.add(Projections.property("ta.nomTipoAto"),"ideTipoAto.nomTipoAto")
		)
		
		.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class))
		;
		
		return atoAmbientalDAO.listarPorCriteria(criteria, Order.asc("nomAtoAmbiental"));
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> obterAtoAmbientalPorTipoAto(Integer ideTipoAto)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		
		criteria
			.createAlias("ideTipoAto", "ta", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ta.ideTipoAto",ideTipoAto));
	
	
			criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
				.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
				.add(Projections.property("ideTipoAto.ideTipoAto"),"ideTipoAto.ideTipoAto")
			//	.add(Projections.property("ideTipoAto.nomTipoAto"),"ideTipoAto.nomTipoAto")
				
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class))
		;
		
		return atoAmbientalDAO.listarPorCriteria(criteria, Order.asc("nomAtoAmbiental"));
	}
	/**
	 * Retorna os atos ambientais descritos no parametro volume florestal
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> obterAtoAmbientalVolumeFlorestal() {
		Parametro ideAtosAmbientais = parametroService.obterPorId(ParametroEnum.VOLUME_FLORESTAL.getIdeParametro());
		
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		
		if(!Util.isNullOuVazio(ideAtosAmbientais)){
			criteria.add(Restrictions.in("ideAtoAmbiental", Util.stringToArrayInt(ideAtosAmbientais.getDscValor())));
		}
		
		return atoAmbientalDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalByTipoForSearch(Integer ideTipoAto) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AtoAmbiental.class)
		.add(Restrictions.eq("ideTipoAto.ideTipoAto", ideTipoAto));
		return atoAmbientalDAO.listarPorCriteria(detachedCriteria,Order.asc("nomAtoAmbiental"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtosPorProcesso(Integer ideProcesso) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideProcesso", ideProcesso);
		return atoAmbientalDAO.buscarPorNamedQuery("AtoAmbiental.findByIdeProcesso", parameters);
	}

	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> filtrarListaAtoAmbiental(AtoAmbiental atoAmbiental) {
		return atoAmbientalDAO.listarPorExemplo(atoAmbiental);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalTipologiaGrupo(TipologiaGrupo tipologiaGrupo)  {
		 DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		 criteria.createAlias("tipologiaTipoAtoCollection", "grupoTipoAtoAmbiental");
		 criteria.add(Restrictions.eq("grupoTipoAtoAmbiental.ideTipologiaGrupo", tipologiaGrupo));
		 return atoAmbientalDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> filtrarListaAtoAmbientalSouce(Collection<AtoAmbiental> listAtoAmbiental)  {
		 DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		 Collection<Integer> ids = new ArrayList<Integer>();
		 for (AtoAmbiental atoAmbiental : listAtoAmbiental) {
			ids.add(atoAmbiental.getIdeAtoAmbiental());
		}
		 // clausula not in
		 Criterion in = Restrictions.in("ideAtoAmbiental", ids);
		 criteria.add(Restrictions.not(in));  
		 return atoAmbientalDAO.listarPorCriteria(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoAmbiental> filtrarListaAtosAmbientalPorProcessos(List<VwConsultaProcesso> listaProcessos)  {
		 Integer[] idsDosProcessosIntegers = new Integer[listaProcessos.size()];
		 for (int i = 0; i < listaProcessos.size(); i++) {
			idsDosProcessosIntegers[i] = listaProcessos.get(i).getIdeProcesso(); 
		}
		 DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class);
		 criteria.createAlias("processoAtoCollection", "processoAto");
		 criteria.add(Restrictions.in("processoAto.processo.ideProcesso", idsDosProcessosIntegers));
		 return atoAmbientalDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarDocumentosAtoAmbientalWhereIn(Collection<AtoAmbiental> pAtoAmbiental) throws Exception {
		List<Integer> idsAtos = new ArrayList<Integer>(); 
		ArrayList<AtoAmbiental> atosAmbientaisDistinct = new ArrayList<AtoAmbiental>();
		for (AtoAmbiental lAtoAmbiental : pAtoAmbiental) {
			idsAtos.add(lAtoAmbiental.getIdeAtoAmbiental());
		}
		
		DetachedCriteria criteria = getCriteriaListarDocumentosAtoAmbientalWhereIn(idsAtos);

		ArrayList<AtoAmbiental> atosAmbientais =  (ArrayList<AtoAmbiental>) atoAmbientalDAO.listarPorCriteria(criteria);
		
		for (AtoAmbiental lAtoAmbiental : atosAmbientais) {
			if (!atosAmbientaisDistinct.contains(lAtoAmbiental)) {
				lAtoAmbiental.setDocumentoAtoCollection(docAtoAmbientalService.listarDocumentoAtoWhereInAto(lAtoAmbiental));
				atosAmbientaisDistinct.add(lAtoAmbiental);
			}
		}
		return atosAmbientaisDistinct;
	}


	private DetachedCriteria getCriteriaListarDocumentosAtoAmbientalWhereIn(List<Integer> idsAtos) {
		return DetachedCriteria.forClass(AtoAmbiental.class)
				.createAlias("documentoAtoCollection", "docAto", JoinType.INNER_JOIN)
				.createAlias("docAto.ideDocumentoObrigatorio", "docObr", JoinType.INNER_JOIN)
				.add(Restrictions.in("ideAtoAmbiental", idsAtos))
				.add(Restrictions.eq("docAto.indAtivo", Boolean.TRUE))
				.add(Restrictions.isNull("docAto.ideTipologia"))
				.add(Restrictions.eq("docObr.indFormulario", Boolean.FALSE))
				.setProjection(Projections.distinct(
									Projections.projectionList()
										.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
										.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
										.add(Projections.property("docAto.ideDocumentoAto"),"documentoAtoCollection.ideDocumentoAto")
									)
								)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class));
		
	}

	public List<ParametroTaxaCertificado> buscarParametrosTaxaPorRequerimentoUnico(Integer ideRequerimentoUnico) {
		DetachedCriteria criteria =  DetachedCriteria.forClass(ParametroTaxaCertificado.class)
				.createAlias("atoAmbiental", "ato")
				.createAlias("ato.enquadramentoCollection", "enq")
				.createAlias("enq.ideRequerimentoUnico", "req");
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ato.ideAtoAmbiental"),"atoAmbiental.ideAtoAmbiental")
				.add(Projections.property("ato.nomAtoAmbiental"),"atoAmbiental.nomAtoAmbiental")
				.add(Projections.property("valTaxaCertificado"),"valTaxaCertificado"));
		
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(ParametroTaxaCertificado.class));
		
		criteria.add(Restrictions.eq("req.ideRequerimentoUnico", ideRequerimentoUnico));
		
		return parametroTaxaCertificadoDao.listarPorCriteria(criteria);
		
	}

	public ParametroTaxaCertificado buscarValorDeTaxaReservaLegal(boolean maiorTaxa)  {
		DetachedCriteria criteria =  DetachedCriteria.forClass(ParametroTaxaCertificado.class)
				.createAlias("atoAmbiental", "ato");
		
		ProjectionList projectionList = Projections.projectionList();		
		
		if(maiorTaxa){
			projectionList.add(Projections.max("valTaxaCertificado"),"valTaxaCertificado");
		}else{
			projectionList.add(Projections.min("valTaxaCertificado"),"valTaxaCertificado");
		}
		
		criteria.setProjection(projectionList);		
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(ParametroTaxaCertificado.class));
		criteria.add(Restrictions.eq("ato.ideAtoAmbiental", AtoAmbientalEnum.ARLSF.getId()));
		
		return parametroTaxaCertificadoDao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> filtrarListaAtoAmbientalPorEnquadramentoRequerimento(Integer ideRequerimento)  {
		DetachedCriteria criteria = filtroAtosRequerimento(ideRequerimento);
		return atoAmbientalDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> filtrarListaAtoAmbientalOutorgaPorEnquadramentoRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = filtroAtosRequerimento(ideRequerimento);
		criteria.add(Restrictions.eq("ideTipoAto.ideTipoAto", TipoAtoEnum.OUTORGA.getId()));
		return atoAmbientalDAO.listarPorCriteria(criteria);
	}
	

	private DetachedCriteria filtroAtosRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria =  DetachedCriteria.forClass(AtoAmbiental.class)
				.createAlias("ideTipoAto", "tipoAto")
				.createAlias("enquadramentoCollection", "enq")
				.createAlias("enq.ideRequerimento", "req", JoinType.LEFT_OUTER_JOIN)
				.createAlias("enq.ideRequerimentoUnico", "reqUnico", JoinType.LEFT_OUTER_JOIN);
		criteria.setProjection(
				Projections.projectionList()
				.add(Projections.property("ideAtoAmbiental").as("ideAtoAmbiental"))
				.add(Projections.property("nomAtoAmbiental").as("nomAtoAmbiental"))
				.add(Projections.property("ideTipoAto.ideTipoAto").as("ideTipoAto.ideTipoAto"))
		);
		
		Disjunction disjunction = Restrictions.disjunction();
		
		disjunction.add(Restrictions.eq("req.ideRequerimento",ideRequerimento));
		disjunction.add(Restrictions.eq("reqUnico.ideRequerimentoUnico",ideRequerimento));
		
		criteria.add(disjunction);
		
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class));
		return criteria;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalPorEnquadramentoRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = filtroAtosNovoRequerimento(ideRequerimento);
		return atoAmbientalDAO.listarPorCriteria(criteria);
	}
	
	public DetachedCriteria filtroAtosNovoRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria =  DetachedCriteria.forClass(AtoAmbiental.class)
				.createAlias("enquadramentoCollection", "enq")
				.createAlias("enq.ideRequerimento", "req");
		criteria.setProjection(
				Projections.projectionList()
				.add(Projections.property("ideAtoAmbiental").as("ideAtoAmbiental"))
				.add(Projections.property("nomAtoAmbiental").as("nomAtoAmbiental"))
				.add(Projections.property("indDeclaratorio").as("indDeclaratorio"))
				.add(Projections.property("sglAtoAmbiental").as("sglAtoAmbiental"))
		);
		criteria.add(Restrictions.eq("req.ideRequerimento",ideRequerimento));
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class));
		return criteria;
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoAmbiental> listarTodosAtos() {
		return atoAmbientalDAO.listarTodos();
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtoAmbientalSolicitacaoAdministrativo(SolicitacaoAdministrativo solicitacaoAdministrativo) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class)
				
				.createAlias ("solicitacaoCollection" ,"solicitacao")
				.setFetchMode("solicitacaoCollection", FetchMode.JOIN)
				.setFetchMode("solicitacao.solicitacaoAdminstrativoAtoAmbientalCollection", FetchMode.JOIN)
			 .add(Restrictions.eq("solicitacao.ideSolicitacaoAdministrativo", solicitacaoAdministrativo.getIdeSolicitacaoAdministrativo()));
		
		 return atoAmbientalDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia  getTipologiaByAtoBySolicitacaoAdministrativo(SolicitacaoAdministrativo sa, AtoAmbiental atoAmbiental) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativoAtoAmbiental.class)
			 
			 .createAlias("ideTipologia", "ideTipologia")
			 .add(Restrictions.eq("ideSolicitacaoAdministrativo", sa))
			 .add(Restrictions.eq("ideAtoAmbiental", atoAmbiental))			 ;
		 
		if(!Util.isNullOuVazio(solicitacaoAdministrativoAtoAmbientalDAO.buscarPorCriteria(criteria).getIdeTipologia())){
			return solicitacaoAdministrativoAtoAmbientalDAO.buscarPorCriteria(criteria).getIdeTipologia();			
		}else {
			return null;
		}
		
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> listarAtosEnquadradosByRequerimento(Integer ideRequerimento) {

		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class)
				.createAlias("enquadramentoAtoAmbientalCollection", "eaac")
				.createAlias("eaac.tipologia", "ti", JoinType.LEFT_OUTER_JOIN)
				.createAlias("eaac.enquadramento", "enq")
				.createAlias("enq.ideRequerimento", "req")
				.createAlias("ideTipoAto","ta", JoinType.LEFT_OUTER_JOIN )
				
		.setProjection(Projections.projectionList()
				.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
				.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
				.add(Projections.property("ta.ideTipoAto"),"ideTipoAto.ideTipoAto")
				.add(Projections.property("ta.nomTipoAto"),"ideTipoAto.nomTipoAto")
				.add(Projections.property("ta.dscSiglaTipoAto"),"ideTipoAto.dscSiglaTipoAto")
				
		).setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class))		
				
		.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));	
		
		return atoAmbientalDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countAtosEnquadradosBySolicitacaoAdminstrativo(Integer ideRequerimento) {

		DetachedCriteria criteria = DetachedCriteria.forClass(SolicitacaoAdministrativo.class, "sol")
					
				.createAlias("sol.solicitacaoAdminstrativoAtoAmbientalCollection", "solAdm", JoinType.INNER_JOIN)
				.createAlias("sol.ideRequerimento","req")
				.createAlias("solAdm.ideAtoAmbiental", "ato")
				
		.setProjection(Projections.projectionList()
				.add(Projections.property("ato.ideAtoAmbiental"),"ato.ideAtoAmbiental")
				.add(Projections.property("ato.nomAtoAmbiental"),"ato.nomAtoAmbiental")
				
		).setResultTransformer(new AliasToNestedBeanResultTransformer(SolicitacaoAdministrativo.class))		
			
		.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));	
		
		return atoAmbientalDAO.count(criteria);
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public GrupoProcesso carregarGrupoProcessoByRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class)
				.createAlias("enquadramentoAtoAmbientalCollection", "eaac")
				.createAlias("eaac.enquadramento", "enq")
				.createAlias("enq.ideRequerimento", "req")
				.createAlias("ideTipoAto", "tipo")
				.createAlias("tipo.ideGrupoProcesso", "gp")
				
				.setProjection(Projections.distinct(
						Projections.projectionList()
							.add(Projections.property("gp.dscSiglaGrupoProcesso"),"dscSiglaGrupoProcesso")
						)
				).setResultTransformer(new AliasToNestedBeanResultTransformer(GrupoProcesso.class))		
						
			.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()));	
		
		return grupoProcessoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AtoAmbiental> isTodosAtosDeclaratorios(Integer ideRequerimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class)
				.createAlias("enquadramentoAtoAmbientalCollection", "eaac")
				.createAlias("eaac.enquadramento", "enq")
				.createAlias("enq.ideRequerimento", "req")
				
		.setProjection(Projections.projectionList()
				.add(Projections.property("ideAtoAmbiental"),"ideAtoAmbiental")
				.add(Projections.property("nomAtoAmbiental"),"nomAtoAmbiental")
				.add(Projections.property("indDeclaratorio"),"indDeclaratorio")
		).setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbiental.class))		
				
		.add(Restrictions.eq("req.ideRequerimento", ideRequerimento))
		.add(Restrictions.eq("enq.indEnquadramentoAprovado", true));	
		
		return atoAmbientalDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtoAmbiental carregarById(Integer ideAtoAmbiental) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AtoAmbiental.class)
			.add(Restrictions.eq("this.ideAtoAmbiental", ideAtoAmbiental));
		
		return this.atoAmbientalDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoAmbiental> listarAtosTLA() {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class).add(Restrictions.eq("indVisivelSolicitacaoTla",true));
		return atoAmbientalDAO.listarPorCriteria(criteria, Order.asc("nomAtoAmbiental"));
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoAmbientalTipologia> listarAtoAmbientalTipologiaByIsVisivelTLA (Boolean isVisivelTla) {
	
			DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbientalTipologia.class)
					.createAlias("ideTipologia"   ,"ideTipologia")
					.createAlias("ideAtoAmbiental","ideAtoAmbiental")
					.add(Restrictions.eq("ideAtoAmbiental.indAtivo",true))
					.add(Restrictions.eq("ideTipologia.indExcluido",false))
					.addOrder(Order.asc("ideAtoAmbiental.nomAtoAmbiental"))
					.addOrder(Order.asc("ideTipologia.desTipologia"))
			
			.setProjection(Projections.projectionList()
					
					.add(Projections.property("ideAtoAmbientalTipologia"),"ideAtoAmbientalTipologia")

					.add(Projections.property("ideAtoAmbiental.ideAtoAmbiental"),"ideAtoAmbiental.ideAtoAmbiental")
					.add(Projections.property("ideAtoAmbiental.nomAtoAmbiental"),"ideAtoAmbiental.nomAtoAmbiental")
					.add(Projections.property("ideAtoAmbiental.indDeclaratorio"),"ideAtoAmbiental.indDeclaratorio")
					
					.add(Projections.property("ideTipologia.ideTipologia"),"ideTipologia.ideTipologia")
					.add(Projections.property("ideTipologia.desTipologia"),"ideTipologia.desTipologia")
					.add(Projections.property("ideTipologia.codTipologia"),"ideTipologia.codTipologia")
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(AtoAmbientalTipologia.class));		
			
			if(isVisivelTla){
				criteria.add(Restrictions.eq("ideAtoAmbiental.indVisivelSolicitacaoTla",isVisivelTla));
			}
		return atoAmbientalTipologiaDAO.listarPorCriteria(criteria);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoAmbiental> listarAtoAmbientalSemTipologiaByIsVisivelTLA (Boolean isVisivelTla, List<Integer> ideAtos)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class)
			.add(Restrictions.not(Restrictions.in("ideAtoAmbiental", ideAtos)))
			.addOrder(Order.asc("nomAtoAmbiental"))
			.add(Restrictions.eq("indAtivo",true));
				
		if(isVisivelTla){
			criteria.add(Restrictions.eq("indVisivelSolicitacaoTla",isVisivelTla));
		}
		
		return atoAmbientalDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoAmbiental> listarAtoAmbientalPorDemanda(String nomAtoAmbiental, int first, int pageSize){
		return atoAmbientalDAOImpl.listarAtoAmbientalPorDemanda(nomAtoAmbiental, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countAtoAmbiental(String nomAtoAmbiental) {
		return atoAmbientalDAOImpl.countAtoAmbiental(nomAtoAmbiental);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtoAmbiental obterAtoAmbientalPorIde(Integer ideAtoAmbiental)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AtoAmbiental.class)
			.createAlias("ideTipoAto", "tipo")
			.add(Restrictions.eq("this.ideAtoAmbiental", ideAtoAmbiental));
		
		return this.atoAmbientalDAO.buscarPorCriteria(detachedCriteria);
	}

	public Collection<AtoAmbiental> listarAtosEnquadradosByReenquadramento(ProcessoReenquadramento reenquadramento) {

		return null;
	}
}
