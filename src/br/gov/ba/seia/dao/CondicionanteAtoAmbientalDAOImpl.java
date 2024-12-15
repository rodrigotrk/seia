/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 12 de set de 2017
 * Objetivo: 	
	
 */
package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.CondicionanteAtoAmbiental;

/**
 * PRODEB - Companhia de processamento de dados do Estado da Bahia
 * Classe: CondicionanteAtoAmbientalDAOImpl.java
 * Projeto: seia
 * Pacote: br.gov.ba.seia.dao
 * @autor: diegoraian em 12 de set de 2017
 * Objetivo: 	
	
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CondicionanteAtoAmbientalDAOImpl extends BaseDAO<CondicionanteAtoAmbiental>{

	/**
	 * Propriedade: dao
	 * @type: IDAO<CondicionanteAtoAmbiental>
	 */
	@Inject
	IDAO<CondicionanteAtoAmbiental> dao;


	@Override
	protected IDAO<CondicionanteAtoAmbiental> getDao() {
		return dao;
	}

	/**
	 *
	 * @param item
	 * @author diegoraian
	 */
	public void removerItem(CondicionanteAtoAmbiental item) {
		Map<String,Object> parametros = new HashMap<String,Object>();
		parametros.put("ideAtoAmbiental", item.getIdeAtoAmbiental().getIdeAtoAmbiental());
		parametros.put("ideCondicionante", item.getIdeCondicionante().getIdeCondicionante());
		getDao().executarNamedQuery("CondicionanteAto.remove", parametros);
	}

}
