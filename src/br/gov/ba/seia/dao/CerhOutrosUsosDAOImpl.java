package br.gov.ba.seia.dao;
import java.util.Collection;
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

import br.gov.ba.seia.entity.CerhLancamentoCaracterizacaoOrigem;
import br.gov.ba.seia.entity.CerhOutrosUsos;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @author Alexandre Queiroz
 * @since 27/03/2017
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhOutrosUsosDAOImpl {
	
	@Inject
	private IDAO<CerhOutrosUsos> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhOutrosUsos> listarTodos() {
		return dao.listarTodos();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhOutrosUsos buscarCerhLacamentoOutrosUsos(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			DetachedCriteria criteria = DetachedCriteria.forClass(CerhOutrosUsos.class)
				.createAlias("cerhLancamentoOutrosUsoCollection", "cerhLancamentoOutrosUso")
				.createAlias("cerhLancamentoOutrosUso.ideCerhLancamentoCaracterizacaoOrigem", "ideCerhLancamentoCaracterizacaoOrigem")
				
				.add(Restrictions.eq("ideCerhLancamentoCaracterizacaoOrigem.ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoCaracterizacaoOrigem.getIdeCerhLancamentoCaracterizacaoOrigem()))
					
					.setProjection(
						Projections.projectionList()
							.add(Projections.alias(Projections.property("ideCerhOutrosUsos"), "ideCerhOutrosUsos"))
							.add(Projections.alias(Projections.property("ideObjetoPai"), "ideObjetoPai"))
							.add(Projections.alias(Projections.property("dscOutrosUsos"), "dscOutrosUsos"))
						
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhOutrosUsos.class));
					
			return dao.buscarPorCriteria(criteria);
			
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarCerhLacamentoOutrosUsos(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			if(cerhLancamentoCaracterizacaoOrigem != null && cerhLancamentoCaracterizacaoOrigem.getIdeCerhLancamentoCaracterizacaoOrigem()!=null){
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoCaracterizacaoOrigem.getIdeCerhLancamentoCaracterizacaoOrigem());
				dao.executarNativeQuery("DELETE FROM cerh_lancamento_outros_usos  WHERE ide_cerh_lancamento_caracterizacao_origem = :ideCerhLancamentoCaracterizacaoOrigem", params);
			}
		
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}