package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.DeclaracaoQueimaControlada;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaTecnicaUtilizada;
import br.gov.ba.seia.entity.TecnicaQueimaControlada;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TecnicaQueimaControladaDaoImpl {
	
	@Inject
	private IDAO<TecnicaQueimaControlada> tqcDAO;
	
	@Inject
	private IDAO<DeclaracaoQueimaControladaTecnicaUtilizada> dqcTqcDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaQueimaControlada> listarTodos() {
		try {
			return tqcDAO.listarTodos();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDQCTecnicaUtilizada(DeclaracaoQueimaControladaTecnicaUtilizada dqcTqc) {
		try {
			dqcTqcDAO.salvarOuAtualizar(dqcTqc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDQCTecnicaUtilizada(DeclaracaoQueimaControladaTecnicaUtilizada dqcTqc) {
		try {
			dqcTqcDAO.remover(dqcTqc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoQueimaControladaTecnicaUtilizada> listarPorDeclaracaoQueimaControlada(DeclaracaoQueimaControlada dqc) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoQueimaControladaTecnicaUtilizada.class)
					.createAlias("ideDeclaracaoQueimaControlada", "dqc", JoinType.INNER_JOIN)
					.createAlias("ideTecnicaQueimaControlada", "tqc", JoinType.INNER_JOIN)
					
					.setProjection
						(Projections.projectionList()
							.add(Projections.property("ideDeclaracaoQueimaControladaTecnicaUtilizada"), "ideDeclaracaoQueimaControladaTecnicaUtilizada")
							.add(Projections.property("dqc.ideDeclaracaoQueimaControlada"), "ideDeclaracaoQueimaControlada.ideDeclaracaoQueimaControlada")
							.add(Projections.property("tqc.ideTecnicaQueimaControlada"), "ideTecnicaQueimaControlada.ideTecnicaQueimaControlada")
							.add(Projections.property("tqc.nomTecnicaQueimaControlada"), "ideTecnicaQueimaControlada.nomTecnicaQueimaControlada")
						)
					
					.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoQueimaControladaTecnicaUtilizada.class))
					
					.add(Restrictions.eq("dqc.ideDeclaracaoQueimaControlada", dqc.getIdeDeclaracaoQueimaControlada()));
					 
				 return dqcTqcDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}