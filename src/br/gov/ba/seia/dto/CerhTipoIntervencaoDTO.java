package br.gov.ba.seia.dto;

import br.gov.ba.seia.interfaces.BaseEntity;



public class CerhTipoIntervencaoDTO implements BaseEntity {

	private Integer ideCerhTipoIntervencao;
	private String dscCerhTipoIntervencao;
	
	public CerhTipoIntervencaoDTO(){}
	
	public CerhTipoIntervencaoDTO(Integer ide) {
		this.ideCerhTipoIntervencao = ide;
	}
	
	public CerhTipoIntervencaoDTO(Integer ide, String dsc){
		this(ide);
		this.dscCerhTipoIntervencao = dsc;
	}

	public Integer getIdeCerhTipoIntervencao() {
		return ideCerhTipoIntervencao;
	}

	public void setIdeCerhTipoIntervencao(Integer ideCerhTipoIntervencao) {
		this.ideCerhTipoIntervencao = ideCerhTipoIntervencao;
	}
	
	public String getDscCerhTipoIntervencao() {
		return dscCerhTipoIntervencao;
	}
	
	public void setDscCerhTipoIntervencao(String dscCerhTipoIntervencao) {
		this.dscCerhTipoIntervencao = dscCerhTipoIntervencao;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCerhTipoIntervencao == null) ? 0
						: ideCerhTipoIntervencao.hashCode());
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
		CerhTipoIntervencaoDTO other = (CerhTipoIntervencaoDTO) obj;
		if (ideCerhTipoIntervencao == null) {
			if (other.ideCerhTipoIntervencao != null)
				return false;
		} else if (!ideCerhTipoIntervencao
				.equals(other.ideCerhTipoIntervencao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.valueOf(ideCerhTipoIntervencao);
	}

	@Override
	public Long getId() {
		return Long.valueOf(this.ideCerhTipoIntervencao);
	}

}
