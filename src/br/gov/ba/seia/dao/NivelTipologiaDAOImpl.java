package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import br.gov.ba.seia.entity.NivelTipologia;


public class NivelTipologiaDAOImpl {
	
	@Inject
	IDAO<NivelTipologia> nivelTipologiaDAO;
	
	
	public NivelTipologia carregarNivelTipologia(Integer id){
		return nivelTipologiaDAO.carregarGet(id) ;
	}

	public NivelTipologia filtrarNivelTipologia(Integer id)  {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("numNivelTipologia", id);
		return nivelTipologiaDAO.buscarEntidadePorNamedQuery("NivelTipologia.findByNumNivelTipologia", params);
	}
	
	public Collection<NivelTipologia> listaNivelTipologia() {
		return nivelTipologiaDAO.listarTodos();
	}


}