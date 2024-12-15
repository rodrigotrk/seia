package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "ctps", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"num_ctps"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ctps.findAll", query = "SELECT c FROM Ctps c"),
    @NamedQuery(name = "Ctps.findByIdeDocumentoIdentificacao", query = "SELECT c FROM Ctps c WHERE c.ideDocumentoIdentificacao = :ideDocumentoIdentificacao"),
    @NamedQuery(name = "Ctps.findByNumCtps", query = "SELECT c FROM Ctps c WHERE c.numCtps = :numCtps"),
    @NamedQuery(name = "Ctps.findByNumSerie", query = "SELECT c FROM Ctps c WHERE c.numSerie = :numSerie")})
public class Ctps implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_documento_identificacao", nullable = false)
    private Integer ideDocumentoIdentificacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "num_ctps", nullable = false, length = 30)
    private String numCtps;
    @Size(max = 15)
    @Column(name = "num_serie", length = 15)
    private String numSerie;

    public Ctps() {
    }

    public Ctps(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public Ctps(Integer ideDocumentoIdentificacao, String numCtps) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
        this.numCtps = numCtps;
    }

    public Integer getIdeDocumentoIdentificacao() {
        return ideDocumentoIdentificacao;
    }

    public void setIdeDocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public String getNumCtps() {
        return numCtps;
    }

    public void setNumCtps(String numCtps) {
        this.numCtps = numCtps;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideDocumentoIdentificacao != null ? ideDocumentoIdentificacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Ctps)) {
            return false;
        }
        Ctps other = (Ctps) object;
        if ((this.ideDocumentoIdentificacao == null && other.ideDocumentoIdentificacao != null) || (this.ideDocumentoIdentificacao != null && !this.ideDocumentoIdentificacao.equals(other.ideDocumentoIdentificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.Ctps[ ideDocumentoIdentificacao=" + ideDocumentoIdentificacao + " ]";
    }
    
}
