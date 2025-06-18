package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.List;

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
import javax.persistence.Transient;

import org.primefaces.event.FileUploadEvent;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.util.Util;


/**
 * The persistent class for the fce_aquicultura database table.
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 05/11/2014
 */
@Entity
@Table(name="fce_aquicultura")
@NamedQuery(name="FceAquicultura.findAll", query="SELECT f FROM FceAquicultura f")
public class FceAquicultura extends AbstractEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_AQUICULTURA_IDEFCEAQUICULTURA_GENERATOR", sequenceName="FCE_AQUICULTURA_IDE_FCE_AQUICULTURA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_AQUICULTURA_IDEFCEAQUICULTURA_GENERATOR")
	@Column(name="ide_fce_aquicultura")
	private Integer ideFceAquicultura;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_documento_obrigatorio_requerimento", referencedColumnName = "ide_documento_obrigatorio_requerimento")
	private DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce", referencedColumnName = "ide_fce", nullable = false)
	private Fce ideFce;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_lancamento_efluente", referencedColumnName = "ide_fce_lancamento_efluente")
	private FceLancamentoEfluente ideFceLancamentoEfluente;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_periodo_derivacao", referencedColumnName = "ide_tipo_periodo_derivacao" )
	private TipoPeriodoDerivacao ideTipoPeriodoDerivacao;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipo_aquicultura", referencedColumnName="ide_tipo_aquicultura")
	private TipoAquicultura ideTipoAquicultura;

	@Column(name="num_area_total_ocupada")
	private BigDecimal numAreaTotalOcupada;

	@Column(name="num_producao_cultivo_anual")
	private BigDecimal numProducaoCultivoAnual;

	@Column(name="num_tempo_captacao")
	private Integer numTempoCaptacao;

	@Column(name="num_vazao_requerida")
	private BigDecimal numVazaoRequerida;

	@Column(name="num_viveiro")
	private BigDecimal numViveiro;

	@Column(name="num_volume_viveiro")
	private BigDecimal numVolumeViveiro;
	
	@Column(name="profundidade_media")
	private BigDecimal profundidadeMedia;
	
	@Column(name="taxa_renovacao_diaria_agua")
	private BigDecimal taxaRenovacaoDiariaAgua;
	
	@Column(name="num_dias_renovacao_agua")
	private Integer numDiasRenovacaoAgua;
	
	@Column(name="volume_recirculado_diariamente_metros")
	private BigDecimal volumeRecirculadoDiariamenteMetros;

	@Column(name="volume_recirculado_diariamente_porcentagem")
	private BigDecimal volumeRecirculadoDiariamentePorcentagem;
	
	@Column(name="esvaziamento_viveiros")
	private Integer esvaziamentoViveiros; 
	
	@Column(name="area_total_espelho_agua")
	private BigDecimal areaTotalEspelhoAgua;
	
	@Column(name="volume_total_armazenado")
	private BigDecimal volumeTotalArmazenado;
	
	@Transient
	private FileUploadEvent fileUploadEvent;

	@Transient
	private List<String> uploadCaminhoArquivo;

	@Transient
	private List<AquiculturaTipoAtividade> listaAquiculturaTipoAtividade;

	public FceAquicultura() {
	}

	public FceAquicultura(Fce fce, TipoAquiculturaEnum tipoAquiculturaEnum) {
		this.ideFce = fce;
		this.ideTipoAquicultura = new TipoAquicultura(tipoAquiculturaEnum);
	}

	public FceAquicultura(Fce fce, FceLancamentoEfluente fceLancamentoEfluente, TipoAquiculturaEnum tipoAquiculturaEnum) {
		this.ideFce = fce;
		this.ideFceLancamentoEfluente = fceLancamentoEfluente;
		this.ideTipoAquicultura = new TipoAquicultura(tipoAquiculturaEnum.getId());
		this.ideTipoPeriodoDerivacao = new TipoPeriodoDerivacao();
	}

	public Integer getIdeFceAquicultura() {
		return this.ideFceAquicultura;
	}

	public void setIdeFceAquicultura(Integer ideFceAquicultura) {
		this.ideFceAquicultura = ideFceAquicultura;
	}

	public DocumentoObrigatorioRequerimento getIdeDocumentoObrigatorioRequerimento() {
		return this.ideDocumentoObrigatorioRequerimento;
	}

	public void setIdeDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento) {
		this.ideDocumentoObrigatorioRequerimento = ideDocumentoObrigatorioRequerimento;
	}

	public Fce getIdeFce() {
		return this.ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public FceLancamentoEfluente getIdeFceLancamentoEfluente() {
		return this.ideFceLancamentoEfluente;
	}

	public void setIdeFceLancamentoEfluente(FceLancamentoEfluente ideFceLancamentoEfluente) {
		this.ideFceLancamentoEfluente = ideFceLancamentoEfluente;
	}

	public TipoPeriodoDerivacao getIdeTipoPeriodoDerivacao() {
		return this.ideTipoPeriodoDerivacao;
	}

	public void setIdeTipoPeriodoDerivacao(TipoPeriodoDerivacao ideTipoPeriodoDerivacao) {
		// Caso seja da Aba Viveiro Escavado - Lançamento
		if(!Util.isNull(this.ideFceLancamentoEfluente)) {
			ideFceLancamentoEfluente.setIdeTipoPeriodoDerivacao(ideTipoPeriodoDerivacao);
		}
		this.ideTipoPeriodoDerivacao = ideTipoPeriodoDerivacao;
	}

	public BigDecimal getNumAreaTotalOcupada() {
		return this.numAreaTotalOcupada;
	}

	public void setNumAreaTotalOcupada(BigDecimal numAreaTotalOcupada) {
		this.numAreaTotalOcupada = numAreaTotalOcupada;
	}

	public BigDecimal getNumProducaoCultivoAnual() {
		return this.numProducaoCultivoAnual;
	}

	public void setNumProducaoCultivoAnual(BigDecimal numProducaoCultivoAnual) {
		this.numProducaoCultivoAnual = numProducaoCultivoAnual;
	}

	public Integer getNumTempoCaptacao() {
		return this.numTempoCaptacao;
	}

	public void setNumTempoCaptacao(Integer numTempoCaptacao) {
		if(!Util.isNull(this.ideFceLancamentoEfluente)) {
			this.ideFceLancamentoEfluente.setNumTempoLancamento(numTempoCaptacao);
		}
		this.numTempoCaptacao = numTempoCaptacao;
	}

	public BigDecimal getNumVazaoRequerida() {
		return this.numVazaoRequerida;
	}

	public void setNumVazaoRequerida(BigDecimal numVazaoRequerida) {
		this.numVazaoRequerida = numVazaoRequerida;
	}

	public BigDecimal getNumViveiro() {
		return this.numViveiro;
	}

	public void setNumViveiro(BigDecimal numViveiro) {
		this.numViveiro = numViveiro;
	}

	public BigDecimal getNumVolumeViveiro() {
		return this.numVolumeViveiro;
	}

	public void setNumVolumeViveiro(BigDecimal numVolumeViveiro) {
		this.numVolumeViveiro = numVolumeViveiro;
	}

	public TipoAquicultura getIdeTipoAquicultura() {
		return this.ideTipoAquicultura;
	}

	public void setIdeTipoAquicultura(TipoAquicultura tipoAquicultura) {
		this.ideTipoAquicultura = tipoAquicultura;
	}

	public FileUploadEvent getFileUploadEvent() {
		return fileUploadEvent;
	}

	public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
		this.fileUploadEvent = fileUploadEvent;
	}

	public List<String> getUploadCaminhoArquivo() {
		return uploadCaminhoArquivo;
	}

	public void setUploadCaminhoArquivo(List<String> uploadCaminhoArquivo) {
		this.uploadCaminhoArquivo = uploadCaminhoArquivo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceAquicultura == null) ? 0 : ideFceAquicultura
						.hashCode());
		result = prime
				* result
				+ ((ideTipoAquicultura == null) ? 0 : ideTipoAquicultura
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FceAquicultura other = (FceAquicultura) obj;
		if (ideFceAquicultura == null) {
			if (other.ideFceAquicultura != null) {
				return false;
			}
		} else if (!ideFceAquicultura.equals(other.ideFceAquicultura)) {
			return false;
		}
		if (ideTipoAquicultura == null) {
			if (other.ideTipoAquicultura != null) {
				return false;
			}
		} else if (!ideTipoAquicultura.equals(other.ideTipoAquicultura)) {
			return false;
		}
		return true;
	}

	public List<AquiculturaTipoAtividade> getListaAquiculturaTipoAtividade() {
		return listaAquiculturaTipoAtividade;
	}

	public void setListaAquiculturaTipoAtividade(
			List<AquiculturaTipoAtividade> listaAquiculturaTipoAtividade) {
		this.listaAquiculturaTipoAtividade = listaAquiculturaTipoAtividade;
	}

	public BigDecimal getProfundidadeMedia() {
		return profundidadeMedia;
	}

	public void setProfundidadeMedia(BigDecimal profundidadeMedia) {
		this.profundidadeMedia = profundidadeMedia;
	}

	public BigDecimal getTaxaRenovacaoDiariaAgua() {
		return taxaRenovacaoDiariaAgua;
	}

	public void setTaxaRenovacaoDiariaAgua(BigDecimal taxaRenovacaoDiariaAgua) {
		this.taxaRenovacaoDiariaAgua = taxaRenovacaoDiariaAgua;
	}

	public Integer getNumDiasRenovacaoAgua() {
		return numDiasRenovacaoAgua;
	}

	public void setNumDiasRenovacaoAgua(Integer numDiasRenovacaoAgua) {
		this.numDiasRenovacaoAgua = numDiasRenovacaoAgua;
	}

	public BigDecimal getVolumeRecirculadoDiariamenteMetros() {
		return volumeRecirculadoDiariamenteMetros;
	}

	public void setVolumeRecirculadoDiariamenteMetros(
			BigDecimal volumeRecirculadoDiariamenteMetros) {
		this.volumeRecirculadoDiariamenteMetros = volumeRecirculadoDiariamenteMetros;
	}

	public BigDecimal getVolumeRecirculadoDiariamentePorcentagem() {
		return volumeRecirculadoDiariamentePorcentagem;
	}

	public void setVolumeRecirculadoDiariamentePorcentagem(
			BigDecimal volumeRecirculadoDiariamentePorcentagem) {
		this.volumeRecirculadoDiariamentePorcentagem = volumeRecirculadoDiariamentePorcentagem;
	}

	public Integer getEsvaziamentoViveiros() {
		return esvaziamentoViveiros;
	}

	public void setEsvaziamentoViveiros(Integer esvaziamentoViveiros) {
		this.esvaziamentoViveiros = esvaziamentoViveiros;
	}

	public BigDecimal getAreaTotalEspelhoAgua() {
		return areaTotalEspelhoAgua;
	}

	public void setAreaTotalEspelhoAgua(BigDecimal areaTotalEspelhoAgua) {
		this.areaTotalEspelhoAgua = areaTotalEspelhoAgua;
	}

	public BigDecimal getVolumeTotalArmazenado() {
		return volumeTotalArmazenado;
	}

	public void setVolumeTotalArmazenado(BigDecimal volumeTotalArmazenado) {
		this.volumeTotalArmazenado = volumeTotalArmazenado;
	}
	
	public FceAquicultura clone() throws CloneNotSupportedException{
		return (FceAquicultura) super.clone();
	}
}