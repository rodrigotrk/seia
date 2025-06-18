package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

public class EmpreendimentoImovel implements Serializable {

	private static final long serialVersionUID = 123L;
	
	private Integer idEmpreendimentoImovel;
	private String denominacao;
	private String areaIntervencao;
	private String latitude;
	private String longitude;
	private String pontoReferencia;
	private Date data;

	public EmpreendimentoImovel() {
		super();
	}

	public Integer getIdEmpreendimentoImovel() {
		return idEmpreendimentoImovel;
	}

	public void setIdEmpreendimentoImovel(Integer idEmpreendimentoImovel) {
		this.idEmpreendimentoImovel = idEmpreendimentoImovel;
	}

	public String getDenominacao() {
		return denominacao;
	}

	public void setDenominacao(String denominacao) {
		this.denominacao = denominacao;
	}

	public String getAreaIntervencao() {
		return areaIntervencao;
	}

	public void setAreaIntervencao(String areaIntervencao) {
		this.areaIntervencao = areaIntervencao;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getPontoReferencia() {
		return pontoReferencia;
	}

	public void setPontoReferencia(String pontoReferencia) {
		this.pontoReferencia = pontoReferencia;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idEmpreendimentoImovel == null) ? 0
						: idEmpreendimentoImovel.hashCode());
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
		EmpreendimentoImovel other = (EmpreendimentoImovel) obj;
		if (idEmpreendimentoImovel == null) {
			if (other.idEmpreendimentoImovel != null)
				return false;
		} else if (!idEmpreendimentoImovel.equals(other.idEmpreendimentoImovel))
			return false;
		return true;
	}

}
