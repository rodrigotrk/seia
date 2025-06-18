package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
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


/**
 * The persistent class for the tipo_combustivel_silo_armazens database table.
 * 
 */
@Entity
@Table(name="tipo_combustivel_silo_armazens")
@NamedQuery(name="TipoCombustivelSiloArmazen.findAll", query="SELECT t FROM TipoCombustivelSiloArmazen t")
public class TipoCombustivelSiloArmazen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_COMBUSTIVEL_SILO_ARMAZENS_IDETIPOCOMBUSTIVELSILOARMAZENS_GENERATOR", sequenceName="TIPO_COMBUSTIVEL_SILO_ARMAZENS_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_COMBUSTIVEL_SILO_ARMAZENS_IDETIPOCOMBUSTIVELSILOARMAZENS_GENERATOR")
	@Column(name="ide_tipo_combustivel_silo_armazens")
	private Integer ideTipoCombustivelSiloArmazens;

	@Column(name="nom_tipo_combustivel")
	private String nomTipoCombustivel;

	//bi-directional many-to-one association to SilosArmazensTipoCombustivel
	@OneToMany(mappedBy="tipoCombustivelSiloArmazen")
	private List<SilosArmazensTipoCombustivel> silosArmazensTipoCombustivels;

	//bi-directional many-to-one association to TipoMadeira
	@ManyToOne
	@JoinColumn(name="ide_tipo_madeira")
	private TipoMadeira tipoMadeira;
	
	@Column(name = "ide_tipo_combustivel_silo_armazens_pai")
	private Integer ideTipoCombustivelSiloArmazensPai;
	
	@Transient
	private List<TipoCombustivelSiloArmazen> tipoCombustivelSiloArmazensAuxiliar;
	
	@Transient
	private Boolean indNumRaf;
	
	@Transient
	private String numeroRaf;
	
	@Transient
	private Boolean indSelecionado;


	public TipoCombustivelSiloArmazen() {
	}

	public Integer getIdeTipoCombustivelSiloArmazens() {
		return this.ideTipoCombustivelSiloArmazens;
	}

	public void setIdeTipoCombustivelSiloArmazens(Integer ideTipoCombustivelSiloArmazens) {
		this.ideTipoCombustivelSiloArmazens = ideTipoCombustivelSiloArmazens;
	}

	public String getNomTipoCombustivel() {
		return this.nomTipoCombustivel;
	}

	public void setNomTipoCombustivel(String nomTipoCombustivel) {
		this.nomTipoCombustivel = nomTipoCombustivel;
	}

	public List<SilosArmazensTipoCombustivel> getSilosArmazensTipoCombustivels() {
		return this.silosArmazensTipoCombustivels;
	}

	public void setSilosArmazensTipoCombustivels(List<SilosArmazensTipoCombustivel> silosArmazensTipoCombustivels) {
		this.silosArmazensTipoCombustivels = silosArmazensTipoCombustivels;
	}

	public TipoMadeira getTipoMadeira() {
		return this.tipoMadeira;
	}

	public void setTipoMadeira(TipoMadeira tipoMadeira) {
		this.tipoMadeira = tipoMadeira;
	}

	public List<TipoCombustivelSiloArmazen> getTipoCombustivelSiloArmazensAuxiliar() {
		return tipoCombustivelSiloArmazensAuxiliar;
	}

	public void setTipoCombustivelSiloArmazensAuxiliar(
			List<TipoCombustivelSiloArmazen> tipoCombustivelSiloArmazensAuxiliar) {
		this.tipoCombustivelSiloArmazensAuxiliar = tipoCombustivelSiloArmazensAuxiliar;
	}

	public Boolean getIndNumRaf() {
		return indNumRaf;
	}

	public void setIndNumRaf(Boolean indNumRaf) {
		this.indNumRaf = indNumRaf;
	}

	public String getNumeroRaf() {
		return numeroRaf;
	}

	public void setNumeroRaf(String numeroRaf) {
		this.numeroRaf = numeroRaf;
	}

	public Integer getIdeTipoCombustivelSiloArmazensPai() {
		return ideTipoCombustivelSiloArmazensPai;
	}

	public void setIdeTipoCombustivelSiloArmazensPai(
			Integer ideTipoCombustivelSiloArmazensPai) {
		this.ideTipoCombustivelSiloArmazensPai = ideTipoCombustivelSiloArmazensPai;
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
				+ ((ideTipoCombustivelSiloArmazens == null) ? 0
						: ideTipoCombustivelSiloArmazens.hashCode());
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
		TipoCombustivelSiloArmazen other = (TipoCombustivelSiloArmazen) obj;
		if (ideTipoCombustivelSiloArmazens == null) {
			if (other.ideTipoCombustivelSiloArmazens != null)
				return false;
		} else if (!ideTipoCombustivelSiloArmazens
				.equals(other.ideTipoCombustivelSiloArmazens))
			return false;
		return true;
	}

	
}