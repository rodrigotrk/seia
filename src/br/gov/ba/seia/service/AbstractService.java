package br.gov.ba.seia.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.generic.AbstractEntity;

public abstract class AbstractService <T extends AbstractEntity> implements Serializable {
	private static final long serialVersionUID = 1L;

	public abstract AbstractDAO<T> dao();
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public T buscar(T t) {
		return dao().buscar(t);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<T> listar(){
		return dao().listar();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(T t) {
		dao().salvar(t);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(T t) throws Exception{
		dao().excluir(t);
	}
	
}
