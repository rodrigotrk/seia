package br.gov.ba.seia.entity.generic;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.log4j.Level;

import br.gov.ba.seia.enumerator.TelaAcaoEnum;
import br.gov.ba.seia.util.Log4jUtil;

/**
 * Hieraquia
 *
 * [AbstractEntity]
 *
 * Classe criada para padronizar as entidades persistiveis.
 *
 * É mãe de:
 * -> [AbstractEntityHist]
 * -> [AbstractEntityLocalizacaoGeografica]
 **/
public class AbstractEntity implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;

	@Transient
	private boolean visualizar;

	@Transient
	private boolean editar;

	@Transient
	private boolean value;

	@Transient
	private TelaAcaoEnum acaoTelaEnum;

	public AbstractEntity() {
	}

	public Integer getId(){

		for (Field f: this.getClass().getDeclaredFields()) {
			f.setAccessible(true);

			if(f.isAnnotationPresent(Id.class)){
				try {
					return (Integer) f.get(this);
				} catch (IllegalArgumentException e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				} catch (IllegalAccessException e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}

		}

		return null;
	}

	public void setId(Integer id){

		for (Field f: this.getClass().getDeclaredFields()) {
			f.setAccessible(true);

			if(f.isAnnotationPresent(Id.class)){
				try {
					 f.set(this, id);
				} catch (IllegalArgumentException e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				} catch (IllegalAccessException e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}

		}
	}

	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean visualizar) {
		this.visualizar = visualizar;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	public TelaAcaoEnum getAcaoTelaEnum() {
		return acaoTelaEnum;
	}

	public void setAcaoTelaEnum(TelaAcaoEnum acaoTelaEnum) {
		this.acaoTelaEnum = acaoTelaEnum;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
    	
    	if(object == null) {
    		return false;
    	}

        if (!(object instanceof AbstractEntity)) {
            return false;
        }

        AbstractEntity other = (AbstractEntity) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }

	@Override
	public AbstractEntity clone() throws CloneNotSupportedException {
		return (AbstractEntity) super.clone();
	}
}
