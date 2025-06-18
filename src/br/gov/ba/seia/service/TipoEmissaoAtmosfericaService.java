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
import br.gov.ba.seia.entity.FceLicenciamentoMineralEmissaoAtmosferica;
import br.gov.ba.seia.entity.TipoEmissaoAtmosferica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoEmissaoAtmosfericaService {

	@Inject
	private IDAO<TipoEmissaoAtmosferica> tipoEmissaoAtmosfericaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoEmissaoAtmosferica> buscarListaTipoEmissaoAtmosferica() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoEmissaoAtmosferica.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoEmissaoAtmosfericaIDAO.listarPorCriteria(criteria, Order.asc("ideTipoEmissaoAtmosferica"));
	}

	/**
	 * Método que vai retornar os {@link TipoEmissaoAtmosferica} que foram
	 * associados ao {@link FceLicenciamentoMineral}.
	 * 
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	public List<TipoEmissaoAtmosferica> listaTipoEmissaoAtmosfericaSalvoBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralEmissaoAtmosferica.class)
				.createAlias("tipoEmissaoAtmosferica", "emi")
				.add(Restrictions.eq("fceLicenciamentoMineral.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("emi.ideTipoEmissaoAtmosferica"),"ideTipoEmissaoAtmosferica")
						.add(Projections.property("emi.dscTipoEmissaoAtmosferica"), "dscTipoEmissaoAtmosferica")
						.add(Projections.property("emi.indAtivo"), "indAtivo")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoEmissaoAtmosferica.class));
		 return tipoEmissaoAtmosfericaIDAO.listarPorCriteria(criteria);
	}
}



