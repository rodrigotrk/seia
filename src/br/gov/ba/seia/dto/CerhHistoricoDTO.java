package br.gov.ba.seia.dto;

import java.util.List;

public class CerhHistoricoDTO {

	private List<HistoricoDTO> abaDadosGerais;
	private List<HistoricoDTO> abaBarragem;
	private List<HistoricoDTO> abaIntervencao;
	private List<HistoricoDTO> abaCaptacaoSuperficial;
	private List<HistoricoDTO> abaCaptacaoSubteranea;
	private List<HistoricoDTO> abaLancamentoEfluente;

	public CerhHistoricoDTO() {
	}
	
	public List<HistoricoDTO> getAbaDadosGerais() {
		return abaDadosGerais;
	}
	
	public void setAbaDadosGerais(List<HistoricoDTO> abaDadosGerais) {
		this.abaDadosGerais = abaDadosGerais;
	}
	
	public List<HistoricoDTO> getAbaBarragem() {
		return abaBarragem;
	}
	
	public void setAbaBarragem(List<HistoricoDTO> abaBarragem) {
		this.abaBarragem = abaBarragem;
	}
	
	public List<HistoricoDTO> getAbaIntervencao() {
		return abaIntervencao;
	}
	
	public void setAbaIntervencao(List<HistoricoDTO> abaIntervencao) {
		this.abaIntervencao = abaIntervencao;
	}
	
	public List<HistoricoDTO> getAbaCaptacaoSuperficial() {
		return abaCaptacaoSuperficial;
	}
	
	public void setAbaCaptacaoSuperficial(List<HistoricoDTO> abaCaptacaoSuperficial) {
		this.abaCaptacaoSuperficial = abaCaptacaoSuperficial;
	}
	
	public List<HistoricoDTO> getAbaCaptacaoSubteranea() {
		return abaCaptacaoSubteranea;
	}

	public void setAbaCaptacaoSubteranea(List<HistoricoDTO> abaCaptacaoSubteranea) {
		this.abaCaptacaoSubteranea = abaCaptacaoSubteranea;
	}

	public List<HistoricoDTO> getAbaLancamentoEfluente() {
		return abaLancamentoEfluente;
	}
	
	public void setAbaLancamentoEfluente(List<HistoricoDTO> abaLancamentoEfluente) {
		this.abaLancamentoEfluente = abaLancamentoEfluente;
	}
}
