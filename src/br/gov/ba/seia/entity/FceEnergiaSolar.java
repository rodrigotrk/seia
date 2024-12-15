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
@Table(name="FCE_ENERGIA_SOLAR")
public class FceEnergiaSolar implements Serializable {
	
	private static final long serialVersionUID = -3103773444342588377L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_solar_seq")
	@SequenceGenerator(name = "fce_energia_solar_seq", sequenceName = "fce_energia_solar_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FCE_ENERGIA_SOLAR")
	private Integer ideFceEnergiaSolar;

	
	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_FCE_ENERGIA", nullable = false)
	private FceEnergia ideFceEnergia;


	
	
	public Integer getIdeFceEnergiaSolar() {
		return ideFceEnergiaSolar;
	}


	public void setIdeFceEnergiaSolar(Integer ideFceEnergiaSolar) {
		this.ideFceEnergiaSolar = ideFceEnergiaSolar;
	}


	public FceEnergia getIdeFceEnergia() {
		return ideFceEnergia;
	}


	public void setIdeFceEnergia(FceEnergia ideFceEnergia) {
		this.ideFceEnergia = ideFceEnergia;
	}
	
	
	
}