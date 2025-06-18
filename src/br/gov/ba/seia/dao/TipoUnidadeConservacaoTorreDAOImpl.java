/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 5 de out de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoUnidadeConservacaoTorre;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: TipoUnidadeConservacaoTorreDAOImpl.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 5 de out de 2017
 * Objetivo: 	
	
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoUnidadeConservacaoTorreDAOImpl extends BaseDAO<TipoUnidadeConservacaoTorre>{

	
	@Inject
	IDAO<TipoUnidadeConservacaoTorre> dao;
	/**
	 *
	 * @return
	 * @author diegoraian
	 * @throws Exception 
	 */
	public Collection<TipoUnidadeConservacaoTorre> listar() {
		return dao.buscarPorNamedQuery("TipoUnidadeConservacaoTorre.findAll");
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.dao.BaseDAO#getDao()
	 */
	@Override
	protected IDAO<TipoUnidadeConservacaoTorre> getDao() {
		return dao;
	}

}
