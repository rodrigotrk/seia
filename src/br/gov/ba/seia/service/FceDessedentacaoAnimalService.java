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

import br.gov.ba.seia.controller.FceDessedentacaoAnimalController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceDessedentacaoAnimal;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceDessedentacaoAnimalService {

	@Inject
	private IDAO<FceDessedentacaoAnimal> fceDessedentacaoAnimalDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceDessedentacaoAnimal(FceDessedentacaoAnimal fceDessedentacaoAnimal) {
		fceDessedentacaoAnimalDAO.salvarOuAtualizar(fceDessedentacaoAnimal);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceDessedentacaoAnimal buscarFceDesAnimalByIdeFce (Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceDessedentacaoAnimal.class);
		criteria.add(Restrictions.eq("ideFce", fce));
		criteria.setFetchMode("ideTipoPeriodoDerivacao", FetchMode.JOIN);
		return fceDessedentacaoAnimalDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FceDessedentacaoAnimalController ctrl) throws Exception  {
		ctrl.prepararParaFinalizar();
	}
}
