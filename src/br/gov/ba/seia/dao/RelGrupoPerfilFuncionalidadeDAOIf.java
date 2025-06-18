package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.RelGrupoPerfilFuncionalidade;

public interface RelGrupoPerfilFuncionalidadeDAOIf {

	public List<RelGrupoPerfilFuncionalidade> getPermissoes(RelGrupoPerfilFuncionalidade pRelGrupoPerfilFuncionalidade);
}