package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceOutorgaTipoInfraestrutura;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceOutorgaTipoInfraestruturaService {

	@Inject
	private IDAO<FceOutorgaTipoInfraestrutura> fceOutorgaTipoInfraestruturaDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceOutorgaTipoInfraestrutura> listarTodosFceOutorgaTipoInfraestrutura() {
		
		return fceOutorgaTipoInfraestruturaDAO.listarPorCriteria(
				DetachedCriteria.forClass(FceOutorgaTipoInfraestrutura.class).add(Restrictions.eq("indExcluido", false)));
	}
}
