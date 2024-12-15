package br.gov.ba.seia.dao;

import java.util.List;

import br.gov.ba.seia.entity.Usuario;

public interface UsuarioDAOIf {

	public List<Usuario> getUsuarios(Usuario pUsuario);
}