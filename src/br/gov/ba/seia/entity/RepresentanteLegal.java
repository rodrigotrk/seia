package br.gov.ba.seia.entity;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.Auditoria;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.auditoria.AuditoriaUtil;
import flexjson.JSON;

@Entity
@Table(name = "representante_legal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RepresentanteLegal.findAll", query = "SELECT r FROM RepresentanteLegal r"),
    @NamedQuery(name = "RepresentanteLegal.findByIdeRepresentanteLegal", query = "SELECT r FROM RepresentanteLegal r WHERE r.ideRepresentanteLegal = :ideRepresentanteLegal"),
    @NamedQuery(name = "RepresentanteLegal.findByDtcInicio", query = "SELECT r FROM RepresentanteLegal r WHERE r.dtcInicio = :dtcInicio"),
    @NamedQuery(name = "RepresentanteLegal.findByIndExcluido", query = "SELECT r FROM RepresentanteLegal r WHERE r.indExcluido = :indExcluido"),
    @NamedQuery(name = "RepresentanteLegal.findByDscCaminhoRepresentacao", query = "SELECT r FROM RepresentanteLegal r WHERE r.dscCaminhoRepresentacao = :dscCaminhoRepresentacao"),
    @NamedQuery(name = "RepresentanteLegal.findByDtcInicioRepresentacao", query = "SELECT r FROM RepresentanteLegal r WHERE r.dtcInicioRepresentacao = :dtcInicioRepresentacao"),
    @NamedQuery(name = "RepresentanteLegal.findByDtcFimRepresentacao", query = "SELECT r FROM RepresentanteLegal r WHERE r.dtcFimRepresentacao = :dtcFimRepresentacao"),
    @NamedQuery(name = "RepresentanteLegal.findByDtcExclusao", query = "SELECT r FROM RepresentanteLegal r WHERE r.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "RepresentanteLegal.findByIdePessoaJuridica", query = "SELECT r FROM RepresentanteLegal r WHERE r.idePessoaJuridica.idePessoaJuridica = :idePessoaJuridica and r.indExcluido = :indExcluido"),
    @NamedQuery(name = "RepresentanteLegal.findByIdePessoaFisica", query = "SELECT r FROM RepresentanteLegal r WHERE r.idePessoaFisica.idePessoaFisica = :idePessoaFisica and r.indExcluido = :indExcluido"),
    @NamedQuery(name = "RepresentanteLegal.remove", query = "update RepresentanteLegal r set r.dtcExclusao = :dtcExclusao, r.indExcluido = :indExcluido, r.idePessoaFisicaUsuario = :idePessoaFisicaUsuario, r.enderecoIp = :enderecoIp, r.caminhoRequisicao = :caminhoRequisicao where r.ideRepresentanteLegal = :ideRepresentanteLegal"),
   /*O erro no select: 'SELECT rl, pf.pessoa FROM RepresentanteLegal' ???*/ @NamedQuery(name = "RepresentanteLegal.findByRequerente", query = "SELECT rl, pf.pessoa FROM RepresentanteLegal rl left join rl.idePessoaFisica pf left join rl.idePessoaJuridica pj where pj.idePessoaJuridica = :idePessoa and rl.indAssinaturaObrigatoria = :indAssinaturaObrigatoria")
})
public class RepresentanteLegal implements Serializable, Auditoria{
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="REPRESENTANTE_LEGAL_IDE_REPRESENTANTE_LEGAL_seq")    
    @SequenceGenerator(name="REPRESENTANTE_LEGAL_IDE_REPRESENTANTE_LEGAL_seq", sequenceName="REPRESENTANTE_LEGAL_IDE_REPRESENTANTE_LEGAL_seq", allocationSize=1)
    @Basic(optional = false)
    @Column(name = "ide_representante_legal", nullable = false)
    private Integer ideRepresentanteLegal;
    
	@Basic(optional = false)
    @Column(name = "dtc_inicio", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcInicio;
    
	@Basic(optional = false)
    @Column(name = "dtc_inicio_representacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcInicioRepresentacao;
    
	@Basic(optional = false)
    @Column(name = "dtc_fim_representacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcFimRepresentacao;
    
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;    
    
	@Basic(optional = false)
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
	@Basic(optional = false)
    @Column(name = "dsc_caminho_representacao", nullable = false, length = 1000)
    private String dscCaminhoRepresentacao;
    
	@JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaJuridica idePessoaJuridica;
    
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private PessoaFisica idePessoaFisica;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_assinatura_obrigatoria")
    private boolean indAssinaturaObrigatoria;
	
	@Column(name = "ide_usuario")
	private Integer idePessoaFisicaUsuario;
	
	@Column(name = "endereco_ip")
	private String enderecoIp;
	
	@Column(name = "caminho_requisicao")
	private String caminhoRequisicao;
	
	@Transient
	private Empreendimento empreendimento;
	
	
	@JSON(include = false)
	public String getNomeRazao() {
		if(!Util.isNull(this.getIdePessoaFisica())) {
    		return this.getIdePessoaFisica().getNomPessoa();
    	}
    	else {
    		if(!Util.isNull(this.getIdePessoaJuridica())) {
    			return this.getIdePessoaJuridica().getNomRazaoSocial();
    		} else {
    			return "";
    		}
    	}
	}
	
	@JSON(include = false)
	public String getCpfCnpjFormatado() {
		if(!Util.isNull(this.getIdePessoaFisica())) {
    		return Util.formatarCPF(this.getIdePessoaFisica().getNumCpf());
    	}
    	else {
    		if(!Util.isNull(this.getIdePessoaJuridica())) {
    			return Util.formatarCNPJ(this.getIdePessoaJuridica().getNumCnpj());
    		} else {
    			return "";
    		}
    	}
	}

    public RepresentanteLegal() { }

    public RepresentanteLegal(Integer ideRepresentanteLegal) {
        this.ideRepresentanteLegal = ideRepresentanteLegal;
    }

    public RepresentanteLegal(Integer ideRepresentanteLegal, Date dtcInicio, boolean indExcluido, String dscCaminhoRepresentacao, Date dtcInicioRepresentacao, Date dtcFimRepresentacao) {
        this.ideRepresentanteLegal = ideRepresentanteLegal;
        this.dtcInicio = dtcInicio;
        this.indExcluido = indExcluido;
        this.dscCaminhoRepresentacao = dscCaminhoRepresentacao;
        this.dtcInicioRepresentacao = dtcInicioRepresentacao;
        this.dtcFimRepresentacao = dtcFimRepresentacao;
    }

    public Integer getIdeRepresentanteLegal() {
        return ideRepresentanteLegal;
    }

    public void setIdeRepresentanteLegal(Integer ideRepresentanteLegal) {
        this.ideRepresentanteLegal = ideRepresentanteLegal;
    }

    public Date getDtcInicio() {
        return dtcInicio;
    }

    public void setDtcInicio(Date dtcInicio) {
        this.dtcInicio = dtcInicio;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public String getDscCaminhoRepresentacao() {
        return dscCaminhoRepresentacao;
    }

    public void setDscCaminhoRepresentacao(String dscCaminhoRepresentacao) {
        this.dscCaminhoRepresentacao = dscCaminhoRepresentacao;
    }

    public Date getDtcInicioRepresentacao() {
        return dtcInicioRepresentacao;
    }

    public void setDtcInicioRepresentacao(Date dtcInicioRepresentacao) {
        this.dtcInicioRepresentacao = dtcInicioRepresentacao;
    }

    public Date getDtcFimRepresentacao() {
        return dtcFimRepresentacao;
    }

    public void setDtcFimRepresentacao(Date dtcFimRepresentacao) {
        this.dtcFimRepresentacao = dtcFimRepresentacao;
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

    public PessoaFisica getIdePessoaFisica() {
        return idePessoaFisica;
    }

    public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
        this.idePessoaFisica = idePessoaFisica;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideRepresentanteLegal != null ? ideRepresentanteLegal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RepresentanteLegal)) {
            return false;
        }
        RepresentanteLegal other = (RepresentanteLegal) object;
        if ((this.ideRepresentanteLegal == null && other.ideRepresentanteLegal != null) || (this.ideRepresentanteLegal != null && !this.ideRepresentanteLegal.equals(other.ideRepresentanteLegal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RepresentanteLegal[ ideRepresentanteLegal=" + ideRepresentanteLegal + " ]";
    }

	public boolean isIndAssinaturaObrigatoria() {
		return indAssinaturaObrigatoria;
	}

	public void setIndAssinaturaObrigatoria(boolean indAssinaturaObrigatoria) {
		this.indAssinaturaObrigatoria = indAssinaturaObrigatoria;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	@Override
	public Integer getIdePessoaFisicaUsuario() {
		
		return idePessoaFisicaUsuario;
	}

	@Override
	public void setIdePessoaFisicaUsuario(Integer idePessoaFisicaUsuario) {
		
		this.idePessoaFisicaUsuario = idePessoaFisicaUsuario;
	}

	@Override
	public String getEnderecoIp() {
		
		return enderecoIp;
	}

	@Override
	public void setEnderecoIp(String enderecoIp) {
		
		this.enderecoIp = enderecoIp;
	}

	@Override
	public String getCaminhoRequisicao() {
		
		return caminhoRequisicao;
	}

	@Override
	public void setCaminhoRequisicao(String caminhoRequisicao) {
		
		this.caminhoRequisicao = caminhoRequisicao;
	}  

	@Override
	public void capturarCamposAuditoria() {
		setIdePessoaFisicaUsuario(AuditoriaUtil.obterUsuario().getIdePessoaFisica());
		setEnderecoIp(AuditoriaUtil.obterIp());
		setCaminhoRequisicao(AuditoriaUtil.obterCaminhoPaginaRequisicao());
	}	
}
