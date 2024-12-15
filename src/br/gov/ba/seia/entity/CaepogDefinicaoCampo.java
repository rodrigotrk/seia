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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * Tabela que armazena os Campos de exploração do cadastro
 */
@Entity
@Table(name = "caepog_definicao_campo", uniqueConstraints = {
	    @UniqueConstraint(columnNames = {"ide_caepog_campo"}),
	    @UniqueConstraint(columnNames = {"ide_caepog"})})
public class CaepogDefinicaoCampo extends AbstractEntity implements Cloneable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Chave primária da tabela CAEPOG_DEFINICAO_CAMPO
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caepog_definicao_campo_ide_caepog_definicao_campo_seq")
	@SequenceGenerator(name = "caepog_definicao_campo_ide_caepog_definicao_campo_seq", sequenceName = "caepog_definicao_campo_ide_caepog_definicao_campo_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_caepog_definicao_campo", nullable = false)
	private Integer ideCaepogDefinicaoCampo;
	
	/**
	 * Chave primária da tabela CAEPOG_CAMPO
	 */
	@Basic(optional = false)
	@JoinColumn(name = "ide_caepog_campo", referencedColumnName = "ide_caepog_campo", nullable = false, unique = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogCampo ideCaepogCampo;

	/**
	 * Chave primária da tabela CAEPOG
	 */
	@Basic(optional = false)
	@JoinColumn(name = "ide_caepog", referencedColumnName = "ide_caepog", nullable = false, unique = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Caepog ideCaepog;
	
	/**
	 * Número do processo vinculado ao campo
	 */
	@Column(name = "num_processo_caepog_campo", length = 80)
	private String numProcessoCaepogCampo;
	
	/**
	 * Quantidade de poços de óleo e gás perfurados no campo
	 */
	@Column(name = "qtd_pocos")
	private Integer qtdPocos;
	
	/**
	 * Chave estrangeira da localização geográfica do shape do ring fence
	 */
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@OneToMany(mappedBy = "ideCaepogDefinicaoCampo", fetch = FetchType.LAZY)
	private Collection<CaepogLocacao> caepogLocacaoCollection;
	
	@Transient
	private Municipio ideMunicipioTransient;
	
	public CaepogDefinicaoCampo() {
		super();
	}

	public CaepogDefinicaoCampo clone() throws CloneNotSupportedException {
		return (CaepogDefinicaoCampo) super.clone();
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogDefinicaoCampo() {
		return ideCaepogDefinicaoCampo;
	}

	public void setIdeCaepogDefinicaoCampo(Integer ideCaepogDefinicaoCampo) {
		this.ideCaepogDefinicaoCampo = ideCaepogDefinicaoCampo;
	}

	public CaepogCampo getIdeCaepogCampo() {
		return ideCaepogCampo;
	}

	public void setIdeCaepogCampo(CaepogCampo ideCaepogCampo) {
		this.ideCaepogCampo = ideCaepogCampo;
	}

	public Caepog getIdeCaepog() {
		return ideCaepog;
	}

	public void setIdeCaepog(Caepog ideCaepog) {
		this.ideCaepog = ideCaepog;
	}

	public String getNumProcessoCaepogCampo() {
		return numProcessoCaepogCampo;
	}

	public void setNumProcessoCaepogCampo(String numProcessoCaepogCampo) {
		this.numProcessoCaepogCampo = numProcessoCaepogCampo;
	}

	public Integer getQtdPocos() {
		return qtdPocos;
	}

	public void setQtdPocos(Integer qtdPocos) {
		this.qtdPocos = qtdPocos;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public Municipio getIdeMunicipioTransient() {
		return ideMunicipioTransient;
	}

	public void setIdeMunicipioTransient(Municipio ideMunicipioTransient) {
		this.ideMunicipioTransient = ideMunicipioTransient;
	}

	public Collection<CaepogLocacao> getCaepogLocacaoCollection() {
		return caepogLocacaoCollection;
	}

	public void setCaepogLocacaoCollection(Collection<CaepogLocacao> caepogLocacaoCollection) {
		this.caepogLocacaoCollection = caepogLocacaoCollection;
	}
}