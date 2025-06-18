package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author carlos.sousa
 */
@Embeddable
public class RequerimentoImovelPK implements Serializable {
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
    @Column(name = "ide_imovel", nullable = false)
    private int ideImovel;

    public RequerimentoImovelPK() {
    }

    public RequerimentoImovelPK(int ideRequerimento, int ideImovel) {
        this.ideRequerimento = ideRequerimento;
        this.ideImovel = ideImovel;
    }

    public int getIdeRequerimento() {
        return ideRequerimento;
    }

    public void setIdeRequerimento(int ideRequerimento) {
        this.ideRequerimento = ideRequerimento;
    }

    public int getIdeImovel() {
        return ideImovel;
    }

    public void setIdeImovel(int ideImovel) {
        this.ideImovel = ideImovel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideRequerimento;
        hash += (int) ideImovel;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof RequerimentoImovelPK)) {
            return false;
        }
        RequerimentoImovelPK other = (RequerimentoImovelPK) object;
        if (this.ideRequerimento != other.ideRequerimento) {
            return false;
        }
        if (this.ideImovel != other.ideImovel) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RequerimentoImovelPK[ ideRequerimento=" + ideRequerimento + ", ideImovel=" + ideImovel + " ]";
    }
    
}
