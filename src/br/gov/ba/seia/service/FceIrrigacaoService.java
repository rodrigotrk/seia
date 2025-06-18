/**
 * 		05/02/14
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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceIrrigacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIrrigacaoService {

	@Inject
	private IDAO<FceIrrigacao> fceIrrigacaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceIrrigacao buscarFceIrrigacaoByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceIrrigacao.class);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		criteria.createAlias("ideTipologiaIrrigacao", "tipoIrrigacao");
		criteria.createAlias("ideTipoPeriodoDerivacao", "periodoDerivacao");
		return fceIrrigacaoIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceIrrigacao(FceIrrigacao fceIrrigacao) {
		fceIrrigacaoIDAO.salvarOuAtualizar(fceIrrigacao);
	}
}