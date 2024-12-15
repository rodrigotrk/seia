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
import br.gov.ba.seia.entity.FceEnergiaEolicaLicencaPrevia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaEolicaLicencaPreviaService {
	
	@Inject
	private IDAO<FceEnergiaEolicaLicencaPrevia> fceEnergiaEolicaLicencaPreviaIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergiaEolicaLicencaPrevia fceEnergiaEolicaLicencaPrevia) {
		this.fceEnergiaEolicaLicencaPreviaIDAO.salvarOuAtualizar(fceEnergiaEolicaLicencaPrevia);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceEnergiaEolicaLicencaPrevia buscarFceEnergiaEolicaLicencaPrevia(Integer ideFceEnergiaEolica) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceEnergiaEolicaLicencaPrevia.class);
		
		criteria.add(Restrictions.eq("fceEnergiaEolica.ideFceEnergiaEolica",  ideFceEnergiaEolica));
		
		return fceEnergiaEolicaLicencaPreviaIDAO.buscarPorCriteria(criteria);
	}
}
