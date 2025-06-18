package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.AtoAmbientalTipologiaDAOimpl;
import br.gov.ba.seia.entity.AtoAmbientalTipologia;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtoAmbientalTipologiaService {

	@Inject
	AtoAmbientalTipologiaDAOimpl ambientalTipologiaDAOimpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<AtoAmbientalTipologia> listarAtoAmbientalTipologiaPorDemanda(int ideAtoAmbiental,int first, int pageSize) {	
		return this.ambientalTipologiaDAOimpl.listarAtoAmbientalTipologiaPorDemanda(ideAtoAmbiental, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Integer countAtoAmbientalTipologia (int ideAtoAmbiental) {	
		return this.ambientalTipologiaDAOimpl.countAtoAmbientalTipologia(ideAtoAmbiental);
	}
	public Collection<TipoFinalidadeUsoAgua> buscarFinalidadePorTipologia(int ideTipologia) {
		return this.ambientalTipologiaDAOimpl.buscarFinalidadePorTipologia(ideTipologia);
	}

}
