package br.gov.ba.seia.entity;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.gov.ba.seia.util.Util;

/**
 * Tabela que armazena os documentos que foram apensados
 */
@Entity
@Table(name = "caepog_documento_apensado")
public class CaepogDocumentoApensado implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária composta da tabela CAEPOG_DOCUMENTO_APENSADO
	 */
	@EmbeddedId
	@Basic(optional = false)
	private CaepogDocumentoApensadoPK ideCaepogDocumentoApensadoPK;

	/**
	 * Data do envio documento pelo requerente
	 */
	@Column(name = "dtc_envio_caepog_documento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcEnvioCaepogDocumento;

	/**
	 * Data da validação do documento pelo inema
	 */
	@Column(name = "dtc_validado_caepog_documento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcValidadoCaepogDocumento;

	/**
	 * URL do diretório onde será armazenado o documento enviado
	 */
	@Column(name = "url_caepog_documento", length = 500)
	private String urlCaepogDocumento;

	/**
	 * Pessoa física responsável pelo envio do documento
	 */
	@JoinColumn(name = "ide_pessoa_fisica_envio", referencedColumnName = "ide_pessoa_fisica")
	@ManyToOne(fetch = FetchType.LAZY)
	private PessoaFisica idePessoaFisicaEnvio;

	/**
	 * Pessoa física responsável pela validação do documento
	 */
	@JoinColumn(name = "ide_pessoa_fisica_validacao", referencedColumnName = "ide_pessoa_fisica")
	@ManyToOne(fetch = FetchType.LAZY)
	private PessoaFisica idePessoaFisicaValidacao;

	/**
	 * Caepog do documento
	 */
	@JoinColumn(name = "ide_caepog", referencedColumnName = "ide_caepog", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Caepog caepog;

	/**
	 * Documento qual foi apensado
	 */
	@JoinColumn(name = "ide_caepog_documento", referencedColumnName = "ide_caepog_documento", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogDocumento caepogDocumento;

	public CaepogDocumentoApensado() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogDocumentoApensadoPK == null) ? 0 : ideCaepogDocumentoApensadoPK.hashCode());
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
		CaepogDocumentoApensado other = (CaepogDocumentoApensado) obj;
		if (ideCaepogDocumentoApensadoPK == null) {
			if (other.ideCaepogDocumentoApensadoPK != null)
				return false;
		} else if (!ideCaepogDocumentoApensadoPK.equals(other.ideCaepogDocumentoApensadoPK))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogDocumentoApensado [ideCaepogDocumentoApensadoPK=" + ideCaepogDocumentoApensadoPK + "]";
	}

	/**
	 * 
	 * 
	 * TRANSIENT
	 * 
	 * 
	 */

	@Transient
	public String getNomeDocumento() {
		if (Util.isNullOuVazio(urlCaepogDocumento)) {
			return "";
		}
		return new File(urlCaepogDocumento).getName();
	}

	@Transient
	public String getTamanhoArquivos() {
		if (!Util.isNullOuVazio(urlCaepogDocumento)) {
			return String.valueOf((new File(urlCaepogDocumento).length() / 1024));
		}
		return "";
	}

	@Transient
	public boolean isExisteArquivo() {
		return !Util.isNullOuVazio(urlCaepogDocumento);
	}

	@Transient
	public boolean isArquivoValidado() {
		return !Util.isNullOuVazio(dtcValidadoCaepogDocumento);
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public CaepogDocumentoApensadoPK getIdeCaepogDocumentoApensadoPK() {
		return ideCaepogDocumentoApensadoPK;
	}

	public void setIdeCaepogDocumentoApensadoPK(CaepogDocumentoApensadoPK ideCaepogDocumentoApensadoPK) {
		this.ideCaepogDocumentoApensadoPK = ideCaepogDocumentoApensadoPK;
	}

	public Date getDtcEnvioCaepogDocumento() {
		return dtcEnvioCaepogDocumento;
	}

	public void setDtcEnvioCaepogDocumento(Date dtcEnvioCaepogDocumento) {
		this.dtcEnvioCaepogDocumento = dtcEnvioCaepogDocumento;
	}

	public Date getDtcValidadoCaepogDocumento() {
		return dtcValidadoCaepogDocumento;
	}

	public void setDtcValidadoCaepogDocumento(Date dtcValidadoCaepogDocumento) {
		this.dtcValidadoCaepogDocumento = dtcValidadoCaepogDocumento;
	}

	public String getUrlCaepogDocumento() {
		return urlCaepogDocumento;
	}

	public void setUrlCaepogDocumento(String urlCaepogDocumento) {
		this.urlCaepogDocumento = urlCaepogDocumento;
	}

	public PessoaFisica getIdePessoaFisicaEnvio() {
		return idePessoaFisicaEnvio;
	}

	public void setIdePessoaFisicaEnvio(PessoaFisica idePessoaFisicaEnvio) {
		this.idePessoaFisicaEnvio = idePessoaFisicaEnvio;
	}

	public PessoaFisica getIdePessoaFisicaValidacao() {
		return idePessoaFisicaValidacao;
	}

	public void setIdePessoaFisicaValidacao(PessoaFisica idePessoaFisicaValidacao) {
		this.idePessoaFisicaValidacao = idePessoaFisicaValidacao;
	}

	public Caepog getCaepog() {
		return caepog;
	}

	public void setCaepog(Caepog caepog) {
		this.caepog = caepog;
	}

	public CaepogDocumento getCaepogDocumento() {
		return caepogDocumento;
	}

	public void setCaepogDocumento(CaepogDocumento caepogDocumento) {
		this.caepogDocumento = caepogDocumento;
	}
}