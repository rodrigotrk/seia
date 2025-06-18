package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "passaporte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Passaporte.findAll", query = "SELECT p FROM Passaporte p"),
    @NamedQuery(name = "Passaporte.findByIdeDocumentoIdentificacao", query = "SELECT p FROM Passaporte p WHERE p.ideDocumentoIdentificacao = :ideDocumentoIdentificacao"),
    @NamedQuery(name = "Passaporte.findByNumPassporte", query = "SELECT p FROM Passaporte p WHERE p.numPassporte = :numPassporte"),
    @NamedQuery(name = "Passaporte.findByDtcValidade", query = "SELECT p FROM Passaporte p WHERE p.dtcValidade = :dtcValidade")})
public class Passaporte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_documento_identificacao", nullable = false)
    private Integer ideDocumentoIdentificacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "num_passporte", nullable = false, length = 30)
    private String numPassporte;
    @Column(name = "dtc_validade")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcValidade;

    public Passaporte() {
    }

    public Passaporte(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public Passaporte(Integer ideDocumentoIdentificacao, String numPassporte) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
        this.numPassporte = numPassporte;
    }

    public Integer getIdeDocumentoIdentificacao() {
        return ideDocumentoIdentificacao;
    }

    public void setIdeDocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public String getNumPassporte() {
        return numPassporte;
    }

    public void setNumPassporte(String numPassporte) {
        this.numPassporte = numPassporte;
    }

    public Date getDtcValidade() {
        return dtcValidade;
    }

    public void setDtcValidade(Date dtcValidade) {
        this.dtcValidade = dtcValidade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideDocumentoIdentificacao != null ? ideDocumentoIdentificacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Passaporte)) {
            return false;
        }
        Passaporte other = (Passaporte) object;
        if ((this.ideDocumentoIdentificacao == null && other.ideDocumentoIdentificacao != null) || (this.ideDocumentoIdentificacao != null && !this.ideDocumentoIdentificacao.equals(other.ideDocumentoIdentificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.Passaporte[ ideDocumentoIdentificacao=" + ideDocumentoIdentificacao + " ]";
    }
    
}
