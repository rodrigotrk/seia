package br.gov.ba.seia.dto;

import java.util.List;

import br.gov.ba.seia.interfaces.AgrupadorInterface;
/**
 * Classe de integração
 * @author 
 *
 * @param <K>
 * @param <T>
 */
public class AgrupadorDeListPorObjectDTO<K,T> {
	
	private K objetoAgrupador;
	
	private List<AgrupadorInterface<T>> listaASerAgrupada;
	
	/**
	 * 
	 * @param objetoAgrupador
	 * @param listaASerAgrupada
	 */
	public AgrupadorDeListPorObjectDTO(K objetoAgrupador, List<AgrupadorInterface<T>> listaASerAgrupada) {
		this.objetoAgrupador = objetoAgrupador;
		this.listaASerAgrupada = listaASerAgrupada;
	}

	public K getObjetoAgrupador() {
		return objetoAgrupador;
	}

	public void setObjetoAgrupador(K objetoAgrupador) {
		this.objetoAgrupador = objetoAgrupador;
	}

	public List<AgrupadorInterface<T>> getListaASerAgrupada() {
		return listaASerAgrupada;
	}

	public void setListaASerAgrupada(List<AgrupadorInterface<T>> listaASerAgrupada) {
		this.listaASerAgrupada = listaASerAgrupada;
	}
}
