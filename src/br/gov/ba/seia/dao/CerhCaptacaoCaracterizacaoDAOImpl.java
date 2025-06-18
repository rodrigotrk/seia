package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 03/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaptacaoCaracterizacaoDAOImpl extends AbstractDAO<CerhCaptacaoCaracterizacao>{
	private static final long serialVersionUID = 1L;

	@Override
	protected IDAO<CerhCaptacaoCaracterizacao> getDAO() {
		return dao;
	}

	@Inject
	private IDAO<CerhCaptacaoCaracterizacao> dao;
	
	@Inject
	private IDAO<Integer> ideDAO;

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoCaracterizacao carregarParaHistorico(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoCaracterizacao.class)
			.createAlias("ideBarragem", "ideBarragem", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhNaturezaPoco", "ideCerhNaturezaPoco", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhSituacaoTipoUso", "ideCerhSituacaoTipoUso", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoCorpoHidrico", "ideTipoCorpoHidrico", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhLocalizacaoGeografica", "cerhLoc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("cerhLoc.ideLocalizacaoGeografica", "loc", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("cerhLoc.ideCerhLocalizacaoGeografica", cerhLocalizacaoGeografica.getIdeCerhLocalizacaoGeografica()))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("ideCerhCaptacaoCaracterizacao"), "ideCerhCaptacaoCaracterizacao")
					.add(Projections.property("nomCorpoHidrico"), "nomCorpoHidrico")
					.add(Projections.property("dtInicioCaptacao"), "dtInicioCaptacao")
					.add(Projections.property("indCaptacaoReservatorio"), "indCaptacaoReservatorio")
					.add(Projections.property("valProfundidade"), "valProfundidade")
					.add(Projections.property("valVazaoTeste"), "valVazaoTeste")
					.add(Projections.property("valNivelEstatico"), "valNivelEstatico")
					.add(Projections.property("valNivelDinamico"), "valNivelDinamico")
					.add(Projections.property("valVazaoMaximaInstantanea"), "valVazaoMaximaInstantanea")
					.add(Projections.property("dscObservacao"), "dscObservacao")
					
					.add(Projections.property("ideTipoCorpoHidrico.ideTipoCorpoHidrico"), "ideTipoCorpoHidrico.ideTipoCorpoHidrico")
					.add(Projections.property("ideTipoCorpoHidrico.nomTipoCorpoHidrico"), "ideTipoCorpoHidrico.nomTipoCorpoHidrico")
					
					.add(Projections.property("ideBarragem.ideBarragem"), "ideBarragem.ideBarragem")
					.add(Projections.property("ideBarragem.nomBarragem"), "ideBarragem.nomBarragem")
					.add(Projections.property("ideBarragem.indOrigemUsuario"), "ideBarragem.indOrigemUsuario")
					
					.add(Projections.property("ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso"), "ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso")
					.add(Projections.property("ideCerhSituacaoTipoUso.dscSituacaoTipoUso"), "ideCerhSituacaoTipoUso.dscSituacaoTipoUso")
					
					.add(Projections.property("cerhLoc.ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica")
					.add(Projections.property("loc.ideLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideLocalizacaoGeografica.ideLocalizacaoGeografica")
					.add(Projections.property("ideCerhNaturezaPoco.ideCerhNaturezaPoco"), "ideCerhNaturezaPoco.ideCerhNaturezaPoco")
					.add(Projections.property("ideCerhNaturezaPoco.dscNaturezaPoco"), "ideCerhNaturezaPoco.dscNaturezaPoco")
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoCaracterizacao.class));
		return dao.buscarPorCriteria(criteria);
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoCaracterizacao carregarIdeCerhCaptacaoCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoCaracterizacao.class)
			.add(Restrictions.eq("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica", cerhLocalizacaoGeografica.getIdeCerhLocalizacaoGeografica()))
			.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("ideCerhCaptacaoCaracterizacao"), "ideCerhCaptacaoCaracterizacao")
						.add(Projections.property("dtInicioCaptacao"), "dtInicioCaptacao")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoCaracterizacao.class))
			;
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoCaracterizacao carregar(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoCaracterizacao.class)
			.add(Restrictions.eq("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica", cerhLocalizacaoGeografica.getIdeCerhLocalizacaoGeografica()))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("ideCerhCaptacaoCaracterizacao"), "ideCerhCaptacaoCaracterizacao")
					.add(Projections.property("nomCorpoHidrico"), "nomCorpoHidrico")
					.add(Projections.property("dtInicioCaptacao"), "dtInicioCaptacao")
					.add(Projections.property("indCaptacaoReservatorio"), "indCaptacaoReservatorio")
					.add(Projections.property("valProfundidade"), "valProfundidade")
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("valVazaoTeste"), "valVazaoTeste")
					.add(Projections.property("valNivelEstatico"), "valNivelEstatico")
					.add(Projections.property("valNivelDinamico"), "valNivelDinamico")
					.add(Projections.property("valVazaoMaximaInstantanea"), "valVazaoMaximaInstantanea")
					.add(Projections.property("dscObservacao"), "dscObservacao")
					.add(Projections.property("ideTipoCorpoHidrico.ideTipoCorpoHidrico"), "ideTipoCorpoHidrico.ideTipoCorpoHidrico")
					.add(Projections.property("ideBarragem.ideBarragem"), "ideBarragem.ideBarragem")
					.add(Projections.property("ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso"), "ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso")
					.add(Projections.property("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica")
					.add(Projections.property("ideCerhNaturezaPoco.ideCerhNaturezaPoco"), "ideCerhNaturezaPoco.ideCerhNaturezaPoco")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoCaracterizacao.class))
			;
		return dao.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Integer> listarByIdeCerhLocalizacaoGeografica(Collection<Integer> ideCerhLocalizacaoGeograficaCollection) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoCaracterizacao.class)
				.createAlias("ideCerhLocalizacaoGeografica", "clg")
				.add(Restrictions.in("clg.ideCerhLocalizacaoGeografica", ideCerhLocalizacaoGeograficaCollection))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideCerhCaptacaoCaracterizacao"), "ideCerhCaptacaoCaracterizacao"))
				;
		return ideDAO.listarPorCriteria(criteria);
	}

}
