package br.gov.ba.seia.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.entity.CerhLancamentoEfluenteSazonalidade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhLancamentoEfluenteSazonalidadeDAOImpl extends AbstractDAO<CerhLancamentoEfluenteSazonalidade>{
	private static final long serialVersionUID = 1L;
	
	@Inject
	private IDAO<CerhLancamentoEfluenteSazonalidade> dao;
	
	@Override
	protected IDAO<CerhLancamentoEfluenteSazonalidade> getDAO() {
		return dao;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<CerhLancamentoEfluenteSazonalidade> cerhLancamentoEfluenteSazonalidade) {
		List<CerhLancamentoEfluenteSazonalidade> listaParaSalvar = new ArrayList<CerhLancamentoEfluenteSazonalidade>();
		
		try{
			
			for (CerhLancamentoEfluenteSazonalidade lancamento : cerhLancamentoEfluenteSazonalidade) {
				
				CerhLancamentoEfluenteSazonalidade carregarId = carregarId(lancamento);
				
				if(carregarId.getIdeCerhLancamentoEfluenteSazonalidade()!=null){
					carregarId.setValDiaMes(lancamento.getValDiaMes());
					carregarId.setValTempoLancamento(lancamento.getValTempoLancamento());
					carregarId.setValVazaoEfluente(lancamento.getValVazaoEfluente());
				}
				
				listaParaSalvar.add(carregarId);
			}
			
			for (CerhLancamentoEfluenteSazonalidade lancamento : listaParaSalvar) {
				if(lancamento.getIdeCerhLancamentoEfluenteSazonalidade()==null){
					dao.salvar(lancamento);
				}
				else {
					dao.atualizar(lancamento);
				}
			}
		
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Collection<CerhLancamentoEfluenteSazonalidade> cerhLancamentoEfluenteSazonalidadeCollection) {
		try{
			
			for (CerhLancamentoEfluenteSazonalidade cerhLancamentoEfluenteSazonalidade : cerhLancamentoEfluenteSazonalidadeCollection) {
				if(cerhLancamentoEfluenteSazonalidade.getIdeCerhLancamentoEfluenteSazonalidade()!=null){
					
					Map<String, Object> params = new HashMap<String, Object>();
					params.put("ideCerhLancamentoEfluenteSazonalidade", cerhLancamentoEfluenteSazonalidade.getIdeCerhLancamentoEfluenteSazonalidade());
					
					dao.executarQuery(
						  "DELETE FROM CerhLancamentoEfluenteSazonalidade c WHERE c.ideCerhLancamentoEfluenteSazonalidade = :ideCerhLancamentoEfluenteSazonalidade ",
					params);
				}
			}
			
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhLancamentoEfluenteSazonalidade carregarId(CerhLancamentoEfluenteSazonalidade cerhLancamentoEfluenteSazonalidade) {
		try{
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CerhLancamentoEfluenteSazonalidade.class)
				.add(Restrictions.eq("ideCerhLancamentoEfluenteCaracterizacao", cerhLancamentoEfluenteSazonalidade.getIdeCerhLancamentoEfluenteCaracterizacao()))
				.add(Restrictions.eq("ideMes.ideMes", cerhLancamentoEfluenteSazonalidade.getIdeMes().getIdeMes()))
				
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideCerhLancamentoEfluenteSazonalidade"),"ideCerhLancamentoEfluenteSazonalidade")
						.add(Projections.property("valDiaMes"),"valDiaMes")
						.add(Projections.property("valTempoLancamento"),"valTempoLancamento")
						.add(Projections.property("valVazaoEfluente"),"valVazaoEfluente")
						.add(Projections.property("ideMes"),"ideMes")
						.add(Projections.property("ideCerhLancamentoEfluenteCaracterizacao"),"ideCerhLancamentoEfluenteCaracterizacao")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLancamentoEfluenteSazonalidade.class));
			 CerhLancamentoEfluenteSazonalidade lancamento = dao.buscarPorCriteria(detachedCriteria);
			 
			 if(lancamento!=null){
				 return lancamento;
			 }
			 
			 return cerhLancamentoEfluenteSazonalidade;
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhLancamentoEfluenteSazonalidade> listarPorCerhLancamentoEfluenteCaracterizacao(CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao) {
		try{
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CerhLancamentoEfluenteSazonalidade.class)
				.add(Restrictions.eq("ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao", cerhLancamentoEfluenteCaracterizacao.getIdeCerhLancamentoEfluenteCaracterizacao()))
				
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideCerhLancamentoEfluenteSazonalidade"),"ideCerhLancamentoEfluenteSazonalidade")
						.add(Projections.property("valDiaMes"),"valDiaMes")
						.add(Projections.property("ideObjetoPai"),"ideObjetoPai")
						.add(Projections.property("valTempoLancamento"),"valTempoLancamento")
						.add(Projections.property("valVazaoEfluente"),"valVazaoEfluente")
						.add(Projections.property("ideMes"),"ideMes")
						.add(Projections.property("ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao"),"ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhLancamentoEfluenteSazonalidade.class));
			
			return dao.listarPorCriteria(detachedCriteria);
			
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletar(CerhLancamentoEfluenteCaracterizacao cerhLancamentoEfluenteCaracterizacao) {
		try{
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideCerhLancamentoEfluenteCaracterizacao", cerhLancamentoEfluenteCaracterizacao.getIdeCerhLancamentoEfluenteCaracterizacao());
			
			dao.executarQuery(
				  "DELETE FROM CerhLancamentoEfluenteSazonalidade c WHERE ideCerhLancamentoEfluenteCaracterizacao.ideCerhLancamentoEfluenteCaracterizacao = :ideCerhLancamentoEfluenteCaracterizacao ",
			params);
			
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	
}