package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="FCE_ENERGIA_EOLICA_PARQUE")
public class FceEnergiaEolicaParque implements Serializable {
	
	private static final long serialVersionUID = -4936822540993660625L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_eolica_parque_seq")
	@SequenceGenerator(name = "fce_energia_eolica_parque_seq", sequenceName = "fce_energia_eolica_parque_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FCE_ENERGIA_EOLICA_PARQUE")
	private Integer ideFceEnergiaEolicaParque;

    @Column(name = "DES_IDENTIFICADOR_PARQUE")
	private String desIdentificadorParque;
	
    @Column(name = "VAL_AREA_PARQUE")
	private BigDecimal valorAreaParque;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_LOCALIZACAO_GEOGRAFICA")
	private LocalizacaoGeografica localizacaoGeografica;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_FCE_ENERGIA_EOLICA", nullable = false)
	private FceEnergiaEolica fceEnergiaEolica;

	@Transient
	private Integer numeroAerogeradores;
	
	@OneToMany (cascade={CascadeType.ALL}, orphanRemoval=true, mappedBy="fceEnergiaEolicaParque")
	private List<FceEnergiaEolicaParqueAerogerador> listaAerogerador;
	
	
	public Integer getIdeFceEnergiaEolicaParque() {
		return ideFceEnergiaEolicaParque;
	}

	public void setIdeFceEnergiaEolicaParque(Integer ideFceEnergiaEolicaParque) {
		this.ideFceEnergiaEolicaParque = ideFceEnergiaEolicaParque;
	}

	public String getDesIdentificadorParque() {
		return desIdentificadorParque;
	}

	public void setDesIdentificadorParque(String desIdentificadorParque) {
		this.desIdentificadorParque = desIdentificadorParque;
	}

	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	public FceEnergiaEolica getFceEnergiaEolica() {
		return fceEnergiaEolica;
	}

	public void setFceEnergiaEolica(FceEnergiaEolica fceEnergiaEolica) {
		this.fceEnergiaEolica = fceEnergiaEolica;
	}

	public BigDecimal getValorAreaParque() {
		return valorAreaParque;
	}

	public void setValorAreaParque(BigDecimal valorAreaParque) {
		this.valorAreaParque = valorAreaParque;
	}

	public Integer getNumeroAerogeradores() {
		return numeroAerogeradores;
	}

	public void setNumeroAerogeradores(Integer numeroAerogeradores) {
		this.numeroAerogeradores = numeroAerogeradores;
	}

	public List<FceEnergiaEolicaParqueAerogerador> getListaAerogerador() {
		return listaAerogerador;
	}

	public void setListaAerogerador(
			List<FceEnergiaEolicaParqueAerogerador> listaAerogerador) {
		this.listaAerogerador = listaAerogerador;
	}

}