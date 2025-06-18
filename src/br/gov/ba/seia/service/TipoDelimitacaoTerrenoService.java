package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipoDelimitacaoTerrenoDAOImpl;
import br.gov.ba.seia.entity.TipoDelimitacaoTerreno;

/***
 * 
 * @author luis
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoDelimitacaoTerrenoService {
	
	@Inject
	TipoDelimitacaoTerrenoDAOImpl tipoDelimitacaoTerrenoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoDelimitacaoTerreno> listar() throws Exception {
		return this.tipoDelimitacaoTerrenoDAOImpl.listar();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoDelimitacaoTerreno carregar(Integer pIdeTipoDelimitacaoTerreno) throws Exception {
		return this.tipoDelimitacaoTerrenoDAOImpl.carregar(pIdeTipoDelimitacaoTerreno);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoDelimitacaoTerreno> carregarByIdeLacErb(Integer pIdeLacErb) throws Exception {
		return this.tipoDelimitacaoTerrenoDAOImpl.carregarByIdeLacErb(pIdeLacErb);
	}
	
}
