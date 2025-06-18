package br.gov.ba.seia.dao;

import java.util.ArrayList;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.MotivoNotificacaoImovel;
import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MotivoNotificacaoImovelDAOImpl {
	
	@Inject
	private IDAO<MotivoNotificacaoImovel> motivoNotificacaoImovelDAO;
	
	public Collection<MotivoNotificacaoImovel> listarMotivoNotificacaoImovelPor(Integer ideNotificacaoMotivoNotificacao)  {	
		
		DetachedCriteria criteria = DetachedCriteria.forClass(MotivoNotificacaoImovel.class);
		criteria
			.createAlias("ideImovel", "i", JoinType.INNER_JOIN)
			.createAlias("i.imovelRural", "ir", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideNotificacaoMotivoNotificacao", "nmn", JoinType.INNER_JOIN)
			.add(Restrictions.eq("nmn.ideNotificacaoMotivoNotificacao", ideNotificacaoMotivoNotificacao))
			.addOrder(Order.asc("ir.desDenominacao"))
		;
		
		return motivoNotificacaoImovelDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacaoImovel> listarMotivoNotificacaoImovelParaRemover(NotificacaoMotivoNotificacao nmn)  {
		
		if(Util.isNullOuVazio(nmn.getMotivoNotificacaoImovelCollection())) {
			return null;
		}
		
		Collection<Integer> lista = new ArrayList<Integer>();
		for(MotivoNotificacaoImovel mni : nmn.getMotivoNotificacaoImovelCollection()) {
			if(!Util.isNull(mni.getIdeMotivoNotificacaoImovel())) {
				lista.add(mni.getIdeMotivoNotificacaoImovel());
			}
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoMotivoNotificacao.class);
		criteria
			.createAlias("motivoNotificacaoImovelCollection", "mni", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideNotificacaoMotivoNotificacao", nmn.getIdeNotificacaoMotivoNotificacao()))
		;
		
		if(!Util.isNullOuVazio(lista)) {
			criteria.add(Restrictions.not(Restrictions.in("mni.ideMotivoNotificacaoImovel", lista)));
		}
		
		criteria
			.setProjection(Projections.property("mni.ideMotivoNotificacaoImovel").as("ideMotivoNotificacaoImovel"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(MotivoNotificacaoImovel.class))
		;
		
		return motivoNotificacaoImovelDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirMotivoNotificacaoImovel(NotificacaoMotivoNotificacao nmn) {
		String deleteSQL = "DELETE FROM motivo_notificacao_imovel WHERE ide_notificacao_motivo_notificacao = :ide_notificacao_motivo_notificacao";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ide_notificacao_motivo_notificacao", nmn.getIdeNotificacaoMotivoNotificacao());
		motivoNotificacaoImovelDAO.executarNativeQuery(deleteSQL, params);	
	}
}
