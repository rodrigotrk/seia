package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "procurador_pf_empreendimento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcuradorPfEmpreendimento.findAll", query = "SELECT p FROM ProcuradorPfEmpreendimento p"),
    @NamedQuery(name = "ProcuradorPfEmpreendimento.findByIdeProcuradorPfEmpreendimento", query = "SELECT p FROM ProcuradorPfEmpreendimento p WHERE p.ideProcuradorPfEmpreendimento = :ideProcuradorPfEmpreendimento"),
    @NamedQuery(name = "ProcuradorPfEmpreendimento.findByDtcCriacao", query = "SELECT p FROM ProcuradorPfEmpreendimento p WHERE p.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "ProcuradorPfEmpreendimento.findByIndExcluido", query = "SELECT p FROM ProcuradorPfEmpreendimento p WHERE p.indExcluido = :indExcluido"),
    @NamedQuery(name = "ProcuradorPfEmpreendimento.findByDtcExclusao", query = "SELECT p FROM ProcuradorPfEmpreendimento p WHERE p.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "ProcuradorPfEmpreendimento.findByDscCaminhoProcuracao", query = "SELECT p FROM ProcuradorPfEmpreendimento p WHERE p.dscCaminhoProcuracao = :dscCaminhoProcuracao"),
    @NamedQuery(name = "ProcuradorPfEmpreendimento.findByDtcInicioProcuracao", query = "SELECT p FROM ProcuradorPfEmpreendimento p WHERE p.dtcInicioProcuracao = :dtcInicioProcuracao"),
    @NamedQuery(name = "ProcuradorPfEmpreendimento.findByDtcFimProcuracao", query = "SELECT p FROM ProcuradorPfEmpreendimento p WHERE p.dtcFimProcuracao = :dtcFimProcuracao"),
    @NamedQuery(name = "ProcuradorPfEmpreendimento.findByIndAssinaturaObrigatoria", query = "SELECT p FROM ProcuradorPfEmpreendimento p WHERE p.indAssinaturaObrigatoria = :indAssinaturaObrigatoria"),
    @NamedQuery(name = "ProcuradorPfEmpreendimento.UpdateParaExclusao", query = "UPDATE ProcuradorPfEmpreendimento p SET p.dtcExclusao = :dtcExclusao, p.indExcluido = :indExcluido WHERE p.ideProcuradorPfEmpreendimento = :ideProcuradorPfEmpreendimento"),
})
public class ProcuradorPfEmpreendimento implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ide_procurador_pf_empreendimento_seq")    
    @SequenceGenerator(name="ide_procurador_pf_empreendimento_seq", sequenceName="ide_procurador_pf_empreendimento_seq", allocationSize=1)
    @Column(name = "ide_procurador_pf_empreendimento", nullable = false)
    private Integer ideProcuradorPfEmpreendimento;
    
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
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "dsc_caminho_procuracao", nullable = false, length = 1000)
    private String dscCaminhoProcuracao;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_inicio_procuracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcInicioProcuracao;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_fim_procuracao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcFimProcuracao;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_assinatura_obrigatoria", nullable = false)
    private boolean indAssinaturaObrigatoria;
    
	@OneToMany(mappedBy = "ideProcuradorPfEmpreendimento", fetch = FetchType.LAZY)
    private Collection<DocumentoRepresentacaoRequerimento> documentoRepresentacaoRequerimentoCollection;
    
	@JoinColumn(name = "ide_procurador_pessoa_fisica", referencedColumnName = "ide_procurador_pessoa_fisica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProcuradorPessoaFisica ideProcuradorPessoaFisica;
    
	@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Empreendimento ideEmpreendimento;

    public ProcuradorPfEmpreendimento() {
    }

    public ProcuradorPfEmpreendimento(Integer ideProcuradorPfEmpreendimento) {
        this.ideProcuradorPfEmpreendimento = ideProcuradorPfEmpreendimento;
    }

    public ProcuradorPfEmpreendimento(Integer ideProcuradorPfEmpreendimento, Date dtcCriacao, boolean indExcluido, String dscCaminhoProcuracao, Date dtcInicioProcuracao, Date dtcFimProcuracao, boolean indAssinaturaObrigatoria) {
        this.ideProcuradorPfEmpreendimento = ideProcuradorPfEmpreendimento;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
        this.dscCaminhoProcuracao = dscCaminhoProcuracao;
        this.dtcInicioProcuracao = dtcInicioProcuracao;
        this.dtcFimProcuracao = dtcFimProcuracao;
        this.indAssinaturaObrigatoria = indAssinaturaObrigatoria;
    }
    
    public ProcuradorPfEmpreendimento(Integer ideProcuradorPfEmpreendimento, Date dtcCriacao, boolean indExcluido, Date dtcExclusao, String dscCaminhoProcuracao,
			Date dtcInicioProcuracao, Date dtcFimProcuracao, boolean indAssinaturaObrigatoria,
			Collection<DocumentoRepresentacaoRequerimento> documentoRepresentacaoRequerimentoCollection, ProcuradorPessoaFisica ideProcuradorPessoaFisica,
			Empreendimento ideEmpreendimento) {
	
		super();
		this.ideProcuradorPfEmpreendimento = ideProcuradorPfEmpreendimento;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
		this.dtcExclusao = dtcExclusao;
		this.dscCaminhoProcuracao = dscCaminhoProcuracao;
		this.dtcInicioProcuracao = dtcInicioProcuracao;
		this.dtcFimProcuracao = dtcFimProcuracao;
		this.indAssinaturaObrigatoria = indAssinaturaObrigatoria;
		this.documentoRepresentacaoRequerimentoCollection = documentoRepresentacaoRequerimentoCollection;
		this.ideProcuradorPessoaFisica = ideProcuradorPessoaFisica;
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public Integer getIdeProcuradorPfEmpreendimento() {
        return ideProcuradorPfEmpreendimento;
    }

    public void setIdeProcuradorPfEmpreendimento(Integer ideProcuradorPfEmpreendimento) {
        this.ideProcuradorPfEmpreendimento = ideProcuradorPfEmpreendimento;
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

    public String getDscCaminhoProcuracao() {
        return dscCaminhoProcuracao;
    }

    public void setDscCaminhoProcuracao(String dscCaminhoProcuracao) {
        this.dscCaminhoProcuracao = dscCaminhoProcuracao;
    }

    public Date getDtcInicioProcuracao() {
        return dtcInicioProcuracao;
    }

    public void setDtcInicioProcuracao(Date dtcInicioProcuracao) {
        this.dtcInicioProcuracao = dtcInicioProcuracao;
    }

    public Date getDtcFimProcuracao() {
        return dtcFimProcuracao;
    }

    public void setDtcFimProcuracao(Date dtcFimProcuracao) {
        this.dtcFimProcuracao = dtcFimProcuracao;
    }

    public boolean getIndAssinaturaObrigatoria() {
        return indAssinaturaObrigatoria;
    }

    public void setIndAssinaturaObrigatoria(boolean indAssinaturaObrigatoria) {
        this.indAssinaturaObrigatoria = indAssinaturaObrigatoria;
    }

    @XmlTransient
    public Collection<DocumentoRepresentacaoRequerimento> getDocumentoRepresentacaoRequerimentoCollection() {
        return documentoRepresentacaoRequerimentoCollection;
    }

    public void setDocumentoRepresentacaoRequerimentoCollection(Collection<DocumentoRepresentacaoRequerimento> documentoRepresentacaoRequerimentoCollection) {
        this.documentoRepresentacaoRequerimentoCollection = documentoRepresentacaoRequerimentoCollection;
    }

    public ProcuradorPessoaFisica getIdeProcuradorPessoaFisica() {
        return ideProcuradorPessoaFisica;
    }

    public void setIdeProcuradorPessoaFisica(ProcuradorPessoaFisica ideProcuradorPessoaFisica) {
        this.ideProcuradorPessoaFisica = ideProcuradorPessoaFisica;
    }

    public Empreendimento getIdeEmpreendimento() {
        return ideEmpreendimento;
    }

    public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
        this.ideEmpreendimento = ideEmpreendimento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideProcuradorPfEmpreendimento != null ? ideProcuradorPfEmpreendimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ProcuradorPfEmpreendimento)) {
            return false;
        }
        ProcuradorPfEmpreendimento other = (ProcuradorPfEmpreendimento) object;
        if ((this.ideProcuradorPfEmpreendimento == null && other.ideProcuradorPfEmpreendimento != null) || (this.ideProcuradorPfEmpreendimento != null && !this.ideProcuradorPfEmpreendimento.equals(other.ideProcuradorPfEmpreendimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ProcuradorPfEmpreendimento[ ideProcuradorPfEmpreendimento=" + ideProcuradorPfEmpreendimento + " ]";
    }
    
}
