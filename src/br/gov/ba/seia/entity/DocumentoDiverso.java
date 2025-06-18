package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "documento_diverso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DocumentoDiverso.findAll", query = "SELECT d FROM DocumentoDiverso d"),
    @NamedQuery(name = "DocumentoDiverso.findByIdeDocumentoIdentificacao", query = "SELECT d FROM DocumentoDiverso d WHERE d.ideDocumentoIdentificacao = :ideDocumentoIdentificacao"),
    @NamedQuery(name = "DocumentoDiverso.findByNumDocumento", query = "SELECT d FROM DocumentoDiverso d WHERE d.numDocumento = :numDocumento")})
public class DocumentoDiverso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_documento_identificacao", nullable = false)
    private Integer ideDocumentoIdentificacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "num_documento", nullable = false, length = 30)
    private String numDocumento;

    public DocumentoDiverso() {
    }

    public DocumentoDiverso(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public DocumentoDiverso(Integer ideDocumentoIdentificacao, String numDocumento) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
        this.numDocumento = numDocumento;
    }

    public Integer getIdeDocumentoIdentificacao() {
        return ideDocumentoIdentificacao;
    }

    public void setIdeDocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideDocumentoIdentificacao != null ? ideDocumentoIdentificacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof DocumentoDiverso)) {
            return false;
        }
        DocumentoDiverso other = (DocumentoDiverso) object;
        if ((this.ideDocumentoIdentificacao == null && other.ideDocumentoIdentificacao != null) || (this.ideDocumentoIdentificacao != null && !this.ideDocumentoIdentificacao.equals(other.ideDocumentoIdentificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.DocumentoDiverso[ ideDocumentoIdentificacao=" + ideDocumentoIdentificacao + " ]";
    }
    
}
