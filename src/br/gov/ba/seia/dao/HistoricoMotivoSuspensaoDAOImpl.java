package br.gov.ba.seia.dao;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.HistoricoMotivoSuspensao;



public class HistoricoMotivoSuspensaoDAOImpl implements HistoricoMotivoSuspensaoDAOIf {

	@Inject
	IDAO<HistoricoMotivoSuspensao> histMotivoSuspensaoDAO;

	public IDAO<HistoricoMotivoSuspensao> getHistMotivoSuspensaoDAO() {
		return histMotivoSuspensaoDAO;
	}

	public void setHistMotivoSuspensaoDAO(IDAO<HistoricoMotivoSuspensao> histMotivoSuspensaoDAO) {
		this.histMotivoSuspensaoDAO = histMotivoSuspensaoDAO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public HistoricoMotivoSuspensao carregarTudo(final HistoricoMotivoSuspensao historicoMotivoSuspensao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(HistoricoMotivoSuspensao.class);
		criteria.add(Restrictions.eq("ideSuspensaoCadastro", historicoMotivoSuspensao.getIdeSuspensaoCadastro()));
		criteria.add(Restrictions.eq("ideMotivoSuspensaoCadastro", historicoMotivoSuspensao.getIdeMotivoSuspensaoCadastro()));
		return histMotivoSuspensaoDAO.buscarPorCriteria(criteria);
	}
	
	
}
