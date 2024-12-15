package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class StatusRequerimentoService {

	@Inject
	IDAO<StatusRequerimento> daoStatusRequerimento;
	@EJB
	TramitacaoRequerimentoService tramitacaoRequerimento;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<StatusRequerimento> listarStatusRequerimento() {
		return daoStatusRequerimento.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusRequerimento carregarGet(Integer pIdeStatus){
		return daoStatusRequerimento.carregarGet(pIdeStatus);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusRequerimento getMaxStatusTramitacaoRequerimantoPorData(Enquadramento enquadramento) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Enquadramento.class)
				.createCriteria("ideRequerimento", "req", JoinType.INNER_JOIN)
				.createCriteria("req.tramitacaoRequerimentoCollection", "tramreq", JoinType.INNER_JOIN)
				.setProjection(Projections.max("tramreq.dtcMovimentacao"))
				.add(Restrictions.eq("ideRequerimento.ideRequerimento", enquadramento.getIdeRequerimento().getIdeRequerimento() ));
		Date data = (Date) (Object) daoStatusRequerimento.buscarPorCriteria(criteria);
		StatusRequerimento statusRequerimento = null;
		if (data != null) {
			criteria = DetachedCriteria.forClass(Enquadramento.class)
					.createCriteria("ideRequerimento", "req", JoinType.INNER_JOIN)
					.createCriteria("req.tramitacaoRequerimentoCollection", "tramreq", JoinType.INNER_JOIN)
					.add(Restrictions.eq("tramreq.dtcMovimentacao", data))
					.setProjection(Projections.groupProperty("tramreq.ideTramitacaoRequerimento"))
					.add(Restrictions.eq("ideRequerimento.ideRequerimento", enquadramento.getIdeRequerimento().getIdeRequerimento()));
			Integer id = Integer.parseInt(((Object) daoStatusRequerimento.buscarPorCriteria(criteria)).toString());
			TramitacaoRequerimento tramit = tramitacaoRequerimento.buscarPorIdCriteria(id);
			statusRequerimento = tramit.getIdeStatusRequerimento();
		}
		return statusRequerimento;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusRequerimento getMaxStatusTramitacaoRequerimantoPorData(Requerimento requerimento) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(Enquadramento.class)
				.createCriteria("ideRequerimento", "req", JoinType.INNER_JOIN)
				.createCriteria("req.tramitacaoRequerimentoCollection", "tramreq", JoinType.INNER_JOIN)
				.setProjection(Projections.max("tramreq.dtcMovimentacao"))
				.add(Restrictions.eq("ideRequerimento", requerimento));		
		Date data = (Date)(Object)daoStatusRequerimento.buscarPorCriteria(criteria);
		StatusRequerimento statusRequerimento = null;
		if (data != null) {
			criteria = DetachedCriteria.forClass(Enquadramento.class)
					.createCriteria("ideRequerimento", "req", JoinType.INNER_JOIN)
					.createCriteria("req.tramitacaoRequerimentoCollection", "tramreq", JoinType.INNER_JOIN)
					.add(Restrictions.eq("tramreq.dtcMovimentacao", data))
					.setProjection(Projections.groupProperty("tramreq.ideTramitacaoRequerimento"))
					.add(Restrictions.eq("ideRequerimento", requerimento));
			Integer id = Integer.parseInt(((Object)daoStatusRequerimento.buscarPorCriteria(criteria)).toString());
			TramitacaoRequerimento tramit =  tramitacaoRequerimento.buscarPorIdCriteria(id);
			statusRequerimento = tramit.getIdeStatusRequerimento();
		}
		return statusRequerimento;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<StatusRequerimento> getMaxStatusRequerimentosByEmpreendimento(Empreendimento empreendimento)
			throws Exception {
		StringBuilder lSql = new StringBuilder();
		lSql.append("SELECT status ");
		lSql.append("FROM Requerimento req ");
		lSql.append("INNER JOIN req.tramitacaoRequerimentoCollection tran ");
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		
		//lSql.append("INNER JOIN req.empreendimentoCollection emp ");
		lSql.append("INNER JOIN req.empreendimentoRequerimentoCollection empReq ");
		lSql.append("INNER JOIN empReq.ideEmpreendimento emp ");
		
		lSql.append("INNER JOIN tran.ideStatusRequerimento status ");
		lSql.append("WHERE tran.dtcMovimentacao = (SELECT MAX(req_tran.dtcMovimentacao) FROM req.tramitacaoRequerimentoCollection req_tran) ");
		lSql.append(" AND emp.ideEmpreendimento = :ideEmpreendimento ");

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ideEmpreendimento", empreendimento.getIdeEmpreendimento());
		List<StatusRequerimento> result = daoStatusRequerimento.listarPorQuery(lSql.toString(), parametros);
		return result;

	}

}