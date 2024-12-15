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
import br.gov.ba.seia.entity.ReenquadramentoProcessoAtoTipoAtividadeFauna;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ReenquadramentoProcessoAtoTipoAtividadeFaunaDAOImpl {
	
	@Inject
	private IDAO<ReenquadramentoProcessoAtoTipoAtividadeFauna> reenquadramentoProcessoAtoTipoAtividadeFaunaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerPor(ReenquadramentoProcesso reenquadramentoProcesso)  {
		for (ReenquadramentoProcessoAto rpa : reenquadramentoProcesso.getReenquadramentoProcessoAtoCollection()) {
			if(!Util.isNullOuVazio(rpa)) {
				StringBuilder sql = new StringBuilder();
				sql.append("delete from ReenquadramentoProcessoAtoTipoAtividadeFauna rpataf where rpataf.ideReenquadramentoProcessoAto = :ideReenquadramentoProcessoAto");
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ideReenquadramentoProcessoAto", rpa);
				reenquadramentoProcessoAtoTipoAtividadeFaunaDAO.executarQuery(sql.toString(), params);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ReenquadramentoProcessoAtoTipoAtividadeFauna> listar(ReenquadramentoProcessoAto reenquadramentoProcessoAto)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(ReenquadramentoProcessoAtoTipoAtividadeFauna.class);
		
		criteria
			.createAlias("ideTipoAtividadeFauna", "taf", JoinType.INNER_JOIN)
			.createAlias("ideReenquadramentoProcessoAto", "rpa", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideReenquadramentoProcessoAto.ideReenquadramentoProcessoAto", reenquadramentoProcessoAto.getIdeReenquadramentoProcessoAto()))
			.setProjection(Projections.projectionList()
				.add(Projections.property("rpa.ideReenquadramentoProcessoAto"), "ideReenquadramentoProcessoAto.ideReenquadramentoProcessoAto")
				.add(Projections.property("taf.ideTipoAtividadeFauna"), "ideTipoAtividadeFauna.ideTipoAtividadeFauna")
				.add(Projections.property("taf.nomTipoAtividadeFauna"), "ideTipoAtividadeFauna.nomTipoAtividadeFauna")
				.add(Projections.property("taf.ideTipoAtividadeFauna"), "reenquadramentoProcessoAtoTipoAtividadeFaunaPK.ideTipoAtividadeFauna")
				.add(Projections.property("rpa.ideReenquadramentoProcessoAto"), "reenquadramentoProcessoAtoTipoAtividadeFaunaPK.ideReenquadramentoProcessoAto")
			)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(ReenquadramentoProcessoAtoTipoAtividadeFauna.class))
		;

		return reenquadramentoProcessoAtoTipoAtividadeFaunaDAO.listarPorCriteria(criteria);

	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ReenquadramentoProcessoAtoTipoAtividadeFauna rpataf)  {
		reenquadramentoProcessoAtoTipoAtividadeFaunaDAO.salvar(rpataf);
	}
	
}