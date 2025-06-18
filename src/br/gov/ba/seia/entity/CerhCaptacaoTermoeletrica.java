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

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;


@Entity
@Table(name="cerh_captacao_termoeletrica")
public class CerhCaptacaoTermoeletrica extends AbstractEntityHist implements CerhDadosFinalidadeInterface  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_termoeletrica_seq")
	@SequenceGenerator(name = "cerh_captacao_termoeletrica_seq", sequenceName = "cerh_captacao_termoeletrica_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_termoeletrica")
	private Integer ideCerhCaptacaoTermoeletrica;

	@Historico(name="Combustível principal") 
	@Column(name="nom_combustivel_principal")
	private String nomCombustivelPrincipal;

	@Historico(name="Estimativa de consumo de água utilizada (m³/mês)")
	@Column(name="val_estimativa_consumo_agua")
	private BigDecimal valEstimativaConsumoAgua;

	@Historico(name="Potência Instalada (MW)")
	@Column(name="val_potencia_instalada")
	private BigDecimal valPotenciaInstalada;

	@Historico(name="Produção Mensal de Energia (MWh)")
	@Column(name="val_producao_mensal_energia")
	private BigDecimal valProducaoMensalEnergia;

	@ManyToOne
	@JoinColumn(name="ide_cerh_captacao_caracterizacao_finalidade")
	private CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade;

	public CerhCaptacaoTermoeletrica() {
	}

	public CerhCaptacaoTermoeletrica(CerhCaptacaoCaracterizacaoFinalidade cerhFinalidadeTermoeletrica) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = cerhFinalidadeTermoeletrica;
	}

	public Integer getIdeCerhCaptacaoTermoeletrica() {
		return ideCerhCaptacaoTermoeletrica;
	}

	public void setIdeCerhCaptacaoTermoeletrica(Integer ideCerhCaptacaoTermoeletrica) {
		this.ideCerhCaptacaoTermoeletrica = ideCerhCaptacaoTermoeletrica;
	}

	public String getNomCombustivelPrincipal() {
		return nomCombustivelPrincipal;
	}

	public void setNomCombustivelPrincipal(String nomCombustivelPrincipal) {
		this.nomCombustivelPrincipal = nomCombustivelPrincipal;
	}

	public BigDecimal getValEstimativaConsumoAgua() {
		return valEstimativaConsumoAgua;
	}

	public void setValEstimativaConsumoAgua(BigDecimal valEstimativaConsumoAgua) {
		this.valEstimativaConsumoAgua = valEstimativaConsumoAgua;
	}

	public BigDecimal getValPotenciaInstalada() {
		return valPotenciaInstalada;
	}

	public void setValPotenciaInstalada(BigDecimal valPotenciaInstalada) {
		this.valPotenciaInstalada = valPotenciaInstalada;
	}

	public BigDecimal getValProducaoMensalEnergia() {
		return valProducaoMensalEnergia;
	}

	public void setValProducaoMensalEnergia(BigDecimal valProducaoMensalEnergia) {
		this.valProducaoMensalEnergia = valProducaoMensalEnergia;
	}

	public CerhCaptacaoCaracterizacaoFinalidade getIdeCerhCaptacaoCaracterizacaoFinalidade() {
		return ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public void setIdeCerhCaptacaoCaracterizacaoFinalidade(CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = ideCerhCaptacaoCaracterizacaoFinalidade;
	}
	
	@Override
	public Integer getIde() {
		return this.ideCerhCaptacaoTermoeletrica;
	}
}