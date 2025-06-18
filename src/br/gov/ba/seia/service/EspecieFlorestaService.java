package br.gov.ba.seia.service;

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
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.EspecieFloresta;
import br.gov.ba.seia.entity.NaturezaEspecieFloresta;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EspecieFlorestaService {

	@Inject
	private IDAO<EspecieFloresta> especieFlorestaDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<EspecieFloresta> listarEspecieFloresta(NaturezaEspecieFloresta naturezaEspecieFloresta) {
		try {
			
			DetachedCriteria criteria = DetachedCriteria.forClass(EspecieFloresta.class)
					.add(Restrictions.eq("ideNaturezaEspecieFloresta.ideNaturezaEspecieFloresta", naturezaEspecieFloresta.getIdeNaturezaEspecieFloresta()));
			
			return especieFlorestaDAO.listarPorCriteria(criteria, Order.asc("nomEspecieFloresta"));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	


}
