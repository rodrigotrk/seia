package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.SubTipoApp;
import br.gov.ba.seia.entity.TipoApp;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SubTipoAppService {

	@Inject
	IDAO<SubTipoApp> dao;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<SubTipoApp> listarSubTipoAppByTipoApp(TipoApp tipoApp) {
		List<SubTipoApp> listSubTipoApp = new ArrayList<SubTipoApp>();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideTipoApp", tipoApp);
		listSubTipoApp = dao.buscarPorNamedQuery("SubTipoApp.findByTipoApp", parameters);
		
		return listSubTipoApp;
	}

}
