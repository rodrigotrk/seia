package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.TipoAproveitamentoHidreletrico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoAproveitamentoHidreletricoDAOImpl {

	@Inject
	private IDAO<TipoAproveitamentoHidreletrico> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAproveitamentoHidreletrico> listar() {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoAproveitamentoHidreletrico.class)
				.add(Restrictions.eq("indAtivo", true));
		return dao.listarPorCriteria(criteria);
	}
	
}
