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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "rel_acao_funcionalidade", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_acao_funcionalidade"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RelAcaoFuncionalidade.findAll", query = "SELECT r FROM RelAcaoFuncionalidade r"),
    @NamedQuery(name = "RelAcaoFuncionalidade.findByIdeAcaoFuncionalidade", query = "SELECT r FROM RelAcaoFuncionalidade r WHERE r.ideAcaoFuncionalidade = :ideAcaoFuncionalidade")})
public class RelAcaoFuncionalidade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_acao_funcionalidade", nullable = false)
    private Integer ideAcaoFuncionalidade;

    public RelAcaoFuncionalidade() {
    }

    public RelAcaoFuncionalidade(Integer ideAcaoFuncionalidade) {
        this.ideAcaoFuncionalidade = ideAcaoFuncionalidade;
    }

    public Integer getIdeAcaoFuncionalidade() {
        return ideAcaoFuncionalidade;
    }

    public void setIdeAcaoFuncionalidade(Integer ideAcaoFuncionalidade) {
        this.ideAcaoFuncionalidade = ideAcaoFuncionalidade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideAcaoFuncionalidade != null ? ideAcaoFuncionalidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof RelAcaoFuncionalidade)) {
            return false;
        }
        RelAcaoFuncionalidade other = (RelAcaoFuncionalidade) object;
        if ((this.ideAcaoFuncionalidade == null && other.ideAcaoFuncionalidade != null) || (this.ideAcaoFuncionalidade != null && !this.ideAcaoFuncionalidade.equals(other.ideAcaoFuncionalidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RelAcaoFuncionalidade[ ideAcaoFuncionalidade=" + ideAcaoFuncionalidade + " ]";
    }
    
}
