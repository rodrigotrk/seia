package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.util.Util;

public class UsuarioDAOImpl implements UsuarioDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<Usuario> getUsuarios(Usuario pUsuario) {

    	StringBuilder lEjbql = new StringBuilder("select usuario from Usuario usuario ");

    	if (!Util.isNull(pUsuario)) {

    		lEjbql.append("where usuario.indExcluido = :indExcluido ");

    		if (!Util.isNull(pUsuario.getIdePessoaFisica())) lEjbql.append(" AND usuario.idePessoaFisica = :idePessoaFisica");

    		if (!Util.isNull(pUsuario.getDscLogin())) lEjbql.append(" AND usuario.dscLogin = :dscLogin)");
    	}

    	lEjbql.append(" order by usuario.dscLogin");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pUsuario)) {

    		lQuery.setParameter("indExcluido", pUsuario.getIndExcluido());

    		if (!Util.isNull(pUsuario.getIdePessoaFisica())) lQuery.setParameter("idePessoaFisica", pUsuario.getIdePessoaFisica());

    		if (!Util.isNull(pUsuario.getDscLogin())) lQuery.setParameter("dscLogin", pUsuario.getDscLogin());
    	}

    	return lQuery.getResultList();
    }

    @SuppressWarnings("unchecked")
	public Usuario getUsuario(Usuario pUsuario) {

    	StringBuilder lEjbql = new StringBuilder("select usuario from Usuario usuario ");

    	if (!Util.isNull(pUsuario)) {

    		lEjbql.append("where usuario.indExcluido = :indExcluido");

    		if (!Util.isNull(pUsuario.getIdePessoaFisica())) lEjbql.append(" AND usuario.idePessoaFisica = :idePessoaFisica");

    		if (!Util.isNull(pUsuario.getIdePerfil()) && !Util.isNull(pUsuario.getIdePerfil().getIdePerfil())) lEjbql.append(" AND usuario.idePerfil.idePerfil = :idePerfil");

    		if (!Util.isNull(pUsuario.getDscLogin())) lEjbql.append(" AND usuario.dscLogin = :dscLogin)");
    	}

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pUsuario)) {

    		lQuery.setParameter("indExcluido", pUsuario.getIndExcluido());

    		if (!Util.isNull(pUsuario.getIdePessoaFisica())) lQuery.setParameter("idePessoaFisica", pUsuario.getIdePessoaFisica());

    		if (!Util.isNull(pUsuario.getIdePerfil()) && !Util.isNull(pUsuario.getIdePerfil().getIdePerfil())) lQuery.setParameter("idePerfil", pUsuario.getIdePerfil().getIdePerfil());

    		if (!Util.isNull(pUsuario.getDscLogin())) lQuery.setParameter("dscLogin", pUsuario.getDscLogin());
    	}

    	Collection<Usuario> lColecaoUsuario = lQuery.getResultList();

    	for (Usuario lUsuario : lColecaoUsuario) {

    		return lUsuario;
    	}

   		return null;
    }
}