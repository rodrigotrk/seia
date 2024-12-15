package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 * Tabela que armazena as fases da perfuração dos poços
 */
@Entity
@Table(name = "caepog_fase_perfuracao")
public class CaepogFasePerfuracao extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_FASE_PERFURACAO
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caepog_fase_perfuracao_ide_caepog_fase_perfuracao_seq")
	@SequenceGenerator(name = "caepog_fase_perfuracao_ide_caepog_fase_perfuracao_seq", sequenceName = "caepog_fase_perfuracao_ide_caepog_fase_perfuracao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name = "ide_caepog_fase_perfuracao", nullable = false)
	private Integer ideCaepogFasePerfuracao;
	
	/**
	 * Sequencial da fase da perfuração para cada poço
	 */
	@Column(name = "seq_caepog_perfuracao_poco")
	private Integer seqCaepogPerfuracaoPoco;
	
	/**
	 * Chave primária da tabela CAEPOG_POCO
	 */
	@JoinColumn(name = "ide_caepog_poco", referencedColumnName = "ide_caepog_poco")
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogPoco ideCaepogPoco;
	
	/**
	 * Chave primária da tabela CAEPOG_TIPO_RESIDUO
	 */
	@JoinColumn(name = "ide_caepog_tipo_residuo", referencedColumnName = "ide_caepog_tipo_residuo")
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogTipoResiduo ideCaepogTipoResiduo;
	
	/**
	 * Chave primária da tabela CAEPOG_CLASSE_RESIDUO
	 */
	@JoinColumn(name = "ide_caepog_classe_residuo", referencedColumnName = "ide_caepog_classe_residuo")
	@ManyToOne(fetch = FetchType.LAZY)
	private CaepogClasseResiduo ideCaepogClasseResiduo;
	
	/**
	 * Chave estrangeira da tabela PESSOA_JURIDICA que será o destino do resíduo gerado na fase de perfuração
	 */
	@JoinColumn(name = "ide_pessoa_juridica_destino", referencedColumnName = "ide_pessoa_juridica")
	@ManyToOne(fetch = FetchType.LAZY)
	private PessoaJuridica idePessoaJuridicaDestino;
	
	@Transient
	private CaepogFaseVariavel profundidadePerfurada;
	
	@Transient
	private CaepogFaseVariavel extensao;
	
	@Transient
	private CaepogFaseVariavel diametro;
	
	@Transient
	private CaepogFaseVariavel tph;
	
	@Transient
	private CaepogFaseVariavel volumeEstimado;
	
	@Transient
	private CaepogFaseVariavel salinidade;
	
	@Transient
	private boolean confirmadoAbaPoco;
	
	@Transient
	private boolean confirmadoAbaResiduo;
	
	public CaepogFasePerfuracao() {
		super();
	}
	
	public CaepogFasePerfuracao(Integer seqCaepogPerfuracaoPoco) {
		super();
		this.seqCaepogPerfuracaoPoco = seqCaepogPerfuracaoPoco;
	}

	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogFasePerfuracao() {
		return ideCaepogFasePerfuracao;
	}

	public void setIdeCaepogFasePerfuracao(Integer ideCaepogFasePerfuracao) {
		this.ideCaepogFasePerfuracao = ideCaepogFasePerfuracao;
	}

	public Integer getSeqCaepogPerfuracaoPoco() {
		return seqCaepogPerfuracaoPoco;
	}

	public void setSeqCaepogPerfuracaoPoco(Integer seqCaepogPerfuracaoPoco) {
		this.seqCaepogPerfuracaoPoco = seqCaepogPerfuracaoPoco;
	}

	public CaepogPoco getIdeCaepogPoco() {
		return ideCaepogPoco;
	}

	public void setIdeCaepogPoco(CaepogPoco ideCaepogPoco) {
		this.ideCaepogPoco = ideCaepogPoco;
	}

	public CaepogTipoResiduo getIdeCaepogTipoResiduo() {
		return ideCaepogTipoResiduo;
	}

	public void setIdeCaepogTipoResiduo(CaepogTipoResiduo ideCaepogTipoResiduo) {
		this.ideCaepogTipoResiduo = ideCaepogTipoResiduo;
	}

	public CaepogClasseResiduo getIdeCaepogClasseResiduo() {
		return ideCaepogClasseResiduo;
	}

	public void setIdeCaepogClasseResiduo(CaepogClasseResiduo ideCaepogClasseResiduo) {
		this.ideCaepogClasseResiduo = ideCaepogClasseResiduo;
	}

	public PessoaJuridica getIdePessoaJuridicaDestino() {
		return idePessoaJuridicaDestino;
	}

	public void setIdePessoaJuridicaDestino(PessoaJuridica idePessoaJuridicaDestino) {
		this.idePessoaJuridicaDestino = idePessoaJuridicaDestino;
	}

	public CaepogFaseVariavel getProfundidadePerfurada() {
		return profundidadePerfurada;
	}

	public void setProfundidadePerfurada(CaepogFaseVariavel profundidadePerfurada) {
		this.profundidadePerfurada = profundidadePerfurada;
	}

	public CaepogFaseVariavel getExtensao() {
		return extensao;
	}

	public void setExtensao(CaepogFaseVariavel extensao) {
		this.extensao = extensao;
	}

	public CaepogFaseVariavel getDiametro() {
		return diametro;
	}

	public void setDiametro(CaepogFaseVariavel diametro) {
		this.diametro = diametro;
	}

	public CaepogFaseVariavel getTph() {
		return tph;
	}

	public void setTph(CaepogFaseVariavel tph) {
		this.tph = tph;
	}

	public CaepogFaseVariavel getVolumeEstimado() {
		return volumeEstimado;
	}

	public void setVolumeEstimado(CaepogFaseVariavel volumeEstimado) {
		this.volumeEstimado = volumeEstimado;
	}

	public CaepogFaseVariavel getSalinidade() {
		return salinidade;
	}

	public void setSalinidade(CaepogFaseVariavel salinidade) {
		this.salinidade = salinidade;
	}

	public boolean isConfirmadoAbaPoco() {
		return confirmadoAbaPoco;
	}

	public void setConfirmadoAbaPoco(boolean confirmadoAbaPoco) {
		this.confirmadoAbaPoco = confirmadoAbaPoco;
	}

	public boolean isConfirmadoAbaResiduo() {
		return confirmadoAbaResiduo;
	}

	public void setConfirmadoAbaResiduo(boolean confirmadoAbaResiduo) {
		this.confirmadoAbaResiduo = confirmadoAbaResiduo;
	}
}