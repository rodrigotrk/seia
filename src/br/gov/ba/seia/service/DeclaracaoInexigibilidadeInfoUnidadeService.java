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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoUnidade;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoInexigibilidadeInfoUnidadeService {

	@Inject
	private IDAO<DeclaracaoInexigibilidadeInfoUnidade> declaracaoInfoUnidadeDAO;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	public List<DeclaracaoInexigibilidadeInfoUnidade> obterListaEnderecoUnidade(DeclaracaoInexigibilidade declaracaoInexigibilidade) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoUnidade.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.createAlias("endereco", "endereco", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		List<DeclaracaoInexigibilidadeInfoUnidade> lista = declaracaoInfoUnidadeDAO.listarPorCriteria(detachedCriteria, Order.asc("ideDeclaracaoInexigibilidadeInfoUnidade"));
		
		for (DeclaracaoInexigibilidadeInfoUnidade entidade : lista) {
			if(entidade.getEndereco() != null) {
				Endereco endereco = this.enderecoFacade.carregar(entidade.getEndereco().getIdeEndereco());
				
				if(!Util.isNull(endereco)) {
					entidade.setEndereco(endereco);
				}
			}
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirEnderecoUnidade(DeclaracaoInexigibilidadeInfoUnidade declaracaoInexigibilidadeInfoUnidade) {
		declaracaoInfoUnidadeDAO.remover(declaracaoInexigibilidadeInfoUnidade);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoUnidade(DeclaracaoInexigibilidadeInfoUnidade declaracaoInexigibilidadeInfoUnidade) {
		declaracaoInfoUnidadeDAO.salvarOuAtualizar(declaracaoInexigibilidadeInfoUnidade);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDeclaracaoInfoUnidade(DeclaracaoInexigibilidade declaracaoInexigibilidade) throws Exception {
		List<DeclaracaoInexigibilidadeInfoUnidade> listaExclusao = obterListaEnderecoUnidade(declaracaoInexigibilidade);
		
		if(!Util.isNullOuVazio(listaExclusao)) {
			for (DeclaracaoInexigibilidadeInfoUnidade unidade : listaExclusao) {
				this.declaracaoInfoUnidadeDAO.remover(unidade);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidadeInfoUnidade obterDeclaracaoInfoUnidadePor(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoUnidade.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		return this.declaracaoInfoUnidadeDAO.buscarPorCriteria(detachedCriteria);
	}
}
