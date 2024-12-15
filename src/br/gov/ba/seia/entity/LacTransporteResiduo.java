package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 
 * @author eduardo.fernandes
 *
 */
@Entity
@Table(name = "lac_transporte_residuo")
@XmlRootElement
@NamedQueries({
@NamedQuery(name = "LacTransporteResiduo.findByIdeLacTransporte", query = "SELECT l FROM LacTransporteResiduo l WHERE l.ideLacTransporte = :ideLacTransporte"),
@NamedQuery(name = "LacTransporteResiduo.findByIdeResiduo", query = "SELECT l FROM LacTransporteResiduo l WHERE l.ideResiduo = :ideResiduo")})

public class LacTransporteResiduo implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected LacTransporteResiduoPK lacTransporteResiduoPK;
	
	@NotNull
	@JoinColumn(name = "ide_lac_transporte", referencedColumnName = "ide_lac_transporte", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private LacTransporte ideLacTransporte;

	@NotNull
	@JoinColumn(name = "ide_residuo", referencedColumnName = "ide_residuo", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Residuo ideResiduo;

	@Column(name = "qtd_media_transporte_anual",precision = 10, scale = 2)
	private Double qtdMediaTransporteAnual;
	
	public LacTransporteResiduo() {

	}

	public LacTransporteResiduo(LacTransporteResiduoPK lacTransporteResiduoPK) {
		this.lacTransporteResiduoPK = lacTransporteResiduoPK;
	}

	public LacTransporteResiduo(int ideLacTransporte, int ideResiduo) {
		this.lacTransporteResiduoPK = new LacTransporteResiduoPK(ideLacTransporte, ideResiduo);
	}

	public LacTransporteResiduoPK getLacTransporteResiduoPK() {
		return lacTransporteResiduoPK;
	}

	public void setLacTransporteResiduoPK(LacTransporteResiduoPK lacTransporteResiduoPK) {
		this.lacTransporteResiduoPK = lacTransporteResiduoPK;
	}
	
	public LacTransporte getIdeLacTransporte() {
		return ideLacTransporte;
	}

	public void setIdeLacTransporte(LacTransporte ideLacTransporte) {
		this.ideLacTransporte = ideLacTransporte;
	}

	public Residuo getIdeResiduo() {
		return ideResiduo;
	}

	public void setIdeResiduo(Residuo ideResiduo) {
		this.ideResiduo = ideResiduo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lacTransporteResiduoPK == null) ? 0 : lacTransporteResiduoPK.hashCode());
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
		LacTransporteResiduo other = (LacTransporteResiduo) obj;
		if (lacTransporteResiduoPK == null) {
			if (other.lacTransporteResiduoPK != null)
				return false;
		} else if (!lacTransporteResiduoPK.equals(other.lacTransporteResiduoPK))
			return false;
		return true;
	}

	public Double getQtdMediaTransporteAnual() {
		return qtdMediaTransporteAnual;
	}

	public void setQtdMediaTransporteAnual(Double qtdMediaTransporteAnual) {
		this.qtdMediaTransporteAnual = qtdMediaTransporteAnual;
	}
	
}