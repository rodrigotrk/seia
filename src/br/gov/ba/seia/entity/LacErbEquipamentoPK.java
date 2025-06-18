package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class LacErbEquipamentoPK implements Serializable {
	private static final long serialVersionUID = 1L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_lac_erb", nullable = false)
    private int ideLacErb;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_erb_equipamento", nullable = false)
    private Integer ideErbEquipamento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_canal_erb", nullable = false)
    private int ideTipoCanalErb;

    public LacErbEquipamentoPK() {
    }

    public LacErbEquipamentoPK(int ideLacErb, int ideErbEquipamento, int ideTipoCanalErb) {
        this.ideLacErb = ideLacErb;
        this.ideErbEquipamento = ideErbEquipamento;
        this.ideTipoCanalErb = ideTipoCanalErb;
    }

    public int getIdeLacErb() {
        return ideLacErb;
    }

    public void setIdeLacErb(int ideLacErb) {
        this.ideLacErb = ideLacErb;
    }

    public Integer getIdeErbEquipamento() {
        return ideErbEquipamento;
    }

    public void setIdeErbEquipamento(Integer ideErbEquipamento) {
        this.ideErbEquipamento = ideErbEquipamento;
    }


    public int getIdeTipoCanalErb() {
		return ideTipoCanalErb;
	}

	public void setIdeTipoCanalErb(int ideTipoCanalErb) {
		this.ideTipoCanalErb = ideTipoCanalErb;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideLacErb;
        hash += (int) ideErbEquipamento;
        hash += (int) ideTipoCanalErb;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof LacErbEquipamentoPK)) {
            return false;
        }
        LacErbEquipamentoPK other = (LacErbEquipamentoPK) object;
        if (this.ideLacErb != other.ideLacErb) {
            return false;
        }
        if (this.ideErbEquipamento != other.ideErbEquipamento) {
            return false;
        }
        if (this.ideTipoCanalErb != other.ideTipoCanalErb) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.LacErbEquipamentoPK[ ideLacErb=" + ideLacErb + ", ideErbEquipamento=" + ideErbEquipamento + " ]";
    }
    
}
