package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhProcesso;
import br.gov.ba.seia.entity.CerhProcessoSuspensao;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhProcessoSuspensaoDAOImpl  extends AbstractDAO<CerhProcessoSuspensao>{
	private static final long serialVersionUID = 1L;

	@Override
	protected IDAO<CerhProcessoSuspensao> getDAO() {
		return cerhProcessoSuspensaoDAO;
	}
	
	@Inject
	private IDAO<CerhProcessoSuspensao> cerhProcessoSuspensaoDAO;
	
	@Inject
	private IDAO<Integer> ideDAO;

	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void gerenciarCerhProcessoSuspensaoRemovido(CerhProcesso cerhProcesso) {
		Collection<Integer> listaRemocao = listarIdeCerhProcessoSuspensaoRemocao(cerhProcesso);
		if(!Util.isNullOuVazio(listaRemocao)) {
			StringBuilder hql = new StringBuilder();
			hql
			.append("delete from CerhProcessoSuspensao cps where cps.ideCerhProcessoSuspensao in :listaRemocao")
			;
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("listaRemocao", listaRemocao);
			
			cerhProcessoSuspensaoDAO.executarQuery(hql.toString(), params);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Collection<Integer> listarIdeCerhProcessoSuspensaoRemocao(CerhProcesso cerhProcesso) {
		
		Collection<Integer> listaIdeObjetosEmBanco = null;
		
		if(!Util.isNullOuVazio(cerhProcesso.getCerhProcessoSuspensaoCollection())) {
			listaIdeObjetosEmBanco = new ArrayList<Integer>();
			for (CerhProcessoSuspensao cps : cerhProcesso.getCerhProcessoSuspensaoCollection()) {
				if(!Util.isNull(cps.getIdeCerhProcessoSuspensao())) {
					listaIdeObjetosEmBanco.add(cps.getIdeCerhProcessoSuspensao());
				}
			}
		}
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhProcessoSuspensao.class);
		criteria
			.add(Restrictions.eq("ideCerhProcesso", cerhProcesso))
		;
		
		if(!Util.isNullOuVazio(listaIdeObjetosEmBanco)) {
			criteria.add(Restrictions.not(Restrictions.in("ideCerhProcessoSuspensao", listaIdeObjetosEmBanco)));
		}
		
		criteria
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideCerhProcessoSuspensao"),"ideCerhProcessoSuspensao")
			)
		;
		
		return ideDAO.listarPorCriteria(criteria);
	}

	

}