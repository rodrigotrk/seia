package br.gov.ba.seia.entity;

import java.io.Serializable;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the fce_outorga_localizacao_aquicultura database table.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/11/2014
 */
@Entity
@Table(name="fce_outorga_localizacao_aquicultura")
@NamedQueries({
	@NamedQuery(name="FceOutorgaLocalizacaoAquicultura.findAll", query="SELECT f FROM FceOutorgaLocalizacaoAquicultura f"),
	@NamedQuery(name = "FceOutorgaLocalizacaoAquicultura.removeByIdeFceAquicultura", query = "DELETE FROM FceOutorgaLocalizacaoAquicultura f WHERE f.fceAquicultura.ideFceAquicultura = :ideFceAquicultura")})
public class FceOutorgaLocalizacaoAquicultura implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_OUTORGA_LOCALIZACAO_AQUICULTURA_IDEFCEOUTORGALOCALIZACAOAQUICULTURA_GENERATOR", sequenceName="FCE_OUTORGA_LOCALIZACAO_AQUICULTURA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_OUTORGA_LOCALIZACAO_AQUICULTURA_IDEFCEOUTORGALOCALIZACAOAQUICULTURA_GENERATOR")
	@Column(name="ide_fce_outorga_localizacao_aquicultura")
	private Integer ideFceOutorgaLocalizacaoAquicultura;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_outorga_localizacao_geografica", referencedColumnName="ide_fce_outorga_localizacao_geografica")
	private FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica;

	//bi-directional many-to-one association to FceAquicultura
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_aquicultura", referencedColumnName="ide_fce_aquicultura")
	private FceAquicultura fceAquicultura;

	public FceOutorgaLocalizacaoAquicultura() {
	}

	public FceOutorgaLocalizacaoAquicultura(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica, FceAquicultura fceAquicultura) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
		this.fceAquicultura = fceAquicultura;
	}

	public Integer getIdeFceOutorgaLocalizacaoAquicultura() {
		return this.ideFceOutorgaLocalizacaoAquicultura;
	}

	public void setIdeFceOutorgaLocalizacaoAquicultura(Integer ideFceOutorgaLocalizacaoAquicultura) {
		this.ideFceOutorgaLocalizacaoAquicultura = ideFceOutorgaLocalizacaoAquicultura;
	}

	public FceOutorgaLocalizacaoGeografica getIdeFceOutorgaLocalizacaoGeografica() {
		return this.ideFceOutorgaLocalizacaoGeografica;
	}

	public void setIdeFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}

	public FceAquicultura getFceAquicultura() {
		return this.fceAquicultura;
	}

	public void setFceAquicultura(FceAquicultura fceAquicultura) {
		this.fceAquicultura = fceAquicultura;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fceAquicultura == null) ? 0 : fceAquicultura.hashCode());
		result = prime
				* result
				+ ((ideFceOutorgaLocalizacaoAquicultura == null) ? 0
						: ideFceOutorgaLocalizacaoAquicultura.hashCode());
		result = prime
				* result
				+ ((ideFceOutorgaLocalizacaoGeografica == null) ? 0
						: ideFceOutorgaLocalizacaoGeografica.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		FceOutorgaLocalizacaoAquicultura other = (FceOutorgaLocalizacaoAquicultura) obj;
		if (fceAquicultura == null) {
			if (other.fceAquicultura != null) {
				return false;
			}
		} else if (!fceAquicultura.equals(other.fceAquicultura)) {
			return false;
		}
		if (ideFceOutorgaLocalizacaoAquicultura == null) {
			if (other.ideFceOutorgaLocalizacaoAquicultura != null) {
				return false;
			}
		} else if (!ideFceOutorgaLocalizacaoAquicultura
				.equals(other.ideFceOutorgaLocalizacaoAquicultura)) {
			return false;
		}
		if (ideFceOutorgaLocalizacaoGeografica == null) {
			if (other.ideFceOutorgaLocalizacaoGeografica != null) {
				return false;
			}
		} else if (!ideFceOutorgaLocalizacaoGeografica
				.equals(other.ideFceOutorgaLocalizacaoGeografica)) {
			return false;
		}
		return true;
	}

	@Override
    public FceOutorgaLocalizacaoAquicultura clone() throws CloneNotSupportedException {
         return (FceOutorgaLocalizacaoAquicultura) super.clone();
    }
}