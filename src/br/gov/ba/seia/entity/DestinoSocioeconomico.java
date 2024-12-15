package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the destino_socioeconomico database table.
 * 
 */
@Entity
@Table(name="destino_socioeconomico")
public class DestinoSocioeconomico implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_destino_socioeconomico")
	private Integer ideDestinoSocioeconomico;

	@Column(name="dsc_destino_socioeconomico")
	private String dscDestinoSocioeconomico;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public DestinoSocioeconomico() {
	}

	public Integer getIdeDestinoSocioeconomico() {
		return this.ideDestinoSocioeconomico;
	}

	public void setIdeDestinoSocioeconomico(Integer ideDestinoSocioeconomico) {
		this.ideDestinoSocioeconomico = ideDestinoSocioeconomico;
	}

	public String getDscDestinoSocioeconomico() {
		return this.dscDestinoSocioeconomico;
	}

	public void setDscDestinoSocioeconomico(String dscDestinoSocioeconomico) {
		this.dscDestinoSocioeconomico = dscDestinoSocioeconomico;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(ideDestinoSocioeconomico);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideDestinoSocioeconomico == null) ? 0
						: ideDestinoSocioeconomico.hashCode());
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
		DestinoSocioeconomico other = (DestinoSocioeconomico) obj;
		if (ideDestinoSocioeconomico == null) {
			if (other.ideDestinoSocioeconomico != null) {
				return false;
			}
		} else if (!ideDestinoSocioeconomico
				.equals(other.ideDestinoSocioeconomico)) {
			return false;
		}
		return true;
	}


}