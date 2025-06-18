package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoEquipamentoEntornoPosto;

public class TipoEquipamentoEntornoPostoDAOImpl extends BaseDAO<TipoEquipamentoEntornoPosto> {
	
	@Inject
	IDAO<TipoEquipamentoEntornoPosto> tipoEquipamentoEntornoPostoDAO;
	
	public Collection<TipoEquipamentoEntornoPosto> carregarByIdeLac(Integer ideLac) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("pIdeLac", ideLac);
		return this.tipoEquipamentoEntornoPostoDAO.buscarPorNamedQuery("TipoEquipamentoEntornoPosto.findByIdeLac", parametros);
	}
	
	@Override
	protected IDAO<TipoEquipamentoEntornoPosto> getDao() {
		return this.tipoEquipamentoEntornoPostoDAO;
	}

	public void remover(Integer ideLac, Integer idEquipamentoEntorno)  {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("pIdeLac", ideLac);
		parametros.put("pIdeEquipamento", idEquipamentoEntorno);
		this.tipoEquipamentoEntornoPostoDAO.executarNamedQuery("TipoEquipamentoEntornoPosto.removerByIdeLac", parametros);
	}


}
