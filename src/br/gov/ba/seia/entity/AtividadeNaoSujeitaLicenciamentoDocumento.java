package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a> 
 */
@Entity
@Table(name="atividade_nao_sujeita_licenciamento_documento")
@NamedQuery(name="AtividadeNaoSujeitaLicenciamentoDocumento.findAll", query="SELECT a FROM AtividadeNaoSujeitaLicenciamentoDocumento a")
public class AtividadeNaoSujeitaLicenciamentoDocumento implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "ATIVIDADE_NAO_SUJEITA_LICENCIAMENTO_DOCUMENTO_GENERATOR", sequenceName = "ATIVIDADE_NAO_SUJEITA_LICENCIAMENTO_DOCUMENTO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATIVIDADE_NAO_SUJEITA_LICENCIAMENTO_DOCUMENTO_GENERATOR")
	@Column(name="ide_atividade_nao_sujeita_licenciamento_documento")
	private Integer ideAtividadeNaoSujeitaLicenciamentoDocumento;

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_documento_obrigatorio", referencedColumnName = "ide_documento_obrigatorio", nullable = false)
	private DocumentoObrigatorio ideDocumentoObrigatorio;

	//bi-directional many-to-one association to TipoAtividadeNaoSujeitaLicenciamento
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name="ide_tipo_atividade_nao_sujeita_licenciamento")
	private TipoAtividadeNaoSujeitaLicenciamento tipoAtividadeNaoSujeitaLicenciamento;

	public AtividadeNaoSujeitaLicenciamentoDocumento() {
	}

	public Integer getIdeAtividadeNaoSujeitaLicenciamentoDocumento() {
		return this.ideAtividadeNaoSujeitaLicenciamentoDocumento;
	}

	public void setIdeAtividadeNaoSujeitaLicenciamentoDocumento(Integer ideAtividadeNaoSujeitaLicenciamentoDocumento) {
		this.ideAtividadeNaoSujeitaLicenciamentoDocumento = ideAtividadeNaoSujeitaLicenciamentoDocumento;
	}

	public DocumentoObrigatorio getIdeDocumentoObrigatorio() {
		return this.ideDocumentoObrigatorio;
	}

	public void setIdeDocumentoObrigatorio(DocumentoObrigatorio ideDocumentoObrigatorio) {
		this.ideDocumentoObrigatorio = ideDocumentoObrigatorio;
	}

	public TipoAtividadeNaoSujeitaLicenciamento getTipoAtividadeNaoSujeitaLicenciamento() {
		return this.tipoAtividadeNaoSujeitaLicenciamento;
	}

	public void setTipoAtividadeNaoSujeitaLicenciamento(TipoAtividadeNaoSujeitaLicenciamento tipoAtividadeNaoSujeitaLicenciamento) {
		this.tipoAtividadeNaoSujeitaLicenciamento = tipoAtividadeNaoSujeitaLicenciamento;
	}

}