/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.service
 * @autor: diegoraian em 5 de out de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipoNaturezaTorreDAOImpl;
import br.gov.ba.seia.dao.TipoUnidadeConservacaoTorreDAOImpl;
import br.gov.ba.seia.entity.TipoNaturezaTorre;
import br.gov.ba.seia.entity.TipoUnidadeConservacaoTorre;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: TipoNaturezaTorreService.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.service
 * @autor: diegoraian em 5 de out de 2017
 * Objetivo: 	
	
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoUnidadeConservacaoTorreService {

	@Inject
	TipoUnidadeConservacaoTorreDAOImpl daoTipoUnidadeConservacaoTorre;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipoUnidadeConservacaoTorre> listar() throws Exception{
		return daoTipoUnidadeConservacaoTorre.listar();
	}
}
