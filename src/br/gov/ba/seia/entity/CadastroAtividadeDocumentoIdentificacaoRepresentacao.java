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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Util;


/**
 * The persistent class for the cadastro_atividade_documento_identificacao_representacao database table.
 * 
 */
@Entity
@Table(name="cadastro_atividade_documento_identificacao_representacao")

@NamedQueries({
	@NamedQuery(name="CadastroAtividadeDocumentoIdentificacaoRepresentacao.findAll", query="SELECT c FROM CadastroAtividadeDocumentoIdentificacaoRepresentacao c"),
	@NamedQuery(name = "CadastroAtividadeDocumentoIdentificacaoRepresentacao.removeByIde", query = "DELETE FROM CadastroAtividadeDocumentoIdentificacaoRepresentacao c WHERE c.ideCadastroAtividadeDocumentoIdentificacaoRepresentacao = :ideCadastroAtividadeDocumentoIdentificacaoRepresentacao"),
	@NamedQuery(name = "CadastroAtividadeDocumentoIdentificacaoRepresentacao.removeByIdeCadastroAtividade", query = "DELETE FROM CadastroAtividadeDocumentoIdentificacaoRepresentacao c WHERE c.ideCadastroAtividadeNaoSujeitaLic.ideCadastroAtividadeNaoSujeitaLic = :ideCadastroAtividadeNaoSujeitaLic"),
	@NamedQuery(name = "CadastroAtividadeDocumentoIdentificacaoRepresentacao.removeByIdeCadastroAtividadeDocIdentificacaoExcluido", query = "DELETE FROM CadastroAtividadeDocumentoIdentificacaoRepresentacao c WHERE c.ideCadastroAtividadeNaoSujeitaLic.ideCadastroAtividadeNaoSujeitaLic = :ideCadastroAtividadeNaoSujeitaLic AND c.ideDocumentoIdentificacao = (SELECT di.ideDocumentoIdentificacao FROM DocumentoIdentificacao di WHERE c.ideDocumentoIdentificacao = di.ideDocumentoIdentificacao AND di.indExcluido = :indExcluido)")

})
public class CadastroAtividadeDocumentoIdentificacaoRepresentacao implements Serializable, DocumentoValidacao {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CADASTRO_ATIVIDADE_DOCUMENTO_IDENTIFICACAO_REPRESENTACAO_GENERATOR", sequenceName="CADASTRO_ATIVIDADE_DOCUMENTO_IDENTIFICACAO_REPRESENTACAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CADASTRO_ATIVIDADE_DOCUMENTO_IDENTIFICACAO_REPRESENTACAO_GENERATOR")
	@Column(name="ide_cadastro_atividade_documento_identificacao_representacao")
	private Integer ideCadastroAtividadeDocumentoIdentificacaoRepresentacao;

	@Column(name="dsc_url_documento")
	private String dscUrlDocumento;

	@Column(name="dtc_documento_validado")
	private Date dtcDocumentoValidado;

	@Column(name="dtc_envio_documento")
	private Date dtcEnvioDocumento;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_cadastro_atividade_nao_sujeita_lic", referencedColumnName = "ide_cadastro_atividade_nao_sujeita_lic")
	private CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_documento_identificacao", referencedColumnName = "ide_documento_identificacao")
	private DocumentoIdentificacao ideDocumentoIdentificacao;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_pessoa_fisica_envio", referencedColumnName = "ide_pessoa_fisica")
	private PessoaFisica idePessoaFisicaEnvio;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_pessoa_fisica_validacao", referencedColumnName = "ide_pessoa_fisica")
	private PessoaFisica idePessoaFisicaValidacao;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_procurador", referencedColumnName = "ide_procurador_representante")
	private ProcuradorRepresentante ideProcurador;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_procurador_rep_empreendimento", referencedColumnName = "ide_procurador_rep_empreendimento")
	private ProcuradorRepEmpreendimento ideProcuradorRepEmpreendimento;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_representante_legal", referencedColumnName = "ide_representante_legal")
	private RepresentanteLegal ideRepresentanteLegal;

	@Column(name="ind_documento_validado")
	private Boolean indDocumentoValidado;

	public CadastroAtividadeDocumentoIdentificacaoRepresentacao() {
		
	}
	
	public CadastroAtividadeDocumentoIdentificacaoRepresentacao(CadastroAtividadeNaoSujeitaLic cadastro, DocumentoIdentificacao documento) {
		this.ideCadastroAtividadeNaoSujeitaLic = cadastro;
		this.ideDocumentoIdentificacao = documento;
	}

	public CadastroAtividadeDocumentoIdentificacaoRepresentacao(CadastroAtividadeNaoSujeitaLic cadastro, ProcuradorRepEmpreendimento procurador) {
		this.ideCadastroAtividadeNaoSujeitaLic = cadastro;
		this.ideProcuradorRepEmpreendimento = procurador;
	}
	
	public CadastroAtividadeDocumentoIdentificacaoRepresentacao(CadastroAtividadeNaoSujeitaLic cadastro, RepresentanteLegal representanteLegal) {
		this.ideCadastroAtividadeNaoSujeitaLic = cadastro;
		this.ideRepresentanteLegal = representanteLegal;
	}
	

	public Integer getIdeCadastroAtividadeDocumentoIdentificacaoRepresentacao() {
		return ideCadastroAtividadeDocumentoIdentificacaoRepresentacao;
	}

	public void setIdeCadastroAtividadeDocumentoIdentificacaoRepresentacao(
			Integer ideCadastroAtividadeDocumentoIdentificacaoRepresentacao) {
		this.ideCadastroAtividadeDocumentoIdentificacaoRepresentacao = ideCadastroAtividadeDocumentoIdentificacaoRepresentacao;
	}

