package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "enquadramento_finalidade_uso_agua")
@NamedQueries({ @NamedQuery(name = "enquadramentoFinalidadeUsoAgua.findAll", query = "SELECT e FROM EnquadramentoFinalidadeUsoAgua e") })
public class EnquadramentoFinalidadeUsoAgua implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@SequenceGenerator(name = "enquadramento_finalidade_uso_agua_seq", sequenceName = "enquadramento_finalidade_uso_agua_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "enquadramento_finalidade_uso_agua_seq")
	@Column(name = "ide_enquadramento_finalidade_uso_agua", nullable = false)
	private Integer ideEnquadramentoFinalidadeUsoAgua;
	
	@JoinColumn(name = "ide_tipo_finalidade_uso_agua", referencedColumnName = "ide_tipo_finalidade_uso_agua")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;
	
	@JoinColumn(name = "ide_enquadramento_ato_ambiental", referencedColumnName = "ide_enquadramento_ato_ambiental")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbiental;
	
	public EnquadramentoFinalidadeUsoAgua() {
	}
	
	public EnquadramentoFinalidadeUsoAgua(Integer ideEnquadramentoFinalidadeUsoAgua) {
		this.ideEnquadramentoFinalidadeUsoAgua = ideEnquadramentoFinalidadeUsoAgua;
	}
	
	public EnquadramentoFinalidadeUsoAgua(Integer ideEnquadramentoFinalidadeUsoAgua, TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua,
			EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbiental) {
		this.ideEnquadramentoFinalidadeUsoAgua = ideEnquadramentoFinalidadeUsoAgua;
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
		this.ideEnquadramentoAtoAmbiental = ideEnquadramentoAtoAmbiental;
	}
	
	public Integer getIdeEnquadramentoFinalidadeUsoAgua() {
		return ideEnquadramentoFinalidadeUsoAgua;
	}
	
	public void setIdeEnquadramentoFinalidadeUsoAgua(Integer ideEnquadramentoFinalidadeUsoAgua) {
		this.ideEnquadramentoFinalidadeUsoAgua = ideEnquadramentoFinalidadeUsoAgua;
	}
	
	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}
	
	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}
	
	public EnquadramentoAtoAmbiental getIdeEnquadramentoAtoAmbiental() {
		return ideEnquadramentoAtoAmbiental;
	}
	
	public void setIdeEnquadramentoAtoAmbiental(EnquadramentoAtoAmbiental ideEnquadramentoAtoAmbiental) {
		this.ideEnquadramentoAtoAmbiental = ideEnquadramentoAtoAmbiental;
	}
	
	@Override
	public String toString() {
		return "EnquadramentoFinalidadeUsoAgua [ideEnquadramentoFinalidadeUsoAgua=" + ideEnquadramentoFinalidadeUsoAgua + ", ideTipoFinalidadeUsoAgua="
				+ ideTipoFinalidadeUsoAgua + ", ideEnquadramentoAtoAmbiental=" + ideEnquadramentoAtoAmbiental + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideEnquadramentoAtoAmbiental == null) ? 0 : ideEnquadramentoAtoAmbiental.hashCode());
		result = prime * result + ((ideTipoFinalidadeUsoAgua == null) ? 0 : ideTipoFinalidadeUsoAgua.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		EnquadramentoFinalidadeUsoAgua other = (EnquadramentoFinalidadeUsoAgua) obj;
		if (ideEnquadramentoAtoAmbiental == null) {
			if (other.ideEnquadramentoAtoAmbiental != null) return false;
		} else if (!ideEnquadramentoAtoAmbiental.equals(other.ideEnquadramentoAtoAmbiental)) return false;
		if (ideTipoFinalidadeUsoAgua == null) {
			if (other.ideTipoFinalidadeUsoAgua != null) return false;
		} else if (!ideTipoFinalidadeUsoAgua.equals(other.ideTipoFinalidadeUsoAgua)) return false;
		return true;
	}
	
	@Override
	public EnquadramentoFinalidadeUsoAgua clone() throws CloneNotSupportedException {
		return (EnquadramentoFinalidadeUsoAgua) super.clone();
	}
}
