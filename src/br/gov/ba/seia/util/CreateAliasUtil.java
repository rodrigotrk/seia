package br.gov.ba.seia.util;

import java.lang.reflect.Field;

import javax.persistence.OneToOne;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.sql.JoinType;

/**
 * @author alexandre queiroz
 * @since 26/07/2018
 * 
 **/

public class CreateAliasUtil {
	
	public static DetachedCriteria add(DetachedCriteria criteria, Class<?> c) {

		try {
			for(Field f: c.getDeclaredFields()){
				f.setAccessible(true);
				
				if (f.getAnnotation(OneToOne.class)!=null && !f.getAnnotation(OneToOne.class).mappedBy().equals("")){
					criteria.createAlias(f.getName(), f.getName(), JoinType.LEFT_OUTER_JOIN);
				}
							
			}
			
		} catch (IllegalArgumentException e) {
			Log4jUtil.log(CreateAliasUtil.class.getName(),Level.ERROR, e);
		}
		
		
		return criteria;
	}
	
}
