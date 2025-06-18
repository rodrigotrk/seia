package br.gov.ba.seia.dao;

import java.util.Collection;

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
import br.gov.ba.seia.entity.CerhBarragemCaracterizacaoFinalidade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhBarragemCaracterizacaoFinalidadeDAOImpl extends AbstractDAO<CerhBarragemCaracterizacaoFinalidade> {
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhBarragemCaracterizacaoFinalidade> dao;
	
	@Override
	protected IDAO<CerhBarragemCaracterizacaoFinalidade> getDAO() {
		return dao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhBarragemCaracterizacaoFinalidade> listarParaHistorico(Integer ideCerhCaptacaoCaracterizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhBarragemCaracterizacaoFinalidade.class)
			.createAlias("ideTipoFinalidadeUsoAgua", "ideTipoFinalidadeUsoAgua", JoinType.INNER_JOIN)
			.createAlias("ideCerhBarragemAproveitamentoHidreletrico", "ideCerhBarragemAproveitamentoHidreletrico", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhBarragemAproveitamentoHidreletrico.ideTipoAproveitamentoHidreletrico", "ideTipoAproveitamentoHidreletrico", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideCerhBarragemCaracterizacao.ideCerhBarragemCaracterizacao", ideCerhCaptacaoCaracterizacao))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("ideCerhBarragemCaracterizacaoFinalidade"), "ideCerhBarragemCaracterizacaoFinalidade")
					
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico.ideCerhBarragemAproveitamentoHidreletrico"), "ideCerhBarragemAproveitamentoHidreletrico.ideCerhBarragemAproveitamentoHidreletrico")
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico.indDesvioTrecho"), "ideCerhBarragemAproveitamentoHidreletrico.indDesvioTrecho")
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico.indEmOperacao"), "ideCerhBarragemAproveitamentoHidreletrico.indEmOperacao")
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico.indPch"), "ideCerhBarragemAproveitamentoHidreletrico.indPch")
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico.indPotenciaInstaladaIntervencao"), "ideCerhBarragemAproveitamentoHidreletrico.indPotenciaInstaladaIntervencao")
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico.valExtensao"), "ideCerhBarragemAproveitamentoHidreletrico.valExtensao")
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico.valPotenciaInstaladaTotal"), "ideCerhBarragemAproveitamentoHidreletrico.valPotenciaInstaladaTotal")
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico.valProducaoAnualEfetivamenteVerificada"), "ideCerhBarragemAproveitamentoHidreletrico.valProducaoAnualEfetivamenteVerificada")
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico.valTrechoVazaoReduzida"), "ideCerhBarragemAproveitamentoHidreletrico.valTrechoVazaoReduzida")
					.add(Projections.property("ideCerhBarragemAproveitamentoHidreletrico.dtInicioOperacao"), "ideCerhBarragemAproveitamentoHidreletrico.dtInicioOperacao")
			
					
					.add(Projections.property("ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
					.add(Projections.property("ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
					
					.add(Projections.property("ideTipoAproveitamentoHidreletrico.ideTipoAproveitamentoHidreletrico"), "ideCerhBarragemAproveitamentoHidreletrico.ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
					.add(Projections.property("ideTipoAproveitamentoHidreletrico.dscTipoAproveitamentoHidreletrico"), "ideCerhBarragemAproveitamentoHidreletrico.ideTipoFinalidadeUsoAgua.dscTipoAproveitamentoHidreletrico")
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhBarragemCaracterizacaoFinalidade.class));
		return dao.listarPorCriteria(criteria);
	}

	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhBarragemCaracterizacaoFinalidade> listar(Integer ideCerhCaptacaoCaracterizacao){
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhBarragemCaracterizacaoFinalidade.class)
			.createAlias("ideTipoFinalidadeUsoAgua", "tfua")
			.add(Restrictions.eq("ideCerhBarragemCaracterizacao.ideCerhBarragemCaracterizacao", ideCerhCaptacaoCaracterizacao))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("ideCerhBarragemCaracterizacaoFinalidade"), "ideCerhBarragemCaracterizacaoFinalidade")
					.add(Projections.property("tfua.ideTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
					.add(Projections.property("tfua.nomTipoFinalidadeUsoAgua"), "ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
					
					.add(Projections.property("ideCerhBarragemCaracterizacao.ideCerhBarragemCaracterizacao"), "ideCerhBarragemCaracterizacao.ideCerhBarragemCaracterizacao")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhBarragemCaracterizacaoFinalidade.class))
				;
		return dao.listarPorCriteria(criteria);
	}

	
}
