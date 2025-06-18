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

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "caepog_comunicacao")
public class CaepogComunicacao implements Serializable, BaseEntity, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caepog_comunicacao_ide_caepog_comunicacao_seq")
	@SequenceGenerator(name = "caepog_comunicacao_ide_caepog_comunicacao_seq", sequenceName = "caepog_comunicacao_ide_caepog_comunicacao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_caepog_comunicacao", nullable = false)
	private Integer ideCaepogComunicacao;
	
	@JoinColumn(name = "ide_caepog", referencedColumnName = "ide_caepog")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Caepog ideCaepog;
	
	@Column(name = "dtc_comunicacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcComunicacao;
	
	@Column(name = "des_mensagem", length = 150)
	private String desMensagem;
	
	@Column(name = "assunto")
	private String assunto;
	
	@Column(name = "ind_enviado")
	private boolean indEnviado;

	public CaepogComunicacao() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogComunicacao == null) ? 0 : ideCaepogComunicacao.hashCode());
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
		CaepogComunicacao other = (CaepogComunicacao) obj;
		if (ideCaepogComunicacao == null) {
			if (other.ideCaepogComunicacao != null)
				return false;
		} else if (!ideCaepogComunicacao.equals(other.ideCaepogComunicacao))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "CaepogComunicacao [ideCaepogComunicacao=" + ideCaepogComunicacao + "]";
	}

	@Override
	public Long getId() {
		return ideCaepogComunicacao.longValue();
	}

	@Override
	public CaepogComunicacao clone() throws CloneNotSupportedException {
		return (CaepogComunicacao) super.clone();
	}
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */
	
	public Integer getIdeCaepogComunicacao() {
		return ideCaepogComunicacao;
	}

	public void setIdeCaepogComunicacao(Integer ideCaepogComunicacao) {
		this.ideCaepogComunicacao = ideCaepogComunicacao;
	}

	public Caepog getCaepog() {
		return ideCaepog;
	}

	public void setCaepog(Caepog caepog) {
		this.ideCaepog = caepog;
	}

	public Date getDtcComunicacao() {
		return dtcComunicacao;
	}

	public void setDtcComunicacao(Date dtcComunicacao) {
		this.dtcComunicacao = dtcComunicacao;
	}

	public String getDesMesagem() {
		return desMensagem;
	}
	
	public String getDesMensagemHtml() {
		return desMensagem.replace("\n", "<br/>");
	}

	public void setDesMesagem(String desMesagem) {
		this.desMensagem = desMesagem;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public boolean isIndEnviado() {
		return indEnviado;
	}

	public void setIndEnviado(boolean indEnviado) {
		this.indEnviado = indEnviado;
	}
}