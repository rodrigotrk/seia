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
@Table(name = "cerh_parcelas_dae")
public class CerhParcelasDae {
	
	@Id
	@Column(name = "ide_cerh_parcelas_dae")
	@GeneratedValue(generator = "cerh_parcelas_dae_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "cerh_parcelas_dae_seq", sequenceName = "cerh_parcelas_dae_seq", allocationSize = 1 )
	private Integer ideCerhParcelasDae;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_cerh_parcelas_cobranca", nullable = false)
	private CerhParcelasCobranca ideCerhParcelasCobranca;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ide_cerh_dae", nullable = false)
	private Dae ideCerhDae;

	public CerhParcelasDae() {
	}

	public Integer getIdeCerhParcelasDae() {
		return ideCerhParcelasDae;
	}

	public void setIdeCerhParcelasDae(Integer ideCerhParcelasDae) {
		this.ideCerhParcelasDae = ideCerhParcelasDae;
	}

	public CerhParcelasCobranca getIdeCerhParcelasCobranca() {
		return ideCerhParcelasCobranca;
	}

	public void setIdeCerhParcelasCobranca(
			CerhParcelasCobranca ideCerhParcelasCobranca) {
		this.ideCerhParcelasCobranca = ideCerhParcelasCobranca;
	}

	public Dae getIdeCerhDae() {
		return ideCerhDae;
	}

	public void setIdeCerhDae(Dae ideCerhDae) {
		this.ideCerhDae = ideCerhDae;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCerhParcelasDae == null) ? 0 : ideCerhParcelasDae
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
		CerhParcelasDae other = (CerhParcelasDae) obj;
		if (ideCerhParcelasDae == null) {
			if (other.ideCerhParcelasDae != null)
				return false;
		} else if (!ideCerhParcelasDae.equals(other.ideCerhParcelasDae))
			return false;
		return true;
	}
	
	
	

}
