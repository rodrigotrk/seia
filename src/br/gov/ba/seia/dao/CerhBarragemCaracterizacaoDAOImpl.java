package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhBarragemCaracterizacao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhBarragemCaracterizacaoDAOImpl extends AbstractDAO<CerhBarragemCaracterizacao>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhBarragemCaracterizacao> dao;

	@Override
	protected IDAO<CerhBarragemCaracterizacao> getDAO() {
		return dao;
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemCaracterizacao buscar(CerhBarragemCaracterizacao cerhBarragemCaracterizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhBarragemCaracterizacao.class)
			.createAlias("ideTipoBarragem", "ideTipoBarragem", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideBarragem", "ideBarragem", JoinType.INNER_JOIN)
			.createAlias("ideCerhSituacaoTipoUso", "ideCerhSituacaoTipoUso", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoCorpoHidrico", "ideTipoCorpoHidrico", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhLocalizacaoGeografica", "cerhLoc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("cerhLoc.ideLocalizacaoGeografica", "loc", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideCerhBarragemCaracterizacao", cerhBarragemCaracterizacao.getId()))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("ideCerhBarragemCaracterizacao"), "ideCerhBarragemCaracterizacao")
					.add(Projections.property("nomCorpoHidrico"), "nomCorpoHidrico")
					.add(Projections.property("dscObservacao"), "dscObservacao")
					
					.add(Projections.property("valAlturaMaximaBarragem"), "valAlturaMaximaBarragem")
					.add(Projections.property("valVazaoLiberadaJusante"), "valVazaoLiberadaJusante")
					.add(Projections.property("valVazaoRegularizada"), "valVazaoRegularizada")
					.add(Projections.property("valVolumeMaximoReservatorio"), "valVolumeMaximoReservatorio")
					
					.add(Projections.property("ideBarragem.ideBarragem"), "ideBarragem.ideBarragem")
					.add(Projections.property("ideBarragem.nomBarragem"), "ideBarragem.nomBarragem")

					.add(Projections.property("ideTipoBarragem.ideTipoBarragem"), "ideTipoBarragem.ideTipoBarragem")
					.add(Projections.property("ideTipoBarragem.nomTipoBarragem"), "ideTipoBarragem.nomTipoBarragem")
					
					.add(Projections.property("ideTipoCorpoHidrico.ideTipoCorpoHidrico"), "ideTipoCorpoHidrico.ideTipoCorpoHidrico")
					.add(Projections.property("ideTipoCorpoHidrico.nomTipoCorpoHidrico"), "ideTipoCorpoHidrico.nomTipoCorpoHidrico")
					
					.add(Projections.property("cerhLoc.ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica")
					.add(Projections.property("loc.ideLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideLocalizacaoGeografica.ideLocalizacaoGeografica")
					
					.add(Projections.property("ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso"), "ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso")
					.add(Projections.property("ideCerhSituacaoTipoUso.dscSituacaoTipoUso"), "ideCerhSituacaoTipoUso.dscSituacaoTipoUso")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhBarragemCaracterizacao.class))
			;
		return dao.buscarPorCriteria(criteria);
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemCaracterizacao carregarIdeCerhBarragemCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhBarragemCaracterizacao.class)
				.add(Restrictions.eq("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica", cerhLocalizacaoGeografica.getIdeCerhLocalizacaoGeografica()))
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideCerhBarragemCaracterizacao"), "ideCerhBarragemCaracterizacao")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhBarragemCaracterizacao.class))
				;
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemCaracterizacao carregarCerhBarragemCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhBarragemCaracterizacao.class)
				.add(Restrictions.eq("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica", cerhLocalizacaoGeografica.getIdeCerhLocalizacaoGeografica()))
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("ideCerhBarragemCaracterizacao"), "ideCerhBarragemCaracterizacao")
						.add(Projections.property("nomCorpoHidrico"), "nomCorpoHidrico")
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("dscObservacao"), "dscObservacao")
						.add(Projections.property("valAlturaMaximaBarragem"), "valAlturaMaximaBarragem")
						.add(Projections.property("valVazaoLiberadaJusante"), "valVazaoLiberadaJusante")
						.add(Projections.property("valVazaoRegularizada"), "valVazaoRegularizada")
						.add(Projections.property("valVolumeMaximoReservatorio"), "valVolumeMaximoReservatorio")
						.add(Projections.property("ideTipoBarragem.ideTipoBarragem"), "ideTipoBarragem.ideTipoBarragem")
						.add(Projections.property("ideBarragem.ideBarragem"), "ideBarragem.ideBarragem")
						.add(Projections.property("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica")
						.add(Projections.property("ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso"), "ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso")
						.add(Projections.property("ideTipoCorpoHidrico.ideTipoCorpoHidrico"), "ideTipoCorpoHidrico.ideTipoCorpoHidrico")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhBarragemCaracterizacao.class))
				;
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhBarragemCaracterizacao carregarIdeCerhBarragemCaracterizacao(CerhBarragemCaracterizacao cerhBarragemCaracterizacao){
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhBarragemCaracterizacao.class)
				.add(Restrictions.eq("ideCerhBarragemCaracterizacao", cerhBarragemCaracterizacao.getIdeCerhBarragemCaracterizacao()))
				.setFetchMode("ideCerhLocalizacaoGeografica", FetchMode.JOIN);
		
		return dao.buscarPorCriteria(criteria);
	}

	

}
