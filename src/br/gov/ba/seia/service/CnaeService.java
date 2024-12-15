package br.gov.ba.seia.service;

import java.util.Collection;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CnaeDAOImpl;
import br.gov.ba.seia.dao.PessoaJuridicaCnaeDAOImpl;
import br.gov.ba.seia.entity.Cnae;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Tipologia;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CnaeService {

	@Inject
	CnaeDAOImpl cnaeDAOImpl;

	@Inject
	PessoaJuridicaCnaeDAOImpl pessoaJuridicaCnaeDAOImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Cnae> listarCnaeSecao()  {
		return cnaeDAOImpl.listarCnaeSecao();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Cnae> listarCnaePorPai(Cnae cnaeSecao)  {
		return cnaeDAOImpl.listarCnaePorPai(cnaeSecao);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cnae obterCnaePaiPorCnae(Cnae cnae)  {
		return cnaeDAOImpl.obterCnaePaiPorCnae(cnae);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCnae(Cnae cnae)  {
		cnaeDAOImpl.salvarCnae(cnae);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarCnae(Cnae cnae)  {
		cnaeDAOImpl.atualizarCnae(cnae);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Cnae carregarCnae(Integer pIdeCnae)  {
		return cnaeDAOImpl.carregarCnae(pIdeCnae);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Cnae> listarCnaePorPessoaJuridica(PessoaJuridica pessoaJuridica)  {
		return pessoaJuridicaCnaeDAOImpl.listarCnaePorPessoaJuridica(pessoaJuridica);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipologiaCnae(Cnae cnae, Tipologia tipologia)  {
		cnaeDAOImpl.excluirTipologiaCnae(cnae, tipologia);

	}

	public Cnae carregarCnae(String codCnae)  {
		return cnaeDAOImpl.carregarCnae(codCnae);
	}

}