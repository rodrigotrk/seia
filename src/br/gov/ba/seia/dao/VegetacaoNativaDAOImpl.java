package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.VegetacaoNativa;
import br.gov.ba.seia.entity.VegetacaoNativaFinalidade;

public class VegetacaoNativaDAOImpl {
	
	@Inject
	IDAO<VegetacaoNativa> dao;
	@Inject
	IDAO<VegetacaoNativaFinalidade> daoVegetacaoNativaFinalidade;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VegetacaoNativa filtrarById(VegetacaoNativa pVegetacaoNativa) {
		return dao.buscarEntidadePorExemplo(pVegetacaoNativa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(VegetacaoNativa pVegetacaoNativa)  {
		dao.salvar(pVegetacaoNativa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(VegetacaoNativa pVegetacaoNativa)  {
		dao.atualizar(pVegetacaoNativa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(VegetacaoNativa pVegetacaoNativa)  {
		removerAllVegetacaoNativaFinalidade(pVegetacaoNativa);
		String deleteSQL = "delete from vegetacao_nativa where ide_vegetacao_nativa = :ideVegetacaoNativa";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideVegetacaoNativa", pVegetacaoNativa.getIdeVegetacaoNativa());
		dao.executarNativeQuery(deleteSQL, params);	
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAllVegetacaoNativaFinalidade(VegetacaoNativa pVegetacaoNativa)  {
		removerAllVegetacaoNativaFinalidade(pVegetacaoNativa);
		for (VegetacaoNativaFinalidade vegetacaoNativaFinalidade : pVegetacaoNativa.getVegetacaoNativaFinalidadeCollection()) {
			vegetacaoNativaFinalidade.setIdeVegetacaoNativa(pVegetacaoNativa);
			daoVegetacaoNativaFinalidade.salvar(vegetacaoNativaFinalidade);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerAllVegetacaoNativaFinalidade(VegetacaoNativa pVegetacaoNativa)  {
		String deleteSQL = "delete from vegetacao_nativa_finalidade where ide_vegetacao_nativa = :ideVegetacaoNativa";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideVegetacaoNativa", pVegetacaoNativa.getIdeVegetacaoNativa());
		dao.executarNativeQuery(deleteSQL, params);	
	}
	
}
