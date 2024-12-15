package br.gov.ba.seia.dao;

import java.util.List;


import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.HistSituacaoDae;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.util.Util;

public class CerhHistSituacaoDaeDAOImpl {

	@Inject
	IDAO<HistSituacaoDae> cerhHistSituacaoDaeDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarHistSituacaoDae(HistSituacaoDae cerhHistSituacaoDae) {
		cerhHistSituacaoDaeDAO.salvar(cerhHistSituacaoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarHistSituacaoDae(HistSituacaoDae cerhHistSituacaoDae){
		cerhHistSituacaoDaeDAO.atualizar(cerhHistSituacaoDae);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<HistSituacaoDae> listarHistSituacaoDae() {
		return cerhHistSituacaoDaeDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public HistSituacaoDae obterHistSituacaoDae(Dae ideDae, Requerimento req) {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" Select usu.idePessoaFisicaUsuario from Usuario usu ");
		hql.append(" where usu.idePessoaFisica = :idePessoa             ");
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		
		Query lQuery = lEntityManager.createQuery(hql.toString());
		lQuery.setParameter("idePessoa", req.getRequerente().getIdePessoa());
		
		DetachedCriteria criteria = DetachedCriteria.forClass(HistSituacaoDae.class);
		
		criteria.createAlias("ideSituacaoDae", "sit", JoinType.INNER_JOIN);
		criteria.createAlias("ideDae", "dae", JoinType.INNER_JOIN);
		criteria.createAlias("ideUsuario", "usuario", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("dae.ideDae", ideDae.getIdeDae()));
		criteria.add(Restrictions.eq("usuario.idePessoaFisicaUsuario", (Integer)lQuery.getSingleResult()));
		
		criteria.addOrder(Order.desc("ideHistSituacaoDae"));
		
		List<HistSituacaoDae> lista = cerhHistSituacaoDaeDAO.listarPorCriteria(criteria);
		if (!Util.isNullOuVazio(lista)) {
			return lista.get(0);
		}
        return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public HistSituacaoDae obterUltimoHistSituacaoDae(Dae ideDae) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HistSituacaoDae.class);
		
		criteria.createAlias("ideSituacaoDae", "sit", JoinType.INNER_JOIN);
		criteria.createAlias("ideDae", "dae", JoinType.INNER_JOIN);
		
		criteria.add(Restrictions.eq("dae.ideDae", ideDae.getIdeDae()));
		
		criteria.addOrder(Order.desc("ideHistSituacaoDae"));
		
		List<HistSituacaoDae> lista = cerhHistSituacaoDaeDAO.listarPorCriteria(criteria);
		if (!Util.isNullOuVazio(lista)) {
			return lista.get(0);
		}
        return null;

	}
}
