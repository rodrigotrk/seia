package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.RelGrupoPerfilFuncionalidade;
import br.gov.ba.seia.util.Util;

public class RelGrupoPerfilFuncionalidadeDAOImpl implements RelGrupoPerfilFuncionalidadeDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<RelGrupoPerfilFuncionalidade> getPermissoes(RelGrupoPerfilFuncionalidade pRelGrupoPerfilFuncionalidade) {

    	StringBuilder lEjbql = new StringBuilder("select relGrupoPerfilFuncionalidade from RelGrupoPerfilFuncionalidade relGrupoPerfilFuncionalidade ");

    	if (!Util.isNull(pRelGrupoPerfilFuncionalidade)) {

    		lEjbql.append("where 1 = 1 ");

    		if (!Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getPerfil()) && !Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getPerfil().getIdePerfil())) lEjbql.append(" AND relGrupoPerfilFuncionalidade.perfil.idePerfil = :idePerfil");

    		if (!Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getFuncionalidade()) && !Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getFuncionalidade().getIdeFuncionalidade())) lEjbql.append(" AND relGrupoPerfilFuncionalidade.funcionalidade.ideFuncionalidade = :ideFuncionalidade");

    		if (!Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getAcao()) && !Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getAcao().getIdeAcao())) lEjbql.append(" AND relGrupoPerfilFuncionalidade.acao.ideAcao = :ideAcao");
    	}

    	lEjbql.append(" order by relGrupoPerfilFuncionalidade.perfil.dscPerfil, relGrupoPerfilFuncionalidade.funcionalidade.dscFuncionalidade, relGrupoPerfilFuncionalidade.acao.dscAcao");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pRelGrupoPerfilFuncionalidade)) {

    		if (!Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getPerfil()) && !Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getPerfil().getIdePerfil())) lQuery.setParameter("idePerfil", pRelGrupoPerfilFuncionalidade.getPerfil().getIdePerfil());

    		if (!Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getFuncionalidade()) && !Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getFuncionalidade().getIdeFuncionalidade())) lQuery.setParameter("ideFuncionalidade", pRelGrupoPerfilFuncionalidade.getFuncionalidade().getIdeFuncionalidade()); 

    		if (!Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getAcao()) && !Util.isNullOuVazio(pRelGrupoPerfilFuncionalidade.getAcao().getIdeAcao())) lQuery.setParameter("ideAcao", pRelGrupoPerfilFuncionalidade.getAcao().getIdeAcao());
    	}

    	return lQuery.getResultList();
    }
}