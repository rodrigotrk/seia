package br.gov.ba.seia.entity;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="cadastro_atividade_nao_sujeita_lic_doc_apensado")
@NamedQueries({
		@NamedQuery(name = "CadastroAtividadeNaoSujeitaLicDocApensado.findAll", query = "SELECT c FROM CadastroAtividadeNaoSujeitaLicDocApensado c"),
		@NamedQuery(name = "CadastroAtividadeNaoSujeitaLicDocApensado.removeByIde", query = "DELETE FROM CadastroAtividadeNaoSujeitaLicDocApensado c WHERE c.ideCadastroAtividadeNaoSujeitaLicDocApensado = :ideCadastroAtividadeNaoSujeitaLicDocApensado"),
		@NamedQuery(name = "CadastroAtividadeNaoSujeitaLicDocApensado.removeByIdeCadastroAtividade", query = "DELETE FROM CadastroAtividadeNaoSujeitaLicDocApensado c WHERE c.cadastroAtividadeNaoSujeitaLic.ideCadastroAtividadeNaoSujeitaLic = :ideCadastroAtividadeNaoSujeitaLic")

})
public class CadastroAtividadeNaoSujeitaLicDocApensado implements Serializable, DocumentoValidacao {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_DOC_APENSADO_SEQ", sequenceName = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_DOC_APENSADO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_DOC_APENSADO_SEQ")
	@Column(name="ide_cadastro_atividade_nao_sujeita_lic_doc_apensado")
	private Integer ideCadastroAtividadeNaoSujeitaLicDocApensado;

	@Column(name="dtc_envio_documento")
	private Date dtcEnvioDocumento;

	@Column(name="dtc_validado_documento")
	private Date dtcValidadoDocumento;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_obrigatorio", nullable = true)
	private DocumentoObrigatorio ideDocumentoObrigatorio;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_pessoa_fisica_envio", referencedColumnName = "ide_pessoa_fisica", nullable = false)
	private PessoaFisica idePessoaFisicaEnvio;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_pessoa_fisica_validacao", referencedColumnName = "ide_pessoa_fisica", nullable = true)
	private PessoaFisica idePessoaFisicaValidacao;

	@Column(name="url_documento")
	private String urlDocumento;
	
	@Column(name="ind_validado")
	private Boolean indValidado;

	//bi-directional many-to-one association to CadastroAtividadeNaoSujeitaLic
	@ManyToOne
	@JoinColumn(name = "ide_cadastro_atividade_nao_sujeita_lic")
	private CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic;
	
	@Transient
	private Boolean exibir;

	public CadastroAtividadeNaoSujeitaLicDocApensado() {
	}

	public CadastroAtividadeNaoSujeitaLicDocApensado(CadastroAtividadeNaoSujeitaLic cadastroAtividade, DocumentoObrigatorio ideDocumentoObrigatorio) {
		this.cadastroAtividadeNaoSujeitaLic = cadastroAtividade;
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}

	public Integer getIdeCadastroAtividadeNaoSujeitaLicDocApensado() {
		return this.ideCadastroAtividadeNaoSujeitaLicDocApensado;
	}

	public void setIdeCadastroAtividadeNaoSujeitaLicDocApensado(Integer ideCadastroAtividadeNaoSujeitaLicDocApensado) {
		this.ideCadastroAtividadeNaoSujeitaLicDocApensado = ideCadastroAtividadeNaoSujeitaLicDocApensado;
	}

	public Date getDtcEnvioDocumento() {
		return this.dtcEnvioDocumento;
	}

	public void setDtcEnvioDocumento(Date dtcEnvioDocumento) {
		this.dtcEnvioDocumento = dtcEnvioDocumento;
	}

	public Date getDtcValidadoDocumento() {
		return this.dtcValidadoDocumento;
	}

	public void setDtcValidadoDocumento(Date dtcValidadoDocumento) {
		this.dtcValidadoDocumento = dtcValidadoDocumento;
	}

	public DocumentoObrigatorio getIdeDocumentoObrigatorio() {
		return this.ideDocumentoObrigatorio;
	}

	public void setIdeDocumentoObrigatorio(DocumentoObrigatorio ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}

	public PessoaFisica getIdePessoaFisicaEnvio() {
		return this.idePessoaFisicaEnvio;
	}

	public void setIdePessoaFisicaEnvio(PessoaFisica idePessoaFisicaEnvio) {
		this.idePessoaFisicaEnvio = idePessoaFisicaEnvio;
	}

	public PessoaFisica getIdePessoaFisicaValidacao() {
		return this.idePessoaFisicaValidacao;
	}

	public void setIdePessoaFisicaValidacao(PessoaFisica idePessoaFisicaValidacao) {
		this.idePessoaFisicaValidacao = idePessoaFisicaValidacao;
	}

	public String getUrlDocumento() {
		return this.urlDocumento;
	}

	public void setUrlDocumento(String urlDocumento) {
		this.urlDocumento = urlDocumento;
	}

	public CadastroAtividadeNaoSujeitaLic getCadastroAtividadeNaoSujeitaLic() {
		return this.cadastroAtividadeNaoSujeitaLic;
	}

	public void setCadastroAtividadeNaoSujeitaLic(CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic) {
		this.cadastroAtividadeNaoSujeitaLic = cadastroAtividadeNaoSujeitaLic;
	}

	public Boolean getExibir() {
		return exibir;
	}

	public void setExibir(Boolean exibir) {
		this.exibir = exibir;
	}

	public String getTamanhoDoc() {
		File arquivo = null;
		if (!Util.isNull(this.urlDocumento)) {
			arquivo = new File(this.urlDocumento);
			if (!Util.isNullOuVazio(arquivo)) {
				return Long.valueOf(arquivo.length() / 1024).toString() + " KB";
			}
		}
		return "";
	}

	public String getNomeDocEnviado() {
		if (!Util.isNullOuVazio(this.urlDocumento)) {
			return FileUploadUtil.getFileName(this.urlDocumento);
		}
		return "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideDocumentoObrigatorio == null) ? 0 : ideDocumentoObrigatorio.hashCode());
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
		CadastroAtividadeNaoSujeitaLicDocApensado other = (CadastroAtividadeNaoSujeitaLicDocApensado) obj;
		if (ideDocumentoObrigatorio == null) {
			if (other.ideDocumentoObrigatorio != null)
				return false;
		}
		else if (!ideDocumentoObrigatorio.equals(other.ideDocumentoObrigatorio))
			return false;
		return true;
	}

	public Boolean getIndValidado() {
		return indValidado;
	}

	public void setIndValidado(Boolean indValidado) {
		this.indValidado = indValidado;
	}

	@Override
	public String getDescricao() {
		return this.ideDocumentoObrigatorio.getNomDocumentoObrigatorio();
	}

}