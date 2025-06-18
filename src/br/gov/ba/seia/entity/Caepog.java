package br.gov.ba.seia.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * Tabela principal que armazena o cadastro de atividades de exploração e produção de óleo e gás
 */
@Entity
@Table(name = "caepog")
public class Caepog extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caepog_ide_caepog_seq")
	@SequenceGenerator(name = "caepog_ide_caepog_seq", sequenceName = "caepog_ide_caepog_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_caepog", nullable = false)
	private Integer ideCaepog;
	
	/**
	 * Indicador de exclusão lógica do cadastro, sendo true para excluído e false para válido
	 */
	@Column(name = "ind_excluido_caepog")
	private Boolean indExcluidoCaepog;
	
	/**
	 * Número único da caepog
	 */
	@Column(name = "num_caepog")
	private String numCaepog;
	
	/**
	 * Data de criação da atividade
	 */
	@Basic(optional = false)
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	/**
	 * Chave estrangeira da pessoa requerente
	 */
	@JoinColumn(name = "ide_pessoa_requerente", referencedColumnName = "ide_pessoa")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pessoa idePessoaRequerente;
	
	/**
	 * Chave estrangeira da pessoa física responsável pelo cadastro
	 */
	@JoinColumn(name = "ide_pessoa_fisica_cadastro", referencedColumnName = "ide_pessoa_fisica")
	@ManyToOne(fetch = FetchType.LAZY)
	private PessoaFisica idePessoaFisicaCadastro;
	
	/**
	 * Chave estrangeira do empreendimento associado ao cadastro CAEPOG
	 */
	@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento")
	@ManyToOne(fetch = FetchType.LAZY)
	private Empreendimento  ideEmpreendimento;
	
	/**
	 * 
	 * COLLECTIONS
	 * 
	 */
	
	@OneToMany(mappedBy = "ideCaepog", fetch = FetchType.LAZY)
	private Collection<CaepogDefinicaoCampo> caepogDefinicaoCampoCollection;
	
	@OneToMany(mappedBy = "ideCaepog", fetch = FetchType.LAZY)
	private Collection<CaepogStatus> caepogStatusCollection;
	
	@OneToMany(mappedBy = "ideCaepog", fetch = FetchType.LAZY)
	private Collection<CaepogCertificado> caepogCertificadoCollection;
	
	/**
	 * 
	 * TRANSIENTES
	 * 
	 */
	
	@Transient
	private Municipio ideMunicipioTransient;
	
	@Transient
	private CaepogStatus ideUltimoStatus;
	
	@Transient
	private boolean isVisualizando;
	
	@Transient
	private boolean isCadastroIncompleto;
	
	@Transient
	private boolean isAguardandoValidacao;
	
	@Transient
	private boolean isPendenciaValidacao;
	
	@Transient
	private boolean isValidado;
	
	@Transient
	private boolean isPendenciaLocalizacao;
	
	@Transient
	private String msgErroPendenciaLocalizacao;
	
	@Transient
	private boolean validando;

	public Caepog() {
		super();
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */
	
	public Integer getIdeCaepog() {
		return ideCaepog;
	}

	public void setIdeCaepog(Integer ideCaepog) {
		this.ideCaepog = ideCaepog;
	}

	public Boolean getIndExcluidoCaepog() {
		return indExcluidoCaepog;
	}

	public void setIndExcluidoCaepog(Boolean indExcluidoCaepog) {
		this.indExcluidoCaepog = indExcluidoCaepog;
	}

	public Pessoa getIdePessoaRequerente() {
		return idePessoaRequerente;
	}

	public void setIdePessoaRequerente(Pessoa idePessoaRequerente) {
		this.idePessoaRequerente = idePessoaRequerente;
	}

	public PessoaFisica getIdePessoaFisicaCadastro() {
		return idePessoaFisicaCadastro;
	}

	public void setIdePessoaFisicaCadastro(PessoaFisica idePessoaFisicaCadastro) {
		this.idePessoaFisicaCadastro = idePessoaFisicaCadastro;
	}

	public Empreendimento getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public Collection<CaepogDefinicaoCampo> getCaepogDefinicaoCampoCollection() {
		return caepogDefinicaoCampoCollection;
	}

	public void setCaepogDefinicaoCampoCollection(Collection<CaepogDefinicaoCampo> caepogDefinicaoCampoCollection) {
		this.caepogDefinicaoCampoCollection = caepogDefinicaoCampoCollection;
	}

	public Municipio getIdeMunicipioTransient() {
		return ideMunicipioTransient;
	}

	public void setIdeMunicipioTransient(Municipio ideMunicipioTransient) {
		this.ideMunicipioTransient = ideMunicipioTransient;
	}

	public Collection<CaepogStatus> getCaepogStatusCollection() {
		return caepogStatusCollection;
	}

	public void setCaepogStatusCollection(Collection<CaepogStatus> caepogStatusCollection) {
		this.caepogStatusCollection = caepogStatusCollection;
	}

	public CaepogStatus getIdeUltimoStatus() {
		return ideUltimoStatus;
	}

	public void setIdeUltimoStatus(CaepogStatus ideUltimoStatus) {
		this.ideUltimoStatus = ideUltimoStatus;
	}

	public String getNumCaepog() {
		return numCaepog;
	}

	public void setNumCaepog(String numCaepog) {
		this.numCaepog = numCaepog;
	}

	public boolean isVisualizando() {
		return isVisualizando;
	}

	public void setVisualizando(boolean isVisualizando) {
		this.isVisualizando = isVisualizando;
	}
	
	public Collection<CaepogCertificado> getCaepogCertificadoCollection() {
		return caepogCertificadoCollection;
	}
	
	public void setCaepogCertificadoCollection(Collection<CaepogCertificado> caepogCertificadoCollection) {
		this.caepogCertificadoCollection = caepogCertificadoCollection;
	}

	public boolean isCadastroIncompleto() {
		return isCadastroIncompleto;
	}

	public void setCadastroIncompleto(boolean isCadastroIncompleto) {
		this.isCadastroIncompleto = isCadastroIncompleto;
	}

	public boolean isAguardandoValidacao() {
		return isAguardandoValidacao;
	}

	public void setAguardandoValidacao(boolean isAguardandoValidacao) {
		this.isAguardandoValidacao = isAguardandoValidacao;
	}

	public boolean isPendenciaValidacao() {
		return isPendenciaValidacao;
	}

	public void setPendenciaValidacao(boolean isPendenciaValidacao) {
		this.isPendenciaValidacao = isPendenciaValidacao;
	}

	public boolean isValidado() {
		return isValidado;
	}

	public void setValidado(boolean isValidado) {
		this.isValidado = isValidado;
	}

	public boolean isPendenciaLocalizacao() {
		return isPendenciaLocalizacao;
	}

	public void setPendenciaLocalizacao(boolean isPendenciaLocalizacao) {
		this.isPendenciaLocalizacao = isPendenciaLocalizacao;
	}

	public String getMsgErroPendenciaLocalizacao() {
		return msgErroPendenciaLocalizacao;
	}

	public void setMsgErroPendenciaLocalizacao(String msgErroPendenciaLocalizacao) {
		this.msgErroPendenciaLocalizacao = msgErroPendenciaLocalizacao;
	}

	public boolean isValidando() {
		return validando;
	}

	public void setValidando(boolean validando) {
		this.validando = validando;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}
}