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
@Table(name="FCE_ENERGIA_SOLAR_USINA")
public class FceEnergiaSolarUsina implements Serializable {
	
	private static final long serialVersionUID = -3564030837224915999L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_solar_usina_seq")
	@SequenceGenerator(name = "fce_energia_solar_usina_seq", sequenceName = "fce_energia_solar_usina_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FCE_ENERGIA_SOLAR_USINA")
	private Integer ideFceEnergiaSolarUsina;

	@Basic(optional = false)
    @Column(name = "DES_IDENTIFICACAO_USINA", length = 20)
	private String desIdentificacaoUsina;
	
	@Basic(optional = false)
    @Column(name = "VAL_AREA_USINA")
	private BigDecimal valorAreaUsina;
	
	@Basic(optional = false)
    @Column(name = "VAL_POTENCIA_USINA")
	private BigDecimal valorPotenciaUsina;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_FCE_ENERGIA_SOLAR")
	private FceEnergiaSolar ideFceEnergiaSolar;

	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_LOCALIZACAO_GEOGRAFICA")
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	public Integer getIdeFceEnergiaSolarUsina() {
		return ideFceEnergiaSolarUsina;
	}

	public void setIdeFceEnergiaSolarUsina(Integer ideFceEnergiaSolarUsina) {
		this.ideFceEnergiaSolarUsina = ideFceEnergiaSolarUsina;
	}

	public String getDesIdentificacaoUsina() {
		return desIdentificacaoUsina;
	}

	public void setDesIdentificacaoUsina(String desIdentificacaoUsina) {
		this.desIdentificacaoUsina = desIdentificacaoUsina;
	}

	public BigDecimal getValorAreaUsina() {
		return valorAreaUsina;
	}

	public void setValorAreaUsina(BigDecimal valorAreaUsina) {
		this.valorAreaUsina = valorAreaUsina;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(
			LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public BigDecimal getValorPotenciaUsina() {
		return valorPotenciaUsina;
	}

	public void setValorPotenciaUsina(BigDecimal valorPotenciaUsina) {
		this.valorPotenciaUsina = valorPotenciaUsina;
	}

	public FceEnergiaSolar getIdeFceEnergiaSolar() {
		return ideFceEnergiaSolar;
	}

	public void setIdeFceEnergiaSolar(FceEnergiaSolar ideFceEnergiaSolar) {
		this.ideFceEnergiaSolar = ideFceEnergiaSolar;
	}
	

}