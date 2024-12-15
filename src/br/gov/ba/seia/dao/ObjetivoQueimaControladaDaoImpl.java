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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.DeclaracaoQueimaControladaImovel;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaImovelObjetivoQueimaControlada;
import br.gov.ba.seia.entity.ObjetivoQueimaControlada;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ObjetivoQueimaControladaDaoImpl {
	
	@Inject
	private IDAO<ObjetivoQueimaControlada> oqcDAO;
	
	@Inject
	private IDAO<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada> dqciOqcDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ObjetivoQueimaControlada> listarTodos() {
		try {
			return oqcDAO.listarPorCriteria(
					DetachedCriteria.forClass(ObjetivoQueimaControlada.class).add(Restrictions.eq("indExcluido", false)), Order.asc("ideObjetivoQueimaControlada"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDQCIObjetivoQueimaControlada(DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqciOqc) {
		try {
			dqciOqcDAO.salvarOuAtualizar(dqciOqc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDQCIObjetivoQueimaControlada(DeclaracaoQueimaControladaImovelObjetivoQueimaControlada dqciOqc) {
		try {
			dqciOqcDAO.remover(dqciOqc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoQueimaControladaImovelObjetivoQueimaControlada> listarPorDeclaracaoQueimaControladaImovel(DeclaracaoQueimaControladaImovel dqci) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoQueimaControladaImovelObjetivoQueimaControlada.class)
					.createAlias("ideDeclaracaoQueimaControladaImovel", "dqci", JoinType.INNER_JOIN)
					.createAlias("ideObjetivoQueimaControlada", "oqc", JoinType.INNER_JOIN)
					
					.setProjection
						(Projections.projectionList()
							.add(Projections.property("ideDeclaracaoQueimaControladaImovelObjetivoQueimaControlada"), "ideDeclaracaoQueimaControladaImovelObjetivoQueimaControlada")
							.add(Projections.property("valAreaPrevistaQueima"), "valAreaPrevistaQueima")
							
							.add(Projections.property("dqci.ideDeclaracaoQueimaControladaImovel"), "ideDeclaracaoQueimaControladaImovel.ideDeclaracaoQueimaControladaImovel")
							.add(Projections.property("dqci.indArrendado"), "ideDeclaracaoQueimaControladaImovel.indArrendado")
							
							.add(Projections.property("oqc.ideObjetivoQueimaControlada"), "ideObjetivoQueimaControlada.ideObjetivoQueimaControlada")
							.add(Projections.property("oqc.desObjetivoQueimaControlada"), "ideObjetivoQueimaControlada.desObjetivoQueimaControlada")
							.add(Projections.property("oqc.indPossuiAreaPrevista"), "ideObjetivoQueimaControlada.indPossuiAreaPrevista")
						)
					
					.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoQueimaControladaImovelObjetivoQueimaControlada.class))
					
					.add(Restrictions.eq("dqci.ideDeclaracaoQueimaControladaImovel", dqci.getIdeDeclaracaoQueimaControladaImovel()))
					.add(Restrictions.eq("oqc.indExcluido", false));
					 
				 return dqciOqcDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}