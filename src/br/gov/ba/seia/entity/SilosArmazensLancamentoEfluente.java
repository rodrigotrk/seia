package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * The persistent class for the silos_armazens_lancamento_efluente database table.
 * 
 */
@Entity
@Table(name="silos_armazens_lancamento_efluente")
@NamedQueries({
	@NamedQuery(name="SilosArmazensLancamentoEfluente.removerLancamentoEfluente", query="DELETE FROM SilosArmazensLancamentoEfluente s WHERE s.caracterizacaoAmbientalSilosArmazen = :ideCaracterizacaoAmbientalSilosArmazen")	
})

public class SilosArmazensLancamentoEfluente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_LANCAMENTO_EFLUENTE_IDESILOSARMAZENSLANCAMENTOEFLUENTE_GENERATOR", sequenceName="SILOS_ARMAZENS_LANCAMENTO_EFLUENTE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_LANCAMENTO_EFLUENTE_IDESILOSARMAZENSLANCAMENTOEFLUENTE_GENERATOR")
	@Column(name="ide_silos_armazens_lancamento_efluente")
	private Integer ideSilosArmazensLancamentoEfluente;

	@Column(name="num_documento_dispensa")
	private String numDocumentoDispensa;

	@Column(name="num_documento_portaria")
	private String numDocumentoPortaria;

	//bi-directional many-to-one association to CaracterizacaoAmbientalSilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_caracterizacao_ambiental_silos_armazens")
	private CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen;
	
	@Transient
	private String tipoDocumento;

	public SilosArmazensLancamentoEfluente() {
	}

	public Integer getIdeSilosArmazensLancamentoEfluente() {
		return this.ideSilosArmazensLancamentoEfluente;
	}

	public void setIdeSilosArmazensLancamentoEfluente(Integer ideSilosArmazensLancamentoEfluente) {
		this.ideSilosArmazensLancamentoEfluente = ideSilosArmazensLancamentoEfluente;
	}

	public String getNumDocumentoDispensa() {
		return this.numDocumentoDispensa;
	}

	public void setNumDocumentoDispensa(String numDocumentoDispensa) {
		this.numDocumentoDispensa = numDocumentoDispensa;
	}

	public String getNumDocumentoPortaria() {
		return this.numDocumentoPortaria;
	}

	public void setNumDocumentoPortaria(String numDocumentoPortaria) {
		this.numDocumentoPortaria = numDocumentoPortaria;
	}

	public CaracterizacaoAmbientalSilosArmazen getCaracterizacaoAmbientalSilosArmazen() {
		return this.caracterizacaoAmbientalSilosArmazen;
	}

	public void setCaracterizacaoAmbientalSilosArmazen(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		this.caracterizacaoAmbientalSilosArmazen = caracterizacaoAmbientalSilosArmazen;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazensLancamentoEfluente == null) ? 0
						: ideSilosArmazensLancamentoEfluente.hashCode());
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
		SilosArmazensLancamentoEfluente other = (SilosArmazensLancamentoEfluente) obj;
		if (ideSilosArmazensLancamentoEfluente == null) {
			if (other.ideSilosArmazensLancamentoEfluente != null)
				return false;
		} else if (!ideSilosArmazensLancamentoEfluente
				.equals(other.ideSilosArmazensLancamentoEfluente))
			return false;
		return true;
	}

	
}