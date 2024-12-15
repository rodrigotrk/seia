package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoEstadoConservacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoEstadoConservacaoService {
	
	@Inject
	IDAO<TipoEstadoConservacao> dao;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<TipoEstadoConservacao> listarTipoEstadoConservacao() {
		return  dao.listarTodos();
	}

}
