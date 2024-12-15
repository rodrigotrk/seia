package br.gov.ba.seia.util;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.ResultTransformer;
/**
 * Classe util
 * @author 
 *
 */
@SuppressWarnings({"all"})
public class AliasToNestedBeanResultTransformer implements ResultTransformer {
	
	private final Class resultClass;
	
	public AliasToNestedBeanResultTransformer(Class resultClass) {
		if(resultClass == null) throw new IllegalArgumentException("resultClass cannot be null");
		this.resultClass = resultClass; 	
	}

	public Object transformTuple(Object[] tuple, String[] aliases) {
		Object result;
		
		try {
			result = resultClass.newInstance();
			
			for (int i = 0; i < aliases.length; i++) {
				if(tuple[i] != null)
					result = ReflectUtil.setAttributeValueInObject(result, aliases[i], tuple[i]);
			}
			
		} catch (InstantiationException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());			
		} catch (IllegalAccessException e) {
			throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
		}		
		
		return result;
	}	
	
	public List transformList(List collection) {
		return collection;
	}
}