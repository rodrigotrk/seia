package br.gov.ba.seia.service;

import java.math.BigDecimal;
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
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovel;
import br.gov.ba.seia.entity.OutorgaTipoCaptacao;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoIntervencao;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoIntervencaoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OutorgaLocalizacaoGeograficaService {

	@Inject
	private IDAO<OutorgaLocalizacaoGeografica> outorgaLocalizacaoGeograficaDAO;
	@Inject
	private IDAO<DadoGeografico> dadoGeograficolDAO;
	@Inject
	private IDAO<LocalizacaoGeografica> localizacaoGeolDAO;
	@Inject
	private IDAO<TipoIntervencao> tipoIntervencaoDAO;
	@Inject
	private IDAO<TipoCaptacao> tipoCaptacaoDAO;
	@Inject
	private IDAO<OutorgaLocalizacaoGeograficaImovel> outorgaLocalizacaoGeogarficaImovelDAO;
	@Inject
	private IDAO<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaDAO;
	@Inject
	private IDAO<FceOutorgaLocalizacaoGeografica> fceOutorgaLocalizacaoGeografica;

	@EJB
	private PerguntaRequerimentoService perguntaRequerService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private OutorgaLocalizacaoGeograficaFinalidadeService outorgaLocalizacaoGeograficaFinalidadeService; 
	
	/*
	 *
	 * METODOS DE BUSCA
	 *
	 */

	/**
	 * @param outorgaLocalizacaoGeografica
	 * @return List<OutorgaLocalizacaoGeograficaImovel>
	 * @
	 * @Comment Busca todas as 'OutorgaLocalizacaoGeograficaImovel' que tenha o mesmo ID de 'OutorgaLocalizacaoGeografica'
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeograficaImovel> buscarOutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeograficaImovel.class);
		criteria.setFetchMode("ideImovel", FetchMode.JOIN);
		criteria.add(Restrictions.eq("ideOutorgaLocalizacaoGeografica.ideOutorgaLocalizacaoGeografica", outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica()));

		return outorgaLocalizacaoGeogarficaImovelDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoIntervencao buscarTipoIntervencaoByIdeOutorga(Outorga outorga) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideOutorga", outorga.getIdeOutorga());
		return tipoIntervencaoDAO.buscarPorNamedQuery("TipoIntervencao.findByIdeOutorga", map).get(0);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> buscarOutorgaLocalizacaoGeoByIdOutorga(Outorga outorga)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.createAlias("ideOutorga", "o");
		criteria.createAlias("tipoIntervencao", "tipoIntervencao",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("o.ideModalidadeOutorga", "m", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideLocalizacaoGeografica", "locPai", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideLocalizacaoGeograficaFinal", "locFim", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideOutorga", outorga));
		return outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> buscarOutorgaLocalizacaoGeoByIdOutorgaAndIdTipoIntervencao(Outorga outorga, TipoIntervencao tipoIntervencao)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.createAlias("ideOutorga", "o");
		criteria.createAlias("tipoIntervencao", "ti");
		criteria.setFetchMode("ideTipoBarragem", FetchMode.JOIN);
		criteria.setFetchMode("ideLocalizacaoGeografica", FetchMode.JOIN);
		criteria.add(Restrictions.eq("ti.ideTipoIntervencao", tipoIntervencao.getIdeTipoIntervencao()));
		criteria.add(Restrictions.eq("ideOutorga", outorga));
		List<OutorgaLocalizacaoGeografica> list = outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);
		for (OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : list) {
			outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
		}
		return list;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaByModalidadeOutorgaRequerimento(
			ModalidadeOutorgaEnum modalidadeOutorgaEnum, Requerimento requerimento)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.createAlias("ideLocalizacaoGeografica", "lg");
		criteria.createAlias("ideLocalizacaoGeograficaFinal", "lgF", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideOutorga", "o");
		criteria.createAlias("ideTipoBarragem", "tb", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipoIntervencao", "ti", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoTravessia", "tr", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ti.ideTipoFinalidadeUsoAgua", "tfua", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("o.ideRequerimento", "r");
		criteria.createAlias("o.ideModalidadeOutorga", "mo");
		criteria.createAlias("o.ideTipoSolicitacao", "ts");
		criteria.createAlias("lg.dadoGeograficoCollection", "dg", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()));
		criteria.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()));
		
		List<OutorgaLocalizacaoGeografica> lista = outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);
		
		for(OutorgaLocalizacaoGeografica out: lista){
			
			if(!Util.isNullOuVazio(out.getIdeLocalizacaoGeograficaFinal()) && !Util.isNullOuVazio(out.getIdeLocalizacaoGeograficaFinal().getDadoGeograficoCollection())){
				Hibernate.initialize(out.getIdeLocalizacaoGeograficaFinal().getDadoGeograficoCollection());
			}
			
			out.setOutorgaLocalizacaoGeograficaFinalidadeCollection(
					outorgaLocalizacaoGeograficaFinalidadeService.obterListaOutorgaLocalizacaoGeograficaFinalidades(out));
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaByModalidadeOutorgaRequerimentoTecnico(
			ModalidadeOutorgaEnum modalidadeOutorgaEnum, Requerimento requerimento, DadoOrigemEnum dadoOrigem)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.createAlias("ideLocalizacaoGeografica", "lg");
		criteria.createAlias("ideLocalizacaoGeograficaFinal", "lgF", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideOutorga", "o");
		criteria.createAlias("ideTipoBarragem", "tb", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipoIntervencao", "ti", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoTravessia", "tr", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("o.ideRequerimento", "r");
		
		criteria.createAlias("r.fceCollection", "fce");
		criteria.createAlias("fce.fceIntervencaoBarragemCollection", "fci");
		
		
		
		criteria.createAlias("o.ideModalidadeOutorga", "mo");
		criteria.createAlias("o.ideTipoSolicitacao", "ts");
		criteria.createAlias("lg.dadoGeograficoCollection", "dg", JoinType.LEFT_OUTER_JOIN);

		criteria.add(Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()));
		criteria.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()));

		criteria.add(Restrictions.eq("fce.ideDadoOrigem.ideDadoOrigem", dadoOrigem.getId()));
		
		criteria.add(Restrictions.eqProperty("fci.ideOutorgaLocalizacaoGeografica.ideOutorgaLocalizacaoGeografica", "ideOutorgaLocalizacaoGeografica"));
		
		
		return outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaByModalidadeOutorga(ModalidadeOutorgaEnum modalidadeOutorgaEnum, Requerimento requerimento)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.createAlias("ideLocalizacaoGeografica", "lg");
		criteria.createAlias("ideLocalizacaoGeograficaFinal", "lgF", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideOutorga", "o");
		criteria.createAlias("ideTipoBarragem", "tb", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipoIntervencao", "ti", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoTravessia", "tr", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("o.ideRequerimento", "r");
		criteria.createAlias("o.ideModalidadeOutorga", "mo");
		criteria.createAlias("o.ideTipoSolicitacao", "ts");
		criteria.add(Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()));
		criteria.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()));
		
		return outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);
	}
	
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum tipoCaptacaoEnum, Requerimento requerimento)  {
		
		List<OutorgaLocalizacaoGeografica> listOutorga = new ArrayList<OutorgaLocalizacaoGeografica>();
		List<OutorgaLocalizacaoGeografica> list;
		List<TipoFinalidadeUsoAgua> listOutorgaFinalidadeTeste = new ArrayList<TipoFinalidadeUsoAgua>();
		List<OutorgaLocalizacaoGeograficaFinalidade> listOutorgaTeste = new ArrayList<OutorgaLocalizacaoGeograficaFinalidade>();
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.setFetchMode("ideTipoBarragem", FetchMode.JOIN);
		criteria.createAlias("ideOutorga", "o");
		criteria.createAlias("o.ideTipoSolicitacao", "tipoSolicitacao", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("o.ideModalidadeOutorga", "mo");
		criteria.createAlias("ideLocalizacaoGeografica", "lg");
		criteria.createAlias("lg.dadoGeograficoCollection", "dg", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("o.ideModalidadeOutorga.ideModalidadeOutorga", ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade()));
		criteria.add(Restrictions.eq("o.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		list = outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);
		
		for (OutorgaLocalizacaoGeografica obj : list) {
			criteria = DetachedCriteria.forClass(OutorgaTipoCaptacao.class);
			criteria.add(Restrictions.and(Restrictions.eq("ideTipoCaptacao.ideTipoCaptacao", tipoCaptacaoEnum.getId()), Restrictions.eq("ideOutorga", obj.getIdeOutorga())));
			
			obj.setOutorgaLocalizacaoGeograficaFinalidadeCollection(
					outorgaLocalizacaoGeograficaFinalidadeService.obterListaOutorgaLocalizacaoGeograficaFinalidades(obj));
			
		if(!TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId().equals(tipoCaptacaoEnum.getId())) {	
			for(OutorgaLocalizacaoGeograficaFinalidade item : obj.getOutorgaLocalizacaoGeograficaFinalidadeCollection()) {
				if(!listOutorgaFinalidadeTeste.contains(item.getIdeTipoFinalidadeUsoAgua())) {
					listOutorgaFinalidadeTeste.add(item.getIdeTipoFinalidadeUsoAgua());
					listOutorgaTeste.add(item);
				}
			}
			
			obj.setOutorgaLocalizacaoGeograficaFinalidadeCollection(listOutorgaTeste);
			
			}
			
			if (!Util.isNullOuVazio(tipoCaptacaoDAO.listarPorCriteria(criteria))) {
				listOutorga.add(obj);
			}
			
		}
		
		return listOutorga;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeografica(Integer ideRequerimento, ModalidadeOutorgaEnum modalidadeOutorgaEnum, TipoCaptacaoEnum tipoCaptacaoEnum)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(PerguntaRequerimento.class);
		
		criteria
			.createAlias("ideRequerimento", "r", JoinType.INNER_JOIN)
			.createAlias("ideLocalizacaoGeografica", "l", JoinType.INNER_JOIN)
			.createAlias("l.outorgaLocalizacaoGeograficaCollection", "olg", JoinType.INNER_JOIN)
			.createAlias("olg.ideOutorga", "o", JoinType.INNER_JOIN)
			.createAlias("o.ideModalidadeOutorga", "m", JoinType.INNER_JOIN)
			.createAlias("o.tipoCaptacaoCollection", "tc", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("r.ideRequerimento", ideRequerimento))
			.add(Restrictions.eq("m.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()))
			
		;
		
		if(tipoCaptacaoEnum != null) {
			
			criteria.add(Restrictions.eq("tc.ideTipoCaptacao", tipoCaptacaoEnum.getId()));
		}
			
		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("olg.ideOutorgaLocalizacaoGeografica"),"ideOutorgaLocalizacaoGeografica")
				.add(Projections.groupProperty("olg.numVazao"),"numVazao")
				.add(Projections.groupProperty("olg.numVazaoFinal"),"numVazaoFinal")
				.add(Projections.groupProperty("olg.numAreaIrrigada"),"numAreaIrrigada")
				.add(Projections.groupProperty("olg.numAreaIrrigadaCaptacao"),"numAreaIrrigadaCaptacao")
				.add(Projections.groupProperty("olg.numAreaPulverizada"),"numAreaPulverizada")
				.add(Projections.groupProperty("olg.dtcPerfuracaoPoco"),"dtcPerfuracaoPoco")
				.add(Projections.groupProperty("olg.numOficio"),"numOficio")
				.add(Projections.groupProperty("olg.dtcEmissaoOficio"),"dtcEmissaoOficio")
				.add(Projections.groupProperty("olg.numProcessoBarragem"),"numProcessoBarragem")
				.add(Projections.groupProperty("olg.numPortariaBarragem"),"numPortariaBarragem")
				.add(Projections.groupProperty("olg.numPortariaLicencaBarragem"),"numPortariaLicencaBarragem")
				.add(Projections.groupProperty("olg.dtcPublicacaoPortariaBarragem"),"dtcPublicacaoPortariaBarragem")
				.add(Projections.groupProperty("olg.numVolumeAcumulacaoBarragem"),"numVolumeAcumulacaoBarragem")
				.add(Projections.groupProperty("olg.numDescargaFundo"),"numDescargaFundo")
				.add(Projections.groupProperty("olg.nomIntervencao"),"nomIntervencao")
				.add(Projections.groupProperty("olg.indFinalidade"),"indFinalidade")
				.add(Projections.groupProperty("olg.numVolumeMaximoAcumulado"),"numVolumeMaximoAcumulado")
				.add(Projections.groupProperty("olg.numAreaInundadaReservatorio"),"numAreaInundadaReservatorio")
				.add(Projections.groupProperty("l.ideLocalizacaoGeografica"),"ideLocalizacaoGeografica.ideLocalizacaoGeografica")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(OutorgaLocalizacaoGeografica.class));
		
		List<OutorgaLocalizacaoGeografica> lista = outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);
		
		for( OutorgaLocalizacaoGeografica olg : lista) {
			Collection<DadoGeografico> listaDadoGeografico = listarDadoGeografico(olg.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			if(!Util.isNullOuVazio(listaDadoGeografico)) {
				for(DadoGeografico dg : listaDadoGeografico) {
					String coordenadas = dg.getCoordGeoNumerica().replace("POINT (", "").replace(")", "");
					String latitude = coordenadas.substring(0,coordenadas.lastIndexOf(" "));
					String longitude = coordenadas.substring(coordenadas.lastIndexOf(" "),coordenadas.length()-1);
					olg.getIdeLocalizacaoGeografica().setPontoLatitude(latitude);
					olg.getIdeLocalizacaoGeografica().setPontoLongitude(longitude);
				}
				olg.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(listaDadoGeografico);		
			}
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<DadoGeografico> listarDadoGeografico(Integer ideLocalizacaoGeografica)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(DadoGeografico.class);
		criteria.add(Restrictions.eq("ideLocalizacaoGeografica.ideLocalizacaoGeografica", ideLocalizacaoGeografica));
		return dadoGeograficolDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaByRequerimento(Requerimento requerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.setFetchMode("ideTipoBarragem", FetchMode.JOIN);
		criteria.createAlias("ideOutorga", "o");
		criteria.createAlias("o.ideTipoSolicitacao", "ts", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("o.ideModalidadeOutorga", "mo");
		criteria.createAlias("ideLocalizacaoGeografica", "lg");
		criteria.createAlias("lg.dadoGeograficoCollection", "dg", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("o.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		return outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int countOutorgaLocalizacaoGeograficaByRequerimento(Requerimento requerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class)
				.createAlias("ideOutorga", "o")
				
				.add(Restrictions.eq("o.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		return outorgaLocalizacaoGeograficaDAO.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int countOutorgaLocalizacaoGeograficaByRequerimentoPerfuracaoPoco(Requerimento requerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class)
				.createAlias("ideOutorga", "o")
				
				.add(Restrictions.eq("o.ideModalidadeOutorga.ideModalidadeOutorga", ModalidadeOutorgaEnum.POCO.getIdModalidade()))
				.add(Restrictions.eq("o.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		return outorgaLocalizacaoGeograficaDAO.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int countOutorgaLocalizacaoGeograficaByRequerimentoLancamentoEfluentes(Requerimento requerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class)
				.createAlias("ideOutorga", "o")
				
				.add(Restrictions.eq("o.ideModalidadeOutorga.ideModalidadeOutorga", ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES.getIdModalidade()))
				.add(Restrictions.eq("o.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		return outorgaLocalizacaoGeograficaDAO.count(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int countOutorgaLocalizacaoGeograficaByRequerimentoIntervencao(Requerimento requerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class)
				.createAlias("ideOutorga", "o")
				
				.add(Restrictions.eq("o.ideModalidadeOutorga.ideModalidadeOutorga", ModalidadeOutorgaEnum.INTERVENCAO.getIdModalidade()))
				.add(Restrictions.eq("o.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		
		return outorgaLocalizacaoGeograficaDAO.count(criteria);
	}
	


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeoByIdOutorga(Outorga outorga)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.setFetchMode("ideLocalizacaoGeografica", FetchMode.JOIN);
		criteria.createAlias("ideOutorga", "o");
		criteria.createAlias("o.ideModalidadeOutorga", "mod");
		criteria.add(Restrictions.eq("ideOutorga", outorga));
		List<OutorgaLocalizacaoGeografica> list = outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria, Order.asc("ideLocalizacaoGeografica"));
		for (OutorgaLocalizacaoGeografica obj : list) {
			obj.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
		}
		return list;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeoByOutorgaLoadAll(Outorga outorga)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.createAlias("outorgaLocalizacaoGeograficaFinalidadeCollection", "OutLocGeoFinalidade", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("OutLocGeoFinalidade.ideTipoFinalidadeUsoAgua", "TipoFinalidadeUsoAgua", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideOutorga.tipoCaptacaoCollection", "tipoCaptacao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideOutorga.ideOutorga", outorga.getIdeOutorga()));

		return outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoFinalidadeUsoAgua> carregaListaTipoFinalidadeUsoAgua()  {

		DetachedCriteria criteria = DetachedCriteria.forClass(TipoFinalidadeUsoAgua.class);
		return tipoFinalidadeUsoAguaDAO.listarPorCriteria(criteria, Order.asc("nomTipoFinalidadeUsoAgua"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoFinalidadeUsoAgua> carregaListaTipoFinalidadeUsoAguaByOutorga(Outorga outorga)  {

		List<TipoFinalidadeUsoAgua> listFinalidUsoAgua = new ArrayList<TipoFinalidadeUsoAgua>();

		List<OutorgaLocalizacaoGeografica> listOutLocGeo = listarOutorgaLocalizacaoGeograficaByOutorgaComIntervencao(outorga);

		for (OutorgaLocalizacaoGeografica outLocGeo : listOutLocGeo) {
			listFinalidUsoAgua.add(outLocGeo.getTipoIntervencao().getIdeTipoFinalidadeUsoAgua());
		}
		Util.sigletonList(listFinalidUsoAgua);

		return listFinalidUsoAgua;
	}

	private List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaByOutorgaComIntervencao(Outorga outorga)  {

		List<OutorgaLocalizacaoGeografica> listOutLocGeo;
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.createAlias("tipoIntervencao", "tipoIntervencao");
		criteria.createAlias("tipoIntervencao.ideTipoFinalidadeUsoAgua", "tipoFinalidadeUsoAgua");
		criteria.add(Restrictions.eq("ideOutorga.ideOutorga", outorga.getIdeOutorga()));

		listOutLocGeo = outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria, Order.asc("tipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua"));
		return listOutLocGeo;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaByRequerimentoAndIdeTipoFinalidade(Requerimento requerimento, TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua)  {
		List<OutorgaLocalizacaoGeografica> listaOutLocGeo = new ArrayList<OutorgaLocalizacaoGeografica>();
		List<OutorgaLocalizacaoGeograficaFinalidade> tempOutLocGeoFinalidade = outorgaLocalizacaoGeograficaFinalidadeService.listarOutorgaLocalizacaoGeoFinalidadeByIdRequerimentoAndIdeFin(requerimento, tipoFinalidadeUsoAgua);
		for(OutorgaLocalizacaoGeograficaFinalidade outLocGeoFin : tempOutLocGeoFinalidade) {
			listaOutLocGeo.add(outLocGeoFin.getIdeOutorgaLocalizacaoGeografica());
		}
		return listaOutLocGeo;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocGeoByModalidadeOutorgaRequerimentoAndTipoIntervencao(ModalidadeOutorgaEnum modalidadeOutorgaEnum, Requerimento requerimento,
			TipoIntervencaoEnum tipoIntervencaoEnum)  {

		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.createAlias("ideLocalizacaoGeografica", "lg");
		criteria.createAlias("ideOutorga", "o");
		criteria.createAlias("tipoIntervencao", "ti");
		criteria.createAlias("ideTipoBarragem", "tb", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("o.ideRequerimento", "r");
		criteria.createAlias("o.ideModalidadeOutorga", "mo");
		criteria.createAlias("lg.dadoGeograficoCollection", "dg");
		criteria.createAlias("lg.ideSistemaCoordenada", "sc");
		criteria.createAlias("lg.ideClassificacaoSecao", "cs");
		criteria.add(Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()));
		criteria.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()));
		criteria.add(Restrictions.eq("ti.ideTipoIntervencao", tipoIntervencaoEnum.getId()));
		List<OutorgaLocalizacaoGeografica> list = outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);
		for (OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : list) {
			outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
		}
		return list;
	}

	/*
	 *
	 * METODOS DE SALVAMENTO
	 *
	 */

	/**
	 *
	 * @param outorgaLocalizacaoGeograficaImovel
	 * @
	 * @Comment Salva uma OutorgaLocalizacaoGeograficaImovel
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarOutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeograficaImovel outorgaLocalizacaoGeograficaImovel)  {

		outorgaLocalizacaoGeogarficaImovelDAO.salvar(outorgaLocalizacaoGeograficaImovel);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizar(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {

		outorgaLocalizacaoGeograficaDAO.salvarOuAtualizar(outorgaLocalizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSemFlush(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {
		outorgaLocalizacaoGeograficaDAO.salvarSemFlush(outorgaLocalizacaoGeografica);
	}

	/*
	 *
	 * METODOS DE EXCLUSAO
	 *
	 */

	/**
	 *
	 * @param outorgaLocalizacaoGeografica
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirOutorgaLocalizacaoGeograficaImovel(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ideOutorgaLocalizacaoGeografica", outorgaLocalizacaoGeografica);
		outorgaLocalizacaoGeogarficaImovelDAO.executarNamedQuery("OutorgaLocalizacaoGeograficaImovel.removeByideOLGI", map);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {

		excluirFceOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideOutorgaLocalizacaoGeografica", outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica());
		outorgaLocalizacaoGeograficaDAO.executarNamedQuery("OutorgaLocalizacaoGeografica.excluirByIdeOutorgaLocGeo", params);
		params.clear();

		perguntaRequerService.removerPerguntaReqByIdLocalizacaoGeografica(outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());

		LocalizacaoGeografica ideLocGeo = outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica();

		if (!Util.isNullOuVazio(ideLocGeo)) {
			params.put("ideLocalizacaoGeografica", ideLocGeo.getIdeLocalizacaoGeografica());
			dadoGeograficolDAO.executarNamedQuery("DadoGeografico.removerByIdLocalizacaoGeo", params);
			params.clear();

			params.put("ideLocalizacaoGeografica", ideLocGeo.getIdeLocalizacaoGeografica());
			localizacaoGeolDAO.executarNamedQuery("LocalizacaoGeografica.deleteByIde", params);
			params.clear();
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {

		try{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideOutorgaLocalizacaoGeografica", outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica());
			fceOutorgaLocalizacaoGeografica.executarNamedQuery("FceOutorgaLocalizacaoGeografica.deleteByideOutorgaLocGeografica", params);

		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica)  {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideOutorgaLocalizacaoGeografica", outorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica());
		outorgaLocalizacaoGeograficaDAO.executarNamedQuery("OutorgaLocalizacaoGeografica.excluirByIdeOutorgaLocGeo", params);
		params.clear();

		perguntaRequerService.removerPerguntaReqByIdLocalizacaoGeografica(outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());

		LocalizacaoGeografica ideLocGeo = outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica();

		if (!Util.isNullOuVazio(ideLocGeo)) {
			params.put("ideLocalizacaoGeografica", ideLocGeo.getIdeLocalizacaoGeografica());
			dadoGeograficolDAO.executarNamedQuery("DadoGeografico.removerByIdLocalizacaoGeo", params);
			params.clear();

			params.put("ideLocalizacaoGeografica", ideLocGeo.getIdeLocalizacaoGeografica());
			localizacaoGeolDAO.executarNamedQuery("LocalizacaoGeografica.deleteByIde", params);
			params.clear();
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerIntervencao(TipoIntervencao tipoIntervencao, Outorga outorga)  {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideTipoIntervencao", tipoIntervencao.getIdeTipoIntervencao());
		params.put("ideOutorga", outorga.getIdeOutorga());
		tipoIntervencaoDAO.executarNamedQuery("TipoIntervencao.excluirByIdeTipoIntervencao", params);
	}

	/*
	 *
	 * OUTROS
	 *
	 */

	public boolean isPassivelDispensaOutorga(Integer ideRequerimento)  {

		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class).createAlias("ideOutorga", "ideOutorga")
				.createAlias("ideOutorga.ideModalidadeOutorga", "ideModalidadeOutorga").createAlias("ideOutorga.ideRequerimento", "ideRequerimento")
				.setProjection(Projections.projectionList().add(Projections.sum("numVazao"), "numVazao"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(OutorgaLocalizacaoGeografica.class));

		detachedCriteria.add(Restrictions.eq("ideRequerimento.ideRequerimento", ideRequerimento));
		detachedCriteria.add(Restrictions.eq("ideModalidadeOutorga.ideModalidadeOutorga", ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade()));

		OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica = this.outorgaLocalizacaoGeograficaDAO.buscarPorCriteria(detachedCriteria);

		if (!Util.isNull(outorgaLocalizacaoGeografica) && !Util.isNull(outorgaLocalizacaoGeografica.getNumVazao())) {
			return outorgaLocalizacaoGeografica.getNumVazao().doubleValue() <= BigDecimal.valueOf(43.2).doubleValue();
		}

		return false;

	}

	/**
	 * Método utilizado pelo {@link Fce} no carregamento da {@link OutorgaLocalizacaoGeografica} com {@link DadoGeografico}
	 *
	 * @author eduardo.fernandes
	 * @param modalidadeOutorgaEnum
	 * @param requerimento
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeograficaComDadoGeograficoByModalidadeOutorga(ModalidadeOutorgaEnum modalidadeOutorgaEnum, Requerimento requerimento)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class);
		criteria.createAlias("ideLocalizacaoGeografica", "lg");
		criteria.createAlias("ideOutorga", "o");
		criteria.createAlias("o.ideRequerimento", "r");
		criteria.createAlias("o.ideModalidadeOutorga", "mo");
		criteria.createAlias("lg.dadoGeograficoCollection", "dg");
		criteria.add(Restrictions.eq("mo.ideModalidadeOutorga", modalidadeOutorgaEnum.getIdModalidade()));
		criteria.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()));
		List<OutorgaLocalizacaoGeografica> list = outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria);
		for (OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : list) {
			outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
		}
		return list;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeogaficaDeTipoBarragem(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaLocalizacaoGeografica.class, "olg")
				.createAlias("ideTipoBarragem", "barragem")
				.createAlias("ideOutorga", "outorga")
				.createAlias("tipoIntervencao", "intervencao")
				.createAlias("outorga.ideModalidadeOutorga", "mod")
				.createAlias("ideLocalizacaoGeografica", "locGeo")
				.createAlias("locGeo.ideSistemaCoordenada", "sisCoord")
				.createAlias("locGeo.ideClassificacaoSecao", "clas")
				.add(Restrictions.eq("outorga.ideRequerimento", requerimento))
				.add(Restrictions.eq("outorga.ideTipoSolicitacao.ideTipoSolicitacao", TipoSolicitacaoEnum.NOVA_OUTORGA.getId()))
				.add(Restrictions.eq("intervencao.ideTipoIntervencao", TipoIntervencaoEnum.CONSTRUCAO_BARRAGEM.getId()))
				.add(Restrictions.or(Restrictions.eq("mod.ideModalidadeOutorga", ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade()),	Restrictions.eq("mod.ideModalidadeOutorga", ModalidadeOutorgaEnum.INTERVENCAO.getIdModalidade())))
				.setProjection(Projections.projectionList()

						.add(Projections.property("olg.ideOutorgaLocalizacaoGeografica"), "ideOutorgaLocalizacaoGeografica")
						.add(Projections.property("olg.numVazao"), "numVazao")
						.add(Projections.property("olg.numVazaoFinal"), "numVazaoFinal")
						.add(Projections.property("olg.numAreaIrrigada"), "numAreaIrrigada")
						.add(Projections.property("olg.numAreaIrrigadaCaptacao"), "numAreaIrrigadaCaptacao")
						.add(Projections.property("olg.numAreaPulverizada"), "numAreaPulverizada")
						.add(Projections.property("olg.numOficio"), "numOficio")
						.add(Projections.property("olg.dtcEmissaoOficio"), "dtcEmissaoOficio")
						.add(Projections.property("olg.numPortariaBarragem"), "numPortariaBarragem")
						.add(Projections.property("olg.numPortariaLicencaBarragem"), "numPortariaLicencaBarragem")
						.add(Projections.property("olg.dtcPublicacaoPortariaBarragem"), "dtcPublicacaoPortariaBarragem")
						.add(Projections.property("olg.numProcessoBarragem"), "numProcessoBarragem")
						.add(Projections.property("olg.numVolumeAcumulacaoBarragem"), "numVolumeAcumulacaoBarragem")
						.add(Projections.property("olg.numDescargaFundo"), "numDescargaFundo")
						.add(Projections.property("olg.numAreaInundadaReservatorio"), "numAreaInundadaReservatorio")
						.add(Projections.property("olg.nomIntervencao"), "nomIntervencao")
						.add(Projections.property("olg.numVolumeMaximoAcumulado"), "numVolumeMaximoAcumulado")

						.add(Projections.property("outorga.ideOutorga"), "ideOutorga.ideOutorga")
						.add(Projections.property("intervencao.ideTipoIntervencao"), "tipoIntervencao.ideTipoIntervencao")
						.add(Projections.property("locGeo.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
						.add(Projections.property("barragem.ideTipoBarragem"), "ideTipoBarragem.ideTipoBarragem")
						.add(Projections.property("barragem.nomTipoBarragem"), "ideTipoBarragem.nomTipoBarragem")
						.add(Projections.property("sisCoord.ideSistemaCoordenada"), "ideLocalizacaoGeografica.ideSistemaCoordenada.ideSistemaCoordenada")
						.add(Projections.property("sisCoord.srid"), "ideLocalizacaoGeografica.ideSistemaCoordenada.srid")
						.add(Projections.property("clas.ideClassificacaoSecao"), "ideLocalizacaoGeografica.ideClassificacaoSecao.ideClassificacaoSecao"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(OutorgaLocalizacaoGeografica.class));
		
		List<OutorgaLocalizacaoGeografica> list = outorgaLocalizacaoGeograficaDAO.listarPorCriteria(criteria, Order.asc("olg.ideOutorgaLocalizacaoGeografica"));
		
		for (OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : list) {
			if(Util.isLazyInitExcepOuNull(outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getDadoGeograficoCollection())){
				LocalizacaoGeografica locGeo = outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica();
				locGeo.setDadoGeograficoCollection(localizacaoGeograficaService.listarDadoGeografico(locGeo, ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
			}
		}
		
		return list;
	}
}