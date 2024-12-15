package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
@Entity
@Table(name="fce_asv_justificativa_supressao")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceAsvJustificativaSupressao.deleteByIdeFceAsv", query = "DELETE FROM FceAsvJustificativaSupressao WHERE ideFceAsv = :ideFceAsv")})
public class FceAsvJustificativaSupressao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceAsvJustificativaSupressaoPK ideFceAsvJustificativaSupressaoPK;

	@NotNull
	@JoinColumn(name = "ide_justificativa_supressao", referencedColumnName = "ide_justificativa_supressao", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private JustificativaSupressao ideJustificativaSupressao;

	@NotNull
	@JoinColumn(name = "ide_fce_asv", referencedColumnName = "ide_fce_asv", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAsv ideFceAsv;

	public FceAsvJustificativaSupressao() {
	}

	public FceAsvJustificativaSupressao(FceAsvJustificativaSupressaoPK ideFceAsvJustificativaSupressaoPK) {
		this.ideFceAsvJustificativaSupressaoPK = ideFceAsvJustificativaSupressaoPK;
	}

	public FceAsvJustificativaSupressao(Integer ideFceAsv, Integer ideJustificativa) {
		this.ideFceAsvJustificativaSupressaoPK = new FceAsvJustificativaSupressaoPK(ideFceAsv, ideJustificativa);
	}

	public FceAsv getIdeFceAsv() {
		return this.ideFceAsv;
	}

	public void setIdeFceAsv(FceAsv ideFceAsv) {
		this.ideFceAsv = ideFceAsv;
	}

	public JustificativaSupressao getIdeJustificativaSupressao() {
		return ideJustificativaSupressao;
	}

	public void setIdeJustificativaSupressao(JustificativaSupressao ideJustificativaSupressao) {
		this.ideJustificativaSupressao = ideJustificativaSupressao;
	}

	public FceAsvJustificativaSupressaoPK getIdeFceAsvJustificativaSupressaoPK() {
		return ideFceAsvJustificativaSupressaoPK;
	}

	public void setIdeFceAsvJustificativaSupressaoPK(FceAsvJustificativaSupressaoPK ideFceAsvJustificativaSupressaoPK) {
		this.ideFceAsvJustificativaSupressaoPK = ideFceAsvJustificativaSupressaoPK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceAsvJustificativaSupressaoPK == null) ? 0
						: ideFceAsvJustificativaSupressaoPK.hashCode());
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
		FceAsvJustificativaSupressao other = (FceAsvJustificativaSupressao) obj;
		if (ideFceAsvJustificativaSupressaoPK == null) {
			if (other.ideFceAsvJustificativaSupressaoPK != null) {
				return false;
			}
		} else if (!ideFceAsvJustificativaSupressaoPK
				.equals(other.ideFceAsvJustificativaSupressaoPK)) {
			return false;
		}
		return true;
	}
}