package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "area_municipio")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AreaMunicipio.findAll", query = "SELECT a FROM AreaMunicipio a"),
    @NamedQuery(name = "AreaMunicipio.findByIdeArea", query = "SELECT a FROM AreaMunicipio a WHERE a.areaMunicipioPK.ideArea = :ideArea"),
    @NamedQuery(name = "AreaMunicipio.findByIdeMunicipio", query = "SELECT a FROM AreaMunicipio a WHERE a.areaMunicipioPK.ideMunicipio = :ideMunicipio"),
    @NamedQuery(name = "AreaMunicipio.findByIdeTipoAreaMunicipio", query = "SELECT a FROM AreaMunicipio a WHERE a.tipoAreaMunicipio.ideTipoAreaMunicipio = :ideTipoAreaMunicipio")})
public class AreaMunicipio implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected AreaMunicipioPK areaMunicipioPK;
	@JoinColumn(name = "ide_area", referencedColumnName = "ide_area", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Area area;
	@JoinColumn(name = "ide_municipio", referencedColumnName = "ide_municipio", nullable = false, insertable = false, updatable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Municipio municipio;
	@JoinColumn(name = "ide_tipo_area_municipio", referencedColumnName = "ide_tipo_area_municipio")
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private TipoAreaMunicipio tipoAreaMunicipio;

	public AreaMunicipioPK getAreaMunicipioPK() {
		return areaMunicipioPK;
	}

	public void setAreaMunicipioPK(AreaMunicipioPK areaMunicipioPK) {
		this.areaMunicipioPK = areaMunicipioPK;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public TipoAreaMunicipio getTipoAreaMunicipio() {
		return tipoAreaMunicipio;
	}

	public void setTipoAreaMunicipio(TipoAreaMunicipio tipoAreaMunicipio) {
		this.tipoAreaMunicipio = tipoAreaMunicipio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((areaMunicipioPK == null) ? 0 : areaMunicipioPK.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AreaMunicipio other = (AreaMunicipio) obj;
		if (areaMunicipioPK == null) {
			if (other.areaMunicipioPK != null)
				return false;
		} else if (!areaMunicipioPK.equals(other.areaMunicipioPK))
			return false;
		return true;
	}
	
	

}
