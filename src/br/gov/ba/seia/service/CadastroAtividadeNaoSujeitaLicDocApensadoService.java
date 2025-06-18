package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CadastroAtividadeNaoSujeitaLicDocApensadoDAOImpl;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicDocApensado;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @since 07/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitaLicDocApensadoService {
	
	@Inject
	private CadastroAtividadeNaoSujeitaLicDocApensadoDAOImpl dao;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicDocApensado> listar(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return dao.listar(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeNaoSujeitaLicDocApensado doc, List<CadastroAtividadeNaoSujeitaLicDocApensado> lista)  {
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

}
