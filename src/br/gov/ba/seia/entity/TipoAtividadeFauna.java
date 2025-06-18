package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * 
 * @author joao.maciel
 * 
 */

@Entity
@Table(name = "tipo_atividade_fauna")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoAtividadeFauna.findAll", query = "SELECT t FROM TipoAtividadeFauna t"),
		@NamedQuery(name = "TipoAtividadeFauna.findByIdeFauna", query = "SELECT t FROM TipoAtividadeFauna t INNER JOIN t.faunaCollection f WHERE f.ideFauna = :ideFauna"),
		@NamedQuery(name = "TipoAtividadeFauna.findByIdeTipoAtividadeFauna", query = "SELECT t FROM TipoAtividadeFauna t WHERE t.ideTipoAtividadeFauna = :ideTipoAtividadeFauna"),
		@NamedQuery(name = "TipoAtividadeFauna.findByNoTipoAtividadeFaunao", query = "SELECT t FROM TipoAtividadeFauna t WHERE t.nomTipoAtividadeFauna = :nomTipoAtividadeFauna") })
public class TipoAtividadeFauna implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "tipo_atividade_fauna_ide_tipo_atividade_fauna_generator", sequenceName = "tipo_atividade_fauna_ide_tipo_atividade_fauna_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_atividade_fauna_ide_tipo_atividade_fauna_generator")
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_atividade_fauna", nullable = false)
	private Integer ideTipoAtividadeFauna;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "nom_tipo_atividade_fauna")
	private String nomTipoAtividadeFauna;

	@Basic(optional = false)
	@Column(name = "ind_ativo")
	private boolean indAtivo;

	@ManyToMany(mappedBy = "tipoAtividadeFaunaCollection", fetch = FetchType.LAZY)
	private Collection<Fauna> faunaCollection;

	public boolean isIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public TipoAtividadeFauna(Integer ideTipoAtividadeFauna, String nomTipoAtividadeFauna) {
		this.ideTipoAtividadeFauna = ideTipoAtividadeFauna;
		this.nomTipoAtividadeFauna = nomTipoAtividadeFauna;
	}

	public TipoAtividadeFauna() {
	}

	public Integer getIdeTipoAtividadeFauna() {
		return ideTipoAtividadeFauna;
	}

	public void setIdeTipoAtividadeFauna(Integer ideTipoAtividadeFauna) {
		this.ideTipoAtividadeFauna = ideTipoAtividadeFauna;
	}

	public String getNomTipoAtividadeFauna() {
		return nomTipoAtividadeFauna;
	}

	public void setNomTipoAtividadeFauna(String nomTipoAtividadeFauna) {
		this.nomTipoAtividadeFauna = nomTipoAtividadeFauna;
	}

	@XmlTransient
	public Collection<Fauna> getFaunaCollection() {
		return faunaCollection;
	}

	public void setFaunaCollection(Collection<Fauna> faunaCollection) {
		this.faunaCollection = faunaCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoAtividadeFauna == null) ? 0 : ideTipoAtividadeFauna.hashCode());
		result = prime * result + (indAtivo ? 1231 : 1237);
		result = prime * result + ((nomTipoAtividadeFauna == null) ? 0 : nomTipoAtividadeFauna.hashCode());
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
		TipoAtividadeFauna other = (TipoAtividadeFauna) obj;
		if (ideTipoAtividadeFauna == null) {
			if (other.ideTipoAtividadeFauna != null)
				return false;
		} else if (!ideTipoAtividadeFauna.equals(other.ideTipoAtividadeFauna))
			return false;
		if (indAtivo != other.indAtivo)
			return false;
		if (nomTipoAtividadeFauna == null) {
			if (other.nomTipoAtividadeFauna != null)
				return false;
		} else if (!nomTipoAtividadeFauna.equals(other.nomTipoAtividadeFauna))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoAtividadeFauna);
	}

	@Override
	public String toString() {
		return "entity.TipoAtividadeFauna[ ideTipoAtividadeFauna=" + ideTipoAtividadeFauna + " ]";
	}

}