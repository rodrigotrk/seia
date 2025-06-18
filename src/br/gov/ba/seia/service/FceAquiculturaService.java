package br.gov.ba.seia.service;

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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 21/11/2014
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceAquiculturaService {

	@Inject
	private IDAO<FceAquicultura> fceAquiculturaIDAO;

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fce
	 * @param criteria
	 * @since 28/04/2015
	 */
	private void montarCriteria(DetachedCriteria criteria) {
		criteria.createAlias("ideFce", "ideFce", JoinType.INNER_JOIN)
		.createAlias("ideFce.ideRequerimento", "ideRequerimento", JoinType.INNER_JOIN)
		.createAlias("ideTipoAquicultura", "ideTipoAquicultura", JoinType.INNER_JOIN)
		.createAlias("ideTipoAquicultura.ideTipoAquiculturaPai", "ideTipoAquiculturaPai", JoinType.INNER_JOIN)
		.createAlias("ideTipoPeriodoDerivacao", "tipoPeriodoDerivacao", JoinType.LEFT_OUTER_JOIN)
		.createAlias("ideDocumentoObrigatorioRequerimento", "ideDocumentoObrigatorioRequerimento", JoinType.LEFT_OUTER_JOIN)
		.createAlias("ideFceLancamentoEfluente", "ideFceLancamentoEfluente", JoinType.LEFT_OUTER_JOIN)
		.createAlias("ideFceLancamentoEfluente.ideFceOutorgaLocalizacaoGeografica", "ideFceOutorgaLocalizacaoGeografica", JoinType.LEFT_OUTER_JOIN);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquicultura> listarFceAquiculturaByIdeFce(Fce fce) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquicultura.class);
		montarCriteria(criteria);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		return fceAquiculturaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceAquicultura> listarFceAquiculturaByIdeRequerimento(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquicultura.class);
		montarCriteria(criteria);
		criteria.add(Restrictions.eq("ideFce.ideRequerimento.ideRequerimento", requerimento.getIdeRequerimento()))
		.setProjection(Projections.projectionList()
				.add(Projections.property("ideTipoAquicultura.ideTipoAquicultura"), "ideTipoAquicultura.ideTipoAquicultura")
				.add(Projections.property("numVazaoRequerida"), "numVazaoRequerida")
				.add(Projections.property("ideFceLancamentoEfluente.numVazaoEfluente"), "ideFceLancamentoEfluente.numVazaoEfluente"))
				.setResultTransformer(new AliasToNestedBeanResultTransformer(FceAquicultura.class));
		return fceAquiculturaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAquicultura buscarFceAquiculturaByIdeFceAndTipoAquicultura(Fce fce, TipoAquiculturaEnum tipoAquiculturaEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquicultura.class);
		montarCriteria(criteria);
		criteria.add(Restrictions.eq("ideFce.ideFce", fce.getIdeFce()));
		criteria.add(Restrictions.eq("ideTipoAquicultura.ideTipoAquicultura", tipoAquiculturaEnum.getId()));
		return fceAquiculturaIDAO.buscarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAquicultura buscarFceAquiculturaByIdeFceAndTipoAquicultura(Requerimento requerimento, TipoAquiculturaEnum tipoAquiculturaEnum) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquicultura.class)
				.createAlias("ideTipoAquicultura", "ta", JoinType.INNER_JOIN)
				.createAlias("ta.ideTipoAquiculturaPai", "tap", JoinType.INNER_JOIN)
				.createAlias("ideDocumentoObrigatorioRequerimento", "doc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideFce", "f", JoinType.INNER_JOIN)
				.createAlias("f.ideRequerimento", "r", JoinType.INNER_JOIN)
				.createAlias("f.ideDadoOrigem", "of")
				.add(Restrictions.eq("of.ideDadoOrigem", DadoOrigemEnum.REQUERIMENTO.getId()))
				.add(Restrictions.eq("r.ideRequerimento", requerimento.getIdeRequerimento()))
				.add(Restrictions.eq("ta.ideTipoAquicultura", tipoAquiculturaEnum.getId()));
		return fceAquiculturaIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FceAquicultura buscarVazaoByRequerimentoAndTipoAquicultura(Requerimento requerimento, TipoAquiculturaEnum tipoAquiculturaEnum)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceAquicultura.class);
		criteria.createAlias("ideFce", "fce");
		if(tipoAquiculturaEnum.equals(TipoAquiculturaEnum.LANCAMENTO)){
			criteria.createAlias("ideFceLancamentoEfluente", "lancamento");
		}
		criteria.createAlias("fce.ideRequerimento", "req");
		criteria.createAlias("fce.ideDocumentoObrigatorio", "doc");
		criteria.createAlias("ideTipoAquicultura", "tipoAquicultura");
		criteria.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()));
		criteria.add(Restrictions.eq("doc.ideDocumentoObrigatorio", DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId()));
		criteria.add(Restrictions.eq("ideTipoAquicultura.ideTipoAquicultura", tipoAquiculturaEnum.getId()));
		if(tipoAquiculturaEnum.equals(TipoAquiculturaEnum.LANCAMENTO)){
			criteria.setProjection(Projections.projectionList().add(Projections.property("lancamento.numVazaoEfluente"), "ideFceLancamentoEfluente.numVazaoEfluente"));
		}
		else if(tipoAquiculturaEnum.equals(TipoAquiculturaEnum.CAPTACAO)) {
			criteria.setProjection(Projections.projectionList().add(Projections.property("numVazaoRequerida"), "numVazaoRequerida"));
		}
		criteria.setResultTransformer(new AliasToNestedBeanResultTransformer(FceAquicultura.class));
		return fceAquiculturaIDAO.buscarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceAquicultura(FceAquicultura fceAquicultura) {
		fceAquiculturaIDAO.salvarOuAtualizar(fceAquicultura);
	}
}