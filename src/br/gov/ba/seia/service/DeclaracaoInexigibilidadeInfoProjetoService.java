package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoProjeto;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoInexigibilidadeInfoProjetoService {

	@Inject
	private IDAO<DeclaracaoInexigibilidadeInfoProjeto> declaracaoInfoProjetoDAO;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	public List<DeclaracaoInexigibilidadeInfoProjeto> obterListaEnderecoProjeto(DeclaracaoInexigibilidade declaracaoInexigibilidade) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoProjeto.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		List<DeclaracaoInexigibilidadeInfoProjeto> lista = declaracaoInfoProjetoDAO.listarPorCriteria(detachedCriteria, Order.asc("ideDeclaracaoInexigibilidadeInfoProjeto"));
		
		for (DeclaracaoInexigibilidadeInfoProjeto entidade : lista) {
			Endereco endereco = this.enderecoFacade.carregar(entidade.getEndereco().getIdeEndereco());
			
			if(!Util.isNull(endereco)) {
				entidade.setEndereco(endereco);
			}
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirEnderecoProjeto(DeclaracaoInexigibilidadeInfoProjeto declaracaoInexigibilidadeInfoProjeto) {
		declaracaoInfoProjetoDAO.remover(declaracaoInexigibilidadeInfoProjeto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoProjeto(DeclaracaoInexigibilidadeInfoProjeto declaracaoInexigibilidadeInfoProjeto) {
		declaracaoInfoProjetoDAO.salvarOuAtualizar(declaracaoInexigibilidadeInfoProjeto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDeclaracaoInfoProjeto(DeclaracaoInexigibilidade declaracaoInexigibilidade) throws Exception {
		List<DeclaracaoInexigibilidadeInfoProjeto> listaExclusao = obterListaEnderecoProjeto(declaracaoInexigibilidade);
		
		if(!Util.isNullOuVazio(listaExclusao)) {
			for (DeclaracaoInexigibilidadeInfoProjeto projeto : listaExclusao) {
				this.declaracaoInfoProjetoDAO.remover(projeto);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidadeInfoProjeto obterDeclaracaoInfoProjetoPor(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoProjeto.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		return this.declaracaoInfoProjetoDAO.buscarPorCriteria(detachedCriteria);
	}
}
