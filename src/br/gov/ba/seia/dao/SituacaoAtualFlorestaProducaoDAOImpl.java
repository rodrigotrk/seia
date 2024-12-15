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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelPlantio;
import br.gov.ba.seia.entity.SituacaoAtualFlorestaProducao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SituacaoAtualFlorestaProducaoDAOImpl {

	@Inject
	private IDAO<SituacaoAtualFlorestaProducao> situacaoAtualFlorestaProducaoImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SituacaoAtualFlorestaProducao> listarSituacaoAtualFlorestaProducao(){
		try {
			return 
				situacaoAtualFlorestaProducaoImpl.listarPorCriteria(
					DetachedCriteria.forClass(SituacaoAtualFlorestaProducao.class)
						.setProjection(Projections.projectionList()
							.add(Projections.property("ideSituacaoAtualFlorestaProducao"),"ideSituacaoAtualFlorestaProducao")
							.add(Projections.property("desSituacaoFlorestaProducao"),"desSituacaoFlorestaProducao")
							.add(Projections.property("dtcCriacao"),"dtcCriacao")
							.add(Projections.property("dtcExclusao"),"dtcExclusao")
							.add(Projections.property("indExcluido"),"indExcluido")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(SituacaoAtualFlorestaProducao.class)),Order.asc("ideSituacaoAtualFlorestaProducao"));
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SituacaoAtualFlorestaProducao buscarSituacaoAtualFlorestaProducao(RegistroFlorestaProducaoImovelPlantio registroFlorestaProducaoImovelPlantio) {
		try {
			return 
				situacaoAtualFlorestaProducaoImpl.buscarPorCriteria(
					DetachedCriteria.forClass(RegistroFlorestaProducaoImovelPlantio.class)
						.createAlias("ideSituacaoAtualFlorestaProducao", "ideSituacaoAtualFlorestaProducao", JoinType.INNER_JOIN)
					
						.setProjection(Projections.projectionList()
							.add(Projections.property("ideSituacaoAtualFlorestaProducao.ideSituacaoAtualFlorestaProducao"),"ideSituacaoAtualFlorestaProducao")
							.add(Projections.property("ideSituacaoAtualFlorestaProducao.desSituacaoFlorestaProducao"),"desSituacaoFlorestaProducao")
							.add(Projections.property("ideSituacaoAtualFlorestaProducao.dtcCriacao"),"dtcCriacao")
							.add(Projections.property("ideSituacaoAtualFlorestaProducao.dtcExclusao"),"dtcExclusao")
							.add(Projections.property("ideSituacaoAtualFlorestaProducao.indExcluido"),"indExcluido")
							
						).setResultTransformer(new AliasToNestedBeanResultTransformer(SituacaoAtualFlorestaProducao.class)));
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	

	
}
