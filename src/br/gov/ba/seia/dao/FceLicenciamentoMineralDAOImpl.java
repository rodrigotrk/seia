package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralDAOImpl {

	@Inject
	private IDAO<FceLicenciamentoMineral> fceLicenciamentoMineralIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceLicenciamentoMineral buscarFceLicenciamentoMineralByFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineral.class)
			.createAlias("ideFce", "f", JoinType.INNER_JOIN)
			.createAlias("ideLocalizacaoGeograficaLavra", "locLavra", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideLocalizacaoGeograficaServidao", "locServidao", JoinType.LEFT_OUTER_JOIN)
			.add(Restrictions.eq("f.ideFce", fce.getIdeFce()));
		return fceLicenciamentoMineralIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		fceLicenciamentoMineralIDAO.salvarOuAtualizar(fceLicenciamentoMineral);
	}
}
