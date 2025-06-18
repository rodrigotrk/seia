package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLinhaEnergia;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLinhaEnergiaDAOImpl {
	
	@Inject
	IDAO<FceLinhaEnergia> fceLinhaEnergiaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(FceLinhaEnergia fceLinhaEnergia) {
		
		if (Util.isNull(fceLinhaEnergia.getIdeFceLinhaEnergia())) {
			fceLinhaEnergiaDAO.salvar(fceLinhaEnergia);
		} else {
			fceLinhaEnergiaDAO.merge(fceLinhaEnergia);
		}
	}
	
	public FceLinhaEnergia buscarFceLinhaEnergiaPorFce(Fce fce)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLinhaEnergia.class, "fceLE")
			.add(Restrictions.eq("fceLE.ideFce", fce))
			.add(Restrictions.eq("indExcluido", false));
		
		return fceLinhaEnergiaDAO.buscarPorCriteria(criteria);
	}
}
