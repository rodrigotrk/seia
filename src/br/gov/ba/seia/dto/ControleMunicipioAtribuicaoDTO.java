package br.gov.ba.seia.dto;

public class ControleMunicipioAtribuicaoDTO  {

	private String nomeDetalhe;

	private boolean isSelecionado;

	public ControleMunicipioAtribuicaoDTO(String nomeDetalhe, boolean isSelecionado) {
		this.nomeDetalhe = nomeDetalhe;
		this.isSelecionado = isSelecionado;
	}
	
	public String getNomeDetalhe() {
		return nomeDetalhe;
	}

	public void setNomeDetalhe(String nomeDetalhe) {
		this.nomeDetalhe = nomeDetalhe;
	}

	public boolean isSelecionado() {
		return isSelecionado;
	}

	public void setSelecionado(boolean isSelecionado) {
		this.isSelecionado = isSelecionado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isSelecionado ? 1231 : 1237);
		result = prime * result
				+ ((nomeDetalhe == null) ? 0 : nomeDetalhe.hashCode());
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
		ControleMunicipioAtribuicaoDTO other = (ControleMunicipioAtribuicaoDTO) obj;
		if (isSelecionado != other.isSelecionado)
			return false;
		if (nomeDetalhe == null) {
			if (other.nomeDetalhe != null)
				return false;
		} else if (!nomeDetalhe.equals(other.nomeDetalhe))
			return false;
		return true;
	}
}
