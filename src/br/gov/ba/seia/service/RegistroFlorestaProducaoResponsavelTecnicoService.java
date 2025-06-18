package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.RegistroFlorestaProducaoResponsavelTecnicoDAOImpl;
import br.gov.ba.seia.entity.RegistroFlorestaProducao;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoResponsavelTecnico;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegistroFlorestaProducaoResponsavelTecnicoService {

	@Inject
	private RegistroFlorestaProducaoResponsavelTecnicoDAOImpl registroFlorestaProducaoResponsavelTecnicoDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirRegistroFlorestaProducaoResponsavelTecnico(RegistroFlorestaProducaoResponsavelTecnico registroFlorestaProducaoResponsavelTecnico)  {
	
		try {
			registroFlorestaProducaoResponsavelTecnicoDAOImpl.excluirRegistroFlorestaProducaoResponsavelTecnico(registroFlorestaProducaoResponsavelTecnico);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<RegistroFlorestaProducaoResponsavelTecnico> registroFlorestaProducaoResponsavelTecnicoList)  {
		
		try {
			registroFlorestaProducaoResponsavelTecnicoDAOImpl.salvar(registroFlorestaProducaoResponsavelTecnicoList);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<RegistroFlorestaProducaoResponsavelTecnico> listarRegistroFlorestaProducaoResponsavelTecnico(RegistroFlorestaProducao ideRegistroFlorestaProducao)  {

		try {
			 return registroFlorestaProducaoResponsavelTecnicoDAOImpl.listarRegistroFlorestaProducaoResponsavelTecnico(ideRegistroFlorestaProducao);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

}
