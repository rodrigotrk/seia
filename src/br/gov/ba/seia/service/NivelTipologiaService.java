package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.NivelTipologiaDAOImpl;
import br.gov.ba.seia.entity.NivelTipologia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NivelTipologiaService {

	@Inject
	NivelTipologiaDAOImpl nivelTipologiaDAOImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NivelTipologia carregarNivelTipologia(Integer id)  {
		return nivelTipologiaDAOImpl.carregarNivelTipologia(id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NivelTipologia filtrarNivelTipologia(Integer id)  {
		return nivelTipologiaDAOImpl.filtrarNivelTipologia(id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<NivelTipologia> listaNivelTipologia()  {
		return nivelTipologiaDAOImpl.listaNivelTipologia();
	}

}
