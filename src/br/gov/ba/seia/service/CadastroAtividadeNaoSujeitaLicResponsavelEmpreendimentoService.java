package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoDAOImpl;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoService {
	
	@Inject 
	private CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimentoDAOImpl dao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento responsavelEmpreendimento) {
		dao.salvar(responsavelEmpreendimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> listarPorCadastroAtividade(Integer ideCadastroAtividadeNaoSujeitaLic) {
		return dao.listarPorCadastroAtividade(ideCadastroAtividadeNaoSujeitaLic);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento objeto) {
		dao.remover(objeto);
	}

}
