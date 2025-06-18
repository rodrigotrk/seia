package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alex.santos
 */
@Entity
@Table(name = "requerimento_legislacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RequerimentoLegislacao.findAll", query = "SELECT r FROM RequerimentoLegislacao r"),
    @NamedQuery(name = "RequerimentoLegislacao.findByIdeRequerimento", query = "SELECT r FROM RequerimentoLegislacao r WHERE r.requerimentoLegislacaoPK.ideRequerimento = :ideRequerimento"),
    @NamedQuery(name = "RequerimentoLegislacao.findByIdeLegislacao", query = "SELECT r FROM RequerimentoLegislacao r WHERE r.requerimentoLegislacaoPK.ideLegislacao = :ideLegislacao")})
public class RequerimentoLegislacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RequerimentoLegislacaoPK requerimentoLegislacaoPK;
    @JoinColumn(name = "ide_legislacao", referencedColumnName = "ide_legislacao", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Legislacao legislacao;

    public RequerimentoLegislacao() {
    }

    public RequerimentoLegislacao(RequerimentoLegislacaoPK requerimentoLegislacaoPK) {
        this.requerimentoLegislacaoPK = requerimentoLegislacaoPK;
    }

    public RequerimentoLegislacao(int ideRequerimento, int ideLegislacao) {
        this.requerimentoLegislacaoPK = new RequerimentoLegislacaoPK(ideRequerimento, ideLegislacao);
    }

    public RequerimentoLegislacaoPK getRequerimentoLegislacaoPK() {
        return requerimentoLegislacaoPK;
    }

    public void setRequerimentoLegislacaoPK(RequerimentoLegislacaoPK requerimentoLegislacaoPK) {
        this.requerimentoLegislacaoPK = requerimentoLegislacaoPK;
    }

    public Legislacao getLegislacao() {
        return legislacao;
    }

    public void setLegislacao(Legislacao legislacao) {
        this.legislacao = legislacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requerimentoLegislacaoPK != null ? requerimentoLegislacaoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof RequerimentoLegislacao)) {
            return false;
        }
        RequerimentoLegislacao other = (RequerimentoLegislacao) object;
        if ((this.requerimentoLegislacaoPK == null && other.requerimentoLegislacaoPK != null) || (this.requerimentoLegislacaoPK != null && !this.requerimentoLegislacaoPK.equals(other.requerimentoLegislacaoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RequerimentoLegislacao[ requerimentoLegislacaoPK=" + requerimentoLegislacaoPK + " ]";
    }
    
}
