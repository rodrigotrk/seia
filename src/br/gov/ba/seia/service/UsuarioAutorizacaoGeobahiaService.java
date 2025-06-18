package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.UsuarioAutorizacaoGeobahiaDAOImpl;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.UsuarioAutorizacaoGeobahia;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsuarioAutorizacaoGeobahiaService {

	
	@Inject
	private UsuarioAutorizacaoGeobahiaDAOImpl usuarioAutorizacaoGeobahiaDAOImpl;
	 
	
	public String carregarUsuarioAutorizacaoGeobahia() {
		
		String usuarioAutorizacaoGeobahia = usuarioAutorizacaoGeobahiaDAOImpl.carregarUsuarioAutorizacaoGeobahia(ContextoUtil.getContexto().getUsuarioLogado());
		
		if(!Util.isNullOuVazio(usuarioAutorizacaoGeobahia)) {
			return usuarioAutorizacaoGeobahia;
		}
		
		return "";
	}
	
	public String carregarUsuarioAutorizacaoGeobahiaCefir() {
		UsuarioAutorizacaoGeobahia usuarioAutorizacaoGeobahia =usuarioAutorizacaoGeobahiaDAOImpl.carregarUsuarioAutorizacaoGeobahiaCefir(ContextoUtil.getContexto().getUsuarioLogado());
		
		if(!Util.isNullOuVazio(usuarioAutorizacaoGeobahia) && !Util.isNullOuVazio(usuarioAutorizacaoGeobahia.getToken())) {
			return usuarioAutorizacaoGeobahia.getToken();
		}
		return "";
	}
}
