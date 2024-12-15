package br.gov.ba.seia.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.stat.Statistics;

import br.gov.ba.seia.enumerator.CrudOperationEnum;
import br.gov.ba.seia.service.rules.impl.BusinessManager;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.auditoria.AuditoriaUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DAOGeneric<T> implements IDAO<T>, Serializable {

	private static final long serialVersionUID = -5786630429581694776L;

	private EntityManager entityManager;

	private Class persistentClass;
	private Session session;

	@Override
	public T carregarLoad(Integer id) {
		Session sessionLoad = getSession();
		return (T) sessionLoad.load(persistentClass, id);
	}

	@Override
	public T carregarGet(Integer id) {
		Session sessionGet = getSession();
		return (T) sessionGet.get(persistentClass, id);
	}

	@Override
	public T carregarLoad(Serializable id) {
		Session sessionCarregarLoad = getSession();
		return (T) sessionCarregarLoad.load(persistentClass, id);
	}

	@Override
	public T carregarGet(Serializable id) {
		Session sessionCarregarGet = getSession();
		return (T) sessionCarregarGet.get(persistentClass, id);
	}

	@Override
	public List<T> listarTodos() {
		Session sessionListar = getSession();
		Criteria criteria = sessionListar.createCriteria(persistentClass);
		return criteria.list();
	}

	@Override
	public List<T> listarPorExemplo(T objeto) {
		return listarPorExemplo(objeto, null);
	}

	@Override
	public List<T> listarPorExemplo(T objeto, MatchMode matchMode) {
		Session sessionLista = getSession();
		Example example = null;

		if (Util.isNull(matchMode)) {
			example = criaExemplo(objeto);
		} else {
			example = criaExemplo(objeto, matchMode);
		}

		Criteria criteria = sessionLista.createCriteria(objeto.getClass()).add(example);
		return criteria.list();
	}

	/**
	 * ELIMINAR METODO!
	 */
	@Override
	public T buscarEntidadePorExemplo(T objeto) {
		Session sessionBuscar = getSession();
		Example example = criaExemplo(objeto);
		Criteria criteria = sessionBuscar.createCriteria(objeto.getClass()).add(example);
		return (T) criteria.uniqueResult();
	}
	
	@Override
	public List<T> buscarPorNamedQuery(String namedQuery) {
		return buscarPorNamedQuery(namedQuery, null);
	}
	
	@Override
	public List<T> listarClasseComNativeQuery(String sql, Class<T> classeT) {
		entityManager.joinTransaction();
		Query lQuery = entityManager.createNativeQuery(sql, classeT);
		return lQuery.getResultList();
	}	

	@Override
	public List<T> buscarPorNamedQuery(String namedQuery, Map<String, Object> parametros) {
		if (Util.isNull(parametros)) {
			return getEntityManager().createNamedQuery(namedQuery).getResultList();
		} else {
			Query query = createQuery(namedQuery, parametros);
			try {
				return query.getResultList();
			} catch (NoResultException nre) {
				return null;
			}
		}
	}
	
	public T obterPorNamedQuery(String namedQuery, Map<String, Object> parametros) {
		if (Util.isNull(parametros)) {
			return (T) getEntityManager().createNamedQuery(namedQuery).getSingleResult();
		} else {
			Query query = createQuery(namedQuery, parametros);
			try {
				return (T) query.getSingleResult();
			} catch (NoResultException nre) {
				return null;
			}
		}
	}
	
	@Override
	public T buscarPorCriteria(DetachedCriteria detachedCriteria) {
		return (T) detachedCriteria.getExecutableCriteria(getSession()).uniqueResult();
	}
	
	@Override
	public List<T> listarPorCriteria(DetachedCriteria detachedCriteria, Order... order) {

		if (!Util.isNull(order)) {
			for (Order ord : order) {
				detachedCriteria.addOrder(ord);
			}
		}
		return detachedCriteria.getExecutableCriteria(getSession()).list();
	}

	@Override
	public List<T> buscarPorNativeQuery(String sql, Map<String, Object> parametros) {
		Query query = entityManager.createNativeQuery(sql);
		if (!Util.isNull(parametros) && !parametros.isEmpty()) {
			Iterator<String> it = parametros.keySet().iterator();
			while (it.hasNext()) {
				String parametroIdentifier = it.next();
				query.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
			}
		}
		return query.getResultList();
	}
	
	@Override
	public T obterPorNativeQuery(String sql, Map<String, Object> parametros) {
		Query query = entityManager.createNativeQuery(sql);
		if (!Util.isNull(parametros) && !parametros.isEmpty()) {
			Iterator<String> it = parametros.keySet().iterator();
			while (it.hasNext()) {
				String parametroIdentifier = it.next();
				query.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
			}
		}
		return (T) query.getSingleResult();
	}
	
	@Override
	public BigDecimal obterBigDecimalPorNativeQuery(String sql, Map<String, Object> parametros) {
		Query query = entityManager.createNativeQuery(sql);
		if (!Util.isNull(parametros) && !parametros.isEmpty()) {
			Iterator<String> it = parametros.keySet().iterator();
			while (it.hasNext()) {
				String parametroIdentifier = it.next();
				query.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
			}
		}
		Number singleResult = (Number) query.getSingleResult();
		return new BigDecimal(singleResult.toString());
	}
	
	@Override
	public List<T> listarPorQuery(String sql, Map<String, Object> parametros){
		Query query = entityManager.createQuery(sql);
		if (!Util.isNull(parametros) && !parametros.isEmpty()) {
			Iterator<String> it = parametros.keySet().iterator();
			while (it.hasNext()) {
				String parametroIdentifier = it.next();
				query.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
			}
		}
		return query.getResultList();
		
	}
	
	@Override
	public List<T> listarPorQueryPorDemanda(String sql, int first, int pageSize, Map<String, Object> parametros){
		Query query = entityManager.createQuery(sql);
		if (!Util.isNull(parametros) && !parametros.isEmpty()) {
			Iterator<String> it = parametros.keySet().iterator();
			while (it.hasNext()) {
				String parametroIdentifier = it.next();
				query.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
			}
		}
		return query.setFirstResult(first).setMaxResults(pageSize).getResultList();
		
	}
	
	@Override
	public List<T> listarClasseComNativeQuery(String sql, Class<T> classeT, Map<String, Object> parametros) {
		Query lQuery = entityManager.createNativeQuery(sql, classeT);
		if (!Util.isNull(parametros) && !parametros.isEmpty()) {
			Iterator<String> it = parametros.keySet().iterator();
			while (it.hasNext()) {
				String parametroIdentifier = it.next();
				lQuery.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
			}
		}
		return  lQuery.getResultList();
	}
	
	@Override
	public T buscarPorQuery(String sql, Map<String, Object> parametros){
		Query query = entityManager.createQuery(sql);
		if (!Util.isNull(parametros) && !parametros.isEmpty()) {
			Iterator<String> it = parametros.keySet().iterator();
			while (it.hasNext()) {
				String parametroIdentifier = it.next();
				query.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
			}
		}
		try{
			return (T) query.getSingleResult();			
		}
		catch(NoResultException e){
			return null;
		}
	}
	
	@Override
	public T buscarEntidadePorNamedQuery(String namedQuery, Map<String, Object> parametros) {
		Query query = createQuery(namedQuery, parametros);
		query.setMaxResults(1);
		try {
			return (T) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	@Override
	public T buscarEntidadePorNamedQueryWithMaxResult(String namedQuery, Map<String, Object> parametros,Integer maxResult) {
		Query query = createQuery(namedQuery, parametros);
		query.setMaxResults(maxResult);
		try {
			return  (T) query.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	public List<T> listarPorCriteriaDemanda(DetachedCriteria detachedCriteria, Integer startPage, Integer maxPage) {

		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setFirstResult(startPage);
		criteria.setMaxResults(maxPage);
		return criteria.list();
	}
	
	@Override
	public T buscarPorCriteriaMaxResult(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setMaxResults(1);
		criteria.uniqueResult();
		
		return (T) criteria.uniqueResult();		
	}

	public Integer count(DetachedCriteria detachedCriteria) {
		Integer size = 0;
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setProjection(Projections.rowCount());
		size = Integer.parseInt(criteria.uniqueResult().toString());
		return size;
	}
	
	public Integer countPagination(int firstPage, int pageSize, DetachedCriteria detachedCriteria) {
		
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());
		criteria.setProjection(Projections.rowCount());
		criteria.setFirstResult(firstPage);
		criteria.setMaxResults(pageSize);
		return Integer.parseInt(criteria.uniqueResult().toString());
		
	}

	@Override
	public BigInteger countNativeQuery(String sql) {
		Query query = entityManager.createNativeQuery(sql);
		return (BigInteger)query.getSingleResult();
	}
	
	@Override
	public Integer count(String sql, Map<String, Object> params) {
		Query query = entityManager.createQuery(sql);
		if(!Util.isNull(params) && !params.isEmpty()) {
			Iterator<String> it = params.keySet().iterator();
			while (it.hasNext()) {
				String parametroIdentifier = it.next();
				query.setParameter(parametroIdentifier, params.get(parametroIdentifier));
			}
		}
		return Integer.parseInt(query.getSingleResult().toString());
	}
	
	@Override
	public void atualizar(T objeto) {
		AuditoriaUtil.auditoria(objeto);
		getSession().update(objeto);
		getSession().flush();
	}
	
	@Override
	public void salvar(T objeto) {
		AuditoriaUtil.auditoria(objeto);
		getSession().save(objeto);
		getSession().flush();
	}
	
	@Override
	public void salvarSemFlush(T objeto) {
		AuditoriaUtil.auditoria(objeto);
		getSession().save(objeto);
	}

	@Override
	public void salvarEmLote(List<T> pLista) {
		for (T item : pLista) {
			AuditoriaUtil.auditoria(item);
			getSession().saveOrUpdate(item);
		}
		getSession().flush();
	}
	
	@Override
	public void salvarOuAtualizar(T objeto) {
		AuditoriaUtil.auditoria(objeto);
		getSession().saveOrUpdate(objeto);
		getSession().flush();
	}
	
	@Override
	public void persistir(T objeto, CrudOperationEnum crudOperationEnum) {
		BusinessManager bm = BusinessManager.loadBusinessRules(this.getSession(), objeto.getClass().getName());
		
		if (bm != null) {
			if (bm.prePersist(objeto, crudOperationEnum)) {
				doSave(crudOperationEnum, objeto);
				bm.postPersist(objeto, crudOperationEnum);
			}
		} else {
			doSave(crudOperationEnum, objeto);
		}
		
		getSession().flush();
	}
	
	@Override
	public void mergeEmLote(List<T> pLista) {
		for (T item : pLista) {
			AuditoriaUtil.auditoria(item);
			getSession().merge(item);
		}
		getSession().flush();
	}
	
	@Override
	public void merge(T object) {
		AuditoriaUtil.auditoria(object);
		entityManager.merge(object);
	}
	
	@Override
	public Object mergeComRetorno(T object) {
		AuditoriaUtil.auditoria(object);
		return entityManager.merge(object);
	}
	
	@Override
	public void remover(T objeto) {
		objeto = entityManager.merge(objeto);
		getSession().delete(objeto);
	}
	

	public DAOGeneric(Class<T> persistentClass, EntityManager entityManager) {
		this.persistentClass = persistentClass;
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public Statistics getStatistics() {
		SessionFactory sessionFactory = getSession().getSessionFactory();
		sessionFactory.getStatistics().setStatisticsEnabled(true);
		return sessionFactory.getStatistics();
	}

	public Session getSession() {
		if (Util.isNull(session) || !session.isOpen()) {

			if (entityManager.getDelegate() instanceof org.hibernate.ejb.HibernateEntityManager) {
				session = ((org.hibernate.ejb.HibernateEntityManager) entityManager.getDelegate()).getSession();
			} else {
				session = (org.hibernate.Session) entityManager.getDelegate();
			}
		}
		
		return session;
	}
	
	@Override
	public Class recuperarPersisteClass() {
		return persistentClass;
	}
	
	private void doSave(CrudOperationEnum crudOperation, T bean) {

		if (CrudOperationEnum.INSERT.equals(crudOperation)) {
			this.salvar(bean);
		} else if (CrudOperationEnum.UPDATE.equals(crudOperation)) {
			this.atualizar(bean);
		} else if (CrudOperationEnum.DELETE.equals(crudOperation)) {
			this.remover(bean);
		}

	}

	@Override
	public void executarNamedQuery(String namedQuery, Map<String, Object> parametros, Object ... objects) {
		if (Util.isNull(parametros)) {
			getEntityManager().createNamedQuery(namedQuery);
		} else {
			AuditoriaUtil.auditoria(objects, parametros);
			Query query = createQuery(namedQuery, parametros);
			query.executeUpdate();
		}
	}

	private Query createQuery(String namedQuery, Map<String, Object> parametros) {
		Iterator<String> it = parametros.keySet().iterator();
		Query query = entityManager.createNamedQuery(namedQuery);

		while (it.hasNext()) {
			String parametroIdentifier = it.next();
			query.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
		}
		return query;
	}

	protected final Example criaExemplo(T objeto, MatchMode matchMode) {
		Example example = Example.create(objeto);
		example.enableLike(matchMode);
		example.excludeZeroes();
		example.ignoreCase();
		return example;
	}

	@Override
	public Object executarCriteria(DetachedCriteria detachedCriteria) {
		return detachedCriteria.getExecutableCriteria(getSession()).uniqueResult();
	}

	@Override
	public void executarNativeQuery(String sql, Map<String, Object> parametros) {
		Iterator<String> it = parametros.keySet().iterator();
		Query query = entityManager.createNativeQuery(sql);

		while (it.hasNext()) {
			String parametroIdentifier = it.next();
			query.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
		}

		query.executeUpdate();
	}
	
	@Override
	public String executarFuncaoNativeQuery(String sql, Map<String, Object> parametros) {
		Iterator<String> it = null;
		Query query = entityManager.createNativeQuery(sql);
		if(parametros != null){
			it = parametros.keySet().iterator();
			while (it.hasNext()) {
				String parametroIdentifier = it.next();
				query.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
			}
		}

		return (String) query.getSingleResult();
	}

	@Override
	public void executarQuery(String sql, Map<String, Object> parametros) {
		Iterator<String> it = parametros.keySet().iterator();
		Query query = entityManager.createQuery(sql);

		while (it.hasNext()) {
			String parametroIdentifier = it.next();
			query.setParameter(parametroIdentifier, parametros.get(parametroIdentifier));
		}

		query.executeUpdate();
	}

	protected final Example criaExemplo(T objeto) {
		Example example = Example.create(objeto);
		example.enableLike(MatchMode.ANYWHERE);
		example.excludeZeroes();
		example.ignoreCase();
		return example;
	}

	protected final Criteria criaCriteria() {
		Session sessionCriar = getSession();
		return sessionCriar.createCriteria(recuperarPersisteClass());
	}

	public Object sum(DetachedCriteria detachedCriteria, Projection projection) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(getSession()).setProjection(projection);
		return criteria.uniqueResult();
	}
	
	public Boolean isExiste(DetachedCriteria detachedCriteria) {				
		Criteria criteria = (Criteria) detachedCriteria.getExecutableCriteria(getSession());
		criteria.setMaxResults(1);
		return !criteria.list().isEmpty(); 
	}
	
	public void sessionClear() {
		getSession().clear();
	}
	
	public void sessionFlush() {
		getSession().flush();
	}
}