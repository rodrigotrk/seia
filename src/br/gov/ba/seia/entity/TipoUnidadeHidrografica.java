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

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_unidade_hidrografica", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cod_tipo_unidade_hidrografica"}),
    @UniqueConstraint(columnNames = {"nom_tipo_unidade_hidrografica"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoUnidadeHidrografica.findAll", query = "SELECT t FROM TipoUnidadeHidrografica t"),
    @NamedQuery(name = "TipoUnidadeHidrografica.findByIdeTipoUnidadeHidrografica", query = "SELECT t FROM TipoUnidadeHidrografica t WHERE t.ideTipoUnidadeHidrografica = :ideTipoUnidadeHidrografica"),
    @NamedQuery(name = "TipoUnidadeHidrografica.findByCodTipoUnidadeHidrografica", query = "SELECT t FROM TipoUnidadeHidrografica t WHERE t.codTipoUnidadeHidrografica = :codTipoUnidadeHidrografica"),
    @NamedQuery(name = "TipoUnidadeHidrografica.findByNomTipoUnidadeHidrografica", query = "SELECT t FROM TipoUnidadeHidrografica t WHERE t.nomTipoUnidadeHidrografica = :nomTipoUnidadeHidrografica")})
public class TipoUnidadeHidrografica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_unidade_hidrografica", nullable = false)
    private Integer ideTipoUnidadeHidrografica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "cod_tipo_unidade_hidrografica", nullable = false, length = 3)
    private String codTipoUnidadeHidrografica;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_tipo_unidade_hidrografica", nullable = false, length = 50)
    private String nomTipoUnidadeHidrografica;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoUnidadeHidrografica", fetch = FetchType.LAZY)
    private Collection<UnidadeHidrografica> unidadeHidrograficaCollection;

    public TipoUnidadeHidrografica() {
    }

    public TipoUnidadeHidrografica(Integer ideTipoUnidadeHidrografica) {
        this.ideTipoUnidadeHidrografica = ideTipoUnidadeHidrografica;
    }

    public TipoUnidadeHidrografica(Integer ideTipoUnidadeHidrografica, String codTipoUnidadeHidrografica, String nomTipoUnidadeHidrografica) {
        this.ideTipoUnidadeHidrografica = ideTipoUnidadeHidrografica;
        this.codTipoUnidadeHidrografica = codTipoUnidadeHidrografica;
        this.nomTipoUnidadeHidrografica = nomTipoUnidadeHidrografica;
    }

    public Integer getIdeTipoUnidadeHidrografica() {
        return ideTipoUnidadeHidrografica;
    }

    public void setIdeTipoUnidadeHidrografica(Integer ideTipoUnidadeHidrografica) {
        this.ideTipoUnidadeHidrografica = ideTipoUnidadeHidrografica;
    }

    public String getCodTipoUnidadeHidrografica() {
        return codTipoUnidadeHidrografica;
    }

    public void setCodTipoUnidadeHidrografica(String codTipoUnidadeHidrografica) {
        this.codTipoUnidadeHidrografica = codTipoUnidadeHidrografica;
    }

    public String getNomTipoUnidadeHidrografica() {
        return nomTipoUnidadeHidrografica;
    }

    public void setNomTipoUnidadeHidrografica(String nomTipoUnidadeHidrografica) {
        this.nomTipoUnidadeHidrografica = nomTipoUnidadeHidrografica;
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
        hash += (ideTipoUnidadeHidrografica != null ? ideTipoUnidadeHidrografica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoUnidadeHidrografica)) {
            return false;
        }
        TipoUnidadeHidrografica other = (TipoUnidadeHidrografica) object;
        if ((this.ideTipoUnidadeHidrografica == null && other.ideTipoUnidadeHidrografica != null) || (this.ideTipoUnidadeHidrografica != null && !this.ideTipoUnidadeHidrografica.equals(other.ideTipoUnidadeHidrografica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoUnidadeHidrografica[ ideTipoUnidadeHidrografica=" + ideTipoUnidadeHidrografica + " ]";
    }
    
}
