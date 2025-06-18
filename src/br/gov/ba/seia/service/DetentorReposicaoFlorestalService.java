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
import br.gov.ba.seia.entity.DetentorReposicaoFlorestal;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DetentorReposicaoFlorestalService {

	@Inject
	private IDAO<DetentorReposicaoFlorestal> detentorReposicaoFlorestalDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DetentorReposicaoFlorestal obterDetentorReposicaoFlorestalPorCumprimentoReposicaoFlorestal(Integer ideCumprimentoReposicaoFlorestal)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DetentorReposicaoFlorestal.class);
		
		criteria.createAlias("ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		criteria.createAlias("ideUnidadeMedida", "um", JoinType.INNER_JOIN);
		criteria.createAlias("ideTipoVolumeFlorestalRemanescente", "tvfr", JoinType.LEFT_OUTER_JOIN);
		
		criteria.add(Restrictions.eq("ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal", ideCumprimentoReposicaoFlorestal));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideDetentorReposicaoFlorestal"), "ideDetentorReposicaoFlorestal")
					.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
					.add(Projections.property("numPortaria"), "numPortaria")
					.add(Projections.property("dataPublicacaoPortaria"), "dataPublicacaoPortaria")
					.add(Projections.property("numProcesso"), "numProcesso")
					.add(Projections.property("volumeAutorizado"), "volumeAutorizado")
					.add(Projections.property("tvfr.ideTipoVolumeFlorestalRemanescente"), "ideTipoVolumeFlorestalRemanescente.ideTipoVolumeFlorestalRemanescente")
					.add(Projections.property("numProcessoAsvAml"), "numProcessoAsvAml")
					.add(Projections.property("um.ideUnidadeMedida"), "ideUnidadeMedida.ideUnidadeMedida")
					.add(Projections.property("um.codUnidadeMedida"), "ideUnidadeMedida.codUnidadeMedida")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(DetentorReposicaoFlorestal.class))
				;
		return detentorReposicaoFlorestalDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DetentorReposicaoFlorestal obterDetentorReposicaoFlorestalPorRequerimento(Integer ideRequerimento)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DetentorReposicaoFlorestal.class);
		
		criteria.createAlias("ideCumprimentoReposicaoFlorestal", "crf", JoinType.INNER_JOIN);
		criteria.createAlias("crf.requerimento", "req", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("req.ideRequerimento", ideRequerimento));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideDetentorReposicaoFlorestal"), "ideDetentorReposicaoFlorestal")
					.add(Projections.property("req.ideRequerimento"), "ideDetentorReposicaoFlorestal.requerimento.ideRequerimento")
					.add(Projections.property("crf.ideCumprimentoReposicaoFlorestal"), "ideCumprimentoReposicaoFlorestal.ideCumprimentoReposicaoFlorestal")
					.add(Projections.property("numPortaria"), "numPortaria")
					.add(Projections.property("dataPublicacaoPortaria"), "dataPublicacaoPortaria")
					.add(Projections.property("numProcesso"), "numProcesso")
					.add(Projections.property("volumeAutorizado"), "volumeAutorizado")
					.add(Projections.property("numProcessoAsvAml"), "numProcessoAsvAml")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(DetentorReposicaoFlorestal.class))
				;
		return detentorReposicaoFlorestalDAO.buscarPorCriteria(criteria);
	}

}
