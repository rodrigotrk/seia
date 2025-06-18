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
import br.gov.ba.seia.entity.UsoReservatorio;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsoReservatorioService{
	
	@Inject
	IDAO<UsoReservatorio> idao;

	public List<UsoReservatorio> listar() {
		return idao.listarTodos();
	}
}
