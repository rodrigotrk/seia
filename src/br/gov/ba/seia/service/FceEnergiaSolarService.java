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
import br.gov.ba.seia.entity.FceEnergiaSolar;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaSolarService {
	
	@Inject
	private IDAO<FceEnergiaSolar> fceEnergiaSolarIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergiaSolar fceEnergiaSolar) {
		this.fceEnergiaSolarIDAO.salvarOuAtualizar(fceEnergiaSolar);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceEnergiaSolar buscarFceEnergiaSolarByFceEnergia(FceEnergia fceEnergia)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergiaSolar.class)		
		.add(Restrictions.eq("ideFceEnergia.ideFceEnergia", fceEnergia.getIdeFceEnergia())); 
		
		return fceEnergiaSolarIDAO.buscarPorCriteria(detachedCriteria);

	}
}
