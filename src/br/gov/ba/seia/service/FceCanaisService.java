package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceCanal;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCanaisService {

	@Inject
	private IDAO<FceCanal> fceCanalIDAO;
	
	public void salvar(FceCanal fceCanal) {
		if(fceCanal.getId() != null){
			fceCanalIDAO.salvar(fceCanal);
		}else{
			fceCanalIDAO.atualizar(fceCanal);
		}
	}
}
