package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhSituacaoRegularizacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhSituacaoRegularizacaoDAOImpl extends AbstractDAO<CerhSituacaoRegularizacao>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhSituacaoRegularizacao> cerhSituacaoRegularizacaoDAO;
	
	@Override
	protected IDAO<CerhSituacaoRegularizacao> getDAO() {
		return cerhSituacaoRegularizacaoDAO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhSituacaoRegularizacao> listarTodos() {
		return cerhSituacaoRegularizacaoDAO.listarTodos();
	}
	
}