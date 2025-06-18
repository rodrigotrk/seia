package br.gov.ba.seia.service;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtividadeInexigivel;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtividadeInexigivelService {
	
	@Inject
	private IDAO<AtividadeInexigivel> atividadeInexigivelDAO;
	
	@Inject
	private AtividadeDAOImpl atividadeInexigivelImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtividadeInexigivel> buscarPorDescricaoTipoAtividade(AtividadeInexigivel atividade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtividadeInexigivel.class)
				.createAlias("tipoAtividadeInexigivel", "tipoAtividadeInexigivel");
		
		if(!Util.isNullOuVazio(atividade.getTipoAtividadeInexigivel()) && !Util.isNullOuVazio(atividade.getTipoAtividadeInexigivel().getIdeTipoAtividadeInexigivel())){
			criteria.add(Restrictions.eq("tipoAtividadeInexigivel.ideTipoAtividadeInexigivel", atividade.getTipoAtividadeInexigivel().getIdeTipoAtividadeInexigivel()));
		}
		
		if(!Util.isNullOuVazio(atividade.getNomAtividadeInexigivel())) {
			criteria.add(Restrictions.ilike("nomAtividadeInexigivel", atividade.getNomAtividadeInexigivel(), MatchMode.ANYWHERE));
		}
		
		criteria.add(Restrictions.eq("indAtivo", true));
		
		criteria.addOrder(Order.asc("nomAtividadeInexigivel"));
		
		return this.atividadeInexigivelDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtividadeInexigivel> filtrarAtividade(String textFilter) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtividadeInexigivel.class);
		criteria.add(Restrictions.ilike("nomAtividadeInexigivel","%" + textFilter + "%"));
		criteria.add(Restrictions.eq("indAtivo", true));
		criteria.addOrder(Order.asc("nomAtividadeInexigivel"));
		return atividadeInexigivelDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtividadeInexigivel buscarAtividadeInexigivelPorIde(Integer ideAtividadeInexigivel){
		DetachedCriteria criteria = DetachedCriteria.forClass(AtividadeInexigivel.class);
		criteria.add(Restrictions.eq("ideAtividadeInexigivel", ideAtividadeInexigivel));
		criteria.add(Restrictions.eq("indAtivo", true));
		
		return atividadeInexigivelDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AtividadeInexigivel buscarAtividadeInexigivelPorNome(String nomeAtividade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(AtividadeInexigivel.class);
		criteria.add(Restrictions.eq("nomAtividadeInexigivel", nomeAtividade));
		criteria.add(Restrictions.eq("indAtivo", true));
		
		List<AtividadeInexigivel> itens =  atividadeInexigivelDAO.listarPorCriteria(criteria, Order.asc("nomAtividadeInexigivel"));
		
		return (!Util.isNullOuVazio(itens)) ? itens.get(0) : null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(AtividadeInexigivel pAtividade) {
		pAtividade.setDtcCriacao(new Date());
		this.atividadeInexigivelImpl.salvar(pAtividade);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(AtividadeInexigivel pAtividade)  {
		this.atividadeInexigivelImpl.atualizar(pAtividade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(AtividadeInexigivel pAtividade)  {
		this.atividadeInexigivelImpl.excluir(pAtividade);
	}
	
}
