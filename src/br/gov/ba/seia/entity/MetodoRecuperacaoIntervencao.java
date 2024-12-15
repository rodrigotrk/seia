package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@Table(name = "metodo_recuperacao_intervencao")
public class MetodoRecuperacaoIntervencao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "METODO_RECUPERACAO_INTERVENCAO_IDEMETODORECUPERACAOINTERVENCAO_GENERATOR", sequenceName = "METODO_RECUPERACAO_INTERVENCAO_IDE_METODO_RECUPERACAO_INTERVENCAO_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "METODO_RECUPERACAO_INTERVENCAO_IDEMETODORECUPERACAOINTERVENCAO_GENERATOR")
	@Column(name = "ide_metodo_recuperacao_intervencao")
	private Integer ideMetodoRecuperacaoIntervencao;

	@NotNull
	@Column(name = "nom_metodo_recuperacao_intervencao")
	private String nomMetodoRecuperacaoIntervencao;

	@Transient
	private boolean checked;
	
	public MetodoRecuperacaoIntervencao() {
	}

	public MetodoRecuperacaoIntervencao(Integer ideMetodoRecuperacaoIntervencao) {
		this.ideMetodoRecuperacaoIntervencao = ideMetodoRecuperacaoIntervencao;
	}
	public MetodoRecuperacaoIntervencao(Integer ide, String nom) {
		this.ideMetodoRecuperacaoIntervencao = ide;
		this.nomMetodoRecuperacaoIntervencao = nom;
	}

	public Integer getIdeMetodoRecuperacaoIntervencao() {
		return this.ideMetodoRecuperacaoIntervencao;
	}

	public void setIdeMetodoRecuperacaoIntervencao(Integer ideMetodoRecuperacaoIntervencao) {
		this.ideMetodoRecuperacaoIntervencao = ideMetodoRecuperacaoIntervencao;
	}

	public String getNomMetodoRecuperacaoIntervencao() {
		return this.nomMetodoRecuperacaoIntervencao;
	}

	public void setNomMetodoRecuperacaoIntervencao(String nomMetodoRecuperacaoIntervencao) {
		this.nomMetodoRecuperacaoIntervencao = nomMetodoRecuperacaoIntervencao;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Transient
	public boolean isOutros(){
		if(nomMetodoRecuperacaoIntervencao.contains("Outros")){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideMetodoRecuperacaoIntervencao == null) ? 0
						: ideMetodoRecuperacaoIntervencao.hashCode());
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
		MetodoRecuperacaoIntervencao other = (MetodoRecuperacaoIntervencao) obj;
		if (ideMetodoRecuperacaoIntervencao == null) {
			if (other.ideMetodoRecuperacaoIntervencao != null)
				return false;
		} else if (!ideMetodoRecuperacaoIntervencao
				.equals(other.ideMetodoRecuperacaoIntervencao))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideMetodoRecuperacaoIntervencao);
	}
	
	

}