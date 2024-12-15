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
import br.gov.ba.seia.entity.MaterialUtilizado;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MaterialUtilizadoService {

	@Inject
	IDAO<MaterialUtilizado> idao;
	
	public List<MaterialUtilizado> listar() {
		return idao.listarTodos();
	}
}
