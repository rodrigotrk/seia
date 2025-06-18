/**
 * 		29/01/14
 * @author eduardo.fernandes
 */
package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAtividadeArea;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAtividadeAreaService {

	@Inject
	private IDAO<FceAtividadeArea> fceAtividadeAreaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAtividadeArea> buscarListaFceAtividadeAreaByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAtividadeArea.class);
		criteria.createAlias("ideFce", "f");
		criteria.createAlias("f.ideAnaliseTecnica", "at", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("f.ideDadoOrigem", "of", JoinType.INNER_JOIN);
		criteria.createAlias("ideTipologiaAtividade", "tipologia");
		criteria.createAlias("ideMetodoIrrigacao", "mi", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideTipoPeriodoDerivacao", "tpd", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("f.ideFce", fce.getIdeFce()));
		List<FceAtividadeArea> listaTemp = fceAtividadeAreaIDAO.listarPorCriteria(criteria);
		for(FceAtividadeArea fceAtividadeArea : listaTemp){
			fceAtividadeArea.setConfirmado(true);
		}
		return listaTemp;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceAtividadeArea(Fce fce, List<FceAtividadeArea> listaFceAtividadeArea) {
		for(FceAtividadeArea fceAtividadeArea : listaFceAtividadeArea){
			if(fceAtividadeArea.getIdeTipologiaAtividade().getDscTipologiaAtividade().compareTo("Outros") != 0){
				fceAtividadeArea.setIdeFce(fce);
				if(!Util.isNullOuVazio(fceAtividadeArea.getIdeMetodoIrrigacao())){
					fceAtividadeArea.setIndOutrosMetodo(fceAtividadeArea.getIdeMetodoIrrigacao().isOutros());
					if(fceAtividadeArea.getIndOutrosMetodo()){
						fceAtividadeArea.setIdeMetodoIrrigacao(null);
					}
				}
				fceAtividadeAreaIDAO.salvarOuAtualizar(fceAtividadeArea);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceAtividadeAreaByIdeFce(Fce fce) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFce", fce.getIdeFce());
		fceAtividadeAreaIDAO.executarNamedQuery("FceAtividadeArea.removeByIdeFce",parameters);
	}
}