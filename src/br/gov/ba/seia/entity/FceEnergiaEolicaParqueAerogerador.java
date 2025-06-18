package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
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
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="FCE_ENERGIA_EOLICA_PARQUE_AEROGERADOR")
public class FceEnergiaEolicaParqueAerogerador implements Serializable {
	

	private static final long serialVersionUID = 8171662511246362553L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_eolica_parque_aerogerador_seq")
	@SequenceGenerator(name = "fce_energia_eolica_parque_aerogerador_seq", sequenceName = "fce_energia_eolica_parque_aerogerador_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FCE_ENERGIA_EOLICA_PARQUE_AEROGERADOR")
	private Integer ideFceEnergiaEolicaParqueAerogerador;

	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_FCE_ENERGIA_EOLICA_PARQUE")
	private FceEnergiaEolicaParque fceEnergiaEolicaParque;
	
	@Basic(optional = false)
    @Column(name = "VAL_POTENCIA_AEROGERADOR")
	private BigDecimal valorPotenciaAerogerador;
	
	@Column(name="val_qtde_aerogeradores")
	private Integer quantidadeAerogeradores;
	
	@Transient
	private BigDecimal totalPotencia;
	
	
	public Integer getIdeFceEnergiaEolicaParqueAerogerador() {
		return ideFceEnergiaEolicaParqueAerogerador;
	}

	public void setIdeFceEnergiaEolicaParqueAerogerador(
			Integer ideFceEnergiaEolicaParqueAerogerador) {
		this.ideFceEnergiaEolicaParqueAerogerador = ideFceEnergiaEolicaParqueAerogerador;
	}


	public FceEnergiaEolicaParque getFceEnergiaEolicaParque() {
		return fceEnergiaEolicaParque;
	}

	public void setFceEnergiaEolicaParque(
			FceEnergiaEolicaParque fceEnergiaEolicaParque) {
		this.fceEnergiaEolicaParque = fceEnergiaEolicaParque;
	}

	public BigDecimal getValorPotenciaAerogerador() {
		return valorPotenciaAerogerador;
	}

	public void setValorPotenciaAerogerador(BigDecimal valorPotenciaAerogerador) {
		this.valorPotenciaAerogerador = valorPotenciaAerogerador;
	}

	public Integer getQuantidadeAerogeradores() {
		return quantidadeAerogeradores;
	}

	public void setQuantidadeAerogeradores(Integer quantidadeAerogeradores) {
		this.quantidadeAerogeradores = quantidadeAerogeradores;
	}

	public BigDecimal getTotalPotencia() {
		return totalPotencia;
	}

	public void setTotalPotencia(BigDecimal totalPotencia) {
		this.totalPotencia = totalPotencia;
	}
	
}