package br.gov.ba.seia.service;

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
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceOutorgaInfraestrutura;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceOutorgaInfraestruturaService {

	@Inject
	private IDAO<FceOutorgaInfraestrutura> fceOutorgaInfraestruturaDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceOutorgaInfraestrutura buscarFceOutorgaInfraestruturaPorFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceOutorgaInfraestrutura.class)
				.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()))
				.createAlias("ideFce", "fce", JoinType.INNER_JOIN)
				.createAlias("ideTipoPeriodoDerivacao", "tipoPeriodo", JoinType.INNER_JOIN);
		
		return fceOutorgaInfraestruturaDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FceOutorgaInfraestrutura infra) {
		try {
			fceOutorgaInfraestruturaDAO.salvarOuAtualizar(infra);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
