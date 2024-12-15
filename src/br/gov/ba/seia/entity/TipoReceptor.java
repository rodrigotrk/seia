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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_receptor", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_receptor"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoReceptor.findAll", query = "SELECT t FROM TipoReceptor t order by t.nomTipoReceptor"),
    @NamedQuery(name = "TipoReceptor.findByIdeTipoReceptor", query = "SELECT t FROM TipoReceptor t WHERE t.ideTipoReceptor = :ideTipoReceptor"),
    @NamedQuery(name = "TipoReceptor.findByNomTipoReceptor", query = "SELECT t FROM TipoReceptor t WHERE t.nomTipoReceptor = :nomTipoReceptor"),
    @NamedQuery(name = "TipoReceptor.findTipoReceptorByRequerimentoUnico", query = "SELECT t FROM TipoReceptor t left join t.requerimentoUnicoCollection ru WHERE ru.ideRequerimentoUnico = :ideRequerimentoUnico")})
public class TipoReceptor implements Serializable, BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_receptor", nullable = false)
    private Integer ideTipoReceptor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nom_tipo_receptor", nullable = false, length = 25)
    private String nomTipoReceptor;
    @ManyToMany(mappedBy = "tipoReceptorCollection", fetch = FetchType.LAZY)
    private Collection<RequerimentoUnico> requerimentoUnicoCollection;

    public TipoReceptor() {
    }

    public TipoReceptor(Integer ideTipoReceptor) {
        this.ideTipoReceptor = ideTipoReceptor;
    }

    public TipoReceptor(Integer ideTipoReceptor, String nomTipoReceptor) {
        this.ideTipoReceptor = ideTipoReceptor;
        this.nomTipoReceptor = nomTipoReceptor;
    }

    public Integer getIdeTipoReceptor() {
        return ideTipoReceptor;
    }

    public void setIdeTipoReceptor(Integer ideTipoReceptor) {
        this.ideTipoReceptor = ideTipoReceptor;
    }

    public String getNomTipoReceptor() {
        return nomTipoReceptor;
    }

    public void setNomTipoReceptor(String nomTipoReceptor) {
        this.nomTipoReceptor = nomTipoReceptor;
    }

    public Collection<RequerimentoUnico> getRequerimentoUnicoCollection() {
		return requerimentoUnicoCollection;
	}

	public void setRequerimentoUnicoCollection(
			Collection<RequerimentoUnico> requerimentoUnicoCollection) {
		this.requerimentoUnicoCollection = requerimentoUnicoCollection;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoReceptor != null ? ideTipoReceptor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoReceptor)) {
            return false;
        }
        TipoReceptor other = (TipoReceptor) object;
        if ((this.ideTipoReceptor == null && other.ideTipoReceptor != null) || (this.ideTipoReceptor != null && !this.ideTipoReceptor.equals(other.ideTipoReceptor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoReceptor[ ideTipoReceptor=" + ideTipoReceptor + " ]";
    }

	@Override
	public Long getId() {
		return new Long(this.ideTipoReceptor);
	}
}
