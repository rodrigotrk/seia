package br.gov.ba.seia.entity;

import static br.gov.ba.seia.util.fce.FceUtil.isExibirParaAnaliseTecnica;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoBarragemEnum;

/**
 * 		04/10/13
 * @author eduardo.fernandes
 */
@Entity
@Table(name="fce_intervencao_barragem")
public class FceIntervencaoBarragem extends AbstractEntity implements Cloneable {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_intervencao_barragem_ide_fce_intervencao_barragem")
	@SequenceGenerator(name = "fce_intervencao_barragem_ide_fce_intervencao_barragem", sequenceName = "fce_intervencao_barragem_ide_fce_intervencao_barragem_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_fce_intervencao_barragem")
	private Integer ideFceIntervencaoBarragem;

	@Column(name="dtc_fim_vazao_regularizada")
	private Integer dtcFimVazaoRegularizada;

	@Column(name="dtc_ini_vazao_regularizada")
	private Integer dtcIniVazaoRegularizada;

	@Column(name="ind_descarga_fundo")
	private Boolean indDescargaFundo;

	@Column(name="num_altura_maxima")
	private Double numAlturaMaxima;

	@Column(name="num_bacia_hidrografica")
	private Double numBaciaHidrografica;

	@Column(name="num_comprimento_total")
	private Double numComprimentoTotal;

	@Column(name="num_garantia_atendimento_vazao")
	private Double numGarantiaAtendimentoVazao;

	@Column(name="num_largura_base")
	private Double numLarguraBase;

	@Column(name="num_largura_coroamento")
	private Double numLarguraCoroamento;

	@Column(name="num_nivel_agua_maxima")
	private Double numNivelAguaMaxima;

	@Column(name="num_nivel_agua_maximorum")
	private Double numNivelAguaMaximorum;

	@Column(name="num_nivel_agua_minima")
	private Double numNivelAguaMinima;

	@Column(name="num_vazao_regularizada")
	private Double numVazaoRegularizada;

	@Column(name="num_volume_maximo_acumulado")
	private Double numVolumeMaximoAcumulado;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	private Fce ideFce;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_outorga_localizacao_geografica", referencedColumnName = "ide_outorga_localizacao_geografica")
	private OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_documento_obrigatorio_requerimento", referencedColumnName = "ide_documento_obrigatorio_requerimento")
	private DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_outorga_localizacao_geografica")
	private FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_barragem", referencedColumnName = "ide_tipo_barragem", nullable = true)
	private TipoBarragem ideTipoBarragem;
	
	@Transient
	private boolean edicao;
	
	@Transient
	private boolean confirmar;

	@JoinColumn(name = "ide_fce_barragem", referencedColumnName = "ide_fce_barragem")
	@ManyToOne(fetch = FetchType.LAZY/*, cascade = CascadeType.ALL*/)//
	private FceBarragem fceBarragem;
	
	public FceIntervencaoBarragem() {
		
	}
	
	public FceIntervencaoBarragem(Fce fce) {
		this.ideFce = fce;
	}

	public FceIntervencaoBarragem(Fce fce, OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideFce = fce;
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}
	public FceIntervencaoBarragem(Fce fce, FceBarragem fceBarragem, OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideFce = fce;
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
		this.fceBarragem = fceBarragem;
	}

	public Integer getIdeFceIntervencaoBarragem() {
		return this.ideFceIntervencaoBarragem;
	}

	public void setIdeFceIntervencaoBarragem(Integer ideFceIntervencaoBarragem) {
		this.ideFceIntervencaoBarragem = ideFceIntervencaoBarragem;
	}

	public Integer getDtcFimVazaoRegularizada() {
		return this.dtcFimVazaoRegularizada;
	}

	public void setDtcFimVazaoRegularizada(Integer dtcFimVazaoRegularizada) {
		this.dtcFimVazaoRegularizada = dtcFimVazaoRegularizada;
	}

	public Integer getDtcIniVazaoRegularizada() {
		return this.dtcIniVazaoRegularizada;
	}

	public void setDtcIniVazaoRegularizada(Integer dtcIniVazaoRegularizada) {
		this.dtcIniVazaoRegularizada = dtcIniVazaoRegularizada;
	}

	public Boolean getIndDescargaFundo() {
		return this.indDescargaFundo;
	}

	public void setIndDescargaFundo(Boolean indDescargaFundo) {
		this.indDescargaFundo = indDescargaFundo;
	}

	public Double getNumAlturaMaxima() {
		return numAlturaMaxima;
	}

	public void setNumAlturaMaxima(Double numAlturaMaxima) {
		this.numAlturaMaxima = numAlturaMaxima;
	}

	public Double getNumBaciaHidrografica() {
		return numBaciaHidrografica;
	}

	public void setNumBaciaHidrografica(Double numBaciaHidrografica) {
		this.numBaciaHidrografica = numBaciaHidrografica;
	}

	public Double getNumComprimentoTotal() {
		return numComprimentoTotal;
	}

	public void setNumComprimentoTotal(Double numComprimentoTotal) {
		this.numComprimentoTotal = numComprimentoTotal;
	}

	public Double getNumGarantiaAtendimentoVazao() {
		return numGarantiaAtendimentoVazao;
	}

	public void setNumGarantiaAtendimentoVazao(Double numGarantiaAtendimentoVazao) {
		this.numGarantiaAtendimentoVazao = numGarantiaAtendimentoVazao;
	}

	public Double getNumLarguraBase() {
		return numLarguraBase;
	}

	public void setNumLarguraBase(Double numLarguraBase) {
		this.numLarguraBase = numLarguraBase;
	}

