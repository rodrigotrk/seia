package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoPonte;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoInexigibilidadeInfoPonteService {

	@Inject
	private IDAO<DeclaracaoInexigibilidadeInfoPonte> declaracaoInfoPonteDAO;
	
	public List<DeclaracaoInexigibilidadeInfoPonte> obterListaEnderecoPonte(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoPonte.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		return declaracaoInfoPonteDAO.listarPorCriteria(detachedCriteria, Order.asc("ideDeclaracaoInexigibilidadeInfoPonte"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDeclaracaoInfoPonte(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		List<DeclaracaoInexigibilidadeInfoPonte> listaExclusao = obterListaEnderecoPonte(declaracaoInexigibilidade);
		
		if(!Util.isNullOuVazio(listaExclusao)) {
			for (DeclaracaoInexigibilidadeInfoPonte ponte : listaExclusao) {
				declaracaoInfoPonteDAO.remover(ponte);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidadeInfoPonte obterDeclaracaoInfoPontePor(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoPonte.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		return this.declaracaoInfoPonteDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DeclaracaoInexigibilidadeInfoPonte declaracaoInexigibilidadeInfoPonte) {
		this.declaracaoInfoPonteDAO.salvarOuAtualizar(declaracaoInexigibilidadeInfoPonte);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidadeInfoPonte obterDeclaracaoInfoPontePorId(Integer ideDeclaracaoInfoPonte) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoPonte.class)
				.add(Restrictions.idEq(ideDeclaracaoInfoPonte))
				.setProjection(
		Projections.projectionList()
					.add(Projections.property("ideDeclaracaoInexigibilidadeInfoPonte"),"ideDeclaracaoInexigibilidadeInfoPonte")
					.add(Projections.property("nomPonte"),"nomPonte"))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(DeclaracaoInexigibilidadeInfoPonte.class));
		
		return this.declaracaoInfoPonteDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPonte(DeclaracaoInexigibilidadeInfoPonte declaracaoInexigibilidadeInfoPonte) {
		this.declaracaoInfoPonteDAO.remover(declaracaoInexigibilidadeInfoPonte);
	}
}
