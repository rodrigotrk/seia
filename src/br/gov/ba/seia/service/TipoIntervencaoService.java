package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.TipoIntervencao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoIntervencaoService {
	
	@Inject
	IDAO<TipoIntervencao> tipoIntervencaoDAO;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoIntervencao> obterTodos() throws Exception {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select t ");
		sql.append("from TipoIntervencao t ");
		sql.append("order by t.nomTipoIntervencao asc");
		
		return tipoIntervencaoDAO.listarPorQuery(sql.toString(), null);
	}

}
