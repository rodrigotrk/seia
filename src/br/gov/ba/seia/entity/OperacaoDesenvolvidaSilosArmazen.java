package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * The persistent class for the operacao_desenvolvida_silos_armazens database table.
 * 
 */
@Entity
@Table(name="operacao_desenvolvida_silos_armazens")
@NamedQuery(name="OperacaoDesenvolvidaSilosArmazen.findAll", query="SELECT o FROM OperacaoDesenvolvidaSilosArmazen o")
public class OperacaoDesenvolvidaSilosArmazen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="OPERACAO_DESENVOLVIDA_SILOS_ARMAZENS_IDEOPERACAODESENVOLVIDASILOSARMAZENS_GENERATOR", sequenceName="OPERACAO_DESENVOLVIDA_SILOS_ARMAZENS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="OPERACAO_DESENVOLVIDA_SILOS_ARMAZENS_IDEOPERACAODESENVOLVIDASILOSARMAZENS_GENERATOR")
	@Column(name="ide_operacao_desenvolvida_silos_armazens")
	private Integer ideOperacaoDesenvolvidaSilosArmazens;

	@Temporal(TemporalType.DATE)
	@Column(name="dtc_exclusao")
	private Date dtcExclusao;

	@Column(name="ide_operacao_desenvolvida_pai")
	private Integer ideOperacaoDesenvolvidaPai;

	@Column(name="ind_excluido")
	private Boolean indExcluido;

	@Column(name="nom_operacao_desevolvida")
	private String nomOperacaoDesevolvida;

	//bi-directional many-to-one association to SilosArmazensOperacaoDesenvolvida
	@OneToMany(mappedBy="operacaoDesenvolvidaSilosArmazen")
	private List<SilosArmazensOperacaoDesenvolvida> silosArmazensOperacaoDesenvolvidas;
	
	@Transient
	private List<OperacaoDesenvolvidaSilosArmazen> OperacaoDesenvolvidaSilosArmazenAuxiliar;
	
	@Transient
	private Boolean indSelecionado;

	public OperacaoDesenvolvidaSilosArmazen() {
	}

	public Integer getIdeOperacaoDesenvolvidaSilosArmazens() {
		return this.ideOperacaoDesenvolvidaSilosArmazens;
	}

	public void setIdeOperacaoDesenvolvidaSilosArmazens(Integer ideOperacaoDesenvolvidaSilosArmazens) {
		this.ideOperacaoDesenvolvidaSilosArmazens = ideOperacaoDesenvolvidaSilosArmazens;
	}

	public Date getDtcExclusao() {
		return this.dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Integer getIdeOperacaoDesenvolvidaPai() {
		return this.ideOperacaoDesenvolvidaPai;
	}

	public void setIdeOperacaoDesenvolvidaPai(Integer ideOperacaoDesenvolvidaPai) {
		this.ideOperacaoDesenvolvidaPai = ideOperacaoDesenvolvidaPai;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getNomOperacaoDesevolvida() {
		return this.nomOperacaoDesevolvida;
	}

	public void setNomOperacaoDesevolvida(String nomOperacaoDesevolvida) {
		this.nomOperacaoDesevolvida = nomOperacaoDesevolvida;
	}

	public List<SilosArmazensOperacaoDesenvolvida> getSilosArmazensOperacaoDesenvolvidas() {
		return this.silosArmazensOperacaoDesenvolvidas;
	}

	public void setSilosArmazensOperacaoDesenvolvidas(List<SilosArmazensOperacaoDesenvolvida> silosArmazensOperacaoDesenvolvidas) {
		this.silosArmazensOperacaoDesenvolvidas = silosArmazensOperacaoDesenvolvidas;
	}

	public List<OperacaoDesenvolvidaSilosArmazen> getOperacaoDesenvolvidaSilosArmazenAuxiliar() {
		return OperacaoDesenvolvidaSilosArmazenAuxiliar;
	}

	public void setOperacaoDesenvolvidaSilosArmazenAuxiliar(
			List<OperacaoDesenvolvidaSilosArmazen> operacaoDesenvolvidaSilosArmazenAuxiliar) {
		OperacaoDesenvolvidaSilosArmazenAuxiliar = operacaoDesenvolvidaSilosArmazenAuxiliar;
	}

	public Boolean getIndSelecionado() {
		return indSelecionado;
	}

	public void setIndSelecionado(Boolean indSelecionado) {
		this.indSelecionado = indSelecionado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideOperacaoDesenvolvidaSilosArmazens == null) ? 0
						: ideOperacaoDesenvolvidaSilosArmazens.hashCode());
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
		OperacaoDesenvolvidaSilosArmazen other = (OperacaoDesenvolvidaSilosArmazen) obj;
		if (ideOperacaoDesenvolvidaSilosArmazens == null) {
			if (other.ideOperacaoDesenvolvidaSilosArmazens != null)
				return false;
		} else if (!ideOperacaoDesenvolvidaSilosArmazens
				.equals(other.ideOperacaoDesenvolvidaSilosArmazens))
			return false;
		return true;
	}

	
}