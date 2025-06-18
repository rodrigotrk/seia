package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import br.gov.ba.seia.entity.Segmento;
import br.gov.ba.seia.util.Util;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: SegmentoDAOImpl.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 6 de set de 2017
 * Objetivo: 	
	
 */
public class SegmentoDAOImpl  extends BaseDAO<Segmento>{

	
	/**
	 * Propriedade: residuoDAO
	 * @type: IDAO<Residuo>
	 */
	@Inject
	IDAO<Segmento> segmentoDAO;
	

	@Override
	protected IDAO<Segmento> getDao() {
		return this.segmentoDAO;
	}


	/**
	 *
	 * @param segmento
	 * @return
	 * @author diegoraian
	 * @ 
	 */
	
	public Collection<Segmento> buscarSegmentosPorFiltro(Segmento segmento)  {
		Map<String,Object> parametros = null;
		parametros = new HashMap<String,Object>();
		StringBuilder query = new StringBuilder("SELECT seg FROM Segmento seg WHERE indExcluido = false "); 
		if(!Util.isNullOuVazio(segmento.getNomSegmento())){
			query.append("AND nomSegmento like :nomSegmento ");
			parametros.put("nomSegmento", '%'+segmento.getNomSegmento()+'%');
		}
		 
		return segmentoDAO.listarPorQuery(query.toString(), parametros);
	}
	
	public Collection<Segmento> listarTodos() {
		return getDao().buscarPorNamedQuery("Segmento.findAll");
	}


	/**
	 *
	 * @param nomSegmento
	 * @return
	 * @author diegoraian
	 * @ 
	 */
	public boolean consultarSegmentoCadastrado(Segmento segmento)  {
		Map<String,Object> parametros = new HashMap<String,Object>();
		parametros.put("nomSegmento", segmento.getNomSegmento());
		List<Segmento> resultado = getDao().buscarPorNamedQuery("Segmento.findExistente", parametros);
		if(resultado == null){
			return false;
		} else {
			//Caso tenha resultado e o segmento for novo
			if ( !resultado.isEmpty() && segmento.getIdeSegmento() == null) {
				return true;
			//Caso não tenha resultado e o segmento já foi cadastrado
			} else if(!resultado.isEmpty()  && segmento.getIdeSegmento() != null) {
				for (Segmento segmento2 : resultado) {
					if (!segmento2.getIdeSegmento().equals(segmento.getIdeSegmento())) {
						return true;
					}
				}
			} 
		}
		return false;
	}
}
