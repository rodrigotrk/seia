/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.service
 * @autor: diegoraian em 11 de set de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.hibernate.Hibernate;

import br.gov.ba.seia.dao.CondicionanteAmbientalDAOImpl;
import br.gov.ba.seia.dao.CondicionanteAtoAmbientalDAOImpl;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.CondicionanteAmbiental;
import br.gov.ba.seia.entity.CondicionanteAtoAmbiental;
import br.gov.ba.seia.entity.CondicionanteAtoAmbientalPK;
import br.gov.ba.seia.entity.Segmento;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: CondicionanteService.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.service
 * @autor: diegoraian em 11 de set de 2017
 * Objetivo: 	
	
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CondicionanteService {

	/**
	 * Propriedade: daoComunicacao
	 * @type: IDAO<CondicionanteAmbiental>
	 */
	@EJB
	CondicionanteAmbientalDAOImpl daoCondicionanteAmbiental;
	
	
	/**
	 * Propriedade: daoCondicionanteAtoAmbiental
	 * @type: IDAO<CondicionanteAtoAmbiental>
	 */
	@EJB
	CondicionanteAtoAmbientalDAOImpl daoCondicionanteAtoAmbiental;
	
	/**
	 * Propriedade: atoAmbientalService
	 * @type: AtoAmbientalService
	 */
	@EJB
	AtoAmbientalService atoAmbientalService;
	
	/**
	 * Propriedade: segmentoService
	 * @type: SegmentoService
	 */
	@EJB
	SegmentoService segmentoService;
	
	/**
	 *
	 * @return
	 * @
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtoAmbiental> listarAtosAmbientais() {
		return atoAmbientalService.listarTodosAtos();
	}
	
	/**
	 *
	 * @return
	 * @
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Segmento> listarTodosSegmentos() {
		return (List<Segmento>) segmentoService.listarTodosSegmentos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CondicionanteAmbiental> listarTodosCondicionantes(CondicionanteAmbiental item) {
		return daoCondicionanteAmbiental.listarTodos(item);
	}

	/**
	 *
	 * @param itemSelecionado
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void salvar(CondicionanteAmbiental itemSelecionado,List<AtoAmbiental> atosAmbientaisSelecionados) {
		List<CondicionanteAtoAmbiental> condicionantes = new ArrayList<CondicionanteAtoAmbiental>();
		itemSelecionado.setIndExcluido(Boolean.FALSE);	
		daoCondicionanteAmbiental.salvar(itemSelecionado);
		
		for (AtoAmbiental atoAmbiental : atosAmbientaisSelecionados) {
			CondicionanteAtoAmbiental condicionanteAto  = new CondicionanteAtoAmbiental();
			CondicionanteAtoAmbientalPK pk =  new CondicionanteAtoAmbientalPK(atoAmbiental.getIdeAtoAmbiental(), 
					itemSelecionado.getIdeCondicionante());
			condicionanteAto.setCondicionanteAtoAmbientalPK(pk);
			condicionanteAto.setIdeAtoAmbiental(atoAmbiental);
			condicionanteAto.setIdeCondicionante(itemSelecionado);
			daoCondicionanteAtoAmbiental.salvar(condicionanteAto);
			condicionantes.add(condicionanteAto);
		}
		Hibernate.initialize(itemSelecionado.getCondicionanteAtoAmbientalCollection());
		itemSelecionado.setCondicionanteAtoAmbientalCollection(condicionantes);
		
		daoCondicionanteAmbiental.atualizar(itemSelecionado);
		

	}
	
	/**
	 * Verifica se existe uma condicionante cadastrada com o nome 
	 * @param itemSelecionado
	 * @throws Exception 
	 * @
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void verificarRegistroExistente(CondicionanteAmbiental itemSelecionado) throws Exception {
		//Verifica se a condicionante já foi cadastrada
		if(daoCondicionanteAmbiental.consultarCondicionanteExistente(itemSelecionado)){
			throw new Exception("Condicionante já foi cadastrada");
		}
	}
	
	/**
	 * Atualiza o registro já cadastrado removendo sempre todos os itens de atos ambientais
	 * @param itemSelecionado
	 * @param atosAmbientaisSelecionados
	 * @
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void atualizar(CondicionanteAmbiental itemSelecionado, List<AtoAmbiental> atosAmbientaisSelecionados)  {
		//Remove todos os condicionantes do ato
		for(CondicionanteAtoAmbiental item : itemSelecionado.getCondicionanteAtoAmbientalCollection()){
			daoCondicionanteAtoAmbiental.removerItem(item);
		}
		//Atualiza o objeto do item selecionado
		CondicionanteAmbiental condicionante = daoCondicionanteAmbiental.carregarGet(itemSelecionado.getIdeCondicionante());
		condicionante.setNomCondicionante(itemSelecionado.getNomCondicionante());
		condicionante.setIdeSegmento(itemSelecionado.getIdeSegmento());
		
		List<CondicionanteAtoAmbiental> condicionantes = new ArrayList<CondicionanteAtoAmbiental>();
		for (AtoAmbiental atoAmbiental : atosAmbientaisSelecionados) {
			CondicionanteAtoAmbiental condicionanteAto  = new CondicionanteAtoAmbiental();
			CondicionanteAtoAmbientalPK pk =  new CondicionanteAtoAmbientalPK(atoAmbiental.getIdeAtoAmbiental(), 
					itemSelecionado.getIdeCondicionante());
			condicionanteAto.setCondicionanteAtoAmbientalPK(pk);
			condicionanteAto.setIdeAtoAmbiental(atoAmbiental);
			condicionanteAto.setIdeCondicionante(condicionante);
			daoCondicionanteAtoAmbiental.salvar(condicionanteAto);
			condicionantes.add(condicionanteAto);
		}
		daoCondicionanteAmbiental.atualizar(condicionante);
	}
	/***
	 * 
	 *
	 * @param itemSelecionado
	 * @
	 * @author diegoraian
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CondicionanteAmbiental itemSelecionado) {
		itemSelecionado.setIndExcluido(Boolean.TRUE);
		daoCondicionanteAmbiental.atualizar(itemSelecionado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CondicionanteAmbiental carregarCondicionante(CondicionanteAmbiental condicionante) {
		return daoCondicionanteAmbiental.carregarGet(condicionante.getIdeCondicionante());
	}	
	
}
