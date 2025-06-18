package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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


/**
 * The persistent class for the silos_armazens_unidade_armazenadora database table.
 * 
 */
@Entity
@Table(name="silos_armazens_unidade_armazenadora")
@NamedQueries({
	@NamedQuery(name = "SilosArmazensUnidadeArmazenadora.removerByIdeSilos", query ="DELETE FROM SilosArmazensUnidadeArmazenadora s WHERE s.silosArmazen = :ideSilosArmazen") })
public class SilosArmazensUnidadeArmazenadora implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_UNIDADE_ARMAZENADORA_IDESILOSARMAZENSUNIDADEARMAZENADORA_GENERATOR", sequenceName="SILOS_ARMAZENS_UNIDADE_ARMAZENADORA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_UNIDADE_ARMAZENADORA_IDESILOSARMAZENSUNIDADEARMAZENADORA_GENERATOR")
	@Column(name="ide_silos_armazens_unidade_armazenadora")
	private Integer ideSilosArmazensUnidadeArmazenadora;

	@Column(name="cod_cda")
	private String codCda;
	
	@ManyToOne
	@JoinColumn(name="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@Column(name="nom_unidade_armazenadora")
	private String nomUnidadeArmazenadora;

	@Column(name="val_capacidade_estatica")
	private BigDecimal valCapacidadeEstatica;

	//bi-directional many-to-one association to SilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_silos_armazens")
	private SilosArmazen silosArmazen;

	//bi-directional many-to-one association to TipoArmazem
	@ManyToOne
	@JoinColumn(name="ide_tipo_armazem")
	private TipoArmazem tipoArmazem;

	public SilosArmazensUnidadeArmazenadora() {
		this.ideLocalizacaoGeografica = new LocalizacaoGeografica();
	}

	public Integer getIdeSilosArmazensUnidadeArmazenadora() {
		return this.ideSilosArmazensUnidadeArmazenadora;
	}

	public void setIdeSilosArmazensUnidadeArmazenadora(Integer ideSilosArmazensUnidadeArmazenadora) {
		this.ideSilosArmazensUnidadeArmazenadora = ideSilosArmazensUnidadeArmazenadora;
	}

	public String getCodCda() {
		return this.codCda;
	}

	public void setCodCda(String codCda) {
		this.codCda = codCda;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public String getNomUnidadeArmazenadora() {
		return this.nomUnidadeArmazenadora;
	}

	public void setNomUnidadeArmazenadora(String nomUnidadeArmazenadora) {
		this.nomUnidadeArmazenadora = nomUnidadeArmazenadora;
	}

	public BigDecimal getValCapacidadeEstatica() {
		return this.valCapacidadeEstatica;
	}

	public void setValCapacidadeEstatica(BigDecimal valCapacidadeEstatica) {
		this.valCapacidadeEstatica = valCapacidadeEstatica;
	}

	public SilosArmazen getSilosArmazen() {
		return this.silosArmazen;
	}

	public void setSilosArmazen(SilosArmazen silosArmazen) {
		this.silosArmazen = silosArmazen;
	}

	public TipoArmazem getTipoArmazem() {
		return this.tipoArmazem;
	}

	public void setTipoArmazem(TipoArmazem tipoArmazem) {
		this.tipoArmazem = tipoArmazem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazensUnidadeArmazenadora == null) ? 0
						: ideSilosArmazensUnidadeArmazenadora.hashCode());
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
		SilosArmazensUnidadeArmazenadora other = (SilosArmazensUnidadeArmazenadora) obj;
		if (ideSilosArmazensUnidadeArmazenadora == null) {
			if (other.ideSilosArmazensUnidadeArmazenadora != null)
				return false;
		} else if (!ideSilosArmazensUnidadeArmazenadora
				.equals(other.ideSilosArmazensUnidadeArmazenadora))
			return false;
		return true;
	}

	
}