package br.gov.ba.seia.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.util.Util;

public class FuncionarioDAOImpl implements FuncionarioDAOIf {

    @Override
	@SuppressWarnings("unchecked")
    public List<Funcionario> getFuncionarios(Funcionario pFuncionario) {

    	StringBuilder lEjbql = new StringBuilder("select funcionario from Funcionario funcionario ");

    	if (!Util.isNull(pFuncionario)) {

    		lEjbql.append("where 1 = 1 ");

    		if (!Util.isNullOuVazio(pFuncionario.getIdePessoaFisica())) lEjbql.append(" AND funcionario.idePessoaFisica = :idePessoaFisica");

    		if (!Util.isNullOuVazio(pFuncionario.getPessoaFisica()) && !Util.isNullOuVazio(pFuncionario.getPessoaFisica().getIdeOcupacao())) lEjbql.append(" AND funcionario.pessoaFisica.ideOcupacao = :ideOcupacao");

    		if (!Util.isNullOuVazio(pFuncionario.getIdeArea())) lEjbql.append(" AND funcionario.ideArea = :ideArea");

    		if (!Util.isNullOuVazio(pFuncionario.getMatricula())) lEjbql.append(" AND funcionario.matricula = :matricula");
    	}

    	lEjbql.append(" order by funcionario.matricula");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pFuncionario)) {

    		if (!Util.isNullOuVazio(pFuncionario.getIdePessoaFisica())) lQuery.setParameter("idePessoaFisica", pFuncionario.getIdePessoaFisica());

    		if (!Util.isNullOuVazio(pFuncionario.getPessoaFisica()) && !Util.isNullOuVazio(pFuncionario.getPessoaFisica().getIdeOcupacao())) lQuery.setParameter("ideOcupacao", pFuncionario.getPessoaFisica().getIdeOcupacao());

    		if (!Util.isNullOuVazio(pFuncionario.getIdeArea())) lQuery.setParameter("ideArea", pFuncionario.getIdeArea());

    		if (!Util.isNullOuVazio(pFuncionario.getMatricula())) lQuery.setParameter("matricula", pFuncionario.getMatricula());
    	}

    	return lQuery.getResultList();
    }
}