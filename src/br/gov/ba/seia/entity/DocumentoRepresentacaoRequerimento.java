package br.gov.ba.seia.entity;

import java.io.File;
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
import javax.xml.bind.annotation.XmlRootElement;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author MJunior
 */
@Entity
@Table(name = "documento_representacao_requerimento")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "DocumentoRepresentacaoRequerimento.findAll", query = "SELECT d FROM DocumentoRepresentacaoRequerimento d"),
		@NamedQuery(name = "DocumentoRepresentacaoRequerimento.findByIdeDocumentoRepresentacaoRequerimento",
				query = "SELECT d FROM DocumentoRepresentacaoRequerimento d WHERE d.ideDocumentoRepresentacaoRequerimento = :ideDocumentoRepresentacaoRequerimento"),
		@NamedQuery(name = "DocumentoRepresentacaoRequerimento.findByIndDocumentoValidado", query = "SELECT d FROM DocumentoRepresentacaoRequerimento d WHERE d.indDocumentoValidado = :indDocumentoValidado"),
		@NamedQuery(name = "DocumentoRepresentacaoRequerimento.findByDtcValidacao", query = "SELECT d FROM DocumentoRepresentacaoRequerimento d WHERE d.dtcValidacao = :dtcValidacao") })
public class DocumentoRepresentacaoRequerimento implements Serializable,DocumentoValidacao {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "ide_documento_representacao_requerimento_seq", sequenceName = "ide_documento_representacao_requerimento_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ide_documento_representacao_requerimento_seq")
	@Basic(optional = false)
	@Column(name = "ide_documento_representacao_requerimento", nullable = false)
	private Integer ideDocumentoRepresentacaoRequerimento;
	
	@Basic(optional = false)
	@Column(name = "ind_documento_validado", nullable = false)
	private boolean indDocumentoValidado;
	
	@Column(name = "dtc_validacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidacao;
	
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@ManyToOne(fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;
	
	@JoinColumn(name = "ide_representante_legal", referencedColumnName = "ide_representante_legal")
	@ManyToOne(fetch = FetchType.EAGER)
	private RepresentanteLegal ideRepresentanteLegal;
	
	@JoinColumn(name = "ide_procurador_rep_empreendimento", referencedColumnName = "ide_procurador_rep_empreendimento")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProcuradorRepEmpreendimento ideProcuradorRepEmpreendimento;
    
	@JoinColumn(name = "ide_procurador_pf_empreendimento", referencedColumnName = "ide_procurador_pf_empreendimento")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProcuradorPfEmpreendimento ideProcuradorPfEmpreendimento;
	
	@JoinColumn(name = "ide_pessoa_validacao", referencedColumnName = "ide_pessoa")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pessoa idePessoaValidacao;
	
	@Transient
	private String tipoDocumento;
	
	@Column(name = "dsc_caminho_arquivo", length = 1000)
	private String dscCaminhoArquivo;
	
	@Transient
	private StreamedContent file;
	
	@Transient
	private Long fileSize;
	
	@Transient
	private Boolean arquivoChanged;
	
	@Transient
	private FileUploadEvent fileUpload;
	
	@Transient 
	private String caminhoTransient; 
	
	@Transient 
	private Long fileSizeTransient;
	
	public String getCaminhoArquivoTransient(){
		
		if(!Util.isNullOuVazio(dscCaminhoArquivo)){
			caminhoTransient = dscCaminhoArquivo;
		}
		else if (!Util.isNullOuVazio(ideProcuradorRepEmpreendimento) && !Util.isNullOuVazio(ideProcuradorRepEmpreendimento.getDscCaminhoProcuracao())){
			caminhoTransient = ideProcuradorRepEmpreendimento.getDscCaminhoProcuracao();
		}
		else if(!Util.isNullOuVazio(ideProcuradorPfEmpreendimento) && !Util.isNullOuVazio(ideProcuradorPfEmpreendimento.getDscCaminhoProcuracao())){
			caminhoTransient = ideProcuradorPfEmpreendimento.getDscCaminhoProcuracao();
		}
		else if(!Util.isNullOuVazio(ideRepresentanteLegal) && !Util.isNullOuVazio(ideRepresentanteLegal.getDscCaminhoRepresentacao())){
			caminhoTransient = ideRepresentanteLegal.getDscCaminhoRepresentacao();
		}
		else{
			caminhoTransient = " - ";
		}
		
			return caminhoTransient;
	}
	
	public String getFileNameTransient(){
		return FileUploadUtil.getFileName(getCaminhoArquivoTransient());
	}
	

	public String getTipoDocumento() {
		
		if(Util.isNullOuVazio(tipoDocumento)){
			if(!Util.isNull(ideProcuradorPfEmpreendimento) || !Util.isNull(ideProcuradorRepEmpreendimento)){
				this.tipoDocumento = "Procurador Empreendimento"; 
			}else if(!Util.isNull(ideRepresentanteLegal)){
				this.tipoDocumento = "Representante Legal"; 
			}
		}
		
		return tipoDocumento;
	}
	
	public String getDscCaminhoArquivoNome() {
		
		if(dscCaminhoArquivo==null){
			return "";
		}
		
		return new File(dscCaminhoArquivo).getName();
	}
	
	@Override
	public String getDescricao() {
		return this.getTipoDocumento();
	}
	
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
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

	public Boolean getArquivoChanged() {
		return arquivoChanged;
	}

	public void setArquivoChanged(Boolean arquivoChanged) {
		this.arquivoChanged = arquivoChanged;
	}

	public DocumentoRepresentacaoRequerimento() {
	}

	public DocumentoRepresentacaoRequerimento(Integer ideDocumentoRepresentacaoRequerimento) {
		this.ideDocumentoRepresentacaoRequerimento = ideDocumentoRepresentacaoRequerimento;
	}

	public DocumentoRepresentacaoRequerimento(Integer ideDocumentoRepresentacaoRequerimento, boolean indDocumentoValidado) {
		this.ideDocumentoRepresentacaoRequerimento = ideDocumentoRepresentacaoRequerimento;
		this.indDocumentoValidado = indDocumentoValidado;
	}

	public Integer getIdeDocumentoRepresentacaoRequerimento() {
		return ideDocumentoRepresentacaoRequerimento;
	}

	public void setIdeDocumentoRepresentacaoRequerimento(Integer ideDocumentoRepresentacaoRequerimento) {
		this.ideDocumentoRepresentacaoRequerimento = ideDocumentoRepresentacaoRequerimento;
	}

	public boolean getIndDocumentoValidado() {
		return indDocumentoValidado;
	}

	public void setIndDocumentoValidado(boolean indDocumentoValidado) {
		this.indDocumentoValidado = indDocumentoValidado;
	}

	public Date getDtcValidacao() {
		return dtcValidacao;
	}

	public void setDtcValidacao(Date dtcValidacao) {
		this.dtcValidacao = dtcValidacao;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public RepresentanteLegal getIdeRepresentanteLegal() {
		return ideRepresentanteLegal;
	}

	public void setIdeRepresentanteLegal(RepresentanteLegal ideRepresentanteLegal) {
		this.ideRepresentanteLegal = ideRepresentanteLegal;
	}

	public Pessoa getIdePessoaValidacao() {
		return idePessoaValidacao;
	}

	public void setIdePessoaValidacao(Pessoa idePessoaValidacao) {
		this.idePessoaValidacao = idePessoaValidacao;
	}

	public ProcuradorRepEmpreendimento getIdeProcuradorRepEmpreendimento() {
		return ideProcuradorRepEmpreendimento;
	}

	public void setIdeProcuradorRepEmpreendimento(
			ProcuradorRepEmpreendimento ideProcuradorRepEmpreendimento) {
		this.ideProcuradorRepEmpreendimento = ideProcuradorRepEmpreendimento;
	}

	public ProcuradorPfEmpreendimento getIdeProcuradorPfEmpreendimento() {
		return ideProcuradorPfEmpreendimento;
	}

	public void setIdeProcuradorPfEmpreendimento(
			ProcuradorPfEmpreendimento ideProcuradorPfEmpreendimento) {
		this.ideProcuradorPfEmpreendimento = ideProcuradorPfEmpreendimento;
	}

	public FileUploadEvent getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(FileUploadEvent fileUpload) {
		this.fileUpload = fileUpload;
	}
	
	public void setDscCaminhoArquivo(String caminho) {
		if(!Util.isNull(ideProcuradorPfEmpreendimento)) {
			ideProcuradorPfEmpreendimento.setDscCaminhoProcuracao(caminho);
			
		} else if(!Util.isNull(ideProcuradorRepEmpreendimento)) {
			ideProcuradorRepEmpreendimento.setDscCaminhoProcuracao(caminho);
			
		} else if(!Util.isNull(ideRepresentanteLegal)) {
			ideRepresentanteLegal.setDscCaminhoRepresentacao(caminho);
			
		}
		this.dscCaminhoArquivo = caminho;
	}
	
	public String getDscCaminhoArquivo() {
		if(!Util.isNullOuVazio(dscCaminhoArquivo)) {
			return this.dscCaminhoArquivo;
		
		} else if(!Util.isNull(ideProcuradorPfEmpreendimento)) {
			return ideProcuradorPfEmpreendimento.getDscCaminhoProcuracao(); 
		
		} else if(!Util.isNull(ideRepresentanteLegal)) {
			return ideRepresentanteLegal.getDscCaminhoRepresentacao();
		}
		
		return "";
	}
	
	public String getFileName() {
		if (!Util.isNullOuVazio(this.dscCaminhoArquivo)) {
			return FileUploadUtil.getFileName(this.dscCaminhoArquivo);
		
		} else if(!Util.isNull(ideProcuradorPfEmpreendimento)) {
			return FileUploadUtil.getFileName(ideProcuradorPfEmpreendimento.getDscCaminhoProcuracao()); 
		
		} else if(!Util.isNull(ideRepresentanteLegal)) {
			return FileUploadUtil.getFileName(ideRepresentanteLegal.getDscCaminhoRepresentacao());
		}
		
		return "";
	}
	
	public String getSizeFile() {
		
		
		File arquivo = null;
		
		if (!Util.isNull(this.caminhoTransient)) {
			arquivo = new File(this.caminhoTransient);
			
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		}
		
		if(!Util.isNull(ideProcuradorPfEmpreendimento)) {
			arquivo = new File(this.ideProcuradorPfEmpreendimento.getDscCaminhoProcuracao());
			
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		} else if(!Util.isNull(ideRepresentanteLegal)) {
			arquivo = new File(ideRepresentanteLegal.getDscCaminhoRepresentacao());
			
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
			}
		}
		
		return "";
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideDocumentoRepresentacaoRequerimento != null ? ideDocumentoRepresentacaoRequerimento.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof DocumentoRepresentacaoRequerimento)) {
			return false;
		}
		DocumentoRepresentacaoRequerimento other = (DocumentoRepresentacaoRequerimento) object;
		if ((this.ideDocumentoRepresentacaoRequerimento == null && other.ideDocumentoRepresentacaoRequerimento != null)
				|| (this.ideDocumentoRepresentacaoRequerimento != null && !this.ideDocumentoRepresentacaoRequerimento.equals(other.ideDocumentoRepresentacaoRequerimento))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideDocumentoRepresentacaoRequerimento);
	}
}
