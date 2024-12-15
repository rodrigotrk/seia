package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

public abstract class BaseDAO<T> implements IBaseDAO<T>{

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(T objeto)  {
		this.getDao().salvar(objeto);		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(T objeto)  {
		this.getDao().salvarOuAtualizar(objeto);		
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
	public void salvar(Collection<T> objetos)  {
		for (T objeto : objetos) {
			this.salvar(objeto);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(T objeto)  {
		this.getDao().atualizar(objeto);		
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(T objeto)  {
		this.getDao().remover(objeto);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public T carregarByIde(Integer ide){
		return this.getDao().carregarGet(ide);
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<T> listar() {
		return this.getDao().listarTodos();
	}

	public void remover(Collection<T> objetos)  {
		for (T objeto : objetos) {
			this.getDao().remover(objeto);			
		}
	}
	
	protected abstract IDAO<T> getDao();

}
