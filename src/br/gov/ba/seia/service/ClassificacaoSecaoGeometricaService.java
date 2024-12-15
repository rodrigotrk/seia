/**
 * 
 */
package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;

/**
 * @author mario.junior
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClassificacaoSecaoGeometricaService {

	@Inject
	private IDAO<ClassificacaoSecaoGeometrica> daoClassifSecGeometrica;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ClassificacaoSecaoGeometrica> listarClassificacaoSecaoGeometrica() {
		return daoClassifSecGeometrica.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ClassificacaoSecaoGeometrica carregar(Integer id) {
		return daoClassifSecGeometrica.carregarGet(id);
	}
	
	
}
