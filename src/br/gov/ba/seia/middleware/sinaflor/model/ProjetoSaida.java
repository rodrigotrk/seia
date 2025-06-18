package br.gov.ba.seia.middleware.sinaflor.model;

import java.math.BigDecimal;
import java.util.List;
/**
 * Classe modelo projeto de saida
 * @author 
 *
 */
public class ProjetoSaida {

	private BigDecimal areaAutorizada;
	private String autorizacao;
	private Integer empreendimento;
	private Integer id;
	private List<IndividuoSaida> inventarioIndividuos;
	private AnexoSaida inventarioPlanilha;
	private BigDecimal latitudeProjeto;
	private BigDecimal longitudeProjeto;
	private List<PropriedadeRuralSaida> propriedadesRurais;
	private List<ResponsavelTecnicoSaida> responsaveisTecnicos;
	private AnexoSaida shapePRJ;
	private AnexoSaida shapeSHP;
	private DadosApoioSaida statusAutorizacao;
	private DadosApoioSaida tipoAtividade;
	private UnidadeIbamaSaida unidadeIbama;
	private String validadeFim;
	private String validadeInicio;
	private List<VolumeAutorizadoSaida> volumesAutorizados;

	public ProjetoSaida() {

	}

	public BigDecimal getAreaAutorizada() {
		return areaAutorizada;
	}

	public void setAreaAutorizada(BigDecimal areaAutorizada) {
		this.areaAutorizada = areaAutorizada;
	}

	public String getAutorizacao() {
		return autorizacao;
	}

	public void setAutorizacao(String autorizacao) {
		this.autorizacao = autorizacao;
	}

	public Integer getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Integer empreendimento) {
		this.empreendimento = empreendimento;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<IndividuoSaida> getInventarioIndividuos() {
		return inventarioIndividuos;
	}

	public void setInventarioIndividuos(List<IndividuoSaida> inventarioIndividuos) {
		this.inventarioIndividuos = inventarioIndividuos;
	}

	public AnexoSaida getInventarioPlanilha() {
		return inventarioPlanilha;
	}

	public void setInventarioPlanilha(AnexoSaida inventarioPlanilha) {
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

	public List<PropriedadeRuralSaida> getPropriedadesRurais() {
		return propriedadesRurais;
	}

	public void setPropriedadesRurais(List<PropriedadeRuralSaida> propriedadesRurais) {
		this.propriedadesRurais = propriedadesRurais;
	}

	public List<ResponsavelTecnicoSaida> getResponsaveisTecnicos() {
		return responsaveisTecnicos;
	}

	public void setResponsaveisTecnicos(List<ResponsavelTecnicoSaida> responsaveisTecnicos) {
		this.responsaveisTecnicos = responsaveisTecnicos;
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

	public DadosApoioSaida getStatusAutorizacao() {
		return statusAutorizacao;
	}

	public void setStatusAutorizacao(DadosApoioSaida statusAutorizacao) {
		this.statusAutorizacao = statusAutorizacao;
	}

	public DadosApoioSaida getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(DadosApoioSaida tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public UnidadeIbamaSaida getUnidadeIbama() {
		return unidadeIbama;
	}

	public void setUnidadeIbama(UnidadeIbamaSaida unidadeIbama) {
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

	public List<VolumeAutorizadoSaida> getVolumesAutorizados() {
		return volumesAutorizados;
	}

	public void setVolumesAutorizados(List<VolumeAutorizadoSaida> volumesAutorizados) {
		this.volumesAutorizados = volumesAutorizados;
	}

}
