package br.gov.ba.seia.dao;


import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.AreaProdutiva;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividade;

public class AreaProdutivaDAOImpl {

	@Inject
	IDAO<AreaProdutiva> dao;

	@Inject
	IDAO<AreaProdutivaTipologiaAtividade> daoAPTA;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AreaProdutiva filtrarById(AreaProdutiva pAreaProdutiva) {
		return dao.buscarEntidadePorExemplo(pAreaProdutiva);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(AreaProdutiva pAreaProdutiva) {
		dao.salvar(pAreaProdutiva);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(AreaProdutiva pAreaProdutiva) {
		dao.salvarOuAtualizar(pAreaProdutiva);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(AreaProdutiva pAreaProdutiva)  {
		dao.atualizar(pAreaProdutiva);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(AreaProdutiva pAreaProdutiva) {
		String deleteSQL = "DELETE FROM area_produtiva_tipologia_atividade WHERE ide_area_produtiva = :ideAreaProdutiva";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideAreaProdutiva", pAreaProdutiva.getIdeAreaProdutiva());
		daoAPTA.executarNativeQuery(deleteSQL, params);
		
		deleteSQL = "DELETE FROM area_produtiva WHERE ide_area_produtiva = :ideAreaProdutiva";
		dao.executarNativeQuery(deleteSQL, params);
	}
}
