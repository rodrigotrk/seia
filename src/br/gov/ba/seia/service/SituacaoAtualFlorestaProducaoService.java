package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.SituacaoAtualFlorestaProducaoDAOImpl;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelPlantio;
import br.gov.ba.seia.entity.SituacaoAtualFlorestaProducao;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SituacaoAtualFlorestaProducaoService {

	@Inject
	private SituacaoAtualFlorestaProducaoDAOImpl situacaoAtualFlorestaProducaoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SituacaoAtualFlorestaProducao> listarSituacaoAtualFlorestaProducao(){
		try {
			return situacaoAtualFlorestaProducaoDAOImpl.listarSituacaoAtualFlorestaProducao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SituacaoAtualFlorestaProducao buscarSituacaoAtualFlorestaProducao(RegistroFlorestaProducaoImovelPlantio registroFlorestaProducaoImovelPlantio) {
		try {
			return situacaoAtualFlorestaProducaoDAOImpl.buscarSituacaoAtualFlorestaProducao(registroFlorestaProducaoImovelPlantio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		    throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
