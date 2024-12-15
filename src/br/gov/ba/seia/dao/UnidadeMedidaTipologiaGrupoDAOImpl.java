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
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;


public class UnidadeMedidaTipologiaGrupoDAOImpl {
	
	@Inject
	IDAO<UnidadeMedidaTipologiaGrupo> unidadeMedidaTipologiaGrupoDAO;	

	
	public UnidadeMedidaTipologiaGrupo filtrarUnidadeMedidaTipologiaGrupoById(UnidadeMedidaTipologiaGrupo pUnidadeMedidaTipologiaGrupo) {
		return unidadeMedidaTipologiaGrupoDAO.buscarEntidadePorExemplo(pUnidadeMedidaTipologiaGrupo);
	}
	
	
	public void salvarUnidadeMedidaTipologiaGrupo(UnidadeMedidaTipologiaGrupo pUnidadeMedidaTipologiaGrupo)  {
		unidadeMedidaTipologiaGrupoDAO.salvarOuAtualizar(pUnidadeMedidaTipologiaGrupo);
	}
	
	public void salvarUnidadeMedidaTipologiaGrupos(Collection<UnidadeMedidaTipologiaGrupo> pColUnidadeMedidaTipologiaGrupo)  {
		for (UnidadeMedidaTipologiaGrupo listUnidadeMedidaTipologiaGrupo : pColUnidadeMedidaTipologiaGrupo) {
			listUnidadeMedidaTipologiaGrupo.setIdeTipologiaGrupo(listUnidadeMedidaTipologiaGrupo.getIdeTipologiaGrupo());
			listUnidadeMedidaTipologiaGrupo.setIdeUnidadeMedida(listUnidadeMedidaTipologiaGrupo.getIdeUnidadeMedida());
			unidadeMedidaTipologiaGrupoDAO.salvar(listUnidadeMedidaTipologiaGrupo);
		}
	}
	
	public void atualizarUnidadeMedidaTipologiaGrupo(UnidadeMedidaTipologiaGrupo pUnidadeMedidaTipologiaGrupo)  {
		unidadeMedidaTipologiaGrupoDAO.atualizar(pUnidadeMedidaTipologiaGrupo);
	}
	
	public  UnidadeMedidaTipologiaGrupo carregarUnidadeMedidaTipologiaGrupo(Integer id){		
		return 	unidadeMedidaTipologiaGrupoDAO.carregarLoad(id);
	}
	
	
	public  Collection<UnidadeMedidaTipologiaGrupo> recuperarUnidadeMedidaTipologiaGrupo(Integer id) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(UnidadeMedidaTipologiaGrupo.class);
		detachedCriteria.setFetchMode("ideTipologiaGrupo", FetchMode.JOIN);
		detachedCriteria.add(Restrictions.eq("ideTipologiaGrupo.ideTipologiaGrupo", id));
		return unidadeMedidaTipologiaGrupoDAO.listarPorCriteria(detachedCriteria);
		
	}
	
	public void deletarUnidadeMedidaTipologiaGrupos(Collection<UnidadeMedidaTipologiaGrupo> pUnidadeMedidaTipologiaGrupo)  {
		for (UnidadeMedidaTipologiaGrupo listUnidadeMedidaTipologiaGrupo : pUnidadeMedidaTipologiaGrupo) {
			unidadeMedidaTipologiaGrupoDAO.remover(listUnidadeMedidaTipologiaGrupo);
		}
		Logger.getLogger("Unidade Medida Tipologia grupo  deletado com sucesso.");
	}
	
	public UnidadeMedidaTipologiaGrupo listarUnidadeMedidaTipologiaGrupo(TipologiaGrupo tipologiaGrupo, Integer ideUnidadeMedida ) {
		DetachedCriteria criteria = DetachedCriteria.forClass(UnidadeMedidaTipologiaGrupo.class);
		criteria.add(Restrictions.eq("ideTipologiaGrupo", tipologiaGrupo));
		criteria.add(Restrictions.eq("ideUnidadeMedida.ideUnidadeMedida", ideUnidadeMedida));
		return unidadeMedidaTipologiaGrupoDAO.buscarPorCriteria(criteria);
	}

	
	public Collection<UnidadeMedidaTipologiaGrupo> filtrarUnidadeMedidaTipologiaGrupo(TipologiaGrupo tipologiaGrupo,Collection<UnidadeMedidaTipologiaGrupo> listUnidadeMedidaTipologiaGrupo)  {
		 DetachedCriteria criteria = DetachedCriteria.forClass(UnidadeMedidaTipologiaGrupo.class);
		 criteria.add(Restrictions.eq("ideTipologiaGrupo",tipologiaGrupo ));
		 Collection<Integer> ids = new ArrayList<Integer>();
		 for (UnidadeMedidaTipologiaGrupo unMedTipGrup : listUnidadeMedidaTipologiaGrupo) {
			ids.add(unMedTipGrup.getIdeUnidadeMedida().getIdeUnidadeMedida());
		}
		 // clausula not in
		 Criterion in = Restrictions.in("ideUnidadeMedida.ideUnidadeMedida", ids);
		 criteria.add(Restrictions.not(in));  
		 return unidadeMedidaTipologiaGrupoDAO.listarPorCriteria(criteria);
	}
	
	
	public void excluirUnidadeMedidaTipologiaGrupo(Collection<UnidadeMedidaTipologiaGrupo> pColUnidadeMedidaTipologiaGrupo){
		for (UnidadeMedidaTipologiaGrupo listUnidadeMedidaTipologiaGrupo : pColUnidadeMedidaTipologiaGrupo) {
			listUnidadeMedidaTipologiaGrupo = unidadeMedidaTipologiaGrupoDAO.carregarGet(listUnidadeMedidaTipologiaGrupo.getIdeUnidadeMedidaTipologiaGrupo());
			unidadeMedidaTipologiaGrupoDAO.remover(listUnidadeMedidaTipologiaGrupo);
		}
	}
	

	public void excluirUnidadeMedidaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		String deleteSQL = "delete from unidade_medida_tipologia_grupo where ide_tipologia_grupo = :ideTipologiaGrupo ";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideTipologiaGrupo", tipologiaGrupo.getIdeTipologiaGrupo());
		unidadeMedidaTipologiaGrupoDAO.executarNativeQuery(deleteSQL, params);
		
	}

}