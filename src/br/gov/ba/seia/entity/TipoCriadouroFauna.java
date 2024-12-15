package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_criadouro_fauna")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoCriadouroFauna.findAll", query = "SELECT t FROM TipoCriadouroFauna t"),
		@NamedQuery(name = "TipoCriadouroFauna.findByIdeFauna", query = "SELECT t FROM TipoCriadouroFauna t INNER JOIN t.faunaCollection f WHERE f.ideFauna = :ideFauna"),
		@NamedQuery(name = "TipoCriadouroFauna.findByIdeTipoCriadouroFauna", query = "SELECT t FROM TipoCriadouroFauna t WHERE t.ideTipoCriadouroFauna = :ideTipoCriadouroFauna")})
public class TipoCriadouroFauna implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_criadouro_fauna", nullable = false)
	private Integer ideTipoCriadouroFauna;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "nom_tipo_criadouro_fauna")
	private String nomTipoCriadouroFauna;

	@Basic(optional = false)
	@Column(name = "ind_ativo")
	private boolean indAtivo;

	@ManyToMany(mappedBy = "tipoCriadouroFaunaCollection", fetch = FetchType.LAZY)
	private Collection<Fauna> faunaCollection;
	
	public TipoCriadouroFauna() {
		super();
	}

	public Integer getIdeTipoCriadouroFauna() {
		return ideTipoCriadouroFauna;
	}

	public void setIdeTipoCriadouroFauna(Integer ideTipoCriadouroFauna) {
		this.ideTipoCriadouroFauna = ideTipoCriadouroFauna;
	}

	public String getNomTipoCriadouroFauna() {
		return nomTipoCriadouroFauna;
	}

	public void setNomTipoCriadouroFauna(String nomTipoCriadouroFauna) {
		this.nomTipoCriadouroFauna = nomTipoCriadouroFauna;
	}

	public boolean isIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Collection<Fauna> getFaunaCollection() {
		return faunaCollection;
	}

	public void setFaunaCollection(Collection<Fauna> faunaCollection) {
		this.faunaCollection = faunaCollection;
	}
	
	@Override
	public Long getId() {
		return new Long(this.ideTipoCriadouroFauna);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoCriadouroFauna == null) ? 0 : ideTipoCriadouroFauna
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
		TipoCriadouroFauna other = (TipoCriadouroFauna) obj;
		if (ideTipoCriadouroFauna == null) {
			if (other.ideTipoCriadouroFauna != null)
				return false;
		} else if (!ideTipoCriadouroFauna.equals(other.ideTipoCriadouroFauna))
			return false;
		return true;
	}
}