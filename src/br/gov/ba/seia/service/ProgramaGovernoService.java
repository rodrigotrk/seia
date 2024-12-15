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
import br.gov.ba.seia.entity.ProgramaGoverno;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProgramaGovernoService {

	@Inject
	private IDAO<ProgramaGoverno> programaGovernoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ProgramaGoverno findByID(int ideProgramaGoverno)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProgramaGoverno.class);
		criteria.add(Restrictions.eq("ideProgramaGoverno", ideProgramaGoverno));
		return programaGovernoIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProgramaGoverno> findAll()  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProgramaGoverno.class);
		return programaGovernoIDAO.listarPorCriteria(criteria);
	}
}