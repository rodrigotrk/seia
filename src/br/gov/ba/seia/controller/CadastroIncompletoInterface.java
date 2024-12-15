package br.gov.ba.seia.controller;

import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;


/**
 * 
 * Interface criada para lançar {@link SeiaValidacaoRuntimeException} quando houver algum tipo de pendência no Cadastro.
 * 
 * @author eduardo.fernandes 
 * @since 26/04/2017
 *
 */
public interface CadastroIncompletoInterface {
	
	public void verificarPendenciaNoCadastro() ;
	
}
