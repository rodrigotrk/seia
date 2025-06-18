package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.LegislacaoDAOImpl;
import br.gov.ba.seia.entity.Legislacao;

/***
 * 
 * @author luis
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LegislacaoService {

	@Inject
	LegislacaoDAOImpl legislacaoDAOImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Legislacao> listar(Integer ideTipo)  {		
		return this.legislacaoDAOImpl.listarByIdeTipo(ideTipo);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Legislacao carregar(Integer pIde)  {
		return this.legislacaoDAOImpl.carregar(pIde);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Legislacao carregarByTipo(Integer pIde)  {
		return this.legislacaoDAOImpl.carregarByTipo(pIde);
	}

}
