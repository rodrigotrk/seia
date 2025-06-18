package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.ModalidadeOutorga;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoUsoRecursoHidricoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoFinalidadeUsoAguaService {
	
	@Inject
	private IDAO<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoFinalidadeUsoAgua> buscarFinalidadeUsoAguaByIdeTipologia(Integer ideTipologia)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideTipologia", ideTipologia);
		params.put("indRequerimento", true);
		return tipoFinalidadeUsoAguaIDAO.buscarPorNamedQuery("TipoFinalidadeUsoAgua.findTipoFinalidadeAguaByTipologia", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoFinalidadeUsoAgua carregar(Integer ideTipoFinalidadeUsoAgua) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideTipoFinalidadeUsoAgua", ideTipoFinalidadeUsoAgua);
		params.put("indRequerimento", true);
		return tipoFinalidadeUsoAguaIDAO.obterPorNamedQuery("TipoFinalidadeUsoAgua.findByIdeTipoFinalidadeUsoAgua", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> buscarFinalidadeUsoAguaByIdeTipologiaAndAto(EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideTipologia", enquadramentoAtoAmbiental.getTipologia());
		params.put("ideAtoAmbiental", enquadramentoAtoAmbiental.getAtoAmbiental());
		params.put("indRequerimento", true);
		return tipoFinalidadeUsoAguaIDAO.buscarPorNamedQuery("TipoFinalidadeUsoAgua.findTipoFinalidadeAguaByTipologiaAndAto", params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoFinalidadeUsoAgua> listarFinalidadesByRequerimentoByModalidadeByTipologia(Requerimento ideRequerimento, ModalidadeOutorga ideModalidade, Tipologia tipologia) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Outorga.class, "outorga");	
		criteria.createAlias("ideModalidadeOutorga", "modalidade", JoinType.INNER_JOIN);
		criteria.createAlias("ideTipoSolicitacao", "tipoSolicitacao", JoinType.INNER_JOIN);
		criteria.createAlias("outorgaLocalizacaoGeograficaCollection", "outorgaLocGeo", JoinType.INNER_JOIN);
		criteria.createAlias("tipoCaptacaoCollection", "tipoCaptacao", JoinType.LEFT_OUTER_JOIN);
		
		if (ModalidadeOutorgaEnum.INTERVENCAO.getIdModalidade().equals(ideModalidade.getIdeModalidadeOutorga())) {
			criteria.createAlias("outorgaLocGeo.tipoIntervencao", "tipoIntervencao", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("tipoIntervencao.ideTipoFinalidadeUsoAgua", "tipoFinalidadeUsoAgua", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("tipoFinalidadeUsoAgua.indRequerimento", true));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.distinct(Projections.property("tipoIntervencao.ideTipoFinalidadeUsoAgua")))
					.add(Projections.property("tipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua"),"nomTipoFinalidadeUsoAgua")
					.add(Projections.property("tipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua")
					.add(Projections.property("tipoFinalidadeUsoAgua.indRequerimento"),"indRequerimento")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoFinalidadeUsoAgua.class));
		}else{
			criteria.createAlias("outorgaLocGeo.outorgaLocalizacaoGeograficaFinalidadeCollection", "outLocGeoFinalidade", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("outLocGeoFinalidade.ideTipoFinalidadeUsoAgua", "tipoFinalidadeUsoAgua", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.eq("tipoFinalidadeUsoAgua.indRequerimento", true));
			criteria.setProjection(Projections.projectionList()
					.add(Projections.distinct(Projections.property("outLocGeoFinalidade.ideTipoFinalidadeUsoAgua")))
					.add(Projections.property("tipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua"),"nomTipoFinalidadeUsoAgua")
					.add(Projections.property("tipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua")
					.add(Projections.property("tipoFinalidadeUsoAgua.indRequerimento"),"indRequerimento")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoFinalidadeUsoAgua.class));
			if (ideModalidade.getIdeModalidadeOutorga().equals(ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade())) {
				criteria.add(Restrictions.eq("tipoCaptacao.ideTipologia.ideTipologia", tipologia.getIdeTipologia()));
			}
		}
		
		criteria.add(Restrictions.eq("ideRequerimento", ideRequerimento));
		criteria.add(Restrictions.eq("ideModalidadeOutorga", ideModalidade));
		criteria.add(Restrictions.eq("ideTipoSolicitacao.ideTipoSolicitacao", 4));
		
		List<TipoFinalidadeUsoAgua> listTipoFinalidadeUsoAgua = tipoFinalidadeUsoAguaIDAO.listarPorCriteria(criteria);

		return listTipoFinalidadeUsoAgua;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAgua() throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoFinalidadeUsoAgua.class);
		criteria
			.addOrder(Order.asc("nomTipoFinalidadeUsoAgua"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua")
				.add(Projections.property("nomTipoFinalidadeUsoAgua"),"nomTipoFinalidadeUsoAgua")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoFinalidadeUsoAgua.class))
		;
		
		return tipoFinalidadeUsoAguaIDAO.listarPorCriteria(criteria);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAgua(List<Integer> listaveis) throws Exception{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoFinalidadeUsoAgua.class);
		criteria
			.addOrder(Order.asc("nomTipoFinalidadeUsoAgua"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua")
				.add(Projections.property("nomTipoFinalidadeUsoAgua"),"nomTipoFinalidadeUsoAgua")
			)
			.add(Restrictions.in("ideTipoFinalidadeUsoAgua", listaveis))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoFinalidadeUsoAgua.class))
		;
		
		return tipoFinalidadeUsoAguaIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> buscarAllTipoFinalidadeUsoAgua() {
		return tipoFinalidadeUsoAguaIDAO.listarTodos();
	} 

	/**
	 * 
	 * Método que irá listar os {@link TipoFinalidadeUsoAgua} que estão vinculados aos FCE's analisados pelo {@link Processo}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 04/04/2017
	 * @param numProcesso
	 * @param tipoUsoRecursoHidricoEnum
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAguaBy(String numProcesso, TipoUsoRecursoHidricoEnum tipoUsoRecursoHidricoEnum) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoFinalidadeUsoAgua.class)
			.createAlias("fceOutorgaLocalizacaoGeograficaFinalidadeCollection", "folgf")
			.createAlias("folgf.ideFceOutorgaLocalizacaoGeografica", "folg")
			.createAlias("folg.ideFce", "f")
			.createAlias("f.ideAnaliseTecnica", "at")
			.createAlias("at.ideProcesso", "p");
		if(tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL)) {
			criteria.createAlias("folg.ideFceCaptacaoSuperficial", "fcs");
		} 
		else if(tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA)) {
			criteria.createAlias("folg.ideFceCaptacaoSubterranea", "fcs");
		}
		else if(tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.LANCAMENTO_EFLUENTE)) {
			criteria.createAlias("folg.ideFceLancamentoEfluente", "fle");
		}
		criteria.add(Restrictions.eq("folgf.indCaptacao", tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUPERFICIAL) || tipoUsoRecursoHidricoEnum.equals(TipoUsoRecursoHidricoEnum.CAPTACAO_SUBTERRANEA)))
			.add(Restrictions.eq("indRequerimento", true))
			.add(Restrictions.eq("indAtivo", true))
			.add(Restrictions.ilike("p.numProcesso", numProcesso, MatchMode.ANYWHERE))
			.setProjection(
					Projections.projectionList()
						.add(Projections.alias(Projections.property("ideTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua"))
						.add(Projections.alias(Projections.property("nomTipoFinalidadeUsoAgua"), "nomTipoFinalidadeUsoAgua"))
						.add(Projections.alias(Projections.property("indAtivo"), "indAtivo"))
						).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoFinalidadeUsoAgua.class));
		return tipoFinalidadeUsoAguaIDAO.listarPorCriteria(criteria);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAgua(Integer ideTipologia, Boolean indRequerimento, Boolean indCerh) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoFinalidadeUsoAgua.class)
				.createAlias("atoAmbientalTipologiaFinalidadeCollection", "aatf")
				.createAlias("aatf.ideAtoAmbientalTipologia", "aat")
				.createAlias("aat.ideTipologia", "t")
				.add(Restrictions.eq("indAtivo", true));
		if(!Util.isNull(ideTipologia)){
			criteria.add(Restrictions.eq("t.ideTipologia", ideTipologia));
		}
		if(!Util.isNull(indRequerimento)) {
			criteria.add(Restrictions.eq("indRequerimento", indRequerimento));
		}
		if(!Util.isNull(indCerh)) {
			criteria.add(Restrictions.eq("indCerh", indCerh));
		}
		criteria.setProjection(
				Projections.projectionList()
				.add(Projections.groupProperty("ideTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua")
				.add(Projections.groupProperty("nomTipoFinalidadeUsoAgua"), "nomTipoFinalidadeUsoAgua")
				.add(Projections.groupProperty("indAtivo"), "indAtivo")
				.add(Projections.groupProperty("indRequerimento"), "indRequerimento")
				.add(Projections.groupProperty("indCerh"), "indCerh")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoFinalidadeUsoAgua.class))
				;
		return tipoFinalidadeUsoAguaIDAO.listarPorCriteria(criteria, Order.asc("nomTipoFinalidadeUsoAgua"));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAgua(AtoAmbiental atoAmbiental, Tipologia tipologia) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoFinalidadeUsoAgua.class)
			.createAlias("atoAmbientalTipologiaFinalidadeCollection", "aatf")
			.createAlias("aatf.ideAtoAmbientalTipologia", "aat")
			.createAlias("aat.ideTipologia", "t")
			.createAlias("aat.ideAtoAmbiental", "aa")
			.add(Restrictions.eq("indAtivo", true))
			.add(Restrictions.eq("indRequerimento", true))
			.add(Restrictions.eq("t.ideTipologia", tipologia.getIdeTipologia()))
			.add(Restrictions.eq("aa.ideAtoAmbiental", atoAmbiental.getIdeAtoAmbiental()))
			.addOrder(Order.asc("nomTipoFinalidadeUsoAgua"))
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua")
				.add(Projections.groupProperty("nomTipoFinalidadeUsoAgua"), "nomTipoFinalidadeUsoAgua")
				.add(Projections.groupProperty("indAtivo"), "indAtivo")
				.add(Projections.groupProperty("indRequerimento"), "indRequerimento")
				.add(Projections.groupProperty("indCerh"), "indCerh")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoFinalidadeUsoAgua.class))
		;
		return tipoFinalidadeUsoAguaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAguaCerh(TipologiaEnum tipologiaEnum) throws Exception {
		return listarTipoFinalidadeUsoAgua(tipologiaEnum.getId(), null, true);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAguaRequerimento() throws Exception{
		return listarTipoFinalidadeUsoAgua(null, true, null);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoFinalidadeUsoAgua> listarTipoFinalidadeUsoAguaRequerimentoAtivo() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoFinalidadeUsoAgua.class)
				.add(Restrictions.eq("indAtivo", true))
				.add(Restrictions.eq("indRequerimento", true));
		
		return tipoFinalidadeUsoAguaIDAO.listarPorCriteria(criteria, Order.asc("nomTipoFinalidadeUsoAgua"));
	}
	
}
