 package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.gov.ba.seia.dao.BaseDAO;


abstract class BaseService<T> implements IBaseService<T> {

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(T objeto) {
		this.getDaoImpl().salvar(objeto);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(T objeto)  {
		this.getDaoImpl().salvarOuAtualizar(objeto);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Collection<T> objetos)  {
		for (T objeto : objetos) {
			this.salvar(objeto);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Collection<T> objetos)  {
		for (T objeto : objetos) {
			this.salvarOuAtualizar(objeto);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(T objeto)  {
		this.getDaoImpl().atualizar(objeto);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(T objeto)  {
		this.getDaoImpl().remover(objeto);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(Collection<T> objetos)  {
		this.getDaoImpl().remover(objetos);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public T carregarByIde(Integer ide)  {
		return this.getDaoImpl().carregarByIde(ide);
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<T> listar()  {
		return this.getDaoImpl().listar();
	}
		
	protected abstract BaseDAO<T> getDaoImpl();
	
}
