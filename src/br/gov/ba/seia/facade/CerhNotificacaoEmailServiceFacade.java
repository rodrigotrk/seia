package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.CerhNotificacaoEmailImpl;
import br.gov.ba.seia.entity.CerhNotificacaoEmail;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhNotificacaoEmailServiceFacade {
	
	@EJB
	private CerhNotificacaoEmailImpl cerhNotificacaoEmailImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCerhNotificacaoEmail(CerhNotificacaoEmail cerhNotificacaoEmail)  {
		cerhNotificacaoEmailImpl.salvar(cerhNotificacaoEmail);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarCerhNotificacaoEmail(CerhNotificacaoEmail cerhNotificacaoEmail)  {
		cerhNotificacaoEmailImpl.atualizar(cerhNotificacaoEmail);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhNotificacaoEmail> listarEmailsNaoEnviados() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhNotificacaoEmail.class);
		criteria
			.add(Restrictions.eq("indeEnviado", Boolean.FALSE));
		
		return cerhNotificacaoEmailImpl.listarEmailsNaoEnviados(criteria); 	
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countEmailsPendentes() {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhNotificacaoEmail.class);
		criteria
			.add(Restrictions.eq("indeEnviado", Boolean.FALSE));
		
		return cerhNotificacaoEmailImpl.countEmailsPendentes(criteria); 	
	}

	
}
