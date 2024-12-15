/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 5 de out de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.inject.Inject;

import br.gov.ba.seia.entity.TipoNaturezaTorre;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: TipoNaturezaTorreDAOImpl.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 5 de out de 2017
 * Objetivo: 	
	
 */
public class TipoNaturezaTorreDAOImpl extends BaseDAO< TipoNaturezaTorre>{

	@Inject
	IDAO<TipoNaturezaTorre> dao;

	public Collection<TipoNaturezaTorre> listar(){
		return getDao().buscarPorNamedQuery("TipoNaturezaTorre.findAll");
	}
	
	@Override
	protected IDAO< TipoNaturezaTorre> getDao() {
		return dao;
	}
	
}
