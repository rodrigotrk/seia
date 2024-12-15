package br.gov.ba.seia.dto;

import br.gov.ba.seia.enumerator.UnidadeMedidaEnum;

public class UnidadeMedidaCalculoDTO {

	private UnidadeMedidaEnum unidadeMedida;
	
	private Double valor;

	public UnidadeMedidaCalculoDTO(UnidadeMedidaEnum unidadeMedida, Double valor) {
		this.unidadeMedida = unidadeMedida;
		this.valor = valor;
	}
	
	
	public UnidadeMedidaEnum getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(UnidadeMedidaEnum unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
