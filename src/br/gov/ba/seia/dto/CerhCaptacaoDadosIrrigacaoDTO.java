package br.gov.ba.seia.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.gov.ba.seia.entity.CerhCaptacaoDadosIrrigacao;
import br.gov.ba.seia.entity.MetodoIrrigacao;
import br.gov.ba.seia.entity.TipologiaAtividade;


/**
 * @author eduardo.fernandes 
 * @since 31/03/2017
 *
 */
public class CerhCaptacaoDadosIrrigacaoDTO {
	
	private String nomeCulturaIrrigadaPesquisa;
	
	private List<TipologiaAtividade> listaTipologiaAtividade;
	private List<TipologiaAtividade> listaTipologiaAtividadeFULL;
	
	private Collection<MetodoIrrigacao> metodoIrrigacaoCollection;
	
	private CerhCaptacaoDadosIrrigacao cerhCaptacaoDadosIrrigacao;

	public CerhCaptacaoDadosIrrigacaoDTO() {
		
	}
	
	public void clonarListaTipoAtividade() {
		listaTipologiaAtividadeFULL = new ArrayList<TipologiaAtividade>();
		listaTipologiaAtividadeFULL.addAll(listaTipologiaAtividade);
	}
	
	public String getNomeCulturaIrrigadaPesquisa() {
		return nomeCulturaIrrigadaPesquisa;
	}

	public void setNomeCulturaIrrigadaPesquisa(String nomeCulturaIrrigadaPesquisa) {
		this.nomeCulturaIrrigadaPesquisa = nomeCulturaIrrigadaPesquisa;
	}

	public List<TipologiaAtividade> getListaTipologiaAtividade() {
		return listaTipologiaAtividade;
	}

	public void setListaTipologiaAtividade(List<TipologiaAtividade> listaTipologiaAtividade) {
		this.listaTipologiaAtividade = listaTipologiaAtividade;
	}

	public Collection<MetodoIrrigacao> getMetodoIrrigacaoCollection() {
		return metodoIrrigacaoCollection;
	}

	public void setMetodoIrrigacaoCollection(Collection<MetodoIrrigacao> metodoIrrigacaoCollection) {
		this.metodoIrrigacaoCollection = metodoIrrigacaoCollection;
	}

	public List<TipologiaAtividade> getListaTipologiaAtividadeFULL() {
		return listaTipologiaAtividadeFULL;
	}

	public void setListaTipologiaAtividadeFULL(List<TipologiaAtividade> listaTipologiaAtividadeFULL) {
		this.listaTipologiaAtividadeFULL = listaTipologiaAtividadeFULL;
	}

	public CerhCaptacaoDadosIrrigacao getCerhCaptacaoDadosIrrigacao() {
		return cerhCaptacaoDadosIrrigacao;
	}

	public void setCerhCaptacaoDadosIrrigacao(CerhCaptacaoDadosIrrigacao cerhCaptacaoDadosIrrigacao) {
		this.cerhCaptacaoDadosIrrigacao = cerhCaptacaoDadosIrrigacao;
	}
	
}
