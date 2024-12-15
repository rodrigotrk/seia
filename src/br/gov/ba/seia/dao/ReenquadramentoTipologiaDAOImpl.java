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

import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.entity.ReenquadramentoTipologia;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReenquadramentoTipologiaDAOImpl {
	
	@Inject
	private IDAO<ReenquadramentoTipologia> reenquadramentoTipologiaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReenquadramentoTipologia reenquadramentoTipologia)  {
		reenquadramentoTipologiaDAO.salvar(reenquadramentoTipologia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ReenquadramentoTipologia reenquadramentoTipologia)  {
		reenquadramentoTipologiaDAO.remover(reenquadramentoTipologia);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPor(ReenquadramentoProcesso reenquadramentoProcesso)  {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ReenquadramentoTipologia rt where rt.ideReenquadramentoProcesso = :ideReenquadramentoProcesso");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideReenquadramentoProcesso", reenquadramentoProcesso);
		
		reenquadramentoTipologiaDAO.executarQuery(sql.toString(),params);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Collection<ReenquadramentoTipologia> listarReenquadramentoTipologia(ReenquadramentoProcesso reenquadramentoProcesso)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoTipologia.class);
		criteria
			.createAlias("ideTipologia","t")
			.add(Restrictions.eq("ideReenquadramentoProcesso", reenquadramentoProcesso))
			.setProjection(Projections.projectionList()
				.add(Projections.property("t.ideTipologia"),"ideTipologia.ideTipologia")
				.add(Projections.property("t.codTipologia"),"ideTipologia.codTipologia")
				.add(Projections.property("t.desTipologia"),"ideTipologia.desTipologia")
				.add(Projections.property("ideReenquadramentoProcesso.ideReenquadramentoProcesso"),"ideReenquadramentoProcesso.ideReenquadramentoProcesso")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReenquadramentoTipologia.class))
		;
		
		return reenquadramentoTipologiaDAO.listarPorCriteria(criteria);
	}
}