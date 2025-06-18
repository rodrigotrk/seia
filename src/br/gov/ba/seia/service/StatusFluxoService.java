package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.StatusFluxo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StatusFluxoService {


	@Inject
	IDAO<StatusFluxo> daoStatusFluxo;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<StatusFluxo> listarTodosStatusFluxoProcesso() {
		return daoStatusFluxo.listarTodos();
	}
		
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<StatusFluxo> listarTodosStatusProcessoOrderByAsc() throws Exception {
		StringBuilder sql = new StringBuilder();		
		
		sql.append("select st ");
		sql.append("from StatusFluxo st ");
		sql.append("order by st.dscStatusFluxo asc ");
		
		return daoStatusFluxo.listarPorQuery(sql.toString(), new HashMap<String, Object>());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<StatusFluxo> listarStatusProcessoPorIde(List<Integer> statusValidos) throws Exception {
		Map<String,Object> param = new HashMap<String,Object>();
		StringBuilder sql = new StringBuilder();
		
		sql.append("select st ");
		sql.append("from StatusFluxo st ");
		sql.append("where st.ideStatusFluxo in :statusValidos ");
		sql.append("order by st.dscStatusFluxo asc ");
		
		param.put("statusValidos", statusValidos);
		
		
		return daoStatusFluxo.listarPorQuery(sql.toString(), param);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<StatusFluxo> listarStatusRequerimento(ArrayList<Integer> status) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(StatusFluxo.class)
				.add(Restrictions.in("this.ideStatusFluxo", status));
		criteria.addOrder(Order.asc("dscStatusFluxo"));
		
		return daoStatusFluxo.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusFluxo carregar(Integer ideStatusFluxo){
		StatusFluxo statusFluxo = daoStatusFluxo.carregarGet(ideStatusFluxo);
		return statusFluxo;
	}
	
}