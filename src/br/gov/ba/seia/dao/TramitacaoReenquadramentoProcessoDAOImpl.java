package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.TramitacaoReenquadramentoProcesso;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TramitacaoReenquadramentoProcessoDAOImpl {

	@Inject
	private IDAO<TramitacaoReenquadramentoProcesso> dao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcesso)  {
		dao.salvarOuAtualizar(tramitacaoReenquadramentoProcesso);
	}

}
