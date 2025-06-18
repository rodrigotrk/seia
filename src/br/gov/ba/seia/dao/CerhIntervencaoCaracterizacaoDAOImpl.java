package br.gov.ba.seia.dao;

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
import br.gov.ba.seia.entity.CerhIntervencaoCaracterizacao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhIntervencaoCaracterizacaoDAOImpl extends AbstractDAO<CerhIntervencaoCaracterizacao>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhIntervencaoCaracterizacao> dao;
	
	@Override
	protected IDAO<CerhIntervencaoCaracterizacao> getDAO() {
		return dao;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhIntervencaoCaracterizacao carregarParaHistorico(CerhLocalizacaoGeografica clg)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CerhIntervencaoCaracterizacao.class)
			
			.createAlias("ideCerhObrasHidraulicas", "ideCerhObrasHidraulicas", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhIntervencaoServico", "ideCerhIntervencaoServico", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhSituacaoTipoUso", "ideCerhSituacaoTipoUso", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("ideTipoIntervencao", "ideTipoIntervencao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoCorpoHidrico", "ideTipoCorpoHidrico", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhLocalizacaoGeografica", "cerhLoc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("cerhLoc.ideLocalizacaoGeografica", "loc", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("cerhLoc.ideCerhLocalizacaoGeografica", clg.getId()))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideCerhIntervencaoCaracterizacao"), "ideCerhIntervencaoCaracterizacao")
					.add(Projections.property("indOperacao"), "indOperacao")
					.add(Projections.property("indPch"), "indPch")
					.add(Projections.property("dscObservacao"), "dscObservacao")
					.add(Projections.property("indPotenciaInstaladaBarragem"), "indPotenciaInstaladaBarragem")
					.add(Projections.property("nomCorpoHidrico"), "nomCorpoHidrico")
					.add(Projections.property("valPotenciaInstaladaTotal"), "valPotenciaInstaladaTotal")
					.add(Projections.property("valProducaoAnualEfetivamenteVerificada"), "valProducaoAnualEfetivamenteVerificada")
					.add(Projections.property("dtInicioOperacao"), "dtInicioOperacao")
					.add(Projections.property("dtInicioOperacao"), "dtInicioOperacao")
					
					.add(Projections.property("cerhLoc.ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica")
					.add(Projections.property("loc.ideLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideLocalizacaoGeografica.ideLocalizacaoGeografica")
					
					.add(Projections.property("ideCerhIntervencaoServico.ideCerhIntervencaoServico"), "ideCerhIntervencaoServico.ideCerhIntervencaoServico")
					.add(Projections.property("ideCerhIntervencaoServico.dscIntervencaoServico"), "ideCerhIntervencaoServico.dscIntervencaoServico")
					
					.add(Projections.property("ideCerhObrasHidraulicas.dscObrasHidraulicas"), "ideCerhObrasHidraulicas.dscObrasHidraulicas")
					.add(Projections.property("ideCerhObrasHidraulicas.dscObrasHidraulicas"), "ideCerhObrasHidraulicas.dscObrasHidraulicas")
					
					.add(Projections.property("ideCerhSituacaoTipoUso.dscSituacaoTipoUso"), "ideCerhSituacaoTipoUso.dscSituacaoTipoUso")
					.add(Projections.property("ideCerhSituacaoTipoUso.dscSituacaoTipoUso"), "ideCerhSituacaoTipoUso.dscSituacaoTipoUso")

					.add(Projections.property("ideTipoIntervencao.ideTipoIntervencao"), "ideTipoIntervencao.ideTipoIntervencao")
					.add(Projections.property("ideTipoIntervencao.nomTipoIntervencao"), "ideTipoIntervencao.nomTipoIntervencao")
					.add(Projections.property("ideTipoCorpoHidrico.ideTipoCorpoHidrico"), "ideTipoCorpoHidrico.ideTipoCorpoHidrico")
					.add(Projections.property("ideTipoCorpoHidrico.nomTipoCorpoHidrico"), "ideTipoCorpoHidrico.nomTipoCorpoHidrico")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhIntervencaoCaracterizacao.class));
		return dao.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhIntervencaoCaracterizacao carregarCerhIntervencaoCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica){
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhIntervencaoCaracterizacao.class)
			.add(Restrictions.eq("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica", cerhLocalizacaoGeografica.getIdeCerhLocalizacaoGeografica()))
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideCerhIntervencaoCaracterizacao"), "ideCerhIntervencaoCaracterizacao")
						.add(Projections.property("indOperacao"), "indOperacao")
						.add(Projections.property("indPch"), "indPch")
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("dscObservacao"), "dscObservacao")
						.add(Projections.property("indPotenciaInstaladaBarragem"), "indPotenciaInstaladaBarragem")
						.add(Projections.property("nomCorpoHidrico"), "nomCorpoHidrico")
						.add(Projections.property("valPotenciaInstaladaTotal"), "valPotenciaInstaladaTotal")
						.add(Projections.property("valProducaoAnualEfetivamenteVerificada"), "valProducaoAnualEfetivamenteVerificada")
						.add(Projections.property("dtInicioOperacao"), "dtInicioOperacao")
						.add(Projections.property("ideCerhIntervencaoServico.ideCerhIntervencaoServico"), "ideCerhIntervencaoServico.ideCerhIntervencaoServico")
						.add(Projections.property("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica")
						.add(Projections.property("ideCerhObrasHidraulicas.ideCerhObrasHidraulicas"), "ideCerhObrasHidraulicas.ideCerhObrasHidraulicas")
						.add(Projections.property("ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso"), "ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso")
						.add(Projections.property("ideTipoCorpoHidrico.ideTipoCorpoHidrico"), "ideTipoCorpoHidrico.ideTipoCorpoHidrico")
						.add(Projections.property("ideCerhLocalizacaoGeograficaBarragem.ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeograficaBarragem.ideCerhLocalizacaoGeografica")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhIntervencaoCaracterizacao.class));
		return dao.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhIntervencaoCaracterizacao carregarIdeCerhIntervencaoCaracterizacao(CerhIntervencaoCaracterizacao cerhIntervencaoCaracterizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhIntervencaoCaracterizacao.class)
			.add(Restrictions.eq("ideCerhIntervencaoCaracterizacao", cerhIntervencaoCaracterizacao.getIdeCerhIntervencaoCaracterizacao()))
				.createAlias("ideCerhLocalizacaoGeografica", "ideCerhLocalizacaoGeografica", JoinType.INNER_JOIN)
				.createAlias("ideCerhLocalizacaoGeograficaBarragem", "ideCerhLocalizacaoGeograficaBarragem",  JoinType.LEFT_OUTER_JOIN);
		return dao.buscarPorCriteria(criteria);
	}


	
}
