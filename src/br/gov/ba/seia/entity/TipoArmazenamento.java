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
@Table(name = "tipo_armazenamento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_armazenamento"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoArmazenamento.findAll", query = "SELECT t FROM TipoArmazenamento t"),
    @NamedQuery(name = "TipoArmazenamento.findByIdeTipoArmazenamento", query = "SELECT t FROM TipoArmazenamento t WHERE t.ideTipoArmazenamento = :ideTipoArmazenamento"),
    @NamedQuery(name = "TipoArmazenamento.findByNomTipoArmazenamento", query = "SELECT t FROM TipoArmazenamento t WHERE t.nomTipoArmazenamento = :nomTipoArmazenamento")})
public class TipoArmazenamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_armazenamento", nullable = false)
    private Integer ideTipoArmazenamento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nom_tipo_armazenamento", nullable = false, length = 15)
    private String nomTipoArmazenamento;

    public TipoArmazenamento() {
    }

    public TipoArmazenamento(Integer ideTipoArmazenamento) {
        this.ideTipoArmazenamento = ideTipoArmazenamento;
    }

    public TipoArmazenamento(Integer ideTipoArmazenamento, String nomTipoArmazenamento) {
        this.ideTipoArmazenamento = ideTipoArmazenamento;
        this.nomTipoArmazenamento = nomTipoArmazenamento;
    }

    public Integer getIdeTipoArmazenamento() {
        return ideTipoArmazenamento;
    }

    public void setIdeTipoArmazenamento(Integer ideTipoArmazenamento) {
        this.ideTipoArmazenamento = ideTipoArmazenamento;
    }

    public String getNomTipoArmazenamento() {
        return nomTipoArmazenamento;
    }

    public void setNomTipoArmazenamento(String nomTipoArmazenamento) {
        this.nomTipoArmazenamento = nomTipoArmazenamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoArmazenamento != null ? ideTipoArmazenamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoArmazenamento)) {
            return false;
        }
        TipoArmazenamento other = (TipoArmazenamento) object;
        if ((this.ideTipoArmazenamento == null && other.ideTipoArmazenamento != null) || (this.ideTipoArmazenamento != null && !this.ideTipoArmazenamento.equals(other.ideTipoArmazenamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoArmazenamento[ ideTipoArmazenamento=" + ideTipoArmazenamento + " ]";
    }
    
}
