package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;

@Entity
@Table(name="cerh_lancamento_outros_usos")
public class CerhLancamentoOutrosUsos extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ide_cerh_lancamento_outros_usos")
	@SequenceGenerator(name = "cerh_lancamento_outros_usos_seq", sequenceName = "cerh_lancamento_outros_usos_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_lancamento_outros_usos_seq")
	private Integer ideCerhLancamentoOutrosUsos;
	
	@Historico
	@OneToOne
	@JoinColumn(name = "ide_cerh_outros_usos", referencedColumnName = "ide_cerh_outros_usos")
	private CerhOutrosUsos ideCerhOutrosUsos;

	@ManyToOne
	@JoinColumn(name = "ide_cerh_lancamento_caracterizacao_origem", referencedColumnName = "ide_cerh_lancamento_caracterizacao_origem")
	private CerhLancamentoCaracterizacaoOrigem ideCerhLancamentoCaracterizacaoOrigem;
	
	public CerhLancamentoOutrosUsos() {
	}

	public CerhLancamentoOutrosUsos(CerhOutrosUsos cerhOutrosUsos, CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		this.ideCerhOutrosUsos = cerhOutrosUsos;
		this.ideCerhLancamentoCaracterizacaoOrigem = cerhLancamentoCaracterizacaoOrigem;
	}

	public CerhLancamentoOutrosUsos(CerhLancamentoCaracterizacaoOrigem ideCerhLancamentoCaracterizacaoOrigem) {
		this.ideCerhLancamentoCaracterizacaoOrigem = ideCerhLancamentoCaracterizacaoOrigem;
	}

	public Integer getIdeCerhLancamentoOutrosUsos() {
		return ideCerhLancamentoOutrosUsos;
	}

	public void setIdeCerhLancamentoOutrosUsos(Integer ideCerhLancamentoOutrosUsos) {
		this.ideCerhLancamentoOutrosUsos = ideCerhLancamentoOutrosUsos;
	}
	
	public CerhOutrosUsos getIdeCerhOutrosUsos() {
		return ideCerhOutrosUsos;
	}

	public void setIdeCerhOutrosUsos(CerhOutrosUsos ideOutrosUsoCerh) {
		this.ideCerhOutrosUsos = ideOutrosUsoCerh;
	}

	public CerhLancamentoCaracterizacaoOrigem getIdeCerhLancamentoCaracterizacaoOrigem() {
		return ideCerhLancamentoCaracterizacaoOrigem;
	}

	public void setIdeCerhLancamentoCaracterizacaoOrigem(CerhLancamentoCaracterizacaoOrigem ideCerhLancamentoCaracterizacaoOrigem) {
		this.ideCerhLancamentoCaracterizacaoOrigem = ideCerhLancamentoCaracterizacaoOrigem;
	}
}