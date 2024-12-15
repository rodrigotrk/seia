package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class DocumentoAtoPK implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8L;
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_documento_obrigatorio", nullable = false)
    private int ideDocumentoObrigatorio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_ato_ambiental", nullable = false)
    private int ideAtoAmbiental;

    public DocumentoAtoPK() {
    }

    public DocumentoAtoPK(int ideDocumentoObrigatorio, int ideAtoAmbiental) {
        this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
        this.ideAtoAmbiental = ideAtoAmbiental;
    }

    public int getIdeDocumentoObrigatorio() {
        return ideDocumentoObrigatorio;
    }

    public void setIdeDocumentoObrigatorio(int ideDocumentoObrigatorio) {
        this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
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
        hash += (int) ideDocumentoObrigatorio;
        hash += (int) ideAtoAmbiental;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof DocumentoAtoPK)) {
            return false;
        }
        DocumentoAtoPK other = (DocumentoAtoPK) object;
        if (this.ideDocumentoObrigatorio != other.ideDocumentoObrigatorio) {
            return false;
        }
        if (this.ideAtoAmbiental != other.ideAtoAmbiental) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.DocumentoAtoPK[ ideDocumentoObrigatorio=" + ideDocumentoObrigatorio + ", ideAtoAmbiental=" + ideAtoAmbiental + " ]";
    }
    
}
