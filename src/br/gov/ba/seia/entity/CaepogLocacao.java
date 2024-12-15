package br.gov.ba.seia.entity;

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

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * Tabela que armazena a locação do campo de exploração
 */
@Entity
@Table(name = "caepog_locacao")
public class CaepogLocacao extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_LOCACAO
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caepog_locacao_ide_caepog_locacao_seq")
	@SequenceGenerator(name = "caepog_locacao_ide_caepog_locacao_seq", sequenceName = "caepog_locacao_ide_caepog_locacao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_caepog_locacao", nullable = false)
	private Integer ideCaepogLocacao;
	
	/**
	 * Prefixo do poço da locação
	 */
	@Column(name = "prefix_poco_caepog_locacao", length = 20)
	private String prefixPocoCaepogLocacao;

	/**
	 * Número do processo que licenciou a locação
	 */
	@Column(name = "num_processo_caepog_locacao", length = 60)
	private String numProcessoCaepogLocacao;

	/**
	 * Tamanho da área da locação (m²)
	 */
	@Basic(optional = false)
	@Column(name = "area_locacao_caepog_locacao", nullable = false)
	private Integer areaLocacaoCaepogLocacao;

	/**
	 * Chave estrangeira da localização geográfica do shape da locação
	 */
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	/**
	 * Chave primária da tabela CAEPOG_DEFINICAO_CAMPO
	 */
	@JoinColumn(name = "ide_caepog_definicao_campo", referencedColumnName = "ide_caepog_definicao_campo")
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogDefinicaoCampo ideCaepogDefinicaoCampo;
	
	@OneToMany(mappedBy = "ideCaepogLocacao", fetch = FetchType.LAZY)
	private Collection<CaepogPoco> caepogPocoCollection;
	
	public CaepogLocacao() {
		super();
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogLocacao() {
		return ideCaepogLocacao;
	}

	public void setIdeCaepogLocacao(Integer ideCaepogLocacao) {
		this.ideCaepogLocacao = ideCaepogLocacao;
	}

	public String getPrefixPocoCaepogLocacao() {
		return prefixPocoCaepogLocacao;
	}

	public void setPrefixPocoCaepogLocacao(String prefixPocoCaepogLocacao) {
		this.prefixPocoCaepogLocacao = prefixPocoCaepogLocacao;
	}

	public String getNumProcessoCaepogLocacao() {
		return numProcessoCaepogLocacao;
	}

	public void setNumProcessoCaepogLocacao(String numProcessoCaepogLocacao) {
		this.numProcessoCaepogLocacao = numProcessoCaepogLocacao;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public CaepogDefinicaoCampo getIdeCaepogDefinicaoCampo() {
		return ideCaepogDefinicaoCampo;
	}

	public void setIdeCaepogDefinicaoCampo(CaepogDefinicaoCampo ideCaepogDefinicaoCampo) {
		this.ideCaepogDefinicaoCampo = ideCaepogDefinicaoCampo;
	}

	public Collection<CaepogPoco> getCaepogPocoCollection() {
		return caepogPocoCollection;
	}

	public void setCaepogPocoCollection(Collection<CaepogPoco> caepogPocoCollection) {
		this.caepogPocoCollection = caepogPocoCollection;
	}

	public Integer getAreaLocacaoCaepogLocacao() {
		return areaLocacaoCaepogLocacao;
	}

	public void setAreaLocacaoCaepogLocacao(Integer areaLocacaoCaepogLocacao) {
		this.areaLocacaoCaepogLocacao = areaLocacaoCaepogLocacao;
	}
}