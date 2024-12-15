package br.gov.ba.seia.dao;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.LacPostoCombustivel;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

public class LacPostoCombustivelDAOImpl extends BaseDAO<LacPostoCombustivel>{

	@Inject
	IDAO<LacPostoCombustivel> lacPostoCombustivelDAO;
	
	@Override
	protected IDAO<LacPostoCombustivel> getDao() {
		return this.lacPostoCombustivelDAO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)	
	public LacPostoCombustivel carregarByIdeRequerimento(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LacPostoCombustivel.class)
				.createAlias("ideDocumentoObrigatorio", "documento", JoinType.INNER_JOIN)				
				.createAlias("ideRequerimento", "requerimento", JoinType.INNER_JOIN)
				.createAlias("ideTipoBandeiraPostoCombustivel", "tipoBandeira", JoinType.INNER_JOIN)
				.createAlias("ideDistribuidoraPosto", "distribuidora", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideClasseNbrPosto", "classe", JoinType.LEFT_OUTER_JOIN)
				.createAlias("requerimento.ideOrgao", "orgao", JoinType.INNER_JOIN)
				.createAlias("requerimento.certificados", "certificados", JoinType.LEFT_OUTER_JOIN)
				.addOrder(Order.desc("certificados.dtcEmissaoCertificado"));

		criteria.setProjection(Projections.projectionList()

				.add(Projections.property("ideLac"), "ideLac")
				.add(Projections.property("dtcValidadeContratoDistribuidora"), "dtcValidadeContratoDistribuidora")
				.add(Projections.property("dtcInicioOperacao"), "dtcInicioOperacao")
				.add(Projections.property("indFlutuante"), "indFlutuante")
				
				.add(Projections.property("dscOutrosDistribuidora"), "dscOutrosDistribuidora")
				
				.add(Projections.property("indReformado"), "indReformado")
				.add(Projections.property("dtcInicioOperacao"), "dtcInicioOperacao")

				.add(Projections.property("profundidadeLencol"), "profundidadeLencol")
				.add(Projections.property("qtdTrocaOleo"), "qtdTrocaOleo")
				
				.add(Projections.property("qtdAreaTotal"), "qtdAreaTotal")
				.add(Projections.property("qtdAreaConstruida"), "qtdAreaConstruida")
				.add(Projections.property("qtdAreaTratamento"), "qtdAreaTratamento")
				.add(Projections.property("qtdAreaAmpliacao"), "qtdAreaAmpliacao")
				.add(Projections.property("qtdAreaOutras"), "qtdAreaOutras")
				
				.add(Projections.property("tipoBandeira.ideTipoBandeiraPostoCombustivel"), "ideTipoBandeiraPostoCombustivel.ideTipoBandeiraPostoCombustivel")
				
				.add(Projections.property("distribuidora.ideDistribuidoraPosto"), "ideDistribuidoraPosto.ideDistribuidoraPosto")
				
				.add(Projections.property("classe.ideClasseNbrPosto"), "ideClasseNbrPosto.ideClasseNbrPosto")
				
				.add(Projections.property("codAnp"), "codAnp")
				.add(Projections.property("dscDestinoTanquesRemovidos"), "dscDestinoTanquesRemovidos")

				.add(Projections.property("indSistemaControleAutomatico"), "indSistemaControleAutomatico")

				.add(Projections.property("indAcidente"), "indAcidente")
				.add(Projections.property("dscOcorrenciaAcidente"), "dscOcorrenciaAcidente")

				.add(Projections.property("qtdLavagemVeiculos"), "qtdLavagemVeiculos")
				
				.add(Projections.property("indAreaIndigena"), "indAreaIndigena")
				.add(Projections.property("indSitioArqueologico"), "indSitioArqueologico")
				.add(Projections.property("dtcCriacao"), "dtcCriacao")

				.add(Projections.property("requerimento.ideRequerimento"), "ideRequerimento.ideRequerimento")
				
				.add(Projections.property("orgao.ideOrgao"), "ideRequerimento.ideOrgao.ideOrgao")
				.add(Projections.property("orgao.codOrgao"), "ideRequerimento.ideOrgao.codOrgao")
				
				.add(Projections.property("certificados.ideCertificado"), "certificado.ideCertificado")
				
				.add(Projections.property("documento.ideDocumentoObrigatorio"), "ideDocumentoObrigatorio.ideDocumentoObrigatorio"));
				
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(LacPostoCombustivel.class));

		criteria.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));

		criteria.add(Restrictions.or(Restrictions.eq("certificados.ideAtoAmbiental.ideAtoAmbiental", AtoAmbientalEnum.LAC.getId()),Restrictions.isNull("certificados.ideAtoAmbiental.ideAtoAmbiental")));
		
		return lacPostoCombustivelDAO.buscarPorCriteriaMaxResult(criteria);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)	
	public Boolean hasLac(Integer ideRequerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(LacPostoCombustivel.class)
				.createAlias("ideDocumentoObrigatorio", "documento", JoinType.INNER_JOIN)				
				.createAlias("ideRequerimento", "requerimento", JoinType.INNER_JOIN)
				.createAlias("ideTipoBandeiraPostoCombustivel", "tipoBandeira", JoinType.INNER_JOIN)
				.createAlias("ideDistribuidoraPosto", "distribuidora", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideClasseNbrPosto", "classe", JoinType.LEFT_OUTER_JOIN)
				.createAlias("requerimento.ideOrgao", "orgao", JoinType.INNER_JOIN)
				.createAlias("requerimento.certificados", "certificados", JoinType.LEFT_OUTER_JOIN);

		criteria.setProjection(Projections.projectionList()
			.add(Projections.property("ideLac"), "ideLac")
		);
				
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(LacPostoCombustivel.class));

		criteria.add(Restrictions.eq("requerimento.ideRequerimento", ideRequerimento));

		criteria.add(Restrictions.or(Restrictions.eq("certificados.ideAtoAmbiental.ideAtoAmbiental", AtoAmbientalEnum.LAC.getId()),Restrictions.isNull("certificados.ideAtoAmbiental.ideAtoAmbiental")));
		
		return !Util.isNull(lacPostoCombustivelDAO.buscarPorCriteria(criteria));
		
	}
}
