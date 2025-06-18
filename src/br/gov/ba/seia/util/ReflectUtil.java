package br.gov.ba.seia.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import br.gov.ba.seia.validators.ValidationUtil;

@SuppressWarnings("all")
public class ReflectUtil {
	static Logger log = Logger.getLogger(ReflectUtil.class);
	
	public static Object getMethodValueWithOutParam(Class clazz, Object objectValue, String methodName) throws Exception {
		return clazz.getDeclaredMethod(methodName).invoke(objectValue);
	}
	
	public static Method getSetterMethod(Object object, Field field) throws NoSuchMethodException {
		Method declaredMethod = null;
		try {
			declaredMethod = object.getClass().getDeclaredMethod( "set" + field.getName().replaceFirst( field.getName().substring( 0 , 1 ) , field.getName().substring( 0 , 1 ).toUpperCase() ), field.getType() );
		} catch (Exception e) {
			declaredMethod = object.getClass().getSuperclass().getDeclaredMethod( "set" + field.getName().replaceFirst( field.getName().substring( 0 , 1 ) , field.getName().substring( 0 , 1 ).toUpperCase() ), field.getType() );			
		}
		return declaredMethod;
	}
	
	public static Method getGetterMethod(Object object, String methodName) throws NoSuchMethodException {
		Method declaredMethod;
		try {			
			declaredMethod = object.getClass().getDeclaredMethod( "get" + methodName.replaceFirst( methodName.substring( 0 , 1 ) , methodName.substring( 0 , 1 ).toUpperCase() ) );
		} catch (Exception e) {
			declaredMethod = object.getClass().getSuperclass().getDeclaredMethod( "get" + methodName.replaceFirst( methodName.substring( 0 , 1 ) , methodName.substring( 0 , 1 ).toUpperCase() ) );			
		}
		return declaredMethod;
	}
	
