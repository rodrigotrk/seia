package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CerhPondClasCorpoHidrico;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhPondClasCorpoHidricoService {
	
	@Inject
	IDAO<CerhPondClasCorpoHidrico> idao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhPondClasCorpoHidrico getCerhPondClasCorpoHidricoByTipoUsoRecursoHidrico(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhPondClasCorpoHidrico.class, "a")
				.createAlias("a.classeCorpoHidrico", "b", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("a.tipoUsoRecursoHidrico", tipoUsoRecursoHidrico))
				.add(Restrictions.eq("a.indConsumo", false));
		return idao.buscarPorCriteriaMaxResult(criteria);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhPondClasCorpoHidrico getCerhPondClasCorpoHidricoByTipoUsoRecursoHidricoConsumo(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhPondClasCorpoHidrico.class, "a")
				.createAlias("a.classeCorpoHidrico", "b", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("a.tipoUsoRecursoHidrico", tipoUsoRecursoHidrico))
				.add(Restrictions.eq("a.indConsumo", true));
		return idao.buscarPorCriteriaMaxResult(criteria);
	}
	

}
