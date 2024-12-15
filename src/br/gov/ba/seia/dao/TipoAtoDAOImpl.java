package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoAtoDAOImpl {

	@Inject
	private IDAO<TipoAto> tipoAtoDAO;

	@Inject
	private IDAO<AtoAmbiental> atoAmbientalDAO;
	
	public TipoAto carregar(Integer ideTipoAto){
		return tipoAtoDAO.carregarGet(ideTipoAto);
	}
	
	public Collection<TipoAto> listarTiposAtoEnquadraveis()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAto.class)
				.createAlias("atoAmbientalCollection", "ato")
		.add(Restrictions.eq("ato.indAutomatico", false))
		.add(Restrictions.eq("ato.indAtivo", true))
		.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);	
		return tipoAtoDAO.listarPorCriteria(criteria);
	}
	
	public Collection<TipoAto> listarTiposAtosJuridicos()  {
		
		Collection<TipoAto> tipoAto;
		Collection<AtoAmbiental> atoAmbiental;
		
		DetachedCriteria criteria = DetachedCriteria.forClass(AtoAmbiental.class)
			.createAlias("ideTipoAto", "ideTipoAto")
			.setFetchMode("ideTipoAto", FetchMode.JOIN)
			
			.add(Restrictions.eq("indAutomatico", false))
			.add(Restrictions.eq("indAtivo", true))	
			.add(Restrictions.eq("ideTipoAto.ideTipoAto",TipoAtoEnum.JURIDICO.getId()))
		
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipoAto.ideTipoAto"),"ideTipoAto")
				.add(Projections.property("ideTipoAto.nomTipoAto"),"nomTipoAto"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoAto.class));
		
		tipoAto = tipoAtoDAO.listarPorCriteria(criteria);
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(AtoAmbiental.class)
				.add(Restrictions.eq("indAutomatico", false))
				.add(Restrictions.eq("indAtivo", true))
				.add(Restrictions.eq("ideTipoAto.ideTipoAto", TipoAtoEnum.JURIDICO.getId()));
		
		atoAmbiental = atoAmbientalDAO.listarPorCriteria(detachedCriteria);
		
		
		for (TipoAto ta : tipoAto) {
			ta.setAtoAmbientalCollection(atoAmbiental);	
		}
		
		
		return tipoAto;
	}
	
	public Collection<TipoAto> listarTiposAtosNaoJuridicos(Boolean isDla)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAto.class,"tipoAto")
				.createAlias("atoAmbientalCollection", "ato")
		.add(Restrictions.eq("ato.indAutomatico", false))
		.add(Restrictions.eq("ato.indAtivo", true))
		.add(Restrictions.ne("tipoAto.ideTipoAto",TipoAtoEnum.JURIDICO.getId()))
		.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);	
	
		if(!Util.isNull(isDla) && isDla){
			criteria.add(Restrictions.ne("tipoAto.nomTipoAto","Licença"));	
		}
		
		return tipoAtoDAO.listarPorCriteria(criteria);
	}
		
	public Collection<TipoAto> listarTodos() {
		return tipoAtoDAO.listarTodos();
	}
	
	public Collection<TipoAto> listarTiposAtosParaReenquadramento(Boolean isDla)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAto.class,"tipoAto")
				.createAlias("atoAmbientalCollection", "ato")
		.add(Restrictions.eq("ato.indAutomatico", false))
		.add(Restrictions.eq("ato.indAtivo", true))
		.add(Restrictions.ne("tipoAto.ideTipoAto",TipoAtoEnum.DECLARATORIO.getId()))
		.setResultTransformer(DetachedCriteria.DISTINCT_ROOT_ENTITY);	
	
		if(!Util.isNull(isDla) && isDla){
			criteria.add(Restrictions.ne("tipoAto.nomTipoAto","Licença"));	
		}
		
		return tipoAtoDAO.listarPorCriteria(criteria);
	}
	
	/*
	 * #6732
	 * Alexandre Queiroz
	 * */
	public Collection<TipoAto> listarTiposAtoConsultaveis()  {

		int outorga = 4;
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAto.class,"tipoAto")
		   .add(Restrictions.ne("tipoAto.ideTipoAto", outorga));
		
		return tipoAtoDAO.listarPorCriteria(criteria,Order.asc("nomTipoAto"));
		
	}
	
	
	
	public List<TipoAto> listarTiposAtoOrderByAsc()  {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select ta ");
		sql.append("from TipoAto ta ");
		sql.append("order by ta.nomTipoAto asc ");
		
		return tipoAtoDAO.listarPorQuery(sql.toString(), null);
	}

	public Collection<TipoAto> filtrarListaTipoAto(TipoAto tipoAto) {
		return tipoAtoDAO.listarPorExemplo(tipoAto);
	}

	public Collection<TipoAto> listarTipoAtoTipologiaGrupo(TipologiaGrupo tipologiaGrupo)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAto.class);
		criteria.createAlias("tipologiaTipoAtoCollection", "grupoTipoAto");
		criteria.add(Restrictions.eq("grupoTipoAto.ideTipologiaGrupo", tipologiaGrupo));
		return tipoAtoDAO.listarPorCriteria(criteria);
	}
	
	public Collection<TipoAto> filtrarListaTipoAtoSouce(Collection<TipoAto> listTipoAto)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAto.class);
		Collection<Integer> ids = new ArrayList<Integer>();
		for (TipoAto tipoAto : listTipoAto) {
			ids.add(tipoAto.getIdeTipoAto());
		}
		// clausula not in
		Criterion in = Restrictions.in("ideTipoAto", ids);
		criteria.add(Restrictions.not(in));
		return tipoAtoDAO.listarPorCriteria(criteria);

	}
	
	
	public Collection<TipoAto> listarTiposAtoComAtoAmbiental()  {

		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAto.class,"tipoAto")
		   .createAlias("atoAmbientalCollection", "ideAtoAmbiental",JoinType.INNER_JOIN)
		   .add(Restrictions.not(Restrictions.in("ideAtoAmbiental.ideAtoAmbiental", new Integer[]{
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
					AtoAmbientalEnum.LA.getId(),
					AtoAmbientalEnum.DQC.getId(),
					AtoAmbientalEnum.DTRP.getId(),
					AtoAmbientalEnum.CORTE_FLORESTA_PRODUCAO.getId(),
					AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO.getId(),
					AtoAmbientalEnum.LIMPEZA_AREA.getId(),
			
			})))
			.setProjection(Projections.projectionList()
					.add(Projections.distinct(Projections.property("tipoAto.ideTipoAto")))
					.add(Projections.property("tipoAto.ideTipoAto"),"ideTipoAto")
					.add(Projections.property("tipoAto.nomTipoAto"),"nomTipoAto"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoAto.class));
		
		return tipoAtoDAO.listarPorCriteria(criteria);
		
	}
		
}	
