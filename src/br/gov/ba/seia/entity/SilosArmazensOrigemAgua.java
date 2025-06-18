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
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the silos_armazens_origem_agua database table.
 * 
 */
@Entity
@Table(name="silos_armazens_origem_agua")
@NamedQuery(name="SilosArmazensOrigemAgua.findAll", query="SELECT s FROM SilosArmazensOrigemAgua s")
public class SilosArmazensOrigemAgua implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SILOS_ARMAZENS_ORIGEM_AGUA_IDESILOSARMAZENSORIGEMAGUA_GENERATOR", sequenceName="SILOS_ARMAZENS_ORIGEM_AGUA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SILOS_ARMAZENS_ORIGEM_AGUA_IDESILOSARMAZENSORIGEMAGUA_GENERATOR")
	@Column(name="ide_silos_armazens_origem_agua")
	private Integer ideSilosArmazensOrigemAgua;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_origem_agua")
	private String nomOrigemAgua;

	//bi-directional many-to-one association to CaracterizacaoAmbientalOrigemAgua
	@OneToMany(mappedBy="silosArmazensOrigemAgua")
	private List<CaracterizacaoAmbientalOrigemAgua> caracterizacaoAmbientalOrigemAguas;

	@Transient
	private boolean indSelecionado;
	
	@Transient
	private String numDocumento;
	
	@Transient
	private List<TipoConcessionaria> tipoConcessionarias;
	
	public SilosArmazensOrigemAgua() {
	}

	public Integer getIdeSilosArmazensOrigemAgua() {
		return this.ideSilosArmazensOrigemAgua;
	}

	public void setIdeSilosArmazensOrigemAgua(Integer ideSilosArmazensOrigemAgua) {
		this.ideSilosArmazensOrigemAgua = ideSilosArmazensOrigemAgua;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomOrigemAgua() {
		return this.nomOrigemAgua;
	}

	public void setNomOrigemAgua(String nomOrigemAgua) {
		this.nomOrigemAgua = nomOrigemAgua;
	}

	public List<CaracterizacaoAmbientalOrigemAgua> getCaracterizacaoAmbientalOrigemAguas() {
		return this.caracterizacaoAmbientalOrigemAguas;
	}

	public void setCaracterizacaoAmbientalOrigemAguas(List<CaracterizacaoAmbientalOrigemAgua> caracterizacaoAmbientalOrigemAguas) {
		this.caracterizacaoAmbientalOrigemAguas = caracterizacaoAmbientalOrigemAguas;
	}

	public boolean isIndSelecionado() {
		return indSelecionado;
	}

	public void setIndSelecionado(boolean indSelecionado) {
		this.indSelecionado = indSelecionado;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public List<TipoConcessionaria> getTipoConcessionarias() {
		return tipoConcessionarias;
	}

	public void setTipoConcessionarias(List<TipoConcessionaria> tipoConcessionarias) {
		this.tipoConcessionarias = tipoConcessionarias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSilosArmazensOrigemAgua == null) ? 0
						: ideSilosArmazensOrigemAgua.hashCode());
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
		SilosArmazensOrigemAgua other = (SilosArmazensOrigemAgua) obj;
		if (ideSilosArmazensOrigemAgua == null) {
			if (other.ideSilosArmazensOrigemAgua != null)
				return false;
		} else if (!ideSilosArmazensOrigemAgua
				.equals(other.ideSilosArmazensOrigemAgua))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideSilosArmazensOrigemAgua);
	}

	

}