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

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * @author Alexandre Queiroz
 * @since 15/12/2016 
 */
@Entity
@Table(name="registro_floresta_producao_imovel_especie_producao")
public class RegistroFlorestaProducaoImovelEspecieProducao extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator( name="registro_floresta_producao_imovel_especie_producao_seq", sequenceName="registro_floresta_producao_imovel_especie_producao_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="registro_floresta_producao_imovel_especie_producao_seq")
	@Column(name="ide_registro_floresta_producao_imovel_especie_producao", unique=true, nullable=false)
	private Integer ideRegistroFlorestaProducaoImovelEspecieProducao;

	@Column(name="val_ciclo_corte", nullable=false)
	private Integer valCicloCorte;

	@Column(name="val_espacamento", nullable=false, precision=13, scale=4)
	private BigDecimal valEspacamento;

	@Column(name="val_estimativa_volume_total_final", nullable=false, precision=13, scale=4)
	private BigDecimal valEstimativaVolumeTotalFinal;

	@Column(name="val_ima", nullable=false, precision=13, scale=4)
	private BigDecimal valIma;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_especie_floresta")
	private EspecieFloresta ideEspecieFloresta;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_registro_floresta_producao_imovel")
	private RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel;

	public RegistroFlorestaProducaoImovelEspecieProducao() {
	}

	public Integer getIdeRegistroFlorestaProducaoImovelEspecieProducao() {
		return ideRegistroFlorestaProducaoImovelEspecieProducao;
	}

	public void setIdeRegistroFlorestaProducaoImovelEspecieProducao(Integer ideRegistroFlorestaProducaoImovelEspecieProducao) {
		this.ideRegistroFlorestaProducaoImovelEspecieProducao = ideRegistroFlorestaProducaoImovelEspecieProducao;
	}

	public EspecieFloresta getIdeEspecieFloresta() {
		return ideEspecieFloresta;
	}

	public void setIdeEspecieFloresta(EspecieFloresta ideEspecieFloresta) {
		this.ideEspecieFloresta = ideEspecieFloresta;
	}

	public Integer getValCicloCorte() {
		return valCicloCorte;
	}

	public void setValCicloCorte(Integer valCicloCorte) {
		this.valCicloCorte = valCicloCorte;
	}

	public BigDecimal getValEspacamento() {
		return valEspacamento;
	}

	public void setValEspacamento(BigDecimal valEspacamento) {
		this.valEspacamento = valEspacamento;
	}

	public BigDecimal getValEstimativaVolumeTotalFinal() {
		return valEstimativaVolumeTotalFinal;
	}

	public void setValEstimativaVolumeTotalFinal(BigDecimal valEstimativaVolumeTotalFinal) {
		this.valEstimativaVolumeTotalFinal = valEstimativaVolumeTotalFinal;
	}

	public BigDecimal getValIma() {
		return valIma;
	}

	public void setValIma(BigDecimal valIma) {
		this.valIma = valIma;
	}

	public RegistroFlorestaProducaoImovel getIdeRegistroFlorestaProducaoImovel() {
		return ideRegistroFlorestaProducaoImovel;
	}

	public void setIdeRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel) {
		this.ideRegistroFlorestaProducaoImovel = ideRegistroFlorestaProducaoImovel;
	}
	
}