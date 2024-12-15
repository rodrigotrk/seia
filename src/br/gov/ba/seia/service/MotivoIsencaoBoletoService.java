package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.MotivoIsencaoBoleto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MotivoIsencaoBoletoService{

	@Inject
	IDAO<MotivoIsencaoBoleto> motivoDAO;

	public List<MotivoIsencaoBoleto> listarMotivosInsencaoDoRequerimentoAtivos()  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MotivoIsencaoBoleto.class);
		
		criteria
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideMotivoIsencaoBoleto"),"ideMotivoIsencaoBoleto")
					.add(Projections.property("dscMotivoIsencaoBoleto"),"dscMotivoIsencaoBoleto")
					.add(Projections.property("indAtivo"),"indAtivo")
					.add(Projections.property("indRequerimento"),"indRequerimento")
					.add(Projections.property("indReenquadramento"),"indReenquadramento")
			).setResultTransformer(new AliasToNestedBeanResultTransformer(MotivoIsencaoBoleto.class))
			
			.add(Restrictions.eq("indAtivo", true))
			.add(Restrictions.eq("indRequerimento", true));
		;
		
		return motivoDAO.listarPorCriteria(criteria, Order.asc("dscMotivoIsencaoBoleto"));
	}
	
	public List<MotivoIsencaoBoleto> listarMotivosInsencaoDoReenquadramentoAtivos()  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MotivoIsencaoBoleto.class);
		
		criteria
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideMotivoIsencaoBoleto"),"ideMotivoIsencaoBoleto")
					.add(Projections.property("dscMotivoIsencaoBoleto"),"dscMotivoIsencaoBoleto")
					.add(Projections.property("indAtivo"),"indAtivo")
					.add(Projections.property("indRequerimento"),"indRequerimento")
					.add(Projections.property("indReenquadramento"),"indReenquadramento")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(MotivoIsencaoBoleto.class))
				
				.add(Restrictions.eq("indAtivo", true))
				.add(Restrictions.eq("indReenquadramento", true));
		
		return motivoDAO.listarPorCriteria(criteria, Order.asc("dscMotivoIsencaoBoleto"));
	}
}