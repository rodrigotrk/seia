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

import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAtoObjetivoAtividadeManejo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReenquadramentoProcessoAtoObjetivoAtividadeManejoDAOImpl {
	
	@Inject
	private IDAO<ReenquadramentoProcessoAtoObjetivoAtividadeManejo> reenquadramentoProcessoAtoObjetivoAtividadeManejoDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPor(ReenquadramentoProcesso reenquadramentoProcesso)  {
		for (ReenquadramentoProcessoAto rpa : reenquadramentoProcesso.getReenquadramentoProcessoAtoCollection()) {
			if(!Util.isNullOuVazio(rpa)) {
				StringBuilder sql = new StringBuilder();
				sql.append("delete from ReenquadramentoProcessoAtoObjetivoAtividadeManejo rpaoam where rpaoam.ideReenquadramentoProcessoAto = :ideReenquadramentoProcessoAto");
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ideReenquadramentoProcessoAto", rpa);
				reenquadramentoProcessoAtoObjetivoAtividadeManejoDAO.executarQuery(sql.toString(), params);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ReenquadramentoProcessoAtoObjetivoAtividadeManejo> listar(ReenquadramentoProcessoAto reenquadramentoProcessoAto)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoProcessoAtoObjetivoAtividadeManejo.class);
		criteria
			.createAlias("ideObjetivoAtividadeManejo", "oam", JoinType.INNER_JOIN)
			.createAlias("ideReenquadramentoProcessoAto", "rpa", JoinType.INNER_JOIN)
			.add(Restrictions.eq("rpa.ideReenquadramentoProcessoAto", reenquadramentoProcessoAto.getIdeReenquadramentoProcessoAto()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("rpa.ideReenquadramentoProcessoAto"), "ideReenquadramentoProcessoAto.ideReenquadramentoProcessoAto")
				.add(Projections.property("oam.ideObjetivoAtividadeManejo"), "ideObjetivoAtividadeManejo.ideObjetivoAtividadeManejo")
				.add(Projections.property("oam.nomObjetivoAtividadeManejo"), "ideObjetivoAtividadeManejo.nomObjetivoAtividadeManejo")
				.add(Projections.property("oam.ideObjetivoAtividadeManejo"), "reenquadramentoProcessoAtoObjetivoAtividadeManejoPK.ideObjetivoAtividadeManejo")
				.add(Projections.property("rpa.ideReenquadramentoProcessoAto"), "reenquadramentoProcessoAtoObjetivoAtividadeManejoPK.ideReenquadramentoProcessoAto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReenquadramentoProcessoAtoObjetivoAtividadeManejo.class))
		;
		return reenquadramentoProcessoAtoObjetivoAtividadeManejoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReenquadramentoProcessoAtoObjetivoAtividadeManejo rpaoam)  {
		reenquadramentoProcessoAtoObjetivoAtividadeManejoDAO.salvar(rpaoam);
	}
	
}