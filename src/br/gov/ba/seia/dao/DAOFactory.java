package br.gov.ba.seia.dao;

import java.lang.reflect.ParameterizedType;

import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class DAOFactory{    
   
	@PersistenceContext(unitName="seiapu")    
    static EntityManager entityManagerSeia;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Produces
    public DAOGeneric createDAO(InjectionPoint injectionPoint) {
        ParameterizedType type = (ParameterizedType) injectionPoint.getType();
        Class classe = (Class) type.getActualTypeArguments()[0];
        return new DAOGeneric(classe, entityManagerSeia);    
    }
	
	public static EntityManager getEntityManager() {
		return entityManagerSeia;
	}
}
