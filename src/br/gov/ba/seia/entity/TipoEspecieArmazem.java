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
 * The persistent class for the tipo_especie_armazem database table.
 * 
 */
@Entity
@Table(name="tipo_especie_armazem")
@NamedQuery(name="TipoEspecieArmazem.findAll", query="SELECT t FROM TipoEspecieArmazem t")
public class TipoEspecieArmazem implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_ESPECIE_ARMAZEM_IDETIPOESPECIEARMAZEM_GENERATOR", sequenceName="TIPO_ESPECIE_ARMAZEM_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_ESPECIE_ARMAZEM_IDETIPOESPECIEARMAZEM_GENERATOR")
	@Column(name="ide_tipo_especie_armazem")
	private Integer ideTipoEspecieArmazem;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_tipo_especie_armazem")
	private String nomTipoEspecieArmazem;

	//bi-directional many-to-one association to TipoArmazem
	@OneToMany(mappedBy="tipoEspecieArmazem")
	private List<TipoArmazem> tipoArmazems;

	public TipoEspecieArmazem() {
	}

	public Integer getIdeTipoEspecieArmazem() {
		return this.ideTipoEspecieArmazem;
	}

	public void setIdeTipoEspecieArmazem(Integer ideTipoEspecieArmazem) {
		this.ideTipoEspecieArmazem = ideTipoEspecieArmazem;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomTipoEspecieArmazem() {
		return this.nomTipoEspecieArmazem;
	}

	public void setNomTipoEspecieArmazem(String nomTipoEspecieArmazem) {
		this.nomTipoEspecieArmazem = nomTipoEspecieArmazem;
	}

	public List<TipoArmazem> getTipoArmazems() {
		return this.tipoArmazems;
	}

	public void setTipoArmazems(List<TipoArmazem> tipoArmazems) {
		this.tipoArmazems = tipoArmazems;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoEspecieArmazem == null) ? 0 : ideTipoEspecieArmazem
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
		TipoEspecieArmazem other = (TipoEspecieArmazem) obj;
		if (ideTipoEspecieArmazem == null) {
			if (other.ideTipoEspecieArmazem != null)
				return false;
		} else if (!ideTipoEspecieArmazem.equals(other.ideTipoEspecieArmazem))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideTipoEspecieArmazem);
	}

	
}