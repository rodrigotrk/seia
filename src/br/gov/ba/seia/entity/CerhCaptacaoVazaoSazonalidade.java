package br.gov.ba.seia.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
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
import br.gov.ba.seia.enumerator.MesEnum;
import br.gov.ba.seia.interfaces.CerhVazaoSazonalidadeInterface;

@Entity
@Table(name="cerh_captacao_vazao_sazonalidade")
public class CerhCaptacaoVazaoSazonalidade extends AbstractEntityHist implements CerhVazaoSazonalidadeInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_vazao_sazonalidade_seq")
	@SequenceGenerator(name = "cerh_captacao_vazao_sazonalidade_seq", sequenceName = "cerh_captacao_vazao_sazonalidade_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_vazao_sazonalidade")
	private Integer ideCerhCaptacaoVazaoSazonalidade;

	@Historico(name="Dias/Mês")
	@Column(name="val_dia_mes")
	private Integer valDiaMes;

	@Historico(name="Tempo de captação (horas/dias)")
	@Column(name="val_tempo_captacao")
	private Integer valTempoCaptacao;

	@Historico(name="Vazão (m³/h)")
	@Column(name="val_vazao_captacao")
	private BigDecimal valVazaoCaptacao;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="ide_cerh_captacao_caracterizacao")
	private CerhCaptacaoCaracterizacao ideCerhCaptacaoCaracterizacao;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="ide_mes")
	private Mes ideMes;

	@Transient
	private BigDecimal vazaoDia;
	
	@Transient
	private BigDecimal vazaoMes;
	
	public CerhCaptacaoVazaoSazonalidade() {
	}
	
	public CerhCaptacaoVazaoSazonalidade(MesEnum mesEnum) {
		this.ideMes = new Mes(mesEnum);
	}

	public CerhCaptacaoVazaoSazonalidade(CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao, MesEnum mes) {
		this(mes);
		this.ideCerhCaptacaoCaracterizacao = cerhCaptacaoCaracterizacao;
	}

	public Integer getIdeCerhCaptacaoVazaoSazonalidade() {
		return ideCerhCaptacaoVazaoSazonalidade;
	}

	public void setIdeCerhCaptacaoVazaoSazonalidade(Integer ideCerhCaptacaoVazaoSazonalidade) {
		this.ideCerhCaptacaoVazaoSazonalidade = ideCerhCaptacaoVazaoSazonalidade;
	}

	public Integer getValDiaMes() {
		return valDiaMes;
	}

	public void setValDiaMes(Integer valDiaMes) {
		this.valDiaMes = valDiaMes;
	}

	public Integer getValTempoCaptacao() {
		return valTempoCaptacao;
	}

	public void setValTempoCaptacao(Integer valTempoCaptacao) {
		this.valTempoCaptacao = valTempoCaptacao;
	}

	public BigDecimal getValVazaoCaptacao() {
		return valVazaoCaptacao;
	}

	public void setValVazaoCaptacao(BigDecimal valVazaoCaptacao) {
		this.valVazaoCaptacao = valVazaoCaptacao;
	}

	public CerhCaptacaoCaracterizacao getIdeCerhCaptacaoCaracterizacao() {
		return ideCerhCaptacaoCaracterizacao;
	}

	public void setIdeCerhCaptacaoCaracterizacao(CerhCaptacaoCaracterizacao ideCerhCaptacaoCaracterizacao) {
		this.ideCerhCaptacaoCaracterizacao = ideCerhCaptacaoCaracterizacao;
	}

	public Mes getIdeMes() {
		return ideMes;
	}

	public void setIdeMes(Mes ideMes) {
		this.ideMes = ideMes;
	}

	public BigDecimal getVazaoDia() {
		return vazaoDia;
	}

	public void setVazaoDia(BigDecimal vazaoDia) {
		this.vazaoDia = vazaoDia;
	}

	public BigDecimal getVazaoMes() {
		return vazaoMes;
	}

	public void setVazaoMes(BigDecimal vazaoMes) {
		this.vazaoMes = vazaoMes;
	}

	public boolean isJaneiro() {
		return this.ideMes.getIdeMes().equals(MesEnum.JANEIRO.getValue());
	}

	@Override
	public int compareTo(CerhVazaoSazonalidadeInterface o) {
		return this.getIdeMes().getIdeMes().compareTo(o.getIdeMes().getIdeMes());
	}

	@Override
	public Integer getIde() {
		return ideCerhCaptacaoVazaoSazonalidade;
	}

	@Override
	public BigDecimal getValVazao() {
		return valVazaoCaptacao;
	}

	@Override
	public void setValVazao(BigDecimal vazao) {
		this.valVazaoCaptacao = vazao;
	}

	@Override
	public Integer getValTempo() {
		return valTempoCaptacao;
	}

	@Override
	public void setValTempo(Integer valTempo) {
		this.valTempoCaptacao = valTempo;		
	}
}