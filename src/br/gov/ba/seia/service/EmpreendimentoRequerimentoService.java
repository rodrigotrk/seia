package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.EmpreendimentoRequerimentoDAOImpl;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Requerimento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EmpreendimentoRequerimentoService {
	
	@EJB
	private EmpreendimentoRequerimentoDAOImpl empreendimentoRequerimentoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EmpreendimentoRequerimento buscarEmpreendimentoRequerimento(Requerimento requerimento)  {
		
		return empreendimentoRequerimentoDAOImpl.buscarEmpreendimentoRequerimento(requerimento);
	}	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<EmpreendimentoRequerimento> listarEmpreendimentoRequerimento(Requerimento requerimento)  {
		
		return empreendimentoRequerimentoDAOImpl.listarEmpreendimentoRequerimento(requerimento);
	}	
}