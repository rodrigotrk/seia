package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "pessoa_representante_historico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PessoaRepresentanteHistorico.findAll", query = "SELECT p FROM PessoaRepresentanteHistorico p"),
    @NamedQuery(name = "PessoaRepresentanteHistorico.findByIdeRepresentanteHistorico", query = "SELECT p FROM PessoaRepresentanteHistorico p WHERE p.ideRepresentanteHistorico = :ideRepresentanteHistorico"),
    @NamedQuery(name = "PessoaRepresentanteHistorico.findByNumCpf", query = "SELECT p FROM PessoaRepresentanteHistorico p WHERE p.numCpf = :numCpf"),
    @NamedQuery(name = "PessoaRepresentanteHistorico.findByNomPessoa", query = "SELECT p FROM PessoaRepresentanteHistorico p WHERE p.nomPessoa = :nomPessoa"),
    @NamedQuery(name = "PessoaRepresentanteHistorico.findByDtcHistorico", query = "SELECT p FROM PessoaRepresentanteHistorico p WHERE p.dtcHistorico = :dtcHistorico")})
public class PessoaRepresentanteHistorico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_representante_historico", nullable = false)
    private Integer ideRepresentanteHistorico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_cpf", nullable = false)
    private long numCpf;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nom_pessoa", nullable = false, length = 200)
    private String nomPessoa;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_historico", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcHistorico;
    @JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaJuridica idePessoaJuridica;

    public PessoaRepresentanteHistorico() {
    }

    public PessoaRepresentanteHistorico(Integer ideRepresentanteHistorico) {
        this.ideRepresentanteHistorico = ideRepresentanteHistorico;
    }

    public PessoaRepresentanteHistorico(Integer ideRepresentanteHistorico, long numCpf, String nomPessoa, Date dtcHistorico) {
        this.ideRepresentanteHistorico = ideRepresentanteHistorico;
        this.numCpf = numCpf;
        this.nomPessoa = nomPessoa;
        this.dtcHistorico = dtcHistorico;
    }

    public Integer getIdeRepresentanteHistorico() {
        return ideRepresentanteHistorico;
    }

    public void setIdeRepresentanteHistorico(Integer ideRepresentanteHistorico) {
        this.ideRepresentanteHistorico = ideRepresentanteHistorico;
    }

    public long getNumCpf() {
        return numCpf;
    }

    public void setNumCpf(long numCpf) {
        this.numCpf = numCpf;
    }

    public String getNomPessoa() {
        return nomPessoa;
    }

    public void setNomPessoa(String nomPessoa) {
        this.nomPessoa = nomPessoa;
    }

    public Date getDtcHistorico() {
        return dtcHistorico;
    }

    public void setDtcHistorico(Date dtcHistorico) {
        this.dtcHistorico = dtcHistorico;
    }

    public PessoaJuridica getIdePessoaJuridica() {
        return idePessoaJuridica;
    }

    public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
        this.idePessoaJuridica = idePessoaJuridica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRepresentanteHistorico != null ? ideRepresentanteHistorico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof PessoaRepresentanteHistorico)) {
            return false;
        }
        PessoaRepresentanteHistorico other = (PessoaRepresentanteHistorico) object;
        if ((this.ideRepresentanteHistorico == null && other.ideRepresentanteHistorico != null) || (this.ideRepresentanteHistorico != null && !this.ideRepresentanteHistorico.equals(other.ideRepresentanteHistorico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.PessoaRepresentanteHistorico[ ideRepresentanteHistorico=" + ideRepresentanteHistorico + " ]";
    }
    
}
