package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CerhLancamentoOutrosUsosPK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column(name="ide_cerh_outros_usos")
	private Integer ideOutrosUsosCerh;
	
	@Column(name="ide_cerh_lancamento_caracterizacao_origem")
	private Integer ideCerhLancamentoCaracterizacaoOrigem;
	
	public CerhLancamentoOutrosUsosPK() {
		
	}
	
	public CerhLancamentoOutrosUsosPK(Integer ideOutrosUsosCerh, Integer ideCerhLancamentoCaracterizacaoOrigem) {
		this.ideOutrosUsosCerh = ideOutrosUsosCerh;
		this.ideCerhLancamentoCaracterizacaoOrigem = ideCerhLancamentoCaracterizacaoOrigem;
	}

	public Integer getIdeOutrosUsosCerh() {
		return ideOutrosUsosCerh;
	}
	
	public void setIdeOutrosUsosCerh(Integer ideOutrosUsosCerh) {
		this.ideOutrosUsosCerh = ideOutrosUsosCerh;
	}
	
	public Integer getIdeCerhLancamentoCaracterizacaoOrigem() {
		return ideCerhLancamentoCaracterizacaoOrigem;
	}
	
	public void setIdeCerhLancamentoCaracterizacaoOrigem(
			Integer ideCerhLancamentoCaracterizacaoOrigem) {
		this.ideCerhLancamentoCaracterizacaoOrigem = ideCerhLancamentoCaracterizacaoOrigem;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCerhLancamentoCaracterizacaoOrigem == null) ? 0
						: ideCerhLancamentoCaracterizacaoOrigem.hashCode());
		result = prime
				* result
				+ ((ideOutrosUsosCerh == null) ? 0 : ideOutrosUsosCerh
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
		CerhLancamentoOutrosUsosPK other = (CerhLancamentoOutrosUsosPK) obj;
		if (ideCerhLancamentoCaracterizacaoOrigem == null) {
			if (other.ideCerhLancamentoCaracterizacaoOrigem != null)
				return false;
		} else if (!ideCerhLancamentoCaracterizacaoOrigem
				.equals(other.ideCerhLancamentoCaracterizacaoOrigem))
			return false;
		if (ideOutrosUsosCerh == null) {
			if (other.ideOutrosUsosCerh != null)
				return false;
		} else if (!ideOutrosUsosCerh.equals(other.ideOutrosUsosCerh))
			return false;
		return true;
	}
	
}
