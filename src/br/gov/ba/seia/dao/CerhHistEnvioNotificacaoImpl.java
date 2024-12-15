package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;

import br.gov.ba.seia.entity.CerhHistEnvioNotificacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhHistEnvioNotificacaoImpl {

	@Inject
	private IDAO<CerhHistEnvioNotificacao> cerhHistEnvioNotificacaoImplDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CerhHistEnvioNotificacao cerhHistoricoEnvioNotificacao) {
		cerhHistEnvioNotificacaoImplDAO.salvar(cerhHistoricoEnvioNotificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhHistEnvioNotificacao> listar(DetachedCriteria detachedCriteria) {
		return cerhHistEnvioNotificacaoImplDAO.listarPorCriteria(detachedCriteria);
	}
	
}