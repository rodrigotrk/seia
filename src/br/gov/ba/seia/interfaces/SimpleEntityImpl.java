/**
 * 
 */
package br.gov.ba.seia.interfaces;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.log4j.Level;

import br.gov.ba.seia.util.Log4jUtil;

/**
 * @author lesantos
 *
 */
public abstract class SimpleEntityImpl implements BaseEntity, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2661428919183935300L;
	
	@Transient
	private boolean selecionado;

	/**
	 * @return the selecionado
	 */
	public boolean isSelecionado() {
		return selecionado;
	}

	/**
	 * @param selecionado the selecionado to set
	 */
	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	
	@Override
	public Long getId() {
		//descobre o ID
		for(Field field : this.getClass().getDeclaredFields()){
			if(field.getAnnotation(Id.class) != null){
				try {
					return new Long((Integer) field.get(this));
				} catch (IllegalArgumentException e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				} catch (IllegalAccessException e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleEntityImpl other = (SimpleEntityImpl) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
}
