package br.gov.ba.seia.controller;

import javax.ejb.EJB;

import br.gov.ba.seia.controller.abstracts.TemEndereco;
import br.gov.ba.seia.entity.EnderecoPessoa;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes 
 * @since 10/02/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8338">#8338</a>
 *
 */
public abstract class PessoaEndereco extends TemEndereco{

	@EJB
	private EnderecoFacade enderecoFacade;
	
	protected Pessoa pessoa;
	protected EnderecoPessoa enderecoPessoa;
	private boolean pessoaJaExiste;
	
	public void montarEnderecoPessoa(){
		if(Util.isNullOuVazio(enderecoPessoa)){
			enderecoPessoa = new EnderecoPessoa(pessoa, super.enderecoGenericoController.getEndereco());
		}
	}
	
	@Override
	public void prepararEnderecoGenericoController(){
		try {
			enderecoPessoa = enderecoFacade.filtrarEnderecoByPessoa(pessoa);
			if(!Util.isNullOuVazio(enderecoPessoa)){
				super.carregarEndereco(enderecoPessoa.getIdeEndereco());
			} 
			else {
				super.init();
			}
			enviarId();
			desabilitarParaVisualizacao();
			
		} 
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	public void salvarEnderecoPessoaEndereco() throws Exception {
		montarEnderecoPessoa();
		enderecoFacade.salvarEnderecoPessoa(enderecoPessoa);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public boolean isPessoaJaExiste() {
		return pessoaJaExiste = (!Util.isNullOuVazio(pessoa));
	}
	
	public void setPessoaJaExiste(boolean pessoaJaExiste) {
		this.pessoaJaExiste = pessoaJaExiste;
	}
}
