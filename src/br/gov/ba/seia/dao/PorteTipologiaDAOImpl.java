package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PorteTipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;



public class PorteTipologiaDAOImpl {
	
	@Inject
	IDAO<PorteTipologia> porteTipologiaDAO;	

	public PorteTipologia filtrarPorteTipologiaById(PorteTipologia pPorteTipologia) {
		return porteTipologiaDAO.buscarEntidadePorExemplo(pPorteTipologia);
	}
	
	public void salvarPorteTipologia(PorteTipologia pPorteTipologia)  {
		porteTipologiaDAO.salvarOuAtualizar(pPorteTipologia);
	}
	
	public void salvarPorteTipologia2(PorteTipologia pPorteTipologia) {
		porteTipologiaDAO.salvar(pPorteTipologia);
	}
	

	public void salvarPorteTipologias(PorteTipologia porteTipologia)  {
		porteTipologiaDAO.salvarOuAtualizar(porteTipologia);
	}
	

	public void atualizarPorteTipologia(PorteTipologia pPorteTipologia)  {
		porteTipologiaDAO.atualizar(pPorteTipologia);
	}
	
	
	public PorteTipologia carregarPorteTipologia(Integer id){
		return porteTipologiaDAO.carregarGet(id);
	}
	

	public void deletarPorteTipologia(PorteTipologia porteTipologia)  {
		porteTipologiaDAO.remover(porteTipologia);
	}
	
	
	public void deletarPorteTipologias(Collection<PorteTipologia> pPorteTipologia)  {
		for (PorteTipologia listPorteTipologia : pPorteTipologia) {
			porteTipologiaDAO.remover(listPorteTipologia);
		}
	}
	
	
	public  Collection<PorteTipologia> recuperarPorteTipologia(Integer id ) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PorteTipologia.class);
		detachedCriteria.setFetchMode("ideTipologiaGrupo", FetchMode.JOIN);
		detachedCriteria.add(Restrictions.eq("ideTipologiaGrupo.ideTipologiaGrupo", id));
		return porteTipologiaDAO.listarPorCriteria(detachedCriteria);
		
	}
	
	public Collection<PorteTipologia> listarPorteCompetenciaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PorteTipologia.class);
		criteria.setFetchMode("idePorte", FetchMode.JOIN);
		criteria.add(Restrictions.eq("ideTipologiaGrupo", tipologiaGrupo));
		return porteTipologiaDAO.listarPorCriteria(criteria);
	}
	
	public void excluirPorteTipologia(TipologiaGrupo tipologiaGrupo,  Porte porte) {
		String deleteSQL = "delete from Porte_Tipologia  where ide_tipologia_grupo = :ideTipologiaGrupo  and ide_porte = :ideporte";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideTipologiaGrupo", tipologiaGrupo.getIdeTipologiaGrupo());
		params.put("ideporte", porte.getIdePorte());
		porteTipologiaDAO.executarNativeQuery(deleteSQL, params);
	}
	
	public PorteTipologia buscarPorteTipologia(TipologiaGrupo tipologiaGrupo,  Porte porte){
		DetachedCriteria criteria = DetachedCriteria.forClass(PorteTipologia.class);
		criteria.add(Restrictions.eq("ideTipologiaGrupo", tipologiaGrupo));
		criteria.add(Restrictions.eq("idePorte", porte));
		return porteTipologiaDAO.buscarPorCriteria(criteria);
	}

	
	
}