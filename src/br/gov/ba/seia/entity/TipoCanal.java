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
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@XmlRootElement
@Table( name = "tipo_canal")
@NamedQueries({ @NamedQuery(name = "TipoCanal.findAll", query = "SELECT t FROM TipoCanal t")})
public class TipoCanal implements BaseEntity, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="tipo_canal_seq")    
	@SequenceGenerator(name="tipo_canal_seq", sequenceName="tipo_canal_seq", allocationSize=1)
	@Column(name = "ide_tipo_canal")
	private Integer ideTipoCanal;
	
	@Column(name = "dsc_tipo_canal" )
	private String dsTipoCanal;
	
	public Integer getIdeTipoCanal() {
		return ideTipoCanal;
	}

	public void setIdeTipoCanal(Integer ideTipoCanal) {
		this.ideTipoCanal = ideTipoCanal;
	}

	public String getDsTipoCanal() {
		return dsTipoCanal;
	}

	public void setDsTipoCanal(String dsTipoCanal) {
		this.dsTipoCanal = dsTipoCanal;
	}

	@Override
	public Long getId() {
		return new Long(ideTipoCanal);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TipoCanal)) {
			return false;
		}
		TipoCanal other = (TipoCanal) object;
		if ((this.ideTipoCanal == null && other.ideTipoCanal != null)
				|| (this.ideTipoCanal != null && !this.ideTipoCanal.equals(other.ideTipoCanal))) {
			return false;
		}
		return true;
	}
	
}
