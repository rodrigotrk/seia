package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="FCE_ENERGIA_TERMOELETRICA_UNIDADE")
@NamedQueries({
	@NamedQuery(name = "FceEnergiaTermoeletricaUnidade.removeByPk", query = "DELETE FROM FceEnergiaTermoeletricaUnidade f WHERE f.ideFceEnergiaTermoeletricaUnidade = :ideFceEnergiaTermoeletricaUnidade")
})
public class FceEnergiaTermoeletricaUnidade implements Serializable {
	
	private static final long serialVersionUID = 7067705235887065771L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_termoeletrica_unidade_seq")
	@SequenceGenerator(name = "fce_energia_termoeletrica_unidade_seq", sequenceName = "fce_energia_termoeletrica_unidade_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE")
	private Integer ideFceEnergiaTermoeletricaUnidade;
	
	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_FCE_ENERGIA_TERMOELETRICA", nullable = false)
	private FceEnergiaTermoEletrica fceEnergiaTermoeletrica;

	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_LOCALIZACAO_GEOGRAFICA", nullable = false)
	private LocalizacaoGeografica localizacaoGeografica;

	@Basic(optional = false)
    @Column(name = "DES_IDENTIFICACAO_UNIDADE")
	private String desIdentificacaoUnidade;

	@Basic(optional = false)
    @NotNull
    @Column(name = "IND_SISTEMA_CONTROLE_EMISSAO", nullable = false)
    private Boolean indSistemaControleEmissao;
	
	@Column(name = "VAL_EFICIENCIA_CONTROLE_EMISSAO")
	private BigDecimal valEficienciaControleEmissao;
	
	@Column(name = "VAL_AREA_UNIDADE")
	private BigDecimal valAreaUnidade;
	
	@Column(name = "QTD_AGUA_ENERGIA_TERMOELETRICA_UNIDADE")
	private BigDecimal quantidadeAgua;
	
	@Column(name = "VAPOR_GERADO_ENERGIA_TERMOELETRICA_UNIDADE")
	private BigDecimal vaporGerado;
	
	@Transient
	private List<FceEnergiaTermoeletricaUnidadeCombustivel> listaCombustivel;
	
	@Transient
	private List<FceEnergiaTermoeletricaUnidadeGerador> listaGerador;
	
	@Transient
	private List<TipoCombustivel> listaTipoCombustivel;
	
	public Integer getIdeFceEnergiaTermoeletricaUnidade() {
		return ideFceEnergiaTermoeletricaUnidade;
	}

	public void setIdeFceEnergiaTermoeletricaUnidade(
			Integer ideFceEnergiaTermoeletricaUnidade) {
		this.ideFceEnergiaTermoeletricaUnidade = ideFceEnergiaTermoeletricaUnidade;
	}

	public FceEnergiaTermoEletrica getFceEnergiaTermoeletrica() {
		return fceEnergiaTermoeletrica;
	}

	public void setFceEnergiaTermoeletrica(
			FceEnergiaTermoEletrica fceEnergiaTermoeletrica) {
		this.fceEnergiaTermoeletrica = fceEnergiaTermoeletrica;
	}

	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	public String getDesIdentificacaoUnidade() {
		return desIdentificacaoUnidade;
	}

	public void setDesIdentificacaoUnidade(String desIdentificacaoUnidade) {
		this.desIdentificacaoUnidade = desIdentificacaoUnidade;
	}

	public Boolean getIndSistemaControleEmissao() {
		return indSistemaControleEmissao;
	}

	public void setIndSistemaControleEmissao(Boolean indSistemaControleEmissao) {
		this.indSistemaControleEmissao = indSistemaControleEmissao;
	}

	public BigDecimal getValEficienciaControleEmissao() {
		return valEficienciaControleEmissao;
	}

	public void setValEficienciaControleEmissao(
			BigDecimal valEficienciaControleEmissao) {
		this.valEficienciaControleEmissao = valEficienciaControleEmissao;
	}

	public BigDecimal getValAreaUnidade() {
		return valAreaUnidade;
	}

	public void setValAreaUnidade(BigDecimal valAreaUnidade) {
		this.valAreaUnidade = valAreaUnidade;
	}

	public List<FceEnergiaTermoeletricaUnidadeCombustivel> getListaCombustivel() {
		return listaCombustivel;
	}

	public void setListaCombustivel(
			List<FceEnergiaTermoeletricaUnidadeCombustivel> listaCombustivel) {
		this.listaCombustivel = listaCombustivel;
	}

	public List<FceEnergiaTermoeletricaUnidadeGerador> getListaGerador() {
		return listaGerador;
	}

	public void setListaGerador(
			List<FceEnergiaTermoeletricaUnidadeGerador> listaGerador) {
		this.listaGerador = listaGerador;
	}

	public List<TipoCombustivel> getListaTipoCombustivel() {
		return listaTipoCombustivel;
	}

	public void setListaTipoCombustivel(List<TipoCombustivel> listaTipoCombustivel) {
		this.listaTipoCombustivel = listaTipoCombustivel;
	}

	public BigDecimal getQuantidadeAgua() {
		return quantidadeAgua;
	}

	public void setQuantidadeAgua(BigDecimal quantidadeAgua) {
		this.quantidadeAgua = quantidadeAgua;
	}

	public BigDecimal getVaporGerado() {
		return vaporGerado;
	}

	public void setVaporGerado(BigDecimal vaporGerado) {
		this.vaporGerado = vaporGerado;
	}

}