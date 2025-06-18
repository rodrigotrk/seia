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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

/**
 * @author MJunior
 */
@Entity
@Table(name = "vw_documento_identificacao_obrigatorio")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "VwDocumentoIdentificacaoObrigatorio.findAll", query = "SELECT v FROM VwDocumentoIdentificacaoObrigatorio v"),
		@NamedQuery(name = "VwDocumentoIdentificacaoObrigatorio.findByIdeDocumentoRequerimentoDto", query = "SELECT v FROM VwDocumentoIdentificacaoObrigatorio v WHERE v.ideDocumentoRequerimentoDto = :ideDocumentoRequerimentoDto"),
		@NamedQuery(name = "VwDocumentoIdentificacaoObrigatorio.findByIdeRequerimentoDto", query = "SELECT v FROM VwDocumentoIdentificacaoObrigatorio v WHERE v.ideRequerimentoDto = :ideRequerimentoDto"),
		@NamedQuery(name = "VwDocumentoIdentificacaoObrigatorio.findByDtcValidacaoDto", query = "SELECT v FROM VwDocumentoIdentificacaoObrigatorio v WHERE v.dtcValidacaoDto = :dtcValidacaoDto"),
		@NamedQuery(name = "VwDocumentoIdentificacaoObrigatorio.findByIndDocumentoValidadoDto", query = "SELECT v FROM VwDocumentoIdentificacaoObrigatorio v WHERE v.indDocumentoValidadoDto = :indDocumentoValidadoDto"),
		@NamedQuery(name = "VwDocumentoIdentificacaoObrigatorio.findByIdePessoaValidacaoDto", query = "SELECT v FROM VwDocumentoIdentificacaoObrigatorio v WHERE v.idePessoaValidacaoDto = :idePessoaValidacaoDto"),
		@NamedQuery(name = "VwDocumentoIdentificacaoObrigatorio.findByIdeDocumentoDto", query = "SELECT v FROM VwDocumentoIdentificacaoObrigatorio v WHERE v.ideDocumentoDto = :ideDocumentoDto"),
		@NamedQuery(name = "VwDocumentoIdentificacaoObrigatorio.findByDscCaminhoArquivoDto", query = "SELECT v FROM VwDocumentoIdentificacaoObrigatorio v WHERE v.dscCaminhoArquivoDto = :dscCaminhoArquivoDto"),
		@NamedQuery(name = "VwDocumentoIdentificacaoObrigatorio.findByNomDocumentoDto", query = "SELECT v FROM VwDocumentoIdentificacaoObrigatorio v WHERE v.nomDocumentoDto = :nomDocumentoDto"),
		@NamedQuery(name = "VwDocumentoIdentificacaoObrigatorio.findByTipoDocumentoDto", query = "SELECT v FROM VwDocumentoIdentificacaoObrigatorio v WHERE v.tipoDocumentoDto = :tipoDocumentoDto") })
public class VwDocumentoIdentificacaoObrigatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "id_dto")
	private Integer idDTO;
	@Column(name = "ide_documento_requerimento_dto")
	private Integer ideDocumentoRequerimentoDto;
	@Column(name = "ide_requerimento_dto")
	private Integer ideRequerimentoDto;
	@Column(name = "dtc_validacao_dto")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidacaoDto;
	@Column(name = "ind_documento_validado_dto")
	private Boolean indDocumentoValidadoDto;
	@Column(name = "ide_pessoa_validacao_dto")
	private Integer idePessoaValidacaoDto;
	@Column(name = "ide_documento_dto")
	private Integer ideDocumentoDto;
	@Column(name = "dsc_caminho_arquivo_dto", length = 2147483647)
	private String dscCaminhoArquivoDto;
	@Column(name = "nom_documento_dto", length = 2147483647)
	private String nomDocumentoDto;
	@Column(name = "tipo_documento_dto", length = 2147483647)
	private String tipoDocumentoDto;
	@Column(name = "ide_ato_ambiental")
	private Integer ideAtoAmbiental;
	@Column(name = "nom_ato_ambiental")
	private String nomAtoAmbiental;
	@Transient
	private Long fileSize;
	@Transient
	private StreamedContent file;
	@Transient
	private Boolean isArquivoChanged;
	@Transient
	private FileUploadEvent fileUpload;
	@Transient
	private String caminhoArquivoAnterior;

	public VwDocumentoIdentificacaoObrigatorio() {
	}

	public String getFileSizeFormated() {
		if (this.getFileSize() != null) {
			return Long.valueOf(this.getFileSize() / 1024).toString() + " Kb";
		} else {
			return "0 Kb";
		}
	}

	public Integer getIdeDocumentoRequerimentoDto() {
		return ideDocumentoRequerimentoDto;
	}

	public void setIdeDocumentoRequerimentoDto(Integer ideDocumentoRequerimentoDto) {
		this.ideDocumentoRequerimentoDto = ideDocumentoRequerimentoDto;
	}

	public Integer getIdeRequerimentoDto() {
		return ideRequerimentoDto;
	}

	public void setIdeRequerimentoDto(Integer ideRequerimentoDto) {
		this.ideRequerimentoDto = ideRequerimentoDto;
	}

	public Date getDtcValidacaoDto() {
		return dtcValidacaoDto;
	}

	public void setDtcValidacaoDto(Date dtcValidacaoDto) {
		this.dtcValidacaoDto = dtcValidacaoDto;
	}

	public Boolean getIndDocumentoValidadoDto() {
		return indDocumentoValidadoDto;
	}

	public void setIndDocumentoValidadoDto(Boolean indDocumentoValidadoDto) {
		this.indDocumentoValidadoDto = indDocumentoValidadoDto;
	}

	public Integer getIdePessoaValidacaoDto() {
		return idePessoaValidacaoDto;
	}

	public void setIdePessoaValidacaoDto(Integer idePessoaValidacaoDto) {
		this.idePessoaValidacaoDto = idePessoaValidacaoDto;
	}

	public Integer getIdeDocumentoDto() {
		return ideDocumentoDto;
	}

	public void setIdeDocumentoDto(Integer ideDocumentoDto) {
		this.ideDocumentoDto = ideDocumentoDto;
	}

	public String getDscCaminhoArquivoDto() {
		return dscCaminhoArquivoDto;
	}

	public void setDscCaminhoArquivoDto(String dscCaminhoArquivoDto) {
		this.dscCaminhoArquivoDto = dscCaminhoArquivoDto;
	}

	public String getNomDocumentoDto() {
		return nomDocumentoDto;
	}

	public void setNomDocumentoDto(String nomDocumentoDto) {
		this.nomDocumentoDto = nomDocumentoDto;
	}

	public Integer getIdDTO() {
		return idDTO;
	}

	public void setIdDTO(Integer idDTO) {
		this.idDTO = idDTO;
	}

	public String getTipoDocumentoDto() {
		return tipoDocumentoDto;
	}

	public void setTipoDocumentoDto(String tipoDocumentoDto) {
		this.tipoDocumentoDto = tipoDocumentoDto;
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

	public Boolean getIsArquivoChanged() {
		return isArquivoChanged;
	}

	public void setIsArquivoChanged(Boolean isArquivoChanged) {
		this.isArquivoChanged = isArquivoChanged;
	}

	public FileUploadEvent getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(FileUploadEvent fileUpload) {
		this.fileUpload = fileUpload;
	}

	public String getCaminhoArquivoAnterior() {
		return caminhoArquivoAnterior;
	}

	public void setCaminhoArquivoAnterior(String caminhoArquivoAnterior) {
		this.caminhoArquivoAnterior = caminhoArquivoAnterior;
	}

	public Integer getIdeAtoAmbiental() {
		return ideAtoAmbiental;
	}

	public void setIdeAtoAmbiental(Integer ideAtoAmbiental) {
		this.ideAtoAmbiental = ideAtoAmbiental;
	}

	public String getNomAtoAmbiental() {
		return nomAtoAmbiental;
	}

	public void setNomAtoAmbiental(String nomAtoAmbiental) {
		this.nomAtoAmbiental = nomAtoAmbiental;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideDocumentoDto == null) ? 0 : ideDocumentoDto.hashCode());
		result = prime * result + ((ideRequerimentoDto == null) ? 0 : ideRequerimentoDto.hashCode());
		result = prime * result + ((nomDocumentoDto == null) ? 0 : nomDocumentoDto.hashCode());
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
		VwDocumentoIdentificacaoObrigatorio other = (VwDocumentoIdentificacaoObrigatorio) obj;
		if (ideDocumentoDto == null) {
			if (other.ideDocumentoDto != null)
				return false;
		} else if (!ideDocumentoDto.equals(other.ideDocumentoDto))
			return false;
		if (ideRequerimentoDto == null) {
			if (other.ideRequerimentoDto != null)
				return false;
		} else if (!ideRequerimentoDto.equals(other.ideRequerimentoDto))
			return false;
		if (nomDocumentoDto == null) {
			if (other.nomDocumentoDto != null)
				return false;
		} else if (!nomDocumentoDto.equals(other.nomDocumentoDto))
			return false;
		return true;
	}

}
