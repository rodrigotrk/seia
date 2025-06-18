package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.AtividadeInexigivel;
import br.gov.ba.seia.entity.RecomendacaoAtividadeInexigivel;
import br.gov.ba.seia.entity.RecomendacaoInexigibilidade;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RecomendacaoAtividadeInexigivelService {

	@Inject
	private IDAO<RecomendacaoAtividadeInexigivel> recomendacaoAtividadeInexigivelDAO;
	
	@Inject
	private IDAO<RecomendacaoInexigibilidade> recomendacaoInexigibilidadeDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RecomendacaoAtividadeInexigivel> obterRecomendacaoPor(AtividadeInexigivel atividadeInexigivel) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RecomendacaoAtividadeInexigivel.class)
				.createAlias("atividadeInexigivel", "atividadeInexigivel")
				.add(Restrictions.eq("atividadeInexigivel.ideAtividadeInexigivel", atividadeInexigivel.getIdeAtividadeInexigivel()));
		
		List<RecomendacaoAtividadeInexigivel> lista = this.recomendacaoAtividadeInexigivelDAO.listarPorCriteria(detachedCriteria);
		
		if(!Util.isNullOuVazio(lista)){
			RecomendacaoInexigibilidade recomendacao = null;
			
			for (RecomendacaoAtividadeInexigivel recomendacaoAtividadeInexigivel : lista) {
				if(!Util.isNull(recomendacaoAtividadeInexigivel.getRecomendacaoInexigibilidade()) && 
						!Util.isNull(recomendacaoAtividadeInexigivel.getRecomendacaoInexigibilidade().getIdeRecomendacaoInexigibilidade())) {
					recomendacao = recomendacaoInexigibilidadeDAO.buscarEntidadePorExemplo(recomendacaoAtividadeInexigivel.getRecomendacaoInexigibilidade());
					recomendacaoAtividadeInexigivel.setRecomendacaoInexigibilidade(recomendacao);
				}
			}
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RecomendacaoInexigibilidade> filtrarRecomendacao(String textFilter)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(RecomendacaoInexigibilidade.class);
		criteria.add(Restrictions.ilike("desRecomendacaoInexigibilidade","%" + textFilter + "%"));
		criteria.add(Restrictions.eq("indAtivo", true));
		criteria.addOrder(Order.asc("desRecomendacaoInexigibilidade"));
		return recomendacaoInexigibilidadeDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RecomendacaoInexigibilidade> listarRecomendacoes()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(RecomendacaoInexigibilidade.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		criteria.addOrder(Order.asc("desRecomendacaoInexigibilidade"));
		return recomendacaoInexigibilidadeDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(RecomendacaoAtividadeInexigivel recomendacaoAtividadeInexigivel) {
		recomendacaoAtividadeInexigivelDAO.salvar(recomendacaoAtividadeInexigivel);		
	}
	
	
}
