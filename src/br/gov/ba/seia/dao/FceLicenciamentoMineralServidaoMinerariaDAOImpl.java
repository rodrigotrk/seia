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
import br.gov.ba.seia.entity.FceLicenciamentoMineralServidaoMineraria;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralServidaoMinerariaDAOImpl {

	@Inject
	private IDAO<FceLicenciamentoMineralServidaoMineraria> iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLicenciamentoMineralServidaoMineraria(FceLicenciamentoMineralServidaoMineraria servidaoMineraria)  {
		iDAO.salvarOuAtualizar(servidaoMineraria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralServidaoMineraria> listarFceLicenciamentoMineralServidaoMinerariaBy(FceLicenciamentoMineral fceLicenciamentoMineral) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralServidaoMineraria.class)
				.createAlias("fceLicenciamentoMineral", "flm")
				.createAlias("servidaoMineraria", "ser")
				.add(Restrictions.eq("flm.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral())
				).setProjection(Projections.projectionList()
						.add(Projections.property("areaServidaoMineraria"), "areaServidaoMineraria")
						.add(Projections.property("flm.ideFceLicenciamentoMineral"), "fceLicenciamentoMineral.ideFceLicenciamentoMineral")
						.add(Projections.property("ser.ideServidaoMineraria"), "servidaoMineraria.ideServidaoMineraria")
						.add(Projections.property("ser.nomServidaoMineraria"), "servidaoMineraria.nomServidaoMineraria")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(FceLicenciamentoMineralServidaoMineraria.class)
						);
		return iDAO.listarPorCriteria(criteria);
	}

}
