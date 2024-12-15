package br.gov.ba.seia.dto;

import java.util.Map;

import br.gov.ba.seia.enumerator.TipoRelatorioEnum;


public class RelatorioDTO {

	private String titulo;
	private String subreport;
	
	private String tituloNumero;
	private String numero;
	private String tituloData;
	private String data;
	private String pasta;

	private Integer ideEmpreendimento;
	private Integer ideRequerente;
	private Integer ideConsulta;

	private TipoRelatorioEnum tipoRelatorioEnum;
	
	private Boolean isExibirDeclaracao;
	private Boolean isExibirRodape;

	private Map<String, Object> parametros;
	
	
	public RelatorioDTO() {
		isExibirDeclaracao = true;
		isExibirRodape = true;
	}

	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getSubreport() {
		return subreport;
	}
	
	public void setSubreport(String subreport) {
		this.subreport = subreport;
	}

	public Integer getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Integer ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public Integer getIdeRequerente() {
		return ideRequerente;
	}

	public void setIdeRequerente(Integer ideRequerente) {
		this.ideRequerente = ideRequerente;
	}

	public boolean isExibirDeclaracao() {
		return isExibirDeclaracao;
	}

	public void setExibirDeclaracao(boolean isExibirDeclaracao) {
		this.isExibirDeclaracao = isExibirDeclaracao;
	}

	public String getTituloNumero() {
		return tituloNumero;
	}

	public String getNumero() {
		return numero;
	}

	public String getTituloData() {
		return tituloData;
	}

	public String getData() {
		return data;
	}

	public Boolean getIsExibirDeclaracao() {
		return isExibirDeclaracao;
	}

	public void setTituloNumero(String tituloNumero) {
		this.tituloNumero = tituloNumero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public void setTituloData(String tituloData) {
		this.tituloData = tituloData;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setIsExibirDeclaracao(Boolean isExibirDeclaracao) {
		this.isExibirDeclaracao = isExibirDeclaracao;
	}

	public Boolean getIsExibirRodape() {
		return isExibirRodape;
	}

	public void setIsExibirRodape(Boolean isExibirRodape) {
		this.isExibirRodape = isExibirRodape;
	}

	public Integer getIdeConsulta() {
		return ideConsulta;
	}

	public void setIdeConsulta(Integer ideConsulta) {
		this.ideConsulta = ideConsulta;
	}

	public String getPasta() {
		return pasta;
	}

	public void setPasta(String pasta) {
		this.pasta = pasta;
	}

	public Map<String, Object> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}

	public TipoRelatorioEnum getTipoRelatorioEnum() {
		return tipoRelatorioEnum;
	}

	public void setTipoRelatorioEnum(TipoRelatorioEnum tipoRelatorioEnum) {
		this.tipoRelatorioEnum = tipoRelatorioEnum;
	}
	
}
