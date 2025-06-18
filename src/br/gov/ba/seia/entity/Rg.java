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
@Table(name = "rg")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rg.findAll", query = "SELECT r FROM Rg r"),
    @NamedQuery(name = "Rg.findByIdeDocumentoIdentificacao", query = "SELECT r FROM Rg r WHERE r.ideDocumentoIdentificacao = :ideDocumentoIdentificacao"),
    @NamedQuery(name = "Rg.findByNumRg", query = "SELECT r FROM Rg r WHERE r.numRg = :numRg")})
public class Rg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_documento_identificacao", nullable = false)
    private Integer ideDocumentoIdentificacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "num_rg", nullable = false, length = 30)
    private String numRg;

    public Rg() {
    }

    public Rg(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public Rg(Integer ideDocumentoIdentificacao, String numRg) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
        this.numRg = numRg;
    }

    public Integer getIdeDocumentoIdentificacao() {
        return ideDocumentoIdentificacao;
    }

    public void setIdeDocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public String getNumRg() {
        return numRg;
    }

    public void setNumRg(String numRg) {
        this.numRg = numRg;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideDocumentoIdentificacao != null ? ideDocumentoIdentificacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Rg)) {
            return false;
        }
        Rg other = (Rg) object;
        if ((this.ideDocumentoIdentificacao == null && other.ideDocumentoIdentificacao != null) || (this.ideDocumentoIdentificacao != null && !this.ideDocumentoIdentificacao.equals(other.ideDocumentoIdentificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.Rg[ ideDocumentoIdentificacao=" + ideDocumentoIdentificacao + " ]";
    }
    
}
