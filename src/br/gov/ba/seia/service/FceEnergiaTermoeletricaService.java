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
import br.gov.ba.seia.entity.FceEnergia;
import br.gov.ba.seia.entity.FceEnergiaTermoEletrica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaTermoeletricaService {
	
	@Inject
	private IDAO<FceEnergiaTermoEletrica> fceEnergiaTermoeletricaIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergiaTermoEletrica fceEnergiaTermo) {
		this.fceEnergiaTermoeletricaIDAO.salvarOuAtualizar(fceEnergiaTermo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceEnergiaTermoEletrica buscarTermoeletricaByFceEnergia(FceEnergia fceEnergia)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergiaTermoEletrica.class)		
		.add(Restrictions.eq("ideFceEnergia.ideFceEnergia", fceEnergia.getIdeFceEnergia())); 
		
		return fceEnergiaTermoeletricaIDAO.buscarPorCriteria(detachedCriteria);

	}
}
