package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralRppn;

public class ImovelRuralRppnDAOImpl {
	
	@Inject
	IDAO<ImovelRuralRppn> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ImovelRuralRppn filtrarById(ImovelRuralRppn imovelRuralRppn) {
		return dao.buscarEntidadePorExemplo(imovelRuralRppn);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ImovelRuralRppn imovelRuralRppn)  {
		dao.salvar(imovelRuralRppn);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(ImovelRuralRppn imovelRuralRppn)  {
		dao.atualizar(imovelRuralRppn);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(ImovelRuralRppn imovelRuralRppn)  {
		String deleteSQL = "delete from imovel_rural_rppn where ide_imovel_rural_rppn = :ideImovelRuralRppn";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideImovelRuralRppn", imovelRuralRppn.getIdeImovelRuralRppn());
		dao.executarNativeQuery(deleteSQL, params);	
	}

	public void excluirImovelRuralRppnPorImovelRural(ImovelRural ideImovelRural)  {
		String deleteSQL = "delete from imovel_rural_rppn where ide_imovel_rural = :ideImovelRural";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideImovelRural", ideImovelRural.getIdeImovelRural());
		dao.executarNativeQuery(deleteSQL, params);	
	}
	
}
