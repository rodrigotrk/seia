package br.gov.ba.seia.dto;

import java.util.List;

import br.gov.ba.seia.entity.PessoaFisica;

public class PctDTO {

	private List<PessoaFisica> pessoaFisicaRepresentantes;

	private List<PessoaFisica> pessoaFisicas;

	public List<PessoaFisica> getPessoaFisicaRepresentantes() {
		return pessoaFisicaRepresentantes;
	}

	public void setPessoaFisicaRepresentantes(
			List<PessoaFisica> pessoaFisicaRepresentantes) {
		this.pessoaFisicaRepresentantes = pessoaFisicaRepresentantes;
	}

	public List<PessoaFisica> getPessoaFisicas() {
		return pessoaFisicas;
	}

	public void setPessoaFisicas(List<PessoaFisica> pessoaFisicas) {
		this.pessoaFisicas = pessoaFisicas;
	}

}
