package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;

/**
 *
 * @author Lucas Reis
 *
 **/
@Entity
@Table(name = "solicitacao_administrativo")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "SolicitacaoAdministrativo.findAll", query = "SELECT sa FROM SolicitacaoAdministrativo sa"),
	@NamedQuery(name = "SolicitacaoAdministrativo.findByIdeSolicitacaoAdministrativo", query = "SELECT sa FROM SolicitacaoAdministrativo sa WHERE sa.ideSolicitacaoAdministrativo = :ideSolicitacaoAdministrativo"),
	@NamedQuery(name = "SolicitacaoAdministrativo.findByIdeRequerimento", query = "SELECT sa FROM SolicitacaoAdministrativo sa WHERE sa.ideRequerimento = :ideRequerimento"),
	@NamedQuery(name = "SolicitacaoAdministrativo.findByIdeTipoSolicitacao", query = "SELECT sa FROM SolicitacaoAdministrativo sa WHERE sa.ideTipoSolicitacao = :ideTipoSolicitacao"),
	@NamedQuery(name = "SolicitacaoAdministrativo.findByNumProcesso", query = "SELECT sa FROM SolicitacaoAdministrativo sa WHERE sa.numProcesso = :numProcesso"),
	@NamedQuery(name = "SolicitacaoAdministrativo.findByNumPortaria", query = "SELECT sa FROM SolicitacaoAdministrativo sa WHERE sa.numPortaria = :numPortaria"),
	@NamedQuery(name = "SolicitacaoAdministrativo.findByDtcPublicacaoPortaria", query = "SELECT sa FROM SolicitacaoAdministrativo sa WHERE sa.dtcPublicacaoPortaria = :dtcPublicacaoPortaria"),
	@NamedQuery(name = "SolicitacaoAdministrativo.findByDtcValidade", query = "SELECT sa FROM SolicitacaoAdministrativo sa WHERE sa.dtcValidade = :dtcValidade"),
	@NamedQuery(name = "SolicitacaoAdministrativo.findNumCertificado", query = "SELECT sa FROM SolicitacaoAdministrativo sa WHERE sa.numCertificado = :numCertificado"),
	@NamedQuery(name = "SolicitacaoAdministrativo.findNumCondicionante", query = "SELECT sa FROM SolicitacaoAdministrativo sa WHERE sa.numCondicionante = :numCondicionante"),
	@NamedQuery(name = "SolicitacaoAdministrativo.findIdeTipoProrrogacaoPrazoValidade", query = "SELECT sa FROM SolicitacaoAdministrativo sa WHERE sa.ideTipoProrrogacaoPrazoValidade = :ideTipoProrrogacaoPrazoValidade"),
	@NamedQuery(name = "SolicitacaoAdministrativo.deleteByIdeSolicitacaoAdministrativo", query = "DELETE FROM SolicitacaoAdministrativo sa WHERE sa.ideSolicitacaoAdministrativo = :ideSolicitacaoAdministrativo"), })

public class SolicitacaoAdministrativo implements Serializable, BaseEntity {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitacao_administrativo_ide_solicitacao_administrativo_seq")
	@SequenceGenerator(name = "solicitacao_administrativo_ide_solicitacao_administrativo_seq", sequenceName = "solicitacao_administrativo_ide_solicitacao_administrativo_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_solicitacao_administrativo", nullable = false)
	private Integer ideSolicitacaoAdministrativo;

	@Basic(optional = false)
	@Column(name = "num_processo", nullable = true, length = 50)
	private String numProcesso;
	
	@Basic(optional = false)
	@Column(name = "num_portaria", nullable = true, length = 50)
	private String numPortaria;
	
