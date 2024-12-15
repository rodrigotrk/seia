package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.gov.ba.seia.entity.FceLicenciamentoMineralSubstanciaMineralTipologia;
import br.gov.ba.seia.entity.SubstanciaMineralTipologia;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 13/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralSubstanciaMineralDAOImpl {

	@Inject
	private IDAO<FceLicenciamentoMineralSubstanciaMineralTipologia> iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLicenciamentoMineralSubstanciaMineral(FceLicenciamentoMineralSubstanciaMineralTipologia substanciaMineral) {
		iDAO.salvarOuAtualizar(substanciaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralSubstanciaMineralTipologia> listarFceLicenciamentoMineralSubstanciaMineralBy(FceLicenciamentoMineral fceLicenciamentoMineral) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralSubstanciaMineralTipologia.class)
				.createAlias("fceLicenciamentoMineral", "flm")
				.createAlias("substanciaMineralTipologia", "smt")
				.createAlias("smt.ideSubstanciaMineral", "sub")
				.createAlias("smt.ideTipologia", "tip")
				.createAlias("tip.ideTipologiaPai", "pai")
				.add(Restrictions.eq("flm.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral())
				).setProjection(Projections.projectionList()
						.add(Projections.property("numProducaoAnual"), "numProducaoAnual")
						.add(Projections.property("flm.ideFceLicenciamentoMineral"), "fceLicenciamentoMineral.ideFceLicenciamentoMineral")
						.add(Projections.property("smt.ideSubstanciaMineralTipologia"), "substanciaMineralTipologia.ideSubstanciaMineralTipologia")
						.add(Projections.property("sub.ideSubstanciaMineral"), "substanciaMineralTipologia.ideSubstanciaMineral.ideSubstanciaMineral")
						.add(Projections.property("sub.nomSubstanciaMineral"), "substanciaMineralTipologia.ideSubstanciaMineral.nomSubstanciaMineral")
						.add(Projections.property("tip.ideTipologia"), "substanciaMineralTipologia.ideTipologia.ideTipologia")
						.add(Projections.property("tip.desTipologia"), "substanciaMineralTipologia.ideTipologia.desTipologia")
						.add(Projections.property("tip.codTipologia"), "substanciaMineralTipologia.ideTipologia.codTipologia")
						.add(Projections.property("pai.ideTipologia"), "substanciaMineralTipologia.ideTipologia.ideTipologiaPai.ideTipologia")
						.add(Projections.property("pai.desTipologia"), "substanciaMineralTipologia.ideTipologia.ideTipologiaPai.desTipologia")
						.add(Projections.property("pai.codTipologia"), "substanciaMineralTipologia.ideTipologia.ideTipologiaPai.codTipologia")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(FceLicenciamentoMineralSubstanciaMineralTipologia.class)
						);
		return iDAO.listarPorCriteria(criteria);
	}

	/**
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaFceLicenciamentoMineralSubstanciaMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral());
		iDAO.executarNamedQuery("FceLicenciamentoMineralSubstanciaMineralTipologia.removeByIdeFceLicenciamentoMineral", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFceLicenciamentoMineralSubstanciaMineral(FceLicenciamentoMineral fceLicenciamentoMineral, SubstanciaMineralTipologia substanciaMineralTipologia) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral());
		parameters.put("ideSubstanciaMineralTipologia", substanciaMineralTipologia.getIdeSubstanciaMineralTipologia());
		iDAO.executarNamedQuery("FceLicenciamentoMineralSubstanciaMineralTipologia.removeByIdeFceLicenciamentoMineralAndIdeSubstanciaMineralTipologia", parameters);
	}

}
