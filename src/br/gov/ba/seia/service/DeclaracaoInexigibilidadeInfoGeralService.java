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
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoGeral;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoInexigibilidadeInfoGeralService {

	@Inject
	private IDAO<DeclaracaoInexigibilidade> declaracaoInexigibilidadeDAO;
	
	@Inject
	private IDAO<DeclaracaoInexigibilidadeInfoGeral> declaracaoInfoGeralDAO;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	public List<DeclaracaoInexigibilidadeInfoGeral> obterListaEnderecoRealizacaoAtividade(DeclaracaoInexigibilidade declaracaoInexigibilidade) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoGeral.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.createAlias("tipoRioIntervencao","tipoRioIntervencao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipoLocalAtividadeInexigivel","tipoLocalAtividadeInexigivel", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		List<DeclaracaoInexigibilidadeInfoGeral> lista = declaracaoInfoGeralDAO.listarPorCriteria(detachedCriteria, Order.asc("ideDeclaracaoInexigibilidadeInfoGeral"));
		
		for (DeclaracaoInexigibilidadeInfoGeral entidade : lista) {
			Endereco endereco = this.enderecoFacade.carregar(entidade.getEndereco().getIdeEndereco());
			
			if(!Util.isNull(endereco)) {
				entidade.setEndereco(endereco);
			}
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirEnderecoRealizacaoAtividade(DeclaracaoInexigibilidadeInfoGeral declaracaoInexigibilidadeInfoGeral) {
		declaracaoInfoGeralDAO.remover(declaracaoInexigibilidadeInfoGeral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDeclaracaoInfoGeral(DeclaracaoInexigibilidadeInfoGeral declaracaoInexigibilidadeInfoGeral)  {
		this.declaracaoInfoGeralDAO.remover(declaracaoInexigibilidadeInfoGeral);

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidadeInfoGeral obterDeclaracaoInfoGeralPor(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoGeral.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.createAlias("tipoLocalAtividadeInexigivel", "tipoLocalAtividadeInexigivel", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tipoRioIntervencao", "tipoRioIntervencao", JoinType.LEFT_OUTER_JOIN)
				.createAlias("endereco", "endereco", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		return this.declaracaoInfoGeralDAO.buscarPorCriteria(detachedCriteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(DeclaracaoInexigibilidadeInfoGeral declaracaoInexigibilidadeInfoGeral) {
		this.declaracaoInfoGeralDAO.salvarOuAtualizar(declaracaoInexigibilidadeInfoGeral);
	}

}
