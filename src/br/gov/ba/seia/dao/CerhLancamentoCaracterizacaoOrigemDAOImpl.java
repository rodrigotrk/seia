package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
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
import br.gov.ba.seia.entity.CerhLancamentoCaracterizacaoOrigem;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhLancamentoCaracterizacaoOrigemDAOImpl extends AbstractDAO<CerhLancamentoCaracterizacaoOrigem>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhLancamentoCaracterizacaoOrigem> dao;
	
	@EJB
	private CerhLancamentoAbastecimentoPublicoDAOImpl daoCerhLancamentoAbastecimentoPublicoDAOImpl;
	
	@EJB
	private CerhLancamentoOutrosUsosDAOImpl daoCerhLancamentoOutrosUsosDAOImpl;
	
	@Override
	protected IDAO<CerhLancamentoCaracterizacaoOrigem> getDAO() {
		return dao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhLancamentoCaracterizacaoOrigem> listarParaHistorico(CerhLancamentoEfluenteCaracterizacao clc)  {
		
		DetachedCriteria  criteria = DetachedCriteria.forClass(CerhLancamentoCaracterizacaoOrigem.class)
			.createAlias("ideTipoFinalidadeUsoAgua", "ideTipoFinalidadeUsoAgua", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhLancamentoOutrosUsos", "ideCerhLancamentoOutrosUsos", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhLancamentoEfluenteCaracterizacao", "ideCerhLancamentoEfluenteCaracterizacao", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhCaptacaoAbastecimentoPublico", "ideCerhCaptacaoAbastecimentoPublico", JoinType.LEFT_OUTER_JOIN)
			.createAlias("ideCerhCaptacaoAbastecimentoPublico.ideCerhTipoPrestadorServico", "ideCerhTipoPrestadorServico", JoinType.LEFT_OUTER_JOIN)
			
			.createAlias("ideCerhLancamentoOutrosUsos.ideCerhOutrosUsos", "ideCerhOutrosUsos", JoinType.LEFT_OUTER_JOIN)
			
			.add(Restrictions.eq("ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao", clc.getId()))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideCerhLancamentoCaracterizacaoOrigem"),"ideCerhLancamentoCaracterizacaoOrigem")
					.add(Projections.property("ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua")
					.add(Projections.property("ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua.nomTipoFinalidadeUsoAgua")
					.add(Projections.property("ideCerhLancamentoOutrosUsos.ideCerhLancamentoOutrosUsos"),"ideCerhLancamentoOutrosUsos.ideCerhLancamentoOutrosUsos")
					.add(Projections.property("ideObjetoPai"),"ideObjetoPai")
					
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.ideCerhLancamentoAbastecimentoPublico"),"ideCerhCaptacaoAbastecimentoPublico.ideCerhLancamentoAbastecimentoPublico")
					.add(Projections.property("ideCerhCaptacaoAbastecimentoPublico.ideObjetoPai"),"ideCerhCaptacaoAbastecimentoPublico.ideObjetoPai")
					
					.add(Projections.property("ideCerhTipoPrestadorServico.ideCerhTipoPrestadorServico"),"ideCerhCaptacaoAbastecimentoPublico.ideCerhTipoPrestadorServico.ideCerhTipoPrestadorServico")
					.add(Projections.property("ideCerhTipoPrestadorServico.dscTipoPrestadorServico"),"ideCerhCaptacaoAbastecimentoPublico.ideCerhTipoPrestadorServico.dscTipoPrestadorServico")
					
					.add(Projections.property("ideCerhLancamentoOutrosUsos.ideCerhLancamentoOutrosUsos"),"ideCerhCaptacaoAbastecimentoPublico.ideCerhLancamentoOutrosUsos.ideCerhLancamentoOutrosUsos")
					.add(Projections.property("ideCerhLancamentoOutrosUsos.ideCerhLancamentoOutrosUsos"),"ideCerhCaptacaoAbastecimentoPublico.ideCerhLancamentoOutrosUsos.ideCerhLancamentoOutrosUsos")
					
					.add(Projections.property("ideCerhOutrosUsos.ideCerhOutrosUsos"),"ideCerhLancamentoOutrosUsos.ideCerhOutrosUsos.ideCerhOutrosUsos")
					.add(Projections.property("ideCerhOutrosUsos.dscOutrosUsos"),"ideCerhLancamentoOutrosUsos.ideCerhOutrosUsos.dscOutrosUsos")
					
					//dscOutrosUsos
					
			).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLancamentoCaracterizacaoOrigem.class));

		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletar(Collection<CerhLancamentoCaracterizacaoOrigem> cerhLancamentoCaracterizacaoOrigemCollection) {
		for (CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem : cerhLancamentoCaracterizacaoOrigemCollection) {
			deletar(cerhLancamentoCaracterizacaoOrigem);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletar(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			cerhLancamentoCaracterizacaoOrigem = carregarId(cerhLancamentoCaracterizacaoOrigem);
			
			if(cerhLancamentoCaracterizacaoOrigem!=null && cerhLancamentoCaracterizacaoOrigem.getIdeCerhLancamentoCaracterizacaoOrigem()!=null){
			
				if(cerhLancamentoCaracterizacaoOrigem.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.OUTRA.getId())){
					daoCerhLancamentoOutrosUsosDAOImpl.deletar(daoCerhLancamentoOutrosUsosDAOImpl.buscar(cerhLancamentoCaracterizacaoOrigem));
				}
				
				if(cerhLancamentoCaracterizacaoOrigem.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua().equals(TipoFinalidadeUsoAguaEnum.ESGOTAMENTO_SANITARIO_ABASTECIMENTO_PUBLICO.getId())){
					daoCerhLancamentoAbastecimentoPublicoDAOImpl.deletar(daoCerhLancamentoAbastecimentoPublicoDAOImpl.buscar(cerhLancamentoCaracterizacaoOrigem));
				}
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoCaracterizacaoOrigem.getIdeCerhLancamentoCaracterizacaoOrigem());
				dao.executarQuery("DELETE FROM CerhLancamentoCaracterizacaoOrigem c WHERE c.ideCerhLancamentoCaracterizacaoOrigem = :ideCerhLancamentoCaracterizacaoOrigem", params);
			}
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhLancamentoCaracterizacaoOrigem> listarCerhLancamentoCaracterizacaoOrigem(CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao) {
		try{
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CerhLancamentoCaracterizacaoOrigem.class)
				.add(Restrictions.eq("ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao", cerhLancamentoEfluenteCaracterizacao.getIdeCerhLancamentoEfluenteCaracterizacao()))
				.setProjection(
					Projections.projectionList()
						.add(Projections.alias(Projections.property("ideCerhLancamentoCaracterizacaoOrigem"),"ideCerhLancamentoCaracterizacaoOrigem"))
						.add(Projections.alias(Projections.property("ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua"))
						.add(Projections.alias(Projections.property("ideCerhLancamentoEfluenteCaracterizacao"),"ideCerhLancamentoEfluenteCaracterizacao"))
						.add(Projections.alias(Projections.property("ideObjetoPai"),"ideObjetoPai"))
					).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLancamentoCaracterizacaoOrigem.class));
			return dao.listarPorCriteria(detachedCriteria);
			
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhLancamentoCaracterizacaoOrigem carregarId(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CerhLancamentoCaracterizacaoOrigem.class)
				.add(Restrictions.eq("ideTipoFinalidadeUsoAgua.ideTipoFinalidadeUsoAgua", cerhLancamentoCaracterizacaoOrigem.getIdeTipoFinalidadeUsoAgua().getIdeTipoFinalidadeUsoAgua()))
				.add(Restrictions.eq("ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao", cerhLancamentoCaracterizacaoOrigem.getIdeCerhLancamentoEfluenteCaracterizacao().getIdeCerhLancamentoEfluenteCaracterizacao()))
				.setProjection(
					Projections.projectionList()
						.add(Projections.alias(Projections.property("ideCerhLancamentoCaracterizacaoOrigem"),"ideCerhLancamentoCaracterizacaoOrigem"))
						.add(Projections.alias(Projections.property("ideTipoFinalidadeUsoAgua"),"ideTipoFinalidadeUsoAgua"))
						.add(Projections.alias(Projections.property("ideCerhLancamentoEfluenteCaracterizacao"),"ideCerhLancamentoEfluenteCaracterizacao"))
					).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLancamentoCaracterizacaoOrigem.class));
			return dao.buscarPorCriteria(detachedCriteria);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}