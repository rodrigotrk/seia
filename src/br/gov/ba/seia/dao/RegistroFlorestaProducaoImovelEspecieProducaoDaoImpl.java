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

import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelEspecieProducao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistroFlorestaProducaoImovelEspecieProducaoDaoImpl {

	@Inject
	private IDAO<RegistroFlorestaProducaoImovelEspecieProducao> registroFlorestaProducaoImovelEspecieProducaoIDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoImovelEspecieProducao(RegistroFlorestaProducaoImovelEspecieProducao ideRegistroFlorestaProducaoImovelEspecieProducao) {
		try {
			if(ideRegistroFlorestaProducaoImovelEspecieProducao.getIdeRegistroFlorestaProducaoImovelEspecieProducao()!=null){
				registroFlorestaProducaoImovelEspecieProducaoIDAO.remover(ideRegistroFlorestaProducaoImovelEspecieProducao);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoImovelEspecieProducao(List<RegistroFlorestaProducaoImovelEspecieProducao> ideRegistroFlorestaProducaoImovelEspecieProducao) {
		try {
			for (RegistroFlorestaProducaoImovelEspecieProducao registroFlorestaProducaoImovelEspecieProducao : ideRegistroFlorestaProducaoImovelEspecieProducao) {
				if(registroFlorestaProducaoImovelEspecieProducao.getIdeRegistroFlorestaProducaoImovelEspecieProducao()!=null){
					registroFlorestaProducaoImovelEspecieProducaoIDAO.remover(registroFlorestaProducaoImovelEspecieProducao);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovelEspecieProducao(List<RegistroFlorestaProducaoImovelEspecieProducao> registroFlorestaProducaoImovelEspecieProducaoList) {
		try {
			registroFlorestaProducaoImovelEspecieProducaoIDAO.salvarEmLote(registroFlorestaProducaoImovelEspecieProducaoList);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegistroFlorestaProducaoImovelEspecieProducao> listarRegistroFlorestaProducaoImovelEspecieProducao(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel) {
		try {
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RegistroFlorestaProducaoImovelEspecieProducao.class)
				.createAlias("ideRegistroFlorestaProducaoImovel", "ideRegistroFlorestaProducaoImovel")
				.createAlias("ideEspecieFloresta", "ideEspecieFloresta")
				.createAlias("ideEspecieFloresta.ideNaturezaEspecieFloresta", "ideNaturezaEspecieFloresta")

				.add(Restrictions.eq("ideRegistroFlorestaProducaoImovel.ideRegistroFlorestaProducaoImovel", ideRegistroFlorestaProducaoImovel.getIdeRegistroFlorestaProducaoImovel()))
				
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideRegistroFlorestaProducaoImovelEspecieProducao"),"ideRegistroFlorestaProducaoImovelEspecieProducao")
					.add(Projections.property("valCicloCorte"),"valCicloCorte")
					.add(Projections.property("valEspacamento"),"valEspacamento")
					.add(Projections.property("valEstimativaVolumeTotalFinal"),"valEstimativaVolumeTotalFinal")
					.add(Projections.property("valIma"),"valIma")
					
					.add(Projections.property("ideEspecieFloresta.ideEspecieFloresta"),"ideEspecieFloresta.ideEspecieFloresta")
					.add(Projections.property("ideEspecieFloresta.dtcCriacao"),"ideEspecieFloresta.dtcCriacao")
					.add(Projections.property("ideEspecieFloresta.dtcExclusao"),"ideEspecieFloresta.dtcExclusao")
					.add(Projections.property("ideEspecieFloresta.indExcluido"),"ideEspecieFloresta.indExcluido")
					.add(Projections.property("ideEspecieFloresta.nomEspecieFloresta"),"ideEspecieFloresta.nomEspecieFloresta")
					
					.add(Projections.property("ideNaturezaEspecieFloresta.ideNaturezaEspecieFloresta"),"ideEspecieFloresta.ideNaturezaEspecieFloresta.ideNaturezaEspecieFloresta")
					.add(Projections.property("ideNaturezaEspecieFloresta.dtcCriacao"),"ideEspecieFloresta.ideNaturezaEspecieFloresta.dtcCriacao")
					.add(Projections.property("ideNaturezaEspecieFloresta.dtcExclusao"),"ideEspecieFloresta.ideNaturezaEspecieFloresta.dtcExclusao")
					.add(Projections.property("ideNaturezaEspecieFloresta.indExcluido"),"ideEspecieFloresta.ideNaturezaEspecieFloresta.indExcluido")
					.add(Projections.property("ideNaturezaEspecieFloresta.nomNaturezaEspecieFloresta"),"ideEspecieFloresta.ideNaturezaEspecieFloresta.nomNaturezaEspecieFloresta")
					
					.add(Projections.property("ideRegistroFlorestaProducaoImovel"),"ideRegistroFlorestaProducaoImovel")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(RegistroFlorestaProducaoImovelEspecieProducao.class));
			
			return registroFlorestaProducaoImovelEspecieProducaoIDAO.listarPorCriteria(detachedCriteria);
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
}
