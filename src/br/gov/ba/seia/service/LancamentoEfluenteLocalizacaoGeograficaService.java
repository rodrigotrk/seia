package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.LancamentoEfluenteLocalizacaoGeografica;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LancamentoEfluenteLocalizacaoGeograficaService {

	@Inject
	private IDAO<LancamentoEfluenteLocalizacaoGeografica> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(LancamentoEfluenteLocalizacaoGeografica lancamentoEfluenteLocalizacaoGeografica) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();	
			map.put("ideLancamentoEfluenteLocalizacaoGeografica", lancamentoEfluenteLocalizacaoGeografica.getIdeLancamentoEfluenteLocalizacaoGeografica());
			idao.executarNamedQuery("LancamentoEfluenteLocalizacaoGeografica.deleteByIde", map);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(LancamentoEfluenteLocalizacaoGeografica lancamentoEfluenteLocalizacaoGeografica){
		try {
			idao.salvarOuAtualizar(lancamentoEfluenteLocalizacaoGeografica);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}	
}