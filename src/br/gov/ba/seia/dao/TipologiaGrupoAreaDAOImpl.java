package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TipologiaGrupoArea;



public class TipologiaGrupoAreaDAOImpl {
	
	@Inject
	IDAO<TipologiaGrupoArea> tipologiaGrupoAreaDAO;	

	public TipologiaGrupoArea filtrarUnidadeMedidaTipologiaGrupoById(TipologiaGrupoArea pTipologiaGrupoArea) {
		return tipologiaGrupoAreaDAO.buscarEntidadePorExemplo(pTipologiaGrupoArea);
	}
	

	public void salvarTipologiaGrupoArea(TipologiaGrupoArea pTipologiaGrupoArea) {
		tipologiaGrupoAreaDAO.salvarOuAtualizar(pTipologiaGrupoArea);
	}
	

	public void salvarTipologiaGrupoArea(Collection<TipologiaGrupoArea> pColTipologiaGrupoArea) {

		for (TipologiaGrupoArea listTipologiaGrupoArea : pColTipologiaGrupoArea) {
			listTipologiaGrupoArea.setIdeArea(listTipologiaGrupoArea.getIdeArea());
			listTipologiaGrupoArea.setIdeTipologiaGrupo(listTipologiaGrupoArea.getIdeTipologiaGrupo());
			tipologiaGrupoAreaDAO.salvar(listTipologiaGrupoArea);
		}
	}
	

	public void atualizarTipologiaGrupoArea(TipologiaGrupoArea pTipologiaGrupoArea)  {
		tipologiaGrupoAreaDAO.atualizar(pTipologiaGrupoArea);
	}
	

	public  Collection<TipologiaGrupoArea> recuperarTipologiaGrupoArea(Integer id ) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TipologiaGrupoArea.class);
		detachedCriteria.setFetchMode("ideTipologiaGrupo", FetchMode.JOIN);
		detachedCriteria.add(Restrictions.eq("ideTipologiaGrupo.ideTipologiaGrupo", id));
		return tipologiaGrupoAreaDAO.listarPorCriteria(detachedCriteria);
	}
	
	
	public void deletarTipologiaGrupoArea(Collection<TipologiaGrupoArea> pTipologiaGrupoArea)  {
		for (TipologiaGrupoArea listTipologiaGrupoArea : pTipologiaGrupoArea) {
			tipologiaGrupoAreaDAO.remover(listTipologiaGrupoArea);
		}
		Logger.getLogger("Tipologia tipo ato  deletado com sucesso.");
	}
	
	
	
	public Collection<TipologiaGrupoArea> filtrarAreaTipologiaGrupo(TipologiaGrupo tipologiaGrupo,Collection<TipologiaGrupoArea> listAreaTipologiaGrupo) {
		 DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaGrupoArea.class);
		 criteria.add(Restrictions.eq("ideTipologiaGrupo",tipologiaGrupo ));
		 Collection<Integer> ids = new ArrayList<Integer>();
		 
		 for (TipologiaGrupoArea areaTipGrup : listAreaTipologiaGrupo) {
			ids.add(areaTipGrup.getIdeArea().getIdeArea());
		}
		 // clausula not in
		 Criterion in = Restrictions.in("ideArea.ideArea", ids);
		 criteria.add(Restrictions.not(in));  
		 return tipologiaGrupoAreaDAO.listarPorCriteria(criteria);
	}
	
	
	public void excluirAreaTipologiaGrupo(Collection<TipologiaGrupoArea> pColTipologiaGrupoArea){
		for (TipologiaGrupoArea listTipologiaGrupoArea : pColTipologiaGrupoArea) {
			listTipologiaGrupoArea = tipologiaGrupoAreaDAO.carregarGet(listTipologiaGrupoArea.getIdeTipologiaGrupoArea());
			tipologiaGrupoAreaDAO.remover(listTipologiaGrupoArea);
		}
	}
	
	
	
	public void excluirAreaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		String deleteSQL = "delete from Tipologia_Grupo_Area where ide_tipologia_grupo = :ideTipologiaGrupo ";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideTipologiaGrupo", tipologiaGrupo.getIdeTipologiaGrupo());
		tipologiaGrupoAreaDAO.executarNativeQuery(deleteSQL, params);
		
	}
	
	
	public TipologiaGrupoArea listarAreaTipologiaGrupo(TipologiaGrupo tipologiaGrupo, Integer ideUnidadeMedida ) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaGrupoArea.class);
		criteria.add(Restrictions.eq("ideTipologiaGrupo", tipologiaGrupo));
		criteria.add(Restrictions.eq("ideArea.ideArea", ideUnidadeMedida));
		return tipologiaGrupoAreaDAO.buscarPorCriteria(criteria);
	} 


}