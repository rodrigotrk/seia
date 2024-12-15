package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CaepogComunicacaoDAO;
import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.CaepogComunicacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaepogComunicacaoService {
	
	@Inject
	CaepogComunicacaoDAO caepogComunicacaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogComunicacao> listarComunicacaoPorCaepog(Caepog caepog)  {
		return caepogComunicacaoDAO.listarComunicacaoPorCaepog(caepog);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CaepogComunicacao caepogComunicacao)  {
		caepogComunicacaoDAO.salvar(caepogComunicacao);
	}
}