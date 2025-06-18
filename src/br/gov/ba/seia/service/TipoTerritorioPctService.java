package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.TipoTerritorioPctDAOImpl;
import br.gov.ba.seia.entity.TipoDocumentoImovelRural;
import br.gov.ba.seia.entity.TipoTerritorioPct;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoTerritorioPctService {

	@EJB
	private TipoTerritorioPctDAOImpl tipoTerritorioPctDAOImpl;
	
	public TipoTerritorioPct obterTipoTerritorioPct(Integer ideTipoTerritorioPct) throws Exception{
		return tipoTerritorioPctDAOImpl.obterTipoTerritorioPct(ideTipoTerritorioPct);
	}
	
	public List<TipoTerritorioPct> listarTipoTerritorioPct() throws Exception{
		return tipoTerritorioPctDAOImpl.listarTipoTerritorioPct();
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<TipoDocumentoImovelRural> findByIdeTipoVinculoImovelPorTerritorio(Integer ideTipoTerritorioPct) throws Exception {
		return tipoTerritorioPctDAOImpl.findByIdeTipoVinculoImovelPorTerritorio(ideTipoTerritorioPct);
	}
}
