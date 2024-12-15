package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoRecuperacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoRecuperacaoService {

	@Inject
	IDAO<TipoRecuperacao> dao;
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<TipoRecuperacao> listarTipoRecuperacao() {
		return  dao.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public TipoRecuperacao carregaByIdeTipoRecuperacao(Integer id){
		return  dao.carregarGet(id);
	}
}