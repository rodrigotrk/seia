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

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_armazem database table.
 * 
 */
@Entity
@Table(name="tipo_armazem")
@NamedQuery(name="TipoArmazem.findAll", query="SELECT t FROM TipoArmazem t")
public class TipoArmazem implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_ARMAZEM_IDETIPOARMAZEM_GENERATOR", sequenceName="TIPO_ARMAZEM_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_ARMAZEM_IDETIPOARMAZEM_GENERATOR")
	@Column(name="ide_tipo_armazem")
	private Integer ideTipoArmazem;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_tipo_armazem")
	private String nomTipoArmazem;

	//bi-directional many-to-one association to SilosArmazensUnidadeArmazenadora
	@OneToMany(mappedBy="tipoArmazem")
	private List<SilosArmazensUnidadeArmazenadora> silosArmazensUnidadeArmazenadoras;

	//bi-directional many-to-one association to TipoEspecieArmazem
	@ManyToOne
	@JoinColumn(name="ide_tipo_especie_armazem")
	private TipoEspecieArmazem tipoEspecieArmazem;

	public TipoArmazem() {
	}

	public Integer getIdeTipoArmazem() {
		return this.ideTipoArmazem;
	}

	public void setIdeTipoArmazem(Integer ideTipoArmazem) {
		this.ideTipoArmazem = ideTipoArmazem;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomTipoArmazem() {
		return this.nomTipoArmazem;
	}

	public void setNomTipoArmazem(String nomTipoArmazem) {
		this.nomTipoArmazem = nomTipoArmazem;
	}

	public List<SilosArmazensUnidadeArmazenadora> getSilosArmazensUnidadeArmazenadoras() {
		return this.silosArmazensUnidadeArmazenadoras;
	}

	public void setSilosArmazensUnidadeArmazenadoras(List<SilosArmazensUnidadeArmazenadora> silosArmazensUnidadeArmazenadoras) {
		this.silosArmazensUnidadeArmazenadoras = silosArmazensUnidadeArmazenadoras;
	}

	public TipoEspecieArmazem getTipoEspecieArmazem() {
		return this.tipoEspecieArmazem;
	}

	public void setTipoEspecieArmazem(TipoEspecieArmazem tipoEspecieArmazem) {
		this.tipoEspecieArmazem = tipoEspecieArmazem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoArmazem == null) ? 0 : ideTipoArmazem.hashCode());
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
		TipoArmazem other = (TipoArmazem) obj;
		if (ideTipoArmazem == null) {
			if (other.ideTipoArmazem != null)
				return false;
		} else if (!ideTipoArmazem.equals(other.ideTipoArmazem))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideTipoArmazem);
	}
	
	
}