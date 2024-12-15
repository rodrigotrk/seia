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

import br.gov.ba.seia.controller.FceIntervencaoAbastecimentoHumanoController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAbastecimentoHumano;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIntervencaoAbastecimentoHumanoService {

	@Inject
	private IDAO<FceAbastecimentoHumano> fceAbastecimentoHumanoDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceAbastecimentoHumano(FceAbastecimentoHumano fceAbastecimentoHumano) {
		fceAbastecimentoHumanoDAO.salvarOuAtualizar(fceAbastecimentoHumano);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAbastecimentoHumano buscarFceAbsHumanoByIdeFce (Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAbastecimentoHumano.class)
				.setFetchMode("ideTipoPeriodoDerivacao", FetchMode.JOIN)
				.createAlias("ideFce", "f")
				.add(Restrictions.eq("f.ideFce", fce.getIdeFce()));
		return fceAbastecimentoHumanoDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceIntervencaoAbastecimentoHumanoController ctrl) throws Exception  {
		ctrl.prepararParaFinalizar();
	}
}
