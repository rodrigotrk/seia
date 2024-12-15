package br.gov.ba.seia.util;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.log4j.Level;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

/**
 * @author alexandre queiroz
 * @since 25/01/2018
 * 
 **/

public class ProjectionUtil {
	private final static String serialVersionUID = "serialVersionUID";
	
	
	/*Calma, vamos chegar nessa =| 
	 * 
	 * */
	public static ProjectionList add(Class<?> c, Class<?>... classe) {
		ProjectionList projectionList = Projections.projectionList();
		String alias = c.getSimpleName();
		
		try {
			forSuperClasse(c, projectionList);
			forClasse(c, projectionList);
			
			for (Class<?> c1 : classe) {
				forClasseDerivadas(c1, projectionList,alias);
			}
			
		} catch (IllegalArgumentException e) {
			Log4jUtil.log(ProjectionUtil.class.getName(),Level.ERROR, e);
		}
		
		return projectionList;
	}
	
	
	
 	public static ProjectionList add(Class<?> c) {
		ProjectionList projectionList = Projections.projectionList();
		try {
			forSuperClasse(c, projectionList);
			forClasse(c, projectionList);
		} catch (IllegalArgumentException e) {
			Log4jUtil.log(ProjectionUtil.class.getName(),Level.ERROR, e);
		}
	
		return projectionList;
	}

	private static void forClasse(Class<?> c, ProjectionList projectionList) {
		String alias;
		for(Field f: c.getDeclaredFields()){
			f.setAccessible(true);
			
			
			if(f.getName().equals("ideCerhCadastroCancelado")){
				f.getName();
			}
			
			alias = "";
			if(isValidFied(f)){
				if(!isEntidade(f)){
					alias = f.getName();
				}
				else{
					Field id = getCampoChave(f.getType());
					alias= f.getName() +"."+ id.getName();
				}

				projectionList.add(Projections.property(alias),alias);
			}
						
		}
	}

	private static void forClasseDerivadas(Class<?> c, ProjectionList projectionList, String alias) {
		String subAlias;
		for(Field f: c.getDeclaredFields()){
			f.setAccessible(true);
			
			subAlias = alias;
			if(isValidFied(f)){
				if(!isEntidade(f)){
					subAlias = f.getName();
				}
				else{
					Field id = getCampoChave(f.getType());
					subAlias= f.getName() +"."+ id.getName();
				}

				projectionList.add(Projections.property(subAlias),subAlias);
			}
						
		}
	}

	
	


	private static void forSuperClasse(Class<?> c, ProjectionList projectionList) {
		String alias;
		for(Field f: c.getSuperclass().getDeclaredFields()){
			f.setAccessible(true);
			
			alias = "";
			if(isValidFied(f)){
				if(!isEntidade(f)){
					alias = f.getName();
				}
				else{
					Field id = getCampoChave(f.getType());
					alias= f.getName() +"."+ id.getName();
				}

				projectionList.add(Projections.property(alias),alias);
			}
		}
	}

	private static Field getCampoChave(@SuppressWarnings("rawtypes") Class clazz){
		
		for(Field f: clazz.getDeclaredFields()){
			if(f.getAnnotation(Id.class)!=null){
				return f;
			}
		}
		
		return null;
	}
	
	private static boolean isEntidade(Field f){
		return f.getType().toString().startsWith("class br.gov.ba.seia.entity.");
	}
	
	private static boolean isValidFied(Field f){

		if (serialVersionUID.equals(f.getName())){
			return false;
		}
		
		else if (f.getType() == List.class){
			return false;
		}
		
		else if (f.getType() == Collection.class){
			return false;
		}
		
		else if (f.getAnnotation(Transient.class)!=null){
			return false;
		}
		return true;
	}
}
