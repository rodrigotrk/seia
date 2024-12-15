package br.gov.ba.seia.middleware.sinaflor.model;

import java.math.BigDecimal;
import java.util.List;
/**
 * Classe modelo projeto de entrada
 * @author 
 *
 */
public class ProjetoEntrada {

	private BigDecimal areaAutorizada;
	private Integer empreendimento;
	private List<IndividuoEntrada> inventarioIndividuos;
	private AnexoEntrada inventarioPlanilha;
	private BigDecimal latitudeProjeto;
	private BigDecimal longitudeProjeto;
	private List<String> propriedadesRurais;
	private List<ResponsavelTecnicoEntrada> responsaveisTecnicos;
	private AnexoEntrada shapePRJ;
	private AnexoEntrada shapeSHP;
	private Integer statusAutorizacao;
	private Integer tipoAtividade;
	private Integer unidadeIbama;
	private String validadeFim;
	private String validadeInicio;
	private List<VolumeAutorizadoEntrada> volumesAutorizados;
	private AnexoEntrada arquivoIntegrado;

	public ProjetoEntrada() {

	}

	public BigDecimal getAreaAutorizada() {
		return areaAutorizada;
	}

	public void setAreaAutorizada(BigDecimal areaAutorizada) {
		this.areaAutorizada = areaAutorizada;
	}

	public Integer getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Integer empreendimento) {
		this.empreendimento = empreendimento;
	}

	public List<IndividuoEntrada> getInventarioIndividuos() {
		return inventarioIndividuos;
	}

	public void setInventarioIndividuos(List<IndividuoEntrada> inventarioIndividuos) {
		this.inventarioIndividuos = inventarioIndividuos;
	}

	public AnexoEntrada getInventarioPlanilha() {
		return inventarioPlanilha;
	}

	public void setInventarioPlanilha(AnexoEntrada inventarioPlanilha) {
		this.inventarioPlanilha = inventarioPlanilha;
	}

	public BigDecimal getLatitudeProjeto() {
		return latitudeProjeto;
	}

	public void setLatitudeProjeto(BigDecimal latitudeProjeto) {
		this.latitudeProjeto = latitudeProjeto;
	}

	public BigDecimal getLongitudeProjeto() {
		return longitudeProjeto;
	}

	public void setLongitudeProjeto(BigDecimal longitudeProjeto) {
		this.longitudeProjeto = longitudeProjeto;
	}

	public List<String> getPropriedadesRurais() {
		return propriedadesRurais;
	}

	public void setPropriedadesRurais(List<String> propriedadesRurais) {
		this.propriedadesRurais = propriedadesRurais;
	}

	public List<ResponsavelTecnicoEntrada> getResponsaveisTecnicos() {
		return responsaveisTecnicos;
	}

	public void setResponsaveisTecnicos(List<ResponsavelTecnicoEntrada> responsaveisTecnicos) {
		this.responsaveisTecnicos = responsaveisTecnicos;
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

	public Integer getStatusAutorizacao() {
		return statusAutorizacao;
	}

	public void setStatusAutorizacao(Integer statusAutorizacao) {
		this.statusAutorizacao = statusAutorizacao;
	}

	public Integer getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(Integer tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public Integer getUnidadeIbama() {
		return unidadeIbama;
	}

	public void setUnidadeIbama(Integer unidadeIbama) {
		this.unidadeIbama = unidadeIbama;
	}

	public String getValidadeFim() {
		return validadeFim;
	}

	public void setValidadeFim(String validadeFim) {
		this.validadeFim = validadeFim;
	}

	public String getValidadeInicio() {
		return validadeInicio;
	}

	public void setValidadeInicio(String validadeInicio) {
		this.validadeInicio = validadeInicio;
	}

	public List<VolumeAutorizadoEntrada> getVolumesAutorizados() {
		return volumesAutorizados;
	}

	public void setVolumesAutorizados(List<VolumeAutorizadoEntrada> volumesAutorizados) {
		this.volumesAutorizados = volumesAutorizados;
	}

	public AnexoEntrada getArquivoIntegrado() {
		return arquivoIntegrado;
	}

	public void setArquivoIntegrado(AnexoEntrada arquivoIntegrado) {
		this.arquivoIntegrado = arquivoIntegrado;
	}

}
