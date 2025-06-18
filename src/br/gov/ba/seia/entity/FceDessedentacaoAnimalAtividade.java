package br.gov.ba.seia.entity;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;



/**
 * The persistent class for the fce_dessedentacao_animal_atividade database table.
 * 
 */
@Entity
@Table(name="fce_dessedentacao_animal_atividade")
public class FceDessedentacaoAnimalAtividade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_des_animal_atividade_ide_fce_des_animal_atividade_generator")
	@SequenceGenerator(name = "fce_des_animal_atividade_ide_fce_des_animal_atividade_generator", sequenceName = "fce_des_animal_atividade_ide_fce_des_animal_atividade_seq", allocationSize = 1)
	@Column(name="ide_fce_dessedentacao_animal_atividade")
	private Integer ideFceDessedentacaoAnimalAtividade;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipologia_atividade")
	private TipologiaAtividade ideTipologiaAtividade;

	@Column(name="num_cabecas", nullable = false)
	private Integer numCabecas;

	@Column(name="num_consumo_diario_cabeca", precision = 10, scale = 2, nullable = false)
	private BigDecimal numConsumoDiarioCabeca;

	@Column(name="num_consumo_diario_especie", precision = 10, scale = 2, nullable = false)
	private BigDecimal numConsumoDiarioEspecie;

	//bi-directional many-to-one association to FceDessedentacaoAnimal
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_dessedentacao_animal")
	private FceDessedentacaoAnimal ideFceDessedentacaoAnimal;

	@Transient
	private boolean confirmado;
	@Transient
	private boolean edicao;

	public FceDessedentacaoAnimalAtividade() {
	}

	public FceDessedentacaoAnimalAtividade(FceDessedentacaoAnimal ideFceDessedentacaoAnimal){
		this.ideFceDessedentacaoAnimal=ideFceDessedentacaoAnimal;
	}

	public FceDessedentacaoAnimalAtividade(FceDessedentacaoAnimal ideFceDessedentacaoAnimal,TipologiaAtividade ideTipologiaAtividade) {
		this.ideFceDessedentacaoAnimal = ideFceDessedentacaoAnimal;
		this.ideTipologiaAtividade = ideTipologiaAtividade;
		this.numConsumoDiarioCabeca = ideTipologiaAtividade.getNumConsumoDiario();
	}

	public FceDessedentacaoAnimalAtividade(TipologiaAtividade tipologiaAtividade){
		this.ideTipologiaAtividade = tipologiaAtividade;
	}

	public Integer getIdeFceDessedentacaoAnimalAtividade() {
		return this.ideFceDessedentacaoAnimalAtividade;
	}

	public void setIdeFceDessedentacaoAnimalAtividade(Integer ideFceDessedentacaoAnimalAtividade) {
		this.ideFceDessedentacaoAnimalAtividade = ideFceDessedentacaoAnimalAtividade;
	}

	public TipologiaAtividade getIdeTipologiaAtividade() {
		return this.ideTipologiaAtividade;
	}

	public void setIdeTipologiaAtividade(TipologiaAtividade ideTipologiaAtividade) {
		this.ideTipologiaAtividade = ideTipologiaAtividade;
	}

	public Integer getNumCabecas() {
		return this.numCabecas;
	}

	public void setNumCabecas(Integer numCabecas) {
		this.numCabecas = numCabecas;
	}

	public BigDecimal getNumConsumoDiarioCabeca() {
		return this.numConsumoDiarioCabeca;
	}

	public void setNumConsumoDiarioCabeca(BigDecimal numConsumoDiarioCabeca) {
		this.numConsumoDiarioCabeca = numConsumoDiarioCabeca;
	}

	public BigDecimal getNumConsumoDiarioEspecie() {
		return this.numConsumoDiarioEspecie;
	}

	public void setNumConsumoDiarioEspecie(BigDecimal numConsumoDiarioEspecie) {
		this.numConsumoDiarioEspecie = numConsumoDiarioEspecie;
	}

	public FceDessedentacaoAnimal getFceDessedentacaoAnimal() {
		return this.ideFceDessedentacaoAnimal;
	}

	public void setFceDessedentacaoAnimal(FceDessedentacaoAnimal fceDessedentacaoAnimal) {
		this.ideFceDessedentacaoAnimal = fceDessedentacaoAnimal;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

}