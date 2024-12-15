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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="FCE_ENERGIA_EOLICA_LICENCA_PREVIA")
public class FceEnergiaEolicaLicencaPrevia implements Serializable {
	
	private static final long serialVersionUID = 5981473387105182842L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_eolica_licenca_previa_seq")
	@SequenceGenerator(name = "fce_energia_eolica_licenca_previa_seq", sequenceName = "fce_energia_eolica_licenca_previa_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FCE_ENERGIA_EOLICA_LICENCA_PREVIA")
	private Integer ideFceEnergiaEolicaLicencaPrevia;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_FCE_ENERGIA_EOLICA", nullable = false)
	private FceEnergiaEolica fceEnergiaEolica;

	@Column(name = "NUM_PARQUES")
	private Integer numParques;
	
	@Column(name = "QTD_GERAL_AEROGERADOR")
	private Integer quantidadeGeralAerogerador;

	@Column(name = "VAL_POTENCIA_AEROGERADOR")
	private BigDecimal valPotenciaAerogerador;

	
	
	
	public Integer getIdeFceEnergiaEolicaLicencaPrevia() {
		return ideFceEnergiaEolicaLicencaPrevia;
	}

	public void setIdeFceEnergiaEolicaLicencaPrevia(
			Integer ideFceEnergiaEolicaLicencaPrevia) {
		this.ideFceEnergiaEolicaLicencaPrevia = ideFceEnergiaEolicaLicencaPrevia;
	}

	public FceEnergiaEolica getFceEnergiaEolica() {
		return fceEnergiaEolica;
	}

	public void setFceEnergiaEolica(FceEnergiaEolica fceEnergiaEolica) {
		this.fceEnergiaEolica = fceEnergiaEolica;
	}

	public Integer getNumParques() {
		return numParques;
	}

	public void setNumParques(Integer numParques) {
		this.numParques = numParques;
	}

	public Integer getQuantidadeGeralAerogerador() {
		return quantidadeGeralAerogerador;
	}

	public void setQuantidadeGeralAerogerador(Integer quantidadeGeralAerogerador) {
		this.quantidadeGeralAerogerador = quantidadeGeralAerogerador;
	}

	public BigDecimal getValPotenciaAerogerador() {
		return valPotenciaAerogerador;
	}

	public void setValPotenciaAerogerador(BigDecimal valPotenciaAerogerador) {
		this.valPotenciaAerogerador = valPotenciaAerogerador;
	}
	
}