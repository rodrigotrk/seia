package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author luis
 */
@Embeddable
public class EnquadramentoDocumentoAtoPK implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Basic(optional = false)
    @Column(name = "ide_enquadramento")
    private Integer ideEnquadramento;
    @Basic(optional = false)
    @Column(name = "ide_documento_ato")
    private Integer ideDocumentoAto;

    public EnquadramentoDocumentoAtoPK() {
    }

    public EnquadramentoDocumentoAtoPK(Integer ideEnquadramento, Integer ideDocumentoAto) {
        this.ideEnquadramento = ideEnquadramento;
        this.ideDocumentoAto = ideDocumentoAto;
    }

    public Integer getIdeEnquadramento() {
        return ideEnquadramento;
    }

    public void setIdeEnquadramento(Integer ideEnquadramento) {
        this.ideEnquadramento = ideEnquadramento;
    }

    public Integer getIdeDocumentoAto() {
        return ideDocumentoAto;
    }

    public void setIdeDocumentoAto(Integer ideDocumentoAto) {
        this.ideDocumentoAto = ideDocumentoAto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ideEnquadramento;
        hash += (int) ideDocumentoAto;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof EnquadramentoDocumentoAtoPK)) {
            return false;
        }
        EnquadramentoDocumentoAtoPK other = (EnquadramentoDocumentoAtoPK) object;
        if (this.ideEnquadramento != other.ideEnquadramento) {
            return false;
        }
        if (this.ideDocumentoAto != other.ideDocumentoAto) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.EnquadramentoDocumentoAtoPK[ ideEnquadramento=" + ideEnquadramento + ", ideDocumentoAto=" + ideDocumentoAto + " ]";
    }
    
}
