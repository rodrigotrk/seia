package br.gov.ba.seia.entity;

import java.io.File;
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
import javax.xml.bind.annotation.XmlRootElement;

import org.primefaces.model.UploadedFile;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "documento_identificacao")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "DocumentoIdentificacao.findByIdeDocumentoIdentificacao", query = "SELECT d FROM DocumentoIdentificacao d WHERE d.ideDocumentoIdentificacao = :ideDocumentoIdentificacao and d.indExcluido = :indExcluido"),
		})
public class DocumentoIdentificacao extends AbstractEntity implements Cloneable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ide_documento_identificacao", nullable = false)
	@SequenceGenerator(name = "DOCUMENTO_IDENTIFICACAO_IDE_DOCUMENTO_IDENTIFICACAO_seq", sequenceName = "DOCUMENTO_IDENTIFICACAO_IDE_DOCUMENTO_IDENTIFICACAO_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_IDENTIFICACAO_IDE_DOCUMENTO_IDENTIFICACAO_seq")
	private Integer ideDocumentoIdentificacao;
	
	@Basic(optional = false)
	@Column(name = "num_documento", nullable = false, length = 50)
	private String numDocumento;
	
	@Column(name = "num_serie", length = 15)
	private String numSerie;
	
	@Basic(optional = false)
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	@Basic(optional = false)
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;
	
	@Column(name = "dtc_emissao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcEmissao;
	
	@Column(name = "dtc_validade")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidade;
	
	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;
	
	@Column(name = "org_expedidor_outros", length = 150)
	private String orgExpedidorOutros;
	
	@Basic(optional = false)
	@Column(name = "dsc_caminho_arquivo", nullable = false, length = 255)
	private String dscCaminhoArquivo;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideDocumentoIdentificacao", fetch = FetchType.LAZY)
	private Collection<DocumentoIdentificacaoRequerimento> documentoIdentificacaoRequerimentoCollection;
	
	@JoinColumn(name = "ide_tipo_identificacao", referencedColumnName = "ide_tipo_identificacao", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoIdentificacao ideTipoIdentificacao;
	
	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Pessoa idePessoa;
	
	@JoinColumn(name = "ide_orgao_expedidor", referencedColumnName = "ide_orgao_expedidor", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private OrgaoExpedidor ideOrgaoExpedidor;
	
	@JoinColumn(name = "ide_estado", referencedColumnName = "ide_estado")
	@ManyToOne(fetch = FetchType.LAZY)
	private Estado ideEstado;
	
	@Transient
	private UploadedFile arquivoEnviado;

	public DocumentoIdentificacao() {
	}

	public DocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
		this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
	}

	public DocumentoIdentificacao(Integer ideDocumentoIdentificacao, String numDocumento, Date dtcCriacao,
			boolean indExcluido) {
		this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
		this.numDocumento = numDocumento;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
	}
	
	public String getDscCaminhoArquivoNome() {
		return dscCaminhoArquivo.substring(dscCaminhoArquivo.lastIndexOf("/")+1);
	}

	public Integer getIdeDocumentoIdentificacao() {
		return ideDocumentoIdentificacao;
	}

	public void setIdeDocumentoIdentificacao(Integer ideDocumentoIdentificacao) {
		this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public String getNumSerie() {
		return numSerie;
	}

	public void setNumSerie(String numSerie) {
		this.numSerie = numSerie;
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

	public Date getDtcEmissao() {
		return dtcEmissao;
	}

	public void setDtcEmissao(Date dtcEmissao) {
		this.dtcEmissao = dtcEmissao;
	}

	public Date getDtcValidade() {
		return dtcValidade;
	}

	public void setDtcValidade(Date dtcValidade) {
		this.dtcValidade = dtcValidade;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public String getOrgExpedidorOutros() {
		return orgExpedidorOutros;
	}

	public void setOrgExpedidorOutros(String orgExpedidorOutros) {
		this.orgExpedidorOutros = orgExpedidorOutros;
	}

	public TipoIdentificacao getIdeTipoIdentificacao() {
		return ideTipoIdentificacao;
	}

	public void setIdeTipoIdentificacao(TipoIdentificacao ideTipoIdentificacao) {
		this.ideTipoIdentificacao = ideTipoIdentificacao;
	}

	public Pessoa getIdePessoa() {
		return idePessoa;
	}

	public void setIdePessoa(Pessoa idePessoa) {
		this.idePessoa = idePessoa;
	}

	public OrgaoExpedidor getIdeOrgaoExpedidor() {
		return ideOrgaoExpedidor;
	}

	public void setIdeOrgaoExpedidor(OrgaoExpedidor ideOrgaoExpedidor) {
		this.ideOrgaoExpedidor = ideOrgaoExpedidor;
	}

	public Estado getIdeEstado() {
		return ideEstado;
	}

	public void setIdeEstado(Estado ideEstado) {
		this.ideEstado = ideEstado;
	}

	public Collection<DocumentoIdentificacaoRequerimento> getDocumentoIdentificacaoRequerimentoCollection() {
		return documentoIdentificacaoRequerimentoCollection;
	}

	public void setDocumentoIdentificacaoRequerimentoCollection(Collection<DocumentoIdentificacaoRequerimento> documentoIdentificacaoRequerimentoCollection) {
		this.documentoIdentificacaoRequerimentoCollection = documentoIdentificacaoRequerimentoCollection;
	}

	public String getFileName() {
		if (!Util.isNullOuVazio(this.dscCaminhoArquivo)) {
			return FileUploadUtil.getFileName(this.dscCaminhoArquivo);
		}
		else if (!Util.isNull(this.arquivoEnviado)) {
			return this.arquivoEnviado.getFileName();
		}
		return "";
	}

	public String getFileSize() {
		File arquivo = null;
		if (!Util.isNull(this.dscCaminhoArquivo)) {
			arquivo = new File(this.dscCaminhoArquivo);
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		}
		return "";
	}

	public String getDscCaminhoArquivo() {
		return dscCaminhoArquivo;
	}

	public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}
	
	public DocumentoIdentificacao clone() throws CloneNotSupportedException {
		return (DocumentoIdentificacao) super.clone();
	}

	public UploadedFile getArquivoEnviado() {
		return arquivoEnviado;
	}

	public void setArquivoEnviado(UploadedFile arquivoEnviado) {
		this.arquivoEnviado = arquivoEnviado;
	}
}
