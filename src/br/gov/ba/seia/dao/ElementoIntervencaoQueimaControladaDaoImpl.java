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
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaElementoIntervencao;
import br.gov.ba.seia.entity.ElementoIntervencaoQueimaControlada;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ElementoIntervencaoQueimaControladaDaoImpl {
	
	@Inject
	private IDAO<ElementoIntervencaoQueimaControlada> eiqcDAO;
	
	@Inject
	private IDAO<DeclaracaoQueimaControladaElementoIntervencao> dqcEiDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ElementoIntervencaoQueimaControlada> listarTodos() {
		try {
			return eiqcDAO.listarTodos();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDQCElementoIntervencao(DeclaracaoQueimaControladaElementoIntervencao dqcEi) {
		try {
			dqcEiDAO.salvarOuAtualizar(dqcEi);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDQCElementoIntervencao(DeclaracaoQueimaControladaElementoIntervencao dqcEi) {
		try {
			dqcEiDAO.remover(dqcEi);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoQueimaControladaElementoIntervencao> listarPorDeclaracaoQueimaControlada(DeclaracaoQueimaControlada dqc) {
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoQueimaControladaElementoIntervencao.class)
					.createAlias("ideDeclaracaoQueimaControlada", "dqc", JoinType.INNER_JOIN)
					.createAlias("ideElementoIntervencaoQueimaControlada", "eqc", JoinType.INNER_JOIN)
					
					.setProjection
						(Projections.projectionList()
							.add(Projections.property("ideDeclaracaoQueimaControladaElementoIntervencao"), "ideDeclaracaoQueimaControladaElementoIntervencao")
							.add(Projections.property("valDistancia"), "valDistancia")
							.add(Projections.property("dqc.ideDeclaracaoQueimaControlada"), "ideDeclaracaoQueimaControlada.ideDeclaracaoQueimaControlada")
							.add(Projections.property("eqc.ideElementoIntervencaoQueimaControlada"), "ideElementoIntervencaoQueimaControlada.ideElementoIntervencaoQueimaControlada")
							.add(Projections.property("eqc.nomElementoIntervencao"), "ideElementoIntervencaoQueimaControlada.nomElementoIntervencao")
						)
					
					.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoQueimaControladaElementoIntervencao.class))
					
					.add(Restrictions.eq("dqc.ideDeclaracaoQueimaControlada", dqc.getIdeDeclaracaoQueimaControlada()));
					 
				 return dqcEiDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}