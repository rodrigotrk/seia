package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.Acao;
import br.gov.ba.seia.util.Util;

public class AcaoDAOImpl  implements AcaoDAOIf {

	@Inject
	IDAO<Acao> acaoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Acao filtrarById(Acao pAcao) {
		return acaoDAO.buscarEntidadePorExemplo(pAcao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Acao> filtrarLista(Acao pAcao) {

		return new AcaoDAOImpl().getAcoes(pAcao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Acao pAcao) {

		pAcao.setDtcCriacao(new Date());
		acaoDAO.salvar(pAcao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Acao pAcao){
		acaoDAO.atualizar(pAcao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Acao pAcao){

		pAcao.setIndExcluido(true);
		pAcao.setDtcExclusao(new Date());

		acaoDAO.atualizar(pAcao);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Acao> getAcoes(Acao pAcao) {

		StringBuilder lEjbql = new StringBuilder("select acao from Acao acao ");

		if (!Util.isNull(pAcao)) {

			lEjbql.append("where acao.indExcluido = :indExcluido ");

			if (!Util.isNull(pAcao.getIdeAcao()))
				lEjbql.append(" AND acao.ideAcao = :ideAcao");

			if (!Util.isNull(pAcao.getDscAcao()))
				lEjbql.append(" AND lower(acao.dscAcao) LIKE lower(:dscAcao)");
		}

		lEjbql.append(" order by acao.dscAcao");

		EntityManager lEntityManager = DAOFactory.getEntityManager();

		Query lQuery = lEntityManager.createQuery(lEjbql.toString());

		if (!Util.isNull(pAcao)) {

			lQuery.setParameter("indExcluido", pAcao.getIndExcluido());

			if (!Util.isNull(pAcao.getIdeAcao()))
				lQuery.setParameter("ideAcao", pAcao.getIdeAcao());

			if (!Util.isNull(pAcao.getDscAcao()))
				lQuery.setParameter("dscAcao", pAcao.getDscAcao() + "%");
		}

		return lQuery.getResultList();
	}
}