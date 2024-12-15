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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "cadastro_atividade_nao_sujeita_lic_comunicacao")
public class CadastroAtividadeNaoSujeitaLicComunicacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cadastro_atividade_nao_sujeita_licenciamento_comunicacao_seq")
	@SequenceGenerator(name = "cadastro_atividade_nao_sujeita_licenciamento_comunicacao_seq", sequenceName = "cadastro_atividade_nao_sujeita_licenciamento_comunicacao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_cadastro_atividade_nao_sujeita_lic_comunicacao", nullable = false)
	private Integer ideCadastroAtividadeNaoSujeitaLicComunicacao;
	
	@JoinColumn(name = "ide_cadastro_atividade_nao_sujeita_lic", referencedColumnName = "ide_cadastro_atividade_nao_sujeita_lic")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic;
	
	@Column(name = "dtc_comunicacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcComunicacao;
	
	@Column(name = "dsc_mensagem", length = 150)
	private String dscMensagem;
	
	@Column(name = "dsc_assunto")
	private String dscAssunto;
	
	@Column(name = "ind_enviado")
	private boolean indEnviado;

	@Transient
	private boolean visualizar;
	
	public CadastroAtividadeNaoSujeitaLicComunicacao(){
		
	}
	
	public CadastroAtividadeNaoSujeitaLicComunicacao(CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic) {
		this.cadastroAtividadeNaoSujeitaLic = cadastroAtividadeNaoSujeitaLic;
		this.dtcComunicacao = new Date();
	}

	public Integer getIdeCadastroAtividadeNaoSujeitaLicComunicacao() {
		return ideCadastroAtividadeNaoSujeitaLicComunicacao;
	}

	public void setIdeCadastroAtividadeNaoSujeitaLicComunicacao(Integer ideCadastroAtividadeNaoSujeitaLicComunicacao) {
		this.ideCadastroAtividadeNaoSujeitaLicComunicacao = ideCadastroAtividadeNaoSujeitaLicComunicacao;
	}

	public CadastroAtividadeNaoSujeitaLic getCadastroAtividadeNaoSujeitaLic() {
		return cadastroAtividadeNaoSujeitaLic;
	}

	public void setCadastroAtividadeNaoSujeitaLic(
			CadastroAtividadeNaoSujeitaLic cadastroAtividadeNaoSujeitaLic) {
		this.cadastroAtividadeNaoSujeitaLic = cadastroAtividadeNaoSujeitaLic;
	}

	public Date getDtcComunicacao() {
		return dtcComunicacao;
	}

	public void setDtcComunicacao(Date dtcComunicacao) {
		this.dtcComunicacao = dtcComunicacao;
	}

	public String getDscMensagem() {
		return dscMensagem;
	}

	public void setDscMensagem(String desMensagem) {
		this.dscMensagem = desMensagem;
	}

	public String getDscAssunto() {
		return dscAssunto;
	}

	public void setDscAssunto(String assunto) {
		this.dscAssunto = assunto;
	}

	public boolean isIndEnviado() {
		return indEnviado;
	}

	public void setIndEnviado(boolean indEnviado) {
		this.indEnviado = indEnviado;
	}

	public String getDscMensagemHtml() {
		return dscMensagem.replace("\n", "<br/>");
	}

	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean visualizar) {
		this.visualizar = visualizar;
	}	
	
}