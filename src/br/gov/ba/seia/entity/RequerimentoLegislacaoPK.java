package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author alex.santos
 */
@Embeddable
public class RequerimentoLegislacaoPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_requerimento", nullable = false)
    private int ideRequerimento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_legislacao", nullable = false)
    private int ideLegislacao;

    public RequerimentoLegislacaoPK() {
    }

    public RequerimentoLegislacaoPK(int ideRequerimento, int ideLegislacao) {
        this.ideRequerimento = ideRequerimento;
        this.ideLegislacao = ideLegislacao;
    }

    public int getIdeRequerimento() {
        return ideRequerimento;
    }

    public void setIdeRequerimento(int ideRequerimento) {
        this.ideRequerimento = ideRequerimento;
    }

    public int getIdeLegislacao() {
        return ideLegislacao;
    }

    public void setIdeLegislacao(int ideLegislacao) {
        this.ideLegislacao = ideLegislacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideRequerimento;
        hash += (int) ideLegislacao;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof RequerimentoLegislacaoPK)) {
            return false;
        }
        RequerimentoLegislacaoPK other = (RequerimentoLegislacaoPK) object;
        if (this.ideRequerimento != other.ideRequerimento) {
            return false;
        }
        if (this.ideLegislacao != other.ideLegislacao) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RequerimentoLegislacaoPK[ ideRequerimento=" + ideRequerimento + ", ideLegislacao=" + ideLegislacao + " ]";
    }
    
}
