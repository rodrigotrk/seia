/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.service
 * @autor: diegoraian em 6 de out de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.LocalizacaoAtividadeTorreDAOImpl;
import br.gov.ba.seia.entity.LocalizacaoAtividadeTorre;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: LocalizacaoAtividadeTorreService.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.service
 * @autor: diegoraian em 6 de out de 2017
 * Objetivo: 	
	
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LocalizacaoAtividadeTorreService {
	
	@Inject
	private LocalizacaoAtividadeTorreDAOImpl dao;
	
	
	public Collection<LocalizacaoAtividadeTorre> listarLocalizacoesAtividadeTorre()  {
		return dao.listar();
	}
	
}
