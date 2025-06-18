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
@Table(name = "tipo_sistema_limpeza", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_sistema_limpeza"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSistemaLimpeza.findAll", query = "SELECT t FROM TipoSistemaLimpeza t"),
    @NamedQuery(name = "TipoSistemaLimpeza.findByIdeTipoSistemaLimpeza", query = "SELECT t FROM TipoSistemaLimpeza t WHERE t.ideTipoSistemaLimpeza = :ideTipoSistemaLimpeza"),
    @NamedQuery(name = "TipoSistemaLimpeza.findByNomTipoSistemaLimpeza", query = "SELECT t FROM TipoSistemaLimpeza t WHERE t.nomTipoSistemaLimpeza = :nomTipoSistemaLimpeza")})
public class TipoSistemaLimpeza implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_sistema_limpeza", nullable = false)
    private Integer ideTipoSistemaLimpeza;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nom_tipo_sistema_limpeza", nullable = false, length = 25)
    private String nomTipoSistemaLimpeza;

    public TipoSistemaLimpeza() {
    }

    public TipoSistemaLimpeza(Integer ideTipoSistemaLimpeza) {
        this.ideTipoSistemaLimpeza = ideTipoSistemaLimpeza;
    }

    public TipoSistemaLimpeza(Integer ideTipoSistemaLimpeza, String nomTipoSistemaLimpeza) {
        this.ideTipoSistemaLimpeza = ideTipoSistemaLimpeza;
        this.nomTipoSistemaLimpeza = nomTipoSistemaLimpeza;
    }

    public Integer getIdeTipoSistemaLimpeza() {
        return ideTipoSistemaLimpeza;
    }

    public void setIdeTipoSistemaLimpeza(Integer ideTipoSistemaLimpeza) {
        this.ideTipoSistemaLimpeza = ideTipoSistemaLimpeza;
    }

    public String getNomTipoSistemaLimpeza() {
        return nomTipoSistemaLimpeza;
    }

    public void setNomTipoSistemaLimpeza(String nomTipoSistemaLimpeza) {
        this.nomTipoSistemaLimpeza = nomTipoSistemaLimpeza;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoSistemaLimpeza != null ? ideTipoSistemaLimpeza.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoSistemaLimpeza)) {
            return false;
        }
        TipoSistemaLimpeza other = (TipoSistemaLimpeza) object;
        if ((this.ideTipoSistemaLimpeza == null && other.ideTipoSistemaLimpeza != null) || (this.ideTipoSistemaLimpeza != null && !this.ideTipoSistemaLimpeza.equals(other.ideTipoSistemaLimpeza))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoSistemaLimpeza[ ideTipoSistemaLimpeza=" + ideTipoSistemaLimpeza + " ]";
    }
    
}
