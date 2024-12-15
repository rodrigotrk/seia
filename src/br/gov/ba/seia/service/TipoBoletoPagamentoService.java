package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoBoletoPagamento;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Classe de negocio do tipo boleto pagamento
 * 
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 10/12/2013
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoBoletoPagamentoService {

	@Inject
	IDAO<TipoBoletoPagamento> daoTipoBoletoPagamento;

	/**
	 * Consulta que retorna todos os tipos de boletos ativos
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @return Lista de tipos de boleto
	 */
	public List<TipoBoletoPagamento> consultarTipoBoleto() {
		
		Exception erro = null;
		try {
			return daoTipoBoletoPagamento.buscarPorNamedQuery("TipoBoletoPagamento.findAtivos");
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
}