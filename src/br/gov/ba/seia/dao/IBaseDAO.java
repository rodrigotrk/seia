package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.TransactionAttribute;

public interface IBaseDAO<T> {

	@TransactionAttribute
	void salvar(T objeto) ;

	void salvar(Collection<T> objetos) ;

	void atualizar(T objeto) ;

	void remover(T objeto) ;

	T carregarByIde(Integer ide) ;

	Collection<T> listar() ;

	void salvarOuAtualizar(T objeto) ;

	void salvarOuAtualizar(Collection<T> objetos) ;

	void remover(Collection<T> objetos) ;

}
