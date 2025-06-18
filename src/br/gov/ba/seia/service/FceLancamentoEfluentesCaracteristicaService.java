/**
 * 		04/04/14
 * @author eduardo.fernandes
 */
package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceLancamentoEfluenteCaracteristica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLancamentoEfluentesCaracteristicaService {

	@Inject
	private IDAO<FceLancamentoEfluenteCaracteristica> fceLancamentoEfluenteCaracteristicaIDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLancamentoEfluenteCaracteristica(List<FceLancamentoEfluenteCaracteristica> lista) {
		for(FceLancamentoEfluenteCaracteristica fceLancamentoEfluenteCaracteristica : lista){
			fceLancamentoEfluenteCaracteristicaIDAO.salvarOuAtualizar(fceLancamentoEfluenteCaracteristica);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLancamentoEfluenteCaracteristica> buscarFceLancamentoEfluenteCaracteristicaByFceLancEflu(FceLancamentoEfluente fceLancamentoEfluente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLancamentoEfluenteCaracteristica.class);
		criteria.add(Restrictions.eq("ideFceLancamentoEfluente", fceLancamentoEfluente));
		criteria.setFetchMode("ideFceLancamentoEfluente", FetchMode.JOIN);
		criteria.setFetchMode("ideCaracteristicaEfluente", FetchMode.JOIN);
		List<FceLancamentoEfluenteCaracteristica> temp = fceLancamentoEfluenteCaracteristicaIDAO.listarPorCriteria(criteria);
		for(FceLancamentoEfluenteCaracteristica efluenteCaracteristica : temp){
			efluenteCaracteristica.setConfirmado(true);
		}
		return temp;
	}
}