package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceEnergia;
import br.gov.ba.seia.entity.FceEnergiaFinalidade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaFinalidadeService {
	
	@Inject
	private IDAO<FceEnergiaFinalidade> fceEnergiaFinalidadeIDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceEnergiaFinalidade> listarFinalidadesByFceEnergia(FceEnergia fceEnergia) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergiaFinalidade.class)		
		.add(Restrictions.eq("ideFceEnergia.ideFceEnergia", fceEnergia.getIdeFceEnergia())); 
		
		return this.fceEnergiaFinalidadeIDAO.listarPorCriteria(detachedCriteria, Order.asc("finalidadeGeracaoEnergia"));
	}
}
