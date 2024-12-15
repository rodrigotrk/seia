package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhSituacaoTipoUso;

/**
 * @author eduardo.fernandes 
 * @since 24/03/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhSituacaoTipoUsoDAOImpl extends AbstractDAO<CerhSituacaoTipoUso>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhSituacaoTipoUso> dao;

	
	@Override
	protected IDAO<CerhSituacaoTipoUso> getDAO() {
		return dao;
	}
	
}