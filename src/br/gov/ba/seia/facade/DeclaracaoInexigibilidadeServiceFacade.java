package br.gov.ba.seia.facade;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.controller.DeclaracaoInexigibilidadeController;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoInexigibilidadeServiceFacade {
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSistema(DeclaracaoInexigibilidadeController declaracaoInexigibilidadeController) throws Exception {
		declaracaoInexigibilidadeController.prepararParaSalvarSistema();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarPonte(DeclaracaoInexigibilidadeController declaracaoInexigibilidadeController) throws Exception {
		declaracaoInexigibilidadeController.prepararParaSalvarPonte();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarEnderecoProjeto(DeclaracaoInexigibilidadeController declaracaoInexigibilidadeController) throws Exception {
		declaracaoInexigibilidadeController.prepararParaSalvarEnderecoProjeto();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtividadeEndereco(DeclaracaoInexigibilidadeController declaracaoInexigibilidadeController) throws Exception {
		declaracaoInexigibilidadeController.prepararParaSalvarEnderecoRealizacaoAtividade();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarUnidadeEndereco(DeclaracaoInexigibilidadeController declaracaoInexigibilidadeController) throws Exception {
		declaracaoInexigibilidadeController.prepararParaSalvarEnderecoUnidade();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizar(DeclaracaoInexigibilidadeController declaracaoInexigibilidadeController) throws Exception {
		declaracaoInexigibilidadeController.finalizar();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarDeclaracaoInexigibilidade(DeclaracaoInexigibilidadeController declaracaoInexigibilidadeController) throws Exception {
		declaracaoInexigibilidadeController.prepararParaSalvarDeclaracaoInexigibilidade();
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void refazerDeclaracao(DeclaracaoInexigibilidadeController declaracaoInexigibilidadeController) throws Exception {
		declaracaoInexigibilidadeController.prepararParaRefazerDeclaracao();
	}
	
}
