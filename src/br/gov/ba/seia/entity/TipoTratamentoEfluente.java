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
@Table(name = "tipo_tratamento_efluente", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_tratamento"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoTratamentoEfluente.findAll", query = "SELECT t FROM TipoTratamentoEfluente t"),
    @NamedQuery(name = "TipoTratamentoEfluente.findByIdeTipoTratamento", query = "SELECT t FROM TipoTratamentoEfluente t WHERE t.ideTipoTratamento = :ideTipoTratamento"),
    @NamedQuery(name = "TipoTratamentoEfluente.findByNomTipoTratamento", query = "SELECT t FROM TipoTratamentoEfluente t WHERE t.nomTipoTratamento = :nomTipoTratamento")})
public class TipoTratamentoEfluente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_tratamento", nullable = false)
    private Integer ideTipoTratamento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "nom_tipo_tratamento", nullable = false, length = 150)
    private String nomTipoTratamento;

    public TipoTratamentoEfluente() {
    }

    public TipoTratamentoEfluente(Integer ideTipoTratamento) {
        this.ideTipoTratamento = ideTipoTratamento;
    }

    public TipoTratamentoEfluente(Integer ideTipoTratamento, String nomTipoTratamento) {
        this.ideTipoTratamento = ideTipoTratamento;
        this.nomTipoTratamento = nomTipoTratamento;
    }

    public Integer getIdeTipoTratamento() {
        return ideTipoTratamento;
    }

    public void setIdeTipoTratamento(Integer ideTipoTratamento) {
        this.ideTipoTratamento = ideTipoTratamento;
    }

    public String getNomTipoTratamento() {
        return nomTipoTratamento;
    }

    public void setNomTipoTratamento(String nomTipoTratamento) {
        this.nomTipoTratamento = nomTipoTratamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoTratamento != null ? ideTipoTratamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoTratamentoEfluente)) {
            return false;
        }
        TipoTratamentoEfluente other = (TipoTratamentoEfluente) object;
        if ((this.ideTipoTratamento == null && other.ideTipoTratamento != null) || (this.ideTipoTratamento != null && !this.ideTipoTratamento.equals(other.ideTipoTratamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoTratamentoEfluente[ ideTipoTratamento=" + ideTipoTratamento + " ]";
    }
    
}