	/**
	 * Get all attributes names of a class
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getAttributes(Class clazz, List<String> ignoreAttributes) {
		List<String> attributes = new ArrayList<String>();
		
		for(Field field : clazz.getDeclaredFields()) {
			String attribute = field.getName();
            String typeName  = field.getType().getName();
			
            if(!"java.lang.Class".equals(typeName)) {
                if(ignoreAttributes != null) {
                    if(!ignoreAttributes.contains(attribute)) {
                        attributes.add(attribute);
                    }

                } else {
                    attributes.add(attribute);
                }
            }
		}
		
		return attributes;
	}
	
	@SuppressWarnings("unchecked")
	public static List<String> getAttributesInclude(Class clazz, List<String> includeAttributes) {
		List<String> attributes = new ArrayList<String>();
		
		for( Field field : clazz.getDeclaredFields() ) {
			String attribute = field.getName();
			
			if( includeAttributes != null) {
				if( includeAttributes.contains( attribute ) ) {
					attributes.add(attribute);
				}
			} else {
				attributes.add(attribute);
			}
		}
		
		return attributes;
		
	}
	
	@SuppressWarnings("unchecked")
	public static Object getAttributeValue(Object object, String partialAttribute){
		
		if(object == null)
			return null;
		
		if( !ValidationUtil.isEmpty(partialAttribute) ) {
			String[] arr = partialAttribute.split("\\.");
			Field field;
			
			if(arr.length > 1){
				try {
					field = getField(object, arr[0]);
					
				} catch (NoSuchFieldException e) {
					log.debug("Atributo "+arr[0]+" não encontrado na classe " + object.getClass(), e);
					return null;
				}
			
				field.setAccessible(true);
				
				try {
					Object obj = field.get(object);
					String newPartialAttribute = partialAttribute.substring(partialAttribute.indexOf(".") + 1);
					
					Class interfaces[] = field.getType().getInterfaces();
					for (int i = 0; i < interfaces.length;) {
						if (interfaces[i].getName().equals("java.util.Collection")) {
							Collection<Object> list = (Collection<Object>)obj;
							String result = null;
							for (Object actual : list) {
								if(!ValidationUtil.isEmpty(result))
									result += "," + getAttributeValue(actual, newPartialAttribute).toString();
								else
									result = getAttributeValue(actual, newPartialAttribute).toString();
							}
							return result;
						} 
						i++;
					}
					return getAttributeValue(obj, newPartialAttribute);
				}catch (Exception e) {
					return null;
				}
			}else{
				try {
					field = getField(object, partialAttribute);
				} catch (NoSuchFieldException e) {
					log.debug("Atributo "+partialAttribute+" não encontrado na classe " + object.getClass(), e);
					return null;
				}			
				
				field.setAccessible(true);
				
				try {
					Object obj = field.get(object);
					return obj;
				}catch (Exception e) {
					log.debug("Não foi possível pegar o valor do atributo "+partialAttribute+" da classe " + object.getClass(), e);
					return null;
				}
			}
		}
		return null;
	}	

	public static Object setAttributeValueInObject(Object object, String partialAttribute, Object value){
		
		if(object == null)
			return object;
		
		if( !ValidationUtil.isEmpty(partialAttribute) ) {
			String[] arr = partialAttribute.split("\\.");
			Field field;
			
			if(arr.length > 1) {
				//se o objeto for uma collection retorna sem fazer nada
				if ( fieldIsCollection(object, arr[0]) )
					return object;
				
				try {
					field = getField(object, arr[0]);
				} catch (NoSuchFieldException e) {
					log.debug("Atributo "+arr[0]+" não encontrado na classe " + object.getClass(), e);
					return object;
				}
							
				field.setAccessible(true);
				
				try {
					//pega o nested objeto via metodo getter
					Method method = getGetterMethod(object, arr[0]);
					Object obj = method.invoke(object);
										
					//se o mesmo estiver null instancia ele
					if(obj == null)
						obj = Class.forName( field.getType().getName() ).newInstance();
					
					//Entrado no nested objeto para setar o valor no atributo do mesmo
					String newPartialAttribute = partialAttribute.substring(partialAttribute.indexOf(".") + 1);
					setAttributeValueInObject(obj, newPartialAttribute, value);
					
					//setando o nested object no objeto principal
					Method setterMethod = getSetterMethod(object, field);
					setterMethod.invoke(object, obj);
					
					return object;
				}catch (Exception e) {
					log.debug(e);
					return object;
				}
			}else{
				//se o objeto for uma collection retorna sem fazer nada
				if ( fieldIsCollection(object, partialAttribute) )
					return object;
				
				try {
					field = getField(object, partialAttribute);
				} catch (NoSuchFieldException e) {
					log.debug("Atributo "+partialAttribute+" não encontrado na classe " + object.getClass(), e);
					return object;
				}
								
				try {
					//seta o valor via o metodo setter
					Method method = getSetterMethod(object, field);
					method.invoke(object, value);
					return object;
				}catch (Exception e) {
					log.debug("Não foi possível settar o valor no atributo "+partialAttribute+" da classe " + object.getClass(), e);
					return object;
				}
			}
		}
		return object;
	}	
	
	public static Field getField(Object object, String attribute) throws NoSuchFieldException {
		Field field;
		
		try{
			//Tenta pegar o field na classe instanciada
			field = object.getClass().getDeclaredField( attribute );
		} catch (NoSuchFieldException e) {
			//Tenta pegar o field na classe Pai, caso o mesmo seja herdado
			field = object.getClass().getSuperclass().getDeclaredField( attribute );
		}
		
		return field;
	}
	
	/**
	 * Verifica se o atributo do objeto � uma cole��o
	 * Pega a primeira parte do attributo, ou seja, tudo antes do primeiro ponto
	 * 
	 * @param object
	 * @param attribute
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean fieldIsCollection(Object object, String attribute) {
		Field field;
		String[] arr = attribute.split("\\.");
		
		try{
			
			//Tenta pegar o field na classe instanciada
			field = object.getClass().getDeclaredField( arr[0] );
		} catch (NoSuchFieldException e) {
			//Tenta pegar o field na classe Pai, caso o mesmo seja herdado
			try{
				field = object.getClass().getSuperclass().getDeclaredField( arr[0] );
			} catch (NoSuchFieldException er) {
				log.debug(er);
				return false;
			}
		}
		
		Class interfaces[] = field.getType().getInterfaces();
		boolean isCollection = false;
		
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i].getName().equals("java.util.Collection")) {
				isCollection = true;
				break;
			}
		}
		
		return isCollection;		
	}
}
