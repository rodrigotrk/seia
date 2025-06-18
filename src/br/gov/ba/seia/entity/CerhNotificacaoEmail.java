package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="cerh_notificacao_emails")
public class CerhNotificacaoEmail {

	@Id
	@Basic(optional = false)
	@SequenceGenerator(name = "cerh_notificacao_email_seq", sequenceName = "cerh_notificacao_email_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_notificacao_email_seq")
	@Column(name="ide_cerh_notificacao_email", nullable = false)
	private Integer ideCerhNotificacaoEmail;
	
	@Column(name="dsc_email", nullable = false)
	private String destinatario;
	
	@Column(name="dsc_assunto", nullable = false)
	private String assunto;
	
	@Column(name="dsc_conteudo", nullable = false)
	private String conteudo;
	
	@Column(name="dtc_envio", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEnvio;
	
	@Column(name="ide_usuario_envio", nullable = false)
	private Integer ideUsuarioEnvio;
	
	@Column(name="num_ano_base", nullable = false)
	private Integer anoBase;
	
	@Column(name="ind_enviado", nullable = false)
	private Boolean indeEnviado;

	
	public CerhNotificacaoEmail() {

	}
	
	public CerhNotificacaoEmail(Integer ideCerhNotificacaoEmail) {
		this.ideCerhNotificacaoEmail = ideCerhNotificacaoEmail;
	}
	
	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public Integer getIdeUsuarioEnvio() {
		return ideUsuarioEnvio;
	}

	public void setIdeUsuarioEnvio(Integer ideUsuarioEnvio) {
		this.ideUsuarioEnvio = ideUsuarioEnvio;
	}

	public Integer getAnoBase() {
		return anoBase;
	}

	public void setAnoBase(Integer anoBase) {
		this.anoBase = anoBase;
	}

	public Boolean getIndeEnviado() {
		return indeEnviado;
	}

	public void setIndeEnviado(Boolean indeEnviado) {
		this.indeEnviado = indeEnviado;
	}
	
	
}
