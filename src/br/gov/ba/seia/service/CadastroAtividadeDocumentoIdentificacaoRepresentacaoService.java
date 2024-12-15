/**
 * 
 */
package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CadastroAtividadeDocumentoIdentificacaoRepresentacaoDAOImpl;
import br.gov.ba.seia.entity.CadastroAtividadeDocumentoIdentificacaoRepresentacao;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.util.Util;

/**
 * Serviço da classe {@link CadastroAtividadeDocumentoIdentificacaoRepresentacao}.
 * 
 * @author eduardo.fernandes
 * @since 19/12/2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeDocumentoIdentificacaoRepresentacaoService {

	@Inject
	private CadastroAtividadeDocumentoIdentificacaoRepresentacaoDAOImpl dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listar(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return dao.listar(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listarDocumentoIdentificacao(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return dao.listar(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> listarDocumentoRepresentacao(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return dao.listar(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeDocumentoIdentificacaoRepresentacao doc, List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> lista)  {
		if (!Util.isNull(doc)) {
			dao.salvar(doc);
		}
		else if (!Util.isNullOuVazio(lista)) {
			dao.salvar(lista);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Object object)  {
		dao.excluir(object);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirDocumentoIdentificacaoRepresentacaoComDocumentoIdentificacaoExcluido(CadastroAtividadeNaoSujeitaLic cadastro)  {
		dao.excluirDocumentoIdentificacaoExcluido(cadastro);
	}
}
