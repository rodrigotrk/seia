package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CadastroAtividadeNaoSujeitaLicComunicacaoDAOImpl;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicComunicacao;

/**
 * Serviço da classe {@link CadastroAtividadeNaoSujeitaLicComunicacao}
 * 
 * @author eduardo.fernandes
 * @since 14/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8193">#8193</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitaLicComunicacaoService {

	@Inject
	private CadastroAtividadeNaoSujeitaLicComunicacaoDAOImpl dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicComunicacao> listar(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return dao.listar(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeNaoSujeitaLicComunicacao comunicacao)  {
		dao.salvar(comunicacao);
	}
}
