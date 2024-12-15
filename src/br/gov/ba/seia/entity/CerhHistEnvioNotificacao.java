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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="cerh_hist_envio_notificacao")
public class CerhHistEnvioNotificacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_hist_notificacao_seq")
	@SequenceGenerator(name = "cerh_hist_notificacao_seq", sequenceName = "cerh_hist_notificacao_seq", allocationSize = 1)
	@Column(name="ide_cerh_hist_notificacao")
	private Integer ideEnvioNotificacao;

	@Column(name="ide_geo_rpga")
	private Integer ideGeoRpga;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dt_envio")
	private Date dataEnvio;

	@Column(name="ide_usuario_envio")
	private Integer ideUsuarioEnvio;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_usuario_agua")
	private Pessoa ideUsuarioAgua;

	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ide_empreendimento")
	private Empreendimento ideEmpreendimento;

	@Column(name="vlr_quantidade")
	private Integer quantidadeEnvio;

	public CerhHistEnvioNotificacao() {

	}

	public CerhHistEnvioNotificacao(Integer ideEnvioNotificacao) {
		this.ideEnvioNotificacao = ideEnvioNotificacao;
	}

	public Integer getIdeGeoRpga() {
		return ideGeoRpga;
	}

	public void setIdeGeoRpga(Integer ideGeoRpga) {
		this.ideGeoRpga = ideGeoRpga;
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

	public Integer getQuantidadeEnvio() {
		return quantidadeEnvio;
	}

	public void setQuantidadeEnvio(Integer quantidadeEnvio) {
		this.quantidadeEnvio = quantidadeEnvio;
	}

	public Pessoa getIdeUsuarioAgua() {
		return ideUsuarioAgua;
	}

	public void setIdeUsuarioAgua(Pessoa ideUsuarioAgua) {
		this.ideUsuarioAgua = ideUsuarioAgua;
	}

	public Empreendimento getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

}
