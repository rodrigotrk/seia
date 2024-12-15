package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Embeddable
public class EmpreendimentoTipologiaPK implements Serializable {
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_empreendimento", nullable = false)
    private int ideEmpreendimento;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipologia_grupo", nullable = false)
    private int ideTipologiaGrupo;

    public EmpreendimentoTipologiaPK() {}

    public EmpreendimentoTipologiaPK(int ideEmpreendimento, int ideTipologiaGrupo) {
        this.ideEmpreendimento = ideEmpreendimento;
        this.ideTipologiaGrupo = ideTipologiaGrupo;
    }

    public int getIdeEmpreendimento() {
        return ideEmpreendimento;
    }

    public void setIdeEmpreendimento(int ideEmpreendimento) {
        this.ideEmpreendimento = ideEmpreendimento;
    }

    public int getIdeTipologiaGrupo() {
        return ideTipologiaGrupo;
    }

    public void setIdeTipologiaGrupo(int ideTipologiaGrupo) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideEmpreendimento;
        hash += (int) ideTipologiaGrupo;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof EmpreendimentoTipologiaPK)) {
            return false;
        }
        EmpreendimentoTipologiaPK other = (EmpreendimentoTipologiaPK) object;
        if (this.ideEmpreendimento != other.ideEmpreendimento) {
            return false;
        }
        if (this.ideTipologiaGrupo != other.ideTipologiaGrupo) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.EmpreendimentoTipologiaPK[ ideEmpreendimento=" + ideEmpreendimento + ", ideTipologiaGrupo=" + ideTipologiaGrupo + " ]";
    }
}