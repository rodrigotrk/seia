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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the fce_asv database table.
 * 
 */
@Entity
@Table(name="fce_asv")
public class FceAsv implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_asv_ide_fce_asv_generator")
	@SequenceGenerator(name = "fce_asv_ide_fce_asv_generator", sequenceName = "fce_asv_ide_fce_asv_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_fce_asv")
	private Integer ideFceAsv;

	@NotNull
	@JoinColumn(name = "ide_fce", referencedColumnName = "ide_fce", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@Column(name="ind_app")
	private Boolean indApp;

	@Column(name="ind_material_lenhoso")
	private Boolean indMaterialLenhoso;

	@Column(name="num_area_app")
	private Double numAreaApp;

	@Column(name="num_area_supressao")
	private Double numAreaSupressao;

	@Column(name="num_area_suprimida")
	private Double numAreaSuprimida;

	@Column(name="ind_aceite")
	private Boolean indAceite;
	
	public FceAsv() {
		
	}
	
	public FceAsv(Fce fce) {
		this.ideFce = fce;
	}

	public Integer getIdeFceAsv() {
		return this.ideFceAsv;
	}

	public void setIdeFceAsv(Integer ideFceAsv) {
		this.ideFceAsv = ideFceAsv;
	}

	public Boolean getIndApp() {
		return this.indApp;
	}

	public void setIndApp(Boolean indApp) {
		this.indApp = indApp;
	}

	public Boolean getIndMaterialLenhoso() {
		return this.indMaterialLenhoso;
	}

	public void setIndMaterialLenhoso(Boolean indMaterialLenhoso) {
		this.indMaterialLenhoso = indMaterialLenhoso;
	}

	public Double getNumAreaApp() {
		return numAreaApp;
	}

	public void setNumAreaApp(Double numAreaApp) {
		this.numAreaApp = numAreaApp;
	}

	public Double getNumAreaSupressao() {
		return numAreaSupressao;
	}

	public void setNumAreaSupressao(Double numAreaSupressao) {
		this.numAreaSupressao = numAreaSupressao;
	}

	public Double getNumAreaSuprimida() {
		return numAreaSuprimida;
	}

	public void setNumAreaSuprimida(Double numAreaSuprimida) {
		this.numAreaSuprimida = numAreaSuprimida;
	}

	public Boolean getIndAceite() {
		return indAceite;
	}

	public void setIndAceite(Boolean indAceite) {
		this.indAceite = indAceite;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceAsv == null) ? 0 : ideFceAsv.hashCode());
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
		FceAsv other = (FceAsv) obj;
		if (ideFceAsv == null) {
			if (other.ideFceAsv != null)
				return false;
		} else if (!ideFceAsv.equals(other.ideFceAsv))
			return false;
		return true;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

}