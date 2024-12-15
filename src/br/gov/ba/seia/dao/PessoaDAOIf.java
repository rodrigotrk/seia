package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Pessoa;

public interface PessoaDAOIf {

	public Pessoa getPessoa(Pessoa pPessoa);

	public List<Pessoa> getPessoas(Pessoa pPessoa);
}