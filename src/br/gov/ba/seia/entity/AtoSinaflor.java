
package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ato_sinaflor")
public class AtoSinaflor implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_ato_sinaflor")
	private Integer ideAtoSinaflor;

	@Column(name = "nom_ato_sinaflor")
	private String nomAtoSinaflor;

	public AtoSinaflor() {
	}

	public Integer getIdeAtoSinaflor() {
		return ideAtoSinaflor;
	}

	public void setIdeAtoSinaflor(Integer ideAtoSinaflor) {
		this.ideAtoSinaflor = ideAtoSinaflor;
	}

	public String getNomAtoSinaflor() {
		return nomAtoSinaflor;
	}

	public void setNomAtoSinaflor(String nomAtoSinaflor) {
		this.nomAtoSinaflor = nomAtoSinaflor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideAtoSinaflor == null) ? 0 : ideAtoSinaflor.hashCode());
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
		AtoSinaflor other = (AtoSinaflor) obj;
		if (ideAtoSinaflor == null) {
			if (other.ideAtoSinaflor != null)
				return false;
		} else if (!ideAtoSinaflor.equals(other.ideAtoSinaflor))
			return false;
		return true;
	}

}
