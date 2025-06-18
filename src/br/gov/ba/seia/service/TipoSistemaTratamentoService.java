package br.gov.ba.seia.service;

/**
 * 		09/05/14
 * @author marcelo.brito
 */

import java.util.Collections;
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
import br.gov.ba.seia.entity.FceLicenciamentoMineralSistemaTratamento;
import br.gov.ba.seia.entity.TipoSistemaTratamento;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoSistemaTratamentoService {

	private static final int IDE_OUTROS = 5;

	@Inject
	private IDAO<TipoSistemaTratamento> tipoSistemaTratamentoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoSistemaTratamento> buscarListaTipoSistemaTratamento() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(TipoSistemaTratamento.class);
		criteria.add(Restrictions.eq("indAtivo", true));
		return retornarListaOrdenada(tipoSistemaTratamentoIDAO.listarPorCriteria(criteria, Order.asc("ideTipoSistemaTratamento")));
	}

	/**
	 * Método para deixar a opção <b>OUTROS</b> no fim da lista.
	 * 
	 * @param lista
	 * @return
	 * @author eduardo.fernandes
	 * @since 10/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 */
	private List<TipoSistemaTratamento> retornarListaOrdenada(List<TipoSistemaTratamento> lista) {
		TipoSistemaTratamento outros = lista.get(IDE_OUTROS);
		lista.remove(outros);
		Collections.sort(lista);
		lista.add(outros);
		return lista;
	}

	/**
	 * Método que vai retornar os {@link TipoSistemaTratamento} que foram
	 * associados ao {@link FceLicenciamentoMineral}.
	 * 
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	public List<TipoSistemaTratamento> listarTipoSistemaTratamentoBy(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralSistemaTratamento.class)
				.createAlias("tipoSistemaTratamento", "sis")
				.add(Restrictions.eq("fceLicenciamentoMineral.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("sis.ideTipoSistemaTratamento"),"ideTipoSistemaTratamento")
						.add(Projections.property("sis.dscTipoSistemaTratamento"), "dscTipoSistemaTratamento")
						.add(Projections.property("sis.indAtivo"), "indAtivo")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(TipoSistemaTratamento.class));
		 return tipoSistemaTratamentoIDAO.listarPorCriteria(criteria);
	}
}
