package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "participacao_acionaria")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParticipacaoAcionaria.findAll", query = "SELECT p FROM ParticipacaoAcionaria p"),
    @NamedQuery(name = "ParticipacaoAcionaria.findByIdeParticipacaoAcionaria", query = "SELECT p FROM ParticipacaoAcionaria p WHERE p.ideParticipacaoAcionaria = :ideParticipacaoAcionaria"),
    @NamedQuery(name = "ParticipacaoAcionaria.findByTipAcionista", query = "SELECT p FROM ParticipacaoAcionaria p WHERE p.tipAcionista = :tipAcionista"),
    @NamedQuery(name = "ParticipacaoAcionaria.findByPrcParticipacaoAcionaria", query = "SELECT p FROM ParticipacaoAcionaria p WHERE p.prcParticipacaoAcionaria = :prcParticipacaoAcionaria"),
    @NamedQuery(name = "ParticipacaoAcionaria.findByDtcCriacao", query = "SELECT p FROM ParticipacaoAcionaria p WHERE p.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "ParticipacaoAcionaria.findByIndExcluido", query = "SELECT p FROM ParticipacaoAcionaria p WHERE p.indExcluido = :indExcluido"),
    @NamedQuery(name = "ParticipacaoAcionaria.findByDtcExclusao", query = "SELECT p FROM ParticipacaoAcionaria p WHERE p.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "ParticipacaoAcionaria.findByPessoaJuridica", query = "SELECT p FROM ParticipacaoAcionaria p WHERE p.idePessoaJuridica.idePessoaJuridica = :idePessoaJuridica"),
    @NamedQuery(name = "ParticipacaoAcionaria.removerParticipacaoAcionaria", query = "delete from ParticipacaoAcionaria p where p.ideParticipacaoAcionaria = :ideParticipacaoAcionaria")})
public class ParticipacaoAcionaria implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="participacao_acionaria_ide_participacao_acionaria_seq")    
    @SequenceGenerator(name="participacao_acionaria_ide_participacao_acionaria_seq", sequenceName="participacao_acionaria_ide_participacao_acionaria_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_participacao_acionaria", nullable = false)
    private Integer ideParticipacaoAcionaria;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "tip_acionista", nullable = false)
    private int tipAcionista;
    
    @Basic(optional = false)
    @Column(name = "prc_participacao_acionaria", nullable = false, precision = 5, scale = 2)
    private BigDecimal prcParticipacaoAcionaria;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    
    @JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private PessoaJuridica idePessoaJuridica;
    
    @JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Pessoa idePessoa;

    public ParticipacaoAcionaria() { }

    public ParticipacaoAcionaria(Integer ideParticipacaoAcionaria) {
        this.ideParticipacaoAcionaria = ideParticipacaoAcionaria;
    }

    public ParticipacaoAcionaria(Integer ideParticipacaoAcionaria, int tipAcionista, BigDecimal prcParticipacaoAcionaria, Date dtcCriacao, boolean indExcluido) {
        this.ideParticipacaoAcionaria = ideParticipacaoAcionaria;
        this.tipAcionista = tipAcionista;
        this.prcParticipacaoAcionaria = prcParticipacaoAcionaria;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeParticipacaoAcionaria() {
        return ideParticipacaoAcionaria;
    }

    public void setIdeParticipacaoAcionaria(Integer ideParticipacaoAcionaria) {
        this.ideParticipacaoAcionaria = ideParticipacaoAcionaria;
    }

    public int getTipAcionista() {
        return tipAcionista;
    }

    public void setTipAcionista(int tipAcionista) {
        this.tipAcionista = tipAcionista;
    }
    public BigDecimal getPrcParticipacaoAcionaria() {
        return prcParticipacaoAcionaria;
    }

    public void setPrcParticipacaoAcionaria(BigDecimal prcParticipacaoAcionaria) {
        this.prcParticipacaoAcionaria = prcParticipacaoAcionaria;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    public PessoaJuridica getIdePessoaJuridica() {
        return idePessoaJuridica;
    }

    public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
        this.idePessoaJuridica = idePessoaJuridica;
    }

    public Pessoa getIdePessoa() {
        return idePessoa;
    }

    public void setIdePessoa(Pessoa idePessoa) {
        this.idePessoa = idePessoa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideParticipacaoAcionaria != null ? ideParticipacaoAcionaria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ParticipacaoAcionaria)) {
            return false;
        }
        ParticipacaoAcionaria other = (ParticipacaoAcionaria) object;
        if ((this.ideParticipacaoAcionaria == null && other.ideParticipacaoAcionaria != null) || (this.ideParticipacaoAcionaria != null && !this.ideParticipacaoAcionaria.equals(other.ideParticipacaoAcionaria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ParticipacaoAcionaria[ ideParticipacaoAcionaria=" + ideParticipacaoAcionaria + " ]";
    }
    
}
