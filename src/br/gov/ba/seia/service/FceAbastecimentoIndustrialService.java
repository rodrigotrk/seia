package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.controller.FceAbastecimentoIndustrialController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAbastecimentoIndustrial;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAbastecimentoIndustrialService {

	@Inject
	private IDAO<FceAbastecimentoIndustrial> fceAbastecimentoIndustrialDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceAbastecimentoIndustrial(FceAbastecimentoIndustrial fceAbastecimentoIndustrial) {
		fceAbastecimentoIndustrialDAO.salvarOuAtualizar(fceAbastecimentoIndustrial);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAbastecimentoIndustrial buscarFceAbsIndustrialByIdeFce (Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAbastecimentoIndustrial.class);
		criteria.add(Restrictions.eq("ideFce", fce));
		criteria.setFetchMode("ideTipoPeriodoDerivacao", FetchMode.JOIN);
		return fceAbastecimentoIndustrialDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceAbastecimentoIndustrialController ctrl) throws Exception  {
		ctrl.prepararParaFinalizar();
	}
}
