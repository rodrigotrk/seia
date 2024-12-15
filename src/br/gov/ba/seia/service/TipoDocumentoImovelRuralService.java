package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.HashMap;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoDocumentoImovelRural;



@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoDocumentoImovelRuralService {

	@Inject
	IDAO<TipoDocumentoImovelRural> daoTipoDocumentoImovelRural;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<TipoDocumentoImovelRural> findByIdeTipoVinculoImovel(Integer ideTipoVinculoImovel) {
		HashMap< String, Object> params = new HashMap<String, Object>();
		params.put("ideTipoVinculoImovel", ideTipoVinculoImovel);
		Collection<TipoDocumentoImovelRural> listaMunicipio = daoTipoDocumentoImovelRural.buscarPorNamedQuery("TipoDocumentoImovelRural.findByIdeTipoVinculoImovel", params);
		return listaMunicipio;
	}
	
	

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public TipoDocumentoImovelRural findByIdeTipoDocumentoImovelRural(Integer ideTipoDocumentoImovelRural) throws Exception {
		HashMap< String, Object> params = new HashMap<String, Object>();
		params.put("ideTipoDocumentoImovelRural", ideTipoDocumentoImovelRural);
		TipoDocumentoImovelRural tipoDocumentoImovelRural = daoTipoDocumentoImovelRural.buscarEntidadePorNamedQuery("TipoDocumentoImovelRural.findByIdeTipoDocumentoImovelRural", params);
		return tipoDocumentoImovelRural;
	}
}
