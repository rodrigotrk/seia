package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CaepogDocumentoDAO;
import br.gov.ba.seia.entity.CaepogDocumento;
import br.gov.ba.seia.enumerator.CaepogCategoriaDocumentoEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaepogDocumentoService {
	
	@Inject
	CaepogDocumentoDAO caepogDocumentoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CaepogDocumento> listarDocumentoByCateoria(CaepogCategoriaDocumentoEnum categoriaDocumento)  {
		return caepogDocumentoDAO.listarDocumentoByCateoria(categoriaDocumento);
	}

}