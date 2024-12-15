package br.gov.ba.seia.service;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CategoriaTurismo;
import br.gov.ba.seia.enumerator.TipologiaEnum;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 15/12/2014
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CategoriaTurismoService {

	@Inject
	private IDAO<CategoriaTurismo> categoriaTurismoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CategoriaTurismo> listarCategoriaTurismoByIndAtivo() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CategoriaTurismo.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return categoriaTurismoIDAO.listarPorCriteria(criteria, Order.asc("ideCategoriaTurismo"));
	}

	public List<CategoriaTurismo> listarCategoriaTurismoByTipologiaEnum(TipologiaEnum tipologiaEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CategoriaTurismo.class)
				.createAlias("ideTipologia", "t",JoinType.INNER_JOIN)
				.add(Restrictions.eq("indAtivo", true))
				.add(Restrictions.eq("t.indExcluido", false))
				.add(Restrictions.eq("t.ideTipologia", tipologiaEnum.getId()));
		return categoriaTurismoIDAO.listarPorCriteria(criteria, Order.asc("ideCategoriaTurismo"));
	}
}
