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
import br.gov.ba.seia.entity.TaxaIndiceCobranca;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TaxaIndiceCobrancaService {

	@Inject
	IDAO<TaxaIndiceCobranca> taxaIndiceCobrancaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TaxaIndiceCobranca obterTaxaporIndiceCobranca(Integer indiceCobranca) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxaIndiceCobranca.class)
			.add(Restrictions.eq("ideIndiceCobranca.ideIndiceCobranca", indiceCobranca));
		
		return taxaIndiceCobrancaIDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TaxaIndiceCobranca obterTaxaporIndiceCobrancaEMes(Integer indiceCobranca, Integer mesReferencia, Integer anoReferencia) throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TaxaIndiceCobranca.class);
			
			criteria.createAlias("ideIndiceCobranca", "idc", JoinType.INNER_JOIN)
				
			.add(Restrictions.eq("mesReferencia", mesReferencia))
			.add(Restrictions.eq("anoReferencia", anoReferencia))
			.add(Restrictions.eq("idc.ideIndiceCobranca", indiceCobranca))
		
			.setProjection(Projections.distinct(Projections.projectionList()
	                .add(Projections.property("ideTaxaIndiceCobranca"),"ideTaxaIndiceCobranca")
	                .add(Projections.property("valor"),"valor")
	                .add(Projections.property("dtcCriacao"),"dtcCriacao")
	                .add(Projections.property("mesReferencia"),"mesReferencia")
	                .add(Projections.property("anoReferencia"),"anoReferencia")
	                .add(Projections.property("idc.ideIndiceCobranca"),"ideIndiceCobranca.ideIndiceCobranca")
	                .add(Projections.property("idc.dscIndiceCobranca"),"ideIndiceCobranca.dscIndiceCobranca")
	                .add(Projections.property("idc.sglIndiceCobranca"),"ideIndiceCobranca.sglIndiceCobranca")
	            ))
	        .setResultTransformer(new AliasToNestedBeanResultTransformer(TaxaIndiceCobranca.class));
		
		return taxaIndiceCobrancaIDAO.buscarPorCriteria(criteria);
	}

}