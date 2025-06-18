package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJB;
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

import br.gov.ba.seia.dto.NotificacaoFinalDTO;
import br.gov.ba.seia.entity.MotivoNotificacaoImovel;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificacaoMotivoNotificacaoDAOImpl {
	
	@EJB
	private MotivoNotificacaoImovelDAOImpl motivoNotificacaoImovelDAOImpl; 
	
	@Inject
	private IDAO<NotificacaoMotivoNotificacao> notificaMotivoNotificacaoDAO;
	
	@Inject
	private IDAO<MotivoNotificacaoImovel> motivoNotificacaoImovelDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<NotificacaoMotivoNotificacao> listarNotificacaoMotivoNotificacaoPor(Notificacao notificacao) throws Exception {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoMotivoNotificacao.class)
			.createAlias("ideNotificacao", "n", JoinType.INNER_JOIN)
			.createAlias("ideMotivoNotificacao", "mn", JoinType.INNER_JOIN)
			.createAlias("mn.arquivoProcessoCollection", "ap", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ap.localizacaoGeografica", "locGeo", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("n.ideNotificacao", notificacao.getIdeNotificacao()));
		
		Collection<NotificacaoMotivoNotificacao> list = notificaMotivoNotificacaoDAO.listarPorCriteria(criteria);
		
		for(NotificacaoMotivoNotificacao nmn : list) {
			nmn.setMotivoNotificacaoImovelCollection(motivoNotificacaoImovelDAOImpl.listarMotivoNotificacaoImovelPor(nmn.getIdeNotificacaoMotivoNotificacao()));
		}
		
		return list;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<NotificacaoMotivoNotificacao> listarNotificacaoMotivoNotificacaoParaRemover(Notificacao notificacao) throws Exception {
		
		Collection<Integer> lista = new ArrayList<Integer>(); 
		for(NotificacaoMotivoNotificacao nmn : notificacao.getNotificacaoMotivoNotificacaoCollection()) {
			if(!Util.isNull(nmn.getIdeNotificacaoMotivoNotificacao())) {
				lista.add(nmn.getIdeNotificacaoMotivoNotificacao());
			}
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoMotivoNotificacao.class);
		criteria
			.createAlias("ideNotificacao", "n", JoinType.INNER_JOIN)
			.createAlias("ideMotivoNotificacao", "mn", JoinType.INNER_JOIN)
			.add(Restrictions.eq("n.ideNotificacao", notificacao.getIdeNotificacao()))
		;
		
		if(!Util.isNullOuVazio(lista)) {
			criteria.add(Restrictions.not(Restrictions.in("ideNotificacaoMotivoNotificacao", lista)));
		}
		
		criteria
			.setProjection(Projections.property("ideNotificacaoMotivoNotificacao").as("ideNotificacaoMotivoNotificacao"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(NotificacaoMotivoNotificacao.class))
		;
		
		return notificaMotivoNotificacaoDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<NotificacaoMotivoNotificacao> listarNotificacaoMotivoNotificacaoComShapeParaRemover(Notificacao notificacao) throws Exception {
		
		Collection<Integer> lista = new ArrayList<Integer>(); 
		for(NotificacaoMotivoNotificacao nmn : notificacao.getNotificacaoMotivoNotificacaoCollectionComShape()) {
			if(!Util.isNull(nmn.getIdeNotificacaoMotivoNotificacao())) {
				lista.add(nmn.getIdeNotificacaoMotivoNotificacao());
			}
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(NotificacaoMotivoNotificacao.class);
		criteria
			.createAlias("ideNotificacao", "n", JoinType.INNER_JOIN)
			.createAlias("ideMotivoNotificacao", "mn", JoinType.INNER_JOIN)
			.add(Restrictions.eq("n.ideNotificacao", notificacao.getIdeNotificacao()))
		;
		
		if(!Util.isNullOuVazio(lista)) {
			criteria.add(Restrictions.not(Restrictions.in("ideNotificacaoMotivoNotificacao", lista)));
		}
		
		criteria
			.setProjection(Projections.property("ideNotificacaoMotivoNotificacao").as("ideNotificacaoMotivoNotificacao"))
			.setResultTransformer(new AliasToNestedBeanResultTransformer(NotificacaoMotivoNotificacao.class))
		;
		
		return notificaMotivoNotificacaoDAO.listarPorCriteria(criteria);
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Collection<NotificacaoMotivoNotificacao> lista) throws Exception {
		for(NotificacaoMotivoNotificacao nmn : lista) {
			notificaMotivoNotificacaoDAO.salvarOuAtualizar(nmn);
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Collection<NotificacaoMotivoNotificacao> lista, NotificacaoFinalDTO dto) throws Exception {
		
		if(TipoNotificacaoEnum.NOTIFICACAO_COMUNICACAO.equals(dto.getTipoNotificacaoEnum())) {
			for(NotificacaoMotivoNotificacao nmn : lista) {
				removerNotificacaoImovelMotivo(dto.getNotificacaoFinal());
				nmn.setIdeNotificacaoMotivoNotificacao(null);
				notificaMotivoNotificacaoDAO.salvarOuAtualizar(nmn);
			}
			
			return;
		}else if (TipoNotificacaoEnum.NOTIFICACAO_PRAZO.equals(dto.getTipoNotificacaoEnum())) {
			for(NotificacaoMotivoNotificacao nmn : dto.getListaNotificacaoMotivoNotificacaoEnvioShape()) {
				if(!Util.isNullOuVazio(nmn.getIdeNotificacaoMotivoNotificacao())) {
					motivoNotificacaoImovelDAOImpl.excluirMotivoNotificacaoImovel(nmn);
				}
			}
			
			removerNotificacaoImovelMotivo(dto.getNotificacaoFinal());
			
			for(NotificacaoMotivoNotificacao nmn : dto.getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado()) {
				nmn.setIdeNotificacaoMotivoNotificacao(null);
				notificaMotivoNotificacaoDAO.salvar(nmn);
			}
			
			for(NotificacaoMotivoNotificacao nmn : lista) {
				nmn.setIdeNotificacaoMotivoNotificacao(null);
				notificaMotivoNotificacaoDAO.salvar(nmn);
			}
		}else {
			for(NotificacaoMotivoNotificacao nmn : lista) {
				notificaMotivoNotificacaoDAO.salvarOuAtualizar(nmn);
			}
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Collection<NotificacaoMotivoNotificacao> lista) throws Exception {
		for(NotificacaoMotivoNotificacao nmn : lista) {
			notificaMotivoNotificacaoDAO.salvar(nmn);
			for(MotivoNotificacaoImovel notificacaoImovel : nmn.getMotivoNotificacaoImovelCollection()) {
	            notificacaoImovel.setIdeNotificacaoMotivoNotificacao(nmn);
	            motivoNotificacaoImovelDAO.salvar(notificacaoImovel);
	        } 
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerNaoListados(Notificacao notificacao) throws Exception {
		
		for(NotificacaoMotivoNotificacao nmn : notificacao.getNotificacaoMotivoNotificacaoCollection()) {
			Collection<MotivoNotificacaoImovel> lista = motivoNotificacaoImovelDAOImpl.listarMotivoNotificacaoImovelParaRemover(nmn);
			if(!Util.isNullOuVazio(lista)) {
				StringBuilder sql = new StringBuilder();
				sql.append("delete from MotivoNotificacaoImovel mni where mni in :lista");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("lista", lista);
				notificaMotivoNotificacaoDAO.executarQuery(sql.toString(), params);
			}
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerNaoListadosComShape(Notificacao notificacao) throws Exception {
		
		for(NotificacaoMotivoNotificacao nmn : notificacao.getNotificacaoMotivoNotificacaoCollectionComShape()) {
			Collection<MotivoNotificacaoImovel> lista = motivoNotificacaoImovelDAOImpl.listarMotivoNotificacaoImovelParaRemover(nmn);
			if(!Util.isNullOuVazio(lista)) {
				StringBuilder sql = new StringBuilder();
				sql.append("delete from MotivoNotificacaoImovel mni where mni in :lista");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("lista", lista);
				notificaMotivoNotificacaoDAO.executarQuery(sql.toString(), params);
			}
		}
		
		Collection<NotificacaoMotivoNotificacao> lista = listarNotificacaoMotivoNotificacaoComShapeParaRemover(notificacao);
		
		if(!Util.isNullOuVazio(lista)) {
			StringBuilder sql = null;
			Map<String, Object> params = new HashMap<String, Object>();
			for (Iterator<NotificacaoMotivoNotificacao> iterator = lista.iterator(); iterator.hasNext();) {
				NotificacaoMotivoNotificacao notificacaoMotivoNotificacao = (NotificacaoMotivoNotificacao) iterator.next();
				params.put("lista", notificacaoMotivoNotificacao);
				sql = new StringBuilder();
				sql.append("delete from MotivoNotificacaoImovel mni1 ");
				sql.append("where mni1.ideMotivoNotificacaoImovel in (");
				sql.append("	select mni2.ideMotivoNotificacaoImovel ");
				sql.append("	from MotivoNotificacaoImovel mni2 ");
				sql.append("	where  mni2.ideNotificacaoMotivoNotificacao in :lista) ");
				notificaMotivoNotificacaoDAO.executarQuery(sql.toString(), params);
				
				/*
				 * sql = new StringBuilder(); sql.
				 * append("delete from NotificacaoMotivoNotificacao nmn  where nmn in :lista");
				 * notificaMotivoNotificacaoDAO.executarQuery(sql.toString(), params);
				 */
				
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerNotificacaoImovelMotivo(Notificacao notificacao) throws Exception {

		Collection<NotificacaoMotivoNotificacao> resultNotificaMotivoNotificacao = listarNotificacaoMotivoNotificacaoComShapeParaRemover(notificacao);
		if(!Util.isNullOuVazio(resultNotificaMotivoNotificacao)) {
			for(NotificacaoMotivoNotificacao nmn : resultNotificaMotivoNotificacao) {
				StringBuilder sqlDeleteNotificacaoImovel = new StringBuilder();
				sqlDeleteNotificacaoImovel.append("DELETE FROM motivo_notificacao_imovel WHERE ide_motivo_notificacao_imovel = :lista");
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("lista", nmn.getIdeNotificacaoMotivoNotificacao());
				motivoNotificacaoImovelDAO.executarNativeQuery(sqlDeleteNotificacaoImovel.toString(), params);
			}
		}

		String deleteSQL = "DELETE FROM notificacao_motivo_notificacao WHERE ide_notificacao = :ideNotificacao";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideNotificacao", notificacao.getIdeNotificacao());
		notificaMotivoNotificacaoDAO.executarNativeQuery(deleteSQL, params);	
	}
}
