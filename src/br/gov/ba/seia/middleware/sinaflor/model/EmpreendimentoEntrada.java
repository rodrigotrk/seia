package br.gov.ba.seia.middleware.sinaflor.model;

import java.math.BigDecimal;
import java.util.List;
/**
 * Classe modelo empreendimento de entrada
 * @author 
 *
 */
public class EmpreendimentoEntrada {

	private List<String> atividades;
	private BigDecimal centroideLatitude;
	private BigDecimal centroideLongitude;
	private String inscricaoEstadual;
	private Integer municipio;
	private String nome;
	private String proprietario;
	private AnexoEntrada shapePRJ;
	private AnexoEntrada shapeSHP;
	private Integer unidadeIbama;
	
	public EmpreendimentoEntrada() {
		
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

	public Integer getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Integer municipio) {
		this.municipio = municipio;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getProprietario() {
		return proprietario;
	}

	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
	}

	public AnexoEntrada getShapePRJ() {
		return shapePRJ;
	}

	public void setShapePRJ(AnexoEntrada shapePRJ) {
		this.shapePRJ = shapePRJ;
	}

	public AnexoEntrada getShapeSHP() {
		return shapeSHP;
	}

	public void setShapeSHP(AnexoEntrada shapeSHP) {
		this.shapeSHP = shapeSHP;
	}

	public Integer getUnidadeIbama() {
		return unidadeIbama;
	}

	public void setUnidadeIbama(Integer unidadeIbama) {
		this.unidadeIbama = unidadeIbama;
	}

}
