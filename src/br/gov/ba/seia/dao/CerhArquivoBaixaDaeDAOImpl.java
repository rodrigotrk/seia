package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

import br.gov.ba.seia.entity.ArquivoBaixaDae;

public class CerhArquivoBaixaDaeDAOImpl {

	@Inject
	IDAO<ArquivoBaixaDae> cerhArquivoBaixaDaeDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcessodeBaixa(ArquivoBaixaDae cerhArquivoBaixa) {
		cerhArquivoBaixaDaeDAO.salvar(cerhArquivoBaixa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarProcessoBaixa(ArquivoBaixaDae cerhArquivoBaixa) {
		cerhArquivoBaixaDaeDAO.atualizar(cerhArquivoBaixa);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ArquivoBaixaDae> carregarArquivosBaixaDae() {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArquivoBaixaDae.class);
		return cerhArquivoBaixaDaeDAO.listarPorCriteria(criteria, Order.desc("ideArquivoBaixaDae"));
	}
}
