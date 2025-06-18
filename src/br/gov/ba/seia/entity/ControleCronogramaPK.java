package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ControleCronogramaPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_cronograma", nullable = false)
    private int ideCronograma;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_item_cronograma", nullable = false)
    private int ideItemCronograma;

    public ControleCronogramaPK() {
    }
    public ControleCronogramaPK(int ideItemCronograma) {
        this.ideItemCronograma = ideItemCronograma;
    }
    public ControleCronogramaPK(int ideCronograma, int ideItemCronograma) {
        this.ideCronograma = ideCronograma;
        this.ideItemCronograma = ideItemCronograma;
    }

    public int getIdeCronograma() {
        return ideCronograma;
    }

    public void setIdeCronograma(int ideCronograma) {
        this.ideCronograma = ideCronograma;
    }

    public int getIdeItemCronograma() {
        return ideItemCronograma;
    }

    public void setIdeItemCronograma(int ideItemCronograma) {
        this.ideItemCronograma = ideItemCronograma;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideCronograma;
        hash += (int) ideItemCronograma;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ControleCronogramaPK)) {
            return false;
        }
        ControleCronogramaPK other = (ControleCronogramaPK) object;
        if (this.ideCronograma != other.ideCronograma) {
            return false;
        }
        if (this.ideItemCronograma != other.ideItemCronograma) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ControleCronogramaPK[ ideCronograma=" + ideCronograma + ", ideItemCronograma=" + ideItemCronograma + " ]";
    }
    
}
