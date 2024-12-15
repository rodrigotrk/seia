package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author MJunior
 */
@Entity
@Table(name = "grupo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"dsc_grupo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupo.findAll", query = "SELECT g FROM Grupo g"),
    @NamedQuery(name = "Grupo.findByIdeGrupo", query = "SELECT g FROM Grupo g WHERE g.ideGrupo = :ideGrupo"),
    @NamedQuery(name = "Grupo.findByDscGrupo", query = "SELECT g FROM Grupo g WHERE g.dscGrupo = :dscGrupo"),
    @NamedQuery(name = "Grupo.findByDtcCriacao", query = "SELECT g FROM Grupo g WHERE g.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Grupo.findByIndExcluido", query = "SELECT g FROM Grupo g WHERE g.indExcluido = :indExcluido"),
    @NamedQuery(name = "Grupo.findByDtcExclusao", query = "SELECT g FROM Grupo g WHERE g.dtcExclusao = :dtcExclusao")})
public class Grupo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_grupo", nullable = false)
    private Integer ideGrupo;
    @Basic(optional = false)
    @Column(name = "dsc_grupo", nullable = false, length = 25)
    private String dscGrupo;
    @Basic(optional = false)
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Basic(optional = false)
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;

    public Grupo() {
    }

    public Grupo(Integer ideGrupo) {
        this.ideGrupo = ideGrupo;
    }

    public Grupo(Integer ideGrupo, String dscGrupo, Date dtcCriacao, boolean indExcluido) {
        this.ideGrupo = ideGrupo;
        this.dscGrupo = dscGrupo;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeGrupo() {
        return ideGrupo;
    }

    public void setIdeGrupo(Integer ideGrupo) {
        this.ideGrupo = ideGrupo;
    }

    public String getDscGrupo() {
        return dscGrupo;
    }

    public void setDscGrupo(String dscGrupo) {
        this.dscGrupo = dscGrupo;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGrupo != null ? ideGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.ideGrupo == null && other.ideGrupo != null) || (this.ideGrupo != null && !this.ideGrupo.equals(other.ideGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.Grupo[ ideGrupo=" + ideGrupo + " ]";
    }
    
}
