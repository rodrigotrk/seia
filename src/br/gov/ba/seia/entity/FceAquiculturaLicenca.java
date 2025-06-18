package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the fce_aquicultura_licenca database table.
 *
 */
@Entity
@Table(name="fce_aquicultura_licenca")
@NamedQuery(name="FceAquiculturaLicenca.findAll", query="SELECT f FROM FceAquiculturaLicenca f")
public class FceAquiculturaLicenca implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_LICENCA_IDEFCEAQUICULTURALICENCA_GENERATOR", sequenceName="FCE_AQUICULTURA_LICENCA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_LICENCA_IDEFCEAQUICULTURALICENCA_GENERATOR")
	@Column(name="ide_fce_aquicultura_licenca")
	private Integer ideFceAquiculturaLicenca;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_documento_obrigatorio_requerimento", referencedColumnName = "ide_documento_obrigatorio_requerimento")
	private DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce", referencedColumnName = "ide_fce", nullable = false)
	private Fce ideFce;

	@Column(name="ind_aquicultura_tanque_rede")
	private Boolean indAquiculturaTanqueRede;

	@Column(name="ind_aquicultura_viveiro_escavado")
	private Boolean indAquiculturaViveiroEscavado;

	@Column(name="num_area_ocupada")
	private BigDecimal numAreaOcupada;

	@Column(name="num_vazao_captacao")
	private BigDecimal numVazaoCaptacao;

	@Column(name="num_vazao_lancamento")
	private BigDecimal numVazaoLancamento;

	public FceAquiculturaLicenca() {
	}

	public FceAquiculturaLicenca(Fce fce) {
		this.ideFce = fce;
	}

	public Integer getIdeFceAquiculturaLicenca() {
		return this.ideFceAquiculturaLicenca;
	}
	
	public void setIdeFceAquiculturaLicenca(Integer ideFceAquiculturaLicenca) {
		this.ideFceAquiculturaLicenca = ideFceAquiculturaLicenca;
	}

	public DocumentoObrigatorioRequerimento getIdeDocumentoObrigatorioRequerimento() {
		return ideDocumentoObrigatorioRequerimento;
	}

	public void setIdeDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento) {
		this.ideDocumentoObrigatorioRequerimento = ideDocumentoObrigatorioRequerimento;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Boolean getIndAquiculturaTanqueRede() {
		return this.indAquiculturaTanqueRede;
	}

	public void setIndAquiculturaTanqueRede(Boolean indAquiculturaTanqueRede) {
		this.indAquiculturaTanqueRede = indAquiculturaTanqueRede;
	}

	public Boolean getIndAquiculturaViveiroEscavado() {
		return this.indAquiculturaViveiroEscavado;
	}

	public void setIndAquiculturaViveiroEscavado(Boolean indAquiculturaViveiroEscavado) {
		this.indAquiculturaViveiroEscavado = indAquiculturaViveiroEscavado;
	}

	public BigDecimal getNumAreaOcupada() {
		return this.numAreaOcupada;
	}

	public void setNumAreaOcupada(BigDecimal numAreaOcupada) {
		this.numAreaOcupada = numAreaOcupada;
	}

	public BigDecimal getNumVazaoCaptacao() {
		return numVazaoCaptacao;
	}

	public void setNumVazaoCaptacao(BigDecimal numVazaoCaptacao) {
		this.numVazaoCaptacao = numVazaoCaptacao;
	}

	public BigDecimal getNumVazaoLancamento() {
		return numVazaoLancamento;
	}

	public void setNumVazaoLancamento(BigDecimal numVazaoLancamento) {
		this.numVazaoLancamento = numVazaoLancamento;
	}
}