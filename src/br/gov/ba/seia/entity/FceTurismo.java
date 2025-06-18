package br.gov.ba.seia.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntity;


/**
 * Entidade que armazena as informações preenchidas pelo usuário no FCE - Turismo.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 04/12/2014
 */
@Entity
@Table(name="fce_turismo")
public class FceTurismo extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_TURISMO_IDEFCETURISMO_GENERATOR", sequenceName="FCE_TURISMO_IDE_FCE_TURISMO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_TURISMO_IDEFCETURISMO_GENERATOR")
	@Column(name="ide_fce_turismo", nullable = false)
	private Integer ideFceTurismo;

	@Column(name="dsc_lei_municipal", length = 50)
	private String dscLeiMunicipal;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_documento_obrigatorio_requerimento", referencedColumnName = "ide_documento_obrigatorio_requerimento", nullable = false)
	private DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce", referencedColumnName = "ide_fce", nullable = false)
	private Fce ideFce;

	@Column(name="ind_plano_diretor", nullable = false)
	private Boolean indPlanoDiretor;

	@Column(name="ind_zona_extensao", nullable = false)
	private Boolean indZonaExtensao;

	@Column(name="num_area_app")
	private BigDecimal numAreaApp;

	@Column(name="num_area_construida", nullable = false)
	private BigDecimal numAreaConstruida;

	public FceTurismo() {
	}

	public FceTurismo(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Integer getIdeFceTurismo() {
		return this.ideFceTurismo;
	}

	public void setIdeFceTurismo(Integer ideFceTurismo) {
		this.ideFceTurismo = ideFceTurismo;
	}

	public String getDscLeiMunicipal() {
		return this.dscLeiMunicipal;
	}

	public void setDscLeiMunicipal(String dscLeiMunicipal) {
		this.dscLeiMunicipal = dscLeiMunicipal;
	}

	public Boolean getIndPlanoDiretor() {
		return this.indPlanoDiretor;
	}

	public void setIndPlanoDiretor(Boolean indPlanoDiretor) {
		this.indPlanoDiretor = indPlanoDiretor;
	}

	public Boolean getIndZonaExtensao() {
		return this.indZonaExtensao;
	}

	public void setIndZonaExtensao(Boolean indZonaExtensao) {
		this.indZonaExtensao = indZonaExtensao;
	}

	public BigDecimal getNumAreaApp() {
		return this.numAreaApp;
	}

	public void setNumAreaApp(BigDecimal numAreaApp) {
		this.numAreaApp = numAreaApp;
	}

	public BigDecimal getNumAreaConstruida() {
		return this.numAreaConstruida;
	}

	public void setNumAreaConstruida(BigDecimal numAreaConstruida) {
		this.numAreaConstruida = numAreaConstruida;
	}

	public DocumentoObrigatorioRequerimento getIdeDocumentoObrigatorioRequerimento() {
		return ideDocumentoObrigatorioRequerimento;
	}

	public void setIdeDocumentoObrigatorioRequerimento(
			DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento) {
		this.ideDocumentoObrigatorioRequerimento = ideDocumentoObrigatorioRequerimento;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}
}