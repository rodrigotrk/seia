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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceEnergiaEolicaParque;
import br.gov.ba.seia.entity.FceEnergiaEolicaParqueAerogerador;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceEnergiaEolicaParqueAerogeradorService {
	
	@Inject
	private IDAO<FceEnergiaEolicaParqueAerogerador> fceEnergiaEolicaParqueAerogeradorIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceEnergiaEolicaParqueAerogerador fceEnergiaEolicaParqueAerogerador) {
		this.fceEnergiaEolicaParqueAerogeradorIDAO.salvarOuAtualizar(fceEnergiaEolicaParqueAerogerador);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(FceEnergiaEolicaParqueAerogerador fceEnergiaEolicaParqueAerogerador) {
		String deleteSQL = "delete from FCE_ENERGIA_EOLICA_PARQUE_AEROGERADOR where IDE_FCE_ENERGIA_EOLICA_PARQUE_AEROGERADOR = :ideFceEnergiaEolicaParqueAerogerador";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceEnergiaEolicaParqueAerogerador", fceEnergiaEolicaParqueAerogerador.getIdeFceEnergiaEolicaParqueAerogerador()); 
		fceEnergiaEolicaParqueAerogeradorIDAO.executarNativeQuery(deleteSQL, params);	
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceEnergiaEolicaParqueAerogerador> listarAerogeradorByParque(FceEnergiaEolicaParque fceEnergiaEolicaParque) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(FceEnergiaEolicaParqueAerogerador.class)
		.add(Restrictions.eq("fceEnergiaEolicaParque.ideFceEnergiaEolicaParque", fceEnergiaEolicaParque.getIdeFceEnergiaEolicaParque())); 
		
		return this.fceEnergiaEolicaParqueAerogeradorIDAO.listarPorCriteria(detachedCriteria, Order.asc("ideFceEnergiaEolicaParqueAerogerador"));
	}
}
