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
@Table(name = "pessoa_atividade_historico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PessoaAtividadeHistorico.findAll", query = "SELECT p FROM PessoaAtividadeHistorico p"),
    @NamedQuery(name = "PessoaAtividadeHistorico.findByIdeAtividadeHistorico", query = "SELECT p FROM PessoaAtividadeHistorico p WHERE p.ideAtividadeHistorico = :ideAtividadeHistorico"),
    @NamedQuery(name = "PessoaAtividadeHistorico.findByDesAtividade", query = "SELECT p FROM PessoaAtividadeHistorico p WHERE p.desAtividade = :desAtividade"),
    @NamedQuery(name = "PessoaAtividadeHistorico.findByDtcHistorico", query = "SELECT p FROM PessoaAtividadeHistorico p WHERE p.dtcHistorico = :dtcHistorico")})
public class PessoaAtividadeHistorico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_atividade_historico", nullable = false)
    private Integer ideAtividadeHistorico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "des_atividade", nullable = false, length = 250)
    private String desAtividade;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_historico", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcHistorico;
    @JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaJuridica idePessoaJuridica;

    public PessoaAtividadeHistorico() {
    }

    public PessoaAtividadeHistorico(Integer ideAtividadeHistorico) {
        this.ideAtividadeHistorico = ideAtividadeHistorico;
    }

    public PessoaAtividadeHistorico(Integer ideAtividadeHistorico, String desAtividade, Date dtcHistorico) {
        this.ideAtividadeHistorico = ideAtividadeHistorico;
        this.desAtividade = desAtividade;
        this.dtcHistorico = dtcHistorico;
    }

    public Integer getIdeAtividadeHistorico() {
        return ideAtividadeHistorico;
    }

    public void setIdeAtividadeHistorico(Integer ideAtividadeHistorico) {
        this.ideAtividadeHistorico = ideAtividadeHistorico;
    }

    public String getDesAtividade() {
        return desAtividade;
    }

    public void setDesAtividade(String desAtividade) {
        this.desAtividade = desAtividade;
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
        hash += (ideAtividadeHistorico != null ? ideAtividadeHistorico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof PessoaAtividadeHistorico)) {
            return false;
        }
        PessoaAtividadeHistorico other = (PessoaAtividadeHistorico) object;
        if ((this.ideAtividadeHistorico == null && other.ideAtividadeHistorico != null) || (this.ideAtividadeHistorico != null && !this.ideAtividadeHistorico.equals(other.ideAtividadeHistorico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.PessoaAtividadeHistorico[ ideAtividadeHistorico=" + ideAtividadeHistorico + " ]";
    }
    
}
