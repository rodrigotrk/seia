package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Funcionalidade;

public interface FuncionalidadeDAOIf {

	public List<Funcionalidade> getFuncionalidades(Funcionalidade pFuncionalidade);
}