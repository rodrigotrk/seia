package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the fce_pulverizacao database table.
 * 
 */
@Entity
@Table(name="fce_pulverizacao")
public class FcePulverizacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_FCE_PULVERIZACAO_IDE_FCE_PULV_SEQ", sequenceName="FCE_FCE_PULVERIZACAO_IDE_FCE_PULV_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_FCE_PULVERIZACAO_IDE_FCE_PULV_SEQ")
	@Basic(optional = false)
	@NotNull
	@Column(name="ide_fce_pulverizacao")
	private Integer ideFcePulverizacao;

	@NotNull
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@Column(name="ind_pulverizacao_aerea")
	private Boolean indPulverizacaoAerea;

	@Column(name="ind_pulverizacao_terrestre")
	private Boolean indPulverizacaoTerrestre;

	@Column(name="num_volume_aerea")
	private BigDecimal numVolumeAerea;

	@Column(name="num_volume_derivar")
	private BigDecimal numVolumeDerivar;

	@Column(name="num_volume_terrestre")
	private BigDecimal numVolumeTerrestre;

	@Column(name="num_area_pulverizada")
	private BigDecimal numAreaPulverizada;

	@Column(name="ind_outros")
	private Boolean indOutros;

	//bi-directional many-to-one association to TipologiaPulverizacao
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipologia_pulverizacao")
	private TipologiaPulverizacao tipologiaPulverizacao;

	public FcePulverizacao() {
		
	}
	
	public FcePulverizacao(Fce fce) {
		this.ideFce = fce;
	}

	public Integer getIdeFcePulverizacao() {
		return ideFcePulverizacao;
	}

	public void setIdeFcePulverizacao(Integer ideFcePulverizacao) {
		this.ideFcePulverizacao = ideFcePulverizacao;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Boolean getIndPulverizacaoAerea() {
		return indPulverizacaoAerea;
	}

	public void setIndPulverizacaoAerea(Boolean indPulverizacaoAerea) {
		this.indPulverizacaoAerea = indPulverizacaoAerea;
	}

	public Boolean getIndPulverizacaoTerrestre() {
		return indPulverizacaoTerrestre;
	}

	public void setIndPulverizacaoTerrestre(Boolean indPulverizacaoTerrestre) {
		this.indPulverizacaoTerrestre = indPulverizacaoTerrestre;
	}

	public BigDecimal getNumVolumeAerea() {
		return numVolumeAerea;
	}

	public void setNumVolumeAerea(BigDecimal numVolumeAerea) {
		this.numVolumeAerea = numVolumeAerea;
	}

	public BigDecimal getNumVolumeDerivar() {
		return numVolumeDerivar;
	}

	public void setNumVolumeDerivar(BigDecimal numVolumeDerivar) {
		this.numVolumeDerivar = numVolumeDerivar;
	}

	public BigDecimal getNumVolumeTerrestre() {
		return numVolumeTerrestre;
	}

	public void setNumVolumeTerrestre(BigDecimal numVolumeTerrestre) {
		this.numVolumeTerrestre = numVolumeTerrestre;
	}

	public TipologiaPulverizacao getTipologiaPulverizacao() {
		return tipologiaPulverizacao;
	}

	public void setTipologiaPulverizacao(TipologiaPulverizacao tipologiaPulverizacao) {
		this.tipologiaPulverizacao = tipologiaPulverizacao;
	}

	public BigDecimal getNumAreaPulverizada() {
		return numAreaPulverizada;
	}

	public void setNumAreaPulverizada(BigDecimal numAreaPulverizada) {
		this.numAreaPulverizada = numAreaPulverizada;
	}

	public Boolean getIndOutros() {
		return indOutros;
	}

	public void setIndOutros(Boolean indOutros) {
		this.indOutros = indOutros;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFcePulverizacao == null) ? 0 : ideFcePulverizacao
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FcePulverizacao other = (FcePulverizacao) obj;
		if (ideFcePulverizacao == null) {
			if (other.ideFcePulverizacao != null) {
				return false;
			}
		} else if (!ideFcePulverizacao.equals(other.ideFcePulverizacao)) {
			return false;
		}
		return true;
	}
}