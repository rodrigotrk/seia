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
@Table(name = "registro_identidade_civil", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"num_registro_identidade_civil"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RegistroIdentidadeCivil.findAll", query = "SELECT r FROM RegistroIdentidadeCivil r"),
    @NamedQuery(name = "RegistroIdentidadeCivil.findByIdeDocumentoIdentificacao", query = "SELECT r FROM RegistroIdentidadeCivil r WHERE r.ideDocumentoIdentificacao = :ideDocumentoIdentificacao"),
    @NamedQuery(name = "RegistroIdentidadeCivil.findByNumRegistroIdentidadeCivil", query = "SELECT r FROM RegistroIdentidadeCivil r WHERE r.numRegistroIdentidadeCivil = :numRegistroIdentidadeCivil"),
    @NamedQuery(name = "RegistroIdentidadeCivil.findByDtcValidade", query = "SELECT r FROM RegistroIdentidadeCivil r WHERE r.dtcValidade = :dtcValidade")})
public class RegistroIdentidadeCivil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_documento_identificacao", nullable = false)
    private Integer ideDocumentoIdentificacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "num_registro_identidade_civil", nullable = false, length = 25)
    private String numRegistroIdentidadeCivil;
    @Column(name = "dtc_validade")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcValidade;

    public RegistroIdentidadeCivil() {
    }

    public RegistroIdentidadeCivil(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public RegistroIdentidadeCivil(Integer ideDocumentoIdentificacao, String numRegistroIdentidadeCivil) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
        this.numRegistroIdentidadeCivil = numRegistroIdentidadeCivil;
    }

    public Integer getIdeDocumentoIdentificacao() {
        return ideDocumentoIdentificacao;
    }

    public void setIdeDocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
        this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
    }

    public String getNumRegistroIdentidadeCivil() {
        return numRegistroIdentidadeCivil;
    }

    public void setNumRegistroIdentidadeCivil(String numRegistroIdentidadeCivil) {
        this.numRegistroIdentidadeCivil = numRegistroIdentidadeCivil;
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
        
        if (!(object instanceof RegistroIdentidadeCivil)) {
            return false;
        }
        RegistroIdentidadeCivil other = (RegistroIdentidadeCivil) object;
        if ((this.ideDocumentoIdentificacao == null && other.ideDocumentoIdentificacao != null) || (this.ideDocumentoIdentificacao != null && !this.ideDocumentoIdentificacao.equals(other.ideDocumentoIdentificacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RegistroIdentidadeCivil[ ideDocumentoIdentificacao=" + ideDocumentoIdentificacao + " ]";
    }
    
}
