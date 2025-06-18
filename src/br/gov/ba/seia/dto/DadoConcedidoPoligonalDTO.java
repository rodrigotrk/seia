package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 07/03/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8592">#8592</a>
 *
 */
public class DadoConcedidoPoligonalDTO {

	private String numeroNotificacao;
	private Double valArea;
	private LocalizacaoGeografica localizacaoGeografica;
	private boolean concedido;
	
	public DadoConcedidoPoligonalDTO(LocalizacaoGeografica localizacaoGeografica, String numeroNotificacao) {
		this.localizacaoGeografica = localizacaoGeografica;
		this.numeroNotificacao = numeroNotificacao;
	}

	public boolean isConcedido() {
		return concedido;
	}
	
	public void setConcedido(boolean concedido) {
		this.concedido = concedido;
	}
	
	public String getUrlGeoBahia() {
		return Util.criarUrlShape(localizacaoGeografica);
	}

	public String getNumeroNotificacao() {
		return numeroNotificacao;
	}
	
	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}
	
	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}
	
	public Double getValArea() {
		return valArea;
	}

	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((localizacaoGeografica == null) ? 0 : localizacaoGeografica
						.hashCode());
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
		DadoConcedidoPoligonalDTO other = (DadoConcedidoPoligonalDTO) obj;
		if (localizacaoGeografica == null) {
			if (other.localizacaoGeografica != null)
				return false;
		} else if (!localizacaoGeografica.equals(other.localizacaoGeografica))
			return false;
		return true;
	}
	
}
