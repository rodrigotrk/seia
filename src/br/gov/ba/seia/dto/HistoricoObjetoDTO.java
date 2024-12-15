package br.gov.ba.seia.dto;

import java.util.List;

public class HistoricoObjetoDTO {

	private String Titulo;
	private List<HistoricoDTO> historicoDTOList;

	public String getTitulo() {
		return Titulo;
	}
	
	public void setTitulo(String titulo) {
		Titulo = titulo;
	}
	
	public List<HistoricoDTO> getHistoricoDTOList() {
		return historicoDTOList;
	}
	
	public void setHistoricoDTOList(List<HistoricoDTO> historicoDTOList) {
		this.historicoDTOList = historicoDTOList;
	}
}
