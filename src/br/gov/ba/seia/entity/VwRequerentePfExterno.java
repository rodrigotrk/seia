package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author MJunior
 */
@Entity
@Table(name = "vw_requerente_pf_externo")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "VwRequerentePfExterno.findAll", query = "SELECT v FROM VwRequerentePfExterno v"),
		@NamedQuery(name = "VwRequerentePfExterno.findByIdePessoa", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.idePessoa = :idePessoa"),
		@NamedQuery(name = "VwRequerentePfExterno.findByDtcCriacao", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.dtcCriacao = :dtcCriacao"),
		@NamedQuery(name = "VwRequerentePfExterno.findByIndExcluido", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.indExcluido = :indExcluido"),
		@NamedQuery(name = "VwRequerentePfExterno.findByDesEmail", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.desEmail = :desEmail"),
		@NamedQuery(name = "VwRequerentePfExterno.findByDtcExclusao", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.dtcExclusao = :dtcExclusao"),
		@NamedQuery(name = "VwRequerentePfExterno.findByIdePessoaFisica", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.idePessoaFisica = :idePessoaFisica"),
		@NamedQuery(name = "VwRequerentePfExterno.findByIdeEstadoCivil", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.ideEstadoCivil = :ideEstadoCivil"),
		@NamedQuery(name = "VwRequerentePfExterno.findByIdeEscolaridade", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.ideEscolaridade = :ideEscolaridade"),
		@NamedQuery(name = "VwRequerentePfExterno.findByIdeOcupacao", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.ideOcupacao = :ideOcupacao"),
		@NamedQuery(name = "VwRequerentePfExterno.findByIdePais", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.idePais = :idePais"),
		@NamedQuery(name = "VwRequerentePfExterno.findByNomPessoa", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.nomPessoa = :nomPessoa"),
		@NamedQuery(name = "VwRequerentePfExterno.findByTipSexo", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.tipSexo = :tipSexo"),
		@NamedQuery(name = "VwRequerentePfExterno.findByDtcNascimento", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.dtcNascimento = :dtcNascimento"),
		@NamedQuery(name = "VwRequerentePfExterno.findByDesNaturalidade", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.desNaturalidade = :desNaturalidade"),
		@NamedQuery(name = "VwRequerentePfExterno.findByNomPai", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.nomPai = :nomPai"),
		@NamedQuery(name = "VwRequerentePfExterno.findByNomMae", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.nomMae = :nomMae"),
		@NamedQuery(name = "VwRequerentePfExterno.findByNumCpf", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.numCpf = :numCpf"),
		@NamedQuery(name = "VwRequerentePfExterno.findByIdeEstado", query = "SELECT v FROM VwRequerentePfExterno v WHERE v.ideEstado = :ideEstado")})
public class VwRequerentePfExterno implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ide_pessoa")
	private Integer idePessoa;
	@Column(name = "dtc_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	@Column(name = "ind_excluido")
	private Boolean indExcluido;
	@Column(name = "des_email", length = 70)
	private String desEmail;
	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;
	@Column(name = "ide_pessoa_fisica")
	private Integer idePessoaFisica;
	@Column(name = "ide_estado_civil")
	private Integer ideEstadoCivil;
	@Column(name = "ide_escolaridade")
	private Integer ideEscolaridade;
	@Column(name = "ide_ocupacao")
	private Integer ideOcupacao;
	@Column(name = "ide_pais")
	private Integer idePais;
	@Column(name = "nom_pessoa", length = 200)
	private String nomPessoa;
	@Column(name = "tip_sexo")
	private Integer tipSexo;
	@Column(name = "dtc_nascimento")
	@Temporal(TemporalType.DATE)
	private Date dtcNascimento;
	@Column(name = "des_naturalidade", length = 70)
	private String desNaturalidade;
	@Column(name = "nom_pai", length = 200)
	private String nomPai;
	@Column(name = "nom_mae", length = 200)
	private String nomMae;
	@Column(name = "num_cpf", length = 11)
	private String numCpf;
	@Column(name = "ide_estado")
	private Integer ideEstado;

	public VwRequerentePfExterno() {
	}

	public Integer getIdePessoa() {
		return idePessoa;
	}

	public void setIdePessoa(Integer idePessoa) {
		this.idePessoa = idePessoa;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public String getDesEmail() {
		return desEmail;
	}

	public void setDesEmail(String desEmail) {
		this.desEmail = desEmail;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Integer getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Integer idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public Integer getIdeEstadoCivil() {
		return ideEstadoCivil;
	}

	public void setIdeEstadoCivil(Integer ideEstadoCivil) {
		this.ideEstadoCivil = ideEstadoCivil;
	}

	public Integer getIdeEscolaridade() {
		return ideEscolaridade;
	}

	public void setIdeEscolaridade(Integer ideEscolaridade) {
		this.ideEscolaridade = ideEscolaridade;
	}

	public Integer getIdeOcupacao() {
		return ideOcupacao;
	}

	public void setIdeOcupacao(Integer ideOcupacao) {
		this.ideOcupacao = ideOcupacao;
	}

	public Integer getIdePais() {
		return idePais;
	}

	public void setIdePais(Integer idePais) {
		this.idePais = idePais;
	}

	public String getNomPessoa() {
		return nomPessoa;
	}

	public void setNomPessoa(String nomPessoa) {
		this.nomPessoa = nomPessoa;
	}

	public Integer getTipSexo() {
		return tipSexo;
	}

	public void setTipSexo(Integer tipSexo) {
		this.tipSexo = tipSexo;
	}

	public Date getDtcNascimento() {
		return dtcNascimento;
	}

	public void setDtcNascimento(Date dtcNascimento) {
		this.dtcNascimento = dtcNascimento;
	}

	public String getDesNaturalidade() {
		return desNaturalidade;
	}

	public void setDesNaturalidade(String desNaturalidade) {
		this.desNaturalidade = desNaturalidade;
	}

	public String getNomPai() {
		return nomPai;
	}

	public void setNomPai(String nomPai) {
		this.nomPai = nomPai;
	}

	public String getNomMae() {
		return nomMae;
	}

	public void setNomMae(String nomMae) {
		this.nomMae = nomMae;
	}

	public String getNumCpf() {
		return numCpf;
	}

	public void setNumCpf(String numCpf) {
		this.numCpf = numCpf;
	}

	public Integer getIdeEstado() {
		return ideEstado;
	}

	public void setIdeEstado(Integer ideEstado) {
		this.ideEstado = ideEstado;
	}

	
}
