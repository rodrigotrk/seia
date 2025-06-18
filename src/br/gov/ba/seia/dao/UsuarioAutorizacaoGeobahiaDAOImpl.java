package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.entity.UsuarioAutorizacaoGeobahia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsuarioAutorizacaoGeobahiaDAOImpl {

	@Inject
	private IDAO<UsuarioAutorizacaoGeobahia> usuarioAutorizacaoGeobahiaDAOImpl;
	/**
	 * 
	 * @see http://redmine.prodeb.ba.gov.br/issues/129242
	 * @param ideUsuario
	 * @return token
	 */
	public String carregarUsuarioAutorizacaoGeobahia(Usuario ideUsuario) {
		
		String sql = "SELECT (CASE\n" + 
				"WHEN count(uag.token) > 0 THEN\n" + 
				"max(uag.token)\n" + 
				"ELSE\n" + 
				"''\n" + 
				"END) token from usuario_autorizacao_geobahia uag inner join usuario u on\n" + 
				"u.ide_pessoa_fisica = uag.ide_pessoa_fisica " +
				"where uag.ide_pessoa_fisica = :idePessoaFisica";
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idePessoaFisica", ideUsuario.getIdePessoaFisica());

		Object objeto = usuarioAutorizacaoGeobahiaDAOImpl.obterPorNativeQuery(sql, params);
		
		return (String)objeto;
		
	}
	
	public UsuarioAutorizacaoGeobahia carregarUsuarioAutorizacaoGeobahiaCefir(Usuario ideUsuario) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(UsuarioAutorizacaoGeobahia.class, "uAG");
		
		criteria.add(Restrictions.eq("uAG.idePessoaFisica", ideUsuario.getIdePessoaFisica()));
		
		criteria.add(Restrictions.eq("uAG.indExcluido", Boolean.FALSE));
		
		return usuarioAutorizacaoGeobahiaDAOImpl.buscarPorCriteria(criteria);
		 
	}
}
