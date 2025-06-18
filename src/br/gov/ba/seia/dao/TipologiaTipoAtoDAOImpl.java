package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TipologiaTipoAto;


public class TipologiaTipoAtoDAOImpl {
	
	@Inject
	IDAO<TipologiaTipoAto> tipologiaTipoAtoDAO;
	
	
	public TipologiaTipoAto filtrarTipologiaTipoAtoGrupoById(TipologiaTipoAto pTipologiaTipoAto) {
		return tipologiaTipoAtoDAO.buscarEntidadePorExemplo(pTipologiaTipoAto);
	}
	

	public void salvarTipologiaTipoAto(TipologiaTipoAto pTipologiaTipoAto) {
		tipologiaTipoAtoDAO.salvarOuAtualizar(pTipologiaTipoAto);
	}
	

	public void salvarTipologiaTipoAtos(Collection<TipologiaTipoAto> pColTipologiaTipoAto) {

		for (TipologiaTipoAto listTipologiaTipoAto : pColTipologiaTipoAto) {
			listTipologiaTipoAto.setIdeTipologiaGrupo(listTipologiaTipoAto.getIdeTipologiaGrupo());
			listTipologiaTipoAto.setIdeTipoAto(listTipologiaTipoAto.getIdeTipoAto());
			listTipologiaTipoAto.setIdeAtoAmbiental(listTipologiaTipoAto.getIdeAtoAmbiental());
			tipologiaTipoAtoDAO.salvar(listTipologiaTipoAto);
		}
		
		
	}
	
	

	public void atualizarTipologiaTipoAto(TipologiaTipoAto pTipologiaTipoAto) {
		tipologiaTipoAtoDAO.atualizar(pTipologiaTipoAto);
	}
	

	public  TipologiaTipoAto carregarTipologiaTipoAtos(Integer id) {
		return 	tipologiaTipoAtoDAO.carregarLoad(id);
	}
	
	
	public  Collection<TipologiaTipoAto> recuperarTipologiaTipoAto(Integer id ) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(TipologiaTipoAto.class);
		detachedCriteria.setFetchMode("ideTipologiaGrupo", FetchMode.JOIN);
		detachedCriteria.add(Restrictions.eq("ideTipologiaGrupo.ideTipologiaGrupo", id));
		return tipologiaTipoAtoDAO.listarPorCriteria(detachedCriteria);
	}
	
	
	public void deletarTipologiaTipoAto(Collection<TipologiaTipoAto> pTipologiaTipoAto)  {
		for (TipologiaTipoAto listTipologiaTipoAto : pTipologiaTipoAto) {
			tipologiaTipoAtoDAO.remover(listTipologiaTipoAto);
		}
	}

	
	public TipologiaTipoAto listarTipologiaTipoAto(TipologiaGrupo tipologiaGrupo, Integer ideTipoAto ) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaTipoAto.class);
		criteria.add(Restrictions.eq("ideTipologiaGrupo", tipologiaGrupo));
		criteria.add(Restrictions.eq("ideTipoAto.ideTipoAto", ideTipoAto));
		return tipologiaTipoAtoDAO.buscarPorCriteria(criteria);
	} 
	
	
	public Collection<TipologiaTipoAto> filtrarTipologiaTipoAto(TipologiaGrupo tipologiaGrupo,Collection<TipologiaTipoAto> listTipologiaTipoAto)  {
		 DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaTipoAto.class);
		 criteria.add(Restrictions.eq("ideTipologiaGrupo",tipologiaGrupo ));
		 Collection<Integer> ids = new ArrayList<Integer>();
		 
		 for (TipologiaTipoAto tipologiaTipoAto : listTipologiaTipoAto) {
			ids.add(tipologiaTipoAto.getIdeTipoAto().getIdeTipoAto());
		}
		 
		 // clausula not in
		 Criterion in = Restrictions.in("ideTipoAto.ideTipoAto", ids);
		 criteria.add(Restrictions.not(in));  
		 return tipologiaTipoAtoDAO.listarPorCriteria(criteria);
		 
		 
	}
	
	
	public void excluirTipologiaTipoAto(Collection<TipologiaTipoAto> pColTipologiaTipoAto){
		for (TipologiaTipoAto listTipologiaTipoAto : pColTipologiaTipoAto) {
			listTipologiaTipoAto = tipologiaTipoAtoDAO.carregarGet(listTipologiaTipoAto.getIdeTipologiaTipoAto());
			tipologiaTipoAtoDAO.remover(listTipologiaTipoAto);
		}
	}
	
	
	public void excluirTipologiaTipoAto(TipologiaGrupo tipologiaGrupo) {
		String deleteSQL = "delete from tipologia_tipo_ato where ide_tipo_ato  is not null and ide_tipologia_grupo = :ideTipologiaGrupo ";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideTipologiaGrupo", tipologiaGrupo.getIdeTipologiaGrupo());
		tipologiaTipoAtoDAO.executarNativeQuery(deleteSQL, params);
		
	}
	
	public TipologiaTipoAto listarTipologiaAtoAmbiental(TipologiaGrupo tipologiaGrupo, Integer ideAtoAmbiental ) {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaTipoAto.class);
		criteria.add(Restrictions.eq("ideTipologiaGrupo", tipologiaGrupo));
		criteria.add(Restrictions.eq("ideAtoAmbiental.ideAtoAmbiental", ideAtoAmbiental));
		return tipologiaTipoAtoDAO.buscarPorCriteria(criteria);
	}
	
	
	public Collection<TipologiaTipoAto> filtrarTipologiaAtoAmbiental(TipologiaGrupo tipologiaGrupo,Collection<TipologiaTipoAto> listTipologiaTipoAto)  {
		 DetachedCriteria criteria = DetachedCriteria.forClass(TipologiaTipoAto.class);
		 criteria.add(Restrictions.eq("ideTipologiaGrupo",tipologiaGrupo ));
		 Collection<Integer> ids = new ArrayList<Integer>();
		 
		 for (TipologiaTipoAto tipologiaTipoAto : listTipologiaTipoAto) {
			ids.add(tipologiaTipoAto.getIdeAtoAmbiental().getIdeAtoAmbiental());
		}
		 
		 // clausula not in
		 Criterion in = Restrictions.in("ideAtoAmbiental.ideAtoAmbiental", ids);
		 criteria.add(Restrictions.not(in));  
		 return tipologiaTipoAtoDAO.listarPorCriteria(criteria);
	}
	
	
	/**
	 *
	 * @param pColTipologiaTipoAto
	 * @throws Exception
	 * @author diegoraian
	 */
	public void excluirTipologiaAtoAmbiental(Collection<TipologiaTipoAto> pColTipologiaTipoAto){
		for (TipologiaTipoAto listTipologiaTipoAto : pColTipologiaTipoAto) {
			listTipologiaTipoAto = tipologiaTipoAtoDAO.carregarGet(listTipologiaTipoAto.getIdeTipologiaTipoAto());
			tipologiaTipoAtoDAO.remover(listTipologiaTipoAto);
		}
	}
	
	
	public void excluirTipologiaAtoAmbiental(TipologiaGrupo tipologiaGrupo) {
		String deleteSQL = "delete from tipologia_tipo_ato  where ide_ato_ambiental is not null and  ide_tipologia_grupo = :ideTipologiaGrupo ";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideTipologiaGrupo", tipologiaGrupo.getIdeTipologiaGrupo());
		tipologiaTipoAtoDAO.executarNativeQuery(deleteSQL, params);
		
	}


}