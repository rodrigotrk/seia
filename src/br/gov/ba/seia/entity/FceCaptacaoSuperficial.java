package br.gov.ba.seia.entity;

import java.math.BigDecimal;

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

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name="fce_captacao_superficial")
public class FceCaptacaoSuperficial extends AbstractEntity {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "FCE_CAPTACAO_SUPERFICIAL_IDE_FCE_CAP_SUP_SEQ", sequenceName = "FCE_CAPTACAO_SUPERFICIAL_IDE_FCE_CAP_SUP_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FCE_CAPTACAO_SUPERFICIAL_IDE_FCE_CAP_SUP_SEQ")
	@Column(name = "ide_fce_captacao_superficial", nullable = false)
	private Integer ideFceCaptacaoSuperficial;
	
	@Column(name = "num_valor_maximo_acumulado")
	private BigDecimal numValorMaximoAcumulado;
	
	@Column(name = "num_vazao_regularizada")
	private BigDecimal numVazaoRegularizada;
	
	@Column(name = "num_vazao_captacao")
	private BigDecimal numVazaoCaptacao;
	
	@JoinColumn(name = "ide_fce_outorga_localizacao_geografica", referencedColumnName = "ide_fce_outorga_localizacao_geografica", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica;
	
	@JoinColumn(name = "ide_documento_obrigatorio_requerimento", referencedColumnName = "ide_documento_obrigatorio_requerimento", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento;
	
	@JoinColumn(name = "ide_caracteristica_captacao", referencedColumnName = "ide_caracteristica_captacao", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaracteristicaCaptacao caracteristicaCaptacao;
	
	@JoinColumn(name = "ide_caracteristica_sistema_captacao", referencedColumnName = "ide_caracteristica_sistema_captacao")
	@ManyToOne(fetch = FetchType.LAZY)
	private CaracteristicaSistemaCaptacao caracteristicaSistemaCaptacao;
	
	@JoinColumn(name = "ide_dominio_barramento", referencedColumnName = "ide_dominio_barramento")
	@ManyToOne(fetch = FetchType.LAZY)
	private DominioBarramento ideDominioBarramento;
	
	@JoinColumn(name = "ide_tipo_valor_volume")
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoValor tipoValorVolume;
	
	@JoinColumn(name = "ide_tipo_valor_vazao")
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoValor tipoValorVazao;
	
	@Transient
	private boolean edicao;
	
	public FceCaptacaoSuperficial() {}
	
	public FceCaptacaoSuperficial(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}
	
	public FceCaptacaoSuperficial(CaracteristicaSistemaCaptacao caracteristicaSistemaCaptacao) {
		this.caracteristicaSistemaCaptacao = caracteristicaSistemaCaptacao;
	}
	
	public FceCaptacaoSuperficial(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica, 
			DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento,
			CaracteristicaCaptacao caracteristicaCaptacao,
			CaracteristicaSistemaCaptacao caracteristicaSistemaCaptacao) {
		
		super();
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
		this.ideDocumentoObrigatorioRequerimento = ideDocumentoObrigatorioRequerimento;
		this.caracteristicaCaptacao = caracteristicaCaptacao;
		this.caracteristicaSistemaCaptacao = caracteristicaSistemaCaptacao;
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/
	
	public Integer getIdeFceCaptacaoSuperficial() {
		return this.ideFceCaptacaoSuperficial;
	}
	
	public void setIdeFceCaptacaoSuperficial(Integer ideFceCaptacaoSuperficial) {
		this.ideFceCaptacaoSuperficial = ideFceCaptacaoSuperficial;
	}
	
	public BigDecimal getNumValorMaximoAcumulado() {
		return this.numValorMaximoAcumulado;
	}
	
	public void setNumValorMaximoAcumulado(BigDecimal numValorMaximoAcumulado) {
		this.numValorMaximoAcumulado = numValorMaximoAcumulado;
	}
	
	public BigDecimal getNumVazaoRegularizada() {
		return this.numVazaoRegularizada;
	}
	
	public void setNumVazaoRegularizada(BigDecimal numVazaoRegularizada) {
		this.numVazaoRegularizada = numVazaoRegularizada;
	}
	
	public CaracteristicaSistemaCaptacao getCaracteristicaSistemaCaptacao() {
		return this.caracteristicaSistemaCaptacao;
	}
	
	public void setCaracteristicaSistemaCaptacao(CaracteristicaSistemaCaptacao caracteristicaSistemaCaptacao) {
		this.caracteristicaSistemaCaptacao = caracteristicaSistemaCaptacao;
	}
	
	public TipoValor getTipoValorVolume() {
		return tipoValorVolume;
	}
	
	public void setTipoValorVolume(TipoValor tipoValorVolume) {
		this.tipoValorVolume = tipoValorVolume;
	}
	
	public TipoValor getTipoValorVazao() {
		return tipoValorVazao;
	}
	
	public void setTipoValorVazao(TipoValor tipoValorVazao) {
		this.tipoValorVazao = tipoValorVazao;
	}
	
	public FceOutorgaLocalizacaoGeografica getIdeFceOutorgaLocalizacaoGeografica() {
		return ideFceOutorgaLocalizacaoGeografica;
	}
	
	public void setIdeFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}
	
	public DominioBarramento getIdeDominioBarramento() {
		return ideDominioBarramento;
	}
	
	public void setIdeDominioBarramento(DominioBarramento ideDominioBarramento) {
		this.ideDominioBarramento = ideDominioBarramento;
	}
	
	public DocumentoObrigatorioRequerimento getIdeDocumentoObrigatorioRequerimento() {
		return ideDocumentoObrigatorioRequerimento;
	}
	
	public void setIdeDocumentoObrigatorioRequerimento(DocumentoObrigatorioRequerimento ideDocumentoObrigatorioRequerimento) {
		this.ideDocumentoObrigatorioRequerimento = ideDocumentoObrigatorioRequerimento;
	}
	
	public CaracteristicaCaptacao getCaracteristicaCaptacao() {
		return caracteristicaCaptacao;
	}
	
	public void setCaracteristicaCaptacao(CaracteristicaCaptacao caracteristicaCaptacao) {
		this.caracteristicaCaptacao = caracteristicaCaptacao;
	}
	
	public boolean isEdicao() {
		return edicao;
	}
	
	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}
	
	public BigDecimal getNumVazaoCaptacao() {
		return numVazaoCaptacao;
	}
	
	public void setNumVazaoCaptacao(BigDecimal numVazaoCaptacao) {
		this.numVazaoCaptacao = numVazaoCaptacao;
	}
}