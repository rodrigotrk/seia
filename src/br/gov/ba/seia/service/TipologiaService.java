package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.TipologiaDAOImpl;
import br.gov.ba.seia.entity.AreaProdutiva;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividade;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadeAgricultura;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadeAnimal;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadePiscicultura;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoTipologia;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipologiaService {

	@Inject
	private IDAO<Tipologia> dao;

	@Inject
	private IDAO<ProcessoAto> daoProcessoAto;

	@Inject
	private IDAO<TipologiaAtividade> daoTipologiaAtividade;

	@Inject
	private IDAO<AreaProdutivaTipologiaAtividade> daoAPTA;

	@Inject
	private IDAO<AreaProdutivaTipologiaAtividadeAgricultura> daoAPTAAG;

	@Inject
	private IDAO<AreaProdutivaTipologiaAtividadeAnimal> daoAPTAAN;

	@Inject
	private IDAO<AreaProdutivaTipologiaAtividadePiscicultura> daoAPTAPI;

	@EJB
	private PorteService porteService;
	
	@EJB
	private TipologiaDAOImpl tipologiaDAOImpl;

	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> filtrarListaTipologias(Tipologia pTipologia) {
		Collection<Tipologia> tipologia = tipologiaDAOImpl.getTipologias(pTipologia);
		
		//remove as tipologias que não tem codTipologia
		Collection<Tipologia> listaTipologia = new ArrayList<Tipologia>();
		for (Tipologia tipo : tipologia) {
			if(!Util.isNullOuVazio(tipo.getCodTipologia()) && !tipo.getIndAutorizacao()) {
				listaTipologia.add(tipo);
			}
		}
		
	
		return listaTipologia;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> listarTipologiaDivisao() throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class);
		criteria
			.add(Restrictions.isNull("ideTipologiaPai"))
			.add(Restrictions.eq("indPossuiFilhos", true))
			.addOrder(Order.asc("desTipologia"))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipologia"),"ideTipologia")
				.add(Projections.property("desTipologia"),"desTipologia")
				.add(Projections.property("codTipologia"),"codTipologia")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class))
		;
			
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> listarTipologiaAtividade(Tipologia tipologia) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class);
		criteria.add(Restrictions.eq("indPossuiFilhos", false));
			
			if(tipologia != null && !Util.isNullOuVazio(tipologia.getCodTipologia())) {
				criteria.add(Restrictions.ilike("codTipologia", tipologia.getCodTipologia(), MatchMode.START));
			}
			
			criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipologia"),"ideTipologia")
				.add(Projections.property("desTipologia"),"desTipologia")
				.add(Projections.property("codTipologia"),"codTipologia")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class))
		;
		
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia buscarTipologiaDoAtoAmbiental(ProcessoAto processoAto) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoAto.class);
		criteria.createAlias("tipologia", "tipologia", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("processo.ideProcesso", processoAto.getProcesso().getIdeProcesso()));
		criteria.add(Restrictions.eq("atoAmbiental.ideAtoAmbiental", processoAto.getAtoAmbiental().getIdeAtoAmbiental()));
		processoAto = daoProcessoAto.buscarPorCriteria(criteria);
		if (processoAto != null) {
			return processoAto.getTipologia();
		}else {
			return null;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> filtrarListaTipologiasByIdePai(Tipologia pTipologia) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class, "tipologia")
			.createAlias("tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("ideTipologiaPai", pTipologia))
			.addOrder(Order.asc("desTipologia"));
		
		Collection<Tipologia> listSubTipologia = dao.listarPorCriteria(criteria);
		
		for(Tipologia tipologia: listSubTipologia){
			Hibernate.initialize(tipologia.getIdeTipologiaPai());
		}
		
		return listSubTipologia;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipologiaAtividade> filtrarListaTipologiaAtividadeByIdePai(Tipologia pTipologia) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaAtividade.class, "tipologiaAtividade");
		criteria.add(Restrictions.eq("ideTipologia", pTipologia));
		criteria.addOrder(Order.asc("dscTipologiaAtividade"));
		Collection<TipologiaAtividade> tipologiaAtividade = daoTipologiaAtividade.listarPorCriteria(criteria);
		return tipologiaAtividade;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AreaProdutivaTipologiaAtividade> carregarTipologiaAtividadeByAreaProdutiva(AreaProdutiva pAreaProdutiva) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaProdutivaTipologiaAtividade.class, "areaProdutivaTipologiaAtividade");
		criteria.createAlias("ideTipologiaAtividade", "tipologiaAtividade", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ideAreaProdutiva", pAreaProdutiva));
		Collection<AreaProdutivaTipologiaAtividade> tipologiaAtividade = daoAPTA.listarPorCriteria(criteria);
		return tipologiaAtividade;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipologiaAtividade carregarTipologiaAtividadeByIde(Integer pIdeTipologiaAtividade) throws Exception {
				 		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaAtividade.class, "tipologiaAtividade");
		criteria.add(Restrictions.eq("ideTipologiaAtividade", pIdeTipologiaAtividade));
		criteria.addOrder(Order.asc("dscTipologiaAtividade"));
		Collection<TipologiaAtividade> tipologiaAtividade = daoTipologiaAtividade.listarPorCriteria(criteria);		
		
		Set<TipologiaAtividade> listaTipologiaAtividade = new  HashSet<TipologiaAtividade>(tipologiaAtividade);				
		return listaTipologiaAtividade.iterator().next();				
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)	
	public Tipologia carregarTipologiaPorIde(Integer pIdeTipologia) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class)
				.add(Restrictions.eq("ideTipologia", pIdeTipologia))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideTipologia"), "ideTipologia")
						.add(Projections.property("codTipologia"), "codTipologia")
						.add(Projections.property("desTipologia"), "desTipologia")
						.add(Projections.property("dtcCriacao"), "dtcCriacao")
						.add(Projections.property("dtcExclusao"), "dtcExclusao")
						.add(Projections.property("indExcluido"), "indExcluido")
						.add(Projections.property("indAutorizacao"), "indAutorizacao")
						.add(Projections.property("indPossuiFilhos"), "indPossuiFilhos")
						.add(Projections.property("ideNivelTipologia.ideNivelTipologia"), "ideNivelTipologia.ideNivelTipologia")
						.add(Projections.property("ideTipologiaPai.ideTipologia"), "ideTipologiaPai.ideTipologia")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));
		return dao.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia carregarTipologia(Integer pIdeTipologia) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class, "tipologia");
		criteria.createAlias("ideTipologiaPai", "tipologiaPai", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipologiaGrupo.idePotencialPoluicao", "idePotencialPoluicao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideTipologia", pIdeTipologia));
		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.add(Restrictions.eq("tipologiaGrupo.indExcluido", false));
		criteria.addOrder(Order.asc("codTipologia"));
		Tipologia tipologia = dao.buscarPorCriteria(criteria);

		return tipologia;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia carregarTipologia(Tipologia ideTipologia) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class, "tipologia");
		criteria.add(Restrictions.eq("ideTipologia", ideTipologia.getIdeTipologia()));
		return  dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia carregarTipologiaPaiByIdeFilho(Integer pIdeTipologia) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class, "tipologia");
		criteria.createAlias("ideTipologiaPai", "tipologiaPai", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideTipologia", pIdeTipologia));
		criteria.addOrder(Order.asc("codTipologia"));
		Tipologia tipologia = dao.buscarPorCriteria(criteria);

		return tipologia.getIdeTipologiaPai();
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<Tipologia> listarTipologia() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class, "tipologia");
		criteria.add(Restrictions.in("ideTipologia", new Integer[] {4, 7, 22}));
		criteria.addOrder(Order.asc("codTipologia"));
		Collection<Tipologia> tipologia = dao.listarPorCriteria(criteria);
		return tipologia;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AreaProdutivaTipologiaAtividadeAgricultura> carregarTipologiaAtividadeAgricultura(AreaProdutivaTipologiaAtividade pAreaProdutivaTipologiaAtividade) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaProdutivaTipologiaAtividadeAgricultura.class, "areaProdutivaTipologiaAtividadeAgricultura");
		criteria.createAlias("ideMetodoIrrigacao", "metodoIrrigacao", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ideAreaProdutivaTipologiaAtividade", pAreaProdutivaTipologiaAtividade));
		Collection<AreaProdutivaTipologiaAtividadeAgricultura> tipologiaAtividade = daoAPTAAG.listarPorCriteria(criteria);
		return tipologiaAtividade;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AreaProdutivaTipologiaAtividadeAgricultura carregarTipologiaAtividadeAgriculturaByIde(AreaProdutivaTipologiaAtividade pIdeAreaProdutivaTipologiaAtividade) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaProdutivaTipologiaAtividadeAgricultura.class, "areaProdutivaTipologiaAtividadeAgricultura");
		criteria.createAlias("ideMetodoIrrigacao", "metodoIrrigacao", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ideAreaProdutivaTipologiaAtividade", pIdeAreaProdutivaTipologiaAtividade));
		AreaProdutivaTipologiaAtividadeAgricultura tipologiaAtividade = daoAPTAAG.buscarPorCriteria(criteria);
		return tipologiaAtividade;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AreaProdutivaTipologiaAtividadeAnimal> carregarTipologiaAtividadeAnimal(AreaProdutivaTipologiaAtividade pAreaProdutivaTipologiaAtividade) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaProdutivaTipologiaAtividadeAnimal.class, "areaProdutivaTipologiaAtividadeAnimal");
		criteria.createAlias("ideManejo", "manejo", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ideAreaProdutivaTipologiaAtividade", pAreaProdutivaTipologiaAtividade));
		Collection<AreaProdutivaTipologiaAtividadeAnimal> tipologiaAtividade = daoAPTAAN.listarPorCriteria(criteria);
		return tipologiaAtividade;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AreaProdutivaTipologiaAtividadeAnimal carregarTipologiaAtividadeAnimalByIde(Integer pIdeAreaProdutivaTipologiaAtividade) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaProdutivaTipologiaAtividadeAnimal.class, "areaProdutivaTipologiaAtividadeAnimal");
		criteria.createAlias("ideManejo", "manejo", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ideAreaProdutivaTipologiaAtividade", pIdeAreaProdutivaTipologiaAtividade));
		AreaProdutivaTipologiaAtividadeAnimal tipologiaAtividade = daoAPTAAN.buscarPorCriteria(criteria);
		return tipologiaAtividade;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AreaProdutivaTipologiaAtividadeAnimal carregarTipologiaAtividadeAnimalByIde(AreaProdutivaTipologiaAtividade pIdeAreaProdutivaTipologiaAtividade) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(AreaProdutivaTipologiaAtividadeAnimal.class, "areaProdutivaTipologiaAtividadeAnimal");
		criteria.createAlias("ideManejo", "manejo", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideAreaProdutivaTipologiaAtividade", pIdeAreaProdutivaTipologiaAtividade));
		AreaProdutivaTipologiaAtividadeAnimal tipologiaAtividade = daoAPTAAN.buscarPorCriteria(criteria);
		return tipologiaAtividade;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AreaProdutivaTipologiaAtividadePiscicultura carregarTipologiaAtividadePisciculturaByIde(Integer pIdeAreaProdutivaTipologiaAtividadePiscicultura) throws Exception {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideAreaProdutivaTipologiaAtividadePiscicultura", pIdeAreaProdutivaTipologiaAtividadePiscicultura);
		AreaProdutivaTipologiaAtividadePiscicultura tipologiaAtividade = daoAPTAPI.buscarEntidadePorNamedQuery("AreaProdutivaTipologiaAtividadePiscicultura.findByIdeAreaProdutivaTipologiaAtividadePiscicultura", params);
		return tipologiaAtividade;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> listarTipologiaPorProcesso(VwConsultaProcesso processo) throws Exception{

		if(processo.getIdeEmpreendimento()!=null){

			DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class);

			criteria
			.createAlias("ideNivelTipologia", "nt", JoinType.INNER_JOIN)
			.createAlias("tipologiaGrupo", "tg", JoinType.INNER_JOIN)
			.createAlias("tg.empreendimentoTipologiaCollection", "et", JoinType.INNER_JOIN)
			.createAlias("et.empreendimento", "e", JoinType.INNER_JOIN)

			.setProjection(
					Projections.projectionList()
					.add(Projections.property("ideTipologia"),"ideTipologia")
					.add(Projections.property("codTipologia"),"codTipologia")
					.add(Projections.property("desTipologia"),"desTipologia")
					.add(Projections.property("dtcCriacao"),"dtcCriacao")
					.add(Projections.property("dtcExclusao"),"dtcExclusao")
					.add(Projections.property("indExcluido"),"indExcluido")
					.add(Projections.property("indAutorizacao"),"indAutorizacao")
					.add(Projections.property("indPossuiFilhos"),"indPossuiFilhos")
					.add(Projections.property("nt.ideNivelTipologia"),"ideNivelTipologia.ideNivelTipologia")
					.add(Projections.property("tg.ideTipologiaGrupo"),"tipologiaGrupo.ideTipologiaGrupo")
					)

					.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class))

					.add(Restrictions.eq("e.ideEmpreendimento", processo.getIdeEmpreendimento()))
					.add(Restrictions.eq("indExcluido", false))
					.add(Restrictions.eq("tg.indExcluido", false))

					;

			return dao.listarPorCriteria(criteria);

		}

		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> listarTipologiaAto() throws Exception {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tipologia.class)
			.createAlias("atoAmbientalTipologiaCollection", "atosTipologia")
			.createAlias("atosTipologia.ideAtoAmbiental", "ato")
			.createAlias("tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN)
			
			.addOrder(Order.asc("desTipologia"))
		
			.add(Restrictions.or(
					Restrictions.eq("tipologiaGrupo.indExcluido", false), 
					Restrictions.isNull("tipologiaGrupo.indExcluido")
				)
			)
			
			.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("ideTipologia"),"ideTipologia")
				.add(Projections.groupProperty("desTipologia"),"desTipologia")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class))
		;
		
		return dao.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> listarByAto(AtoAmbiental atoAmbiental) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tipologia.class)
				.createAlias("atoAmbientalTipologiaCollection", "atosTipologia")
				.createAlias("atosTipologia.ideAtoAmbiental", "ato")
				.createAlias("tipologiaGrupo", "tipologiaGrupo", JoinType.LEFT_OUTER_JOIN)
		//#9318
		.setProjection(Projections.projectionList()
				.add(Projections.distinct(Projections.property("desTipologia")),"desTipologia")
				.add(Projections.property("codTipologia"),"codTipologia")
				.add(Projections.property("ideTipologia"),"ideTipologia")
				.add(Projections.property("dtcCriacao"),"dtcCriacao")
				.add(Projections.property("dtcExclusao"),"dtcExclusao")
				.add(Projections.property("indExcluido"),"indExcluido")
				.add(Projections.property("indAutorizacao"),"indAutorizacao")
				.add(Projections.property("indPossuiFilhos"),"indPossuiFilhos")
				.add(Projections.property("tipologiaGrupo.ideTipologiaGrupo"),"tipologiaGrupo.ideTipologiaGrupo"))
			
				.addOrder(Order.asc("desTipologia"))
			
				.add(Restrictions.eq("ato.ideAtoAmbiental", atoAmbiental.getIdeAtoAmbiental()))
				.add(Restrictions.or(Restrictions.eq("tipologiaGrupo.indExcluido", false), Restrictions.isNull("tipologiaGrupo.indExcluido")))
				
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideTipologia"), "ideTipologia")
						.add(Projections.property("desTipologia"), "desTipologia")).setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class))
		
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));
		return dao.listarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> listarTodasTipologias() throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tipologia.class)
			.createAlias("tipologiaGrupo", "tipologiaGrupo",  JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.or(Restrictions.eq("tipologiaGrupo.indExcluido", Boolean.FALSE),Restrictions.isNull("tipologiaGrupo.ideTipologiaGrupo")))
			.addOrder(Order.asc("desTipologia"));
		return dao.listarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia buscarTipologiaByDocumentoAto(AtoAmbiental atoAmbiental) throws Exception{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DocumentoAto.class)
				.add(Restrictions.eq("ideAtoAmbiental", atoAmbiental));
		detachedCriteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipologia")))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));

		return dao.buscarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> carregarTipologiasPai() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class, "tipologia");
		criteria.add(Restrictions.isNull("ideTipologiaPai"));
		criteria.add(Restrictions.isNotNull("codTipologia"));
		criteria.add(Restrictions.isNotNull("ideNivelTipologia"));
		criteria.add(Restrictions.eq("indExcluido",false));
		criteria.addOrder(Order.asc("codTipologia"));
		return this.dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> carregarTipologiasPaiAutorizacao() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class, "t1")
				.add(Restrictions.isNull("t1.ideTipologiaPai"))
				.add(Restrictions.isNotNull("t1.codTipologia"))
				.add(Restrictions.isNotNull("t1.ideNivelTipologia"))
				.add(Restrictions.eq("t1.indExcluido", false))
				.addOrder(Order.asc("t1.codTipologia"));
		
		DetachedCriteria subCriteria = DetachedCriteria.forClass(Tipologia.class, "t2")
				.add(Restrictions.isNotNull("t2.ideTipologiaPai"))
				.add(Restrictions.eq("t2.indAutorizacao", true))
				.setProjection(Projections.projectionList().add(Projections.distinct(Projections.property("t2.ideTipologiaPai"))))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));
		
		criteria.add(Subqueries.propertyIn("t1.ideTipologia", subCriteria));
		
		return this.dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TreeNode montarArvoreLicenca(Tipologia divisao, boolean isAutorizacao) throws Exception {
		Collection<Tipologia> grupos = this.listarTipologias(divisao, isAutorizacao);
		
		this.carregarFilhos(grupos);
		
		TreeNode root = new DefaultTreeNode("RAIZ", null);
		
		this.gerarArvoreLicenca(grupos, root);
		
		return root;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarFilhos(Collection<Tipologia> tipologias) throws Exception {
		for (Tipologia tipologia : tipologias) {

			if(tipologia.getIndPossuiFilhos()){
				Collection<Tipologia> filhas = this.listarTipologias(tipologia,false);
				tipologia.setTipologiaCollection(filhas);
				this.carregarFilhos(filhas);
			}

		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> listarTipologias(Tipologia pai, boolean isAutorizacao) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Tipologia.class)
				.createAlias("ideNivelTipologia", "nivel")
				.createAlias("tipologiaGrupo", "grupo",JoinType.LEFT_OUTER_JOIN)
				.createAlias("grupo.unidadeMedidaTipologiaGrupo", "unidadeGrupo",JoinType.LEFT_OUTER_JOIN)
				.createAlias("unidadeGrupo.ideUnidadeMedida", "unidadeMedida",JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipologiaPai", "pai",JoinType.LEFT_OUTER_JOIN)
				.createAlias("pai.ideNivelTipologia", "nivelPai",JoinType.LEFT_OUTER_JOIN);

		detachedCriteria.add(Restrictions.eq("indAutorizacao",isAutorizacao));
		detachedCriteria.add(Restrictions.eq("indExcluido",false));
		detachedCriteria.add(Restrictions.or(Restrictions.eq("grupo.indExcluido",false), Restrictions.isNull("grupo.indExcluido")));

		if(!Util.isNull(pai)) {
			detachedCriteria.add(Restrictions.eq("pai.ideTipologia", pai.getIdeTipologia()));
		}

		return this.dao.listarPorCriteria(detachedCriteria,Order.asc("codTipologia"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void gerarArvoreLicenca(Collection<Tipologia> collection, TreeNode root) {
		for (Tipologia tipologia : collection) {
			if(tipologia.getDesTipologia().contains("Gema")){
				System.out.println();
			}
			if(tipologia.getIndPossuiFilhos() && tipologia.getTipologiaCollection().isEmpty()){
				continue;
			}
			TreeNode node = new DefaultTreeNode(tipologia, root);
			if(tipologia.getIndPossuiFilhos()){
				this.gerarArvoreLicenca(tipologia.getTipologiaCollection(), node);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia carregarPaiByIdeFilho(Integer pIdeTipologia) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class, "tipologia");
		criteria.createAlias("ideTipologiaPai", "tipologiaPai", JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipologia"),"ideTipologia")
				.add(Projections.property("desTipologia"),"desTipologia")
				.add(Projections.property("tipologiaPai.ideTipologia"),"ideTipologiaPai.ideTipologia")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));

		criteria.add(Restrictions.eq("ideTipologia", pIdeTipologia));
		criteria.add(Restrictions.eq("indExcluido", false));
		criteria.addOrder(Order.asc("codTipologia"));
		Tipologia tipologia = dao.buscarPorCriteria(criteria);

		return tipologia;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia buscarTipologiaPorDocumentoObrigatorio (DocumentoObrigatorio documentoObrigatorio) throws Exception{

		DetachedCriteria criteria = DetachedCriteria.forClass(DocumentoAto.class);

		criteria.add(Restrictions.eq("ideDocumentoObrigatorio", documentoObrigatorio));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipologia"))
				).setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));

		return dao.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Porte enquadrarPorteRequerimento(Collection<Tipologia> licencas) throws Exception{
		try {
			if(!this.porteService.calcularValorPorte(licencas)){
				JsfUtil.addErrorMessage("Para o cálculo do porte informe o valor das atividades corretamente. Não é permitido valor vazio ou menor ou igual a 0.");
				return null;
			}
			return defineOPorte(licencas);
		}  catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Porte enquadrarPorteRequerimentoInit(Collection<Tipologia> licencas) throws Exception{
		this.porteService.calcularValorPorte(licencas);
		return defineOPorte(licencas);
	}

	/**
	 * @param licencas
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Porte defineOPorte(Collection<Tipologia> licencas) throws Exception {
		Porte porteIdentificado = this.verificarMaiorPorte(licencas);

		//Todos Não Identificados
		if(porteIdentificado.getIdePorte() == 0) {
			porteIdentificado = this.porteService.carregarPorteDLA();
		}

		return porteIdentificado;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Porte verificarMaiorPorte(Collection<Tipologia> licencas) {
		Porte porte = new Porte(0);
		for (Tipologia tipologia : licencas) {
			if(!Util.isNullOuVazio(tipologia.getPorte()) && ( (tipologia.getPorte().getIdePorte() > porte.getIdePorte()  && ( licencas.size() == 1 || tipologia.getPorte().getIdePorte() != 6))
					|| (tipologia.getPorte().getIdePorte() != 6 && tipologia.getPorte().getIdePorte() > porte.getIdePorte() && porte.getIdePorte() == 0))){
				porte = tipologia.getPorte();
			}
		}
		return porte;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> buscarTipologiasByIdeEmpreendimento(Empreendimento empreendimento) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(EmpreendimentoTipologia.class)
				.createAlias("tipologiaGrupo", "tg")
				.createAlias("tg.ideTipologia", "t")
				//				.createAlias("t.ideTipologiaPai", "tp")
				.add(Restrictions.eq("empreendimento.ideEmpreendimento", empreendimento.getIdeEmpreendimento()));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("t.ideTipologia"), "ideTipologia")
				.add(Projections.property("t.desTipologia"), "desTipologia")
		)
				//				.add(Projections.property("tp.ideTipologia"), "ideTipologiaPai.ideTipologia"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));

		return dao.listarPorCriteria(criteria);
	}
	
	/**
	 * Método que retorna as {@link Tipologia} dentro da faixa [<i>between</i>] de enum1 até enum2.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param enum1
	 * @param enum2
	 * @return
	 * @throws Exception
	 * @since 18/06/2015
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> listarTipologiaBy(TipologiaEnum enum1, TipologiaEnum enum2) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class)
				.add(Restrictions.between("ideTipologia", enum1.getId(), enum2.getId()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideTipologia"), "ideTipologia")
						.add(Projections.property("desTipologia"), "desTipologia"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class));
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Tipologia> listarTipologiasToSubstanciaMineral(Requerimento requerimento) throws Exception {
		DetachedCriteria cTipologia = DetachedCriteria.forClass(Tipologia.class)
				.add(Restrictions.eq("indExcluido", false))
				.add(Restrictions.ne("ideNivelTipologia.ideNivelTipologia", 1))
				;
		DetachedCriteria cRequerimentoTipologiaPai = getCriteriaToTipologiaPai();
		if(!Util.isNull(requerimento) && !Util.isNull(requerimento.getIdeRequerimento())){
			cRequerimentoTipologiaPai.add(Restrictions.eq("indAutorizacao", false));
			cRequerimentoTipologiaPai.add(Restrictions.eq("rt.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()));
		}
		cTipologia.add(Property.forName("ideTipologia").in(cRequerimentoTipologiaPai)
					)
						;
		ProjectionList projecao = Projections.projectionList()
				.add(Property.forName("ideTipologia"),"ideTipologia")
				.add(Property.forName("ideNivelTipologia.ideNivelTipologia"),"ideNivelTipologia.ideNivelTipologia")
				.add(Property.forName("desTipologia"),"desTipologia")
				.add(Property.forName("codTipologia"),"codTipologia")
				.add(Property.forName("indPossuiFilhos"),"indPossuiFilhos")
				.add(Property.forName("ideTipologiaPai.ideTipologia"),"ideTipologiaPai.ideTipologia")
				;
		cTipologia.setProjection(projecao)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class))
				;
		return dao.listarPorCriteria(cTipologia, Order.asc("codTipologia"));
	}

	/**
	 * 
	 * @author eduardo.fernandes 
	 * @since 17/06/2016
	 * @return
	 */
	private DetachedCriteria getCriteriaToTipologiaPai() {
		DetachedCriteria cRequerimentoTipologiaPai = DetachedCriteria.forClass(Tipologia.class)
				.createAlias("tipologiaGrupo", "tg", JoinType.INNER_JOIN)
				.createAlias("tg.unidadeMedidaTipologiaGrupo", "umtg", JoinType.INNER_JOIN)
				.createAlias("umtg.requerimentoTipologiaCollection", "rt" , JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.like("codTipologia", "B", MatchMode.START))
				.add(Restrictions.not(Restrictions.like("codTipologia", "B6", MatchMode.START)))
				.add(Restrictions.eq("indExcluido", false))
				.add(Restrictions.eq("tg.indExcluido", false))
				.setProjection(Projections.projectionList()
						.add(Property.forName("ideTipologia"))
				);
		return cRequerimentoTipologiaPai;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Tipologia buscarTipologiaPaiByTipologiaFilho(Tipologia tipologia) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class, "t")
				.createAlias("ideTipologiaPai", "tp", JoinType.INNER_JOIN)
				.add(Restrictions.eq("t.ideTipologia", tipologia.getIdeTipologia()))
				.add(Restrictions.eq("t.indExcluido", false))
				;
		ProjectionList projecao = Projections.projectionList()
				.add(Property.forName("tp.ideTipologia"),"ideTipologia")
				.add(Property.forName("tp.desTipologia"),"desTipologia")
				.add(Property.forName("tp.codTipologia"),"codTipologia")
				.add(Property.forName("tp.indPossuiFilhos"),"indPossuiFilhos")
				;
		criteria.setProjection(projecao)
		.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class))
		;
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Tipologia> listarPorAtoAmbiental(AtoAmbiental atoAmbiental) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(Tipologia.class)
			.createAlias("atoAmbientalTipologiaCollection", "aat", JoinType.INNER_JOIN)
			.add(Restrictions.eq("aat.ideAtoAmbiental", atoAmbiental))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipologia"),"ideTipologia")
				.add(Projections.property("desTipologia"),"desTipologia")
				.add(Projections.property("codTipologia"),"codTipologia")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(Tipologia.class))
		;
		
		return dao.listarPorCriteria(criteria);
	}
		
}