package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.ClassificacaoRejeitoDnpm;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClassificacaoRejeitoDnpmDAOImpl {

	@Inject
	private IDAO<ClassificacaoRejeitoDnpm> classificacaoRejeitoDnpmIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ClassificacaoRejeitoDnpm> listarClassificacaoRejeitoDnpm() {
		return classificacaoRejeitoDnpmIDAO.listarTodos();
	}
}
