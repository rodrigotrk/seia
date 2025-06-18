package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoDelimitacaoTerreno;

/***
 * 
 * @author luis
 *
 */
public class TipoDelimitacaoTerrenoDAOImpl {

	@Inject
	IDAO<TipoDelimitacaoTerreno> daoTipoDelimitacaoTerreno;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoDelimitacaoTerreno> listar() {
		return daoTipoDelimitacaoTerreno.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoDelimitacaoTerreno carregar(Integer pIdeTipoDelimitacaoTerreno){
		return this.daoTipoDelimitacaoTerreno.carregarGet(pIdeTipoDelimitacaoTerreno);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoDelimitacaoTerreno> carregarByIdeLacErb(Integer pIdeLacErb) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("pIdeLacErb", pIdeLacErb);
		return this.daoTipoDelimitacaoTerreno.buscarPorNamedQuery("TipoDelimitacaoTerreno.findByIdeLacErb", parametros);
	}
	
}
