package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.OutorgaConcedida;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OutorgaConcedidaService {

	@Inject
	private IDAO<OutorgaConcedida> outorgaConcedidaIDAO;

	/**
	 * Método que persiste a lista {@link OutorgaConcedida} no Banco de Dados. 
	 * @author eduardo.fernandes
	 * @
	 * @see <a href="http://10.105.12.26/redmine/issues/7550">#7550</a>
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaOutorgaConcedida(List<OutorgaConcedida> lista) {
		for(OutorgaConcedida outorgaConcedida : lista){
			salvarOutorgaConcedida(outorgaConcedida);
		}
	}

	/**
	 * Método que persiste a {@link OutorgaConcedida} no Banco de Dados. 
	 * @author eduardo.fernandes
	 * @
	 * @see <a href="http://10.105.12.26/redmine/issues/7550">#7550</a>
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarOutorgaConcedida(OutorgaConcedida outorgaConcedida)  {
		outorgaConcedidaIDAO.salvarOuAtualizar(outorgaConcedida);
	}
}
