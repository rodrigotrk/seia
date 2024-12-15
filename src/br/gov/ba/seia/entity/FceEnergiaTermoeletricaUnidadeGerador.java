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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="FCE_ENERGIA_TERMOELETRICA_UNIDADE_GERADOR")
@NamedQueries({
	@NamedQuery(name = "FceEnergiaTermoeletricaUnidadeGerador.removeByPk", query = "DELETE FROM FceEnergiaTermoeletricaUnidadeGerador f WHERE f.ideFceEnergiaTermoeletricaUnidadeGerador = :ideFceEnergiaTermoeletricaUnidadeGerador")
})
public class FceEnergiaTermoeletricaUnidadeGerador implements Serializable {
	

	private static final long serialVersionUID = -7431925057729130847L;


	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_termoeletrica_unidade_gerador_seq")
	@SequenceGenerator(name = "fce_energia_termoeletrica_unidade_gerador_seq", sequenceName = "fce_energia_termoeletrica_unidade_gerador_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE_GERADOR")
	private Integer ideFceEnergiaTermoeletricaUnidadeGerador;

	
	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE", nullable = false)
	private FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade;

	@Basic(optional = false)
    @Column(name = "DES_IDENTIFICACAO_GERADOR")
	private String desIdentificacaoGerador;

	@Column(name = "VAL_POTENCIA_GERADOR")
	private BigDecimal valPotenciaGerador;

	
	
	
	public Integer getIdeFceEnergiaTermoeletricaUnidadeGerador() {
		return ideFceEnergiaTermoeletricaUnidadeGerador;
	}

	public void setIdeFceEnergiaTermoeletricaUnidadeGerador(
			Integer ideFceEnergiaTermoeletricaUnidadeGerador) {
		this.ideFceEnergiaTermoeletricaUnidadeGerador = ideFceEnergiaTermoeletricaUnidadeGerador;
	}

	public FceEnergiaTermoeletricaUnidade getFceEnergiaTermoeletricaUnidade() {
		return fceEnergiaTermoeletricaUnidade;
	}

	public void setFceEnergiaTermoeletricaUnidade(
			FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade) {
		this.fceEnergiaTermoeletricaUnidade = fceEnergiaTermoeletricaUnidade;
	}

	public String getDesIdentificacaoGerador() {
		return desIdentificacaoGerador;
	}

	public void setDesIdentificacaoGerador(String desIdentificacaoGerador) {
		this.desIdentificacaoGerador = desIdentificacaoGerador;
	}

	public BigDecimal getValPotenciaGerador() {
		return valPotenciaGerador;
	}

	public void setValPotenciaGerador(BigDecimal valPotenciaGerador) {
		this.valPotenciaGerador = valPotenciaGerador;
	}
	
		
}