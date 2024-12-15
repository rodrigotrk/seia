package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "cerh_dae_loc_geografica")
public class CerhDaeLocGeografica {

	@Id
	@Column(name = "ide_cerh_dae_loc_geografica")
	@GeneratedValue(generator = "cerh_dae_loc_geografica_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "cerh_dae_loc_geografica_seq", sequenceName = "cerh_dae_loc_geografica_seq", allocationSize = 1)
	private Integer ideCerhDaeLocGegografica;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_cerh_localizacao_geografica")
	private CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica;

	public CerhDaeLocGeografica() {
	}

	public Integer getIdeCerhDaeLocGegografica() {
		return ideCerhDaeLocGegografica;
	}

	public void setIdeCerhDaeLocGegografica(Integer ideCerhDaeLocGegografica) {
		this.ideCerhDaeLocGegografica = ideCerhDaeLocGegografica;
	}

	public CerhLocalizacaoGeografica getIdeCerhLocalizacaoGeografica() {
		return ideCerhLocalizacaoGeografica;
	}

	public void setIdeCerhLocalizacaoGeografica(
			CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = ideCerhLocalizacaoGeografica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCerhDaeLocGegografica == null) ? 0
						: ideCerhDaeLocGegografica.hashCode());
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
		CerhDaeLocGeografica other = (CerhDaeLocGeografica) obj;
		if (ideCerhDaeLocGegografica == null) {
			if (other.ideCerhDaeLocGegografica != null)
				return false;
		} else if (!ideCerhDaeLocGegografica
				.equals(other.ideCerhDaeLocGegografica))
			return false;
		return true;
	}
	
	
	
}
