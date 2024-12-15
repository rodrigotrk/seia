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
@Table(name = "tipo_cartorio_registro", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_cartorio"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoCartorioRegistro.findAll", query = "SELECT t FROM TipoCartorioRegistro t"),
    @NamedQuery(name = "TipoCartorioRegistro.findByIdeTipoCartorio", query = "SELECT t FROM TipoCartorioRegistro t WHERE t.ideTipoCartorio = :ideTipoCartorio"),
    @NamedQuery(name = "TipoCartorioRegistro.findByNomTipoCartorio", query = "SELECT t FROM TipoCartorioRegistro t WHERE t.nomTipoCartorio = :nomTipoCartorio")})
public class TipoCartorioRegistro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_cartorio", nullable = false)
    private Integer ideTipoCartorio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "nom_tipo_cartorio", nullable = false, length = 25)
    private String nomTipoCartorio;

    public TipoCartorioRegistro() {
    }

    public TipoCartorioRegistro(Integer ideTipoCartorio) {
        this.ideTipoCartorio = ideTipoCartorio;
    }

    public TipoCartorioRegistro(Integer ideTipoCartorio, String nomTipoCartorio) {
        this.ideTipoCartorio = ideTipoCartorio;
        this.nomTipoCartorio = nomTipoCartorio;
    }

    public Integer getIdeTipoCartorio() {
        return ideTipoCartorio;
    }

    public void setIdeTipoCartorio(Integer ideTipoCartorio) {
        this.ideTipoCartorio = ideTipoCartorio;
    }

    public String getNomTipoCartorio() {
        return nomTipoCartorio;
    }

    public void setNomTipoCartorio(String nomTipoCartorio) {
        this.nomTipoCartorio = nomTipoCartorio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoCartorio != null ? ideTipoCartorio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoCartorioRegistro)) {
            return false;
        }
        TipoCartorioRegistro other = (TipoCartorioRegistro) object;
        if ((this.ideTipoCartorio == null && other.ideTipoCartorio != null) || (this.ideTipoCartorio != null && !this.ideTipoCartorio.equals(other.ideTipoCartorio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoCartorioRegistro[ ideTipoCartorio=" + ideTipoCartorio + " ]";
    }
    
}
