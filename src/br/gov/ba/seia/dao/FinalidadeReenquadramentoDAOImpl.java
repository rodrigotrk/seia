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

import br.gov.ba.seia.entity.FinalidadeReenquadramento;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FinalidadeReenquadramentoDAOImpl {
	
	@Inject
	private IDAO<FinalidadeReenquadramento> finalidadeReenquadramentoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FinalidadeReenquadramento finalidadeReenquadramento)  {
		finalidadeReenquadramentoDAO.salvar(finalidadeReenquadramento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(FinalidadeReenquadramento finalidadeReenquadramento)  {
		finalidadeReenquadramentoDAO.remover(finalidadeReenquadramento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<FinalidadeReenquadramento> listarFinalidadeReenquadramento(ReenquadramentoProcesso reenquadramentoProcesso)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FinalidadeReenquadramento.class);
		criteria
			.createAlias("finalidadeReequadramentoProcessoCollection", "frp", JoinType.INNER_JOIN)
			.add(Restrictions.eq("frp.ideReenquadramentoProcesso", reenquadramentoProcesso))
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideFinalidadeReenquadramento"),"ideFinalidadeReenquadramento")
				.add(Projections.property("nomMotivoReenquadramento"),"nomMotivoReenquadramento")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(FinalidadeReenquadramento.class))
		;
		
		return finalidadeReenquadramentoDAO.listarPorCriteria(criteria);
	}
}