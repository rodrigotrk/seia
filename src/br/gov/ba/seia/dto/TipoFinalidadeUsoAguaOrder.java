package br.gov.ba.seia.dto;

import java.io.Serializable;

import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;

public class TipoFinalidadeUsoAguaOrder implements Serializable,  Comparable<TipoFinalidadeUsoAguaOrder>{
	private static final long serialVersionUID = 1L;
	
	private int order;
	private TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua;
	
	
	public TipoFinalidadeUsoAguaOrder() {
	}

	public TipoFinalidadeUsoAguaOrder(int order,TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua) {
		this.order = order;
		this.tipoFinalidadeUsoAgua = tipoFinalidadeUsoAgua;
	}

	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public TipoFinalidadeUsoAgua getTipoFinalidadeUsoAgua() {
		return tipoFinalidadeUsoAgua;
	}
	public void setTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua) {
		this.tipoFinalidadeUsoAgua = tipoFinalidadeUsoAgua;
	}

	@Override
	public int compareTo(TipoFinalidadeUsoAguaOrder tipoFinalidadeUsoAguaOrder) {
		if(this.order < tipoFinalidadeUsoAguaOrder.getOrder()){
			return -1;
		}
		if(this.order > tipoFinalidadeUsoAguaOrder.getOrder()){
			return 1;
		}
		
		return 0;
	}

	@Override
	public String toString() {
		return  String.valueOf(order);
	}
}