	public Double getNumLarguraCoroamento() {
		return numLarguraCoroamento;
	}

	public void setNumLarguraCoroamento(Double numLarguraCoroamento) {
		this.numLarguraCoroamento = numLarguraCoroamento;
	}

	public Double getNumNivelAguaMaxima() {
		return numNivelAguaMaxima;
	}

	public void setNumNivelAguaMaxima(Double numNivelAguaMaxima) {
		this.numNivelAguaMaxima = numNivelAguaMaxima;
	}

	public Double getNumNivelAguaMaximorum() {
		return numNivelAguaMaximorum;
	}

	public void setNumNivelAguaMaximorum(Double numNivelAguaMaximorum) {
		this.numNivelAguaMaximorum = numNivelAguaMaximorum;
	}

	public Double getNumNivelAguaMinima() {
		return numNivelAguaMinima;
	}

	public void setNumNivelAguaMinima(Double numNivelAguaMinima) {
		this.numNivelAguaMinima = numNivelAguaMinima;
	}

	public Double getNumVazaoRegularizada() {
		return numVazaoRegularizada;
	}

	public void setNumVazaoRegularizada(Double numVazaoRegularizada) {
		this.numVazaoRegularizada = numVazaoRegularizada;
	}

	public Double getNumVolumeMaximoAcumulado() {
		return numVolumeMaximoAcumulado;
	}

	public void setNumVolumeMaximoAcumulado(Double numVolumeMaximoAcumulado) {
		this.numVolumeMaximoAcumulado = numVolumeMaximoAcumulado;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public OutorgaLocalizacaoGeografica getIdeOutorgaLocalizacaoGeografica() {
		return ideOutorgaLocalizacaoGeografica;
	}

	public void setIdeOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

	public DocumentoObrigatorioRequerimento getIdeDocumentoObrigatorioRequerimento() {
		return ideDocumentoObrigatorioRequerimento;
	}

	public void setIdeDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento) {
		this.ideDocumentoObrigatorioRequerimento = ideDocumentoObrigatorioRequerimento;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

	public FceOutorgaLocalizacaoGeografica getIdeFceOutorgaLocalizacaoGeografica() {
		return ideFceOutorgaLocalizacaoGeografica;
	}

	public void setIdeFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFce == null) ? 0 : ideFce.hashCode());
		result = prime
				* result
				+ ((ideFceIntervencaoBarragem == null) ? 0
						: ideFceIntervencaoBarragem.hashCode());
		result = prime
				* result
				+ ((ideFceOutorgaLocalizacaoGeografica == null) ? 0
						: ideFceOutorgaLocalizacaoGeografica.hashCode());
		result = prime
				* result
				+ ((ideOutorgaLocalizacaoGeografica == null) ? 0
						: ideOutorgaLocalizacaoGeografica.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FceIntervencaoBarragem other = (FceIntervencaoBarragem) obj;
		if (ideFce == null) {
			if (other.ideFce != null)
				return false;
		} else if (!ideFce.equals(other.ideFce))
			return false;
		if (ideFceIntervencaoBarragem == null) {
			if (other.ideFceIntervencaoBarragem != null)
				return false;
		} else if (!ideFceIntervencaoBarragem
				.equals(other.ideFceIntervencaoBarragem))
			return false;
		if (ideFceOutorgaLocalizacaoGeografica == null) {
			if (other.ideFceOutorgaLocalizacaoGeografica != null)
				return false;
		} else if (!ideFceOutorgaLocalizacaoGeografica
				.equals(other.ideFceOutorgaLocalizacaoGeografica))
			return false;
		if (ideOutorgaLocalizacaoGeografica == null) {
			if (other.ideOutorgaLocalizacaoGeografica != null)
				return false;
		} else if (!ideOutorgaLocalizacaoGeografica
				.equals(other.ideOutorgaLocalizacaoGeografica))
			return false;
		return true;
	}
	
	public String getLatitude() {
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideFceOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getLatitudeInicial();
		} 
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getLatitudeInicial();
		}
	}

	public String getLongitude() {
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideFceOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getLongitudeInicial();
		}
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getLongitudeInicial();
		}
	}

	public String getSistemaCoordenada() {
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideFceOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getNomeSistemaCoordenadas();
		} 
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getNomeSistemaCoordenadas();
		}
	}
	
	public boolean isTipoBarragemRegularizacao(){
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideTipoBarragem.getIdeTipoBarragem() == TipoBarragemEnum.REGULARIZACAO.getId();
		} else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeTipoBarragem().getIdeTipoBarragem() == TipoBarragemEnum.REGULARIZACAO.getId();
		}
	}

	public TipoBarragem getIdeTipoBarragem() {
		return ideTipoBarragem;
	}

	public void setIdeTipoBarragem(TipoBarragem ideTipoBarragem) {
		this.ideTipoBarragem = ideTipoBarragem;
	}

	public boolean isConfirmar() {
		return confirmar;
	}

	public void setConfirmar(boolean confirmar) {
		this.confirmar = confirmar;
	}
	/**
	 * @return the fceBarragem
	 */
	public FceBarragem getFceBarragem() {
		return fceBarragem;
	}

	/**
	 * @param fceBarragem the fceBarragem to set
	 */
	public void setFceBarragem(FceBarragem fceBarragem) {
		this.fceBarragem = fceBarragem;
	}
	
	@Override
    public FceIntervencaoBarragem clone() throws CloneNotSupportedException {
         return (FceIntervencaoBarragem)super.clone();
    }
}