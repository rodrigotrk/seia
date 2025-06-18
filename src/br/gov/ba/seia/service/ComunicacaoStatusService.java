package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ComunicacaoStatusDAO;
import br.gov.ba.seia.entity.ComunicacaoStatus;
import br.gov.ba.seia.enumerator.ComunicacaoStatusEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoStatusService {
	
	@Inject
	ComunicacaoStatusDAO comunicacaoStatusDAO;
	
	
	public ComunicacaoStatus findByComunicacao(ComunicacaoStatusEnum comunicacao)  {
		return comunicacaoStatusDAO.findByID(comunicacao.getIdComunicacaoStatus());
	}
	
	public List<ComunicacaoStatus> findAll() {
		return comunicacaoStatusDAO.findAll();
	}

}
