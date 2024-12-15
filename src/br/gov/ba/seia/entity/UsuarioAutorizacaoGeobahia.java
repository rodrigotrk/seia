package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "usuario_autorizacao_geobahia")
public class UsuarioAutorizacaoGeobahia extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_pessoa_fisica", nullable = false)
	private Integer idePessoaFisica;
	
	@Column(name = "token")
	private String token;
	
	@Column(name = "dtc_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	@Column(name = "dtc_expiracao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExpiracao;
	
	@Column(name = "ind_excluido")
	private boolean indExcluido;

	public Integer getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Integer idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExpiracao() {
		return dtcExpiracao;
	}

	public void setDtcExpiracao(Date dtcExpiracao) {
		this.dtcExpiracao = dtcExpiracao;
	}
}
