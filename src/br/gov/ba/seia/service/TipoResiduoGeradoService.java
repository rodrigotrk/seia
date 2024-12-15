package br.gov.ba.seia.service;
/**
 * 		09/05/14
 * @author marcelo.brito
 */

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTipoResiduo;
import br.gov.ba.seia.entity.TipoResiduoGerado;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoResiduoGeradoService {

	@Inject
	private IDAO<TipoResiduoGerado> tipoResiduoGeradoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoResiduoGerado> buscarListaTipoResiduoGerado() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoResiduoGerado.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoResiduoGeradoIDAO.listarPorCriteria(criteria, Order.asc("ideTipoResiduoGerado"));
	}

	/**
	 * Método que vai retornar os {@link TipoResiduoGerado} que foram associados
	 * ao {@link FceLicenciamentoMineral}.
	 * 
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	public List<TipoResiduoGerado> listarTipoResiduoGeradoBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralTipoResiduo.class)
				.createAlias("tipoResiduoGerado", "rg")
				.add(Restrictions.eq("fceLicenciamentoMineral.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("rg.ideTipoResiduoGerado"),"ideTipoResiduoGerado")
						.add(Projections.property("rg.dscTipoResiduoGerado"), "dscTipoResiduoGerado")
						.add(Projections.property("rg.indAtivo"), "indAtivo")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoResiduoGerado.class));
		return tipoResiduoGeradoIDAO.listarPorCriteria(criteria);
	}

}