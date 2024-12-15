package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.MetodoProspeccao;
import br.gov.ba.seia.entity.SubstanciaMineral;

/**
 * Service de {@link SubstanciaMineral}
 * 
 * @author alexandre.queiroz
 * @since 10/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MetodoProspeccaoService {

	@Inject
	private IDAO<MetodoProspeccao> metodoProspeccaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoProspeccao> listarMetodoProspeccao()  {
		return metodoProspeccaoDAO.listarPorCriteria(DetachedCriteria.forClass(MetodoProspeccao.class), Order.asc("ideMetodoProspeccao"));
	}
}
