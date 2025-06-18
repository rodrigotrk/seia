/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 6 de out de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.LocalizacaoAtividadeTorre;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: LocalizacaoAtividadeTorreDAOImpl.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 6 de out de 2017
 * Objetivo: 	
	
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LocalizacaoAtividadeTorreDAOImpl extends BaseDAO<LocalizacaoAtividadeTorre> {

	@Inject
	private IDAO<LocalizacaoAtividadeTorre> dao;
	
	public Collection<LocalizacaoAtividadeTorre> listar() {
		return getDao().buscarPorNamedQuery("LocalizacaoAtividadeTorre.findAll");
	}
	
	@Override
	protected IDAO<LocalizacaoAtividadeTorre> getDao() {
		return dao;
	}

}
