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
import br.gov.ba.seia.entity.FceIntervencaoCaracteristicaExtracao;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceIntervencaoCaracteristicaExtracaoService {

	@Inject
	private IDAO<FceIntervencaoCaracteristicaExtracao> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracao) {
		idao.salvar(fceIntervencaoCaracteristicaExtracao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(FceIntervencaoCaracteristicaExtracao fceIntervencaoCaracteristicaExtracao) {
		idao.atualizar(fceIntervencaoCaracteristicaExtracao);
	}
}