	@Basic(optional = false)
	@Column(name = "dtc_publicacao_portaria", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcPublicacaoPortaria;
	
	@Basic(optional = false)
	@Column(name = "dtc_validade", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidade;
	
	@Basic(optional = false)
	@Column(name = "num_certificado", nullable = true, length = 40)
	private String numCertificado;
	
	@Basic(optional = false)
	@Column(name = "num_condicionante", nullable = true, length = 20)
	private String numCondicionante;
	
	@Column(name = "nom_razao_social_nova", nullable = true, length = 200)
	private String nomRazaoSocialNova;
	
	@Column(name = "ind_detentor_licenca", nullable = false)
	private Boolean indDetentorLicenca;
	
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;
	
	@JoinColumn(name = "ide_tipo_solicitacao", referencedColumnName = "ide_tipo_solicitacao", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoSolicitacao ideTipoSolicitacao;
	
	@JoinColumn(name = "ide_tipo_prorrogacao_prazo_validade", referencedColumnName = "ide_tipo_prorrogacao_prazo_validade", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoProrrogacaoPrazoValidade ideTipoProrrogacaoPrazoValidade;
	
	@JoinColumn(name = "ide_tipo_revisao_condicionante", referencedColumnName = "ide_tipo_revisao_condicionante", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoRevisaoCondicionante ideTipoRevisaoCondicionante;
	
	@JoinColumn(name = "ide_pessoa_detentor_licenca", referencedColumnName = "ide_pessoa", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Pessoa idePessoaDetentorLicenca;
	
	@JoinColumn(name = "ide_pessoa_novo_titular", referencedColumnName = "ide_pessoa", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Pessoa idePessoaNovoTitular;
	
	@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Empreendimento ideEmpreendimento;
	
	@JoinColumn(name = "ide_sistema", referencedColumnName = "ide_sistema", nullable = true)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Sistema ideSistema;
	
	@OneToMany(mappedBy="ideSolicitacaoAdministrativo", fetch=FetchType.LAZY)
	private Collection<SolicitacaoAdministrativoAtoAmbiental> solicitacaoAdminstrativoAtoAmbientalCollection;

	public SolicitacaoAdministrativo() {}

	public SolicitacaoAdministrativo(Requerimento ideRequerimento) {
		super();
		this.ideRequerimento = ideRequerimento;
	}

	public SolicitacaoAdministrativo(Requerimento ideRequerimento, String numProcesso, String numPortaria,
			Date dtcPublicacaoPortaria, Date dtcValidade, String numCertificado,
			TipoRevisaoCondicionante ideTipoRevisaoCondicionante) {

		this.ideRequerimento = ideRequerimento;
		this.numProcesso = numProcesso;
		this.numPortaria = numPortaria;
		this.dtcPublicacaoPortaria = dtcPublicacaoPortaria;
		this.dtcValidade = dtcValidade;
		this.numCertificado = numCertificado;
		this.ideTipoRevisaoCondicionante = ideTipoRevisaoCondicionante;
	}

	public SolicitacaoAdministrativo(Requerimento ideRequerimento, String numProcesso, String numPortaria,
			Date dtcPublicacaoPortaria, TipoRevisaoCondicionante ideTipoRevisaoCondicionante, String numCondicionante) {

		this.ideRequerimento = ideRequerimento;
		this.numProcesso = numProcesso;
		this.numPortaria = numPortaria;
		this.dtcPublicacaoPortaria = dtcPublicacaoPortaria;
		this.numCondicionante = numCondicionante;
		this.ideTipoRevisaoCondicionante = ideTipoRevisaoCondicionante;
	}

	public Integer getIdeSolicitacaoAdministrativo() {
		return ideSolicitacaoAdministrativo;
	}

	public void setIdeSolicitacaoAdministrativo(Integer ideSolicitacaoAdministrativo) {
		this.ideSolicitacaoAdministrativo = ideSolicitacaoAdministrativo;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public TipoSolicitacao getIdeTipoSolicitacao() {
		return ideTipoSolicitacao;
	}

	public void setIdeTipoSolicitacao(TipoSolicitacao ideTipoSolicitacao) {
		this.ideTipoSolicitacao = ideTipoSolicitacao;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso.toUpperCase();
	}

	public String getNumPortaria() {
		return numPortaria;
	}

	public void setNumPortaria(String numPortaria) {
		this.numPortaria = numPortaria;
	}

	public Date getDtcPublicacaoPortaria() {
		return dtcPublicacaoPortaria;
	}

	public void setDtcPublicacaoPortaria(Date dtcPublicacaoPortaria) {
		this.dtcPublicacaoPortaria = dtcPublicacaoPortaria;
	}

	public Date getDtcValidade() {
		return dtcValidade;
	}

	public String getDtcValidadeFormatado() {
		if(!Util.isNullOuVazio(dtcValidade)) {
			SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
			return spf.format(dtcValidade);
		} else {
			return "-";
		}
	}

	public void setDtcValidade(Date dtcValidade) {
		this.dtcValidade = dtcValidade;
	}

	public String getNumCertificado() {
		return numCertificado;
	}

	public void setNumCertificado(String numCertificado) {
		this.numCertificado = numCertificado;
	}

	public String getNumCondicionante() {
		return numCondicionante;
	}

	public void setNumCondicionante(String numCondicionante) {
		this.numCondicionante = numCondicionante;
	}

	public TipoProrrogacaoPrazoValidade getIdeTipoProrrogacaoPrazoValidade() {
		return ideTipoProrrogacaoPrazoValidade;
	}

	public void setIdeTipoProrrogacaoPrazoValidade(TipoProrrogacaoPrazoValidade ideTipoProrrogacaoPrazoValidade) {
		this.ideTipoProrrogacaoPrazoValidade = ideTipoProrrogacaoPrazoValidade;
	}

	public TipoRevisaoCondicionante getIdeTipoRevisaoCondicionante() {
		return ideTipoRevisaoCondicionante;
	}

	public void setIdeTipoRevisaoCondicionante(TipoRevisaoCondicionante ideTipoRevisaoCondicionante) {
		this.ideTipoRevisaoCondicionante = ideTipoRevisaoCondicionante;
	}

	public String getNomRazaoSocialNova() {
		return nomRazaoSocialNova;
	}

	public void setNomRazaoSocialNova(String nomRazaoSocialNova) {
		this.nomRazaoSocialNova = nomRazaoSocialNova;
	}

	public Boolean getIndDetentorLicenca() {
		return indDetentorLicenca;
	}

	public void setIndDetentorLicenca(Boolean indDetentorLicenca) {
		this.indDetentorLicenca = indDetentorLicenca;
	}

	public Pessoa getIdePessoaDetentorLicenca() {
		return idePessoaDetentorLicenca;
	}

	public void setIdePessoaDetentorLicenca(Pessoa idePessoaDetentorLicenca) {
		this.idePessoaDetentorLicenca = idePessoaDetentorLicenca;
	}

	public Pessoa getIdePessoaNovoTitular() {
		return idePessoaNovoTitular;
	}

	public void setIdePessoaNovoTitular(Pessoa idePessoaNovoTitular) {
		this.idePessoaNovoTitular = idePessoaNovoTitular;
	}

	public Collection<SolicitacaoAdministrativoAtoAmbiental> getSolicitacaoAdminstrativoAtoAmbientalCollection() {
		return solicitacaoAdminstrativoAtoAmbientalCollection;
	}

	public void setSolicitacaoAdminstrativoAtoAmbientalCollection(Collection<SolicitacaoAdministrativoAtoAmbiental> solicitacaoAdminstrativoAtoAmbientalCollection) {
		this.solicitacaoAdminstrativoAtoAmbientalCollection = solicitacaoAdminstrativoAtoAmbientalCollection;
	}

	public Empreendimento getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	@Override
	public String toString() {
		return  ideSolicitacaoAdministrativo.toString() ;
	}

	public Sistema getIdeSistema() {
		return ideSistema;
	}

	public void setIdeSistema(Sistema ideSistema) {
		this.ideSistema = ideSistema;
	}

	@Override
	public Long getId() {
		return new Long(this.ideSolicitacaoAdministrativo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideSolicitacaoAdministrativo == null) ? 0 : ideSolicitacaoAdministrativo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SolicitacaoAdministrativo other = (SolicitacaoAdministrativo) obj;
		if (ideSolicitacaoAdministrativo == null) {
			if (other.ideSolicitacaoAdministrativo != null) {
				return false;
			}
		} else if (!ideSolicitacaoAdministrativo.equals(other.ideSolicitacaoAdministrativo)) {
			return false;
		}
		return true;
	}
}