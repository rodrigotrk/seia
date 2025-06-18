package br.gov.ba.seia.entity;

import static br.gov.ba.seia.util.fce.FceUtil.isExibirParaAnaliseTecnica;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.enumerator.StatusReservaAguaEnum;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="fce_outorga_localizacao_geografica")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name  = "FceOutorgaLocalizacaoGeografica.deleteByideOutorgaLocGeografica",
			query = "DELETE FROM FceOutorgaLocalizacaoGeografica fceLocGeo WHERE fceLocGeo.ideOutorgaLocalizacaoGeografica.ideOutorgaLocalizacaoGeografica = :ideOutorgaLocalizacaoGeografica"),
})
public class FceOutorgaLocalizacaoGeografica implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FCE_OUTORGA_LOC_GEO_IDE_FCE_OUTORGA_LOCAL_GEO_SEQ", sequenceName="FCE_OUTORGA_LOC_GEO_IDE_FCE_OUTORGA_LOCAL_GEO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FCE_OUTORGA_LOC_GEO_IDE_FCE_OUTORGA_LOCAL_GEO_SEQ")
	@Column(name="ide_fce_outorga_localizacao_geografica")
	private Integer ideFceOutorgaLocalizacaoGeografica;

	@NotNull
	@JoinColumn(name = "ide_fce", referencedColumnName = "ide_fce", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private Fce ideFce;

	@JoinColumn(name= "ide_outorga_localizacao_geografica", referencedColumnName = "ide_outorga_localizacao_geografica", nullable = true)
	@OneToOne(fetch = FetchType.LAZY)
	private OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica;

	@Column(name="nom_rio")
	private String nomRio;

	@Column(name="num_tempo_captacao")
	private Integer numTempoCaptacao;

	//bi-directional many-to-one association to LocalCaptacao
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_local_captacao")
	private LocalCaptacao localCaptacao;

	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_localizacao_geografica")
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@Column(name="num_area_irrigada")
	private BigDecimal numAreaIrrigada;

	@Column(name="num_area_irrigada_captacao")
	private BigDecimal numAreaIrrigadaCaptacao;

	@Column(name="num_vazao")
	private BigDecimal numVazao;

	@Column(name="num_vazao_final")
	private BigDecimal numVazaoFinal;
	
	@Column(name="num_area_pulverizada")
	private BigDecimal numAreaPulverizada;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tipologia")
	private Tipologia ideTipologia;

	@Transient
	private boolean confirmado;

	@Transient
	private Double vazaoHora;

	@Transient
	private String maskNumVazao;

	@Transient
	private boolean isRioPreenchido;

	@Transient
	private boolean edicao;
	
	// FCE - Análise Técnica
	@Transient
	private List<FceOutorgaLocalizacaoGeograficaFinalidade> listaFinalidade;

	// Reserva de Água
	@OneToMany(mappedBy="ideFceOutorgaLocalizacaoGeografica", fetch=FetchType.LAZY)
	private List<ReservaAgua> listaReservaAgua;
	
	@OneToOne(mappedBy = "ideFceOutorgaLocalizacaoGeografica")
	private FceCaptacaoSuperficial ideFceCaptacaoSuperficial; 
	
	@OneToOne(mappedBy = "ideFceOutorgaLocalizacaoGeografica")
	private FceCaptacaoSubterranea ideFceCaptacaoSubterranea; 
	
	@OneToOne(mappedBy = "ideFceOutorgaLocalizacaoGeografica")
	private FceLancamentoEfluente ideFceLancamentoEfluente; 
	
	public FceOutorgaLocalizacaoGeografica() {
		
	}
	public FceOutorgaLocalizacaoGeografica(Integer ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}
	
	public FceOutorgaLocalizacaoGeografica(Fce fce) {
		this.ideFce = fce;
	}

	public FceOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

	public FceOutorgaLocalizacaoGeografica(Fce fce, OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideFce = fce;
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}
	
	public FceOutorgaLocalizacaoGeografica(Fce fce, LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideFce = fce;
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}
	
	/**
	 * Construtor utilizado para Dulpicar o {@link Fce} durante a {@link AnaliseTecnica}.
	 * @param ideOutorgaLocalizacaoGeografica
	 * @param fce
	 */
	public FceOutorgaLocalizacaoGeografica(Fce fce, OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica, LocalizacaoGeografica localizacaoGeografica, Tipologia tipologia, String nomeRio, LocalCaptacao localCaptacao, int numTempoCaptacao) {
		this.ideFce = fce;
		this.ideTipologia = tipologia;
		this.ideLocalizacaoGeografica = localizacaoGeografica;
		this.nomRio = nomeRio;
		this.localCaptacao = localCaptacao;
		this.numTempoCaptacao = numTempoCaptacao;
		this.confirmado = true;
		if(!Util.isNull(ideOutorgaLocalizacaoGeografica)){
			this.numAreaIrrigada = ideOutorgaLocalizacaoGeografica.getNumAreaIrrigada();
			this.numAreaIrrigadaCaptacao = ideOutorgaLocalizacaoGeografica.getNumAreaIrrigadaCaptacao();
			this.numVazao = ideOutorgaLocalizacaoGeografica.getNumVazao();
			this.numVazaoFinal = ideOutorgaLocalizacaoGeografica.getNumVazaoFinal();
			this.numAreaPulverizada = ideOutorgaLocalizacaoGeografica.getNumAreaPulverizada();
			this.ideLocalizacaoGeografica.setIdeSistemaCoordenada(ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada());
		}
	}

	public Integer getIdeFceOutorgaLocalizacaoGeografica() {
		return ideFceOutorgaLocalizacaoGeografica;
	}

	public void setIdeFceOutorgaLocalizacaoGeografica(Integer ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}

	public String getNomRio() {
		return this.nomRio;
	}

	public void setNomRio(String nomRio) {
		this.nomRio = nomRio;
	}

	public Integer getNumTempoCaptacao() {
		return this.numTempoCaptacao;
	}

	public void setNumTempoCaptacao(Integer numTempoCaptacao) {
		this.numTempoCaptacao = numTempoCaptacao;
	}

	public LocalCaptacao getLocalCaptacao() {
		return this.localCaptacao;
	}

	public void setLocalCaptacao(LocalCaptacao localCaptacao) {
		this.localCaptacao = localCaptacao;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public OutorgaLocalizacaoGeografica getIdeOutorgaLocalizacaoGeografica() {
		return ideOutorgaLocalizacaoGeografica;
	}

	public void setIdeOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}

	public Fce getIdeFce() {
		return ideFce;
	}

	public void setIdeFce(Fce ideFce) {
		this.ideFce = ideFce;
	}

	public Double getVazaoHora() {
		return vazaoHora;
	}

	public void setVazaoHora(Double vazaoHora) {
		this.vazaoHora = vazaoHora;
	}

	public String getMaskNumVazao() {
		return maskNumVazao;
	}

	public void setMaskNumVazao(String maskNumVazao) {
		this.maskNumVazao = maskNumVazao;
	}

	public boolean isRioPreenchido() {
		return isRioPreenchido;
	}

	public void setRioPreenchido(boolean isRioPreenchido) {
		this.isRioPreenchido = isRioPreenchido;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
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
				+ ((ideFceOutorgaLocalizacaoGeografica == null) ? 0
						: ideFceOutorgaLocalizacaoGeografica.hashCode());
		result = prime
				* result
				+ ((ideOutorgaLocalizacaoGeografica == null) ? 0
						: ideOutorgaLocalizacaoGeografica.hashCode());
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
		FceOutorgaLocalizacaoGeografica other = (FceOutorgaLocalizacaoGeografica) obj;
		if (ideFceOutorgaLocalizacaoGeografica == null) {
			if (other.ideFceOutorgaLocalizacaoGeografica != null) {
				return false;
			}
		} else if (!ideFceOutorgaLocalizacaoGeografica
				.equals(other.ideFceOutorgaLocalizacaoGeografica)) {
			return false;
		}
		if (ideOutorgaLocalizacaoGeografica == null) {
			if (other.ideOutorgaLocalizacaoGeografica != null) {
				return false;
			}
		} else if (!ideOutorgaLocalizacaoGeografica
				.equals(other.ideOutorgaLocalizacaoGeografica)) {
			return false;
		}
		return true;
	}

	public String getLatitude() {
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideLocalizacaoGeografica.getLatitudeInicial();
		} 
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getLatitudeInicial();
		}
	}

	public String getLongitude() {
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideLocalizacaoGeografica.getLongitudeInicial();
		}
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getLongitudeInicial();
		}
	}

	public String getSistemaCoordenada() {
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideLocalizacaoGeografica.getNomeSistemaCoordenadas();
		} 
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getNomeSistemaCoordenadas();
		}
	}
	
	public String getBacia(){
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideLocalizacaoGeografica.getBaciaHidrografica();
		} 
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getBaciaHidrografica();
		}
	}

	public String getSubBacia(){
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideLocalizacaoGeografica.getSubBacia();
		} 
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getSubBacia();
		}
	}
	
	public String getRpga(){
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideLocalizacaoGeografica.getRpga();
		} 
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getRpga();
		}
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public BigDecimal getNumAreaIrrigada() {
		return numAreaIrrigada;
	}

	public void setNumAreaIrrigada(BigDecimal numAreaIrrigada) {
		this.numAreaIrrigada = numAreaIrrigada;
	}

	public BigDecimal getNumAreaIrrigadaCaptacao() {
		return numAreaIrrigadaCaptacao;
	}

	public void setNumAreaIrrigadaCaptacao(BigDecimal numAreaIrrigadaCaptacao) {
		this.numAreaIrrigadaCaptacao = numAreaIrrigadaCaptacao;
	}

	public BigDecimal getNumVazao() {
		return numVazao;
	}

	public void setNumVazao(BigDecimal numVazao) {
		this.numVazao = numVazao;
	}

	public BigDecimal getNumVazaoFinal() {
		return numVazaoFinal;
	}

	public void setNumVazaoFinal(BigDecimal numVazaoFinal) {
		this.numVazaoFinal = numVazaoFinal;
	}

	public BigDecimal getNumAreaPulverizada() {
		return numAreaPulverizada;
	}

	public void setNumAreaPulverizada(BigDecimal numAreaPulverizada) {
		this.numAreaPulverizada = numAreaPulverizada;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public List<FceOutorgaLocalizacaoGeograficaFinalidade> getListaFinalidade() {
		return listaFinalidade;
	}

	public void setListaFinalidade(List<FceOutorgaLocalizacaoGeograficaFinalidade> listaFinalidade) {
		this.listaFinalidade = listaFinalidade;
	}

	public boolean isAguaReservada() {
		if(!Util.isLazyInitExcepOuNull(listaReservaAgua) && !Util.isNullOuVazio(listaReservaAgua)){
			ReservaAgua ideReservaAgua = listaReservaAgua.get(listaReservaAgua.size() - 1);
			return !Util.isNull(ideReservaAgua.getIdeStatusReservaAgua()) && ideReservaAgua.getIdeStatusReservaAgua().getIdeStatusReservaAgua().equals(StatusReservaAguaEnum.RESERVADO.getIde());
		}
		return false;
	}

	public List<ReservaAgua> getListaReservaAgua() {
		return listaReservaAgua;
	}

	public void setListaReservaAgua(List<ReservaAgua> listaReservaAgua) {
		this.listaReservaAgua = listaReservaAgua;
	}

	public FceCaptacaoSuperficial getIdeFceCaptacaoSuperficial() {
		return ideFceCaptacaoSuperficial;
	}

	public void setIdeFceCaptacaoSuperficial(FceCaptacaoSuperficial ideFceCaptacaoSuperficial) {
		this.ideFceCaptacaoSuperficial = ideFceCaptacaoSuperficial;
	}

	public FceCaptacaoSubterranea getIdeFceCaptacaoSubterranea() {
		return ideFceCaptacaoSubterranea;
	}

	public void setIdeFceCaptacaoSubterranea(FceCaptacaoSubterranea ideFceCaptacaoSubterranea) {
		this.ideFceCaptacaoSubterranea = ideFceCaptacaoSubterranea;
	}

	public FceLancamentoEfluente getIdeFceLancamentoEfluente() {
		return ideFceLancamentoEfluente;
	}
	
	public void setIdeFceLancamentoEfluente(FceLancamentoEfluente ideFceLancamentoEfluente) {
		this.ideFceLancamentoEfluente = ideFceLancamentoEfluente;
	}
	
	@Override
    public FceOutorgaLocalizacaoGeografica clone() throws CloneNotSupportedException {
         return (FceOutorgaLocalizacaoGeografica)super.clone();
    }
}