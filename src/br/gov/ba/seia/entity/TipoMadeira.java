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


/**
 * The persistent class for the tipo_madeira database table.
 * 
 */
@Entity
@Table(name="tipo_madeira")
@NamedQuery(name="TipoMadeira.findAll", query="SELECT t FROM TipoMadeira t")
public class TipoMadeira implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_MADEIRA_IDETIPOMADEIRA_GENERATOR", sequenceName="TIPO_MADEIRA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_MADEIRA_IDETIPOMADEIRA_GENERATOR")
	@Column(name="ide_tipo_madeira")
	private Integer ideTipoMadeira;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_tipo_madeira")
	private String nomTipoMadeira;

	//bi-directional many-to-one association to TipoCombustivelSiloArmazen
	@OneToMany(mappedBy="tipoMadeira")
	private List<TipoCombustivelSiloArmazen> tipoCombustivelSiloArmazens;

	public TipoMadeira() {
	}

	public Integer getIdeTipoMadeira() {
		return this.ideTipoMadeira;
	}

	public void setIdeTipoMadeira(Integer ideTipoMadeira) {
		this.ideTipoMadeira = ideTipoMadeira;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomTipoMadeira() {
		return this.nomTipoMadeira;
	}

	public void setNomTipoMadeira(String nomTipoMadeira) {
		this.nomTipoMadeira = nomTipoMadeira;
	}

	public List<TipoCombustivelSiloArmazen> getTipoCombustivelSiloArmazens() {
		return this.tipoCombustivelSiloArmazens;
	}

	public void setTipoCombustivelSiloArmazens(List<TipoCombustivelSiloArmazen> tipoCombustivelSiloArmazens) {
		this.tipoCombustivelSiloArmazens = tipoCombustivelSiloArmazens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoMadeira == null) ? 0 : ideTipoMadeira.hashCode());
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
		TipoMadeira other = (TipoMadeira) obj;
		if (ideTipoMadeira == null) {
			if (other.ideTipoMadeira != null)
				return false;
		} else if (!ideTipoMadeira.equals(other.ideTipoMadeira))
			return false;
		return true;
	}

	
}