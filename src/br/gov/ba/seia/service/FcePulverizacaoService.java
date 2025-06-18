/**
 * 		29/01/14
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

import br.gov.ba.seia.controller.FcePulverizacaoController;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FcePulverizacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePulverizacaoService {

	@Inject
	private IDAO<FcePulverizacao> fcePulverizacaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FcePulverizacao buscarFcePulverizacaoByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FcePulverizacao.class);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		criteria.createAlias("tipologiaPulverizacao", "tipoPulver");
		return fcePulverizacaoIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFcePulverizacao(FcePulverizacao fcePulverizacao) {
		fcePulverizacaoIDAO.salvarOuAtualizar(fcePulverizacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(FcePulverizacaoController ctrl) throws Exception  {
		ctrl.prepararParaFinalizar();
	}
}