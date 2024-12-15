package br.gov.ba.seia.dao;

import java.util.Collection;
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

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 10/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaptacaoCaracterizacaoFinalidadeDAOImpl extends AbstractDAO<CerhCaptacaoCaracterizacaoFinalidade>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhCaptacaoCaracterizacaoFinalidade> dao;
	
	@Override
	protected IDAO<CerhCaptacaoCaracterizacaoFinalidade> getDAO() {
		return dao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoCaracterizacaoFinalidade> listar(Integer ideCerhCaptacaoCaracterizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoCaracterizacaoFinalidade.class)
			.createAlias("ideTipoFinalidadeUsoAgua", "tfua", JoinType.INNER_JOIN)
			.add(Restrictions.eq("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao", ideCerhCaptacaoCaracterizacao))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("ideCerhCaptacaoCaracterizacaoFinalidade"), "ideCerhCaptacaoCaracterizacaoFinalidade")
					.add(Projections.property("tfua.ideTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
					.add(Projections.property("tfua.nomTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
					.add(Projections.property("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao"), "ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoCaracterizacaoFinalidade.class));
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoCaracterizacaoFinalidade> listarParaHistorico(Integer id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoCaracterizacaoFinalidade.class)
			.createAlias("ideTipoFinalidadeUsoAgua", "tfua", JoinType.INNER_JOIN )
			.createAlias("ideCerhCaptacaoTermoeletrica", "ideCerhCaptacaoTermoeletrica", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhCaptacaoAbastecimentoPublico", "ideCerhCaptacaoAbastecimentoPublico", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhCaptacaoAbastecimentoPublico.ideCerhTipoPrestadorServico", "ideCerhTipoPrestadorServico", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhCaptacaoOutrosUso", "ideCerhCaptacaoOutrosUso", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhCaptacaoMineracaoExtracaoAreia", "ideCerhCaptacaoMineracaoExtracaoAreia", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao", id))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.ideObjetoPai"), "ideCerhCaptacaoTermoeletrica.ideObjetoPai")
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.ideCerhCaptacaoTermoeletrica"), "ideCerhCaptacaoTermoeletrica.ideCerhCaptacaoTermoeletrica")
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.nomCombustivelPrincipal"), "ideCerhCaptacaoTermoeletrica.nomCombustivelPrincipal")
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.valEstimativaConsumoAgua"), "ideCerhCaptacaoTermoeletrica.valEstimativaConsumoAgua")
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.valPotenciaInstalada"), "ideCerhCaptacaoTermoeletrica.valPotenciaInstalada")
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.valProducaoMensalEnergia"), "ideCerhCaptacaoTermoeletrica.valProducaoMensalEnergia")
					
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.ideObjetoPai"), "ideCerhCaptacaoAbastecimentoPublico.ideObjetoPai")
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.ideCerhCaptacaoAbastecimentoPublico"), "ideCerhCaptacaoAbastecimentoPublico.ideCerhCaptacaoAbastecimentoPublico")
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.indPerdaDistribuicao"), "ideCerhCaptacaoAbastecimentoPublico.indPerdaDistribuicao")
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.indIncertoDesconhecido"), "ideCerhCaptacaoAbastecimentoPublico.indIncertoDesconhecido")
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.valIndicePerdaDistribuicao"), "ideCerhCaptacaoAbastecimentoPublico.valIndicePerdaDistribuicao")
					
					.add(Projections.property("ideCerhTipoPrestadorServico.ideObjetoPai"), "ideCerhCaptacaoAbastecimentoPublico.ideCerhTipoPrestadorServico.ideObjetoPai")
					.add(Projections.property("ideCerhTipoPrestadorServico.ideCerhTipoPrestadorServico"), "ideCerhCaptacaoAbastecimentoPublico.ideCerhTipoPrestadorServico.ideCerhTipoPrestadorServico")
					.add(Projections.property("ideCerhTipoPrestadorServico.dscTipoPrestadorServico"), "ideCerhCaptacaoAbastecimentoPublico.ideCerhTipoPrestadorServico.dscTipoPrestadorServico")
					
					.add(Projections.property("ideCerhCaptacaoCaracterizacaoFinalidade"), "ideCerhCaptacaoCaracterizacaoFinalidade")
					.add(Projections.property("tfua.ideTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
					.add(Projections.property("tfua.nomTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
					.add(Projections.property("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao"), "ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao")
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoCaracterizacaoFinalidade.class));
		return dao.listarPorCriteria(criteria);
	}

	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CerhCaptacaoCaracterizacaoFinalidade> listarParaHistorico(CerhCaptacaoCaracterizacao ccc) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoCaracterizacaoFinalidade.class)
			.createAlias("ideTipoFinalidadeUsoAgua", "ideTipoFinalidadeUsoAgua", JoinType.INNER_JOIN)
			
			.createAlias("ideCerhCaptacaoAbastecimentoPublico", "ideCerhCaptacaoAbastecimentoPublico", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhCaptacaoMineracaoExtracaoAreia", "ideCerhCaptacaoMineracaoExtracaoAreia", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhCaptacaoOutrosUso", "ideCerhCaptacaoOutrosUso", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhCaptacaoOutrosUso.ideCerhOutrosUsos", "ideCerhOutrosUsos", JoinType.LEFT_OUTER_JOIN)
			
			//
			.createAlias("ideCerhCaptacaoTermoeletrica", "ideCerhCaptacaoTermoeletrica", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao", ccc.getId()))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("ideCerhCaptacaoCaracterizacaoFinalidade"), "ideCerhCaptacaoCaracterizacaoFinalidade")
				
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.ideObjetoPai"), "ideCerhCaptacaoAbastecimentoPublico.ideObjetoPai")
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.ideCerhCaptacaoAbastecimentoPublico"), "ideCerhCaptacaoAbastecimentoPublico.ideCerhCaptacaoAbastecimentoPublico")
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.ideCerhCaptacaoAbastecimentoPublico"), "ideCerhCaptacaoAbastecimentoPublico.ideCerhCaptacaoAbastecimentoPublico")
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.indPerdaDistribuicao"), "ideCerhCaptacaoAbastecimentoPublico.indPerdaDistribuicao")
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.indIncertoDesconhecido"), "ideCerhCaptacaoAbastecimentoPublico.indIncertoDesconhecido")
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.valIndicePerdaDistribuicao"), "ideCerhCaptacaoAbastecimentoPublico.valIndicePerdaDistribuicao")
						
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.ideObjetoPai"), "ideCerhCaptacaoTermoeletrica.ideObjetoPai")
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.ideCerhCaptacaoTermoeletrica"), "ideCerhCaptacaoTermoeletrica.ideCerhCaptacaoTermoeletrica")
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.nomCombustivelPrincipal"), "ideCerhCaptacaoTermoeletrica.nomCombustivelPrincipal")
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.valEstimativaConsumoAgua"), "ideCerhCaptacaoTermoeletrica.valEstimativaConsumoAgua")
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.valPotenciaInstalada"), "ideCerhCaptacaoTermoeletrica.valPotenciaInstalada")
					.add(Projections.property("ideCerhCaptacaoTermoeletrica.valProducaoMensalEnergia"), "ideCerhCaptacaoTermoeletrica.valProducaoMensalEnergia")
					
					.add(Projections.property("ideCerhCaptacaoOutrosUso.ideObjetoPai"), "ideCerhCaptacaoOutrosUso.ideObjetoPai")
					.add(Projections.property("ideCerhCaptacaoOutrosUso.ideCerhOutrosUsos"), "ideCerhCaptacaoOutrosUso.ideCerhOutrosUsos")
					
					.add(Projections.property("ideCerhOutrosUsos.ideObjetoPai"), "ideCerhCaptacaoOutrosUso.ideCerhCaptacaoOutrosUso.ideObjetoPai")
					.add(Projections.property("ideCerhOutrosUsos.dscOutrosUsos"), "ideCerhCaptacaoOutrosUso.ideCerhCaptacaoOutrosUso.dscOutrosUsos")
					
					.add(Projections.property("ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
					.add(Projections.property("ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
					
					.add(Projections.property("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao"), "ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoCaracterizacaoFinalidade.class));
	
		return dao.listarPorCriteria(criteria);
	}

}
