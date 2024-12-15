package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;


/**
 * The persistent class for the imovel_rural_rppn database table.
 * 
 */
@Entity
@Table(name="imovel_rural_rppn")
@NamedQuery(name="ImovelRuralRppn.findAll", query="SELECT i FROM ImovelRuralRppn i")
public class ImovelRuralRppn implements Serializable,Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="IMOVEL_RURAL_RPPN_IDEIMOVELRURALRPPN_GENERATOR", sequenceName="IMOVEL_RURAL_RPPN_IDE_IMOVEL_RURAL_RPPN_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="IMOVEL_RURAL_RPPN_IDEIMOVELRURALRPPN_GENERATOR")
	@Column(name="ide_imovel_rural_rppn", nullable = false)
	private Integer ideImovelRuralRppn;

	@Basic(optional = false)
	@Column(name="dsc_numero_decreto")
	private String dscNumeroDecreto;

	@Basic(optional = false)
	@Temporal(TemporalType.DATE)
	@Column(name="dtc_publicacao")
	private Date dtcPublicacao;

	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private ImovelRural ideImovelRural;

	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@OneToOne(optional = true, fetch = FetchType.EAGER)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@Basic(optional = false)
	@Column(name="nom_rppn")
	private String nomRppn;

	@Basic(optional = false)
	@Column(name="val_area", precision = 14, scale = 2)
	private Double valArea;
	
	@Size(max = 500)
	@Column(name = "dsc_observacao_alteracao_shape")
	private String dscObservacaoAlteracaoShape;
	
	@Transient
    private GeoJsonSicar geoJsonSicar;
	
	public ImovelRuralRppn() {
	}
	
	public ImovelRuralRppn(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}
	
	public Integer getIdeImovelRuralRppn() {
		return this.ideImovelRuralRppn;
	}

	public void setIdeImovelRuralRppn(Integer ideImovelRuralRppn) {
		this.ideImovelRuralRppn = ideImovelRuralRppn;
	}

	public String getDscNumeroDecreto() {
		return this.dscNumeroDecreto;
	}

	public void setDscNumeroDecreto(String dscNumeroDecreto) {
		this.dscNumeroDecreto = dscNumeroDecreto;
	}

	public Date getDtcPublicacao() {
		return this.dtcPublicacao;
	}

	public void setDtcPublicacao(Date dtcPublicacao) {
		this.dtcPublicacao = dtcPublicacao;
	}

	public ImovelRural getIdeImovelRural() {
		return this.ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return this.ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public String getNomRppn() {
		return this.nomRppn;
	}

	public void setNomRppn(String nomRppn) {
		this.nomRppn = nomRppn;
	}

	public Double getValArea() {
		return this.valArea;
	}

	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideImovelRuralRppn == null) ? 0 : ideImovelRuralRppn
						.hashCode());
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
		ImovelRuralRppn other = (ImovelRuralRppn) obj;
		if (ideImovelRuralRppn == null) {
			if (other.ideImovelRuralRppn != null)
				return false;
		} else if (!ideImovelRuralRppn.equals(other.ideImovelRuralRppn))
			return false;
		return true;
	}
	
	public GeoJsonSicar getGeoJsonSicar() {
		return geoJsonSicar;
	}

	public void setGeoJsonSicar(GeoJsonSicar geoJsonSicar) {
		this.geoJsonSicar = geoJsonSicar;
	}

	public String getDscObservacaoAlteracaoShape() {
		return dscObservacaoAlteracaoShape;
	}

	public void setDscObservacaoAlteracaoShape(String dscObservacaoAlteracaoShape) {
		this.dscObservacaoAlteracaoShape = dscObservacaoAlteracaoShape;
	}
	
	@Override
	public ImovelRuralRppn clone() throws CloneNotSupportedException {
		return (ImovelRuralRppn) super.clone();
	}
}