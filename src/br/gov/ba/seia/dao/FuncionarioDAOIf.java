package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Funcionario;

public interface FuncionarioDAOIf {

	public List<Funcionario> getFuncionarios(Funcionario pFuncionario);
}