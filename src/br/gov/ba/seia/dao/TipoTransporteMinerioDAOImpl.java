package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTransporteMineiro;
import br.gov.ba.seia.entity.TipoTransporteMinerio;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoTransporteMinerioDAOImpl {

	@Inject
	private IDAO<TipoTransporteMinerio> tipoTransporteMinerioIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoTransporteMinerio> listarTipoTransporteMinerio()  {
		return tipoTransporteMinerioIDAO.listarTodos();
	}

	/**
	 * Método que vai retornar os {@link TipoTransporteMinerio} que foram associados
	 * ao {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param licenciamentoMineral
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoTransporteMinerio> listarTipoTransporteMinerioBy(FceLicenciamentoMineral licenciamentoMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralTransporteMineiro.class)
				.createAlias("ideTipoTransporteMinerio", "tm")
				.add(Restrictions.eq("fceLicenciamentoMineral.ideFceLicenciamentoMineral", licenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("tm.ideTipoTransporteMinerio"),"ideTipoTransporteMinerio")
						.add(Projections.property("tm.nomTipoTransporteMinerio"), "nomTipoTransporteMinerio"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoTransporteMinerio.class));
		return tipoTransporteMinerioIDAO.listarPorCriteria(criteria);
	}
}
