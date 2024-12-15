package br.gov.ba.seia.entity;

import static br.gov.ba.seia.util.fce.FceUtil.isExibirParaAnaliseTecnica;

import java.io.Serializable;
import java.math.BigDecimal;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.util.Util;


@Entity
@Table(name="fce_captacao_subterranea")
@NamedQueries({
    @NamedQuery(name = "FceCaptacaoSubterranea.findAll", query = "SELECT fceCapSub FROM FceCaptacaoSubterranea fceCapSub")
})
public class FceCaptacaoSubterranea implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_captacao_subterranea_ide_fce_captacao_subterranea")
	@SequenceGenerator(name = "fce_captacao_subterranea_ide_fce_captacao_subterranea", sequenceName = "fce_captacao_subterranea_ide_fce_captacao_subterranea_seq", allocationSize = 1)
	@Column(name="ide_fce_captacao_subterranea")
	private Integer ideFceCaptacaoSubterranea;
	
	@Column(name="num_tempo_captacao")
	private Integer numTempoCaptacao;
	
	@JoinColumn(name = "ide_tipo_poco", referencedColumnName = "ide_tipo_poco", nullable = false)
	@OneToOne(fetch=FetchType.LAZY)
	private TipoPoco ideTipoPoco;
	
	@JoinColumn(name = "ide_unidade_geologica_aflorante", referencedColumnName = "ide_unidade_geologica_aflorante", nullable = false)
	@OneToOne(fetch=FetchType.LAZY)
	private UnidadeGeologicaAflorante ideUnidadeGeologicaAflorante;
	
	@JoinColumn(name = "ide_aquifero", referencedColumnName = "ide_aquifero", nullable = false)
	@OneToOne(fetch=FetchType.LAZY)
	private Aquifero ideAquifero;
	
	@JoinColumn(name = "ide_tipo_aquifero", referencedColumnName = "ide_tipo_aquifero", nullable = false)
	@OneToOne(fetch=FetchType.LAZY)
	private TipoAquifero ideTipoAquifero;
	                 
	@Column(name="num_profundidade_poco")
	private Double numProfunidadePoco;
	
	@Column(name="num_nivel_estatico")
	private Double numNivelEstatico;
	
	@Column(name="num_nivel_dinamico")
	private Double numNivelDinamico;
	
	@Column(name="num_vazao_teste")
	private Double numVazaoTeste;
	
	@Column(name="num_vazao_especifica")
	private Double numVazaoEspecifica;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce",referencedColumnName = "ide_fce", nullable = false)
	private Fce ideFce;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_outorga_localizacao_geografica", referencedColumnName = "ide_outorga_localizacao_geografica")
	private OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica;
	
	@OneToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_fce_outorga_localizacao_geografica")
	private FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica;
	
	@Transient
	private boolean confirmado;
	
	@Transient
	private Double vazaoHora;
	
	public FceCaptacaoSubterranea(){
		 
	}
	
	public FceCaptacaoSubterranea(OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica, Fce fce) {
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
		this.ideFce = fce;
	}

	public Integer getIdeFceCaptacaoSubterranea(){
		return ideFceCaptacaoSubterranea;
	}
	
	public void setIdeFceCaptacaoSubterranea(Integer ideFceCaptacaoSubterranea){
		this.ideFceCaptacaoSubterranea = ideFceCaptacaoSubterranea;
		
	}
	
	public Integer getNumTempoCaptacao(){
		return numTempoCaptacao;
	}
	
	public void setNumTempoCaptacao(Integer numTempoCaptacao){
		this.numTempoCaptacao = numTempoCaptacao;
		
	}
	
	public UnidadeGeologicaAflorante getIdeUnidadeGeologicaAflorante(){
		return ideUnidadeGeologicaAflorante;
	}
	
	public void setIdeUnidadeGeologicaAflorante(UnidadeGeologicaAflorante ideUnidadeGeograficaAflorante){
		this.ideUnidadeGeologicaAflorante = ideUnidadeGeograficaAflorante;
		
	}
	
	public Double getNumProfunidadePoco(){
		return numProfunidadePoco;
	}
	
	public void setNumProfunidadePoco(Double numProfunidadePoco){
		this.numProfunidadePoco = numProfunidadePoco;
		
	}
	
	public Double getNumNivelEstatico(){
		return numNivelEstatico;
	}
	
	public void setNumNivelEstatico(Double numNivelEstatico){
		this.numNivelEstatico = numNivelEstatico;
		
	}
	
	public Double getNumNivelDinamico(){
		return numNivelDinamico;
	}
	
	public void setNumNivelDinamico(Double numNivelDinamico){
		this.numNivelDinamico = numNivelDinamico;
		
	}
	
	public Double getNumVazaoTeste(){
		return numVazaoTeste;
	}
	
	public void setNumVazaoTeste(Double numVazaoTeste){
		this.numVazaoTeste = numVazaoTeste;
		
	}
	
	public Double getNumVazaoEspecifica(){
		return numVazaoEspecifica;
	}
	
	public void setNumVazaoEspecifica(Double numVazaoEspecifica){
		this.numVazaoEspecifica = numVazaoEspecifica;
		
	}
	
	public Fce getIdeFce(){
		return ideFce;
		
	}
	
	public void setIdeFce(Fce ideFce){
		this.ideFce = ideFce;
	}
	
	public OutorgaLocalizacaoGeografica getIdeOutorgaLocalizacaoGeografica(){
		return ideOutorgaLocalizacaoGeografica;
	}
	
	public void setIdeOutorgaLocalizacaoGeografica( OutorgaLocalizacaoGeografica ideOutorgaLocalizacaoGeografica){
		this.ideOutorgaLocalizacaoGeografica = ideOutorgaLocalizacaoGeografica;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceCaptacaoSubterranea == null) ? 0 : ideFceCaptacaoSubterranea.hashCode());
		return result;
	}

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
		FceCaptacaoSubterranea other = (FceCaptacaoSubterranea) obj;
		if (ideFceCaptacaoSubterranea == null) {
			if (other.ideFceCaptacaoSubterranea != null) {
				return false;
			}
		} else if (!ideFceCaptacaoSubterranea.equals(other.ideFceCaptacaoSubterranea)) {
			return false;
		}
		return true;
	}

	public TipoPoco getIdeTipoPoco() {
		return ideTipoPoco;
	}

	public void setIdeTipoPoco(TipoPoco ideTipoPoco) {
		this.ideTipoPoco = ideTipoPoco;
	}

	public TipoAquifero getIdeTipoAquifero() {
		return ideTipoAquifero;
	}

	public void setIdeTipoAquifero(TipoAquifero ideTipoAquifero) {
		this.ideTipoAquifero = ideTipoAquifero;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public boolean isSemOutorgaLocGeoNumVazao() {
		return Util.isNullOuVazio(ideOutorgaLocalizacaoGeografica.getNumVazao());
	}

	public Double getVazaoHora(){
			return vazaoHora;
	}
	public void setVazaoHora(Double vazaoHora){
		this.vazaoHora = vazaoHora;
}

	public Aquifero getIdeAquifero() {
		return ideAquifero;
	}

	public void setIdeAquifero(Aquifero ideAquifero) {
		this.ideAquifero = ideAquifero;
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
	
	public String getBacia(){
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideFceOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getBaciaHidrografica();
		} 
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getBaciaHidrografica();
		}
	}

	public String getSubBacia(){
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideFceOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getSubBacia();
		} 
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getSubBacia();
		}
	}
	
	public String getRpga(){
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideFceOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getRpga();
		} 
		else {
			return this.ideOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica().getRpga();
		}
	}
	
	public BigDecimal getNumVazao(){
		if(isExibirParaAnaliseTecnica(ideFce)){
			return this.ideFceOutorgaLocalizacaoGeografica.getNumVazao();
		} else {
			return this.ideOutorgaLocalizacaoGeografica.getNumVazao();
		}
	}
	
	public void setNumVazao(BigDecimal numVazao){
		if(isExibirParaAnaliseTecnica(ideFce)){
			this.ideFceOutorgaLocalizacaoGeografica.setNumVazao(numVazao);
		} else {
			this.ideOutorgaLocalizacaoGeografica.setNumVazao(numVazao);
		}
	}

	public FceOutorgaLocalizacaoGeografica getIdeFceOutorgaLocalizacaoGeografica() {
		return ideFceOutorgaLocalizacaoGeografica;
	}

	public void setIdeFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica ideFceOutorgaLocalizacaoGeografica) {
		this.ideFceOutorgaLocalizacaoGeografica = ideFceOutorgaLocalizacaoGeografica;
	}

	public FceCaptacaoSubterranea clone() throws CloneNotSupportedException {
		return (FceCaptacaoSubterranea) super.clone();
	}
	
}
