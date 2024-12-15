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
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhLancamentoAbastecimentoPublico;
import br.gov.ba.seia.entity.CerhLancamentoCaracterizacaoOrigem;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author Alexandre Queiroz
 * @since 27/03/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhLancamentoAbastecimentoPublicoDAOImpl extends AbstractDAO<CerhLancamentoAbastecimentoPublico> {
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhLancamentoAbastecimentoPublico> dao;

	@Override
	protected IDAO<CerhLancamentoAbastecimentoPublico> getDAO() {
		return dao;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhLancamentoAbastecimentoPublico buscar(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CerhLancamentoAbastecimentoPublico.class)
				.add(Restrictions.eq("ideCerhLancamentoCaracterizacaoOrigem.ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoCaracterizacaoOrigem.getIdeCerhLancamentoCaracterizacaoOrigem()));
		
			return dao.buscarPorCriteria(detachedCriteria);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CerhLancamentoAbastecimentoPublico cerhLancamentoAbastecimentoPublico){
		try{
			dao.salvarOuAtualizar(cerhLancamentoAbastecimentoPublico);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletar(CerhLancamentoAbastecimentoPublico cerhLancamentoAbastecimentoPublico) {
		try{
			
			Map<String, Object> params = new HashMap<String, Object>();
			
			if(cerhLancamentoAbastecimentoPublico.getIdeCerhTipoPrestadorServico()!=null){
				params.put("ideCerhTipoPrestadorServico", cerhLancamentoAbastecimentoPublico.getIdeCerhTipoPrestadorServico());
			}
			
			params.put("ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoAbastecimentoPublico.getIdeCerhLancamentoCaracterizacaoOrigem());
			
			dao.executarQuery(
				  "DELETE FROM CerhLancamentoAbastecimentoPublico c WHERE  ideCerhLancamentoCaracterizacaoOrigem = :ideCerhLancamentoCaracterizacaoOrigem ",
			params);
			
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCerhLancamentoAbastecimentoPublico(Integer ideCerhLancamentoCaracterizacaoOrigem) {
			try{
				
				Map<String, Object> params = new HashMap<String, Object>();
				
				params.put("ideCerhLancamentoCaracterizacaoOrigem", ideCerhLancamentoCaracterizacaoOrigem);
				
				dao.executarQuery(
					  "DELETE FROM CerhLancamentoAbastecimentoPublico c WHERE ideCerhLancamentoCaracterizacaoOrigem.ideCerhLancamentoCaracterizacaoOrigem = :ideCerhLancamentoCaracterizacaoOrigem ",
				params);
				
			}catch(Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		
	}




}
