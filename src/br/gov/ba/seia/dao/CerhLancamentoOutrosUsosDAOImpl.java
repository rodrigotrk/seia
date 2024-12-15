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
import br.gov.ba.seia.entity.CerhLancamentoCaracterizacaoOrigem;
import br.gov.ba.seia.entity.CerhLancamentoOutrosUsos;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author Alexandre Queiroz
 * @since 27/03/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhLancamentoOutrosUsosDAOImpl extends AbstractDAO<CerhLancamentoOutrosUsos>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhLancamentoOutrosUsos> dao;

	@Override
	protected IDAO<CerhLancamentoOutrosUsos> getDAO() {
		return dao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhLancamentoOutrosUsos buscar(CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		try{
			DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CerhLancamentoOutrosUsos.class)
				.add(Restrictions.eq("ideCerhLancamentoCaracterizacaoOrigem.ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoCaracterizacaoOrigem.getIdeCerhLancamentoCaracterizacaoOrigem()));
		
			return dao.buscarPorCriteria(detachedCriteria);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CerhLancamentoOutrosUsos cerhLancamentoOutrosUsos){
		try{
			dao.salvarOuAtualizar(cerhLancamentoOutrosUsos);
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletar(CerhLancamentoOutrosUsos cerhLancamentoOutrosUsos) {
		try{
			if(!Util.isNullOuVazio(cerhLancamentoOutrosUsos) && 
			   !Util.isNullOuVazio(cerhLancamentoOutrosUsos.getIdeCerhLancamentoCaracterizacaoOrigem())){
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("ideCerhLancamentoCaracterizacaoOrigem", cerhLancamentoOutrosUsos.getIdeCerhLancamentoCaracterizacaoOrigem());
				
				dao.executarQuery(""
					+ "DELETE FROM CerhLancamentoOutrosUsos c WHERE ideCerhLancamentoCaracterizacaoOrigem = :ideCerhLancamentoCaracterizacaoOrigem "
				, params);
			}
			
		}catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	

	
	
	
}



