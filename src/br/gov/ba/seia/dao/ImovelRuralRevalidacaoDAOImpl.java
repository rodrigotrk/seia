package br.gov.ba.seia.dao;


import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralRevalidacao;

public class ImovelRuralRevalidacaoDAOImpl {

	@Inject
	IDAO<ImovelRuralRevalidacao> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralRevalidacao filtrarById(ImovelRuralRevalidacao imovelRuralRevalidacao) {
		return dao.buscarEntidadePorExemplo(imovelRuralRevalidacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(ImovelRuralRevalidacao imovelRuralRevalidacao)  {
		dao.salvarOuAtualizar(imovelRuralRevalidacao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ImovelRuralRevalidacao> listarImovelRuralRevalidacaoByImovelRural(ImovelRural imovelRural) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ImovelRuralRevalidacao.class, "imovelRuralRevalidacao");
		criteria.createAlias("ideSecaoRevalidacao", "ideSecaoRevalidacao", JoinType.INNER_JOIN);
		criteria.createAlias("ideLocalizacaoGeografica", "localizacaoGeografica", JoinType.INNER_JOIN);
		criteria.add(Restrictions.eq("ideImovelRural", imovelRural));
		criteria.add(Restrictions.eq("indValidado", false));
		criteria.addOrder(Order.asc("ideImovelRuralRevalidacao"));
		return dao.listarPorCriteria(criteria);
		
	}
	
}
