package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the fce_industria_substancia database table.
 * 
 */
@Entity
@Table(name="fce_industria_substancia")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceIndustriaSubstancia.removeByIdeFceIndustria", query = "DELETE FROM FceIndustriaSubstancia f WHERE f.ideFceIndustria.ideFceIndustria = :ideFceIndustria")})
public class FceIndustriaSubstancia implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceIndustriaSubstanciaPK ideFceIndustriaSubstanciaPK;

	@Column(name="num_media_armazenamento")
	private BigDecimal numMediaArmazenamento;

	//bi-directional many-to-one association to FceIndustria
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_industria", referencedColumnName = "ide_fce_industria", nullable = false, insertable = false, updatable = false)
	private FceIndustria ideFceIndustria;

	//bi-directional many-to-one association to TipoSubstancia
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_substancia", referencedColumnName = "ide_tipo_substancia", nullable = false, insertable = false, updatable = false)
	private TipoSubstancia ideTipoSubstancia;

	@Transient
	private boolean confirmado;
	@Transient
	private boolean edicao;

	public FceIndustriaSubstancia() {
	}

	public FceIndustriaSubstancia(FceIndustriaSubstanciaPK ideFceIndustriaSubstanciaPK) {
		this.ideFceIndustriaSubstanciaPK = ideFceIndustriaSubstanciaPK;
	}

	public FceIndustriaSubstancia(TipoSubstancia ideTipoSubstancia) {
		this.ideTipoSubstancia = ideTipoSubstancia;
	}

	public FceIndustriaSubstancia(FceIndustria ideFceIndustria, TipoSubstancia ideTipoSubstancia) {
		this.ideFceIndustriaSubstanciaPK = new FceIndustriaSubstanciaPK(ideFceIndustria, ideTipoSubstancia);
	}

	public FceIndustriaSubstanciaPK getIdeFceIndustriaSubstanciaPK() {
		return ideFceIndustriaSubstanciaPK;
	}

	public void setIdeFceIndustriaSubstanciaPK(FceIndustriaSubstanciaPK ideFceIndustriaSubstanciaPK) {
		this.ideFceIndustriaSubstanciaPK = ideFceIndustriaSubstanciaPK;
	}

	public BigDecimal getNumMediaArmazenamento() {
		return numMediaArmazenamento;
	}

	public void setNumMediaArmazenamento(BigDecimal numMediaArmazenamento) {
		this.numMediaArmazenamento = numMediaArmazenamento;
	}

	public FceIndustria getIdeFceIndustria() {
		return ideFceIndustria;
	}

	public void setIdeFceIndustria(FceIndustria ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}

	public TipoSubstancia getIdeTipoSubstancia() {
		return ideTipoSubstancia;
	}

	public void setIdeTipoSubstancia(TipoSubstancia ideTipoSubstancia) {
		this.ideTipoSubstancia = ideTipoSubstancia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceIndustriaSubstanciaPK == null) ? 0
						: ideFceIndustriaSubstanciaPK.hashCode());
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
		FceIndustriaSubstancia other = (FceIndustriaSubstancia) obj;
		if (ideFceIndustriaSubstanciaPK == null) {
			if (other.ideFceIndustriaSubstanciaPK != null) {
				return false;
			}
		} else if (!ideFceIndustriaSubstanciaPK
				.equals(other.ideFceIndustriaSubstanciaPK)) {
			return false;
		}
		return true;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}
}