package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table ( name = "tipo_rio")
@XmlRootElement
public class TipoRio implements BaseEntity, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_rio_seq")
	@SequenceGenerator(name = "tipo_rio_seq", sequenceName = "tipo_rio_seq", allocationSize = 1)
	@Column( name = "ide_tipo_rio")
	private Integer ideTipoRio;
	
	@Column ( name = "dsc_tipo_rio")
	private String dsTipoRio;
	
	public TipoRio() {
		
	}

	public TipoRio(Integer ideTipoRio) {
		this.ideTipoRio = ideTipoRio;
	}
	
	public Integer getIdeTipoRio() {
		return ideTipoRio;
	}

	public void setIdeTipoRio(Integer ideTipoRio) {
		this.ideTipoRio = ideTipoRio;
	}

	public String getDsTipoRio() {
		return dsTipoRio;
	}

	public void setDsTipoRio(String dsTipoRio) {
		this.dsTipoRio = dsTipoRio;
	}

	@Override
	public Long getId() {
		return new Long(getIdeTipoRio());
	}

	@Override
	public boolean equals(Object object) {
        if (!(object instanceof TipoRio)) {
            return false;
        }
        TipoRio other = (TipoRio) object;
        if ((this.ideTipoRio == null && other.ideTipoRio != null) || (this.ideTipoRio != null && !this.ideTipoRio.equals(other.ideTipoRio))) {
            return false;
        }
        return true;
    }
	
}