	public String getDscUrlDocumento() {
		return dscUrlDocumento;
	}

	public void setDscUrlDocumento(String dscUrlDocumento) {
		this.dscUrlDocumento = dscUrlDocumento;
	}

	public Date getDtcDocumentoValidado() {
		return dtcDocumentoValidado;
	}

	public void setDtcDocumentoValidado(Date dtcDocumentoValidado) {
		this.dtcDocumentoValidado = dtcDocumentoValidado;
	}

	public Date getDtcEnvioDocumento() {
		return dtcEnvioDocumento;
	}

	public void setDtcEnvioDocumento(Date dtcEnvioDocumento) {
		this.dtcEnvioDocumento = dtcEnvioDocumento;
	}

	public CadastroAtividadeNaoSujeitaLic getIdeCadastroAtividadeNaoSujeitaLic() {
		return ideCadastroAtividadeNaoSujeitaLic;
	}

	public void setIdeCadastroAtividadeNaoSujeitaLic(
			CadastroAtividadeNaoSujeitaLic ideCadastroAtividadeNaoSujeitaLic) {
		this.ideCadastroAtividadeNaoSujeitaLic = ideCadastroAtividadeNaoSujeitaLic;
	}

	public DocumentoIdentificacao getIdeDocumentoIdentificacao() {
		return ideDocumentoIdentificacao;
	}

	public void setIdeDocumentoIdentificacao(
			DocumentoIdentificacao ideDocumentoIdentificacao) {
		this.ideDocumentoIdentificacao = ideDocumentoIdentificacao;
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

	public ProcuradorRepresentante getIdeProcurador() {
		return ideProcurador;
	}

	public void setIdeProcurador(ProcuradorRepresentante ideProcurador) {
		this.ideProcurador = ideProcurador;
	}

	public ProcuradorRepEmpreendimento getIdeProcuradorRepEmpreendimento() {
		return ideProcuradorRepEmpreendimento;
	}

	public void setIdeProcuradorRepEmpreendimento(
			ProcuradorRepEmpreendimento ideProcuradorRepEmpreendimento) {
		this.ideProcuradorRepEmpreendimento = ideProcuradorRepEmpreendimento;
	}

	public RepresentanteLegal getIdeRepresentanteLegal() {
		return ideRepresentanteLegal;
	}

	public void setIdeRepresentanteLegal(RepresentanteLegal ideRepresentanteLegal) {
		this.ideRepresentanteLegal = ideRepresentanteLegal;
	}

	public Boolean getIndDocumentoValidado() {
		return indDocumentoValidado;
	}

	public void setIndDocumentoValidado(Boolean indDocumentoValidado) {
		this.indDocumentoValidado = indDocumentoValidado;
	}

	public boolean isDocumentoIdentificacao(){
		return !Util.isNull(this.ideDocumentoIdentificacao);
	}
	
	public boolean isDocumentoRepresentanteLegal(){
		return !Util.isNull(this.ideRepresentanteLegal);
	}
	
	public boolean isDocumentoProcuradorPessoaJuridica(){
		return !Util.isNull(this.ideProcuradorRepEmpreendimento);
	}
	
	public String getFileName() {
		if (isDocumentoRepresentanteLegal() && !Util.isNullOuVazio(getDscCaminhoContratoSocial())) {
			return FileUploadUtil.getFileName(getDscCaminhoContratoSocial());
		}
		else if (isDocumentoProcuradorPessoaJuridica() && !Util.isNull(getDscCaminhoProcuracaoPJ())) {
			return FileUploadUtil.getFileName(getDscCaminhoProcuracaoPJ());
		}
		else {
			return FileUploadUtil.getFileName(getDscCaminhoDocIdentificacao());
		}
	}

	public String getFileSize() {
		File arquivo = null;
		if (isDocumentoRepresentanteLegal() && !Util.isNullOuVazio(getDscCaminhoContratoSocial())) {
			arquivo = new File(getDscCaminhoContratoSocial());
		}
		else if (isDocumentoProcuradorPessoaJuridica() && !Util.isNull(getDscCaminhoProcuracaoPJ())) {
			arquivo = new File(getDscCaminhoProcuracaoPJ());
		}
		else {
			arquivo = new File(getDscCaminhoDocIdentificacao());
		}
		if (!Util.isNullOuVazio(arquivo)) {
			return Long.valueOf(arquivo.length() / 1024).toString() + " Kb";
		}
		return "";
	}

	public String getDscCaminhoDocIdentificacao() {
		return this.ideDocumentoIdentificacao.getDscCaminhoArquivo();
	}
	
	public String getDscCaminhoProcuracaoPJ() {
		return this.ideProcuradorRepEmpreendimento.getDscCaminhoProcuracao();
	}

	public String getDscCaminhoContratoSocial() {
		return this.ideRepresentanteLegal.getDscCaminhoRepresentacao();
	}

	
	@Override
	public String getDescricao() {
		if(isDocumentoIdentificacao() 
				&& !Util.isNullOuVazio(this.ideDocumentoIdentificacao.getIdeTipoIdentificacao())){
			return this.ideDocumentoIdentificacao.getIdeTipoIdentificacao().getNomTipoIdentificacao();
		} 
		else if(isDocumentoProcuradorPessoaJuridica()){
			return "Procuração";
		}
		else if(isDocumentoRepresentanteLegal()){
			return "Contrato Social";
		}
		return null;
	}

}