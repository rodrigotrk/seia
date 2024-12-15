package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * Tabela que armazena o poço da locação no campo
 */
@Entity
@Table(name = "caepog_poco")
public class CaepogPoco extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_POCO
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caepog_poco_ide_caepog_poco_seq")
	@SequenceGenerator(name = "caepog_poco_ide_caepog_poco_seq", sequenceName = "caepog_poco_ide_caepog_poco_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_caepog_poco", nullable = false)
	private Integer ideCaepogPoco;
	
	/**
	 * Indicador para poços novos, considerando como true poços novos e false como poços antigos
	 */
	@Column(name = "ind_novo_caepog_poco")
	private Boolean indNovoCaepogPoco;
	
	/**
	 * Indicador da alteração do porte, para o caso do requerente marca se houve alteração do porte para um poço novo
	 */
	@Column(name = "ind_alt_porte_caepog_poco")
	private Boolean indAltPorteCaepogPoco;
	
	/**
	 * Número do processo de alteração de licença para o poço novo
	 */
	@Column(name = "num_processo_caepog_poco", length = 60)
	private String numProcessoCaepogPoco;
	
	/**
	 * Indicador de supressão nativa. Marcado true haverá/houve supressão de vegetação
	 */
	@Column(name = "ind_sup_vegeta_caepog_poco")
	private Boolean indSupVegetaCaepogPoco;

	/**
	 * Número do processo da ASV da perfuração do poço
	 */
	@Column(name = "num_processo_asv_caepog_poco", length = 60)
	private String numProcessoAsvCaepogPoco;
	
	
	/**
	 * Quantidade de cascalho gerado por poço (m3)
	 */
	@Column(name = "cascalho_residuo_caepog_poco")
	private BigDecimal cascalhoResiduoCaepogPoco;

	/**
	 * Coeficiente de empolamento do resíduo gerado por poço
	 */
	@Column(name = "empolamento_caepog_poco")
	private Integer empolamentoCaepogPoco;
	
	/**
	 * Profundidade do poço (m3)
	 */
	@Column(name = "profundidade_caepog_poco")
	private Integer profundidadeCaepogPoco;

	/**
	 * Nome do poço cadastrado
	 */
	@Basic(optional = false)
	@Column(name = "nom_poco_caepog_poco", length = 50, nullable = false)
	private String nomPocoCaepogPoco;
	
	/**
	 * Chave estrangeira da localização geográfica do poço
	 */
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	/**
	 * Chave primária da tabela CAEPOG_TIPO_POCO
	 */
	@JoinColumn(name = "ide_caepog_tipo_poco", referencedColumnName = "ide_caepog_tipo_poco")
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogTipoPoco ideCaepogTipoPoco;
	
	/**
	 * Chave primária da tabela CAEPOG_LOCACAO
	 */
	@JoinColumn(name = "ide_caepog_locacao", referencedColumnName = "ide_caepog_locacao")
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogLocacao ideCaepogLocacao;
	
	@OneToMany(mappedBy = "ideCaepogPoco", fetch = FetchType.LAZY)
	private Collection<CaepogFasePerfuracao> caepogFasePerfuracaoCollection;
	
	@Transient
	private Double latitudeCaepogPoco;
	
	@Transient
	private Double longitudeCaepogPoco;
	
	public CaepogPoco() {
		super();
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogPoco() {
		return ideCaepogPoco;
	}

	public void setIdeCaepogPoco(Integer ideCaepogPoco) {
		this.ideCaepogPoco = ideCaepogPoco;
	}

	public Boolean getIndNovoCaepogPoco() {
		return indNovoCaepogPoco;
	}

	public void setIndNovoCaepogPoco(Boolean indNovoCaepogPoco) {
		this.indNovoCaepogPoco = indNovoCaepogPoco;
	}

	public Boolean getIndAltPorteCaepogPoco() {
		return indAltPorteCaepogPoco;
	}

	public void setIndAltPorteCaepogPoco(Boolean indAltPorteCaepogPoco) {
		this.indAltPorteCaepogPoco = indAltPorteCaepogPoco;
	}

	public String getNumProcessoCaepogPoco() {
		return numProcessoCaepogPoco;
	}

	public void setNumProcessoCaepogPoco(String numProcessoCaepogPoco) {
		this.numProcessoCaepogPoco = numProcessoCaepogPoco;
	}

	public Boolean getIndSupVegetaCaepogPoco() {
		return indSupVegetaCaepogPoco;
	}

	public void setIndSupVegetaCaepogPoco(Boolean indSupVegetaCaepogPoco) {
		this.indSupVegetaCaepogPoco = indSupVegetaCaepogPoco;
	}

	public String getNumProcessoAsvCaepogPoco() {
		return numProcessoAsvCaepogPoco;
	}

	public void setNumProcessoAsvCaepogPoco(String numProcessoAsvCaepogPoco) {
		this.numProcessoAsvCaepogPoco = numProcessoAsvCaepogPoco;
	}

	public BigDecimal getCascalhoResiduoCaepogPoco() {
		return cascalhoResiduoCaepogPoco;
	}

	public void setCascalhoResiduoCaepogPoco(BigDecimal cascalhoResiduoCaepogPoco) {
		this.cascalhoResiduoCaepogPoco = cascalhoResiduoCaepogPoco;
	}

	public Integer getEmpolamentoCaepogPoco() {
		return empolamentoCaepogPoco;
	}

	public void setEmpolamentoCaepogPoco(Integer empolamentoCaepogPoco) {
		this.empolamentoCaepogPoco = empolamentoCaepogPoco;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public CaepogTipoPoco getIdeCaepogTipoPoco() {
		return ideCaepogTipoPoco;
	}

	public void setIdeCaepogTipoPoco(CaepogTipoPoco ideCaepogTipoPoco) {
		this.ideCaepogTipoPoco = ideCaepogTipoPoco;
	}

	public CaepogLocacao getIdeCaepogLocacao() {
		return ideCaepogLocacao;
	}

	public void setIdeCaepogLocacao(CaepogLocacao ideCaepogLocacao) {
		this.ideCaepogLocacao = ideCaepogLocacao;
	}

	public Integer getProfundidadeCaepogPoco() {
		return profundidadeCaepogPoco;
	}

	public void setProfundidadeCaepogPoco(Integer profundidadeCaepogPoco) {
		this.profundidadeCaepogPoco = profundidadeCaepogPoco;
	}

	public Collection<CaepogFasePerfuracao> getCaepogFasePerfuracaoCollection() {
		return caepogFasePerfuracaoCollection;
	}

	public void setCaepogFasePerfuracaoCollection(Collection<CaepogFasePerfuracao> caepogFasePerfuracaoCollection) {
		this.caepogFasePerfuracaoCollection = caepogFasePerfuracaoCollection;
	}

	public String getNomPocoCaepogPoco() {
		return nomPocoCaepogPoco;
	}

	public void setNomPocoCaepogPoco(String nomPocoCaepogPoco) {
		this.nomPocoCaepogPoco = nomPocoCaepogPoco;
	}

	public Double getLatitudeCaepogPoco() {
		return latitudeCaepogPoco;
	}

	public void setLatitudeCaepogPoco(Double latitudeCaepogPoco) {
		this.latitudeCaepogPoco = latitudeCaepogPoco;
	}

	public Double getLongitudeCaepogPoco() {
		return longitudeCaepogPoco;
	}

	public void setLongitudeCaepogPoco(Double longitudeCaepogPoco) {
		this.longitudeCaepogPoco = longitudeCaepogPoco;
	}
}