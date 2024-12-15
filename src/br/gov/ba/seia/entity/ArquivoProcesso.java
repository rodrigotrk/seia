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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "arquivo_processo", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_arquivo_processo" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ArquivoProcesso.findAll", query = "SELECT a FROM ArquivoProcesso a"),
		@NamedQuery(name = "ArquivoProcesso.findByIdeArquivoProcesso", query = "SELECT a FROM ArquivoProcesso a WHERE a.ideArquivoProcesso = :ideArquivoProcesso"),
		@NamedQuery(name = "ArquivoProcesso.findByDscCaminhoArquivo", query = "SELECT a FROM ArquivoProcesso a WHERE a.dscCaminhoArquivo = :dscCaminhoArquivo"),
		@NamedQuery(name = "ArquivoProcesso.findByDtcCriacao", query = "SELECT a FROM ArquivoProcesso a WHERE a.dtcCriacao = :dtcCriacao"),
		@NamedQuery(name = "ArquivoProcesso.findByIndExcluido", query = "SELECT a FROM ArquivoProcesso a WHERE a.indExcluido = :indExcluido"),
		@NamedQuery(name = "ArquivoProcesso.findByDtcExclusao", query = "SELECT a FROM ArquivoProcesso a WHERE a.dtcExclusao = :dtcExclusao"),
		@NamedQuery(name = "ArquivoProcesso.findByDtcAlteracao", query = "SELECT a FROM ArquivoProcesso a WHERE a.dtcAlteracao = :dtcAlteracao"),
		@NamedQuery(name = "ArquivoProcesso.findByDscArquivoProcesso", query = "SELECT a FROM ArquivoProcesso a WHERE a.dscArquivoProcesso = :dscArquivoProcesso") })
public class ArquivoProcesso implements Serializable, Comparable<ArquivoProcesso>, GeoReferenciavel {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ARQUIVO_PROCESSO_IDE_ARQUIVO_PROCESSO_seq")
	@SequenceGenerator(name = "ARQUIVO_PROCESSO_IDE_ARQUIVO_PROCESSO_seq", sequenceName = "ARQUIVO_PROCESSO_IDE_ARQUIVO_PROCESSO_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_arquivo_processo", nullable = false)
	private Integer ideArquivoProcesso;
	
	@Size(min = 1, max = 150)
	@Column(name = "dsc_caminho_arquivo", length = 150)
	private String dscCaminhoArquivo;
	
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;
	
	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;
	
	@Column(name = "dtc_alteracao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcAlteracao;
	
	@Size(max=150)
	@Column(name = "dsc_arquivo_processo", nullable = true)
	private String dscArquivoProcesso;
	
	@JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Processo ideProcesso;
	
	@JoinColumn(name = "ide_pessoa_upload", referencedColumnName = "ide_pessoa", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Pessoa idePessoaUpload;
	
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica", nullable = true)
	@ManyToOne(fetch = FetchType.EAGER)
	private LocalizacaoGeografica localizacaoGeografica;

	@JoinColumn(name = "ide_categoria_documento", referencedColumnName = "ide_categoria_documento", nullable = true)
	@ManyToOne(fetch = FetchType.EAGER)
	private CategoriaDocumento categoriaDocumento;

	@JoinColumn(name = "ide_motivo_notificacao", referencedColumnName = "ide_motivo_notificacao", nullable = true)
	@ManyToOne(fetch = FetchType.EAGER)
	private MotivoNotificacao motivoNotificacao;
	
	@JoinColumn(name = "ide_notificacao", referencedColumnName = "ide_notificacao", nullable = true)
	@ManyToOne(fetch = FetchType.EAGER)
	private Notificacao ideNotificacao;

	@JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = true)
	@ManyToOne(fetch = FetchType.EAGER)
	private Imovel ideImovel;
	
	@Transient
	private StreamedContent file;
	@Transient
	private Long fileSize;
	@Transient
	private String fileName;
	@Transient
	private boolean dscConfirmada;
	@Transient
	private boolean permiteDownload;
	@Transient
	private boolean indConcedido;
	
	public ArquivoProcesso() {}

	public ArquivoProcesso(Integer ideArquivoProcesso) {
		this.ideArquivoProcesso = ideArquivoProcesso;
	}

	public ArquivoProcesso(Integer ideArquivoProcesso, String dscCaminhoArquivo, Date dtcCriacao, boolean indExcluido, String dscArquivoProcesso) {
		this.ideArquivoProcesso = ideArquivoProcesso;
		this.dscCaminhoArquivo = dscCaminhoArquivo;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
		this.dscArquivoProcesso = dscArquivoProcesso;
	}

	public ArquivoProcesso(String caminho, String descricao, Processo processo, Pessoa userlogado) {
		this.dscCaminhoArquivo = caminho;
		this.ideProcesso = processo;
		this.dscArquivoProcesso = descricao;
		this.ideProcesso = processo;
		this.idePessoaUpload = userlogado;
	}
	
	public ArquivoProcesso(String caminho, String descricao, Pessoa userlogado) {
		this.dscCaminhoArquivo = caminho;
		this.dscArquivoProcesso = descricao;
		this.idePessoaUpload = userlogado;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dscCaminhoArquivo == null) ? 0 : dscCaminhoArquivo.hashCode());
		result = prime * result + ((dtcCriacao == null) ? 0 : dtcCriacao.hashCode());
		result = prime * result + ((idePessoaUpload == null) ? 0 : idePessoaUpload.hashCode());
		result = prime * result + ((ideProcesso == null) ? 0 : ideProcesso.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArquivoProcesso other = (ArquivoProcesso) obj;
		if (dscCaminhoArquivo == null) {
			if (other.dscCaminhoArquivo != null)
				return false;
		} else if (!dscCaminhoArquivo.equals(other.dscCaminhoArquivo))
			return false;
		if (dtcCriacao == null) {
			if (other.dtcCriacao != null)
				return false;
		} else if (!dtcCriacao.equals(other.dtcCriacao))
			return false;
		if (idePessoaUpload == null) {
			if (other.idePessoaUpload != null)
				return false;
		} else if (!idePessoaUpload.equals(other.idePessoaUpload))
			return false;
		if (ideProcesso == null) {
			if (other.ideProcesso != null)
				return false;
		} else if (!ideProcesso.equals(other.ideProcesso))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.ArquivoProcesso[ ideArquivoProcesso=" + ideArquivoProcesso + " ]";
	}
	
