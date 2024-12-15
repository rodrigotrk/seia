package br.gov.ba.seia.dao;


import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.DocumentoImovelRuralPosse;

public class DocumentoImovelRuralPosseDAOImpl {

	@Inject
	IDAO<DocumentoImovelRuralPosse> dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DocumentoImovelRuralPosse filtrarById(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse) {
		return dao.buscarEntidadePorExemplo(pDocumentoImovelRuralPosse);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse)  {
		dao.salvar(pDocumentoImovelRuralPosse);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse)  {
		dao.atualizar(pDocumentoImovelRuralPosse);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(DocumentoImovelRuralPosse pDocumentoImovelRuralPosse)  {
		String deleteSQL = "delete from documento_imovel_rural_posse where ide_documento_imovel_rural_posse = :ideDocumentoImovelRuralPosse";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideDocumentoImovelRuralPosse", pDocumentoImovelRuralPosse.getIdeDocumentoImovelRuralPosse());
		dao.executarNativeQuery(deleteSQL, params);	
	}

}
