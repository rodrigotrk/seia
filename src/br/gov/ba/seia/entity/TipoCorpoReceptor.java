package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_corpo_receptor", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_corpo_receptor"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoCorpoReceptor.findAll", query = "SELECT t FROM TipoCorpoReceptor t"),
    @NamedQuery(name = "TipoCorpoReceptor.findByIdeCorpoReceptor", query = "SELECT t FROM TipoCorpoReceptor t WHERE t.ideCorpoReceptor = :ideCorpoReceptor"),
    @NamedQuery(name = "TipoCorpoReceptor.findByNomCorpoReceptor", query = "SELECT t FROM TipoCorpoReceptor t WHERE t.nomCorpoReceptor = :nomCorpoReceptor")})
public class TipoCorpoReceptor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_corpo_receptor", nullable = false)
    private Integer ideCorpoReceptor;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nom_corpo_receptor", nullable = false, length = 60)
    private String nomCorpoReceptor;

    public TipoCorpoReceptor() {
    }

    public TipoCorpoReceptor(Integer ideCorpoReceptor) {
        this.ideCorpoReceptor = ideCorpoReceptor;
    }

    public TipoCorpoReceptor(Integer ideCorpoReceptor, String nomCorpoReceptor) {
        this.ideCorpoReceptor = ideCorpoReceptor;
        this.nomCorpoReceptor = nomCorpoReceptor;
    }

    public Integer getIdeCorpoReceptor() {
        return ideCorpoReceptor;
    }

    public void setIdeCorpoReceptor(Integer ideCorpoReceptor) {
        this.ideCorpoReceptor = ideCorpoReceptor;
    }

    public String getNomCorpoReceptor() {
        return nomCorpoReceptor;
    }

    public void setNomCorpoReceptor(String nomCorpoReceptor) {
        this.nomCorpoReceptor = nomCorpoReceptor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCorpoReceptor != null ? ideCorpoReceptor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoCorpoReceptor)) {
            return false;
        }
        TipoCorpoReceptor other = (TipoCorpoReceptor) object;
        if ((this.ideCorpoReceptor == null && other.ideCorpoReceptor != null) || (this.ideCorpoReceptor != null && !this.ideCorpoReceptor.equals(other.ideCorpoReceptor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoCorpoReceptor[ ideCorpoReceptor=" + ideCorpoReceptor + " ]";
    }
    
}
