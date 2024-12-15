package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the destinacao_final database table.
 * 
 */
@Entity
@Table(name="destinacao_final")
@NamedQuery(name="DestinacaoFinal.findAll", query="SELECT d FROM DestinacaoFinal d")
public class DestinacaoFinal implements Serializable,BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="DESTINACAO_FINAL_IDEDESTINACAOFINAL_GENERATOR", sequenceName="DESTINACAO_FINAL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DESTINACAO_FINAL_IDEDESTINACAOFINAL_GENERATOR")
	@Column(name="ide_destinacao_final")
	private Integer ideDestinacaoFinal;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_destinacao_final")
	private String nomDestinacaoFinal;

	//bi-directional many-to-one association to CaracterizacaoAmbientalDestinacaoFinal
	@OneToMany(mappedBy="destinacaoFinal")
	private List<CaracterizacaoAmbientalDestinacaoFinal> caracterizacaoAmbientalDestinacaoFinals;

	public DestinacaoFinal() {
	}

	public Integer getIdeDestinacaoFinal() {
		return this.ideDestinacaoFinal;
	}

	public void setIdeDestinacaoFinal(Integer ideDestinacaoFinal) {
		this.ideDestinacaoFinal = ideDestinacaoFinal;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomDestinacaoFinal() {
		return this.nomDestinacaoFinal;
	}

	public void setNomDestinacaoFinal(String nomDestinacaoFinal) {
		this.nomDestinacaoFinal = nomDestinacaoFinal;
	}

	public List<CaracterizacaoAmbientalDestinacaoFinal> getCaracterizacaoAmbientalDestinacaoFinals() {
		return this.caracterizacaoAmbientalDestinacaoFinals;
	}

	public void setCaracterizacaoAmbientalDestinacaoFinals(List<CaracterizacaoAmbientalDestinacaoFinal> caracterizacaoAmbientalDestinacaoFinals) {
		this.caracterizacaoAmbientalDestinacaoFinals = caracterizacaoAmbientalDestinacaoFinals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideDestinacaoFinal == null) ? 0 : ideDestinacaoFinal
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
		DestinacaoFinal other = (DestinacaoFinal) obj;
		if (ideDestinacaoFinal == null) {
			if (other.ideDestinacaoFinal != null)
				return false;
		} else if (!ideDestinacaoFinal.equals(other.ideDestinacaoFinal))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideDestinacaoFinal);
	}

	

}