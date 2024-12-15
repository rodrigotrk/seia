package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipoSubestacaoDAOImpl;
import br.gov.ba.seia.entity.TipoSubestacao;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoSubestacaoService {

	@Inject
	private TipoSubestacaoDAOImpl tipoSubestacaoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoSubestacao> buscarTipoSubestacao() throws Exception{
		
		return tipoSubestacaoDAO.buscarTipoSubestacao();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoSubestacao buscarPorId(TipoSubestacao tipoSubestacao) throws Exception{
		
		return tipoSubestacaoDAO.buscarPorId(tipoSubestacao);
	}
}
