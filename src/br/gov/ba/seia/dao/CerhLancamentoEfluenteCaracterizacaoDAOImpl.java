package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


/**
 * @author Alexandre Queiroz
 * @since 27/03/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhLancamentoEfluenteCaracterizacaoDAOImpl extends AbstractDAO<CerhLancamentoEfluenteCaracterizacao>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhLancamentoEfluenteCaracterizacao> dao;

	
	@Override
	protected IDAO<CerhLancamentoEfluenteCaracterizacao> getDAO() {
		return dao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhLancamentoEfluenteCaracterizacao carregarParaHistorico(CerhLancamentoEfluenteCaracterizacao ideCerhLancamentoEfluenteCaracterizacao) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhLancamentoEfluenteCaracterizacao.class)
			.createAlias("ideCerhSituacaoTipoUso", "ideCerhSituacaoTipoUso", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideTipoCorpoHidrico", "ideTipoCorpoHidrico", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhTratamentoEfluente", "ideCerhTratamentoEfluente", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhLocalizacaoGeografica", "cerhLoc", JoinType.LEFT_OUTER_JOIN)
			.createAlias("cerhLoc.ideLocalizacaoGeografica", "loc", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideCerhLancamentoEfluenteCaracterizacao", ideCerhLancamentoEfluenteCaracterizacao.getId()))
			
			.setProjection(Projections.projectionList()
				.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
				.add(Projections.property("ideCerhLancamentoEfluenteCaracterizacao"), "ideCerhLancamentoEfluenteCaracterizacao")
				.add(Projections.property("dtInicioLancamento"), "dtInicioLancamento")
				.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
				.add(Projections.property("indEfluenteRecebeTratamento"), "indEfluenteRecebeTratamento")
				.add(Projections.property("nomCorpoHidrico"), "nomCorpoHidrico")
				.add(Projections.property("dscObservacao"), "dscObservacao")
				.add(Projections.property("valDboEficienciaTratamento"), "valDboEficienciaTratamento")
				.add(Projections.property("valDboEfluenteBruto"), "valDboEfluenteBruto")
				.add(Projections.property("valDboEfluenteTratado"), "valDboEfluenteTratado")
				.add(Projections.property("valColiformesEficienciaTratamento"), "valColiformesEficienciaTratamento")
				.add(Projections.property("valColiformesEfluenteTratado"), "valColiformesEfluenteTratado")
				.add(Projections.property("valColiformesEfluenteBruto"), "valColiformesEfluenteBruto")
				.add(Projections.property("valVazaoEfluenteMaximaInstantanea"), "valVazaoEfluenteMaximaInstantanea")
				.add(Projections.property("valVazaoDiluicaoOutorgada"), "valVazaoDiluicaoOutorgada")
				
				.add(Projections.property("ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso"), "ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso")
				.add(Projections.property("ideCerhSituacaoTipoUso.dscSituacaoTipoUso"), "ideCerhSituacaoTipoUso.dscSituacaoTipoUso")
				
				.add(Projections.property("ideTipoCorpoHidrico.ideTipoCorpoHidrico"), "ideTipoCorpoHidrico.ideTipoCorpoHidrico")
				.add(Projections.property("ideTipoCorpoHidrico.nomTipoCorpoHidrico"), "ideTipoCorpoHidrico.nomTipoCorpoHidrico")
				
				.add(Projections.property("cerhLoc.ideCerhLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica")
				.add(Projections.property("loc.ideLocalizacaoGeografica"), "ideCerhLocalizacaoGeografica.ideLocalizacaoGeografica.ideLocalizacaoGeografica")				
				
				.add(Projections.property("ideCerhTratamentoEfluente.ideCerhTratamentoEfluente"), "ideCerhTratamentoEfluente.ideCerhTratamentoEfluente")
				.add(Projections.property("ideCerhTratamentoEfluente.dscTratamentoEfluente"), "ideCerhTratamentoEfluente.dscTratamentoEfluente")
				
			).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLancamentoEfluenteCaracterizacao.class));
		
		return dao.buscarPorCriteria(criteria);
	}
	

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhLancamentoEfluenteCaracterizacao carregar(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		try{
			DetachedCriteria criteria = DetachedCriteria.forClass(CerhLancamentoEfluenteCaracterizacao.class)
				.createAlias("ideCerhSituacaoTipoUso", "ideCerhSituacaoTipoUso", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideCerhTratamentoEfluente", "ideCerhTratamentoEfluente", JoinType.LEFT_OUTER_JOIN)
				.createAlias("ideTipoCorpoHidrico", "ideTipoCorpoHidrico", JoinType.LEFT_OUTER_JOIN)
			
				.add(Restrictions.eq("ideCerhLocalizacaoGeografica.ideCerhLocalizacaoGeografica", cerhLocalizacaoGeografica.getIdeCerhLocalizacaoGeografica()))
				.setProjection(Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("ideCerhLancamentoEfluenteCaracterizacao"), "ideCerhLancamentoEfluenteCaracterizacao")
					.add(Projections.property("dtInicioLancamento"), "dtInicioLancamento")
					.add(Projections.property("indEfluenteRecebeTratamento"), "indEfluenteRecebeTratamento")
					.add(Projections.property("nomCorpoHidrico"), "nomCorpoHidrico")
					.add(Projections.property("dscObservacao"), "dscObservacao")
					.add(Projections.property("valColiformesEficienciaTratamento"), "valColiformesEficienciaTratamento")
					.add(Projections.property("valColiformesEfluenteBruto"), "valColiformesEfluenteBruto")
					.add(Projections.property("valColiformesEfluenteTratado"), "valColiformesEfluenteTratado")
					.add(Projections.property("valDboEficienciaTratamento"), "valDboEficienciaTratamento")
					.add(Projections.property("valDboEfluenteBruto"), "valDboEfluenteBruto")
					.add(Projections.property("valDboEfluenteTratado"), "valDboEfluenteTratado")
					.add(Projections.property("valVazaoEfluenteMaximaInstantanea"), "valVazaoEfluenteMaximaInstantanea")
					.add(Projections.property("valVazaoDiluicaoOutorgada"), "valVazaoDiluicaoOutorgada")
					
					.add(Projections.property("ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso"), "ideCerhSituacaoTipoUso.ideCerhSituacaoTipoUso")
					.add(Projections.property("ideCerhSituacaoTipoUso.dscSituacaoTipoUso"), "ideCerhSituacaoTipoUso.dscSituacaoTipoUso")
					
					.add(Projections.property("ideCerhTratamentoEfluente.ideCerhTratamentoEfluente"), "ideCerhTratamentoEfluente.ideCerhTratamentoEfluente")
					.add(Projections.property("ideCerhTratamentoEfluente.dscTratamentoEfluente"), "ideCerhTratamentoEfluente.dscTratamentoEfluente")
					
					.add(Projections.property("ideTipoCorpoHidrico.ideTipoCorpoHidrico"), "ideTipoCorpoHidrico.ideTipoCorpoHidrico")
					.add(Projections.property("ideTipoCorpoHidrico.nomTipoCorpoHidrico"), "ideTipoCorpoHidrico.nomTipoCorpoHidrico")
					
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLancamentoEfluenteCaracterizacao.class));
			return dao.buscarPorCriteria(criteria);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao) {
		try{
			if(cerhLancamentoEfluenteCaracterizacao.getIdeCerhLancamentoEfluenteCaracterizacao()!=null){
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ideCerhLancamentoEfluenteCaracterizacao", cerhLancamentoEfluenteCaracterizacao.getIdeCerhLancamentoEfluenteCaracterizacao());
				
				dao.executarQuery("DELETE FROM CerhLancamentoEfluenteCaracterizacao c WHERE c.ideCerhLancamentoEfluenteCaracterizacao = :ideCerhLancamentoEfluenteCaracterizacao", params);
			}
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}