package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table( name = "fce_canal_municipio")
@XmlRootElement
public class FceCanalMunicipio implements Serializable, BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_canal_municipio_seq")
	@SequenceGenerator(name = "fce_canal_municipio_seq", sequenceName = "fce_canal_municipio_seq", allocationSize = 1)
	@Column( name = "ide_canal_municipio")
	private Integer ideCanalMunicipio;
	
	@JoinColumn(name = "ide_fce_canal", referencedColumnName = "ide_fce_canal", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private FceCanal fceCanal;
	
	@JoinColumn(name = "ide_municipio", referencedColumnName = "ide_municipio", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Municipio municipio;

	public Integer getIdeCanalMunicipio() {
		return ideCanalMunicipio;
	}

	public void setIdeCanalMunicipio(Integer ideCanalMunicipio) {
		this.ideCanalMunicipio = ideCanalMunicipio;
	}

	public FceCanal getFceCanal() {
		return fceCanal;
	}

	public void setFceCanal(FceCanal fceCanal) {
		this.fceCanal = fceCanal;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	@Override
	public Long getId() {
		return new Long(getIdeCanalMunicipio());
	}

}
