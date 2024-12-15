package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

import br.gov.ba.seia.entity.FinalidadeReenquadramentoProcesso;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FinalidadeReenquadramentoProcessoDAOImpl {
	
	@Inject
	private IDAO<FinalidadeReenquadramentoProcesso> finalidadeReenquadramentoProcessoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FinalidadeReenquadramentoProcesso finalidadeReenquadramentoProcesso)  {
		finalidadeReenquadramentoProcessoDAO.salvar(finalidadeReenquadramentoProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(FinalidadeReenquadramentoProcesso finalidadeReenquadramentoProcesso)  {
		finalidadeReenquadramentoProcessoDAO.remover(finalidadeReenquadramentoProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPor(ReenquadramentoProcesso reenquadramentoProcesso)  {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from FinalidadeReenquadramentoProcesso frp where frp.ideReenquadramentoProcesso = :ideReenquadramentoProcesso");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideReenquadramentoProcesso", reenquadramentoProcesso);
		
		finalidadeReenquadramentoProcessoDAO.executarQuery(sql.toString(),params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<FinalidadeReenquadramentoProcesso> listarFinalidadeReenquadramentoProcesso(ReenquadramentoProcesso reenquadramentoProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FinalidadeReenquadramentoProcesso.class);
			criteria
				.createAlias("ideReenquadramentoProcesso","rp")
				.createAlias("ideFinalidadeReenquadramento","fr")
				.add(Restrictions.eq("ideReenquadramentoProcesso", reenquadramentoProcesso))
				.setProjection(Projections.projectionList()
					.add(Projections.property("rp.ideReenquadramentoProcesso"),"ideReenquadramentoProcesso.ideReenquadramentoProcesso")
					.add(Projections.property("fr.ideFinalidadeReenquadramento"),"ideFinalidadeReenquadramento.ideFinalidadeReenquadramento")
					.add(Projections.property("fr.nomMotivoReenquadramento"),"ideFinalidadeReenquadramento.nomMotivoReenquadramento")
					
					.add(Projections.property("rp.ideReenquadramentoProcesso"),"finalidadeReenquadramentoProcessoPK.ideReenquadramentoProcesso")
					.add(Projections.property("fr.ideFinalidadeReenquadramento"),"finalidadeReenquadramentoProcessoPK.ideFinalidadeReenquadramento")
				)
				.setResultTransformer(new AliasToNestedBeanResultTransformer(FinalidadeReenquadramentoProcesso.class))
		;
		return finalidadeReenquadramentoProcessoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<FinalidadeReenquadramentoProcesso> listarFinalidadeReenquadramentoProcessoPorProceso(Integer ideProcesso) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FinalidadeReenquadramentoProcesso.class);
		criteria
			.createAlias("ideFinalidadeReenquadramento", "fir", JoinType.INNER_JOIN)
			.createAlias("ideReenquadramentoProcesso", "rep", JoinType.INNER_JOIN)
			.createAlias("rep.ideNotificacao", "not", JoinType.INNER_JOIN)
			.createAlias("not.ideProcesso", "pro", JoinType.INNER_JOIN)
			.createAlias("pro.processoAtoCollection", "pra", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("pro.ideProcesso", ideProcesso));
	
		return  finalidadeReenquadramentoProcessoDAO.listarPorCriteria(criteria);
	}
}