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

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceEnergia;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceGeracaoEnergiaDAOImpl {

	@Inject
	private IDAO<FceEnergia> fceGeracaoEnergiaDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceEnergia getFceGeracaoEnergiaBy(Fce ideFce) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergia.class)		
			.add(Restrictions.eq("ideFce.ideFce", ideFce.getIdeFce()))   
			.setProjection(Projections.projectionList()
			   .add(Projections.groupProperty("ideFceEnergia"),"ideFceEnergia")
			   .add(Projections.groupProperty("desIdentificacaoEmpreendimento"),"desIdentificacaoEmpreendimento")
			   .add(Projections.groupProperty("valorArea"),"valorArea")
			   .add(Projections.groupProperty("numProcessoOutorga"),"numProcessoOutorga")
			   .add(Projections.groupProperty("numProcessoASV"),"numProcessoASV")
			   .add(Projections.groupProperty("indASV"),"indASV")
			   .add(Projections.groupProperty("indCaptacao"),"indCaptacao")
			   .add(Projections.groupProperty("ideFce"),"ideFce")
			   .add(Projections.groupProperty("ideLocalizacaoGeografica"),"ideLocalizacaoGeografica")
			   
			).setResultTransformer(new AliasToNestedBeanResultTransformer(FceEnergia.class)) ; 
			
		return fceGeracaoEnergiaDAO.buscarPorCriteria(detachedCriteria);
	}
	
	

}
