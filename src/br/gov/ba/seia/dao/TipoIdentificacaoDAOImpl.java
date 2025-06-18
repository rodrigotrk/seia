package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.TipoIdentificacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoIdentificacaoDAOImpl extends AbstractDAO<TipoIdentificacao> {
	private static final long serialVersionUID = 1L;
	
	@Inject
	private IDAO<TipoIdentificacao> daoImpl;

	@Override
	protected IDAO<TipoIdentificacao> getDAO() {
		return daoImpl;
	}
	
}