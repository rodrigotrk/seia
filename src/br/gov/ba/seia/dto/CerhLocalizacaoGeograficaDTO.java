package br.gov.ba.seia.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.ba.seia.entity.CerhLancamentoEfluenteSazonalidade;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhOutrosUsos;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhTipoPrestadorServico;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Mes;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.enumerator.MesEnum;
import br.gov.ba.seia.util.Util;

public class CerhLocalizacaoGeograficaDTO implements Serializable, Comparable<CerhLocalizacaoGeograficaDTO>, Cloneable{ 
	private static final long serialVersionUID = 1L;

	private CerhProcesso cerhProcesso; 
	private CerhLocalizacaoGeografica cerhLocalizacaoGeografica;
	private CerhTipoPrestadorServico cerhTipoPrestadorServico;
	private CerhOutrosUsos cerhOutrosUsos;
	private boolean possivelCaracterizar; 
	
	private List<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaList;
	private List<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaExcluirList;
	private List<CerhLancamentoEfluenteSazonalidade> cerhLancamentoEfluenteSazonalidadeList;
	

	public CerhLocalizacaoGeograficaDTO(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		this.cerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
		
		tipoFinalidadeUsoAguaList = new ArrayList<TipoFinalidadeUsoAgua>();
		tipoFinalidadeUsoAguaExcluirList = new ArrayList<TipoFinalidadeUsoAgua>();
		cerhLancamentoEfluenteSazonalidadeList =  new ArrayList<CerhLancamentoEfluenteSazonalidade>();
		
		carregarCerhLancamentoVazaoSazonalidadeList();
	}

	public CerhLocalizacaoGeograficaDTO(CerhProcesso cerhProcesso,CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		this.cerhProcesso = cerhProcesso;
		this.cerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
	}

	public CerhLocalizacaoGeograficaDTO() {
		cerhLocalizacaoGeografica = new CerhLocalizacaoGeografica();
		cerhLocalizacaoGeografica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		
		tipoFinalidadeUsoAguaList = new ArrayList<TipoFinalidadeUsoAgua>();
		tipoFinalidadeUsoAguaExcluirList = new ArrayList<TipoFinalidadeUsoAgua>();
		cerhLancamentoEfluenteSazonalidadeList =  new ArrayList<CerhLancamentoEfluenteSazonalidade>();
		
		carregarCerhLancamentoVazaoSazonalidadeList();
		
	}

	
	private void carregarCerhLancamentoVazaoSazonalidadeList() {
		cerhLancamentoEfluenteSazonalidadeList = new ArrayList<CerhLancamentoEfluenteSazonalidade>();
		 
		for (MesEnum mes : MesEnum.values()) {
			cerhLancamentoEfluenteSazonalidadeList.add(new CerhLancamentoEfluenteSazonalidade(new Mes(mes.getValue(), mes.getNomMes())));
		}
	}
	
	public boolean isPossivelCaracterizar() {
		return possivelCaracterizar;
	}

	public void setPossivelCaracterizar(boolean possivelCaracterizar) {
		this.possivelCaracterizar = possivelCaracterizar;
	}

	public CerhProcesso getCerhProcesso() {
		return cerhProcesso;
	}
	
	public void setCerhProcesso(CerhProcesso cerhProcesso) {
		this.cerhProcesso = cerhProcesso;
	}
	
	public CerhLocalizacaoGeografica getCerhLocalizacaoGeografica() {
		return cerhLocalizacaoGeografica;
	}
	
	public void setCerhLocalizacaoGeografica(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		this.cerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
	}
	
	public List<TipoFinalidadeUsoAgua> getTipoFinalidadeUsoAguaList() {
		return tipoFinalidadeUsoAguaList;
	}

	public void setTipoFinalidadeUsoAguaList(List<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaList) {
		this.tipoFinalidadeUsoAguaList = tipoFinalidadeUsoAguaList;
	}

	public List<TipoFinalidadeUsoAgua> getTipoFinalidadeUsoAguaExcluirList() {
		return tipoFinalidadeUsoAguaExcluirList;
	}

	public void setTipoFinalidadeUsoAguaExcluirList(List<TipoFinalidadeUsoAgua> tipoFinalidadeUsoAguaExcluirList) {
		this.tipoFinalidadeUsoAguaExcluirList = tipoFinalidadeUsoAguaExcluirList;
	}

	public List<CerhLancamentoEfluenteSazonalidade> getCerhLancamentoEfluenteSazonalidadeList() {
		return cerhLancamentoEfluenteSazonalidadeList;
	}

	public void setCerhLancamentoEfluenteSazonalidadeList(List<CerhLancamentoEfluenteSazonalidade> cerhLancamentoEfluenteSazonalidadeList) {
		this.cerhLancamentoEfluenteSazonalidadeList = cerhLancamentoEfluenteSazonalidadeList;
	}

	public CerhOutrosUsos getCerhOutrosUsos() {
		return cerhOutrosUsos;
	}

	public void setCerhOutrosUsos(CerhOutrosUsos cerhOutrosUsos) {
		this.cerhOutrosUsos = cerhOutrosUsos;
	}

	public CerhTipoPrestadorServico getCerhTipoPrestadorServico() {
		return cerhTipoPrestadorServico;
	}

	public void setCerhTipoPrestadorServico(CerhTipoPrestadorServico cerhTipoPrestadorServico) {
		this.cerhTipoPrestadorServico = cerhTipoPrestadorServico;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((cerhLocalizacaoGeografica == null) ? 0
						: cerhLocalizacaoGeografica.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CerhLocalizacaoGeograficaDTO other = (CerhLocalizacaoGeograficaDTO) obj;
		if (cerhLocalizacaoGeografica == null) {
			if (other.cerhLocalizacaoGeografica != null)
				return false;
		} else if (!cerhLocalizacaoGeografica.equals(other.cerhLocalizacaoGeografica))
			return false;
		return true;
	}

	@Override
	public CerhLocalizacaoGeograficaDTO clone() throws CloneNotSupportedException {
		return (CerhLocalizacaoGeograficaDTO) super.clone();
	}
	
	@Override
	public String toString() {
		return String.valueOf(cerhLocalizacaoGeografica);
	}

	@Override
	public int compareTo(CerhLocalizacaoGeograficaDTO o) {
		
		if(o==null){
			return 0;
		}
		if(!Util.isNullOuVazio(o.getCerhProcesso())&& !Util.isNullOuVazio(o.getCerhProcesso().getIndDadosConcedidos())){
			if(this.cerhProcesso.getIndDadosConcedidos()){
				return -1;
			}else{
				return 1;
			}
		}
		
		
		return 0;
	}

	
}