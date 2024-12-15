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
@Table(name="fce_asv_obj_supressao")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "FceAsvObjetivoSupressao.deleteByIdeFceAsv", query = "DELETE FROM FceAsvObjetivoSupressao WHERE ideFceAsv = :ideFceAsv")})
public class FceAsvObjetivoSupressao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceAsvObjetivoSupressaoPK ideFceObjetivoSupressaoPK;

	@NotNull
	@JoinColumn(name = "ide_objetivo_supressao", referencedColumnName = "ide_objetivo_supressao", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ObjetivoSupressao ideObjetivoSupressao;

	@NotNull
	@JoinColumn(name = "ide_fce_asv", referencedColumnName = "ide_fce_asv", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAsv ideFceAsv;

	public FceAsvObjetivoSupressao() {
	}

	public FceAsvObjetivoSupressao(FceAsvObjetivoSupressaoPK ideFceObjetivoSupressaoPK) {
		this.ideFceObjetivoSupressaoPK = ideFceObjetivoSupressaoPK;
	}

	public FceAsvObjetivoSupressao(Integer ideFceAsv, Integer ideObjetivoSupressao) {
		this.ideFceObjetivoSupressaoPK = new FceAsvObjetivoSupressaoPK(ideFceAsv, ideObjetivoSupressao);
	}

	public FceAsv getIdeFceAsv() {
		return this.ideFceAsv;
	}

	public void setIdeFceAsv(FceAsv ideFceAsv) {
		this.ideFceAsv = ideFceAsv;
	}

	public ObjetivoSupressao getIdeObjetivoSupressao() {
		return ideObjetivoSupressao;
	}

	public void setIdeObjetivoSupressao(ObjetivoSupressao ideObjetivoSupressao) {
		this.ideObjetivoSupressao = ideObjetivoSupressao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceObjetivoSupressaoPK == null) ? 0 : ideFceObjetivoSupressaoPK.hashCode());
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
		FceAsvObjetivoSupressao other = (FceAsvObjetivoSupressao) obj;
		if (ideFceObjetivoSupressaoPK == null) {
			if (other.ideFceObjetivoSupressaoPK != null) {
				return false;
			}
		} else if (!ideFceObjetivoSupressaoPK
				.equals(other.ideFceObjetivoSupressaoPK)) {
			return false;
		}
		return true;
	}

	public FceAsvObjetivoSupressaoPK getIdeFceObjetivoSupressaoPK() {
		return ideFceObjetivoSupressaoPK;
	}


	public void setIdeFceObjetivoSupressaoPK(FceAsvObjetivoSupressaoPK ideFceObjetivoSupressaoPK) {
		this.ideFceObjetivoSupressaoPK = ideFceObjetivoSupressaoPK;
	}

	@Override
	public String toString() {
		return "FceAsvObjetivoSupressao [ObjetivoSupressao=" + ideObjetivoSupressao.getDscObjetoSupressao() + ", ideFceAsv=" + ideFceAsv + "]";
	}
}