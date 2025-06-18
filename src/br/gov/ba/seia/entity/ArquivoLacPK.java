package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author luis
 */
@Embeddable
public class ArquivoLacPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_arquivo")
    private int ideArquivo;
	
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_lac")
    private int ideLac;

    public ArquivoLacPK() {}

    public ArquivoLacPK(int ideArquivo, int ideLac) {
        this.ideArquivo = ideArquivo;
        this.ideLac = ideLac;
    }

    public int getIdeArquivo() {
        return ideArquivo;
    }

    public void setIdeArquivo(int ideArquivo) {
        this.ideArquivo = ideArquivo;
    }

    public int getIdeLac() {
        return ideLac;
    }

    public void setIdeLac(int ideLac) {
        this.ideLac = ideLac;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideArquivo;
        hash += (int) ideLac;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ArquivoLacPK)) {
            return false;
        }
        ArquivoLacPK other = (ArquivoLacPK) object;
        if (this.ideArquivo != other.ideArquivo) {
            return false;
        }
        if (this.ideLac != other.ideLac) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ArquivoLacPK[ ideArquivo=" + ideArquivo + ", ideLac=" + ideLac + " ]";
    }
    
}
