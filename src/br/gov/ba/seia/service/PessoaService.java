package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PessoaDAOImpl;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.Caepog;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PessoaService {
	
	@Inject
	private PessoaDAOImpl pessoaDAOImpl;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa buscar(Pessoa pessoa)  {
		return pessoaDAOImpl.buscar(pessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPessoa(Pessoa pPessoa)  {
		pessoaDAOImpl.salvarPessoa(pPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarPessoa(Pessoa pPessoa) throws Exception {
		pessoaDAOImpl.salvarAtualizarPessoa(pPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarPessoa(Pessoa pPessoa) throws Exception {
		pessoaDAOImpl.atualizarPessoa(pPessoa);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPessoa(Pessoa pPessoa) {
		pessoaDAOImpl.excluirPessoa(pPessoa);
	}

	
	public void excluirPessoaSemAdmContainerDaEntidadePessoa(Pessoa pPessoa) {
		pessoaDAOImpl.excluirPessoaSemAdmContainerDaEntidadePessoa(pPessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa carregarGet(Integer pIdePessoa)  {
		return pessoaDAOImpl.carregarGet(pIdePessoa);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Pessoa carregarLoad(Integer pIdePessoa)  {
		return pessoaDAOImpl.carregarLoad(pIdePessoa);
	}
	
	public Pessoa carregarDadosTipoPessoaRequerimento(Integer ideRequerimento, TipoPessoaRequerimento tpr, PessoaFisica pessoaFisica)  {
		if(tpr.getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.PROCURADOR.getId())){
			return carregarDadosProcurador(ideRequerimento, pessoaFisica);
		} 
		else if(tpr.getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId())){
			return carregarDadosRepresentanteLegal(ideRequerimento, pessoaFisica);
		} 
		else {
			return carregarDadosRequerente(ideRequerimento, pessoaFisica);
		}
	}
	
	private Pessoa carregarDadosProcurador(Integer ideRequerimento, PessoaFisica pessoaFisica)  {
		return this.pessoaDAOImpl.carregarDadosTipoRequerentePessoa(ideRequerimento, TipoPessoaRequerimentoEnum.PROCURADOR, pessoaFisica);
	}
	
	private Pessoa carregarDadosRepresentanteLegal(Integer ideRequerimento, PessoaFisica pessoaFisica)  {
		return this.pessoaDAOImpl.carregarDadosTipoRequerentePessoa(ideRequerimento, TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL, pessoaFisica);
	}
	
	public Pessoa carregarDadosRequerente(Integer ideRequerimento, PessoaFisica pessoaFisica)  {
		return this.pessoaDAOImpl.carregarDadosRequerente(ideRequerimento, pessoaFisica);
	}
	
	public Pessoa carregarUsuarioSEIA() {
		return this.pessoaDAOImpl.carregarUsuarioSEIA();
	}

	public Pessoa buscarPessoaByDocumento(String documento)  {
		return this.pessoaDAOImpl.buscarPessoaByDocumento(documento);
	}
	
	public List<Pessoa> listarPessoasPorDemanda (String nome, String cpf, int startPage, int maxPage) {
		return this.pessoaDAOImpl.listarPessoasPorDemanda(nome, cpf, startPage, maxPage);
	}
	
	public List<Pessoa> listarPessoasJuridicaPorDemanda (String razao, String cnpj, int startPage, int maxPage) {
		return this.pessoaDAOImpl.listarPessoasJuridicaPorDemanda(razao, cnpj, startPage, maxPage);
	}
	
	public Integer getRowsCountPessoaFisica(String nome, String cpf) {
		return this.pessoaDAOImpl.getRowsCountPessoaFisica(nome, cpf);
	}
	
	public Integer getRowsCountPessoaJuridica(String razao, String cnpj) {
		return this.pessoaDAOImpl.getRowsCountPessoaJuridica(razao, cnpj);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pessoa> listarResponsaveisByCaepog(Caepog caepog) {
		return pessoaDAOImpl.listarResponsaveisByCaepog(caepog);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Pessoa> listarResponsaveisBy(CadastroAtividadeNaoSujeitaLic cadastro) {
		return pessoaDAOImpl.listarResponsaveisBy(cadastro);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa buscaProprietarioPorImovelRural(Integer ideImovelRural)  {
		return this.pessoaDAOImpl.buscaProprietarioPorImovelRural(ideImovelRural);
	}

	
}