package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TipoAtividadeNaoSujeitaLicenciamentoDAOImpl;
import br.gov.ba.seia.entity.TipoAtividadeNaoSujeitaLicenciamento;

/**
 * @author eduardo.fernandes
 * @since 07/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TipoAtividadeNaoSujeitaLicenciamentoService {

	@Inject
	private TipoAtividadeNaoSujeitaLicenciamentoDAOImpl dao;
	
	/**
	 * 
	 * Método que vai retornar uma lista de {@link TipoAtividadeNaoSujeitaLicenciamento}. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 07/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8187">#8187</a>
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoAtividadeNaoSujeitaLicenciamento> listarTipoAtividade() throws Exception{
		return dao.listarTipoAtividadeNaoSujeitaLicenciamentoAtivos();
	}
}
