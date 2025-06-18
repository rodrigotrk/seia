package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhDadosFinalidadeDAO {

	@Inject
	private IDAO<CerhDadosFinalidadeInterface> dao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CerhDadosFinalidadeInterface dadoFinalidade) {
		StringBuilder hql = new StringBuilder();
		
		String entity = dadoFinalidade.getClass().getName().replace("br.gov.ba.seia.entity.", "");
		
		hql.append("DELETE FROM "+entity+" c WHERE c.ide"+entity+" = :ide"+entity);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide"+entity, dadoFinalidade.getIde());
		
		dao.executarQuery(hql.toString(), params);
	}
}
