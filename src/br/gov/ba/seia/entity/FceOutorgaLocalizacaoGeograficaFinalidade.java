package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the fce_outorga_localizacao_geografica_finalidade database table.
 * 
 */
@Entity
@Table(name="fce_outorga_localizacao_geografica_finalidade")
@NamedQuery(name="FceOutorgaLocalizacaoGeograficaFinalidade.findAll", query="SELECT f FROM FceOutorgaLocalizacaoGeograficaFinalidade f")
public class FceOutorgaLocalizacaoGeograficaFinalidade implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_OUTORGA_LOCALIZACAO_GEOGRAFICA_FINALIDADE_IDE_OUTORGA_SEQ", sequenceName="FCE_OUTORGA_LOCALIZACAO_GEOGRAFICA_FINALIDADE_IDE_OUTORGA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_OUTORGA_LOCALIZACAO_GEOGRAFICA_FINALIDADE_IDE_OUTORGA_SEQ")
	@Column(name="ide_fce_outorga_localizacao_geografica_finalidade")
	private Integer ideFceOutorgaLocalizacaoGeograficaFinalidade;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_outorga_localizacao_geografica")
	private FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_finalidade_uso_agua")
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;

	@Column(name="ind_captacao")
	private Boolean indCaptacao;

	
	public FceOutorgaLocalizacaoGeograficaFinalidade() {
		
	}
	
	public FceOutorgaLocalizacaoGeograficaFinalidade(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica, TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua, boolean indCaptacao) {
		this.ideFceOutorgaLocalizacaoGeografica = fceOutorgaLocalizacaoGeografica;
		this.ideTipoFinalidadeUsoAgua = tipoFinalidadeUsoAgua;
		this.indCaptacao = indCaptacao;
	}

	public Integer getIdeFceOutorgaLocalizacaoGeograficaFinalidade() {
		return ideFceOutorgaLocalizacaoGeograficaFinalidade;
	}

	public void setIdeFceOutorgaLocalizacaoGeograficaFinalidade(Integer ideFceOutorgaLocalizacaoGeograficaFinalidade) {
		this.ideFceOutorgaLocalizacaoGeograficaFinalidade = ideFceOutorgaLocalizacaoGeograficaFinalidade;
	}

	public FceOutorgaLocalizacaoGeografica getIdeFceOutorgaLocalizacaoGeografica() {
		return ideFceOutorgaLocalizacaoGeografica;
	}

	public void setIdeFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}

	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public Boolean getIndCaptacao() {
		return indCaptacao;
	}

	public void setIndCaptacao(Boolean indCaptacao) {
		this.indCaptacao = indCaptacao;
	}
	
}