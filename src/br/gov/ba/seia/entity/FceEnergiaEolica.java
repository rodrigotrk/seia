package br.gov.ba.seia.entity;

import java.io.Serializable;

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
@Table(name="FCE_ENERGIA_EOLICA")
public class FceEnergiaEolica implements Serializable {
	
	private static final long serialVersionUID = -4045921221735518806L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_eolica_seq")
	@SequenceGenerator(name = "fce_energia_eolica_seq", sequenceName = "fce_energia_eolica_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FCE_ENERGIA_EOLICA")
	private Integer ideFceEnergiaEolica;

	@Basic(optional = false)
    @NotNull
    @Column(name = "IND_LICENCA_PREVIA", nullable = false)
    private Boolean indLicencaPrevia;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_FCE_ENERGIA", nullable = false)
	private FceEnergia ideFceEnergia;
	
	

	public Integer getIdeFceEnergiaEolica() {
		return ideFceEnergiaEolica;
	}

	public void setIdeFceEnergiaEolica(Integer ideFceEnergiaEolica) {
		this.ideFceEnergiaEolica = ideFceEnergiaEolica;
	}

	public FceEnergia getIdeFceEnergia() {
		return ideFceEnergia;
	}

	public void setIdeFceEnergia(FceEnergia ideFceEnergia) {
		this.ideFceEnergia = ideFceEnergia;
	}

	public Boolean getIndLicencaPrevia() {
		return indLicencaPrevia;
	}

	public void setIndLicencaPrevia(Boolean indLicencaPrevia) {
		this.indLicencaPrevia = indLicencaPrevia;
	}
	
}