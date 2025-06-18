package br.gov.ba.seia.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.ProcessoExterno;

public class ProcessoExternoDAOImpl {
	
	@Inject
	private IDAO<ProcessoExterno> processoExternoDAO;
	
	public List<ProcessoExterno> buscarProcessoExternoBySistemaNumero(ProcessoExterno processoExterno, List<String> listaSistemas)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoExterno.class);		
		criteria.add(Restrictions.or(
				Restrictions.ilike("processo", processoExterno.getProcesso(), MatchMode.START),
				Restrictions.ilike("processo", processoExterno.getProcessoFormatado(), MatchMode.START)
		));
		
		criteria.add(Restrictions.or(Restrictions.eq("sistema", listaSistemas.get(0)), Restrictions.eq("sistema", listaSistemas.get(1))));
		criteria.add(Restrictions.ne("status", "Cancelado"));
		return processoExternoDAO.listarPorCriteria(criteria);
	}
	
	public List<ProcessoExterno> buscarProcessoExternoBySistemaNumero(ProcessoExterno processoExterno)  {
					
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoExterno.class);		
		criteria.add(Restrictions.ilike("processo", processoExterno.getProcesso(), MatchMode.START));	
		
		criteria.add(Restrictions.eq("sistema", processoExterno.getSistema()));		
		criteria.add(Restrictions.ne("status", "Cancelado"));
		return processoExternoDAO.listarPorCriteria(criteria);

	}	

}
