package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipologiaGrupoAreaDAOImpl;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TipologiaGrupoArea;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipologiaGrupoAreaService {

	@Inject
	TipologiaGrupoAreaDAOImpl tipologiaGrupoAreaDAOImpl;	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipologiaGrupoArea filtrarUnidadeMedidaTipologiaGrupoById(TipologiaGrupoArea pTipologiaGrupoArea) throws Exception {
		return tipologiaGrupoAreaDAOImpl.filtrarUnidadeMedidaTipologiaGrupoById(pTipologiaGrupoArea);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTipologiaGrupoArea(TipologiaGrupoArea pTipologiaGrupoArea) throws Exception {
		tipologiaGrupoAreaDAOImpl.salvarTipologiaGrupoArea(pTipologiaGrupoArea);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTipologiaGrupoArea(Collection<TipologiaGrupoArea> pColTipologiaGrupoArea) throws Exception {
		tipologiaGrupoAreaDAOImpl.salvarTipologiaGrupoArea(pColTipologiaGrupoArea);
	}
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarTipologiaGrupoArea(TipologiaGrupoArea pTipologiaGrupoArea) throws Exception {
		tipologiaGrupoAreaDAOImpl.atualizarTipologiaGrupoArea(pTipologiaGrupoArea);
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  Collection<TipologiaGrupoArea> recuperarTipologiaGrupoArea(Integer id ) throws Exception{
		return 	tipologiaGrupoAreaDAOImpl.recuperarTipologiaGrupoArea(id);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarTipologiaGrupoArea(Collection<TipologiaGrupoArea> pTipologiaGrupoArea) throws Exception {
		tipologiaGrupoAreaDAOImpl.deletarTipologiaGrupoArea(pTipologiaGrupoArea);
		Logger.getLogger("Tipologia tipo ato  deletado com sucesso.");
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<TipologiaGrupoArea> filtrarAreaTipologiaGrupo(TipologiaGrupo tipologiaGrupo,Collection<TipologiaGrupoArea> listAreaTipologiaGrupo) throws Exception {
		 return tipologiaGrupoAreaDAOImpl.filtrarAreaTipologiaGrupo(tipologiaGrupo, listAreaTipologiaGrupo);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAreaTipologiaGrupo(Collection<TipologiaGrupoArea> pColTipologiaGrupoArea) throws Exception {
		tipologiaGrupoAreaDAOImpl.excluirAreaTipologiaGrupo(pColTipologiaGrupoArea);
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirAreaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) throws Exception{
		tipologiaGrupoAreaDAOImpl.excluirAreaTipologiaGrupo(tipologiaGrupo);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public TipologiaGrupoArea listarAreaTipologiaGrupo(TipologiaGrupo tipologiaGrupo, Integer ideUnidadeMedida ) throws Exception{
		return tipologiaGrupoAreaDAOImpl.listarAreaTipologiaGrupo(tipologiaGrupo, ideUnidadeMedida);
		
	} 
	
	
	
}