package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


/**
 * The persistent class for the caracterizacao_ambiental_silos_armazens database table.
 * 
 */
@Entity
@Table(name="caracterizacao_ambiental_silos_armazens")
@NamedQuery(name="CaracterizacaoAmbientalSilosArmazen.findAll", query="SELECT c FROM CaracterizacaoAmbientalSilosArmazen c")
public class CaracterizacaoAmbientalSilosArmazen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CARACTERIZACAO_AMBIENTAL_SILOS_ARMAZENS_IDECARACTERIZACAOAMBIENTALSILOSARMAZENS_GENERATOR", sequenceName="CARACTERIZACAO_AMBIENTAL_SILOS_ARMAZENS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARACTERIZACAO_AMBIENTAL_SILOS_ARMAZENS_IDECARACTERIZACAOAMBIENTALSILOSARMAZENS_GENERATOR")
	@Column(name="ide_caracterizacao_ambiental_silos_armazens")
	private Integer ideCaracterizacaoAmbientalSilosArmazens;

	@Column(name="ind_lanca_efluente")
	private Boolean indLancaEfluente;

	@Column(name="ind_medida_controle_emissao")
	private Boolean indMedidaControleEmissao;

	@Column(name="ind_sistema_tratamento")
	private Boolean indSistemaTratamento;

	@Column(name="ind_utiliza_agua")
	private Boolean indUtilizaAgua;

	//bi-directional many-to-one association to CaracterizacaoAmbientalCaracterizacaoAtmosferica
	@OneToMany(mappedBy="caracterizacaoAmbientalSilosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<CaracterizacaoAmbientalCaracterizacaoAtmosferica> caracterizacaoAmbientalCaracterizacaoAtmosfericas;

	//bi-directional many-to-one association to CaracterizacaoAmbientalDestinacaoFinal
	@OneToMany(mappedBy="caracterizacaoAmbientalSilosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<CaracterizacaoAmbientalDestinacaoFinal> caracterizacaoAmbientalDestinacaoFinals;

	//bi-directional many-to-one association to CaracterizacaoAmbientalEquipamentoControle
	@OneToMany(mappedBy="caracterizacaoAmbientalSilosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<CaracterizacaoAmbientalEquipamentoControle> caracterizacaoAmbientalEquipamentoControles;

	//bi-directional many-to-one association to CaracterizacaoAmbientalMedidaControleEmissao
	@OneToMany(mappedBy="caracterizacaoAmbientalSilosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<CaracterizacaoAmbientalMedidaControleEmissao> caracterizacaoAmbientalMedidaControleEmissaos;

	//bi-directional many-to-one association to CaracterizacaoAmbientalOrigemAgua
	@OneToMany(mappedBy="caracterizacaoAmbientalSilosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<CaracterizacaoAmbientalOrigemAgua> caracterizacaoAmbientalOrigemAguas;

	//bi-directional many-to-one association to SilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_silos_armazens")
	private SilosArmazen silosArmazen;

	//bi-directional many-to-one association to ClassificacaoResiduoCaracterizacaoAmbiental
	@OneToMany(mappedBy="caracterizacaoAmbientalSilosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<ClassificacaoResiduoCaracterizacaoAmbiental> classificacaoResiduoCaracterizacaoAmbientals;

	//bi-directional many-to-one association to SilosArmazensLancamentoEfluente
	@OneToMany(mappedBy="caracterizacaoAmbientalSilosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<SilosArmazensLancamentoEfluente> silosArmazensLancamentoEfluentes;

	//bi-directional many-to-one association to SilosArmazensSistemaTratamentoAgua
	@OneToMany(mappedBy="caracterizacaoAmbientalSilosArmazen", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<SilosArmazensSistemaTratamentoAgua> silosArmazensSistemaTratamentoAguas;

	@Transient
	private List<SilosArmazensCaracterizacaoAtmosferica> silosArmazensCaracterizacaoAtmosfericas;

	@Transient
	private List<EquipamentoControle> equipamentoControles;
	
	@Transient
	private List<MedidaControleEmissao> medidaControleEmissaos;
	
	@Transient
	private List<SistemaTratamentoAgua> sistemaTratamentoAguas;
	
	@Transient
	private List<ClassificacaoResiduo> classificacaoResiduos;
	
	@Transient
	private List<DestinacaoFinal> destinacaoFinals;
	
	public CaracterizacaoAmbientalSilosArmazen() {
	}

	public Integer getIdeCaracterizacaoAmbientalSilosArmazens() {
		return this.ideCaracterizacaoAmbientalSilosArmazens;
	}

	public void setIdeCaracterizacaoAmbientalSilosArmazens(Integer ideCaracterizacaoAmbientalSilosArmazens) {
		this.ideCaracterizacaoAmbientalSilosArmazens = ideCaracterizacaoAmbientalSilosArmazens;
	}

	public Boolean getIndLancaEfluente() {
		return this.indLancaEfluente;
	}

	public void setIndLancaEfluente(Boolean indLancaEfluente) {
		this.indLancaEfluente = indLancaEfluente;
	}

	public Boolean getIndMedidaControleEmissao() {
		return this.indMedidaControleEmissao;
	}

	public void setIndMedidaControleEmissao(Boolean indMedidaControleEmissao) {
		this.indMedidaControleEmissao = indMedidaControleEmissao;
	}

	public Boolean getIndSistemaTratamento() {
		return this.indSistemaTratamento;
	}

	public void setIndSistemaTratamento(Boolean indSistemaTratamento) {
		this.indSistemaTratamento = indSistemaTratamento;
	}

	public Boolean getIndUtilizaAgua() {
		return this.indUtilizaAgua;
	}

	public void setIndUtilizaAgua(Boolean indUtilizaAgua) {
		this.indUtilizaAgua = indUtilizaAgua;
	}

	public List<CaracterizacaoAmbientalCaracterizacaoAtmosferica> getCaracterizacaoAmbientalCaracterizacaoAtmosfericas() {
		return this.caracterizacaoAmbientalCaracterizacaoAtmosfericas;
	}

	public void setCaracterizacaoAmbientalCaracterizacaoAtmosfericas(List<CaracterizacaoAmbientalCaracterizacaoAtmosferica> caracterizacaoAmbientalCaracterizacaoAtmosfericas) {
		this.caracterizacaoAmbientalCaracterizacaoAtmosfericas = caracterizacaoAmbientalCaracterizacaoAtmosfericas;
	}


	public List<CaracterizacaoAmbientalDestinacaoFinal> getCaracterizacaoAmbientalDestinacaoFinals() {
		return this.caracterizacaoAmbientalDestinacaoFinals;
	}

	public void setCaracterizacaoAmbientalDestinacaoFinals(List<CaracterizacaoAmbientalDestinacaoFinal> caracterizacaoAmbientalDestinacaoFinals) {
		this.caracterizacaoAmbientalDestinacaoFinals = caracterizacaoAmbientalDestinacaoFinals;
	}

	public List<CaracterizacaoAmbientalEquipamentoControle> getCaracterizacaoAmbientalEquipamentoControles() {
		return this.caracterizacaoAmbientalEquipamentoControles;
	}

	public void setCaracterizacaoAmbientalEquipamentoControles(List<CaracterizacaoAmbientalEquipamentoControle> caracterizacaoAmbientalEquipamentoControles) {
		this.caracterizacaoAmbientalEquipamentoControles = caracterizacaoAmbientalEquipamentoControles;
	}

	public List<CaracterizacaoAmbientalMedidaControleEmissao> getCaracterizacaoAmbientalMedidaControleEmissaos() {
		return this.caracterizacaoAmbientalMedidaControleEmissaos;
	}

	public void setCaracterizacaoAmbientalMedidaControleEmissaos(List<CaracterizacaoAmbientalMedidaControleEmissao> caracterizacaoAmbientalMedidaControleEmissaos) {
		this.caracterizacaoAmbientalMedidaControleEmissaos = caracterizacaoAmbientalMedidaControleEmissaos;
	}

	public List<CaracterizacaoAmbientalOrigemAgua> getCaracterizacaoAmbientalOrigemAguas() {
		return this.caracterizacaoAmbientalOrigemAguas;
	}

	public void setCaracterizacaoAmbientalOrigemAguas(List<CaracterizacaoAmbientalOrigemAgua> caracterizacaoAmbientalOrigemAguas) {
		this.caracterizacaoAmbientalOrigemAguas = caracterizacaoAmbientalOrigemAguas;
	}

	public SilosArmazen getSilosArmazen() {
		return this.silosArmazen;
	}

	public void setSilosArmazen(SilosArmazen silosArmazen) {
		this.silosArmazen = silosArmazen;
	}

	public List<ClassificacaoResiduoCaracterizacaoAmbiental> getClassificacaoResiduoCaracterizacaoAmbientals() {
		return this.classificacaoResiduoCaracterizacaoAmbientals;
	}

	public void setClassificacaoResiduoCaracterizacaoAmbientals(List<ClassificacaoResiduoCaracterizacaoAmbiental> classificacaoResiduoCaracterizacaoAmbientals) {
		this.classificacaoResiduoCaracterizacaoAmbientals = classificacaoResiduoCaracterizacaoAmbientals;
	}

	public List<SilosArmazensLancamentoEfluente> getSilosArmazensLancamentoEfluentes() {
		return this.silosArmazensLancamentoEfluentes;
	}

	public void setSilosArmazensLancamentoEfluentes(List<SilosArmazensLancamentoEfluente> silosArmazensLancamentoEfluentes) {
		this.silosArmazensLancamentoEfluentes = silosArmazensLancamentoEfluentes;
	}

	public List<SilosArmazensSistemaTratamentoAgua> getSilosArmazensSistemaTratamentoAguas() {
		return this.silosArmazensSistemaTratamentoAguas;
	}

	public void setSilosArmazensSistemaTratamentoAguas(List<SilosArmazensSistemaTratamentoAgua> silosArmazensSistemaTratamentoAguas) {
		this.silosArmazensSistemaTratamentoAguas = silosArmazensSistemaTratamentoAguas;
	}

	public List<SilosArmazensCaracterizacaoAtmosferica> getSilosArmazensCaracterizacaoAtmosfericas() {
		return silosArmazensCaracterizacaoAtmosfericas;
	}

	public void setSilosArmazensCaracterizacaoAtmosfericas(
			List<SilosArmazensCaracterizacaoAtmosferica> silosArmazensCaracterizacaoAtmosfericas) {
		this.silosArmazensCaracterizacaoAtmosfericas = silosArmazensCaracterizacaoAtmosfericas;
	}

	public List<EquipamentoControle> getEquipamentoControles() {
		return equipamentoControles;
	}

	public void setEquipamentoControles(List<EquipamentoControle> equipamentoControles) {
		this.equipamentoControles = equipamentoControles;
	}

	public List<MedidaControleEmissao> getMedidaControleEmissaos() {
		return medidaControleEmissaos;
	}

	public void setMedidaControleEmissaos(List<MedidaControleEmissao> medidaControleEmissaos) {
		this.medidaControleEmissaos = medidaControleEmissaos;
	}

	public List<SistemaTratamentoAgua> getSistemaTratamentoAguas() {
		return sistemaTratamentoAguas;
	}

	public void setSistemaTratamentoAguas(List<SistemaTratamentoAgua> sistemaTratamentoAguas) {
		this.sistemaTratamentoAguas = sistemaTratamentoAguas;
	}

	public List<ClassificacaoResiduo> getClassificacaoResiduos() {
		return classificacaoResiduos;
	}

	public void setClassificacaoResiduos(List<ClassificacaoResiduo> classificacaoResiduos) {
		this.classificacaoResiduos = classificacaoResiduos;
	}

	public List<DestinacaoFinal> getDestinacaoFinals() {
		return destinacaoFinals;
	}
	
	public void setDestinacaoFinals(List<DestinacaoFinal> destinacaoFinals) {
		this.destinacaoFinals = destinacaoFinals;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCaracterizacaoAmbientalSilosArmazens == null) ? 0
						: ideCaracterizacaoAmbientalSilosArmazens.hashCode());
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
		CaracterizacaoAmbientalSilosArmazen other = (CaracterizacaoAmbientalSilosArmazen) obj;
		if (ideCaracterizacaoAmbientalSilosArmazens == null) {
			if (other.ideCaracterizacaoAmbientalSilosArmazens != null)
				return false;
		} else if (!ideCaracterizacaoAmbientalSilosArmazens
				.equals(other.ideCaracterizacaoAmbientalSilosArmazens))
			return false;
		return true;
	}

	
}