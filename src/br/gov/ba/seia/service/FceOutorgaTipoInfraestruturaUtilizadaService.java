package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceOutorgaInfraestrutura;
import br.gov.ba.seia.entity.FceOutorgaTipoInfraestruturaUtilizada;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceOutorgaTipoInfraestruturaUtilizadaService {

	@Inject
	private IDAO<FceOutorgaTipoInfraestruturaUtilizada> fceOutorgaTipoInfraestruturaUtilizadaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceOutorgaTipoInfraestruturaUtilizada tipoInfraUtilizada)  {
		fceOutorgaTipoInfraestruturaUtilizadaDAO.salvar(tipoInfraUtilizada);
	}
	
	public List<FceOutorgaTipoInfraestruturaUtilizada> listarPorFceOutorgaTipoInfraestrutura(FceOutorgaInfraestrutura infra)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaTipoInfraestruturaUtilizada.class)
				.createAlias("ideFceOutorgaInfraestrutura", "infra", JoinType.INNER_JOIN)
				.createAlias("ideFceOutorgaTipoInfraestrutura", "tipoInfra", JoinType.INNER_JOIN)
				
				.add(Restrictions.eq("infra.ideFceOutorgaInfraestrutura", infra.getIdeFceOutorgaInfraestrutura()));
		
		return fceOutorgaTipoInfraestruturaUtilizadaDAO.listarPorCriteria(criteria);
	}
	
	public void excluirPorFceOutorgaInfraestrutura(FceOutorgaInfraestrutura infra) {
		try {
			Map<String,Object> params = new HashMap<String, Object>();
			params.put("id", infra.getIdeFceOutorgaInfraestrutura());
			
			StringBuilder sql = new StringBuilder();
			sql.append("delete from FceOutorgaTipoInfraestruturaUtilizada where ideFceOutorgaInfraestrutura.ideFceOutorgaInfraestrutura = :id");
			
			fceOutorgaTipoInfraestruturaUtilizadaDAO.executarQuery(sql.toString(), params);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
