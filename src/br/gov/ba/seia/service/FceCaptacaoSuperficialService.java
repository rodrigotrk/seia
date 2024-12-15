/**
 * 		05/12/13
 * @author eduardo.fernandes
 */
package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceCaptacaoSuperficial;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCaptacaoSuperficialService {

	@Inject
	private IDAO<FceCaptacaoSuperficial> fceCaptacaoSuperficialIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceCaptacaoSuperficial buscarFceCaptacaoSuperficialByIdeFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCaptacaoSuperficial.class);
		criteria.createAlias("ideFceOutorgaLocalizacaoGeografica", "fceLocGeo");
		criteria.createAlias("fceLocGeo.ideOutorgaLocalizacaoGeografica", "outLocGeo", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("fceLocGeo.localCaptacao", "lc", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("outLocGeo.ideLocalizacaoGeografica", "locGeo", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("locGeo.dadoGeograficoCollection", "dg",JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("caracteristicaCaptacao", "carCap");
		criteria.createAlias("caracteristicaSistemaCaptacao", "carSisCap");
		criteria.createAlias("ideDocumentoObrigatorioRequerimento", "doc");
		criteria.createAlias("ideDominioBarramento", "domBar", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipoValorVolume", "tvVolume", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("tipoValorVazao", "tvVazao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideFceOutorgaLocalizacaoGeografica.ideFceOutorgaLocalizacaoGeografica", fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica()));
		return fceCaptacaoSuperficialIDAO.buscarPorCriteriaMaxResult(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceCaptacaoSuperficial(FceCaptacaoSuperficial fceCaptacaoSuperficial) {
		fceCaptacaoSuperficialIDAO.salvarOuAtualizar(fceCaptacaoSuperficial);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceCaptacaoSuperficial> listarFceCaptacaoSuperficial(Fce fce) {

		DetachedCriteria criteria = DetachedCriteria.forClass(FceCaptacaoSuperficial.class);
		criteria.createAlias("ideFceOutorgaLocalizacaoGeografica", "fceLoc");
		criteria.createAlias("fceLoc.ideFce", "fce");
		criteria.add(Restrictions.eq("fce.ideFce", fce.getIdeFce()));

		return fceCaptacaoSuperficialIDAO.listarPorCriteria(criteria);
	}
}