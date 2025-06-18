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
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoAbastecimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoInexigibilidadeInfoAbastecimentoService {

	@Inject
	private IDAO<DeclaracaoInexigibilidadeInfoAbastecimento> declaracaoInfoAbastecimentoDAO;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoService;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade facadeLocalizacaoGeografica;
	
	public List<DeclaracaoInexigibilidadeInfoAbastecimento> obterListaEnderecoSistema(DeclaracaoInexigibilidade declaracaoInexigibilidade) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoAbastecimento.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.createAlias("localizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		List<DeclaracaoInexigibilidadeInfoAbastecimento> lista = declaracaoInfoAbastecimentoDAO.listarPorCriteria(detachedCriteria, Order.asc("ideDeclaracaoInexigibilidadeInfoAbastecimento"));
		
		for (DeclaracaoInexigibilidadeInfoAbastecimento entidade : lista) {
			Endereco endereco = this.enderecoFacade.carregar(entidade.getEndereco().getIdeEndereco());
			
			if(!Util.isNull(endereco)) {
				entidade.setEndereco(endereco);
			}
			
			if(!Util.isNull(entidade.getLocalizacaoGeografica())) {
				LocalizacaoGeografica loc = localizacaoService.carregar(entidade.getLocalizacaoGeografica());
				
				if(!Util.isNullOuVazio(loc)) {
					facadeLocalizacaoGeografica.tratarPonto(loc);
					entidade.setLocalizacaoGeografica(loc);
				}
			}
		}
		
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirEnderecoAbastecimento(DeclaracaoInexigibilidadeInfoAbastecimento declaracaoInexigibilidadeInfoAbastecimento) {
		LocalizacaoGeografica ideLocalizacaoGeografica = declaracaoInexigibilidadeInfoAbastecimento.getLocalizacaoGeografica();
		
		if(ideLocalizacaoGeografica != null) {
			declaracaoInexigibilidadeInfoAbastecimento.setLocalizacaoGeografica(null);
			
			salvarDeclaracaoInexigibilidadeInfoAbastecimento(declaracaoInexigibilidadeInfoAbastecimento);
			
			localizacaoService.excluirByIdLocalizacaoGeografica(ideLocalizacaoGeografica);
		}
		
		this.declaracaoInfoAbastecimentoDAO.remover(declaracaoInexigibilidadeInfoAbastecimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoAbastecimento(DeclaracaoInexigibilidadeInfoAbastecimento declaracaoInexigibilidadeInfoAbastecimento) {
		declaracaoInfoAbastecimentoDAO.salvarOuAtualizar(declaracaoInexigibilidadeInfoAbastecimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerDeclaracaoInfoAbastecimento(DeclaracaoInexigibilidade declaracaoInexigibilidade) throws Exception {
		List<DeclaracaoInexigibilidadeInfoAbastecimento> listaExclusao = obterListaEnderecoSistema(declaracaoInexigibilidade);
		
		if(!Util.isNullOuVazio(listaExclusao)) {
			for (DeclaracaoInexigibilidadeInfoAbastecimento abastecimento : listaExclusao) {
				
				LocalizacaoGeografica locAux = abastecimento.getLocalizacaoGeografica();
				
				abastecimento.setLocalizacaoGeografica(null);
				
				salvarDeclaracaoInexigibilidadeInfoAbastecimento(abastecimento);
				
				localizacaoService.excluirByIdLocalizacaoGeografica(locAux);

				declaracaoInfoAbastecimentoDAO.remover(abastecimento);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidadeInfoAbastecimento obterDeclaracaoInfoAbastecimentoPor(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoAbastecimento.class)
				.createAlias("declaracaoInexigibilidade", "declaracaoInexigibilidade")
				.add(Restrictions.eq("declaracaoInexigibilidade.ideDeclaracaoInexigibilidade", declaracaoInexigibilidade.getIdeDeclaracaoInexigibilidade()));
		
		return this.declaracaoInfoAbastecimentoDAO.buscarPorCriteria(detachedCriteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDeclaracaoInexigibilidadeInfoAbastecimento(DeclaracaoInexigibilidadeInfoAbastecimento abastecimento) {
		this.declaracaoInfoAbastecimentoDAO.salvarOuAtualizar(abastecimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoInexigibilidadeInfoAbastecimento carregarGet(DeclaracaoInexigibilidadeInfoAbastecimento declaracaoInexigibilidadeInfoAbastecimento) throws Exception {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DeclaracaoInexigibilidadeInfoAbastecimento.class)
				.createAlias("localizacaoGeografica", "localizacaoGeografica", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.idEq(declaracaoInexigibilidadeInfoAbastecimento.getIdeDeclaracaoInexigibilidadeInfoAbastecimento()));
		
		DeclaracaoInexigibilidadeInfoAbastecimento retorno = this.declaracaoInfoAbastecimentoDAO.buscarPorCriteria(detachedCriteria);
		
		Endereco endereco = this.enderecoFacade.carregar(retorno.getEndereco().getIdeEndereco());
		
		if(!Util.isNull(endereco)) {
			retorno.setEndereco(endereco);
		}
		
		if(!Util.isNull(retorno.getLocalizacaoGeografica())) {
			LocalizacaoGeografica loc = localizacaoService.carregar(retorno.getLocalizacaoGeografica());
			
			if(!Util.isNullOuVazio(loc)) {
				facadeLocalizacaoGeografica.tratarPonto(loc);
				retorno.setLocalizacaoGeografica(loc);
			}
		}
		
		return retorno; 
	}
}