package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.TipoVinculoPCTDAOImpl;
import br.gov.ba.seia.entity.TipoVinculoPCT;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoVinculoPCTService {

	@EJB
	private TipoVinculoPCTDAOImpl tipoVinculoPCTDAOImpl;
	
	public List<TipoVinculoPCT> listarTipoVinculoPCT() throws Exception{
		
		return tipoVinculoPCTDAOImpl.listarTipoVinculoPCT();
	}
	
	
}
