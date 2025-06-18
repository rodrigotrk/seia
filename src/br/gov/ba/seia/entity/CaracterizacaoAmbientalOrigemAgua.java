package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


/**
 * The persistent class for the caracterizacao_ambiental_origem_agua database table.
 * 
 */
@Entity
@Table(name="caracterizacao_ambiental_origem_agua")
@NamedQueries({
	@NamedQuery(name="CaracterizacaoAmbientalOrigemAgua.removerOrigemAgua", query="DELETE FROM CaracterizacaoAmbientalOrigemAgua s WHERE s.caracterizacaoAmbientalSilosArmazen = :ideCaracterizacaoAmbientalSilosArmazen")	
})
public class CaracterizacaoAmbientalOrigemAgua implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CARACTERIZACAO_AMBIENTAL_ORIGEM_AGUA_IDECARACTERIZACAOAMBIENTALORIGEMAGUA_GENERATOR", sequenceName="CARACTERIZACAO_AMBIENTAL_ORIGEM_AGUA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CARACTERIZACAO_AMBIENTAL_ORIGEM_AGUA_IDECARACTERIZACAOAMBIENTALORIGEMAGUA_GENERATOR")
	@Column(name="ide_caracterizacao_ambiental_origem_agua")
	private Integer ideCaracterizacaoAmbientalOrigemAgua;

	@Column(name="num_documento_dispensa")
	private String numDocumentoDispensa;

	@Column(name="num_documento_portaria")
	private String numDocumentoPortaria;

	//bi-directional many-to-one association to CaracterizacaoAmbientalSilosArmazen
	@ManyToOne
	@JoinColumn(name="ide_caracterizacao_ambiental_silos_armazens")
	private CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen;

	//bi-directional many-to-one association to SilosArmazensOrigemAgua
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ide_silos_armazens_origem_agua")
	private SilosArmazensOrigemAgua silosArmazensOrigemAgua;

	//bi-directional many-to-one association to OrigemAguaTipoConcessionaria
	@OneToMany(mappedBy="caracterizacaoAmbientalOrigemAgua", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<OrigemAguaTipoConcessionaria> origemAguaTipoConcessionarias;
	
	@Transient
	private List<SilosArmazensOrigemAgua> silosArmazensOrigemAguaList;
	
	@Transient
	private Boolean indSelecionado;
	
	@Transient
	private List<TipoConcessionaria> tipoConcessionariasList; 

	public CaracterizacaoAmbientalOrigemAgua() {
		this.silosArmazensOrigemAguaList = new ArrayList<SilosArmazensOrigemAgua>();
	}

	public Integer getIdeCaracterizacaoAmbientalOrigemAgua() {
		return this.ideCaracterizacaoAmbientalOrigemAgua;
	}

	public void setIdeCaracterizacaoAmbientalOrigemAgua(Integer ideCaracterizacaoAmbientalOrigemAgua) {
		this.ideCaracterizacaoAmbientalOrigemAgua = ideCaracterizacaoAmbientalOrigemAgua;
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

	public SilosArmazensOrigemAgua getSilosArmazensOrigemAgua() {
		return this.silosArmazensOrigemAgua;
	}

	public void setSilosArmazensOrigemAgua(SilosArmazensOrigemAgua silosArmazensOrigemAgua) {
		this.silosArmazensOrigemAgua = silosArmazensOrigemAgua;
	}

	public List<OrigemAguaTipoConcessionaria> getOrigemAguaTipoConcessionarias() {
		return this.origemAguaTipoConcessionarias;
	}

	public void setOrigemAguaTipoConcessionarias(List<OrigemAguaTipoConcessionaria> origemAguaTipoConcessionarias) {
		this.origemAguaTipoConcessionarias = origemAguaTipoConcessionarias;
	}

	public List<SilosArmazensOrigemAgua> getSilosArmazensOrigemAguaList() {
		return silosArmazensOrigemAguaList;
	}
	
	public void setSilosArmazensOrigemAguaList(
			List<SilosArmazensOrigemAgua> silosArmazensOrigemAguaList) {
		this.silosArmazensOrigemAguaList = silosArmazensOrigemAguaList;
	}
	
	public Boolean getIndSelecionado() {
		return indSelecionado;
	}

	public void setIndSelecionado(Boolean indSelecionado) {
		this.indSelecionado = indSelecionado;
	}

	public List<TipoConcessionaria> getTipoConcessionariasList() {
		return tipoConcessionariasList;
	}

	public void setTipoConcessionariasList(
			List<TipoConcessionaria> tipoConcessionariasList) {
		this.tipoConcessionariasList = tipoConcessionariasList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCaracterizacaoAmbientalOrigemAgua == null) ? 0
						: ideCaracterizacaoAmbientalOrigemAgua.hashCode());
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
		CaracterizacaoAmbientalOrigemAgua other = (CaracterizacaoAmbientalOrigemAgua) obj;
		if (ideCaracterizacaoAmbientalOrigemAgua == null) {
			if (other.ideCaracterizacaoAmbientalOrigemAgua != null)
				return false;
		} else if (!ideCaracterizacaoAmbientalOrigemAgua
				.equals(other.ideCaracterizacaoAmbientalOrigemAgua))
			return false;
		return true;
	}

}