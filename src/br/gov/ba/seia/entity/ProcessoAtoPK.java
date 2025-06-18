package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ProcessoAtoPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 111L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_processo", nullable = false)
    private int ideProcesso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_ato_ambiental", nullable = false)
    private int ideAtoAmbiental;

    public ProcessoAtoPK() {
    }

    public ProcessoAtoPK(int ideProcesso, int ideAtoAmbiental) {
        this.ideProcesso = ideProcesso;
        this.ideAtoAmbiental = ideAtoAmbiental;
    }

    public int getIdeProcesso() {
        return ideProcesso;
    }

    public void setIdeProcesso(int ideProcesso) {
        this.ideProcesso = ideProcesso;
    }

    public int getIdeAtoAmbiental() {
        return ideAtoAmbiental;
    }

    public void setIdeAtoAmbiental(int ideAtoAmbiental) {
        this.ideAtoAmbiental = ideAtoAmbiental;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideProcesso;
        hash += (int) ideAtoAmbiental;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ProcessoAtoPK)) {
            return false;
        }
        ProcessoAtoPK other = (ProcessoAtoPK) object;
        if (this.ideProcesso != other.ideProcesso) {
            return false;
        }
        if (this.ideAtoAmbiental != other.ideAtoAmbiental) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ProcessoAtoPK[ ideProcesso=" + ideProcesso + ", ideAtoAmbiental=" + ideAtoAmbiental + " ]";
    }
    
}