	public String getFileSizeFormated() {
		if (this.getFileSize() != null) {
			return Long.valueOf(this.getFileSize() / 1024).toString() + " Kb";
		} else
			return "0 Kb";
	}
	
	public boolean isPermiteDownload() {
		if(!Util.isNullOuVazio(dscCaminhoArquivo) && !Util.isNullOuVazio(dscArquivoProcesso)  ){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int compareTo(ArquivoProcesso o) {
		return o.dtcCriacao.compareTo(this.dtcCriacao);
	}

	public String getNomeArquivo() {
		return FileUploadUtil.getFileName(dscCaminhoArquivo);
	}

	@Override
	public Integer getIdeRequerimento() {
		return ideProcesso.getIdeRequerimento().getIdeRequerimento();
	}
	
	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return localizacaoGeografica;
	}
	
	@Override
	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	public Integer getIdeArquivoProcesso() {
		return ideArquivoProcesso;
	}

	public void setIdeArquivoProcesso(Integer ideArquivoProcesso) {
		this.ideArquivoProcesso = ideArquivoProcesso;
	}

	public String getDscCaminhoArquivo() {
		return dscCaminhoArquivo;
	}

	public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
		this.dscCaminhoArquivo = dscCaminhoArquivo;
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

	public Date getDtcAlteracao() {
		return dtcAlteracao;
	}

	public void setDtcAlteracao(Date dtcAlteracao) {
		this.dtcAlteracao = dtcAlteracao;
	}

	public String getDscArquivoProcesso() {
		return dscArquivoProcesso;
	}

	public String getDescricao() {
		return Util.isNull(dscArquivoProcesso) && !Util.isNull(this.motivoNotificacao) ? this.motivoNotificacao.getNomMotivoNotificacao() : dscArquivoProcesso;
	}

	public void setDscArquivoProcesso(String dscArquivoProcesso) {
		this.dscArquivoProcesso = dscArquivoProcesso;
	}

	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}

	public Pessoa getIdePessoaUpload() {
		return idePessoaUpload;
	}

	public void setIdePessoaUpload(Pessoa idePessoaUpload) {
		this.idePessoaUpload = idePessoaUpload;
	}

	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	public MotivoNotificacao getMotivoNotificacao() {
		return motivoNotificacao;
	}

	public void setMotivoNotificacao(MotivoNotificacao motivoNotificacao) {
		this.motivoNotificacao = motivoNotificacao;
	}

	public CategoriaDocumento getCategoriaDocumento() {
		return categoriaDocumento;
	}

	public void setCategoriaDocumento(CategoriaDocumento categoriaDocumento) {
		this.categoriaDocumento = categoriaDocumento;
	}
	
	public StreamedContent getFile() {
		return file;
	}
	
	public void setFile(StreamedContent file) {
		this.file = file;
	}
	
	public Long getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setPermiteDownload(boolean permiteDownload) {
		this.permiteDownload = permiteDownload;
	}

	public Notificacao getIdeNotificacao() {
		return ideNotificacao;
	}

	public void setIdeNotificacao(Notificacao ideNotificacao) {
		this.ideNotificacao = ideNotificacao;
	}

	public boolean isDscConfirmada() {
		return dscConfirmada;
	}

	public void setDscConfirmada(boolean dscConfirmada) {
		this.dscConfirmada = dscConfirmada;
	}

	public Imovel getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

	public boolean isIndConcedido() {
		return indConcedido;
	}

	public void setIndConcedido(boolean indConcedido) {
		this.indConcedido = indConcedido;
	}

	@Override
	public Integer getIde() {
		return getIdeArquivoProcesso();
	}

	@Override
	public void setIde(Integer ide) {
		setIdeArquivoProcesso(ide);
	}
	
}