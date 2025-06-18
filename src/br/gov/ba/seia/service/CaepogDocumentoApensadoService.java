package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CaepogDocumentoApensadoDAO;
import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.CaepogDocumento;
import br.gov.ba.seia.entity.CaepogDocumentoApensado;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaepogDocumentoApensadoService {
	
	@Inject
	CaepogDocumentoApensadoDAO caepogDocumentoApensadoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CaepogDocumentoApensado buscarCaepogDocumentoApensadoByTipoByCaepog(Caepog caepog, CaepogDocumento caepogDocumento) {
		return caepogDocumentoApensadoDAO.buscarCaepogDocumentoApensadoByTipoByCaepog(caepog, caepogDocumento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(CaepogDocumentoApensado caepogDocumentoApensado) {
		caepogDocumentoApensadoDAO.salvarOuAtualizar(caepogDocumentoApensado);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorCaepog(Caepog c)  {
		caepogDocumentoApensadoDAO.excluirPorCaepog(c);
	}
}