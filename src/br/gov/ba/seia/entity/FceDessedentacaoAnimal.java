package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.interfaces.BaseEntity;




/**
 * The persistent class for the fce_dessedentacao_animal database table.
 *
 */
@Entity
@Table(name="fce_dessedentacao_animal")
public class FceDessedentacaoAnimal implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_des_animal_ide_fce_des_animal_generator")
	@SequenceGenerator(name = "fce_des_animal_ide_fce_des_animal_generator", sequenceName = "fce_des_animal_ide_fce_des_animal_seq", allocationSize = 1)
	@Column(name="ide_fce_dessedentacao_animal")
	private Integer ideFceDessedentacaoAnimal;

	@NotNull
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@NotNull
	@JoinColumn(name="ide_tipo_periodo_derivacao",referencedColumnName = "ide_tipo_periodo_derivacao", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private TipoPeriodoDerivacao ideTipoPeriodoDerivacao;

	@Column(name="ind_sistema_extensivo", nullable = false)
	private Boolean indSistemaExtensivo;

	@Column(name="ind_sistema_intesivo", nullable = false)
	private Boolean indSistemaIntesivo;

	@Column(name="ind_sistema_semi_intensivo", nullable = false)
	private Boolean indSistemaSemiIntensivo;

	@Column(name="num_tempo_captacao", nullable = true)
	private Integer numTempoCaptacao;

	@Column(name="num_total_cabecas", nullable = true)
	private Integer numTotalCabecas;

	@Column(name="num_total_consumo_especie", precision = 10, scale = 2, nullable = true)
	private BigDecimal numTotalConsumoEspecie;

	@Column(name="num_volume_derivar", precision = 10, scale = 3, nullable = false)
	private BigDecimal numVolumeDerivar;

	//bi-directional many-to-one association to FceDessedentacaoAnimalAtividade
	@OneToMany(mappedBy="ideFceDessedentacaoAnimal")
	private List<FceDessedentacaoAnimalAtividade> fceDessedentacaoAnimalAtividades;

	@NotNull
	@Column(name="ind_outros", nullable = false)
	private Boolean indOutros;

	public FceDessedentacaoAnimal() {
	}

	public FceDessedentacaoAnimal(Fce fce) {
		this.ideFce = fce;
	}

	public Integer getIdeFceDessedentacaoAnimal() {
		return this.ideFceDessedentacaoAnimal;
	}

	public void setIdeFceDessedentacaoAnimal(Integer ideFceDessedentacaoAnimal) {
		this.ideFceDessedentacaoAnimal = ideFceDessedentacaoAnimal;
	}

	public Boolean getIndSistemaExtensivo() {
		return this.indSistemaExtensivo;
	}

	public void setIndSistemaExtensivo(Boolean indSistemaExtensivo) {
		this.indSistemaExtensivo = indSistemaExtensivo;
	}

	public Boolean getIndSistemaIntesivo() {
		return this.indSistemaIntesivo;
	}

	public void setIndSistemaIntesivo(Boolean indSistemaIntesivo) {
		this.indSistemaIntesivo = indSistemaIntesivo;
	}

	public Integer getNumTempoCaptacao() {
		return this.numTempoCaptacao;
	}

	public void setNumTempoCaptacao(Integer numTempoCaptacao) {
		this.numTempoCaptacao = numTempoCaptacao;
	}

	public Integer getNumTotalCabecas() {
		return this.numTotalCabecas;
	}

	public void setNumTotalCabecas(Integer numTotalCabecas) {
		this.numTotalCabecas = numTotalCabecas;
	}

	public BigDecimal getNumTotalConsumoEspecie() {
		return this.numTotalConsumoEspecie;
	}

	public void setNumTotalConsumoEspecie(BigDecimal numTotalConsumoEspecie) {
		this.numTotalConsumoEspecie = numTotalConsumoEspecie;
	}

	public BigDecimal getNumVolumeDerivar() {
		return this.numVolumeDerivar;
	}

	public void setNumVolumeDerivar(BigDecimal numVolumeDerivar) {
		this.numVolumeDerivar = numVolumeDerivar;
	}

	public List<FceDessedentacaoAnimalAtividade> getFceDessedentacaoAnimalAtividades() {
		return this.fceDessedentacaoAnimalAtividades;
	}

	public void setFceDessedentacaoAnimalAtividades(List<FceDessedentacaoAnimalAtividade> fceDessedentacaoAnimalAtividades) {
		this.fceDessedentacaoAnimalAtividades = fceDessedentacaoAnimalAtividades;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public TipoPeriodoDerivacao getIdeTipoPeriodoDerivacao() {
		return ideTipoPeriodoDerivacao;
	}

	public void setIdeTipoPeriodoDerivacao(
			TipoPeriodoDerivacao ideTipoPeriodoDerivacao) {
		this.ideTipoPeriodoDerivacao = ideTipoPeriodoDerivacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime
				* result)
				+ ((ideFceDessedentacaoAnimal == null) ? 0
						: ideFceDessedentacaoAnimal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FceDessedentacaoAnimal other = (FceDessedentacaoAnimal) obj;
		if (ideFceDessedentacaoAnimal == null) {
			if (other.ideFceDessedentacaoAnimal != null) {
				return false;
			}
		} else if (!ideFceDessedentacaoAnimal
				.equals(other.ideFceDessedentacaoAnimal)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideFceDessedentacaoAnimal);
	}

	public Boolean getIndOutros() {
		return indOutros;
	}

	public void setIndOutros(Boolean indOutros) {
		this.indOutros = indOutros;
	}

	public Boolean getIndSistemaSemiIntensivo() {
		return indSistemaSemiIntensivo;
	}

	public void setIndSistemaSemiIntensivo(Boolean indSistemaSemiIntensivo) {
		this.indSistemaSemiIntensivo = indSistemaSemiIntensivo;
	}

}