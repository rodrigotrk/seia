package br.gov.ba.seia.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;

@Entity
@Table(name="cerh_captacao_dados_irrigacao")
public class CerhCaptacaoDadosIrrigacao extends AbstractEntityHist implements CerhDadosFinalidadeInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_dados_irrigacao_seq")
	@SequenceGenerator(name = "cerh_captacao_dados_irrigacao_seq", sequenceName = "cerh_captacao_dados_irrigacao_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_dados_irrigacao")
	private Integer ideCerhCaptacaoDadosIrrigacao;

	@Historico
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ide_metodo_irrigacao")
	private MetodoIrrigacao ideMetodoIrrigacao;

	@Historico(name="Área Irrigada (ha)")
	@Column(name="val_area_irrigada")
	private BigDecimal valAreaIrrigada;

	@Historico
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ide_tipologia_atividade")
	private TipologiaAtividade ideTipologiaAtividade;

	@ManyToOne
	@JoinColumn(name="ide_cerh_captacao_caracterizacao_finalidade")
	private CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade;

	@Transient
	private boolean confirmado;

	public CerhCaptacaoDadosIrrigacao() {
	}

	public CerhCaptacaoDadosIrrigacao(TipologiaAtividade tipologiaAtividade, CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade) {
		this.ideTipologiaAtividade = tipologiaAtividade;
		this.ideCerhCaptacaoCaracterizacaoFinalidade = cerhCaptacaoCaracterizacaoFinalidade;
	}

	public Integer getIdeCerhCaptacaoDadosIrrigacao() {
		return ideCerhCaptacaoDadosIrrigacao;
	}

	public void setIdeCerhCaptacaoDadosIrrigacao(Integer ideCerhCaptacaoDadosIrrigacao) {
		this.ideCerhCaptacaoDadosIrrigacao = ideCerhCaptacaoDadosIrrigacao;
	}

	public MetodoIrrigacao getIdeMetodoIrrigacao() {
		return ideMetodoIrrigacao;
	}

	public void setIdeMetodoIrrigacao(MetodoIrrigacao ideMetodoIrrigacao) {
		this.ideMetodoIrrigacao = ideMetodoIrrigacao;
	}

	public BigDecimal getValAreaIrrigada() {
		return valAreaIrrigada;
	}

	public void setValAreaIrrigada(BigDecimal valAreaIrrigada) {
		this.valAreaIrrigada = valAreaIrrigada;
	}

	public CerhCaptacaoCaracterizacaoFinalidade getIdeCerhCaptacaoCaracterizacaoFinalidade() {
		return ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public void setIdeCerhCaptacaoCaracterizacaoFinalidade(
			CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public TipologiaAtividade getIdeTipologiaAtividade() {
		return ideTipologiaAtividade;
	}

	public void setIdeTipologiaAtividade(TipologiaAtividade ideTipologiaAtividade) {
		this.ideTipologiaAtividade = ideTipologiaAtividade;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirma) {
		this.confirmado = confirma;
	}

	@Override
	public Integer getIde() {
		return this.ideCerhCaptacaoDadosIrrigacao;
	}
}