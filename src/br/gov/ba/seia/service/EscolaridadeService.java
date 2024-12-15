package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.EscolaridadeDAOImpl;
import br.gov.ba.seia.entity.Escolaridade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EscolaridadeService {
	
	
	@Inject
	private EscolaridadeDAOImpl escolaridadeDAO;
	
	
	public List<Escolaridade> listarTodos() { 
		return escolaridadeDAO.listarTodos();
	}
	
	public List<Escolaridade> listarEscolaridadeResponsavelTecnico() { 
		return escolaridadeDAO.listarEscolaridadeResponsavelTecnico();
	}

}
