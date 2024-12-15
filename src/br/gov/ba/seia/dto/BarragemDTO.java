package br.gov.ba.seia.dto;

import br.gov.ba.seia.entity.Barragem;
import br.gov.ba.seia.interfaces.BaseEntity;

public class BarragemDTO implements Comparable<BarragemDTO>, BaseEntity {

	private Barragem ideBarragem;
	private String longitude;
	private String latitude;
	private String nomMunicipio;
	private Boolean indSelecionado;
	
	/**
	 * Construtor
	 */
	public BarragemDTO() {
		this.indSelecionado = false;
	}
	/**
	 * 
	 * @param ideBarragem
	 */
	public BarragemDTO(Barragem ideBarragem) {
		this();
		this.ideBarragem = ideBarragem;
	}	
	
	public Barragem getIdeBarragem() {
		return ideBarragem;
	}
	
	public void setIdeBarragem(Barragem ideBarragem) {
		this.ideBarragem = ideBarragem;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getNomMunicipio() {
		return nomMunicipio;
	}
	
	public void setNomMunicipio(String nomMunicipio) {
		this.nomMunicipio = nomMunicipio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideBarragem == null) ? 0 : ideBarragem.hashCode());
		result = prime * result
				+ ((latitude == null) ? 0 : latitude.hashCode());
		result = prime * result
				+ ((longitude == null) ? 0 : longitude.hashCode());
		result = prime * result
				+ ((nomMunicipio == null) ? 0 : nomMunicipio.hashCode());
		return result;
	}
	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BarragemDTO other = (BarragemDTO) obj;
		if (ideBarragem == null) {
			if (other.ideBarragem != null)
				return false;
		} 
		else {
			if (latitude == null) {
				if (other.latitude != null)
					return false;
			} else if (!latitude.equals(other.latitude))
				return false;
			if (longitude == null) {
				if (other.longitude != null)
					return false;
			} else if (!longitude.equals(other.longitude))
				return false;
			if (nomMunicipio == null) {
				if (other.nomMunicipio != null)
					return false;
			} else if (!nomMunicipio.equals(other.nomMunicipio))
				return false;
		}
		return true;
	}

	
	@Override
	public int compareTo(BarragemDTO o) {
		return this.ideBarragem.compareTo(o.ideBarragem);
	}

	@Override
	public String toString() {
		return String.valueOf(ideBarragem);
	}

	@Override
	public Long getId() {
		return Long.valueOf(this.ideBarragem.getIdeBarragem());
	}

	public Boolean getIndSelecionado() {
		return indSelecionado;
	}

	public void setIndSelecionado(Boolean indSelecionado) {
		this.indSelecionado = indSelecionado;
	}

}
