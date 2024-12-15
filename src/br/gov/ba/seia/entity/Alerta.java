package br.gov.ba.seia.entity;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import flexjson.JSON;


/**
 * The persistent class for the alerta database table.
 * 
 */
@Entity
@Table(name="alerta")
public class Alerta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ALERTA_SEQ_GENERATOR", sequenceName="ALERTA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ALERTA_SEQ_GENERATOR")
	@Column(name="ide_alerta", nullable = false)
	private Integer ideAlerta;

	@Column(name="des_mensagem", nullable = false)
	private String desMensagem;

	@Column(name="dtc_envio",nullable = false)
	private Date dtcEnvio;

	@Column(name="dtc_leitura")
	private Date dtcLeitura;

	@NotNull
	@JoinColumn(name="ide_usuario",referencedColumnName = "ide_pessoa_fisica", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Usuario ideUsuario;

	@Column(name="ind_lida",nullable = false)
	private Boolean indLida;

	@Column(name="num_documento", length = 50)
	private String numDocumento;

	//bi-directional many-to-one association to TipoAlerta
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_tipo_alerta",nullable = false)
	private TipoAlerta tipoAlerta;

    public Alerta() {
    }

	public Integer getIdeAlerta() {
		return this.ideAlerta;
	}

	public void setIdeAlerta(Integer ideAlerta) {
		this.ideAlerta = ideAlerta;
	}

	public String getDesMensagem() {
		return this.desMensagem;
	}

	public void setDesMensagem(String desMensagem) {
		this.desMensagem = desMensagem;
	}

	public Date getDtcEnvio() {
		return this.dtcEnvio;
	}

	public void setDtcEnvio(Date dtcEnvio) {
		this.dtcEnvio = dtcEnvio;
	}

	@JSON(include = false)
	public Date getDtcLeitura() {
		return this.dtcLeitura;
	}

	public void setDtcLeitura(Date dtcLeitura) {
		this.dtcLeitura = dtcLeitura;
	}

	@JSON(include = false)
	public Usuario getIdeUsuario() {
		return this.ideUsuario;
	}

	public void setIdeUsuario(Usuario ideUsuario) {
		this.ideUsuario = ideUsuario;
	}

	public Boolean getIndLida() {
		return this.indLida;
	}

	public void setIndLida(Boolean indLida) {
		this.indLida = indLida;
	}

	public String getNumDocumento() {
		return this.numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public TipoAlerta getTipoAlerta() {
		return this.tipoAlerta;
	}

	public void setTipoAlerta(TipoAlerta tipoAlerta) {
		this.tipoAlerta = tipoAlerta;
	}
	
	@JSON(include = false)
	public String getDesMensagemTrucada() {
		return this.desMensagem.substring(0, 50);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideAlerta == null) ? 0 : ideAlerta.hashCode());
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
		Alerta other = (Alerta) obj;
		if (ideAlerta == null) {
			if (other.ideAlerta != null)
				return false;
		} else if (!ideAlerta.equals(other.ideAlerta))
			return false;
		return true;
	}
	
}