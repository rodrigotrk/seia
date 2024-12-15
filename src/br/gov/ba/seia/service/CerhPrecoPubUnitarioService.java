package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CerhPrecoPubUnitario;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhPrecoPubUnitarioService {
	
	@Inject
	IDAO<CerhPrecoPubUnitario> idao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhPrecoPubUnitario getCerhPrecoPubUnitarioByTipoUsoRecursoHidrico(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhPrecoPubUnitario.class, "a")
				.add(Restrictions.eq("a.tipoUsoRecursoHidrico", tipoUsoRecursoHidrico))
				.add(Restrictions.eq("a.indConsumo", false));
		return idao.buscarPorCriteriaMaxResult(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhPrecoPubUnitario getCerhPrecoPubUnitarioByTipoUsoRecursoHidricoConsumo(TipoUsoRecursoHidrico tipoUsoRecursoHidrico) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhPrecoPubUnitario.class, "a")
				.add(Restrictions.eq("a.tipoUsoRecursoHidrico", tipoUsoRecursoHidrico))
				.add(Restrictions.eq("a.indConsumo", true));
		return idao.buscarPorCriteriaMaxResult(criteria);
	}

}
