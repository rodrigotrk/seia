package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.AtividadeNaoSujeitaLicenciamentoDocumentoDAOImpl;
import br.gov.ba.seia.entity.AtividadeNaoSujeitaLicenciamentoDocumento;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;

/**
 * @author eduardo.fernandes 
 * @since 24/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AtividadeNaoSujeitaLicenciamentoDocumentoService {

	@Inject
	private AtividadeNaoSujeitaLicenciamentoDocumentoDAOImpl dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<AtividadeNaoSujeitaLicenciamentoDocumento> listarAtividadesByTipoAtividade(TipoAtividadeNaoSujeitaLicenciamentoEnum atividadeNaoSujeitaLicenciamentoEnum) {
		return dao.listarByAtividade(atividadeNaoSujeitaLicenciamentoEnum);
	}
}
