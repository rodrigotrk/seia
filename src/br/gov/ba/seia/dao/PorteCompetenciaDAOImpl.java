package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.NivelCompetencia;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PorteCompetencia;
import br.gov.ba.seia.entity.TipologiaGrupo;

public class PorteCompetenciaDAOImpl {
	
	
	@Inject
	IDAO<PorteCompetencia> porteCompetenciaDAO;	

	public PorteCompetencia filtrarPorteCompetenciaById(PorteCompetencia pPorteCompetencia) {
		return porteCompetenciaDAO.buscarEntidadePorExemplo(pPorteCompetencia);
	}
	

	public void salvarPorteCompetencia(PorteCompetencia pPorteCompetencia)  {
		porteCompetenciaDAO.salvarOuAtualizar(pPorteCompetencia);
	}
	
	
	public void salvarPorteCompetencias(Collection<PorteCompetencia> pColPorteCompetencia)  {
		for (PorteCompetencia listPorteCompetencia : pColPorteCompetencia) {
			listPorteCompetencia.setPorte(listPorteCompetencia.getPorte()) ;
			porteCompetenciaDAO.salvarOuAtualizar(listPorteCompetencia);
		}
	}
	

	public void atualizarTPorteCompetencia(PorteCompetencia pPorteTipologia)  {
		porteCompetenciaDAO.atualizar(pPorteTipologia);
	}
	
	
	public  Collection<PorteCompetencia> recuperarPorteCompetencia(Integer id ) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PorteCompetencia.class);
		detachedCriteria.setFetchMode("tipologiaGrupo", FetchMode.JOIN);
		detachedCriteria.add(Restrictions.eq("tipologiaGrupo.ideTipologiaGrupo", id));
		return 	porteCompetenciaDAO.listarPorCriteria(detachedCriteria);
	}
	
	
	public void deletarPorteCompetencia(Collection<PorteCompetencia> pPorteCompetencia)  {
		for (PorteCompetencia listPorteCompetencia : pPorteCompetencia) {
			porteCompetenciaDAO.remover(listPorteCompetencia);
		}
	}
	
	
	public Collection<PorteCompetencia> listarPorteCompetenciaTipologiaGrupo(TipologiaGrupo tipologiaGrupo) {
		DetachedCriteria criteria = DetachedCriteria.forClass(PorteCompetencia.class);
		criteria.setFetchMode("nivelCompetencia", FetchMode.JOIN);
		criteria.setFetchMode("porte", FetchMode.JOIN);
		criteria.setFetchMode("nivelCompetencia.ideTipoCompetencia", FetchMode.JOIN);
		criteria.add(Restrictions.eq("tipologiaGrupo", tipologiaGrupo));
		return porteCompetenciaDAO.listarPorCriteria(criteria);
	}

	
	public void excluirPorteCompetenciaNivel(TipologiaGrupo tipologiaGrupo, NivelCompetencia nivelCompetencia, Porte porte) {
		String deleteSQL = "delete from Porte_Competencia  where ide_tipologia_grupo = :ideTipologiaGrupo  and ide_nivel_competencia = :ideNivelCompetencia and ide_porte = :ideporte";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideTipologiaGrupo", tipologiaGrupo.getIdeTipologiaGrupo());
		params.put("ideNivelCompetencia", nivelCompetencia.getIdeNivelCompetencia());
		params.put("ideporte", porte.getIdePorte());
		porteCompetenciaDAO.executarNativeQuery(deleteSQL, params);
		
	}

}