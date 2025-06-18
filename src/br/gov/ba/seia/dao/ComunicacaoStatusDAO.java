package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.ComunicacaoStatus;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoStatusDAO {
	
	@Inject
	private IDAO<ComunicacaoStatus> dao;
	
	
	public ComunicacaoStatus findByID(Integer id) {
		return dao.carregarGet(id);
		
	}
	
	public List<ComunicacaoStatus> findAll() {
		return dao.listarTodos();
	}
	
	
	

}
