package br.gov.ba.seia.dto;

import java.util.List;

public class HistoricoDTO{

	private String nomeClasse;
	private String nome;
	private String novo;
	private String antigo;
	private List<HistoricoDTO> historicoObjeto;
	
	public HistoricoDTO() {
	}

	public HistoricoDTO(String nome, Object novo, Object antigo, String nomeClasse) {
		this.nome = nome;
		this.nomeClasse = nomeClasse;
		
		if(novo == null){
			this.novo = "-";
		}else{
			if(novo instanceof Boolean){
				if((Boolean) novo){
					this.novo = "Sim";
				}else{
					this.novo = "Não";
				}
			}else if(novo instanceof String && novo.equals("")){
				this.novo = "-";
			}
			else{
				this.novo = novo.toString();
			}
		}

		if(antigo == null){
			this.antigo = "-";
		}else{
			if(antigo instanceof Boolean){
				if((Boolean) antigo){
					this.antigo = "Sim";
				}else{
					this.antigo = "Não";
				}
			}else if(antigo instanceof String && antigo.equals("")){
				this.antigo = "-";
			}
			else{
				this.antigo = antigo.toString();
			}
		}
	}
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getNovo() {
		return novo;
	}
	
	public void setNovo(String novo) {
		this.novo = novo;
	}
	
	public String getAntigo() {
		return antigo;
	}

	public void setAntigo(String antigo) {
		this.antigo = antigo;
	}
	
	public String getNomeClasse() {
		return nomeClasse;
	}

	public void setNomeClasse(String nomeClasse) {
		this.nomeClasse = nomeClasse;
	}

	public List<HistoricoDTO> getHistoricoObjeto() {
		return historicoObjeto;
	}

	public void setHistoricoObjeto(List<HistoricoDTO> historicoObjeto) {
		this.historicoObjeto = historicoObjeto;
	}

	@Override
	public String toString() {
		return "[nome=" + nome + ", novo=" + novo + ", antigo=" + antigo + "]";
	}



}
