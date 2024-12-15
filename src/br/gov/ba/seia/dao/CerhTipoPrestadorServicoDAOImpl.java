package br.gov.ba.seia.dao;

import java.util.Collection;

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
import br.gov.ba.seia.entity.CerhTipoPrestadorServico;
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
public class CerhTipoPrestadorServicoDAOImpl {

	@Inject
	private IDAO<CerhTipoPrestadorServico> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoPrestadorServico> listarTodos() {
		return dao.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhTipoPrestadorServico buscarCerhTipoPrestadorServico(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			DetachedCriteria criteria = DetachedCriteria.forClass(CerhTipoPrestadorServico.class)
				.createAlias("cerhLancamentoAbastecimentoPublicoCollection", "cerhLancamentoAbastecimentoPublico")
				.createAlias("cerhLancamentoAbastecimentoPublico.ideCerhLancamentoCaracterizacaoOrigem", "ideCerhLancamentoCaracterizacaoOrigem")
				
				.add(Restrictions.eq("ideCerhLancamentoCaracterizacaoOrigem.ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoCaracterizacaoOrigem.getIdeCerhLancamentoCaracterizacaoOrigem()))
					
					.setProjection(
						Projections.projectionList()
							.add(Projections.alias(Projections.property("ideCerhTipoPrestadorServico"), "ideCerhTipoPrestadorServico"))
							.add(Projections.alias(Projections.property("dscTipoPrestadorServico"), "dscTipoPrestadorServico"))
						
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhTipoPrestadorServico.class));
					
			return dao.buscarPorCriteria(criteria);
			
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
}
