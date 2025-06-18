package br.gov.ba.seia.middleware.sinaflor.model;

import java.math.BigDecimal;
import java.util.List;
/**
 * Classe modelo empreendimento de saida
 * @author 
 *
 */
public class EmpreendimentoSaida {

	private List<String> atividades;
	private BigDecimal centroideLatitude;
	private BigDecimal centroideLongitude;
	private Integer id;
	private String inscricaoEstadual;
	private MunicipioSaida municipio;
	private String nome;
	private PessoaSaida proprietario;
	private AnexoSaida shapePRJ;
	private AnexoSaida shapeSHP;
	private UnidadeIbamaSaida unidadeIbama;

	public EmpreendimentoSaida() {

	}

	public List<String> getAtividades() {
		return atividades;
	}

	public void setAtividades(List<String> atividades) {
		this.atividades = atividades;
	}

	public BigDecimal getCentroideLatitude() {
		return centroideLatitude;
	}

	public void setCentroideLatitude(BigDecimal centroideLatitude) {
		this.centroideLatitude = centroideLatitude;
	}

	public BigDecimal getCentroideLongitude() {
		return centroideLongitude;
	}

	public void setCentroideLongitude(BigDecimal centroideLongitude) {
		this.centroideLongitude = centroideLongitude;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MunicipioSaida getMunicipio() {
		return municipio;
	}

	public void setMunicipio(MunicipioSaida municipio) {
		this.municipio = municipio;
	}

	public PessoaSaida getProprietario() {
		return proprietario;
	}

	public void setProprietario(PessoaSaida proprietario) {
		this.proprietario = proprietario;
	}

	public AnexoSaida getShapePRJ() {
		return shapePRJ;
	}

	public void setShapePRJ(AnexoSaida shapePRJ) {
		this.shapePRJ = shapePRJ;
	}

	public AnexoSaida getShapeSHP() {
		return shapeSHP;
	}

	public void setShapeSHP(AnexoSaida shapeSHP) {
		this.shapeSHP = shapeSHP;
	}

	public UnidadeIbamaSaida getUnidadeIbama() {
		return unidadeIbama;
	}

	public void setUnidadeIbama(UnidadeIbamaSaida unidadeIbama) {
		this.unidadeIbama = unidadeIbama;
	}

	@Override
	public String toString() {
		return "EmpreendimentoSaida [\n id=" + id +"\n atividades=" + atividades + ",\n centroideLatitude=" + centroideLatitude
				+ ",\n centroideLongitude=" + centroideLongitude + ",\n inscricaoEstadual="
				+ inscricaoEstadual + ",\n municipio=" + municipio + ",\n nome=" + nome + ",\n proprietario=" + proprietario
				+ ",\n shapePRJ=" + shapePRJ + ",\n shapeSHP=" + shapeSHP + ",\n unidadeIbama=" + unidadeIbama + "\n]";
	}

}
