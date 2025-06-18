package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.interfaces.Auditoria;
import br.gov.ba.seia.util.auditoria.AuditoriaUtil;


@Entity
@Table(name = "procurador_pessoa_fisica")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProcuradorPessoaFisica.findAll", query = "SELECT p FROM ProcuradorPessoaFisica p"),
    @NamedQuery(name = "ProcuradorPessoaFisica.findByIdeProcuradorPessoaFisica", query = "SELECT p FROM ProcuradorPessoaFisica p WHERE p.ideProcuradorPessoaFisica = :ideProcuradorPessoaFisica"),
    @NamedQuery(name = "ProcuradorPessoaFisica.findByDtcInicio", query = "SELECT p FROM ProcuradorPessoaFisica p WHERE p.dtcInicio = :dtcInicio"),
    @NamedQuery(name = "ProcuradorPessoaFisica.findByIndExcluido", query = "SELECT p FROM ProcuradorPessoaFisica p WHERE p.indExcluido = :indExcluido"),
    @NamedQuery(name = "ProcuradorPessoaFisica.findByDtcExclusao", query = "SELECT p FROM ProcuradorPessoaFisica p WHERE p.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "ProcuradorPessoaFisica.findByIdePessoaFisica", query = "SELECT p FROM ProcuradorPessoaFisica p WHERE p.idePessoaFisica.idePessoaFisica = :idePessoaFisica and p.indExcluido = :indExcluido"),
    @NamedQuery(name = "ProcuradorPessoaFisica.findByIdeProcurador", query = "SELECT p FROM ProcuradorPessoaFisica p WHERE p.ideProcurador.idePessoaFisica = :ideProcurador and p.indExcluido = :indExcluido"),
    @NamedQuery(name = "ProcuradorPessoaFisica.updateRemoveById", query = "update ProcuradorPessoaFisica p set p.dtcExclusao = :dtcFinal, p.indExcluido = :indExcluido, p.idePessoaFisicaUsuario = :idePessoaFisicaUsuario, p.enderecoIp = :enderecoIp, p.caminhoRequisicao = :caminhoRequisicao  where p.ideProcuradorPessoaFisica = :ideProcuradorPessoaFisica"),
    @NamedQuery(name = "ProcuradorPessoaFisica.findByRequerenteEmpreendimento", query = "SELECT ppf FROM ProcuradorPessoaFisica ppf left join ppf.procuradorPfEmpreendimentoCollection ppfe left join ppf.ideProcurador pro left join ppfe.ideEmpreendimento e left join e.idePessoa p WHERE p.idePessoa = :idePessoa and e.ideEmpreendimento = :ideEmpreendimento and ppfe.indAssinaturaObrigatoria = :indAssinaturaObrigatoria and ppf.indExcluido = :indExcluido")
})
public class ProcuradorPessoaFisica extends AbstractEntity implements Auditoria{
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROCURADOR_PESSOA_FISICA_IDE_PROCURADOR_PESSOA_FISICA_seq")
    @SequenceGenerator(name="PROCURADOR_PESSOA_FISICA_IDE_PROCURADOR_PESSOA_FISICA_seq", sequenceName="PROCURADOR_PESSOA_FISICA_IDE_PROCURADOR_PESSOA_FISICA_seq", allocationSize=1)
    @Column(name = "ide_procurador_pessoa_fisica", nullable = false)
    private Integer ideProcuradorPessoaFisica;

	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_inicio", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcInicio;

	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;

	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;

	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaFisica idePessoaFisica;

	@JoinColumn(name = "ide_procurador", referencedColumnName = "ide_pessoa_fisica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaFisica ideProcurador;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProcuradorPessoaFisica", fetch = FetchType.LAZY)
    private Collection<ProcuradorPfEmpreendimento> procuradorPfEmpreendimentoCollection;

	@Column(name = "ide_usuario")
	private Integer idePessoaFisicaUsuario;
	
	@Column(name = "endereco_ip")
	private String enderecoIp;
	
	@Column(name = "caminho_requisicao")
	private String caminhoRequisicao;
	
	@Transient
	private Empreendimento empreendimento;

    public ProcuradorPessoaFisica() {
    }

    public ProcuradorPessoaFisica(Integer ideProcuradorPessoaFisica) {
        this.ideProcuradorPessoaFisica = ideProcuradorPessoaFisica;
    }

    public ProcuradorPessoaFisica(PessoaFisica idePessoaFisica) {
    	this.idePessoaFisica = idePessoaFisica;
    }

    public ProcuradorPessoaFisica(Integer ideProcuradorPessoaFisica, Date dtcInicio, boolean indExcluido) {
        this.ideProcuradorPessoaFisica = ideProcuradorPessoaFisica;
        this.dtcInicio = dtcInicio;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeProcuradorPessoaFisica() {
        return ideProcuradorPessoaFisica;
    }

    public void setIdeProcuradorPessoaFisica(Integer ideProcuradorPessoaFisica) {
        this.ideProcuradorPessoaFisica = ideProcuradorPessoaFisica;
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

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    public PessoaFisica getIdePessoaFisica() {
        return idePessoaFisica;
    }

    public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
        this.idePessoaFisica = idePessoaFisica;
    }

    public PessoaFisica getIdeProcurador() {
        return ideProcurador;
    }

    public void setIdeProcurador(PessoaFisica ideProcurador) {
        this.ideProcurador = ideProcurador;
    }

	public Collection<ProcuradorPfEmpreendimento> getProcuradorPfEmpreendimentoCollection() {
		return procuradorPfEmpreendimentoCollection;
	}

	public void setProcuradorPfEmpreendimentoCollection(
			Collection<ProcuradorPfEmpreendimento> procuradorPfEmpreendimentoCollection) {
		this.procuradorPfEmpreendimentoCollection = procuradorPfEmpreendimentoCollection;
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
