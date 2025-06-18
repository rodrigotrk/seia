/**
 * 		14/05/14
 * @author eduardo.fernandes
 */
package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.controller.FceIndustriaController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceIndustria;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIndustriaService {

	@Inject
	private IDAO<FceIndustria> fceIndustriaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceIndustria buscarFceIndustriaByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIndustria.class);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		return fceIndustriaIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceIndustria(FceIndustria fceIndustria) {
		fceIndustriaIDAO.salvarOuAtualizar(fceIndustria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceIndustriaController ctrl) throws Exception  {
		ctrl.prepararParaFinalizar();
	}
}