package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoAreaMunicipio;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoAreaMunicipioService {
	
	@Inject
	IDAO<TipoAreaMunicipio> tipoAreaMunicipioDAO;	

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoAreaMunicipio> listarTipoAreaMunicipio() {
		return tipoAreaMunicipioDAO.listarTodos();
		
	}
}
