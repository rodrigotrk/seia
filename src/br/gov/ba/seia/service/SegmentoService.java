package br.gov.ba.seia.service;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.SegmentoDAOImpl;
import br.gov.ba.seia.entity.Segmento;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: SegmentoService.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.service
 * @autor: diegoraian em 6 de set de 2017
 * Objetivo: 	
	
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SegmentoService {
	
	@Inject
	SegmentoDAOImpl dao;
	/**
	 *
	 * @return
	 * @
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Segmento> listarTodosSegmentos()  {
		return dao.listarTodos();
	}
	
	
	/**
	 *
	 * @param segmento
	 * @return
	 * @
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Segmento> preencherDataList(Segmento segmento) {  
		return dao.buscarSegmentosPorFiltro(segmento);
	}
	
	/**
	 *
	 * @param segmento
	 * @
	 * @author diegoraian
	 */
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Segmento segmento) {
		segmento.setIndExcluido(Boolean.FALSE);
		dao.salvar(segmento);
	}
	
	/**
	 *
	 * @param segmento
	 * @
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void atualizar(Segmento segmento) {
		dao.atualizar(segmento);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void validarExistencia(Segmento segmento) throws Exception{
		if(dao.consultarSegmentoCadastrado(segmento)){
			throw new Exception("Segmento já cadastrado");
		}
	}
	/**
	 *
	 * @param segmento
	 * @
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Segmento segmento) {
		segmento.setIndExcluido(Boolean.TRUE);
		dao.atualizar(segmento);
	}
	
	

}
