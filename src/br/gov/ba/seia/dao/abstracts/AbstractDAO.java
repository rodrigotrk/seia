package br.gov.ba.seia.dao.abstracts;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Id;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.CreateAliasUtil;
import br.gov.ba.seia.util.ProjectionUtil;

public abstract class AbstractDAO <T extends AbstractEntity> implements Serializable{
	private static final long serialVersionUID = 1L;

	protected abstract IDAO<T> getDAO();
	
	@SuppressWarnings("unchecked")
	public Class<?> clazz(){
	  return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	/**
	 * @author Alexandre Queiroz
	 * @see é preciso enviar o ID do Objeto, ou voce recebera uma return nulo 
	 * @param Qualquer classe que extenda de AbstractEntity
	 * */
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public T buscar(T t)  {
		
		if(t== null || t.getId() == null){
			return null;
		}
		
		DetachedCriteria criteria = null;

		criteria = createCriteria();
		criteria = alias(criteria);
		criteria = restrictions(criteria,t);
		
		return getDAO().buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public T buscar(Criterion criterio)  {
		DetachedCriteria criteria = null;
		criteria = createCriteria();
		criteria.add(criterio);
		criteria = alias(criteria);
		
		return getDAO().buscarPorCriteria(criteria);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<T> listar() {
		DetachedCriteria criteria = null;

		criteria = createCriteria();
		
		return getDAO().listarPorCriteria(criteria);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<T> listar(Criterion criterio) {
		DetachedCriteria criteria = null;

		criteria = createCriteria();
		
		criteria.add(criterio);
		criteria = alias(criteria);
		criteria = order(criteria);
		
		return getDAO().listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<T> listar(Criterion criterio,Class<?>... classe) {
		DetachedCriteria criteria = null;

		criteria = createCriteria(classe);
		
		criteria.add(criterio);
		criteria = alias(criteria);
		criteria = order(criteria);
		
		return getDAO().listarPorCriteria(criteria);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<T> listar(Criterion criterio, Order order) {
		DetachedCriteria criteria = null;

		criteria = createCriteria();
		
		criteria.add(criterio);
		criteria = alias(criteria);
		criteria.addOrder(order);
		
		return getDAO().listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<T> listar( Order order) {
		DetachedCriteria criteria = null;
		criteria = createCriteria();
		criteria = alias(criteria);
		criteria.addOrder(order);
		
		return getDAO().listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(T t)   {
		if(t.getId() == null){
			getDAO().salvar(t);
		}else{
			getDAO().atualizar(t);
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<T> ts)  {
		getDAO().salvarEmLote(ts);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(T t) throws Exception{
		
		if(t instanceof AbstractEntity && t!= null && t.getId() != null){
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide"+ classeNome(), t.getId());
		
			String execute= 
				" DELETE FROM " + classeNome()  + 
				" WHERE " + "ide" +classeNome()+ 
				"  = :ide";
			
			getDAO().executarQuery(execute, params);
		}else{
			throw new Exception("Objeto Invalido");
		}
	}
	
	private DetachedCriteria order(DetachedCriteria criteria){
		
		for(Field f: clazz().getDeclaredFields()){
			f.setAccessible(true);
			if(f.getAnnotation(Id.class) != null){
				criteria.addOrder(Order.asc(f.getName()));
			}
		}
		
		return criteria;
	}
	
	private Projection projections() {
		return ProjectionUtil.add(clazz());
	}
	

	private Projection projections(Class<?>... classe) {
		return ProjectionUtil.add(clazz(), classe);
	}
	
	private String classeNome(){
		return clazz().getName();
	}
	
	private String claseNomeSemPackged(){
		return clazz().getName().replace("br.gov.ba.seia.entity.", "");
	}
	
	private  DetachedCriteria createCriteria(){
		return DetachedCriteria.forClass(clazz()).setProjection(projections()).setResultTransformer(new AliasToNestedBeanResultTransformer(clazz()));
	}
	
	private  DetachedCriteria createCriteria(Class<?>... classe){
		return DetachedCriteria.forClass(clazz()).setProjection(projections(classe)).setResultTransformer(new AliasToNestedBeanResultTransformer(clazz()));
	}

	private DetachedCriteria alias(DetachedCriteria detachedCriteria){
		return CreateAliasUtil.add(detachedCriteria, clazz());
	}
	
	private DetachedCriteria restrictions(DetachedCriteria restrictions, T t){
		if(t.getId()!=null){
			restrictions
				.add(Restrictions.eq("ide"+claseNomeSemPackged(), t.getId()));
		}
		return restrictions;
	}
}
