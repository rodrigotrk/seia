/**
 * 
 */
package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceBarragemLicenciamento;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceBarragemLicenciamentoService {
	
	@Inject
	IDAO<FceBarragemLicenciamento> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(FceBarragemLicenciamento fceBarragemLicenciamento)  {
		idao.atualizar(fceBarragemLicenciamento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(FceBarragemLicenciamento fceBarragemLicenciamento)  {
		idao.salvarOuAtualizar(fceBarragemLicenciamento);
	}
}
