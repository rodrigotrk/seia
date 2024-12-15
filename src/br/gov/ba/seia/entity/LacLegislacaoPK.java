package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class LacLegislacaoPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_lac", nullable = false)
    private int ideLac;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_legislacao", nullable = false)
    private int ideLegislacao;

    public LacLegislacaoPK() {
    }

    public LacLegislacaoPK(int ideLac, int ideLegislacao) {
        this.ideLac = ideLac;
        this.ideLegislacao = ideLegislacao;
    }

    public int getIdeLac() {
        return ideLac;
    }

    public void setIdeLac(int ideLacErb) {
        this.ideLac = ideLacErb;
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
        hash += (int) ideLac;
        hash += (int) ideLegislacao;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof LacLegislacaoPK)) {
            return false;
        }
        LacLegislacaoPK other = (LacLegislacaoPK) object;
        if (this.ideLac != other.ideLac) {
            return false;
        }
        if (this.ideLegislacao != other.ideLegislacao) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.LacLegislacaoPK[ ideLac=" + ideLac + ", ideLegislacao=" + ideLegislacao + " ]";
    }
    
}
