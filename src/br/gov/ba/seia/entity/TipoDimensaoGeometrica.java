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
@Table(name = "tipo_dimensao_geometrica", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_dimensao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoDimensaoGeometrica.findAll", query = "SELECT t FROM TipoDimensaoGeometrica t"),
    @NamedQuery(name = "TipoDimensaoGeometrica.findByIdeTipoDimensao", query = "SELECT t FROM TipoDimensaoGeometrica t WHERE t.ideTipoDimensao = :ideTipoDimensao"),
    @NamedQuery(name = "TipoDimensaoGeometrica.findByNomTipoDimensao", query = "SELECT t FROM TipoDimensaoGeometrica t WHERE t.nomTipoDimensao = :nomTipoDimensao")})
public class TipoDimensaoGeometrica implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_dimensao", nullable = false)
    private Integer ideTipoDimensao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nom_tipo_dimensao", nullable = false, length = 15)
    private String nomTipoDimensao;

    public TipoDimensaoGeometrica() {
    }

    public TipoDimensaoGeometrica(Integer ideTipoDimensao) {
        this.ideTipoDimensao = ideTipoDimensao;
    }

    public TipoDimensaoGeometrica(Integer ideTipoDimensao, String nomTipoDimensao) {
        this.ideTipoDimensao = ideTipoDimensao;
        this.nomTipoDimensao = nomTipoDimensao;
    }

    public Integer getIdeTipoDimensao() {
        return ideTipoDimensao;
    }

    public void setIdeTipoDimensao(Integer ideTipoDimensao) {
        this.ideTipoDimensao = ideTipoDimensao;
    }

    public String getNomTipoDimensao() {
        return nomTipoDimensao;
    }

    public void setNomTipoDimensao(String nomTipoDimensao) {
        this.nomTipoDimensao = nomTipoDimensao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoDimensao != null ? ideTipoDimensao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoDimensaoGeometrica)) {
            return false;
        }
        TipoDimensaoGeometrica other = (TipoDimensaoGeometrica) object;
        if ((this.ideTipoDimensao == null && other.ideTipoDimensao != null) || (this.ideTipoDimensao != null && !this.ideTipoDimensao.equals(other.ideTipoDimensao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoDimensaoGeometrica[ ideTipoDimensao=" + ideTipoDimensao + " ]";
    }
    
}
