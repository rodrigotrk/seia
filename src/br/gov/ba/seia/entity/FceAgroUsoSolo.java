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
 * The persistent class for the fce_agro_uso_solo database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="fce_agro_uso_solo")
@NamedQueries({
	@NamedQuery(name = "FceAgroUsoSolo.excluirByIdeFceAgrossilvopastoril", query = "DELETE FROM FceAgroUsoSolo faus WHERE faus.ideFceAgrossilvopastoril.ideFceAgrossilvopastoril = :ideFceAgrossilvopastoril"),
})

public class FceAgroUsoSolo implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceAgroUsoSoloPK ideFceAgroUsoSoloPK;
	
	@NotNull
	@JoinColumn(name = "ide_fce_agrossilvopastoril", referencedColumnName = "ide_fce_agrossilvopastoril", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAgrossilvopastoril ideFceAgrossilvopastoril;

	@NotNull
	@JoinColumn(name = "ide_tipo_uso_solo", referencedColumnName = "ide_tipo_uso_solo", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoUsoSolo ideTipoUsoSolo;

	@Column(name="num_area_imovel_existente",precision = 10, scale = 2, nullable = false)
	private Double numAreaImovelExistente;

	@Column(name="num_area_imovel_planejada",precision = 10, scale = 2, nullable = false)
	private Double numAreaImovelPlanejada;

	@Column(name="num_percentual_area_imovel_existente",precision = 5, scale = 2, nullable = false)
	private Double numPercentualAreaImovelExistente;

	@Column(name="num_percentual_area_planejada",precision = 5, scale = 2, nullable = false)
	private Double numPercentualAreaPlanejada;
	
	@Transient
	private boolean desabilitado;

    public FceAgroUsoSolo() {
    }

	public FceAgroUsoSolo(TipoUsoSolo ideTipoUsoSolo) {
		this.ideTipoUsoSolo = ideTipoUsoSolo;
	}

	public FceAgroUsoSolo(TipoUsoSolo ideTipoUsoSolo, boolean desabilitado) {
		this.ideTipoUsoSolo = ideTipoUsoSolo;
		this.desabilitado = desabilitado;
	}

	public FceAgroUsoSoloPK getIdeFceAgroUsoSoloPK() {
		return ideFceAgroUsoSoloPK;
	}

	public void setIdeFceAgroUsoSoloPK(FceAgroUsoSoloPK ideFceAgroUsoSoloPK) {
		this.ideFceAgroUsoSoloPK = ideFceAgroUsoSoloPK;
	}

	public FceAgrossilvopastoril getIdeFceAgrossilvopastoril() {
		return ideFceAgrossilvopastoril;
	}

	public void setIdeFceAgrossilvopastoril(
			FceAgrossilvopastoril ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}

	public TipoUsoSolo getIdeTipoUsoSolo() {
		return ideTipoUsoSolo;
	}

	public void setIdeTipoUsoSolo(TipoUsoSolo ideTipoUsoSolo) {
		this.ideTipoUsoSolo = ideTipoUsoSolo;
	}

	public Double getNumAreaImovelExistente() {
		return numAreaImovelExistente;
	}

	public void setNumAreaImovelExistente(Double numAreaImovelExistente) {
		this.numAreaImovelExistente = numAreaImovelExistente;
	}

	public Double getNumAreaImovelPlanejada() {
		return numAreaImovelPlanejada;
	}

	public void setNumAreaImovelPlanejada(Double numAreaImovelPlanejada) {
		this.numAreaImovelPlanejada = numAreaImovelPlanejada;
	}

	public Double getNumPercentualAreaImovelExistente() {
		return numPercentualAreaImovelExistente;
	}

	public void setNumPercentualAreaImovelExistente(Double numPercentualAreaImovelExistente) {
		this.numPercentualAreaImovelExistente = numPercentualAreaImovelExistente;
	}

	public Double getNumPercentualAreaPlanejada() {
		return numPercentualAreaPlanejada;
	}

	public void setNumPercentualAreaPlanejada(Double numPercentualAreaPlanejada) {
		this.numPercentualAreaPlanejada = numPercentualAreaPlanejada;
	}
	
	public boolean isDesabilitado() {
		return desabilitado;
	}

	public void setDesabilitado(boolean desabilitado) {
		this.desabilitado = desabilitado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoUsoSolo == null) ? 0 : ideTipoUsoSolo.hashCode());
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
		FceAgroUsoSolo other = (FceAgroUsoSolo) obj;
		if (ideTipoUsoSolo == null) {
			if (other.ideTipoUsoSolo != null)
				return false;
		} else if (!ideTipoUsoSolo.equals(other.ideTipoUsoSolo))
			return false;
		return true;
	}

}