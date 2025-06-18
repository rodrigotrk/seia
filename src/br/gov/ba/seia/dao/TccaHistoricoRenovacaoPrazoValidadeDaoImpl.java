package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.TccaHistoricoRenovacaoPrazoValidade;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TccaHistoricoRenovacaoPrazoValidadeDaoImpl {
	
	@Inject
	private IDAO<TccaHistoricoRenovacaoPrazoValidade> tccaHistoricoRenovacaoPrazoValidadeDao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TccaHistoricoRenovacaoPrazoValidade tda) {
		try {
			tccaHistoricoRenovacaoPrazoValidadeDao.salvarOuAtualizar(tda);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
