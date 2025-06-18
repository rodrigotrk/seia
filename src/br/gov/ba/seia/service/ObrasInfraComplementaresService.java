/**
 * 
 */
package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ObrasInfraComplementares;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ObrasInfraComplementaresService {
	
	@Inject
	IDAO<ObrasInfraComplementares> idao;
	
	public List<ObrasInfraComplementares> listar() {
		return idao.listarTodos();
	}

}
