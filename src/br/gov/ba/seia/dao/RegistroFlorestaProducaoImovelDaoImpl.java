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

import br.gov.ba.seia.entity.RegistroFlorestaProducao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovel;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistroFlorestaProducaoImovelDaoImpl {

	@Inject
	private IDAO<RegistroFlorestaProducaoImovel> registroFlorestaProducaoImovelIDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel registroFlorestaProducaoImovel){
		try {
			registroFlorestaProducaoImovelIDao.salvar(registroFlorestaProducaoImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarRegistroFlorestaProducaoImovel(List<RegistroFlorestaProducaoImovel> registroFlorestaProducaoImovel){
		try {
			registroFlorestaProducaoImovelIDao.salvarEmLote(registroFlorestaProducaoImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegistroFlorestaProducaoImovel> listarRegistroFlorestaProducaoImovel(RegistroFlorestaProducao ideRegistroFlorestaProducao) {
	 	
		try {
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RegistroFlorestaProducaoImovel.class)
				.add(Restrictions.eq("ideRegistroFlorestaProducao.ideRegistroFlorestaProducao", ideRegistroFlorestaProducao.getIdeRegistroFlorestaProducao()))
		
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideRegistroFlorestaProducaoImovel"),"ideRegistroFlorestaProducaoImovel")
					.add(Projections.property("ideImovel.ideImovel"),"ideImovel.ideImovel")
					.add(Projections.property("ideRegistroFlorestaProducao.ideRegistroFlorestaProducao"),"ideRegistroFlorestaProducao.ideRegistroFlorestaProducao")
					.add(Projections.property("indArrendado"),"indArrendado")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(RegistroFlorestaProducaoImovel.class));
	
			return registroFlorestaProducaoImovelIDao.listarPorCriteria(detachedCriteria);
		
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirIdeRegistroFlorestaProducaoImovel(RegistroFlorestaProducaoImovel ideRegistroFlorestaProducaoImovel) {
		try {
			if(ideRegistroFlorestaProducaoImovel.getIdeRegistroFlorestaProducaoImovel()!=null){
				registroFlorestaProducaoImovelIDao.remover(ideRegistroFlorestaProducaoImovel);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
}
