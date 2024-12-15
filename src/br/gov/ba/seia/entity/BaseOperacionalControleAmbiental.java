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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the base_operacional_controle_ambiental database table.
 * 
 */
@Entity
@Table(name="base_operacional_controle_ambiental")
@XmlRootElement
@NamedQueries({
@NamedQuery(name = "BaseOperacionalControleAmbiental.excluirByIdeBaseOperacional", query = "DELETE FROM BaseOperacionalControleAmbiental bc WHERE bc.ideBaseOperacional.ideBaseOperacional = :ideBaseOperacional")
})
public class BaseOperacionalControleAmbiental implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	protected BaseOperacionalControleAmbientalPK id;

	@Column(name="num_borra_oleosa")
	private Double numBorraOleosa;

	@Column(name="num_dbo_media")
	private Double numDboMedia;

	@Column(name="num_og_medio")
	private Double numOgMedio;

	@Column(name="num_oleo_lubrificante")
	private Double numOleoLubrificante;

	@Column(name="num_vazao_media")
	private Double numVazaoMedia;
	
	@Transient
	private String dscEfluente;

	//bi-directional many-to-one association to BaseOperacional
	@ManyToOne(fetch=FetchType.LAZY)
	@NotNull
	@JoinColumn(name="ide_base_operacional",referencedColumnName = "ide_base_operacional", nullable = false, insertable = false, updatable = false)
	private BaseOperacional ideBaseOperacional;

	//bi-directional many-to-one association to TipoControleAmbiental
	@ManyToOne(fetch=FetchType.LAZY)
	@NotNull
	@JoinColumn(name="ide_tipo_controle_ambiental",referencedColumnName = "ide_tipo_controle_ambiental", nullable = false, insertable = false, updatable = false)
	private TipoControleAmbiental ideTipoControleAmbiental;

    public BaseOperacionalControleAmbiental() {
    }
    
	public BaseOperacionalControleAmbiental(String dscEfluente) {
		this.dscEfluente = dscEfluente;
	}

	public BaseOperacionalControleAmbiental(BaseOperacionalControleAmbientalPK id) {
		this.id = id;
	}

	public BaseOperacionalControleAmbientalPK getId() {
		return this.id;
	}

	public void setId(BaseOperacionalControleAmbientalPK id) {
		this.id = id;
	}
	
	public BaseOperacional getIdeBaseOperacional() {
		return ideBaseOperacional;
	}

	public void setIdeBaseOperacional(BaseOperacional ideBaseOperacional) {
		this.ideBaseOperacional = ideBaseOperacional;
	}

	public TipoControleAmbiental getIdeTipoControleAmbiental() {
		return ideTipoControleAmbiental;
	}

	public void setIdeTipoControleAmbiental(TipoControleAmbiental ideTipoControleAmbiental) {
		this.ideTipoControleAmbiental = ideTipoControleAmbiental;
	}

	public String getDscEfluente() {
		return dscEfluente;
	}

	public void setDscEfluente(String dscEfluente) {
		this.dscEfluente = dscEfluente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dscEfluente == null) ? 0 : dscEfluente.hashCode());
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
		BaseOperacionalControleAmbiental other = (BaseOperacionalControleAmbiental) obj;
		if (dscEfluente == null) {
			if (other.dscEfluente != null)
				return false;
		} else if (!dscEfluente.equals(other.dscEfluente))
			return false;
		return true;
	}

	public Double getNumBorraOleosa() {
		return numBorraOleosa;
	}

	public void setNumBorraOleosa(Double numBorraOleosa) {
		this.numBorraOleosa = numBorraOleosa;
	}

	public Double getNumDboMedia() {
		return numDboMedia;
	}

	public void setNumDboMedia(Double numDboMedia) {
		this.numDboMedia = numDboMedia;
	}

	public Double getNumOgMedio() {
		return numOgMedio;
	}

	public void setNumOgMedio(Double numOgMedio) {
		this.numOgMedio = numOgMedio;
	}

	public Double getNumOleoLubrificante() {
		return numOleoLubrificante;
	}

	public void setNumOleoLubrificante(Double numOleoLubrificante) {
		this.numOleoLubrificante = numOleoLubrificante;
	}

	public Double getNumVazaoMedia() {
		return numVazaoMedia;
	}

	public void setNumVazaoMedia(Double numVazaoMedia) {
		this.numVazaoMedia = numVazaoMedia;
	}
	
}