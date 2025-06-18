package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Coordenacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CoordenacaoService {
	
	@Inject
	IDAO<Coordenacao> coordenacaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Coordenacao> listarCoordenacao() {
		return coordenacaoDAO.listarTodos();
	}
}
