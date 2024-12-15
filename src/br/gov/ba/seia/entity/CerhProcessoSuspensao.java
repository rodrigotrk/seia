package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;

@Entity
@Table(name="cerh_processo_suspensao")
public class CerhProcessoSuspensao extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_processo_suspensao_seq")
	@SequenceGenerator(name = "cerh_processo_suspensao_seq", sequenceName = "cerh_processo_suspensao_seq", allocationSize = 1)
	@Column(name="ide_cerh_processo_suspensao")
	private Integer ideCerhProcessoSuspensao;

	@Historico(name="Data final suspensão")
	@Temporal(TemporalType.DATE)
	@Column(name="dt_fim_suspensao")
	private Date dtFimSuspensao;

	@Historico(name="Data inicial suspensão")
	@Temporal(TemporalType.DATE)
	@Column(name="dt_inicio_suspensao")
	private Date dtInicioSuspensao;

	@ManyToOne
	@JoinColumn(name="ide_cerh_processo")
	private CerhProcesso ideCerhProcesso;

	public CerhProcessoSuspensao() {
	}
	
	public CerhProcessoSuspensao(Integer ideCerhProcessoSuspensao) {
		this.ideCerhProcessoSuspensao = ideCerhProcessoSuspensao;
	}

	public Integer getIdeCerhProcessoSuspensao() {
		return this.ideCerhProcessoSuspensao;
	}

	public void setIdeCerhProcessoSuspensao(Integer ideCerhProcessoSuspensao) {
		this.ideCerhProcessoSuspensao = ideCerhProcessoSuspensao;
	}

	public Date getDtFimSuspensao() {
		return this.dtFimSuspensao;
	}

	public void setDtFimSuspensao(Date dtFimSuspensao) {
		this.dtFimSuspensao = dtFimSuspensao;
	}

	public Date getDtInicioSuspensao() {
		return this.dtInicioSuspensao;
	}

	public void setDtInicioSuspensao(Date dtInicioSuspensao) {
		this.dtInicioSuspensao = dtInicioSuspensao;
	}

	public CerhProcesso getIdeCerhProcesso() {
		return this.ideCerhProcesso;
	}

	public void setIdeCerhProcesso(CerhProcesso ideCerhProcesso) {
		this.ideCerhProcesso = ideCerhProcesso;
	}
}