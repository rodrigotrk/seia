package br.gov.ba.seia.service;
/**
 * 		09/05/14
 * @author marcelo.brito
 */

import java.util.ArrayList;
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
import br.gov.ba.seia.entity.FceLicenciamentoMineralOrigemEnergia;
import br.gov.ba.seia.entity.RegimeExploracao;
import br.gov.ba.seia.entity.TipoOrigemEnergia;
import br.gov.ba.seia.enumerator.TipoOrigemEnergiaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoOrigemEnergiaService {

	@Inject
	private IDAO<TipoOrigemEnergia> tipoOrigemEnergiaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoOrigemEnergia> buscarListaTipoOrigemEnergia() throws Exception{
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoOrigemEnergia.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return tipoOrigemEnergiaIDAO.listarPorCriteria(criteria, Order.asc("ideTipoOrigemEnergia"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoOrigemEnergia> buscarListaTipoOrigemEnergiaForListTipoOrigemEnergiaEnum(List<TipoOrigemEnergiaEnum> listTipoOrigemEnergiaEnum) throws Exception{
		List<TipoOrigemEnergia> lista = new ArrayList<TipoOrigemEnergia>();
		for(TipoOrigemEnergiaEnum origemEnergiaEnum : listTipoOrigemEnergiaEnum) {
			DetachedCriteria criteria = DetachedCriteria.forClass(TipoOrigemEnergia.class);
			criteria.add(Restrictions.eq("ideTipoOrigemEnergia", origemEnergiaEnum.getId()));
			lista.add(tipoOrigemEnergiaIDAO.buscarPorCriteria(criteria));
		}
		return lista;
	}

	/**
	 * Método que vai retornar os {@link RegimeExploracao} que foram associados
	 * ao {@link FceLicenciamentoMineral}.
	 * 
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param licenciamentoMineral
	 * @return
	 * @throws Exception 
	 */
	public List<TipoOrigemEnergia> listarTipoOrigemEnergiaBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		 DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralOrigemEnergia.class)
					.createAlias("ideTipoOrigemEnergia", "toe")
					.add(Restrictions.eq("fceLicenciamentoMineral.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral()))
					.setProjection(Projections.projectionList()
							.add(Projections.property("toe.ideTipoOrigemEnergia"),"ideTipoOrigemEnergia")
							.add(Projections.property("toe.dscTipoOrigemEnergia"), "dscTipoOrigemEnergia")
							.add(Projections.property("toe.indAtivo"), "indAtivo"))
							.setResultTransformer(new AliasToNestedBeanResultTransformer(TipoOrigemEnergia.class));
			 return tipoOrigemEnergiaIDAO.listarPorCriteria(criteria);
	}

}
