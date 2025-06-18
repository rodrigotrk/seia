package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;

import br.gov.ba.seia.entity.CerhNotificacaoEmail;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhNotificacaoEmailImpl {

	@Inject
	private IDAO<CerhNotificacaoEmail> cerhNotificacaoEmailDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CerhNotificacaoEmail cerhNotificacaoEmail)  {
		cerhNotificacaoEmailDAOImpl.salvar(cerhNotificacaoEmail);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(CerhNotificacaoEmail cerhNotificacaoEmail)  {
		cerhNotificacaoEmailDAOImpl.atualizar(cerhNotificacaoEmail);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhNotificacaoEmail> listarEmailsNaoEnviados(DetachedCriteria detachedCriteria, Integer first, Integer pageSize) {
		return cerhNotificacaoEmailDAOImpl.listarPorCriteriaDemanda(detachedCriteria, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhNotificacaoEmail> listarEmailsNaoEnviados(DetachedCriteria detachedCriteria) {
		return cerhNotificacaoEmailDAOImpl.listarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countEmailsPendentes(DetachedCriteria detachedCriteria) {
		return cerhNotificacaoEmailDAOImpl.count(detachedCriteria);
	}
}