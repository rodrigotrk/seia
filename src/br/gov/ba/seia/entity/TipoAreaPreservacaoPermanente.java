package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * 
 * @author Eduardo Fernandes
 */
@Entity
@Table(name = "tipo_area_preservacao_permanente")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "TipoAreaPreservacaoPermanente.findAll", query = "SELECT t FROM TipoAreaPreservacaoPermanente t"),
		@NamedQuery(name = "TipoAreaPreservacaoPermanente.findByIdeRequerimento", query = "SELECT t FROM TipoAreaPreservacaoPermanente t INNER JOIN t.requerimentoCollection l WHERE l.ideRequerimento = :ideRequerimento"),
		@NamedQuery(name = "TipoAreaPreservacaoPermanente.findByIdeTipoAreaPreservacaoPermanente", query = "SELECT t FROM TipoAreaPreservacaoPermanente t WHERE t.ideTipoAreaPreservacaoPermanente = :ideTipoAreaPreservacaoPermanente"),
		@NamedQuery(name = "TipoAreaPreservacaoPermanente.findByNomTipoAreaPreservacaoPermanente", query = "SELECT t FROM TipoAreaPreservacaoPermanente t WHERE t.nomTipoAreaPreservacaoPermanente = :nomTipoAreaPreservacaoPermanente"),
		@NamedQuery(name = "TipoAreaPreservacaoPermanente.findByIndAtivo", query = "SELECT t FROM TipoAreaPreservacaoPermanente t WHERE t.indAtivo = :indAtivo"),
		@NamedQuery(name = "Licenca.removeByIdeTipoAreaPreservacaoPermanente", query = "DELETE FROM TipoAreaPreservacaoPermanente t WHERE t.ideTipoAreaPreservacaoPermanente = :ideTipoAreaPreservacaoPermanente") })
public class TipoAreaPreservacaoPermanente implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;
	@Id
	@NotNull
	@Column(name = "ide_tipo_area_preservacao_permanente", nullable = false)
	private Integer ideTipoAreaPreservacaoPermanente;

	@NotNull
	@Column(name = "nom_tipo_area_preservacao_permanente", nullable = false, length = 50)
	private String nomTipoAreaPreservacaoPermanente;

	@JoinColumn(name = "ide_tipo_area_protegida", referencedColumnName = "ide_tipo_area_protegida")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoAreaProtegida ideTipoAreaProtegida;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	@ManyToMany(mappedBy = "tipoAreaPreservacaoPermanenteCollection", fetch = FetchType.LAZY)
	private Collection<Requerimento> requerimentoCollection;

	public TipoAreaPreservacaoPermanente() {
	}

	public TipoAreaPreservacaoPermanente(Integer ideTipoArea) {
		this.ideTipoAreaPreservacaoPermanente = ideTipoArea;
	}

	public Integer getIdeTipoAreaPreservacaoPermanente() {
		return ideTipoAreaPreservacaoPermanente;
	}

	public void setIdeTipoAreaPreservacaoPermanente(Integer ideTipoAreaPreservacaoPermanente) {
		this.ideTipoAreaPreservacaoPermanente = ideTipoAreaPreservacaoPermanente;
	}

	public String getNomTipoAreaPreservacaoPermanente() {
		return nomTipoAreaPreservacaoPermanente;
	}

	public void setNomTipoAreaPreservacaoPermanente(String dscTipoAreaPreservacaoPermanente) {
		this.nomTipoAreaPreservacaoPermanente = dscTipoAreaPreservacaoPermanente;
	}

	public TipoAreaProtegida getTipoAreaProtegida() {
		return ideTipoAreaProtegida;
	}

	public void setTipoAreaProtegida(TipoAreaProtegida tipoAreaProtegida) {
		this.ideTipoAreaProtegida = tipoAreaProtegida;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@XmlTransient
	public Collection<Requerimento> getRequerimentoCollection() {
		return requerimentoCollection;
	}

	public void setRequerimentoCollection(Collection<Requerimento> requerimentoCollection) {
		this.requerimentoCollection = requerimentoCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideTipoAreaPreservacaoPermanente != null ? ideTipoAreaPreservacaoPermanente.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof TipoAreaPreservacaoPermanente)) {
			return false;
		}
		TipoAreaPreservacaoPermanente other = (TipoAreaPreservacaoPermanente) object;
		if ((this.ideTipoAreaPreservacaoPermanente == null && other.ideTipoAreaPreservacaoPermanente != null)
				|| (this.ideTipoAreaPreservacaoPermanente != null && !this.ideTipoAreaPreservacaoPermanente
						.equals(other.ideTipoAreaPreservacaoPermanente))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.TipoAto[ ideTipoAreaPreservacaoPermanente=" + ideTipoAreaPreservacaoPermanente
				+ " ]";
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoAreaPreservacaoPermanente);
	}

}
