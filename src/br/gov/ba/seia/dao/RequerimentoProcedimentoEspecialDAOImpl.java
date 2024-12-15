package br.gov.ba.seia.dao;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoProcedimentoEspecial;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

public class RequerimentoProcedimentoEspecialDAOImpl {

	@Inject
	public IDAO<RequerimentoProcedimentoEspecial> requerimentoProcedimentoEspecialDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(RequerimentoProcedimentoEspecial requerimentoProcedimentoEspecial) {
		requerimentoProcedimentoEspecialDAO.salvarOuAtualizar(requerimentoProcedimentoEspecial);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(RequerimentoProcedimentoEspecial requerimentoProcedimentoEspecial) {
		requerimentoProcedimentoEspecialDAO.remover(requerimentoProcedimentoEspecial);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public RequerimentoProcedimentoEspecial obterRequerimentoProcedimentoEspecialPorRequerimento(Requerimento requerimento){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RequerimentoProcedimentoEspecial.class)
				.add(Restrictions.eq("ideRequerimento", requerimento))
				.setProjection(Projections.projectionList()
						.add(Projections.property("dataAssinaturaTermoCompromisso"), "dataAssinaturaTermoCompromisso")
						.add(Projections.property("ideRequerimento"),"ideRequerimento")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(RequerimentoProcedimentoEspecial.class));
		return requerimentoProcedimentoEspecialDAO.buscarPorCriteria(detachedCriteria);
	}
}
