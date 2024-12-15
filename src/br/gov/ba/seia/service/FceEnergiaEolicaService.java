package br.gov.ba.seia.service;
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
import br.gov.ba.seia.entity.FceEnergia;
import br.gov.ba.seia.entity.FceEnergiaEolica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaEolicaService {
	
	@Inject
	private IDAO<FceEnergiaEolica> fceEnergiaEolicaIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergiaEolica fceEnergiaEolica) {
		this.fceEnergiaEolicaIDAO.salvarOuAtualizar(fceEnergiaEolica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceEnergiaEolica buscarFceEnergiaEolicaByFceEnergia(FceEnergia fceEnergia)  {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergiaEolica.class)	
		.createAlias("ideFceEnergia.ideLocalizacaoGeografica", "ideLocalizacaoGeografica",JoinType.LEFT_OUTER_JOIN)
		.add(Restrictions.eq("ideFceEnergia.ideFceEnergia", fceEnergia.getIdeFceEnergia())); 
		
		return fceEnergiaEolicaIDAO.buscarPorCriteria(detachedCriteria);

	}
}
