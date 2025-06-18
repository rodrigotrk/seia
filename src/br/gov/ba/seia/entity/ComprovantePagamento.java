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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

/**
 *
 * Entidade da table comprovante_pagamento
 * 
 * @author carlos.sousa
 */

@Entity
@Table(name = "comprovante_pagamento")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ComprovantePagamento.findAll", query = "SELECT c FROM ComprovantePagamento c"),
		@NamedQuery(name = "ComprovantePagamento.findByIdeComprovantePagamento", query = "SELECT c FROM ComprovantePagamento c WHERE c.ideComprovantePagamento = :ideComprovantePagamento"),
		@NamedQuery(name = "ComprovantePagamento.findByDscCaminhoArquivo", query = "SELECT c FROM ComprovantePagamento c WHERE c.dscCaminhoArquivo = :dscCaminhoArquivo"),
		@NamedQuery(name = "ComprovantePagamento.findByIndComprovanteValidado", query = "SELECT c FROM ComprovantePagamento c WHERE c.indComprovanteValidado = :indComprovanteValidado"),
		@NamedQuery(name = "ComprovantePagamento.findByRequerimento", query = "SELECT c FROM ComprovantePagamento c WHERE c.ideBoletoPagamentoRequerimento.ideRequerimento.ideRequerimento = :indRequerimento"),
		@NamedQuery(name = "ComprovantePagamento.findByDtcValidacao", query = "SELECT c FROM ComprovantePagamento c WHERE c.dtcValidacao = :dtcValidacao"),
		@NamedQuery(name = "ComprovantePagamento.findByIdBoletoPagamentoRequerimento", query = "SELECT c FROM ComprovantePagamento c WHERE c.ideBoletoPagamentoRequerimento.ideBoletoPagamentoRequerimento = :ideBoletoPagamentoRequerimento order by c.ideComprovantePagamento DESC") })
public class ComprovantePagamento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "COMPROVANTE_PAGAMENTO_IDE_COMPROVANTE_PAGAMENTO_seq", sequenceName = "COMPROVANTE_PAGAMENTO_IDE_COMPROVANTE_PAGAMENTO_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPROVANTE_PAGAMENTO_IDE_COMPROVANTE_PAGAMENTO_seq")
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_comprovante_pagamento", nullable = false)
	private Integer ideComprovantePagamento;
	
	@JoinColumn(name = "ide_boleto_pagamento_requerimento", referencedColumnName = "ide_boleto_pagamento_requerimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private BoletoPagamentoRequerimento ideBoletoPagamentoRequerimento;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 1000)
	@Column(name = "dsc_caminho_arquivo", nullable = false, length = 1000)
	private String dscCaminhoArquivo;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_comprovante_validado", nullable = false)
	private boolean indComprovanteValidado;
	
	@Column(name = "dtc_validacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidacao;
	
	@JoinColumn(name = "ide_pessoa_upload", referencedColumnName = "ide_pessoa", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Pessoa idePessoaUpload;
	
	@JoinColumn(name = "ide_pessoa_validacao", referencedColumnName = "ide_pessoa", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Pessoa idePessoaValidacao;

    public ComprovantePagamento() {
    }

    public ComprovantePagamento(Integer ideComprovantePagamento) {
        this.ideComprovantePagamento = ideComprovantePagamento;
    }

	public ComprovantePagamento(Integer ideComprovantePagamento, String dscCaminhoArquivo, boolean indComprovanteValidado) {
		this.ideComprovantePagamento = ideComprovantePagamento;
		this.dscCaminhoArquivo = dscCaminhoArquivo;
		this.indComprovanteValidado = indComprovanteValidado;
	}

	@JSON(include = false)
    public Integer getIdeComprovantePagamento() {
        return ideComprovantePagamento;
    }

    public void setIdeComprovantePagamento(Integer ideComprovantePagamento) {
        this.ideComprovantePagamento = ideComprovantePagamento;
    }

    @JSON(include = false)
    public String getDscCaminhoArquivo() {
        return dscCaminhoArquivo;
    }

    public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
        this.dscCaminhoArquivo = dscCaminhoArquivo;
    }

    @JSON(include = false)
    public boolean getIndComprovanteValidado() {
        return indComprovanteValidado;
    }

    public void setIndComprovanteValidado(boolean indComprovanteValidado) {
        this.indComprovanteValidado = indComprovanteValidado;
    }

    public Date getDtcValidacao() {
        return dtcValidacao;
    }

    public void setDtcValidacao(Date dtcValidacao) {
        this.dtcValidacao = dtcValidacao;
    }

    @JSON(include = false)
    public Pessoa getIdePessoaUpload() {
        return idePessoaUpload;
    }

    public void setIdePessoaUpload(Pessoa idePessoa) {
        this.idePessoaUpload = idePessoa;
    }

    @JSON(include = false)
    public Pessoa getIdePessoaValidacao() {
		return idePessoaValidacao;
	}

	public void setIdePessoaValidacao(Pessoa idePessoaValidacao) {
		this.idePessoaValidacao = idePessoaValidacao;
	}

	public BoletoPagamentoRequerimento getIdeBoletoPagamentoRequerimento() {
        return ideBoletoPagamentoRequerimento;
    }

    public void setIdeBoletoPagamentoRequerimento(BoletoPagamentoRequerimento ideBoletoPagamentoRequerimento) {
        this.ideBoletoPagamentoRequerimento = ideBoletoPagamentoRequerimento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideComprovantePagamento != null ? ideComprovantePagamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ComprovantePagamento)) {
            return false;
        }
        ComprovantePagamento other = (ComprovantePagamento) object;
        if ((this.ideComprovantePagamento == null && other.ideComprovantePagamento != null) || (this.ideComprovantePagamento != null && !this.ideComprovantePagamento.equals(other.ideComprovantePagamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ComprovantePagamento[ ideComprovantePagamento=" + ideComprovantePagamento + " ]";
    }
    
    @JSON(include = false)
    public String getFileName() {
		if (!Util.isNullOuVazio(this.dscCaminhoArquivo)) {
			return FileUploadUtil.getFileName(this.dscCaminhoArquivo);
		}
		
		return "";
	}

    @JSON(include = false)
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
}