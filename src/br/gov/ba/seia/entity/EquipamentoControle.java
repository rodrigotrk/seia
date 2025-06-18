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
 * The persistent class for the equipamento_controle database table.
 * 
 */
@Entity
@Table(name="equipamento_controle")
@NamedQuery(name="EquipamentoControle.findAll", query="SELECT e FROM EquipamentoControle e")
public class EquipamentoControle implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="EQUIPAMENTO_CONTROLE_IDEEQUIPAMENTOCONTROLE_GENERATOR", sequenceName="EQUIPAMENTO_CONTROLE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="EQUIPAMENTO_CONTROLE_IDEEQUIPAMENTOCONTROLE_GENERATOR")
	@Column(name="ide_equipamento_controle")
	private Integer ideEquipamentoControle;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_equipamento_controle")
	private String nomEquipamentoControle;

	//bi-directional many-to-one association to CaracterizacaoAmbientalEquipamentoControle
	@OneToMany(mappedBy="equipamentoControle")
	private List<CaracterizacaoAmbientalEquipamentoControle> caracterizacaoAmbientalEquipamentoControles;

	public EquipamentoControle() {
	}

	public Integer getIdeEquipamentoControle() {
		return this.ideEquipamentoControle;
	}

	public void setIdeEquipamentoControle(Integer ideEquipamentoControle) {
		this.ideEquipamentoControle = ideEquipamentoControle;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomEquipamentoControle() {
		return this.nomEquipamentoControle;
	}

	public void setNomEquipamentoControle(String nomEquipamentoControle) {
		this.nomEquipamentoControle = nomEquipamentoControle;
	}

	public List<CaracterizacaoAmbientalEquipamentoControle> getCaracterizacaoAmbientalEquipamentoControles() {
		return this.caracterizacaoAmbientalEquipamentoControles;
	}

	public void setCaracterizacaoAmbientalEquipamentoControles(List<CaracterizacaoAmbientalEquipamentoControle> caracterizacaoAmbientalEquipamentoControles) {
		this.caracterizacaoAmbientalEquipamentoControles = caracterizacaoAmbientalEquipamentoControles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideEquipamentoControle == null) ? 0
						: ideEquipamentoControle.hashCode());
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
		EquipamentoControle other = (EquipamentoControle) obj;
		if (ideEquipamentoControle == null) {
			if (other.ideEquipamentoControle != null)
				return false;
		} else if (!ideEquipamentoControle.equals(other.ideEquipamentoControle))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideEquipamentoControle);
	}

	
}