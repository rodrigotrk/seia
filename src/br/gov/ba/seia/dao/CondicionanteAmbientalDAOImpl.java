/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 12 de set de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.Hibernate;

import br.gov.ba.seia.entity.CondicionanteAmbiental;
import br.gov.ba.seia.util.Util;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: CondicionanteAmbientalDAOImpl.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 12 de set de 2017
 * Objetivo: 	
	
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CondicionanteAmbientalDAOImpl   extends BaseDAO<CondicionanteAmbiental>{

	@Inject
	IDAO<CondicionanteAmbiental> condicionanteAmbiental;


	
	
	@Override
	protected IDAO<CondicionanteAmbiental> getDao() {
		return this.condicionanteAmbiental;
	}


	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public CondicionanteAmbiental carregarGet(Integer ideCondicionanteAmbiental){
		CondicionanteAmbiental condicionate = getDao().carregarGet(ideCondicionanteAmbiental);
		Hibernate.initialize(condicionate.getCondicionanteAtoAmbientalCollection());
		Hibernate.initialize(condicionate.getCondicionanteAnaliseTecnicaCollection());
		return condicionate;
	}
	/**
	 *
	 * @return
	 * @author diegoraian
	 * @throws Exception 
	 */
	public List<CondicionanteAmbiental> listarTodos(CondicionanteAmbiental condicionanteAmbiental) {
		Map<String,Object> parametros = new HashMap<String,Object>();
		StringBuilder query = new StringBuilder("SELECT DISTINCT c FROM CondicionanteAmbiental c LEFT JOIN c.condicionanteAtoAmbientalCollection cato WHERE c.indExcluido = false ");
		if(!Util.isNullOuVazio(condicionanteAmbiental.getIdeSegmento())){
			query.append("AND c.ideSegmento.ideSegmento = :ideSegmento ");
			parametros.put("ideSegmento",condicionanteAmbiental.getIdeSegmento().getIdeSegmento());
		}
		
		if (!Util.isNullOuVazio(condicionanteAmbiental.getNomCondicionante())){
			query.append("AND c.nomCondicionante LIKE :nomCondicionante ");
			parametros.put("nomCondicionante", "%"+condicionanteAmbiental.getNomCondicionante()+"%");
		}
		
		if (!Util.isNullOuVazio(condicionanteAmbiental.getAtoAmbiental())){
			query.append("AND cato.ideAtoAmbiental.ideAtoAmbiental = :ideAtoAmbiental ");
			parametros.put("ideAtoAmbiental", condicionanteAmbiental.getAtoAmbiental().getIdeAtoAmbiental());
		}
		
		query.append("ORDER BY c.nomCondicionante ");
		return getDao().listarPorQuery(query.toString(), parametros);
	}




	/**
	 *
	 * @param nomCondicionante
	 * @author diegoraian
	 * @throws Exception 
	 */
	public Boolean consultarCondicionanteExistente(CondicionanteAmbiental condicionante) {
			StringBuilder query = new StringBuilder("SELECT  c FROM CondicionanteAmbiental c  WHERE c.indExcluido = false and lower(c.nomCondicionante) = lower(:nomCondicionante) ");
			Map<String,Object> parametros = new HashMap<String,Object>();
			parametros.put("nomCondicionante", condicionante.getNomCondicionante());
			List<CondicionanteAmbiental> resultado=  getDao().listarPorQuery(query.toString(), parametros) ;
			if(Util.isNull(resultado)){
				return false;
			} else {
				//Caso tenha resultado e o segmento for novo
				if ( !resultado.isEmpty() && Util.isNull(condicionante.getIdeCondicionante())) {
					return true;
				//Caso não tenha resultado e o segmento já foi cadastrado
				} else if(!resultado.isEmpty()  && !Util.isNull(condicionante.getIdeCondicionante())) {
					for (CondicionanteAmbiental item : resultado) {
						if (!item.getIdeCondicionante().equals(condicionante.getIdeCondicionante())) {
							return true;
						}
					}
				} 
			}
			return false;
	}
	
	
}
