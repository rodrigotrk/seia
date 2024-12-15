/**
 * 		03/04/14
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
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLancamentoEfluentesService {

	@Inject
	private IDAO<FceLancamentoEfluente> fceLancamentoEfluentesIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceLancamentoEfluente buscarLancamentoEfluenteByIdeFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLancamentoEfluente.class)
			.createAlias("ideFceOutorgaLocalizacaoGeografica", "fceLocGeo")
			.createAlias("ideTipoPeriodoDerivacao", "periodo")
			.createAlias("ideDocumentoObrigatorioRequerimento", "doc", JoinType.LEFT_OUTER_JOIN);
			
			if(Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeFce().getIdeAnaliseTecnica())){
				criteria
					.createAlias("fceLocGeo.ideOutorgaLocalizacaoGeografica", "olg")
					.createAlias("olg.ideLocalizacaoGeografica", "lg");
			}else {
				criteria
					.createAlias("fceLocGeo.ideLocalizacaoGeografica", "lg");
			}
		
			criteria
				.add(Restrictions.eq("ideFceOutorgaLocalizacaoGeografica.ideFceOutorgaLocalizacaoGeografica", fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica()));
				
		return fceLancamentoEfluentesIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLancamentoEfluentes(FceLancamentoEfluente fceLancamentoEfluente) {
		fceLancamentoEfluentesIDAO.salvarOuAtualizar(fceLancamentoEfluente);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLancamentoEfluente> listarFceLancamentoEfluentesByFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLancamentoEfluente.class);
		criteria.createAlias("ideFceOutorgaLocalizacaoGeografica", "fceLoc");
		criteria.createAlias("fceLoc.ideFce", "fce");
		criteria.add(Restrictions.eq("fce.ideFce", fce.getIdeFce()));
		return fceLancamentoEfluentesIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceLancamentoEfluente buscarFceLancamentoEfluentesByFceLancamentoEfluente(FceLancamentoEfluente fceLancamentoEfluente) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLancamentoEfluente.class);
		criteria.createAlias("ideTipoPeriodoDerivacao", "periodo");
		criteria.add(Restrictions.eq("ideFceLancamentoEfluente", fceLancamentoEfluente.getIdeFceLancamentoEfluente()));
		return fceLancamentoEfluentesIDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceLancamentoEfluente buscarFceLancamentoEfluentesToFceOutorgaAquiculturaBy(AnaliseTecnica analiseTecnica) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLancamentoEfluente.class)
		.createAlias("ideFceOutorgaLocalizacaoGeografica", "fceLoc")
		.createAlias("fceLoc.ideFce", "fce")
		.createAlias("fce.ideDocumentoObrigatorio", "doc")
		.createAlias("fce.ideDadoOrigem", "of")
		.createAlias("fce.ideAnaliseTecnica", "at")
		.add(Restrictions.eq("at.ideAnaliseTecnica", analiseTecnica.getIdeAnaliseTecnica()))
		.add(Restrictions.eq("of.ideDadoOrigem", DadoOrigemEnum.TECNICO.getId()))
		.add(Restrictions.eq("doc.ideDocumentoObrigatorio", DocumentoObrigatorioEnum.FCE_LANCAMENTO_EFLUENTES.getId()));
		return fceLancamentoEfluentesIDAO.buscarPorCriteria(criteria);
	}
}