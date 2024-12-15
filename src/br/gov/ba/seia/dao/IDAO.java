package br.gov.ba.seia.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.stat.Statistics;

import br.gov.ba.seia.dto.EnderecoDTO;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.enumerator.CrudOperationEnum;

public interface IDAO<T> {

	@SuppressWarnings("rawtypes")
	public Class recuperarPersisteClass();

	public void salvar(T objeto);
	
	public void salvarSemFlush(T objeto);
	
	public void persistir(T objeto, CrudOperationEnum crudOperationEnum);

	public void salvarOuAtualizar(T objeto);

	public T carregarLoad(Integer id);
	
	public T carregarGet(Integer id);
	
	public T carregarLoad(Serializable id);

	public T carregarGet(Serializable id);	
	
	public void atualizar(T objeto);

	public void remover(T objeto);

	public T buscarEntidadePorExemplo(final T exemplo);
	
	public T buscarPorQuery(String sql, Map<String, Object> parametros);
	
	public List<T> buscarPorNamedQuery(String namedQuery);
	
	public List<T> buscarPorNamedQuery(String namedQuery,Map<String, Object> parametros);
	
	public T buscarEntidadePorNamedQueryWithMaxResult(String namedQuery, Map<String, Object> parametros,Integer maxResult);
	
	public T obterPorNamedQuery(String namedQuery, Map<String, Object> parametros);
	
	public List<T> buscarPorNativeQuery(String sql, Map<String, Object> parametros);
	
	public T obterPorNativeQuery(String sql, Map<String, Object> parametros);
	
	public void executarNativeQuery(String sql, Map<String, Object> parametros);
	
	public String executarFuncaoNativeQuery(String sql, Map<String, Object> parametros);
	
	public void executarQuery(String sql, Map<String,Object> parametros);
	
	public List<T> listarTodos();
	
	public List<T> listarPorQuery(String sql, Map<String, Object> parametros);
	
	public List<T> listarPorQueryPorDemanda(String sql, int first, int pageSize, Map<String, Object> parametros);

	public List<T> listarPorExemplo(final T exemplo);

	public List<T> listarPorExemplo(T objeto, MatchMode matchMode);	
	
	public void  executarNamedQuery(String namedQuery, Map<String, Object> parametros, Object ... objects);	
	
	public Object executarCriteria(DetachedCriteria detachedCriteria);
	
	public List<T> listarPorCriteria(DetachedCriteria detachedCriteria, Order...order);
	
	public T buscarPorCriteria(DetachedCriteria detachedCriteria);

	public T buscarEntidadePorNamedQuery(String namedQuery, Map<String, Object> parametros);

	void salvarEmLote(List<T> pLista);
	
	public Statistics getStatistics();
	
	public List<T> listarPorCriteriaDemanda(DetachedCriteria detachedCriteria, Integer startPage, Integer maxPage);
	
	public Integer count(DetachedCriteria detachedCriteria);
	
	public Integer count(String sql, Map<String, Object> params);
	
	public BigInteger countNativeQuery(String sql);

	void mergeEmLote(List<T> pLista);
	
	void merge(T object);
	
	public List<T> listarClasseComNativeQuery(String sql, Class<T> classeT);
	
	public List<T> listarClasseComNativeQuery(String sql, Class<T> classeT, Map<String, Object> parametros);

	public T buscarPorCriteriaMaxResult(DetachedCriteria detachedCriteria);

	public Object sum(DetachedCriteria detachedCriteria, Projection projection);

	public BigDecimal obterBigDecimalPorNativeQuery(String sql, Map<String, Object> parametros);

	public Boolean isExiste(DetachedCriteria detachedCriteria);

	public Object mergeComRetorno(T object);
	
	public Session getSession();
	
	public void sessionClear();
	
	public void sessionFlush();
}