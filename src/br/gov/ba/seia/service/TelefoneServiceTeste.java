package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Telefone;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TelefoneServiceTeste {

	@Inject
	private IDAO<Telefone> telefoneDao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Telefone> search(final Telefone filtro) {
		
		return telefoneDao.listarPorExemplo(filtro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void save(final Telefone telefone) throws Exception {
		
		telefoneDao.salvar(telefone);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void edit(final Telefone telefone) throws Exception {
		
		telefoneDao.atualizar(telefone);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void delete(final Telefone telefone) throws Exception {
		
		telefoneDao.remover(telefone);
	}
	
}
