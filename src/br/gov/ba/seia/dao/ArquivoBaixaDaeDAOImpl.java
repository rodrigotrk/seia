package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.ArquivoBaixaDae;

public class ArquivoBaixaDaeDAOImpl {

	@Inject
	IDAO<ArquivoBaixaDae> arquivoBaixaDaeDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcessodeBaixa(ArquivoBaixaDae cerhArquivoBaixa) {
		arquivoBaixaDaeDAO.salvar(cerhArquivoBaixa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarProcessoBaixa(ArquivoBaixaDae cerhArquivoBaixa){
		arquivoBaixaDaeDAO.atualizar(cerhArquivoBaixa);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ArquivoBaixaDae> carregarArquivosBaixaDae(Integer codigoReceita) {
		DetachedCriteria criteria = DetachedCriteria.forClass(ArquivoBaixaDae.class)
				.createAlias("sefazCodigoReceita", "sefaz");
				criteria.add(Restrictions.eq("sefaz.numCodigoCeceita", codigoReceita));
		return arquivoBaixaDaeDAO.listarPorCriteria(criteria, Order.desc("ideArquivoBaixaDae"));
	}
}
