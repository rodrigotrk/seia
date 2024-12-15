package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhTipoAutorizacaoOutorgado;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhTipoAutorizacaoOutorgadoDAOImpl extends AbstractDAO<CerhTipoAutorizacaoOutorgado>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhTipoAutorizacaoOutorgado> cerhTipoAutorizacaoOutorgadoDAO;

	@Override
	protected IDAO<CerhTipoAutorizacaoOutorgado> getDAO() {
		return cerhTipoAutorizacaoOutorgadoDAO;
	}

}