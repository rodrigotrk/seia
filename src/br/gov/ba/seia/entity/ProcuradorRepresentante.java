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
@Table(name = "procurador_representante")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ProcuradorRepresentante.findAll", query = "SELECT p FROM ProcuradorRepresentante p"),
		@NamedQuery(name = "ProcuradorRepresentante.findByIdeProcuradorRepresentante", query = "SELECT p FROM ProcuradorRepresentante p WHERE p.ideProcuradorRepresentante = :ideProcuradorRepresentante"),
		@NamedQuery(name = "ProcuradorRepresentante.findByDtcInicio", query = "SELECT p FROM ProcuradorRepresentante p WHERE p.dtcInicio = :dtcInicio"),
		@NamedQuery(name = "ProcuradorRepresentante.findByIndExcluido", query = "SELECT p FROM ProcuradorRepresentante p WHERE p.indExcluido = :indExcluido"),
		@NamedQuery(name = "ProcuradorRepresentante.findByDtcExclusao", query = "SELECT p FROM ProcuradorRepresentante p WHERE p.dtcExclusao = :dtcExclusao"),
		@NamedQuery(name = "ProcuradorRepresentante.findByIdePessoaJuridica", query = "SELECT p FROM ProcuradorRepresentante p WHERE p.idePessoaJuridica = :idePessoaJuridica and p.indExcluido = :indExcluido"),
		@NamedQuery(name = "ProcuradorRepresentante.findByIdeProcurador", query = "SELECT p FROM ProcuradorRepresentante p WHERE p.ideProcurador.idePessoaFisica = :ideProcurador and p.indExcluido = :indExcluido"),
		@NamedQuery(name = "ProcuradorRepresentante.remove", query = "update ProcuradorRepresentante p set p.indExcluido = :indExcluido, p.dtcExclusao = :dtcExclusao, p.idePessoaFisicaUsuario = :idePessoaFisicaUsuario, p.enderecoIp = :enderecoIp, p.caminhoRequisicao = :caminhoRequisicao where p.ideProcuradorRepresentante = :ideProcuradorRepresentante"),
		@NamedQuery(name = "ProcuradorRepresentante.findByRequerenteEmpreendimento", query = "SELECT ppj FROM ProcuradorRepresentante ppj left join ppj.procuradorRepEmpreendimentoCollection ppje left join ppj.ideProcurador pro left join ppje.ideEmpreendimento e left join e.idePessoa p WHERE p.idePessoa = :idePessoa and e.ideEmpreendimento = :ideEmpreendimento and ppj.indExcluido = :indExcluido")})

public class ProcuradorRepresentante extends AbstractEntity implements Auditoria{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCURADOR_REPRESENTANTE_IDE_PROCURADOR_REPRESENTANTE_seq")
	@SequenceGenerator(name = "PROCURADOR_REPRESENTANTE_IDE_PROCURADOR_REPRESENTANTE_seq", sequenceName = "PROCURADOR_REPRESENTANTE_IDE_PROCURADOR_REPRESENTANTE_seq", allocationSize = 1)
	@Column(name = "ide_procurador_representante", nullable = false)
	private Integer ideProcuradorRepresentante;

	@Basic(optional = false)
	@NotNull
	@Column(name = "dtc_inicio", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcInicio;

	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;

	@JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private PessoaJuridica idePessoaJuridica;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;

	@JoinColumn(name = "ide_procurador", referencedColumnName = "ide_pessoa_fisica", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private PessoaFisica ideProcurador;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProcuradorRepresentante", fetch = FetchType.LAZY)
    private Collection<ProcuradorRepEmpreendimento> procuradorRepEmpreendimentoCollection;
	
	@Column(name = "ide_usuario")
	private Integer idePessoaFisicaUsuario;
	
	@Column(name = "endereco_ip")
	private String enderecoIp;
	
	@Column(name = "caminho_requisicao")
	private String caminhoRequisicao;

	@Transient
	private Empreendimento empreendimento;

	public ProcuradorRepresentante() {
	}

	public ProcuradorRepresentante(Integer ideProcuradorRepresentante) {
		this.ideProcuradorRepresentante = ideProcuradorRepresentante;
	}

	public ProcuradorRepresentante(PessoaJuridica idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
	}

	public ProcuradorRepresentante(Integer ideProcuradorRepresentante,Date dtcInicio, boolean indExcluido) {
		this.ideProcuradorRepresentante = ideProcuradorRepresentante;
		this.dtcInicio = dtcInicio;
		this.indExcluido = indExcluido;

	}

	public Integer getIdeProcuradorRepresentante() {
		return ideProcuradorRepresentante;
	}

	public void setIdeProcuradorRepresentante(Integer ideProcuradorRepresentante) {
		this.ideProcuradorRepresentante = ideProcuradorRepresentante;
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

	public PessoaFisica getIdeProcurador() {
		return ideProcurador;
	}

	public void setIdeProcurador(PessoaFisica ideProcurador) {
		this.ideProcurador = ideProcurador;
	}

	public PessoaJuridica getIdePessoaJuridica() {
		return idePessoaJuridica;
	}

	public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
	}

	public Collection<ProcuradorRepEmpreendimento> getProcuradorRepEmpreendimentoCollection() {
		return procuradorRepEmpreendimentoCollection;
	}

	public void setProcuradorRepEmpreendimentoCollection(Collection<ProcuradorRepEmpreendimento> procuradorRepEmpreendimentoCollection) {
		this.procuradorRepEmpreendimentoCollection = procuradorRepEmpreendimentoCollection;
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