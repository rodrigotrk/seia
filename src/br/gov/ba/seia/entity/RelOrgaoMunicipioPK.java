package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class RelOrgaoMunicipioPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_orgao", nullable = false)
    private int ideOrgao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_municipio", nullable = false)
    private int ideMunicipio;

    public RelOrgaoMunicipioPK() {
    }

    public RelOrgaoMunicipioPK(int ideOrgao, int ideMunicipio) {
        this.ideOrgao = ideOrgao;
        this.ideMunicipio = ideMunicipio;
    }

    public int getIdeOrgao() {
        return ideOrgao;
    }

    public void setIdeOrgao(int ideOrgao) {
        this.ideOrgao = ideOrgao;
    }

    public int getIdeMunicipio() {
        return ideMunicipio;
    }

    public void setIdeMunicipio(int ideMunicipio) {
        this.ideMunicipio = ideMunicipio;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideOrgao;
        hash += (int) ideMunicipio;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof RelOrgaoMunicipioPK)) {
            return false;
        }
        RelOrgaoMunicipioPK other = (RelOrgaoMunicipioPK) object;
        if (this.ideOrgao != other.ideOrgao) {
            return false;
        }
        if (this.ideMunicipio != other.ideMunicipio) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RelOrgaoMunicipioPK[ ideOrgao=" + ideOrgao + ", ideMunicipio=" + ideMunicipio + " ]";
    }
    
}
