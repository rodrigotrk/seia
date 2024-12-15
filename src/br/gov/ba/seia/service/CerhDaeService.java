/**
 * 
 */
package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Dae;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhDaeService {
	
	@Inject
	private IDAO<Dae> idao;
	
	public void atualizar(Dae dae) {
		idao.atualizar(dae);
	}
}
