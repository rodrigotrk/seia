package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaracterizacaoDAOImpl {

	@Inject
	private IDAO<CerhCaracterizacaoInterface> dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CerhCaracterizacaoInterface caracterizacaoIF) {
		if(Util.isNull(caracterizacaoIF.getId())){
			dao.salvar(caracterizacaoIF);
		}
		else {
			caracterizacaoIF = (CerhCaracterizacaoInterface) dao.mergeComRetorno(caracterizacaoIF);
			
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(CerhCaracterizacaoInterface caracterizacaoIF) {
		if(Util.isNull(caracterizacaoIF.getId())){
			dao.salvar(caracterizacaoIF);
		}
		else {
			dao.atualizar(caracterizacaoIF);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CerhCaracterizacaoInterface caracterizacaoIF) {
		StringBuilder hql = new StringBuilder();

		String entity = caracterizacaoIF.getClass().getSimpleName();

		hql.append("DELETE FROM "+entity+" c WHERE c.ide"+entity+" = :ide"+entity);

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("ide"+entity, caracterizacaoIF.getId());

		dao.executarQuery(hql.toString(), params);
	}



}
