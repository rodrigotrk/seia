package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_fornecimento_hidrico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoFornecimentoHidrico.findAll", query = "SELECT t FROM TipoFornecimentoHidrico t"),
    @NamedQuery(name = "TipoFornecimentoHidrico.findByIdeTipoFornecimentoHidrico", query = "SELECT t FROM TipoFornecimentoHidrico t WHERE t.ideTipoFornecimentoHidrico = :ideTipoFornecimentoHidrico"),
    @NamedQuery(name = "TipoFornecimentoHidrico.findByNomTipoFornecimentoHidrico", query = "SELECT t FROM TipoFornecimentoHidrico t WHERE t.nomTipoFornecimentoHidrico = :nomTipoFornecimentoHidrico")})
public class TipoFornecimentoHidrico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_fornecimento_hidrico", nullable = false)
    private Integer ideTipoFornecimentoHidrico;
    @Size(max = 80)
    @Column(name = "nom_tipo_fornecimento_hidrico", length = 80)
    private String nomTipoFornecimentoHidrico;

    public TipoFornecimentoHidrico() {
    }

    public TipoFornecimentoHidrico(Integer ideTipoFornecimentoHidrico) {
        this.ideTipoFornecimentoHidrico = ideTipoFornecimentoHidrico;
    }

    public Integer getIdeTipoFornecimentoHidrico() {
        return ideTipoFornecimentoHidrico;
    }

    public void setIdeTipoFornecimentoHidrico(Integer ideTipoFornecimentoHidrico) {
        this.ideTipoFornecimentoHidrico = ideTipoFornecimentoHidrico;
    }

    public String getNomTipoFornecimentoHidrico() {
        return nomTipoFornecimentoHidrico;
    }

    public void setNomTipoFornecimentoHidrico(String nomTipoFornecimentoHidrico) {
        this.nomTipoFornecimentoHidrico = nomTipoFornecimentoHidrico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoFornecimentoHidrico != null ? ideTipoFornecimentoHidrico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoFornecimentoHidrico)) {
            return false;
        }
        TipoFornecimentoHidrico other = (TipoFornecimentoHidrico) object;
        if ((this.ideTipoFornecimentoHidrico == null && other.ideTipoFornecimentoHidrico != null) || (this.ideTipoFornecimentoHidrico != null && !this.ideTipoFornecimentoHidrico.equals(other.ideTipoFornecimentoHidrico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoFornecimentoHidrico[ ideTipoFornecimentoHidrico=" + ideTipoFornecimentoHidrico + " ]";
    }
    
}
