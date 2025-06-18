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
@Table(name = "tipo_sistema_criacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoSistemaCriacao.findAll", query = "SELECT t FROM TipoSistemaCriacao t"),
    @NamedQuery(name = "TipoSistemaCriacao.findByIdeTipoSistemaCriacao", query = "SELECT t FROM TipoSistemaCriacao t WHERE t.ideTipoSistemaCriacao = :ideTipoSistemaCriacao"),
    @NamedQuery(name = "TipoSistemaCriacao.findByNomTipoSistemaCriacao", query = "SELECT t FROM TipoSistemaCriacao t WHERE t.nomTipoSistemaCriacao = :nomTipoSistemaCriacao")})
public class TipoSistemaCriacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_sistema_criacao", nullable = false)
    private Integer ideTipoSistemaCriacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_tipo_sistema_criacao", nullable = false, length = 20)
    private String nomTipoSistemaCriacao;

    public TipoSistemaCriacao() {
    }

    public TipoSistemaCriacao(Integer ideTipoSistemaCriacao) {
        this.ideTipoSistemaCriacao = ideTipoSistemaCriacao;
    }

    public TipoSistemaCriacao(Integer ideTipoSistemaCriacao, String nomTipoSistemaCriacao) {
        this.ideTipoSistemaCriacao = ideTipoSistemaCriacao;
        this.nomTipoSistemaCriacao = nomTipoSistemaCriacao;
    }

    public Integer getIdeTipoSistemaCriacao() {
        return ideTipoSistemaCriacao;
    }

    public void setIdeTipoSistemaCriacao(Integer ideTipoSistemaCriacao) {
        this.ideTipoSistemaCriacao = ideTipoSistemaCriacao;
    }

    public String getNomTipoSistemaCriacao() {
        return nomTipoSistemaCriacao;
    }

    public void setNomTipoSistemaCriacao(String nomTipoSistemaCriacao) {
        this.nomTipoSistemaCriacao = nomTipoSistemaCriacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoSistemaCriacao != null ? ideTipoSistemaCriacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoSistemaCriacao)) {
            return false;
        }
        TipoSistemaCriacao other = (TipoSistemaCriacao) object;
        if ((this.ideTipoSistemaCriacao == null && other.ideTipoSistemaCriacao != null) || (this.ideTipoSistemaCriacao != null && !this.ideTipoSistemaCriacao.equals(other.ideTipoSistemaCriacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoSistemaCriacao[ ideTipoSistemaCriacao=" + ideTipoSistemaCriacao + " ]";
    }
    
}
