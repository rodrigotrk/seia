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
@Table(name = "procurador_rep_empreendimento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcuradorRepEmpreendimento.findAll", query = "SELECT p FROM ProcuradorRepEmpreendimento p"),
    @NamedQuery(name = "ProcuradorRepEmpreendimento.findByIdeProcuradorRepEmpreendimento", query = "SELECT p FROM ProcuradorRepEmpreendimento p WHERE p.ideProcuradorRepEmpreendimento = :ideProcuradorRepEmpreendimento"),
    @NamedQuery(name = "ProcuradorRepEmpreendimento.findByDtcCriacao", query = "SELECT p FROM ProcuradorRepEmpreendimento p WHERE p.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "ProcuradorRepEmpreendimento.findByIndExcluido", query = "SELECT p FROM ProcuradorRepEmpreendimento p WHERE p.indExcluido = :indExcluido"),
    @NamedQuery(name = "ProcuradorRepEmpreendimento.findByDtcExclusao", query = "SELECT p FROM ProcuradorRepEmpreendimento p WHERE p.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "ProcuradorRepEmpreendimento.findByDscCaminhoProcuracao", query = "SELECT p FROM ProcuradorRepEmpreendimento p WHERE p.dscCaminhoProcuracao = :dscCaminhoProcuracao"),
    @NamedQuery(name = "ProcuradorRepEmpreendimento.findByDtcInicioProcuracao", query = "SELECT p FROM ProcuradorRepEmpreendimento p WHERE p.dtcInicioProcuracao = :dtcInicioProcuracao"),
    @NamedQuery(name = "ProcuradorRepEmpreendimento.findByDtcFimProcuracao", query = "SELECT p FROM ProcuradorRepEmpreendimento p WHERE p.dtcFimProcuracao = :dtcFimProcuracao"),
    @NamedQuery(name = "ProcuradorRepEmpreendimento.findByIndAssinaturaObrigatoria", query = "SELECT p FROM ProcuradorRepEmpreendimento p WHERE p.indAssinaturaObrigatoria = :indAssinaturaObrigatoria"),
    @NamedQuery(name = "ProcuradorRepEmpreendimento.UpdateParaExclusao", query = "UPDATE ProcuradorRepEmpreendimento p SET p.dtcExclusao = :dtcExclusao, p.indExcluido = :indExcluido WHERE p.ideProcuradorRepEmpreendimento = :ideProcuradorRepEmpreendimento"),
})
public class ProcuradorRepEmpreendimento implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ide_procurador_rep_empreendimento_seq")    
    @SequenceGenerator(name="ide_procurador_rep_empreendimento_seq", sequenceName="ide_procurador_rep_empreendimento_seq", allocationSize=1)
    @Column(name = "ide_procurador_rep_empreendimento", nullable = false)
    private Integer ideProcuradorRepEmpreendimento;
    
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
    
    @OneToMany(mappedBy = "ideProcuradorRepEmpreendimento", fetch = FetchType.LAZY)
    private Collection<DocumentoRepresentacaoRequerimento> documentoRepresentacaoRequerimentoCollection;
    
    @JoinColumn(name = "ide_procurador_representante", referencedColumnName = "ide_procurador_representante", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProcuradorRepresentante ideProcuradorRepresentante;
    
    @JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Empreendimento ideEmpreendimento;

    public ProcuradorRepEmpreendimento() {
    }

    public ProcuradorRepEmpreendimento(Integer ideProcuradorRepEmpreendimento) {
        this.ideProcuradorRepEmpreendimento = ideProcuradorRepEmpreendimento;
    }

    public ProcuradorRepEmpreendimento(Integer ideProcuradorRepEmpreendimento, Date dtcCriacao, boolean indExcluido, String dscCaminhoProcuracao, Date dtcInicioProcuracao, Date dtcFimProcuracao, boolean indAssinaturaObrigatoria) {
        this.ideProcuradorRepEmpreendimento = ideProcuradorRepEmpreendimento;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
        this.dscCaminhoProcuracao = dscCaminhoProcuracao;
        this.dtcInicioProcuracao = dtcInicioProcuracao;
        this.dtcFimProcuracao = dtcFimProcuracao;
        this.indAssinaturaObrigatoria = indAssinaturaObrigatoria;
    }

    public Integer getIdeProcuradorRepEmpreendimento() {
        return ideProcuradorRepEmpreendimento;
    }

    public void setIdeProcuradorRepEmpreendimento(Integer ideProcuradorRepEmpreendimento) {
        this.ideProcuradorRepEmpreendimento = ideProcuradorRepEmpreendimento;
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

    public ProcuradorRepresentante getIdeProcuradorRepresentante() {
        return ideProcuradorRepresentante;
    }

    public void setIdeProcuradorRepresentante(ProcuradorRepresentante ideProcuradorRepresentante) {
        this.ideProcuradorRepresentante = ideProcuradorRepresentante;
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
        hash += (ideProcuradorRepEmpreendimento != null ? ideProcuradorRepEmpreendimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ProcuradorRepEmpreendimento)) {
            return false;
        }
        ProcuradorRepEmpreendimento other = (ProcuradorRepEmpreendimento) object;
        if ((this.ideProcuradorRepEmpreendimento == null && other.ideProcuradorRepEmpreendimento != null) || (this.ideProcuradorRepEmpreendimento != null && !this.ideProcuradorRepEmpreendimento.equals(other.ideProcuradorRepEmpreendimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ProcuradorRepEmpreendimento[ ideProcuradorRepEmpreendimento=" + ideProcuradorRepEmpreendimento + " ]";
    }
    
}
