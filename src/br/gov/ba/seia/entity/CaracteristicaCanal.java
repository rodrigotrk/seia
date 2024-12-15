package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "caracteristica_canal")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "CaracteristicaCanal.selectByFceCanal", query = "SELECT fceCanal.caracteristicasCanal FROM FceCanal fceCanal WHERE fceCanal = :fceCanal")})
public class CaracteristicaCanal implements BaseEntity, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caracteristica_canal_seq")
	@SequenceGenerator(name = "caracteristica_canal_seq", sequenceName = "caracteristica_canal_seq", allocationSize = 1)
	@Column(name = "ide_caracteristica_canal")
	private Integer ideCaracteristicaCanal;
	
	@Column(name = "dsc_caracteristica_canal")
	private String dsCaracteristicaCanal;
	
	@Transient
	private boolean selecionado;
	
	public Integer getIdeCaracteristicaCanal() {
		return ideCaracteristicaCanal;
	}

	public void setIdeCaracteristicaCanal(Integer ideCaracteristicaCanal) {
		this.ideCaracteristicaCanal = ideCaracteristicaCanal;
	}

	public String getDsCaracteristicaCanal() {
		return dsCaracteristicaCanal;
	}

	public void setDsCaracteristicaCanal(String dsCaracteristicaCanal) {
		this.dsCaracteristicaCanal = dsCaracteristicaCanal;
	}

	@Override
	public Long getId() {
		return new Long(getIdeCaracteristicaCanal());
	}

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	@Override
	public boolean equals(Object obj) {
        if (!(obj instanceof CaracteristicaCanal)) {
            return false;
        }
        CaracteristicaCanal other = (CaracteristicaCanal) obj;
        if ((this.ideCaracteristicaCanal == null && other.ideCaracteristicaCanal != null) || (this.ideCaracteristicaCanal != null && !this.ideCaracteristicaCanal.equals(other.ideCaracteristicaCanal))) {
            return false;
        }
        return true;
	}
	
}
