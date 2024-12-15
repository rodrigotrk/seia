package br.gov.ba.seia.service;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DevedorReposicaoFlorestal;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DevedorReposicaoFlorestalService {

	@Inject
	private IDAO<DevedorReposicaoFlorestal> devedorReposicaoFlorestalDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DevedorReposicaoFlorestal obterDevedorReposicaoFlorestalCumprimentoReposicaoFlorestal(Integer ideCumprimentoReposicaoFlorestal) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DevedorReposicaoFlorestal.class);
		
		criteria.createAlias("ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		criteria.createAlias("ideOrgaoEmissorAuto", "ora", JoinType.INNER_JOIN);
		criteria.createAlias("ideBioma", "bio", JoinType.INNER_JOIN);
		criteria.createAlias("ideMunicipio", "mun", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal", ideCumprimentoReposicaoFlorestal));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideDevedorReposicaoFlorestal"), "ideDevedorReposicaoFlorestal")
					.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
					.add(Projections.property("ora.ideOrgaoEmissorAuto"), "ideOrgaoEmissorAuto.ideOrgaoEmissorAuto")
					.add(Projections.property("mun.ideMunicipio"), "ideMunicipio.ideMunicipio")
					.add(Projections.property("numAutoInfracao"), "numAutoInfracao")
					.add(Projections.property("bio.ideBioma"), "ideBioma.ideBioma")
					.add(Projections.property("bio.nomBioma"), "ideBioma.nomBioma")
					.add(Projections.property("bio.metrosCubicos"), "ideBioma.metrosCubicos")
					.add(Projections.property("vlrAreaSuprimida"), "vlrAreaSuprimida")
					.add(Projections.property("valVolumeReferencia"), "valVolumeReferencia")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(DevedorReposicaoFlorestal.class))
				;
		return devedorReposicaoFlorestalDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DevedorReposicaoFlorestal obterDevedorPorRequerimento(Integer ideRequerimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DevedorReposicaoFlorestal.class);
		
		criteria.createAlias("ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		criteria.createAlias("crf.requerimento", "req", JoinType.INNER_JOIN);

		
		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideDevedorReposicaoFlorestal"), "ideDevedorReposicaoFlorestal")
					.add(Projections.property("req.ideRequerimento"), "ideDevedorReposicaoFlorestal.requerimento.ideRequerimento")
					.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
					.add(Projections.property("numAutoInfracao"), "numAutoInfracao")
					.add(Projections.property("vlrAreaSuprimida"), "vlrAreaSuprimida")
					.add(Projections.property("ideBioma"), "ideBioma")
					.add(Projections.property("ideOrgaoEmissorAuto"), "ideOrgaoEmissorAuto")
					.add(Projections.property("nomeArquivo"), "nomeArquivo")
					.add(Projections.property("dtcGravado"), "dtcGravado")
					.add(Projections.property("dscCaminhoParecerTecnico"), "dscCaminhoParecerTecnico")
//					.add(Projections.property("ideCumprimentoReposicaoFlorestal"), "ideCumprimentoReposicaoFlorestal")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(DevedorReposicaoFlorestal.class))
				;
		return devedorReposicaoFlorestalDAO.buscarPorCriteria(criteria);
	}
	
	public void salvarDevedorReposicaoFlorestal(DevedorReposicaoFlorestal devedorReposicaoFlorestal){
		devedorReposicaoFlorestalDAO.salvarOuAtualizar(devedorReposicaoFlorestal);
	}
}
