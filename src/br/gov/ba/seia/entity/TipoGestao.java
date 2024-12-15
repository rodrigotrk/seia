package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_gestao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_gestao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoGestao.findAll", query = "SELECT t FROM TipoGestao t"),
    @NamedQuery(name = "TipoGestao.findByIdeTipoGestao", query = "SELECT t FROM TipoGestao t WHERE t.ideTipoGestao = :ideTipoGestao"),
    @NamedQuery(name = "TipoGestao.findByNomTipoGestao", query = "SELECT t FROM TipoGestao t WHERE t.nomTipoGestao = :nomTipoGestao")})
public class TipoGestao implements Serializable, BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_gestao", nullable = false)
    private Integer ideTipoGestao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_tipo_gestao", nullable = false, length = 20)
    private String nomTipoGestao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoGestao", fetch = FetchType.LAZY)
    private Collection<UnidadeHidrografica> unidadeHidrograficaCollection;

    public TipoGestao() {
    }

    public TipoGestao(Integer ideTipoGestao) {
        this.ideTipoGestao = ideTipoGestao;
    }

    public TipoGestao(Integer ideTipoGestao, String nomTipoGestao) {
        this.ideTipoGestao = ideTipoGestao;
        this.nomTipoGestao = nomTipoGestao;
    }

    public Integer getIdeTipoGestao() {
        return ideTipoGestao;
    }

    public void setIdeTipoGestao(Integer ideTipoGestao) {
        this.ideTipoGestao = ideTipoGestao;
    }

    public String getNomTipoGestao() {
        return nomTipoGestao;
    }

    public void setNomTipoGestao(String nomTipoGestao) {
        this.nomTipoGestao = nomTipoGestao;
    }

    @XmlTransient
    public Collection<UnidadeHidrografica> getUnidadeHidrograficaCollection() {
        return unidadeHidrograficaCollection;
    }

    public void setUnidadeHidrograficaCollection(Collection<UnidadeHidrografica> unidadeHidrograficaCollection) {
        this.unidadeHidrograficaCollection = unidadeHidrograficaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoGestao != null ? ideTipoGestao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoGestao)) {
            return false;
        }
        TipoGestao other = (TipoGestao) object;
        if ((this.ideTipoGestao == null && other.ideTipoGestao != null) || (this.ideTipoGestao != null && !this.ideTipoGestao.equals(other.ideTipoGestao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoGestao[ ideTipoGestao=" + ideTipoGestao + " ]";
    }

	@Override
	public Long getId() {
		return new Long(this.ideTipoGestao);
	}
    
}
