package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "geo_rpga")
public class GeoRpga extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "gid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gid;
	
	@Column(name = "nom_rpga", length=100)
	private String nomRpga;
	
	@Column(name = "cod_rpga", length=5)
	private String codRpga;
	
	@Column(name = "dsc_referencia", length=100)
	private String dscReferencia;
	
	@Column(name = "dtc_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	@Column(name = "ind_vigencia")
	private Boolean indVigencia;
	
	@OneToMany(mappedBy = "geoRpga", fetch = FetchType.LAZY)
	private Collection<CerhPrecoPubUnitario> cerhPrecoPubUnitarioCollection;
	
	public GeoRpga() {
	}

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public String getNomRpga() {
		return nomRpga;
	}

	public void setNomRpga(String nomRpga) {
		this.nomRpga = nomRpga;
	}

	public String getCodRpga() {
		return codRpga;
	}

	public void setCodRpga(String codRpga) {
		this.codRpga = codRpga;
	}

	public String getDscReferencia() {
		return dscReferencia;
	}

	public void setDscReferencia(String dscReferencia) {
		this.dscReferencia = dscReferencia;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Boolean getIndVigencia() {
		return indVigencia;
	}

	public void setIndVigencia(Boolean indVigencia) {
		this.indVigencia = indVigencia;
	}

	public Collection<CerhPrecoPubUnitario> getCerhPrecoPubUnitarioCollection() {
		return cerhPrecoPubUnitarioCollection;
	}

	public void setCerhPrecoPubUnitarioCollection(Collection<CerhPrecoPubUnitario> cerhPrecoPubUnitarioCollection) {
		this.cerhPrecoPubUnitarioCollection = cerhPrecoPubUnitarioCollection;
	}
}
