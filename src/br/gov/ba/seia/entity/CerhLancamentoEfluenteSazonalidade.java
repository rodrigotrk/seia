package br.gov.ba.seia.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import br.gov.ba.seia.enumerator.MesEnum;
import br.gov.ba.seia.interfaces.CerhVazaoSazonalidadeInterface;

@Entity
@Table(name="cerh_lancamento_efluente_sazonalidade")
public class CerhLancamentoEfluenteSazonalidade extends AbstractEntityHist implements CerhVazaoSazonalidadeInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "cerh_lancamento_efluente_sazonalidade_seq", sequenceName = "cerh_lancamento_efluente_sazonalidade_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_lancamento_efluente_sazonalidade_seq")
	@Column(name="ide_cerh_lancamento_efluente_sazonalidade")
	private Integer ideCerhLancamentoEfluenteSazonalidade;

	@Historico(name="Dias mês")
	@Column(name="val_dia_mes")
	private Integer valDiaMes;

	@Historico(name="Tempo de lançamento")
	@Column(name="val_tempo_lancamento")
	private Integer valTempoLancamento;

	@Historico(name="Vazão de efluente")
	@Column(name="val_vazao_efluente")
	private BigDecimal valVazaoEfluente;

	@ManyToOne
	@JoinColumn(name="ide_cerh_lancamento_efluente_caracterizacao")
	private CerhLancamentoEfluenteCaracterizacao ideCerhLancamentoEfluenteCaracterizacao;

	@ManyToOne
	@Historico
	@JoinColumn(name="ide_mes")
	private Mes ideMes;

	@Transient
	private BigDecimal valVazaoEfluenteDias;

	@Transient
	private BigDecimal valVazaoEfluenteMes;

	
	public CerhLancamentoEfluenteSazonalidade() {
	}
	
	public CerhLancamentoEfluenteSazonalidade(Mes ideMes) {
		this.ideMes = ideMes;
	}

	public CerhLancamentoEfluenteSazonalidade(CerhLancamentoEfluenteCaracterizacao cerhCaracterizacao, MesEnum mes) {
		this.ideMes = new Mes(mes);
		this.ideCerhLancamentoEfluenteCaracterizacao = cerhCaracterizacao;
	}

	public Integer getIdeCerhLancamentoEfluenteSazonalidade() {
		return ideCerhLancamentoEfluenteSazonalidade;
	}

	public void setIdeCerhLancamentoEfluenteSazonalidade(Integer ideCerhLancamentoEfluenteSazonalidade) {
		this.ideCerhLancamentoEfluenteSazonalidade = ideCerhLancamentoEfluenteSazonalidade;
	}

	public Integer getValDiaMes() {
		return valDiaMes;
	}

	public void setValDiaMes(Integer valDiaMes) {
		this.valDiaMes = valDiaMes;
	}

	public Integer getValTempoLancamento() {
		return valTempoLancamento;
	}

	public void setValTempoLancamento(Integer valTempoLancamento) {
		this.valTempoLancamento = valTempoLancamento;
	}

	public BigDecimal getValVazaoEfluente() {
		return valVazaoEfluente;
	}

	public void setValVazaoEfluente(BigDecimal valVazaoEfluente) {
		this.valVazaoEfluente = valVazaoEfluente;
	}

	public CerhLancamentoEfluenteCaracterizacao getIdeCerhLancamentoEfluenteCaracterizacao() {
		return ideCerhLancamentoEfluenteCaracterizacao;
	}

	public void setIdeCerhLancamentoEfluenteCaracterizacao(
			CerhLancamentoEfluenteCaracterizacao ideCerhLancamentoEfluenteCaracterizacao) {
		this.ideCerhLancamentoEfluenteCaracterizacao = ideCerhLancamentoEfluenteCaracterizacao;
	}

	public Mes getIdeMes() {
		return ideMes;
	}

	public void setIdeMes(Mes ideMes) {
		this.ideMes = ideMes;
	}

	public BigDecimal getValVazaoEfluenteDias() {
		return valVazaoEfluenteDias;
	}

	public void setValVazaoEfluenteDias(BigDecimal valVazaoEfluenteDias) {
		this.valVazaoEfluenteDias = valVazaoEfluenteDias;
	}

	public BigDecimal getValVazaoEfluenteMes() {
		return valVazaoEfluenteMes;
	}

	public void setValVazaoEfluenteMes(BigDecimal valVazaoEfluenteMes) {
		this.valVazaoEfluenteMes = valVazaoEfluenteMes;
	}

	@Override
	public String toString() {
		return String.valueOf(ideCerhLancamentoEfluenteSazonalidade);
	}

	@Override
	public int compareTo(CerhVazaoSazonalidadeInterface o) {
		return this.getIdeMes().getIdeMes().compareTo(o.getIdeMes().getIdeMes());
	}

	@Override
	public Integer getIde() {
		return this.ideCerhLancamentoEfluenteSazonalidade;
	}

	@Override
	public BigDecimal getValVazao() {
		return this.valVazaoEfluente;
	}

	@Override
	public void setValVazao(BigDecimal vazao) {
		this.valVazaoEfluente = vazao;		
	}

	@Override
	public BigDecimal getVazaoDia() {
		return this.valVazaoEfluenteDias;
	}

	@Override
	public void setVazaoDia(BigDecimal vazao) {
		this.valVazaoEfluenteDias = vazao;		
	}

	@Override
	public BigDecimal getVazaoMes() {
		return this.valVazaoEfluenteMes;
	}

	@Override
	public void setVazaoMes(BigDecimal vazao) {
		this.valVazaoEfluenteMes = vazao;		
	}

	@Override
	public Integer getValTempo() {
		return this.valTempoLancamento;
	}

	@Override
	public void setValTempo(Integer valTempo) {
		this.valTempoLancamento = valTempo;		
	}
	
}