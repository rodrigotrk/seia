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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "cnh", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"num_cnh"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cnh.findAll", query = "SELECT c FROM Cnh c"),
    @NamedQuery(name = "Cnh.findByIdeDocumentoIdentificacao", query = "SELECT c FROM Cnh c WHERE c.ideDocumentoIdentificacao = :ideDocumentoIdentificacao"),
    @NamedQuery(name = "Cnh.findByNumCnh", query = "SELECT c FROM Cnh c WHERE c.numCnh = :numCnh"),
    @NamedQuery(name = "Cnh.findByDtcValidade", query = "SELECT c FROM Cnh c WHERE c.dtcValidade = :dtcValidade")})
public class Cnh implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_documento_identificacao", nullable = false)
    private Integer ideDocumentoIdentificacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "num_cnh", nullable = false, length = 25)
    private String numCnh;
    @Column(name = "dtc_validade")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcValidade;

    public Cnh() {
    }

    public Cnh(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public Cnh(Integer ideDocumentoIdentificacao, String numCnh) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
        this.numCnh = numCnh;
    }

    public Integer getIdeDocumentoIdentificacao() {
        return ideDocumentoIdentificacao;
    }

    public void setIdeDocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public String getNumCnh() {
        return numCnh;
    }

    public void setNumCnh(String numCnh) {
        this.numCnh = numCnh;
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
        
        if (!(object instanceof Cnh)) {
            return false;
        }
        Cnh other = (Cnh) object;
        if ((this.ideDocumentoIdentificacao == null && other.ideDocumentoIdentificacao != null) || (this.ideDocumentoIdentificacao != null && !this.ideDocumentoIdentificacao.equals(other.ideDocumentoIdentificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.Cnh[ ideDocumentoIdentificacao=" + ideDocumentoIdentificacao + " ]";
    }
    
}
