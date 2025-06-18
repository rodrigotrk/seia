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
public class PorteCompetenciaPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 25L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipologia_grupo", nullable = false)
    private int ideTipologiaGrupo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nivel_competencia", nullable = false)
    private int ideNivelCompetencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_porte", nullable = false)
    private int idePorte;

    public PorteCompetenciaPK() {
    }

    public PorteCompetenciaPK(int ideTipologiaGrupo, int ideNivelCompetencia, int idePorte) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
        this.ideNivelCompetencia = ideNivelCompetencia;
        this.idePorte = idePorte;
    }

    public int getIdeTipologiaGrupo() {
        return ideTipologiaGrupo;
    }

    public void setIdeTipologiaGrupo(int ideTipologiaGrupo) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
    }

    public int getIdeNivelCompetencia() {
        return ideNivelCompetencia;
    }

    public void setIdeNivelCompetencia(int ideNivelCompetencia) {
        this.ideNivelCompetencia = ideNivelCompetencia;
    }

    public int getIdePorte() {
        return idePorte;
    }

    public void setIdePorte(int idePorte) {
        this.idePorte = idePorte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideTipologiaGrupo;
        hash += (int) ideNivelCompetencia;
        hash += (int) idePorte;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof PorteCompetenciaPK)) {
            return false;
        }
        PorteCompetenciaPK other = (PorteCompetenciaPK) object;
        if (this.ideTipologiaGrupo != other.ideTipologiaGrupo) {
            return false;
        }
        if (this.ideNivelCompetencia != other.ideNivelCompetencia) {
            return false;
        }
        if (this.idePorte != other.idePorte) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.PorteCompetenciaPK[ ideTipologiaGrupo=" + ideTipologiaGrupo + ", ideNivelCompetencia=" + ideNivelCompetencia + ", idePorte=" + idePorte + " ]";
    }
    
}
