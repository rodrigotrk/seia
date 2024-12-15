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

@Entity
@Table(name = "comprovante_pagamento_dae")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComprovantePagamentoDae.findAll", query = "SELECT c FROM ComprovantePagamentoDae c"),
    @NamedQuery(name = "ComprovantePagamentoDae.findByIdeComprovantePagamentoDae", query = "SELECT c FROM ComprovantePagamentoDae c WHERE c.ideComprovantePagamentoDae = :ideComprovantePagamentoDae"),
    @NamedQuery(name = "ComprovantePagamentoDae.findByDscCaminhoArquivo", query = "SELECT c FROM ComprovantePagamentoDae c WHERE c.dscCaminhoArquivo = :dscCaminhoArquivo"),
    @NamedQuery(name = "ComprovantePagamentoDae.findByIndComprovanteValidado", query = "SELECT c FROM ComprovantePagamentoDae c WHERE c.indComprovanteValidado = :indComprovanteValidado"),
    @NamedQuery(name = "ComprovantePagamentoDae.findByDtcValidacao", query = "SELECT c FROM ComprovantePagamentoDae c WHERE c.dtcValidacao = :dtcValidacao"),
    @NamedQuery(name = "ComprovantePagamentoDae.findByIdBoletoDaeRequerimento", query = "SELECT c FROM ComprovantePagamentoDae c WHERE c.ideBoletoDaeRequerimento.ideBoletoDaeRequerimento = :idBoletoDaeRequerimento order by c.ideComprovantePagamentoDae DESC")
})
public class ComprovantePagamentoDae implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="COMPROVANTE_PAGAMENTO_DAE_IDE_COMPROVANTE_PAGAMENTO_DAE_seq")    
    @SequenceGenerator(name="COMPROVANTE_PAGAMENTO_DAE_IDE_COMPROVANTE_PAGAMENTO_DAE_seq", sequenceName="COMPROVANTE_PAGAMENTO_DAE_IDE_COMPROVANTE_PAGAMENTO_DAE_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_comprovante_pagamento_dae", nullable = false)
    private Integer ideComprovantePagamentoDae;
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
    @JoinColumn(name = "ide_pessoa_validacao", referencedColumnName = "ide_pessoa")
    @ManyToOne(fetch = FetchType.LAZY)
    private Pessoa idePessoaValidacao;
    @JoinColumn(name = "ide_pessoa_upload", referencedColumnName = "ide_pessoa", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pessoa idePessoaUpload;
    @JoinColumn(name = "ide_boleto_dae_requerimento", referencedColumnName = "ide_boleto_dae_requerimento", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BoletoDaeRequerimento ideBoletoDaeRequerimento;

    public ComprovantePagamentoDae() {
    }

    public ComprovantePagamentoDae(Integer ideComprovantePagamentoDae) {
        this.ideComprovantePagamentoDae = ideComprovantePagamentoDae;
    }

    public ComprovantePagamentoDae(Integer ideComprovantePagamentoDae, String dscCaminhoArquivo, boolean indComprovanteValidado) {
        this.ideComprovantePagamentoDae = ideComprovantePagamentoDae;
        this.dscCaminhoArquivo = dscCaminhoArquivo;
        this.indComprovanteValidado = indComprovanteValidado;
    }

    @JSON(include = false)
    public Integer getIdeComprovantePagamentoDae() {
        return ideComprovantePagamentoDae;
    }

    public void setIdeComprovantePagamentoDae(Integer ideComprovantePagamentoDae) {
        this.ideComprovantePagamentoDae = ideComprovantePagamentoDae;
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
    public Pessoa getIdePessoaValidacao() {
        return idePessoaValidacao;
    }

    public void setIdePessoaValidacao(Pessoa idePessoaValidacao) {
        this.idePessoaValidacao = idePessoaValidacao;
    }

    @JSON(include = false)
    public Pessoa getIdePessoaUpload() {
        return idePessoaUpload;
    }

    public void setIdePessoaUpload(Pessoa idePessoaUpload) {
        this.idePessoaUpload = idePessoaUpload;
    }

    public BoletoDaeRequerimento getIdeBoletoDaeRequerimento() {
        return ideBoletoDaeRequerimento;
    }

    public void setIdeBoletoDaeRequerimento(BoletoDaeRequerimento ideBoletoDaeRequerimento) {
        this.ideBoletoDaeRequerimento = ideBoletoDaeRequerimento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideComprovantePagamentoDae != null ? ideComprovantePagamentoDae.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ComprovantePagamentoDae)) {
            return false;
        }
        ComprovantePagamentoDae other = (ComprovantePagamentoDae) object;
        if ((this.ideComprovantePagamentoDae == null && other.ideComprovantePagamentoDae != null) || (this.ideComprovantePagamentoDae != null && !this.ideComprovantePagamentoDae.equals(other.ideComprovantePagamentoDae))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ComprovantePagamentoDae[ ideComprovantePagamentoDae=" + ideComprovantePagamentoDae + " ]";
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