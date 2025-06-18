package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="area_rural_consolidada")
@NamedQuery(name="AreaRuralConsolidada.findAll", query="SELECT a FROM AreaRuralConsolidada a")
public class AreaRuralConsolidada implements Serializable {
	
	private static final long serialVersionUID = -2115823352603164697L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AREA_RURAL_CONSOLIDADA_IDE_AREA_RURAL_CONSOLIDADA_SEQ")
	@SequenceGenerator(name = "AREA_RURAL_CONSOLIDADA_IDE_AREA_RURAL_CONSOLIDADA_SEQ", sequenceName = "AREA_RURAL_CONSOLIDADA_IDE_AREA_RURAL_CONSOLIDADA_SEQ", allocationSize = 1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_area_rural_consolidada", nullable = false)
	private Integer ideAreaRuralConsolidada;
	
	@JoinColumn(name="ide_imovel_rural", referencedColumnName="ide_imovel_rural", nullable=false)
	@NotNull
	@OneToOne(optional=false)
	private ImovelRural ideImovelRural;
	
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@OneToOne
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@Basic(optional = false)
	@Column(name = "val_area")
	private Double valArea;
	
	@Transient
    private GeoJsonSicar geoJsonSicar;

	public Integer getIdeAreaRuralConsolidada() {
		return ideAreaRuralConsolidada;
	}

	public void setIdeAreaRuralConsolidada(Integer ideAreaRuralConsolidada) {
		this.ideAreaRuralConsolidada = ideAreaRuralConsolidada;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public Double getValArea() {
		return valArea;
	}

	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.ideAreaRuralConsolidada);
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.ideAreaRuralConsolidada != null ? this.ideAreaRuralConsolidada.hashCode() : 0);
		return hash;
    }

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AreaRuralConsolidada)) {
			return false;
		}
		AreaRuralConsolidada other = (AreaRuralConsolidada) object;
		if ((this.ideAreaRuralConsolidada == null && other.ideAreaRuralConsolidada != null)
				|| (this.ideAreaRuralConsolidada != null && !this.ideAreaRuralConsolidada.equals(other.ideAreaRuralConsolidada))) {
			return false;
		}
		return true;
	}
	
	public GeoJsonSicar getGeoJsonSicar() {
		return geoJsonSicar;
	}

	public void setGeoJsonSicar(GeoJsonSicar geoJsonSicar) {
		this.geoJsonSicar = geoJsonSicar;
	}
	
	
}
