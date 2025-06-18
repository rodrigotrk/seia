package br.gov.ba.seia.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Escolaridade;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.util.Util;

public class EscolaridadeDAOImpl implements EscolaridadeDAOIf {

	@Inject
	private IDAO<Escolaridade> escolaridadeDAO;

	@Inject
	private ParametroService parametroService;

	public List<Escolaridade> listarTodos() {
		return escolaridadeDAO.listarTodos();
	}

	public List<Escolaridade> listarEscolaridadeResponsavelTecnico()  {
		Parametro ideEscolaridades = this.parametroService.obterPorId(ParametroEnum.IDE_ESCOLARIDADE.getIdeParametro());
		DetachedCriteria criteria = DetachedCriteria.forClass(Escolaridade.class);
		if(!Util.isNullOuVazio(ideEscolaridades)){
			criteria.add(Restrictions.in("ideEscolaridade", Util.stringToArrayInt(ideEscolaridades.getDscValor())));
		}
		return escolaridadeDAO.listarPorCriteria(criteria);
	}

	@Override
	@SuppressWarnings("unchecked")
    public List<Escolaridade> getEscolaridades(Escolaridade pEscolaridade) {

    	StringBuilder lEjbql = new StringBuilder("select escolaridade from Escolaridade escolaridade ");

    	if (!Util.isNull(pEscolaridade)) {

    		lEjbql.append("where 1 = 1 ");

    		if (!Util.isNull(pEscolaridade.getIdeEscolaridade())) lEjbql.append(" AND escolaridade.ideEscolaridade = :ideEscolaridade");

    		if (!Util.isNull(pEscolaridade.getNomEscolaridade())) lEjbql.append(" AND lower(escolaridade.nomEscolaridade) LIKE lower(:nomEscolaridade)");
    	}

    	lEjbql.append(" order by escolaridade.nomEscolaridade");

    	EntityManager lEntityManager = DAOFactory.getEntityManager();

    	lEntityManager.joinTransaction();

    	Query lQuery = lEntityManager.createQuery(lEjbql.toString());

    	if (!Util.isNull(pEscolaridade)) {

    		if (!Util.isNull(pEscolaridade.getIdeEscolaridade())) lQuery.setParameter("ideEscolaridade", pEscolaridade.getIdeEscolaridade());

    		if (!Util.isNull(pEscolaridade.getNomEscolaridade())) lQuery.setParameter("nomEscolaridade", pEscolaridade.getNomEscolaridade() + "%");
    	}

    	return lQuery.getResultList();
    }
}
