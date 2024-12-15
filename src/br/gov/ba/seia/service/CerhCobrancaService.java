/**
 * 
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Cerh;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacao;
import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoVazaoSazonalidade;
import br.gov.ba.seia.entity.CerhCobranca;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteSazonalidade;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.CerhTipoUso;
import br.gov.ba.seia.entity.Dae;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.TipoUsoRecursoHidrico;
import br.gov.ba.seia.enumerator.CerhStatusEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCobrancaService {
	
	@Inject
	private IDAO<Cerh> idao;
	@Inject
	private IDAO<CerhTipoUso> idaoTipoUso;
	@Inject
	private IDAO<CerhLocalizacaoGeografica> idaoLoc;
	@Inject
	private IDAO<CerhCaptacaoVazaoSazonalidade> idaoCapVazaoSazo; 
	@Inject
	private IDAO<CerhCaptacaoCaracterizacaoFinalidade> idaoCaptacaoCaracterizacaoFinalidade;
	@Inject
	private IDAO<CerhLancamentoEfluenteSazonalidade> idaoLancamentoEfluenteSazonalidade;
	@Inject
	private IDAO<CerhCobranca> idaoCerhCobranca;
	@Inject
	private IDAO<Dae> idaoCerhDae;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cerh buscarCerh(Empreendimento empreendimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Cerh.class, "a")
				.createAlias("a.cerhStatusCollection", "b", JoinType.LEFT_OUTER_JOIN)
				.createAlias("a.ideEmpreendimento", "c", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("b.ideCerhTipoStatus.ideCerhTipoStatus", CerhStatusEnum.CADASTRO_COMPLETO.getId()))
				.add(Restrictions.eq("a.ideEmpreendimento", empreendimento))
				.addOrder(Order.desc("b.dtcStatus"));
		Cerh cerh = idao.buscarPorCriteriaMaxResult(criteria);
		if(cerh != null){
			//Tipo de Uso
			this.setTipoUsoCriteria(cerh);
			
			//Tipo de Uso -> Cerh Local geo
			for(CerhTipoUso cerhTipoUso : cerh.getCerhTipoUsoCollection()){
				this.setCerhLocalizacaoGeograficaCriteria(cerhTipoUso);
				for(CerhLocalizacaoGeografica cerlLoc : cerhTipoUso.getCerhLocalizacaoGeograficaCollection()){
					if(cerlLoc.getIdeCerhCaptacaoCaracterizacao() != null){
						this.setCerhCaptacaoCaracterizacaoCriteria(cerlLoc.getIdeCerhCaptacaoCaracterizacao());
						this.setCerhCaptacaoCaracterizacaoFinalidadeCriteria(cerlLoc.getIdeCerhCaptacaoCaracterizacao());
					}
					if(cerlLoc.getIdeCerhLancamentoEfluenteCaracterizacao() != null){
						this.setCerhLancamentoEfluenteCaracterizacaoCriteria(cerlLoc.getIdeCerhLancamentoEfluenteCaracterizacao());
					}
				}
			}
		}
		return cerh;
	}
	
	
	
	private void setTipoUsoCriteria(Cerh cerh) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhTipoUso.class)
		.createAlias("ideTipoUsoRecursoHidrico", "a", JoinType.LEFT_OUTER_JOIN)
		.setProjection(Projections.projectionList()
			.add(Projections.property("ideCerhTipoUso"),"ideCerhTipoUso")					
				.add(Projections.property("a.ideTipoUsoRecursoHidrico"), "ideTipoUsoRecursoHidrico.ideTipoUsoRecursoHidrico")	
				.add(Projections.property("a.dscTipoUsoRecursoHidrico"), "ideTipoUsoRecursoHidrico.dscTipoUsoRecursoHidrico")	
			)
		.add(Restrictions.eq("ideCerh", cerh))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(CerhTipoUso.class));
		cerh.setCerhTipoUsoCollection(idaoTipoUso.listarPorCriteria(criteria));
	}
	
	private void setCerhLocalizacaoGeograficaCriteria(CerhTipoUso cerhTipoUso) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhLocalizacaoGeografica.class)
				.createAlias("ideLocalizacaoGeografica", "a", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideCerhBarragemCaracterizacao", "b", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideCerhCaptacaoCaracterizacao", "c", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideCerhIntervencaoCaracterizacao", "d", JoinType.LEFT_OUTER_JOIN)
				.createAlias("cerhLancamentoEfluenteCaracterizacao", "e", JoinType.LEFT_OUTER_JOIN)
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideCerhLocalizacaoGeografica"),"ideCerhLocalizacaoGeografica")					
						.add(Projections.property("a.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")	
							.add(Projections.property("b.ideCerhBarragemCaracterizacao"), "ideCerhBarragemCaracterizacao.ideCerhBarragemCaracterizacao")
							.add(Projections.property("b.valVazaoRegularizada"), "ideCerhBarragemCaracterizacao.valVazaoRegularizada")
							.add(Projections.property("c.ideCerhCaptacaoCaracterizacao"), "ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao")
								.add(Projections.property("c.valVazaoMaximaInstantanea"), "ideCerhCaptacaoCaracterizacao.valVazaoMaximaInstantanea")
								.add(Projections.property("d.ideCerhIntervencaoCaracterizacao"), "ideCerhIntervencaoCaracterizacao.ideCerhIntervencaoCaracterizacao")
								.add(Projections.property("d.valProducaoAnualEfetivamenteVerificada"), "ideCerhIntervencaoCaracterizacao.valProducaoAnualEfetivamenteVerificada")
								.add(Projections.property("e.ideCerhLancamentoEfluenteCaracterizacao"), "cerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao")
								.add(Projections.property("e.valDboEficienciaTratamento"), "cerhLancamentoEfluenteCaracterizacao.valDboEficienciaTratamento")
								.add(Projections.property("e.valColiformesEficienciaTratamento"), "cerhLancamentoEfluenteCaracterizacao.valColiformesEficienciaTratamento")
						)
						.add(Restrictions.eq("ideCerhTipoUso", cerhTipoUso))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLocalizacaoGeografica.class));					
		cerhTipoUso.setCerhLocalizacaoGeograficaCollection(idaoLoc.listarPorCriteria(criteria));
	}
	
	private void setCerhCaptacaoCaracterizacaoCriteria(CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoVazaoSazonalidade.class)
				.add(Restrictions.eq("ideCerhCaptacaoCaracterizacao", cerhCaptacaoCaracterizacao))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideCerhCaptacaoVazaoSazonalidade"),"ideCerhCaptacaoVazaoSazonalidade")					
						.add(Projections.property("valDiaMes"),"valDiaMes")					
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoVazaoSazonalidade.class));					
		
		cerhCaptacaoCaracterizacao.setCerhCaptacaoVazaoSazonalidadeCollection(idaoCapVazaoSazo.listarPorCriteria(criteria));
	}
	
	private void setCerhLancamentoEfluenteCaracterizacaoCriteria(CerhLancamentoEfluenteCaracterizacao caracterizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhLancamentoEfluenteSazonalidade.class)
				.add(Restrictions.eq("ideCerhLancamentoEfluenteCaracterizacao", caracterizacao))
				.setProjection(Projections.projectionList()
						.add(Projections.property("valVazaoEfluente"),"valVazaoEfluente")					
						.add(Projections.property("valDiaMes"),"valDiaMes")					
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLancamentoEfluenteSazonalidade.class));					
		
		caracterizacao.setCerhLancamentoEfluenteSazonalidadeCollection(idaoLancamentoEfluenteSazonalidade.listarPorCriteria(criteria));
	}
	
	private void setCerhCaptacaoCaracterizacaoFinalidadeCriteria(CerhCaptacaoCaracterizacao cerhCaptacaoCaracterizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoCaracterizacaoFinalidade.class)
				.add(Restrictions.eq("ideCerhCaptacaoCaracterizacao", cerhCaptacaoCaracterizacao))
				.createAlias("ideTipoFinalidadeUsoAgua", "a", JoinType.LEFT_OUTER_JOIN)
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideCerhCaptacaoCaracterizacaoFinalidade"),"ideCerhCaptacaoCaracterizacaoFinalidade")					
						.add(Projections.property("a.ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")					
						.add(Projections.property("a.nomTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")					
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoCaracterizacaoFinalidade.class));	
		cerhCaptacaoCaracterizacao.setCerhCaptacaoCaracterizacaoFinalidadeCollection(idaoCaptacaoCaracterizacaoFinalidade.listarPorCriteria(criteria));
		
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvarCerhCobranca(CerhCobranca cobranca) {
		idaoCerhCobranca.salvar(cobranca);
	}
	
	/**
	 * Deve Verificar se já foi emitida a cobrança para os tipos de usos e RPGA no ano de cobrança
	 * @param cobranca
	 * @return cobranca Emitida 
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isCobrancaJaEmitida(CerhCobranca cobranca) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Dae.class, "a")
				.createAlias("a.cerhDaetipoUsos", "cerhDaetipoUsos")
				.createAlias("a.cerhParcelasCobranca", "cerhParcelasCobranca")
				.createAlias("a.cerhParcelasCobranca.ideCerhCobranca", "ideCerhCobranca")
				.createAlias("a.cerhParcelasCobranca.ideCerhCobranca.cerh", "cerh")
				.add(Restrictions.eq("cerhParcelasCobranca.numParcela", 1))
				.add(Restrictions.eq("ideCerhCobranca.numAnoCobranca", cobranca.getNumAnoCobranca()))
				.add(Restrictions.eq("cerh.ideEmpreendimento", cobranca.getCerh().getIdeEmpreendimento()))
				;
		
		Dae dae = idaoCerhDae.buscarPorCriteriaMaxResult(criteria);
		if(dae != null){
			List<TipoUsoRecursoHidrico> tipoUsoRecursoHidricosDBO = dae.getTipoUsoRecursoHidricos();
			List<TipoUsoRecursoHidrico> tipoUsoRecursoHidricosCobranca = cobranca.getCerhParcelasCobrancasCollection().get(0).getCerhDaesCollection().get(0).getTipoUsoRecursoHidricos();
			/** Verificar RPGA*/
			if(tipoUsoRecursoHidricosDBO.containsAll(tipoUsoRecursoHidricosCobranca)){
				return true;
			}
		}
		return false;
	}
}
